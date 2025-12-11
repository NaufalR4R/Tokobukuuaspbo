package admin.buku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import model.Buku;
import model.Kategori;

public class EditBukuDialog extends JDialog {

    private final KelolaBukuPanel parentPanel;
    private final int idBuku;

    private Buku currentBuku;
    private List<Kategori> listKategori;

    private JTextField judulField;
    private JTextField pengarangField;
    private JTextField penerbitField;
    private JComboBox<Kategori> kategoriBox;
    private JTextField hargaField;
    private JTextField stokField;
    private JTextArea deskripsiArea;

    public EditBukuDialog(Window owner, KelolaBukuPanel parentPanel, int idBuku) {
        super(owner, "Edit Detail Buku", ModalityType.APPLICATION_MODAL);
        this.parentPanel = parentPanel;
        this.idBuku = idBuku;

        // --- 1. Ambil Data Buku dan Kategori dari DB ---
        currentBuku = Buku.getById(idBuku);
        listKategori = Kategori.getAllKategori();

        if (currentBuku == null) {
            JOptionPane.showMessageDialog(owner, "Gagal memuat data buku (ID: " + idBuku + ").", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        String currentJudul = currentBuku.getJudul();
        String currentPenulis = currentBuku.getPenulis();
        String currentPenerbit = currentBuku.getPenerbit() != null ? currentBuku.getPenerbit() : "";
        int currentIdKategori = currentBuku.getIdKategori();
        String currentHarga = String.format("%.0f", currentBuku.getHarga());
        String currentStok = String.valueOf(currentBuku.getStok());
        String currentDeskripsi = currentBuku.getDeskripsi() != null ? currentBuku.getDeskripsi() : "";

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Edit Buku ID: " + idBuku);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Judul
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Judul *"), gbc);
        judulField = new JTextField(currentJudul, 20); // Inisialisasi
        gbc.gridx = 1;
        add(judulField, gbc);

        // Pengarang (Penulis)
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Pengarang *"), gbc);
        pengarangField = new JTextField(currentPenulis, 20); // Inisialisasi
        gbc.gridx = 1;
        add(pengarangField, gbc);

        // Penerbit
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Penerbit"), gbc);
        penerbitField = new JTextField(currentPenerbit, 20); // Inisialisasi
        gbc.gridx = 1;
        add(penerbitField, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Kategori"), gbc);
        kategoriBox = new JComboBox<>(listKategori.toArray(new Kategori[0])); // Inisialisasi

        // Atur kategori yang terpilih berdasarkan ID
        for (Kategori k : listKategori) {
            if (k.getId() == currentIdKategori) {
                kategoriBox.setSelectedItem(k);
                break;
            }
        }
        gbc.gridx = 1;
        add(kategoriBox, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Harga (Rp) *"), gbc);
        hargaField = new JTextField(currentHarga, 10); // Inisialisasi
        gbc.gridx = 1;
        add(hargaField, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Stok *"), gbc);
        stokField = new JTextField(currentStok, 10); // Inisialisasi
        gbc.gridx = 1;
        add(stokField, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Deskripsi (Opsional)"), gbc);
        deskripsiArea = new JTextArea(currentDeskripsi, 3, 20); // Inisialisasi
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(deskripsiArea), gbc);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(250, 247, 243));

        JButton batalBtn = new JButton("Batal");
        JButton simpanBtn = new JButton("Simpan Perubahan");
        simpanBtn.setBackground(new Color(80, 60, 40));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFocusPainted(false);

        btnPanel.add(batalBtn);
        btnPanel.add(simpanBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // --- Action Listeners ---
        batalBtn.addActionListener(e -> dispose());

        simpanBtn.addActionListener((ActionEvent e) -> {
            simpanPerubahanBuku();
        });

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    private void simpanPerubahanBuku() {
        // 1. Ambil Data
        String judul = judulField.getText().trim();
        String penulis = pengarangField.getText().trim();
        String penerbit = penerbitField.getText().trim();
        Kategori kategoriDipilih = (Kategori) kategoriBox.getSelectedItem();
        String deskripsi = deskripsiArea.getText().trim();

        String hargaText = hargaField.getText().trim().replaceAll("[^\\d\\.]", "");
        String stokText = stokField.getText().trim().replaceAll("[^\\d]", "");

        // 2. Validasi Input
        if (judul.isEmpty() || penulis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan Pengarang harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (kategoriDipilih == null) {
            JOptionPane.showMessageDialog(this, "Kategori harus dipilih.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double harga;
        int stok;
        int idKategori = kategoriDipilih.getId();

        try {
            harga = Double.parseDouble(hargaText);
            stok = Integer.parseInt(stokText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka yang valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Update Objek Buku
        currentBuku.setIdKategori(idKategori);
        currentBuku.setJudul(judul);
        currentBuku.setPenulis(penulis);
        currentBuku.setPenerbit(penerbit);
        currentBuku.setHarga(harga);
        currentBuku.setStok(stok);
        currentBuku.setDeskripsi(deskripsi);

        // 4. Panggil method UPDATE database
        if (currentBuku.update()) {
            JOptionPane.showMessageDialog(this, "Buku '" + judul + "' berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Muat ulang data di KelolaBukuPanel
            parentPanel.loadBukuData();

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui buku di database. Periksa koneksi atau log sistem.", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}