package admin.buku;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

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
                return col == 6;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) {
                    return Integer.class;
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
            TambahBukuDialog dialog = new TambahBukuDialog(owner, model);
            dialog.setVisible(true);
        });
        header.add(tambahBukuBtn, BorderLayout.EAST);

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBackground(new Color(250, 247, 243));
        JTextField searchField = new JTextField("Cari judul, pengarang, atau kategori...");
        JButton refreshBtn = new JButton("Refresh");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(refreshBtn, BorderLayout.EAST);
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(250, 247, 243));
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        table = new JTable(model);
        table.setRowHeight(30);
        TableColumn actionColumn = table.getColumnModel().getColumn(6);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(table, model, this));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
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

        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{1, "Laskar Pelangi", "Andrea Hirata", "Fiksi", "Rp 85.000", 25, ""});
            model.addRow(new Object[]{2, "Bumi Manusia", "Pramoedya Ananta Toer", "Fiksi", "Rp 95.000", 15, ""});
            model.addRow(new Object[]{3, "Filosofi Kopi", "Dewi Lestari", "Non-Fiksi", "Rp 70.000", 10, ""});
        }
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
    }

    // Method untuk menangani aksi edit
    public void handleEditAction(int row) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        EditBukuDialog dialog = new EditBukuDialog(owner, model, row);
        dialog.setVisible(true);
    }

    // Method untuk menangani aksi hapus (Sama seperti sebelumnya)
    public void handleDeleteAction(int row) {
        String id = model.getValueAt(row, 0).toString();
        String judul = model.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus buku ID " + id + " - " + judul + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Buku ID " + id + " berhasil dihapus.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
