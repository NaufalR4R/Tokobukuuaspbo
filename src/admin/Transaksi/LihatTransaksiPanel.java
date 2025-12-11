package admin.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

// Import Transaksi model
import model.Transaksi;

// Catatan: Anda perlu memastikan DetailTransaksiDialog sudah ada di package admin.Transaksi.

public class LihatTransaksiPanel extends JPanel {

    private final DefaultTableModel model;
    private final JTable table;
    // Format mata uang Rupiah
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public LihatTransaksiPanel() {
        // --- Setup Panel ---
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(250, 247, 243));

        // 1. Inisialisasi Model Tabel Transaksi
        String[] columns = {"ID Transaksi", "Nama Pegawai", "Waktu Transaksi", "Total Harga", "Bayar", "Kembalian", "Aksi"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Hanya kolom "Aksi" (indeks 6) yang bisa diedit (untuk tombol Detail)
                return col == 6;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        // 2. Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 247, 243));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(250, 247, 243));
        JLabel title = new JLabel("Lihat Transaksi");
        title.setFont(new Font("Serif", Font.BOLD, 26));
        titlePanel.add(title);
        JLabel subtitle = new JLabel("Pantau semua riwayat transaksi yang dibuat");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(250, 247, 243));
        topContainer.add(header, BorderLayout.NORTH);

        add(topContainer, BorderLayout.NORTH);

        // 3. Setup Tabel
        table = new JTable(model);
        table.setRowHeight(30);

        TableColumn actionColumn = table.getColumnModel().getColumn(6);
        actionColumn.setPreferredWidth(80);

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

        // 4. Muat Data dari Database
        loadDataFromDatabase();

        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Tambahkan listener untuk aksi klik Detail
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

     // Memuat data transaksi dari database dan mengisi JTable.
    public void loadDataFromDatabase() {
        model.setRowCount(0);
        List<Transaksi> listTransaksi = Transaksi.getAll();

        for (Transaksi t : listTransaksi) {
            // Format harga ke Rupiah
            String totalHarga = currencyFormat.format(t.getHargaTotal());
            String jumlahBayar = currencyFormat.format(t.getJumlahBayar());
            String kembalian = currencyFormat.format(t.getJumlahKembalian());

            // Format Timestamp ke String
            String waktuTransaksi = t.getCreatedAt() != null ? t.getCreatedAt().toString().substring(0, 19) : "-";

            model.addRow(new Object[]{
                    t.getId(),
                    t.getNama(),
                    waktuTransaksi,
                    totalHarga,
                    jumlahBayar,
                    kembalian,
                    "Detail"
            });
        }
    }


    public void handleDetailAction(int row) {
        String idTransaksiStr = model.getValueAt(row, 0).toString();

        Window owner = SwingUtilities.getWindowAncestor(this);
        DetailTransaksiDialog dialog = new DetailTransaksiDialog(owner, idTransaksiStr);

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