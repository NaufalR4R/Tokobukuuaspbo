package admin.buku;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;

// Import kelas Kategori dan Buku
import admin.kategori.Kategori;
// ASUMSI: Class Buku ada di package models. Silakan sesuaikan jika berbeda.
import admin.buku.Buku;

public class KelolaBukuPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public KelolaBukuPanel() {

        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(250, 247, 243));

        String[] columns = {"ID", "Judul", "Pengarang", "Kategori", "Harga", "Stok", "Aksi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6; // Hanya kolom "Aksi" yang bisa diedit (untuk tombol)
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) { // ID dan Stok
                    return Integer.class;
                } else if (columnIndex == 4) { // Harga
                    return String.class;
                }
                return String.class;
            }
        };

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 247, 243));

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(250, 247, 243));
        JLabel title = new JLabel("Kelola Buku");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(title);
        JLabel subtitle = new JLabel("Tambah, edit, atau hapus buku dari katalog");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // Tombol Tambah Buku
        JButton tambahBukuBtn = new JButton("+ Tambah Buku");
        tambahBukuBtn.setBackground(new Color(80, 60, 40));
        tambahBukuBtn.setForeground(Color.WHITE);
        tambahBukuBtn.setFocusPainted(false);
        tambahBukuBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        tambahBukuBtn.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            TambahBukuDialog dialog = new TambahBukuDialog(owner, this);
            dialog.setVisible(true);
        });
        header.add(tambahBukuBtn, BorderLayout.EAST);

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBackground(new Color(250, 247, 243));
        JTextField searchField = new JTextField("Cari judul, pengarang, atau kategori...");
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadBukuData());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(refreshBtn, BorderLayout.EAST);
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(250, 247, 243));
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        table = new JTable(model);
        table.setRowHeight(30);

        // ASUMSI: ButtonRenderer dan ButtonEditor sudah tersedia di package yang sama
        TableColumn actionColumn = table.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(table, model, this));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Atur lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(30); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Judul
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Pengarang
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Kategori
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Harga
        table.getColumnModel().getColumn(5).setPreferredWidth(50);  // Stok
        actionColumn.setPreferredWidth(160); // Aksi

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        loadBukuData();

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    // Memuat data buku dari database dan mengisi JTable.
    public void loadBukuData() {
        model.setRowCount(0);

        // ASUMSI: Method Buku.getAll() sudah ada dan mengembalikan ArrayList<Buku>
        ArrayList<Buku> listBuku = Buku.getAll();

        for (Buku buku : listBuku) {
            String kategoriNama;

            // 1. Dapatkan ID Kategori dari objek Buku
            int idKategori = buku.getIdKategori();

            // 2. Dapatkan objek Kategori berdasarkan ID
            Kategori kategori = Kategori.getKategoriById(idKategori);

            // 3. Ekstrak nama kategori
            if (kategori != null) {
                kategoriNama = kategori.getNama(); // Mengambil nama kategori
            } else {
                kategoriNama = "N/A (ID: " + idKategori + ")"; // Handle jika kategori tidak ditemukan
            }

            String hargaFormatted = String.format("Rp %,.0f", buku.getHarga());

            model.addRow(new Object[]{
                    buku.getId(),
                    buku.getJudul(),
                    buku.getPenulis(),
                    kategoriNama, // Menggunakan Nama Kategori
                    hargaFormatted,
                    buku.getStok(),
                    ""
            });
        }
    }

    // Method untuk menangani aksi edit
    public void handleEditAction(int row) {
        int idBuku = (int) model.getValueAt(row, 0);
        Window owner = SwingUtilities.getWindowAncestor(this);
        // ASUMSI: EditBukuDialog menerima KelolaBukuPanel (this) agar bisa refresh data
        EditBukuDialog dialog = new EditBukuDialog(owner, this.model, idBuku);
        dialog.setVisible(true);
    }

    // Method untuk menangani aksi hapus
    public void handleDeleteAction(int row) {
        int id = (int) model.getValueAt(row, 0);
        String judul = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus buku ID " + id + " - " + judul + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (Buku.delete(id)) {
                loadBukuData();
                JOptionPane.showMessageDialog(this, "Buku ID " + id + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus buku ID " + id + " dari database.", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}