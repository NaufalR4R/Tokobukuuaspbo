import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardTransaksi extends JFrame {

    private JTable tableBuku, tableCart;
    private JComboBox<String> comboID;
    private JTextField txtJudul, txtHarga, txtJumlah, txtTotalBayar;
    private JButton btnAddCart, btnBayar;

    DefaultTableModel modelCart;

    public DashboardTransaksi() {
        setTitle("Dashboard Transaksi Toko Buku");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);


        JLabel lblTitle = new JLabel("Dashboard Transaksi Toko Buku");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(280, 10, 400, 30);
        add(lblTitle);


        String[] kolom = {"ID", "Judul", "Harga"};
        Object[][] data = {
                {"B001", "Pemrograman Java", 75000},
                {"B002", "Belajar Python", 68000},
                {"B003", "Algoritma & Struktur Data", 82000},
                {"B004", "Database MySQL", 60000},
                {"B005", "Jaringan Komputer", 71000}
        };


        JLabel lblDaftarBuku = new JLabel("Daftar Buku:");
        lblDaftarBuku.setBounds(20, 60, 200, 25);
        add(lblDaftarBuku);

        DefaultTableModel model = new DefaultTableModel(data, kolom);
        tableBuku = new JTable(model);
        JScrollPane scrollBuku = new JScrollPane(tableBuku);
        scrollBuku.setBounds(20, 90, 400, 180);
        add(scrollBuku);


        JLabel lblID = new JLabel("ID Buku:");
        lblID.setBounds(450, 90, 100, 25);
        add(lblID);

        comboID = new JComboBox<>();
        comboID.setBounds(550, 90, 150, 25);
        add(comboID);

        // Masukkan ID ke dropdown
        for (Object[] row : data) {
            comboID.addItem(row[0].toString());
        }


        JLabel lblJudul = new JLabel("Judul:");
        lblJudul.setBounds(450, 125, 100, 25);
        add(lblJudul);

        txtJudul = new JTextField();
        txtJudul.setBounds(550, 125, 150, 25);
        txtJudul.setEditable(false);
        add(txtJudul);


        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setBounds(450, 160, 100, 25);
        add(lblHarga);

        txtHarga = new JTextField();
        txtHarga.setBounds(550, 160, 150, 25);
        txtHarga.setEditable(false);
        add(txtHarga);

        JLabel lblJumlah = new JLabel("Jumlah:");
        lblJumlah.setBounds(450, 195, 100, 25);
        add(lblJumlah);

        txtJumlah = new JTextField();
        txtJumlah.setBounds(550, 195, 150, 25);
        add(txtJumlah);

        comboID.addActionListener(e -> {
            String selectedID = comboID.getSelectedItem().toString();

            for (Object[] row : data) {
                if (row[0].equals(selectedID)) {
                    txtJudul.setText(row[1].toString());
                    txtHarga.setText(row[2].toString());
                }
            }
        });

        comboID.setSelectedIndex(0);


        btnAddCart = new JButton("Add to Cart");
        btnAddCart.setBounds(450, 230, 250, 30);
        add(btnAddCart);


        JLabel lblCart = new JLabel("Keranjang Belanja:");
        lblCart.setBounds(20, 290, 200, 25);
        add(lblCart);

        String[] kolomCart = {"ID", "Judul", "Harga", "Qty", "Total"};
        modelCart = new DefaultTableModel(kolomCart, 0);
        tableCart = new JTable(modelCart);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.setBounds(20, 320, 680, 170);
        add(scrollCart);


        JLabel lblTotal = new JLabel("Total Bayar:");
        lblTotal.setBounds(720, 320, 150, 25);
        add(lblTotal);

        txtTotalBayar = new JTextField();
        txtTotalBayar.setBounds(720, 350, 150, 30);
        txtTotalBayar.setEditable(false);
        add(txtTotalBayar);

        
        btnBayar = new JButton("Bayar");
        btnBayar.setBounds(720, 390, 150, 40);
        add(btnBayar);

        
        btnAddCart.addActionListener(e -> {
            try {
                String id = comboID.getSelectedItem().toString();
                String judul = txtJudul.getText();
                int harga = Integer.parseInt(txtHarga.getText());
                int qty = Integer.parseInt(txtJumlah.getText());
                int total = harga * qty;

                modelCart.addRow(new Object[]{id, judul, harga, qty, total});

                updateTotalBayar();
                txtJumlah.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Input tidak valid!");
            }
        });


        btnBayar.addActionListener(e -> {
            if (modelCart.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Keranjang masih kosong!");
                return;
            }
            JOptionPane.showMessageDialog(null, "Pembayaran berhasil!\nTotal: " + txtTotalBayar.getText());
            clearCart();
        });
    }

    private void updateTotalBayar() {
        int total = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            total += Integer.parseInt(modelCart.getValueAt(i, 4).toString());
        }
        txtTotalBayar.setText(String.valueOf(total));
    }

    private void clearCart() {
        modelCart.setRowCount(0);
        txtTotalBayar.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardTransaksi().setVisible(true));
    }
}
