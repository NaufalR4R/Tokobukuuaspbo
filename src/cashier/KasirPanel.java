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
import java.util.ArrayList;

// Import class model yang dibutuhkan
import model.Buku;
import model.Transaksi;
import model.ItemTransaksi;

public class KasirPanel extends JPanel {

    private final JFrame parentFrame;
    private final DefaultTableModel katalogModel;
    private final DefaultTableModel cartModel;
    private final JLabel subTotalLabel;
    private final JTextField bayarField;
    private final JLabel kembalianLabel;

    // ASUMSI: ID User yang sedang login (Ganti dengan logika login Anda)
    private final int currentUserId;

    private double currentSubTotal = 0;

    public KasirPanel(JFrame parentFrame, int userId) {
        this.parentFrame = parentFrame;
        this.currentUserId = userId;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // --- Container Utama (Split Pane) ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.7);

        // ===================================================================
        // 1. Panel Katalog Buku (Kiri)
        // ===================================================================

        // Setup Model Katalog - PENTING: Tentukan Tipe Data untuk ID, Harga, Stok
        String[] katalogColumns = {"ID", "Judul", "Pengarang", "Kategori", "Harga", "Stok"};
        katalogModel = new DefaultTableModel(katalogColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 5) return Integer.class; // ID, Stok
                if (columnIndex == 4) return Double.class; // Harga harus Double
                return String.class;
            }
        };

        JTable katalogTable = new JTable(katalogModel);
        katalogTable.setRowHeight(25);
        katalogTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        // ... (Header Katalog UI)
        JPanel katalogHeader = new JPanel(new BorderLayout(5, 5));
        katalogHeader.setBackground(new Color(250, 250, 250));
        katalogHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel katalogPanel = new JPanel(new BorderLayout());
        katalogPanel.setBorder(BorderFactory.createTitledBorder("Katalog Buku (Klik untuk Menambahkan)"));
        katalogPanel.setBackground(new Color(250, 250, 250));
        katalogPanel.add(katalogHeader, BorderLayout.NORTH);
        katalogPanel.add(new JScrollPane(katalogTable), BorderLayout.CENTER);

        mainSplit.setLeftComponent(katalogPanel);

        // ===================================================================
        // 2. Panel Keranjang & Pembayaran (Kanan)
        // ===================================================================

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(245, 245, 245));

        // --- Keranjang Belanja (Kanan Atas) ---
        // Kolom Cart: {"ID", "Judul", "Harga Satuan", "Qty", "Subtotal"}
        String[] cartColumns = {"ID", "Judul", "Harga Satuan", "Qty", "Subtotal"};
        cartModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return col == 3; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 3) return Integer.class; // ID, Qty
                return String.class;
            }
        };
        JTable cartTable = new JTable(cartModel);
        cartTable.setRowHeight(25);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200);

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


        // --- 3. Data Database dan Listeners ---
        loadKatalogData(); // Panggil data dari database

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
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 3) {
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

        // Tombol batal transaksi
        JButton batalBtn = new JButton("Batal Transaksi");
        batalBtn.addActionListener(e -> batalTransaksi());
        JPanel footerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerButtons.setBackground(new Color(245, 245, 245));
        footerButtons.add(batalBtn);
        paymentPanel.add(footerButtons, BorderLayout.SOUTH);

        updateSubTotal();
    }

    // --- DATABASE INTEGRATION METHODS ---

    private void loadKatalogData() {
        // Kolom: {"ID", "Judul", "Pengarang", "Kategori", "Harga", "Stok"}
        katalogModel.setRowCount(0); // Bersihkan data lama
        ArrayList<Buku> listBuku = Buku.getAll();

        // Ganti Kategori ID ke Nama Kategori (Placeholder mapping)
        // GANTI INI JIKA ANDA MEMILIKI TABEL KATEGORI YANG BENAR
        String[] kategoriOptions = new String[]{"Fiksi", "Non-Fiksi", "Teknologi", "Bisnis", "Lainnya"};

        for (Buku buku : listBuku) {
            String kategoriNama = "Lainnya";
            try {
                // Asumsi ID kategori 1=Fiksi, 2=NonFiksi, dst.
                kategoriNama = kategoriOptions[buku.getIdKategori() - 1];
            } catch (ArrayIndexOutOfBoundsException ignored) {}

            katalogModel.addRow(new Object[]{
                    buku.getId(),
                    buku.getJudul(),
                    buku.getPenulis(),
                    kategoriNama,
                    buku.getHarga(), // Masukkan sebagai Double
                    buku.getStok()
            });
        }
    }


    private void selesaikanTransaksi() {
        if (currentSubTotal <= 0) {
            JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Ambil Data Bayar
            String bayarText = bayarField.getText().replace(".", "").replace(",", "");
            double bayar = Double.parseDouble(bayarText);

            if (bayar < currentSubTotal) {
                JOptionPane.showMessageDialog(this, "Jumlah bayar kurang! Total: " + formatRupiah(currentSubTotal), "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double kembalian = bayar - currentSubTotal;

            // 2. BUAT & SIMPAN TRANSAKSI BARU (MASTER)
            Transaksi transaksiBaru = new Transaksi();
            transaksiBaru.setUserId(this.currentUserId);
            transaksiBaru.setHargaTotal(currentSubTotal);
            transaksiBaru.setJumlahBayar(bayar);
            transaksiBaru.setJumlahKembalian(kembalian);

            if (transaksiBaru.create()) {
                int idTransaksi = transaksiBaru.getId();
                boolean allItemsSaved = true;

                // 3. SIMPAN DETAIL ITEM TRANSAKSI DAN UPDATE STOK
                for (int i = 0; i < cartModel.getRowCount(); i++) {
                    int idBuku = (Integer) cartModel.getValueAt(i, 0);
                    int qty = (Integer) cartModel.getValueAt(i, 3);
                    String hargaRupiah = cartModel.getValueAt(i, 2).toString();
                    double hargaSatuan = parseRupiah(hargaRupiah);

                    ItemTransaksi item = new ItemTransaksi();
                    item.setIdTransaksi(idTransaksi);
                    item.setIdBuku(idBuku);
                    item.setJumlah(qty);
                    item.setHargaItem(hargaSatuan);

                    if (!item.create()) {
                        allItemsSaved = false;
                        System.out.println("Gagal menyimpan item buku ID: " + idBuku);
                    }

                    // 4. Update Stok Buku
                    if (!Buku.kurangiStok(idBuku, qty)) {
                        System.out.println("Gagal mengurangi stok buku ID: " + idBuku + ". Perlu dicek manual.");
                    }
                }

                // --- Transaksi Berhasil ---
                if (allItemsSaved) {
                    JOptionPane.showMessageDialog(this,
                            "Transaksi #" + idTransaksi + " Berhasil!\nTotal: " + formatRupiah(currentSubTotal) +
                                    "\nDibayar: " + formatRupiah(bayar) +
                                    "\nKembalian: " + formatRupiah(kembalian),
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Transaksi utama berhasil, tetapi beberapa item gagal disimpan. Cek log sistem.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }

                // Bersihkan UI dan Muat Ulang Katalog (untuk update stok)
                batalTransaksi(false);
                loadKatalogData();

            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat transaksi di database.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah bayar yang valid (hanya angka).", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(this, "Error saat memproses harga: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void batalTransaksi() {
        batalTransaksi(true);
    }

    private void batalTransaksi(boolean showConfirm) {
        if (!showConfirm || JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin membatalkan transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            cartModel.setRowCount(0);
            currentSubTotal = 0;
            subTotalLabel.setText(formatRupiah(0));
            bayarField.setText("");
            kembalianLabel.setText(formatRupiah(0));

            if (parentFrame != null) {
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        }
    }

    // --- (Sisanya dari method-method UI dan Helper Format Rupiah) ---
    // ... (tambahItemDariKatalog, updateItemInCart, updateSubTotal, hitungKembalian)

    private void tambahItemDariKatalog(int katalogRow) {
        int id = (Integer) katalogModel.getValueAt(katalogRow, 0);
        String judul = katalogModel.getValueAt(katalogRow, 1).toString();
        double hargaSatuan = (Double) katalogModel.getValueAt(katalogRow, 4);
        int stokTersedia = (Integer) katalogModel.getValueAt(katalogRow, 5);
        String hargaRupiahFormatted = formatRupiah(hargaSatuan);

        try {
            for (int i = 0; i < cartModel.getRowCount(); i++) {
                if (((Integer) cartModel.getValueAt(i, 0)) == id) {
                    int currentQty = (Integer) cartModel.getValueAt(i, 3);
                    int newQty = currentQty + 1;

                    if (newQty > stokTersedia) {
                        JOptionPane.showMessageDialog(this, "Stok buku " + judul + " tidak mencukupi! Tersedia: " + stokTersedia, "Peringatan", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    cartModel.setValueAt(newQty, i, 3);
                    double newSubtotal = hargaSatuan * newQty;
                    cartModel.setValueAt(formatRupiah(newSubtotal), i, 4);
                    updateSubTotal();
                    return;
                }
            }
            if (stokTersedia <= 0) {
                JOptionPane.showMessageDialog(this, "Stok buku " + judul + " habis.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double subtotal = hargaSatuan * 1;
            cartModel.addRow(new Object[]{id, judul, hargaRupiahFormatted, 1, formatRupiah(subtotal)});
            updateSubTotal();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memproses item: " + e.getMessage(), "Error Internal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateItemInCart(int cartRow) {
        try {
            int newQty = Integer.parseInt(cartModel.getValueAt(cartRow, 3).toString());
            int id = (Integer) cartModel.getValueAt(cartRow, 0);

            if (newQty <= 0) {
                cartModel.removeRow(cartRow);
                updateSubTotal();
                return;
            }

            int stokTersedia = 0;
            for (int i = 0; i < katalogModel.getRowCount(); i++) {
                if (((Integer) katalogModel.getValueAt(i, 0)) == id) {
                    stokTersedia = (Integer) katalogModel.getValueAt(i, 5);
                    break;
                }
            }

            if (newQty > stokTersedia) {
                JOptionPane.showMessageDialog(this, "Stok buku tidak mencukupi! Maksimum: " + stokTersedia, "Peringatan", JOptionPane.WARNING_MESSAGE);
                newQty = stokTersedia;
                cartModel.setValueAt(newQty, cartRow, 3);
            }

            String hargaRupiah = cartModel.getValueAt(cartRow, 2).toString();
            double hargaSatuan = parseRupiah(hargaRupiah);
            double newSubtotal = hargaSatuan * newQty;

            cartModel.setValueAt(formatRupiah(newSubtotal), cartRow, 4);
            updateSubTotal();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantity harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            cartModel.setValueAt(1, cartRow, 3);
            updateItemInCart(cartRow);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengupdate item.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSubTotal() {
        double total = 0;
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

    private String formatRupiah(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        if (value < 0) {
            return "- " + format.format(-value).replace(",00", "");
        }
        return format.format(value).replace(",00", "");
    }

    private double parseRupiah(String rupiah) throws java.text.ParseException {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        Number number = format.parse(rupiah);
        return number.doubleValue();
    }
}