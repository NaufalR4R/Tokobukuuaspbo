import java.sql.*;
import javax.swing.JOptionPane;

public class Koneksi {
    Connection koneksi;

    public static Connection Koneksi(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/toko_buku_uas_pbo","root","");

            return koneksi;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

            return null;
        }
    }
}
