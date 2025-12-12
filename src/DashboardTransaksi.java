import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardTransaksi extends JFrame {

    private JTable tableBuku;
    private JTextField txtID, txtJudul, txtHarga, txtJumlah, txtTotal;
    private JButton btnHitung, btnBayar;

    public DashboardTransaksi() {
        setTitle("Dashboard Transaksi Toko Buku");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // ============================
        // Judul Dashboard
        // ============================
        JLabel lblTitle = new JLabel("Dashboard Transaksi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(230, 10, 300, 30);
        add(lblTitle);

        // ============================
        // Tabel Buku
        // ============================
        String[] kolom = {"ID Buku", "Judul", "Harga"};
        Object[][] data = {
                {"B001", "Pemrograman Java", 75000},
                {"B002", "Belajar Python", 68000},
                {"B003", "Algoritma & Struktur Data", 82000}
        };

        DefaultTableModel model = new DefaultTableModel(data, kolom);
        tableBuku = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableBuku);
        scrollPane.setBounds(20, 60, 430, 150);
        add(scrollPane);

        // ============================
        // Form Input
        // ============================
        JLabel lblID = new JLabel("ID Buku:");
        lblID.setBounds(470, 60, 100, 25);
        add(lblID);
        txtID = new JTextField();
        txtID.setBounds(540, 60, 120, 25);
        add(txtID);

        JLabel lblJudul = new JLabel("Judul:");
        lblJudul.setBounds(470, 95, 100, 25);
        add(lblJudul);
        txtJudul = new JTextField();
        txtJudul.setBounds(540, 95, 120, 25);
        add(txtJudul);

        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setBounds(470, 130, 100, 25);
        add(lblHarga);
        txtHarga = new JTextField();
        txtHarga.setBounds(540, 130, 120, 25);
        add(txtHarga);

        JLabel lblJumlah = new JLabel("Jumlah:");
        lblJumlah.setBounds(470, 165, 100, 25);
        add(lblJumlah);
        txtJumlah = new JTextField();
        txtJumlah.setBounds(540, 165, 120, 25);
        add(txtJumlah);

        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setBounds(470, 200, 100, 25);
        add(lblTotal);
        txtTotal = new JTextField();
        txtTotal.setBounds(540, 200, 120, 25);
        txtTotal.setEditable(false);
        add(txtTotal);

        // ============================
        // Tombol
        // ============================
        btnHitung = new JButton("Hitung Total");
        btnHitung.setBounds(470, 240, 190, 30);
        add(btnHitung);

        btnBayar = new JButton("Bayar");
        btnBayar.setBounds(470, 280, 190, 30);
        add(btnBayar);

        // ============================
        // EVENT – Klik tabel memindahkan data ke form
        // ============================
        tableBuku.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tableBuku.getSelectedRow();
            if (selectedRow >= 0) {
                txtID.setText(tableBuku.getValueAt(selectedRow, 0).toString());
                txtJudul.setText(tableBuku.getValueAt(selectedRow, 1).toString());
                txtHarga.setText(tableBuku.getValueAt(selectedRow, 2).toString());
            }
        });

        // ============================
        // EVENT – Hitung Total
        // ============================
        btnHitung.addActionListener(e -> {
            try {
                int harga = Integer.parseInt(txtHarga.getText());
                int jumlah = Integer.parseInt(txtJumlah.getText());
                int total = harga * jumlah;
                txtTotal.setText(String.valueOf(total));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Input tidak valid!");
            }
        });

        // ============================
        // EVENT – Bayar
        // ============================
        btnBayar.addActionListener(e -> {
            if (txtTotal.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Hitung total dulu!");
                return;
            }
            JOptionPane.showMessageDialog(null, "Pembayaran Berhasil!\nTotal: " + txtTotal.getText());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardTransaksi().setVisible(true));
    }
}
