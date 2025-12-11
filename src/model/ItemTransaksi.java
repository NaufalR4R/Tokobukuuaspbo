package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.Koneksi;

public class ItemTransaksi {

    private int id;
    private int idTransaksi;
    private int idBuku;
    private int jumlah;
    private double hargaItem;
    private double subTotal;

    //  CONSTRUCTOR
    public ItemTransaksi() {}

    public ItemTransaksi(int id, int idTransaksi, int idBuku, int jumlah, double hargaItem) {
        this.id = id;
        this.idTransaksi = idTransaksi;
        this.idBuku = idBuku;
        this.jumlah = jumlah;
        this.hargaItem = hargaItem;
        this.subTotal = jumlah * hargaItem;
    }

    //  GETTER - SETTER
    public int getId() { return id; }
    public int getIdTransaksi() { return idTransaksi; }
    public int getIdBuku() { return idBuku; }
    public int getJumlah() { return jumlah; }
    public double getHargaItem() { return hargaItem; }
    public double getSubTotal() { return subTotal; }

    public void setId(int id) { this.id = id; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }
    public void setIdBuku(int idBuku) { this.idBuku = idBuku; }
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        this.subTotal = jumlah * hargaItem;
    }
    public void setHargaItem(double hargaItem) {
        this.hargaItem = hargaItem;
        this.subTotal = jumlah * hargaItem;
    }

    //  CREATE (INSERT)
    public boolean create() {
        String query = "INSERT INTO item_transaksi (id_transaksi, id_buku, jumlah, harga_item) VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idTransaksi);
            ps.setInt(2, idBuku);
            ps.setInt(3, jumlah);
            ps.setDouble(4, hargaItem);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) this.id = rs.getInt(1);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error create item transaksi: " + e.getMessage());
        }
        return false;
    }

    //  GET ALL BY TRANSAKSI
    public static List<ItemDetail> getDetailByTransaksi(int transaksiId) {
        List<ItemDetail> items = new ArrayList<>();

        // Query dengan JOIN ke tabel buku untuk mendapatkan judul
        String query = "SELECT it.jumlah, it.harga_item, b.judul " +
                "FROM item_transaksi it " +
                "JOIN buku b ON it.id_buku = b.id " +
                "WHERE it.id_transaksi = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, transaksiId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int jumlah = rs.getInt("jumlah");
                double hargaItem = rs.getDouble("harga_item");
                double subTotal = jumlah * hargaItem; // Hitung subtotal

                items.add(new ItemDetail(
                        rs.getString("judul"),
                        jumlah,
                        hargaItem,
                        subTotal
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error getDetailByTransaksi: " + e.getMessage());
        }
        return items;
    }

    //  UPDATE
    public boolean update() {
        String query = "UPDATE item_transaksi SET id_buku=?, jumlah=?, harga_item=? WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idBuku);
            ps.setInt(2, jumlah);
            ps.setDouble(3, hargaItem);
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error update item transaksi: " + e.getMessage());
        }
        return false;
    }

    //  DELETE
    public static boolean delete(int id) {
        String query = "DELETE FROM item_transaksi WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error delete item transaksi: " + e.getMessage());
        }
        return false;
    }
}
