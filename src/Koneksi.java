import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    // Konfigurasi tetap sesuai kode awal
    private static final String URL = "jdbc:mysql://localhost/toko_buku_uas_pbo";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method untuk mengambil koneksi
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Testing koneksi
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Koneksi ke MySQL berhasil!");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
    }
}
