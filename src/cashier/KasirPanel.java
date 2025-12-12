package cashier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;

public class KasirPanel extends JPanel {

    private final DefaultTableModel katalogModel; // Model untuk daftar buku yang tersedia
    private final DefaultTableModel cartModel;    // Model untuk keranjang belanja
    private final JLabel subTotalLabel;
    private final JTextField bayarField;
    private final JLabel kembalianLabel;

    private double currentSubTotal = 0;

    public KasirPanel(JFrame parentFrame) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // --- Container Utama (Split Pane) ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.7); // Beri ruang lebih besar untuk Katalog

        // ===================================================================
        // 1. Panel Katalog Buku (Kiri - Meniru Kelola Buku)
        // ===================================================================

        // Setup Model Katalog
        String[] katalogColumns = {"ID", "Judul", "Pengarang", "Kategori", "Harga", "Stok"};
        katalogModel = new DefaultTableModel(katalogColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Katalog tidak bisa diedit langsung
            }
        };

        JTable katalogTable = new JTable(katalogModel);
        katalogTable.setRowHeight(25);
        katalogTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Lebar Judul

        // Header Katalog dengan Search/Filter
        JPanel katalogHeader = new JPanel(new BorderLayout(5, 5));
        katalogHeader.setBackground(new Color(250, 250, 250));
        JTextField searchField = new JTextField("Cari Judul, Pengarang, atau ID...");
        katalogHeader.add(searchField, BorderLayout.CENTER);
        katalogHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel katalogPanel = new JPanel(new BorderLayout());
        katalogPanel.setBorder(BorderFactory.createTitledBorder("Katalog Buku (Klik untuk Menambahkan)"));
        katalogPanel.setBackground(new Color(250, 250, 250));
        katalogPanel.add(katalogHeader, BorderLayout.NORTH);
        katalogPanel.add(new JScrollPane(katalogTable), BorderLayout.CENTER);

        mainSplit.setLeftComponent(katalogPanel);

        // ===================================================================
        // 2. Panel Keranjang & Pembayaran (Kanan - Mirip POS)
        // ===================================================================

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(245, 245, 245));

        // --- Keranjang Belanja (Kanan Atas) ---
        String[] cartColumns = {"ID", "Judul", "Harga Satuan", "Qty", "Subtotal"};
        cartModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Hanya Qty (indeks 3) yang bisa diedit di keranjang
                return col == 3;
            }
        };
        JTable cartTable = new JTable(cartModel);
        cartTable.setRowHeight(25);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Lebar Judul

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));

        // --- Panel Pembayaran (Kanan Bawah) ---
        JPanel paymentPanel = new JPanel(new BorderLayout(10, 10));
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        paymentPanel.setBackground(new Color(245, 245, 245));

        // Ringkasan Total
        JPanel totalPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        totalPanel.setBackground(new Color(250, 250, 250));

        totalPanel.add(new JLabel("SUB TOTAL (Rp):"));
        subTotalLabel = new JLabel(formatRupiah(currentSubTotal));
        subTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        subTotalLabel.setForeground(new Color(46, 139, 87));
        totalPanel.add(subTotalLabel);

        totalPanel.add(new JLabel("KEMBALIAN (Rp):"));
        kembalianLabel = new JLabel(formatRupiah(0));
        kembalianLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        kembalianLabel.setForeground(new Color(70, 130, 180));
        totalPanel.add(kembalianLabel);

        // Input Bayar dan Tombol
        JPanel inputBtnPanel = new JPanel(new BorderLayout(5, 5));
        inputBtnPanel.setBackground(new Color(250, 250, 250));
        bayarField = new JTextField();
        bayarField.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JButton selesaiBtn = new JButton("SELESAIKAN TRANSAKSI (F1)");
        selesaiBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        selesaiBtn.setBackground(new Color(46, 139, 87));
        selesaiBtn.setForeground(Color.WHITE);

        inputBtnPanel.add(new JLabel("JUMLAH BAYAR:"), BorderLayout.NORTH);
        inputBtnPanel.add(bayarField, BorderLayout.CENTER);
        inputBtnPanel.add(selesaiBtn, BorderLayout.SOUTH);

        paymentPanel.add(totalPanel, BorderLayout.NORTH);
        paymentPanel.add(inputBtnPanel, BorderLayout.CENTER);

        // Gabungkan Keranjang dan Pembayaran
        rightPanel.add(cartScrollPane, BorderLayout.CENTER);
        rightPanel.add(paymentPanel, BorderLayout.SOUTH);

        mainSplit.setRightComponent(rightPanel);
        add(mainSplit, BorderLayout.CENTER);


        // --- 3. Data Dummy dan Listeners ---
        loadKatalogDummy(); // Isi Katalog

        // Listener: Klik pada tabel katalog untuk menambahkan item
        katalogTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = katalogTable.getSelectedRow();
                    if (row != -1) {
                        tambahItemDariKatalog(row);
                    }
                }
            }
        });

        // Listener untuk Qty di Keranjang (saat diedit)
        cartModel.addTableModelListener(e -> {
            if (e.getColumn() == 3) { // Hanya jika Qty (indeks 3) diubah
                updateItemInCart(e.getFirstRow());
            }
        });

        bayarField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungKembalian();
            }
        });

        selesaiBtn.addActionListener(e -> selesaikanTransaksi());

        // Tambahkan tombol batal transaksi di suatu tempat (misal di paymentPanel)
        JButton batalBtn = new JButton("Batal Transaksi");
        batalBtn.addActionListener(e -> batalTransaksi());
        JPanel footerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerButtons.setBackground(new Color(245, 245, 245));
        footerButtons.add(batalBtn);
        paymentPanel.add(footerButtons, BorderLayout.SOUTH);

        updateSubTotal();
    }

    // --- Method Pembantu ---

    // DALAM KasirPanel.java
    private void loadKatalogDummy() {
        // Kolom: {"ID", "Judul", "Pengarang", "Kategori", "Harga", "Stok"}
        // Ganti formatRupiah(...) menjadi angka biasa di sini
        katalogModel.addRow(new Object[]{1, "Laskar Pelangi", "Andrea Hirata", "Fiksi", 85000.0, 25}); // <-- UBAH KE ANGKA
        katalogModel.addRow(new Object[]{2, "Bumi Manusia", "Pramoedya A. T.", "Fiksi", 95000.0, 15});  // <-- UBAH KE ANGKA
        katalogModel.addRow(new Object[]{3, "Sapiens", "Yuval N. Harari", "Non-Fiksi", 150000.0, 30}); // <-- UBAH KE ANGKA
        katalogModel.addRow(new Object[]{4, "Atomic Habits", "James Clear", "Non-Fiksi", 120000.0, 40}); // <-- UBAH KE ANGKA
        katalogModel.addRow(new Object[]{5, "Rich Dad Poor Dad", "Robert Kiyosaki", "Bisnis", 98000.0, 35}); // <-- UBAH KE ANGKA
    }

    private void tambahItemDariKatalog(int katalogRow) {
        // Ambil data dari baris katalog yang diklik
        Object id = katalogModel.getValueAt(katalogRow, 0);
        String judul = katalogModel.getValueAt(katalogRow, 1).toString();

        // PENTING: Ambil nilai Harga sebagai DOUBLE/NUMBER
        double hargaSatuan;
        try {
            hargaSatuan = (Double) katalogModel.getValueAt(katalogRow, 4);
        } catch (ClassCastException e) {
            // Jika Anda menggunakan Integer atau data string yang belum diubah,
            // Error ini akan muncul. Pastikan loadKatalogDummy sudah diubah ke double.
            JOptionPane.showMessageDialog(this,
                    "Error: Kolom Harga di Katalog harus berupa angka (Double).",
                    "Kesalahan Data Internal", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stokTersedia = (Integer) katalogModel.getValueAt(katalogRow, 5);
        String hargaRupiahFormatted = formatRupiah(hargaSatuan);

        try {
            // Cek apakah item sudah ada di keranjang
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if (cartModel.getValueAt(i, 0).equals(id)) {
                    // Item sudah ada, tambahkan Qty
                    int currentQty = (Integer) cartModel.getValueAt(i, 3);
                    int newQty = currentQty + 1;

                    if (newQty > stokTersedia) {
                        JOptionPane.showMessageDialog(this, "Stok buku " + judul + " tidak mencukupi! Tersedia: " + stokTersedia, "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    cartModel.setValueAt(newQty, i, 3);

                    // Hitung ulang Subtotal (karena TableModelListener mungkin tidak dipanggil saat set value)
                    double newSubtotal = hargaSatuan * newQty;
                    cartModel.setValueAt(formatRupiah(newSubtotal), i, 4);
                    updateSubTotal();
                    return;
                }
            }

            // Item belum ada, tambahkan baris baru
            if (stokTersedia <= 0) {
                JOptionPane.showMessageDialog(this, "Stok buku " + judul + " habis.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double subtotal = hargaSatuan * 1;

            // Kolom Cart: {"ID", "Judul", "Harga Satuan", "Qty", "Subtotal"}
            cartModel.addRow(new Object[]{id, judul, hargaRupiahFormatted, 1, formatRupiah(subtotal)});

            updateSubTotal();

        } catch (Exception e) {
            // Ini menangani error non-parsing (seperti error casting Qty)
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memproses item: " + e.getMessage(), "Error Internal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateItemInCart(int cartRow) {
        try {
            // Ambil nilai Qty yang baru
            int newQty = Integer.parseInt(cartModel.getValueAt(cartRow, 3).toString());
            String id = cartModel.getValueAt(cartRow, 0).toString();

            if (newQty <= 0) {
                // Jika Qty 0 atau kurang, hapus saja
                cartModel.removeRow(cartRow);
                updateSubTotal();
                return;
            }

            // 1. Dapatkan Stok Tersedia dari Tabel Katalog
            int katalogRow = -1;
            int stokTersedia = 0;
            for (int i = 0; i < katalogModel.getRowCount(); i++) {
                if (katalogModel.getValueAt(i, 0).toString().equals(id)) {
                    katalogRow = i;
                    stokTersedia = (Integer) katalogModel.getValueAt(i, 5);
                    break;
                }
            }

            if (katalogRow != -1 && newQty > stokTersedia) {
                JOptionPane.showMessageDialog(this, "Stok buku tidak mencukupi! Maksimum: " + stokTersedia, "Peringatan", JOptionPane.WARNING_MESSAGE);
                // Kembalikan Qty ke nilai sebelumnya (ini rumit, jadi kita bisa batalkan aksi)
                // Untuk sementara, kita set ke stok maksimum dan update
                newQty = stokTersedia;
                cartModel.setValueAt(newQty, cartRow, 3);
            }

            // 2. Hitung Ulang Subtotal
            String hargaRupiah = cartModel.getValueAt(cartRow, 2).toString();
            double hargaSatuan = parseRupiah(hargaRupiah);
            double newSubtotal = hargaSatuan * newQty;

            // 3. Update Model
            cartModel.setValueAt(formatRupiah(newSubtotal), cartRow, 4);
            updateSubTotal();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            // Anda mungkin perlu memuat ulang nilai dari database/status sebelumnya di sini
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengupdate item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSubTotal() {
        double total = 0;
        // Iterasi melalui tabel keranjang
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            try {
                String subtotalString = cartModel.getValueAt(i, 4).toString();
                total += parseRupiah(subtotalString);
            } catch (Exception ex) {
                // Ignore parsing errors
            }
        }
        currentSubTotal = total;
        subTotalLabel.setText(formatRupiah(currentSubTotal));
        hitungKembalian();
    }

    private void hitungKembalian() {
        try {
            String bayarText = bayarField.getText().replace(".", "").replace(",", "");
            double bayar = Double.parseDouble(bayarText);
            double kembalian = bayar - currentSubTotal;
            kembalianLabel.setText(formatRupiah(kembalian));
            kembalianLabel.setForeground(kembalian >= 0 ? new Color(70, 130, 180) : Color.RED);
        } catch (NumberFormatException ex) {
            kembalianLabel.setText(formatRupiah(0));
            kembalianLabel.setForeground(new Color(70, 130, 180));
        }
    }

    private void selesaikanTransaksi() {
        if (currentSubTotal <= 0) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double bayar = Double.parseDouble(bayarField.getText().replace(".", "").replace(",", ""));
            if (bayar < currentSubTotal) {
                JOptionPane.showMessageDialog(this, "Jumlah bayar kurang! Total: " + formatRupiah(currentSubTotal), "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Logika Transaksi ke Database (Mencakup: Transaksi & Detail Transaksi)
            JOptionPane.showMessageDialog(this,
                    "Transaksi Berhasil!\nTotal: " + formatRupiah(currentSubTotal) +
                            "\nKembalian: " + kembalianLabel.getText(),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Bersihkan UI dan Perbarui Stok
            batalTransaksi();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah bayar yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void batalTransaksi() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin membatalkan transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cartModel.setRowCount(0);
            currentSubTotal = 0;
            subTotalLabel.setText(formatRupiah(0));
            bayarField.setText("");
            kembalianLabel.setText(formatRupiah(0));
            // Fokus ke field search/input barcode jika ada
        }
    }

    // --- Helper Format Rupiah ---
    private String formatRupiah(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        if (value < 0) {
            // Handle nilai negatif (untuk kembalian yang kurang bayar)
            return "- " + format.format(-value).replace(",00", "");
        }
        return format.format(value).replace(",00", "");
    }

    private double parseRupiah(String rupiah) throws java.text.ParseException {
        String cleanString = rupiah.replace("Rp ", "").replace(".", "");
        cleanString = cleanString.replace(",", ".");
        return Double.parseDouble(cleanString);
    }
}