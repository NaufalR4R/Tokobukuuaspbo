package admin.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class LihatTransaksiPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;

    public LihatTransaksiPanel() {
        // --- Setup Panel ---
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(250, 247, 243));

        // 1. Inisialisasi Model Tabel Transaksi
        // Kita menggunakan kolom database yang relevan + informasi tambahan (user) dan Aksi
        String[] columns = {"ID Transaksi", "ID User", "Waktu Transaksi", "Total Harga", "Bayar", "Kembalian", "Aksi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Hanya kolom "Aksi" (indeks 6) yang bisa diedit (untuk tombol Detail)
                return col == 6;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 1) { // ID Transaksi dan ID User
                    return Integer.class;
                }
                return String.class;
            }
        };

        // 2. Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 247, 243));

        // Title
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(250, 247, 243));
        JLabel title = new JLabel("Lihat Transaksi");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(title);
        JLabel subtitle = new JLabel("Pantau semua riwayat transaksi yang dibuat");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // --- Search Panel (Tambahkan Filter/Search) ---
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setBackground(new Color(250, 247, 243));
        JTextField searchField = new JTextField("Cari berdasarkan ID atau tanggal...");
        JButton filterBtn = new JButton("Filter Tanggal");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(filterBtn, BorderLayout.EAST);

        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(250, 247, 243));
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);

        // 3. Setup Tabel
        table = new JTable(model);

        // Mengatur ketinggian baris
        table.setRowHeight(30);

        // Kolom Aksi (Detail)
        TableColumn actionColumn = table.getColumnModel().getColumn(6);

        // Catatan: Karena tombolnya hanya satu ('Detail'), kita tidak bisa menggunakan ButtonRenderer/Editor yang lama
        // Kita akan menggunakan logika sederhana untuk tombol Detail.
        // Untuk saat ini, kita akan biarkan sebagai kolom String biasa (tanpa tombol fungsional)
        // Jika Anda ingin tombol, kita perlu membuat ButtonRenderer/Editor baru yang hanya menampilkan satu tombol.
        // Sementara itu, kita gunakan placeholder:

        actionColumn.setPreferredWidth(80);

        // Mengatur lebar kolom lainnya
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID Transaksi
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // ID User
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Waktu Transaksi
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Total Harga
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Bayar
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Kembalian

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // 4. Data Dummy (Sesuai Struktur Database)
        model.addRow(new Object[]{
                101,
                1,
                "2025-12-10 15:30:00",
                "Rp 160.000",
                "Rp 200.000",
                "Rp 40.000",
                "Detail" // Aksi Placeholder
        });
        model.addRow(new Object[]{
                102,
                2,
                "2025-12-10 16:15:00",
                "Rp 70.000",
                "Rp 70.000",
                "Rp 0",
                "Detail"
        });

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Tambahkan listener untuk aksi klik Detail (jika menggunakan String "Detail" sebagai placeholder)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int col = table.columnAtPoint(evt.getPoint());
                int row = table.rowAtPoint(evt.getPoint());
                if (col == 6) { // Kolom Aksi
                    handleDetailAction(row);
                }
            }
        });
    }

    public void handleDetailAction(int row) {
        // Pastikan ID Transaksi adalah String
        String idTransaksi = model.getValueAt(row, 0).toString();

        // 1. Ambil Window parent
        Window owner = SwingUtilities.getWindowAncestor(this);

        // 2. Panggil DetailTransaksiDialog dengan ID Transaksi
        DetailTransaksiDialog dialog = new DetailTransaksiDialog(owner, idTransaksi);

        // 3. Tampilkan dialog
        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Panel - Lihat Transaksi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new LihatTransaksiPanel(), BorderLayout.CENTER);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
