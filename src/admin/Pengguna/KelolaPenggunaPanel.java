package admin.Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

import model.User;


public class KelolaPenggunaPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public KelolaPenggunaPanel() {
        // --- Setup Panel ---
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(250, 247, 243));

        // 1. Inisialisasi Model Tabel Pengguna
        String[] columns = {"ID", "Username", "Nama Lengkap", "Role", "Tanggal Buat", "Aksi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
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

        // 2. Header dan Tombol Tambah (Tidak Berubah)
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

        // LOGIKA Tambah Pengguna:
        tambahPenggunaBtn.addActionListener(e -> {
            Window owner = SwingUtilities.getWindowAncestor(this);
            TambahPenggunaDialog dialog = new TambahPenggunaDialog(owner, model);
            dialog.setVisible(true);
            // Panggil refresh setelah dialog ditutup
            loadDataFromDatabase();
        });

        header.add(tambahPenggunaBtn, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(250, 247, 243));
        topContainer.add(header, BorderLayout.NORTH);

        add(topContainer, BorderLayout.NORTH);

        // 3. Setup Tabel
        table = new JTable(model);
        table.setRowHeight(30);

        TableColumn actionColumn = table.getColumnModel().getColumn(5);

        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(table, model, this));

        // Mengatur lebar kolom
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        actionColumn.setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // 4. Muat Data dari Database
        loadDataFromDatabase();

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    // Memuat data pengguna dari database dan mengisi JTable.
    public void loadDataFromDatabase() {
        model.setRowCount(0);
        List<User> userList = User.getAllUsers();

        for (User user : userList) {
            String createdDateStr = user.getCreatedAt() != null ?
                    dateFormat.format(user.getCreatedAt()) :
                    "-";

            model.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getNamaLengkap(),
                    user.getRole(),
                    createdDateStr,
                    "" // Placeholder untuk tombol Aksi
            });
        }
    }

    // Method untuk menangani aksi edit pengguna
    public void handleEditAction(int row) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        EditPenggunaDialog dialog = new EditPenggunaDialog(owner, model, row);

        dialog.setVisible(true);
        loadDataFromDatabase();
    }

    // Method untuk menangani aksi hapus pengguna
    public void handleDeleteAction(int row) {
        String idStr = model.getValueAt(row, 0).toString();
        int id = Integer.parseInt(idStr);
        String username = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus pengguna ID " + id + " - " + username + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // LOGIKA: Panggil method delete dari model
            if (User.deleteUser(id)) {
                // Hapus baris dari tabel jika penghapusan di DB berhasil
                model.removeRow(row);
                JOptionPane.showMessageDialog(this, "Pengguna ID " + id + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Pesan error dari DB sudah ditangani di model.User.java
            }
        }
    }
}