package admin.buku;
import utils.Koneksi;
import java.sql.*;
import java.util.ArrayList;

public class Buku {

    private int id;
    private int idKategori;
    private String judul;
    private String penulis;
    private String penerbit;
    private double harga;
    private int stok;
    private String deskripsi;

    // CONSTRUCTOR
    public Buku(int id, int idKategori, String judul, String penulis,
                String penerbit, double harga, int stok, String deskripsi) {
        this.id = id;
        this.idKategori = idKategori;
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.harga = harga;
        this.stok = stok;
        this.deskripsi = deskripsi;
    }

    public Buku() {}

    // GETTER & SETTER
    public int getId() { return id; }
    public int getIdKategori() { return idKategori; }
    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public String getPenerbit() { return penerbit; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getDeskripsi() { return deskripsi; }

    public void setId(int id) { this.id = id; }
    public void setIdKategori(int idKategori) { this.idKategori = idKategori; }
    public void setJudul(String judul) { this.judul = judul; }
    public void setPenulis(String penulis) { this.penulis = penulis; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = stok; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    // CREATE — Tambah Data Buku
    public boolean create() {
        String query = "INSERT INTO buku (id_kategori, judul, penulis, penerbit, harga, stok, deskripsi) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idKategori);
            ps.setString(2, judul);
            ps.setString(3, penulis);
            ps.setString(4, penerbit);
            ps.setDouble(5, harga);
            ps.setInt(6, stok);
            ps.setString(7, deskripsi);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal menambah buku: " + e.getMessage());
            return false;
        }
    }

    // READ — Ambil Buku Berdasarkan ID
    public static Buku getById(int idBuku) {
        String query = "SELECT * FROM buku WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idBuku);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Buku(
                        rs.getInt("id"),
                        rs.getInt("id_kategori"),
                        rs.getString("judul"),
                        rs.getString("penulis"),
                        rs.getString("penerbit"),
                        rs.getDouble("harga"),
                        rs.getInt("stok"),
                        rs.getString("deskripsi")
                );
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil buku: " + e.getMessage());
        }

        return null;
    }

    // UPDATE — Edit Data Buku
    public boolean update() {
        String query = "UPDATE buku SET id_kategori=?, judul=?, penulis=?, penerbit=?, harga=?, stok=?, deskripsi=? "
                + "WHERE id=?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idKategori);
            ps.setString(2, judul);
            ps.setString(3, penulis);
            ps.setString(4, penerbit);
            ps.setDouble(5, harga);
            ps.setInt(6, stok);
            ps.setString(7, deskripsi);
            ps.setInt(8, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal mengupdate buku: " + e.getMessage());
            return false;
        }
    }

    // DELETE — Hapus Buku
    public static boolean delete(int idBuku) {
        String query = "DELETE FROM buku WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idBuku);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Gagal menghapus buku: " + e.getMessage());
            return false;
        }
    }

    // READ ALL — Ambil Semua Buku
    public static ArrayList<Buku> getAll() {
        ArrayList<Buku> list = new ArrayList<>();
        String query = "SELECT * FROM buku";

        try (Connection conn = Koneksi.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                list.add(new Buku(
                        rs.getInt("id"),
                        rs.getInt("id_kategori"),
                        rs.getString("judul"),
                        rs.getString("penulis"),
                        rs.getString("penerbit"),
                        rs.getDouble("harga"),
                        rs.getInt("stok"),
                        rs.getString("deskripsi")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil semua buku: " + e.getMessage());
        }

        return list;
    }
}
