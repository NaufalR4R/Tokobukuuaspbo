package admin.kategori;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import utils.Koneksi;

public class Kategori {

    private int id;
    private String nama;
    private String deskripsi;

    // CONSTRUCTOR
    public Kategori() {}

    public Kategori(int id, String nama, String deskripsi) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
    }

    // GETTER & SETTER
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    // CREATE CATEGORY
    public boolean createKategori() {
        String query = "INSERT INTO kategori (nama, deskripsi) VALUES (?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nama);
            stmt.setString(2, deskripsi);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal menambah kategori: " + e.getMessage());
            return false;
        }
    }

    // UPDATE CATEGORY
    public boolean updateKategori() {
        String query = "UPDATE kategori SET nama = ?, deskripsi = ? WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nama);
            stmt.setString(2, deskripsi);
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal mengupdate kategori: " + e.getMessage());
            return false;
        }
    }

    // DELETE CATEGORY
    public static boolean deleteKategori(int idKategori) {
        String query = "DELETE FROM kategori WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idKategori);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal menghapus kategori: " + e.getMessage());
            return false;
        }
    }

    // GET ALL CATEGORIES
    public static List<Kategori> getAllKategori() {
        List<Kategori> list = new ArrayList<>();
        String query = "SELECT * FROM kategori ORDER BY id DESC";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Kategori k = new Kategori(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("deskripsi")
                );
                list.add(k);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal mengambil data kategori: " + e.getMessage());
        }

        return list;
    }

    // GET CATEGORY BY ID
    public static Kategori getKategoriById(int idKategori) {
        String query = "SELECT * FROM kategori WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idKategori);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Kategori(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("deskripsi")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Gagal mengambil kategori: " + e.getMessage());
        }

        return null;
    }
}
