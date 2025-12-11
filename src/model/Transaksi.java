package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.Koneksi;

public class Transaksi {

    private int id;
    private int userId;
    private double hargaTotal;
    private double jumlahBayar;
    private double jumlahKembalian;

    //  CONSTRUCTOR
    public Transaksi() {}

    public Transaksi(int id, int userId, double hargaTotal, double jumlahBayar, double jumlahKembalian) {
        this.id = id;
        this.userId = userId;
        this.hargaTotal = hargaTotal;
        this.jumlahBayar = jumlahBayar;
        this.jumlahKembalian = jumlahKembalian;
    }

    //  GETTER - SETTER
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public double getHargaTotal() { return hargaTotal; }
    public double getJumlahBayar() { return jumlahBayar; }
    public double getJumlahKembalian() { return jumlahKembalian; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setHargaTotal(double hargaTotal) { this.hargaTotal = hargaTotal; }
    public void setJumlahBayar(double jumlahBayar) { this.jumlahBayar = jumlahBayar; }
    public void setJumlahKembalian(double jumlahKembalian) { this.jumlahKembalian = jumlahKembalian; }

    //  CREATE (INSERT)
    public boolean create() {
        String query = "INSERT INTO transaksi (id_pelanggan, user_id, harga_total, jumlah_bayar, jumlah_kembalian) VALUES (NULL, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setDouble(2, hargaTotal);
            ps.setDouble(3, jumlahBayar);
            ps.setDouble(4, jumlahKembalian);

            int affected = ps.executeUpdate();

            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) this.id = rs.getInt(1);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error create transaksi: " + e.getMessage());
        }
        return false;
    }

    //  READ (GET ALL)
    public static List<Transaksi> getAll() {
        List<Transaksi> list = new ArrayList<>();
        String query = "SELECT * FROM transaksi ORDER BY id DESC";

        try (Connection conn = Koneksi.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Transaksi(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("harga_total"),
                        rs.getDouble("jumlah_bayar"),
                        rs.getDouble("jumlah_kembalian")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error getAll transaksi: " + e.getMessage());
        }
        return list;
    }

    //  UPDATE
    public boolean update() {
        String query = "UPDATE transaksi SET user_id=?, harga_total=?, jumlah_bayar=?, jumlah_kembalian=? WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setDouble(2, hargaTotal);
            ps.setDouble(3, jumlahBayar);
            ps.setDouble(4, jumlahKembalian);
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error update transaksi: " + e.getMessage());
        }

        return false;
    }

    //  DELETE
    public static boolean delete(int id) {
        String query = "DELETE FROM transaksi WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error delete transaksi: " + e.getMessage());
        }
        return false;
    }
}
