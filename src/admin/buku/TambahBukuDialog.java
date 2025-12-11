package admin.buku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Import kelas Kategori dan Buku
import model.Buku;
import model.Kategori;
// ASUMSI: Class Buku ada di package models. Silakan sesuaikan jika berbeda.

public class TambahBukuDialog extends JDialog {

    private final KelolaBukuPanel parentPanel;

    // 1. Ganti array String statis dengan List<Kategori> dinamis
    private List<Kategori> listKategori;
    // 2. Ganti tipe JComboBox menjadi JComboBox<Kategori>
    private JComboBox<Kategori> kategoriBox;

    public TambahBukuDialog(Window owner, KelolaBukuPanel parentPanel) {
        super(owner, "Tambah Buku Baru", ModalityType.APPLICATION_MODAL);
        this.parentPanel = parentPanel;

        // Panggil method untuk memuat data kategori sebelum inisialisasi komponen
        loadKategoriData();

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Tambah Buku Baru");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Judul
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Judul *"), gbc);
        JTextField judulField = new JTextField(20);
        gbc.gridx = 1;
        add(judulField, gbc);

        // Pengarang (Penulis)
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Pengarang *"), gbc);
        JTextField pengarangField = new JTextField(20);
        gbc.gridx = 1;
        add(pengarangField, gbc);

        // Penerbit
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Penerbit"), gbc);
        JTextField penerbitField = new JTextField(20);
        gbc.gridx = 1;
        add(penerbitField, gbc);


        // Kategori (Perubahan di sini)
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Kategori"), gbc);
        // Inisialisasi JComboBox dengan List<Kategori> yang sudah dimuat
        kategoriBox = new JComboBox<>(listKategori.toArray(new Kategori[0]));
        gbc.gridx = 1;
        add(kategoriBox, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Harga (Rp) *"), gbc);
        JTextField hargaField = new JTextField("0", 10);
        gbc.gridx = 1;
        add(hargaField, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Stok *"), gbc);
        JTextField stokField = new JTextField("0", 10);
        gbc.gridx = 1;
        add(stokField, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Deskripsi (Opsional)"), gbc);
        JTextArea deskripsiArea = new JTextArea(3, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(deskripsiArea), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(250, 247, 243));

        JButton batalBtn = new JButton("Batal");
        JButton tambahBtn = new JButton("Tambah Buku");
        tambahBtn.setBackground(new Color(80, 60, 40));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFocusPainted(false);

        btnPanel.add(batalBtn);
        btnPanel.add(tambahBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        batalBtn.addActionListener(e -> dispose());

        // LOGIKA BARU: Simpan ke database (Ubah Signature)
        tambahBtn.addActionListener((ActionEvent e) -> {
            simpanBukuBaru(judulField, pengarangField, penerbitField, hargaField, stokField, deskripsiArea);
        });

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    /**
     * Memuat data kategori dari database saat dialog diinisialisasi.
     */
    private void loadKategoriData() {
        // Menggunakan method statis dari kelas Kategori yang sudah tersedia
        listKategori = Kategori.getAllKategori();

        if (listKategori.isEmpty()) {
            // Beri peringatan jika tidak ada kategori
            JOptionPane.showMessageDialog(this,
                    "Tidak ada data kategori. Harap tambahkan kategori terlebih dahulu.",
                    "Error Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method getKategoriIdByName dihapus karena tidak lagi diperlukan.

    /**
     * Logika untuk mengambil data dari form, memvalidasi, membuat objek Buku,
     * menyimpan ke database, dan me-refresh tabel utama. (Ubah Signature)
     */
    private void simpanBukuBaru(
            JTextField judulField, JTextField pengarangField, JTextField penerbitField,
            // JComboBox<String> kategoriBox Dihapus dari argumen
            JTextField hargaField, JTextField stokField,
            JTextArea deskripsiArea) {

        // 1. Ambil dan Bersihkan Data
        String judul = judulField.getText().trim();
        String penulis = pengarangField.getText().trim();
        String penerbit = penerbitField.getText().trim();

        // Ambil objek Kategori yang dipilih langsung dari class field JComboBox
        Kategori kategoriDipilih = (Kategori) kategoriBox.getSelectedItem();

        String deskripsi = deskripsiArea.getText().trim();

        // Hapus format non-digit ('Rp', titik, koma) dari harga dan stok
        String hargaText = hargaField.getText().trim().replaceAll("[^\\d]", "");
        String stokText = stokField.getText().trim().replaceAll("[^\\d]", "");

        // 2. Validasi Input
        if (judul.isEmpty() || penulis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan Pengarang harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (kategoriDipilih == null) {
            JOptionPane.showMessageDialog(this, "Kategori harus dipilih atau tidak ada data kategori.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double harga;
        int stok;
        int idKategori = kategoriDipilih.getId(); // Ambil ID Kategori langsung dari objek

        try {
            harga = Double.parseDouble(hargaText);
            stok = Integer.parseInt(stokText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka yang valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Buat Objek Buku dan Isi Data
        // ASUMSI: Class Buku memiliki constructor yang sesuai atau semua setter dipanggil.
        // Berdasarkan kode Anda yang menggunakan setter:
        Buku bukuBaru = new Buku();
        bukuBaru.setJudul(judul);
        bukuBaru.setPenulis(penulis);
        bukuBaru.setPenerbit(penerbit);
        bukuBaru.setIdKategori(idKategori); // Menggunakan ID Kategori yang benar
        bukuBaru.setHarga(harga);
        bukuBaru.setStok(stok);
        bukuBaru.setDeskripsi(deskripsi);

        // 4. Panggil CREATE Database
        // ASUMSI: Method create() di class Buku adalah method untuk menyimpan ke DB.
        if (bukuBaru.create()) {
            JOptionPane.showMessageDialog(this, "Buku '" + judul + "' berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // PENTING: Muat ulang data di KelolaBukuPanel agar tabel terupdate dari database
            parentPanel.loadBukuData();

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan buku ke database. Periksa koneksi atau log sistem.", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}