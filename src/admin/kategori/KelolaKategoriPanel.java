package admin.kategori;

import admin.kategori.ButtonRenderer;
import admin.kategori.EditKategoriDialog;
import admin.kategori.TambahKategoriDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

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
                // Hanya kolom "Aksi" (indeks 3) yang bisa diedit (untuk tombol)
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

        // --- PERUBAHAN: Memanggil TambahKategoriDialog ---
        tambahKategoriBtn.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            TambahKategoriDialog dialog = new TambahKategoriDialog(owner, model);
            dialog.setVisible(true);
        });
        // ----------------------------------------------------

        header.add(tambahKategoriBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 3. Setup Tabel
        table = new JTable(model);

        // Mengatur ketinggian baris agar tombol muat
        table.setRowHeight(30);

        // PENTING: Atur Button Renderer dan Editor untuk kolom "Aksi" (indeks 3)
        // Catatan: Pastikan ButtonRenderer dan ButtonEditor sudah diupdate untuk mendukung kedua panel!
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

        // 4. Data Dummy
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{1, "Fiksi", "3 buku", ""});
            model.addRow(new Object[]{2, "Non-Fiksi", "2 buku", ""});
            model.addRow(new Object[]{3, "Teknologi", "2 buku", ""});
            model.addRow(new Object[]{4, "Bisnis", "1 buku", ""});
        }


        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    // Method untuk menangani aksi edit kategori (dipanggil dari ButtonEditor)
    public void handleEditAction(int row) {
        // --- PERUBAHAN: Memanggil EditKategoriDialog ---
        Window owner = SwingUtilities.getWindowAncestor(this);
        EditKategoriDialog dialog = new EditKategoriDialog(owner, model, row);
        dialog.setVisible(true);
        // ---------------------------------------------
    }

    // Method untuk menangani aksi hapus kategori (dipanggil dari ButtonEditor)
    public void handleDeleteAction(int row) {
        Object idObj = model.getValueAt(row, 0);
        String nama = model.getValueAt(row, 1).toString();

        String id = (idObj != null) ? idObj.toString() : "N/A";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus kategori ID " + id + " - " + nama + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Kategori ID " + id + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}