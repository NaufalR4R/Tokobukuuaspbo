package admin.kategori;

import model.Kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class KelolaKategoriPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public KelolaKategoriPanel() {
        // --- Setup Panel ---
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(250, 247, 243));

        // 1. Inisialisasi Model Tabel Kategori
        String[] columns = {"ID", "Nama Kategori", "Jumlah Buku", "Aksi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 3;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class; // ID adalah Integer
                }
                return String.class;
            }
        };

        // 2. Header dan Tombol Tambah
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 247, 243));

        // Title dan Subtitle
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(250, 247, 243));
        JLabel title = new JLabel("Kelola Kategori");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(title);
        JLabel subtitle = new JLabel("Atur kategori untuk mengorganisir buku");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // Tombol Tambah Kategori
        JButton tambahKategoriBtn = new JButton("+ Tambah Kategori");
        tambahKategoriBtn.setBackground(new Color(80, 60, 40));
        tambahKategoriBtn.setForeground(Color.WHITE);
        tambahKategoriBtn.setFocusPainted(false);
        tambahKategoriBtn.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Memanggil TambahKategoriDialog dan REFRESH data setelahnya
        tambahKategoriBtn.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            TambahKategoriDialog dialog = new TambahKategoriDialog(owner, this);
            dialog.setVisible(true);
        });

        header.add(tambahKategoriBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 3. Setup Tabel
        table = new JTable(model);

        table.setRowHeight(30);
        TableColumn actionColumn = table.getColumnModel().getColumn(3);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(table, model, this));

        // Mengatur lebar kolom
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Nama Kategori
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Jumlah Buku
        actionColumn.setPreferredWidth(120);                       // Aksi (Edit/Hapus)

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // 4. Panggil method untuk memuat data dari database
        loadDataFromDatabase();

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    // Method untuk memuat data kategori dari database dan menampilkannya di JTable.
    public void loadDataFromDatabase() {
        model.setRowCount(0);

        List<Kategori> listKategori = Kategori.getAllKategori();

        for (Kategori k : listKategori) {

            int jumlahBuku = k.countBuku();
            String jumlahBukuText = jumlahBuku + " buku";
            model.addRow(new Object[]{
                    k.getId(),
                    k.getNama(),
                    jumlahBukuText,
                    ""
            });
        }
    }

    // Method untuk menangani aksi edit kategori (dipanggil dari ButtonEditor)
    public void handleEditAction(int row) {
        int idKategori = (int) model.getValueAt(row, 0);

        Window owner = SwingUtilities.getWindowAncestor(this);

        EditKategoriDialog dialog = new EditKategoriDialog(owner, this, idKategori);
        dialog.setVisible(true);
    }

    // Method untuk menangani aksi hapus kategori (dipanggil dari ButtonEditor)
    public void handleDeleteAction(int row) {
        int idKategori = (int) model.getValueAt(row, 0);
        String nama = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus kategori ID " + idKategori + " - " + nama + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = Kategori.deleteKategori(idKategori);

            if (success) {
                // Refresh data setelah penghapusan berhasil
                loadDataFromDatabase();
                JOptionPane.showMessageDialog(this, "Kategori ID " + idKategori + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kategori. Periksa koneksi atau jika kategori masih digunakan oleh buku.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}