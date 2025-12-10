package admin.Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class KelolaPenggunaPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public KelolaPenggunaPanel() {
        // --- Setup Panel ---
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(250, 247, 243));

        // 1. Inisialisasi Model Tabel Pengguna
        // Kolom diambil dari struktur tabel 'user': id, username, nama_lengkap, role, created_at, Aksi
        String[] columns = {"ID", "Username", "Nama Lengkap", "Role", "Tanggal Buat", "Aksi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Hanya kolom "Aksi" (indeks 5) yang bisa diedit (untuk tombol)
                return col == 5;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) { // ID
                    return Integer.class;
                }
                return String.class;
            }
        };

        // 2. Header dan Tombol Tambah
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 247, 243));

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(250, 247, 243));
        JLabel title = new JLabel("Kelola Pengguna");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(title);
        JLabel subtitle = new JLabel("Tambah, edit, atau hapus data pengguna");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // Tombol Tambah Pengguna
        JButton tambahPenggunaBtn = new JButton("+ Tambah Pengguna");
        tambahPenggunaBtn.setBackground(new Color(80, 60, 40));
        tambahPenggunaBtn.setForeground(Color.WHITE);
        tambahPenggunaBtn.setFocusPainted(false);
        tambahPenggunaBtn.setFont(new Font("SansSerif", Font.BOLD, 12));

        // --- LOGIKA: Memanggil TambahPenggunaDialog ---
        tambahPenggunaBtn.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            TambahPenggunaDialog dialog = new TambahPenggunaDialog(owner, model);
            dialog.setVisible(true);
        });
        // ----------------------------------------------------

        header.add(tambahPenggunaBtn, BorderLayout.EAST);

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBackground(new Color(250, 247, 243));
        JTextField searchField = new JTextField("Cari berdasarkan username atau nama lengkap...");
        JButton refreshBtn = new JButton("Refresh");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(refreshBtn, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(250, 247, 243));
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // 3. Setup Tabel
        table = new JTable(model);

        // Mengatur ketinggian baris agar tombol muat
        table.setRowHeight(30);

        // PENTING: Atur Button Renderer dan Editor untuk kolom "Aksi" (indeks 5)
        TableColumn actionColumn = table.getColumnModel().getColumn(5);
        actionColumn.setCellRenderer(new ButtonRenderer());
        // Menggunakan 'this' agar ButtonEditor bisa memanggil handleAction di kelas ini
        actionColumn.setCellEditor(new ButtonEditor(table, model, this));

        // Mengatur lebar kolom
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // Username
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Nama Lengkap
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Role
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Tanggal Buat
        actionColumn.setPreferredWidth(120);                       // Aksi (Edit/Hapus)

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // 4. Data Dummy
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{1, "admin_user", "Budi Santoso", "admin", "2025-01-01 10:00:00", ""});
            model.addRow(new Object[]{2, "kasir_andi", "Andi Pratama", "cashier", "2025-01-05 11:30:00", ""});
            model.addRow(new Object[]{3, "kasir_siti", "Siti Aisyah", "cashier", "2025-02-10 09:00:00", ""});
        }

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    // Method untuk menangani aksi edit pengguna (Dipanggil dari ButtonEditor)
    public void handleEditAction(int row) {
        // --- LOGIKA: Memanggil EditPenggunaDialog ---
        Window owner = SwingUtilities.getWindowAncestor(this);
        EditPenggunaDialog dialog = new EditPenggunaDialog(owner, model, row);
        dialog.setVisible(true);
        // ---------------------------------------------
    }

    // Method untuk menangani aksi hapus pengguna (Dipanggil dari ButtonEditor)
    public void handleDeleteAction(int row) {
        String id = model.getValueAt(row, 0).toString();
        String username = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus pengguna ID " + id + " - " + username + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Pengguna ID " + id + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}