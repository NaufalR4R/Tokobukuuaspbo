package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import utils.Koneksi;

public class User {

    private int id;
    private String username;
    private String namaLengkap;
    private String password;
    private String role;
    private Timestamp created_at;

    // CONSTRUCTORS
    public User() {}

    // Constructor untuk data lengkap (termasuk password)
    public User(int id, String username, String namaLengkap, String password, String role, Timestamp created_at) {
        this.id = id;
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
    }

    // Constructor untuk data tampilan (tanpa password)
    public User(int id, String username, String namaLengkap, String role, Timestamp created_at) {
        this.id = id;
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.role = role;
        this.created_at = created_at;
        this.password = null;
    }

    // GETTERS & SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp getCreatedAt() { return created_at; }
    public void setCreatedAt(Timestamp created_at) { this.created_at = created_at; }

    // CREATE USER
    public boolean createUser() {
        String query = "INSERT INTO users (username, password, nama_lengkap, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, namaLengkap);
            stmt.setString(4, role);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah user: " + e.getMessage());
            return false;
        }
    }

    // UPDATE USER
    public boolean updateUser() {
        String query = "UPDATE users SET username=?, password=?, nama_lengkap=?, role=? WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, namaLengkap);
            stmt.setString(4, role);
            stmt.setInt(5, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengupdate user: " + e.getMessage());
            return false;
        }
    }

    // DELETE USER
    public static boolean deleteUser(int idUser) {
        String query = "DELETE FROM users WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUser);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus user: " + e.getMessage());
            return false;
        }
    }

    // GET ALL USERS
    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        // Ambil created_at
        String query = "SELECT id, username, nama_lengkap, role, created_at FROM users ORDER BY id DESC";

        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nama_lengkap"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data user: " + e.getMessage());
        }

        return list;
    }

    // GET USER BY ID
    public static User getUserById(int userId) {
        // Ambil created_at
        String query = "SELECT id, username, nama_lengkap, password, role, created_at FROM users WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nama_lengkap"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at") // Ambil created_at
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data user: " + e.getMessage());
        }

        return null;
    }
}
