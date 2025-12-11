package admin.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import model.Transaksi;
import model.ItemTransaksi;
import model.ItemDetail;

public class DetailTransaksiDialog extends JDialog {

    private DefaultTableModel detailModel;
    private JTable detailTable;
    private final Transaksi transaksiData;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public DetailTransaksiDialog(Window owner, String idTransaksiStr) {
        super(owner, "Detail Transaksi #" + idTransaksiStr, ModalityType.APPLICATION_MODAL);

        int idTransaksi;
        try {
            idTransaksi = Integer.parseInt(idTransaksiStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(owner, "ID Transaksi tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            this.transaksiData = null;
            dispose();
            return;
        }

        // --- 1. Ambil Data Transaksi Utama ---
        this.transaksiData = Transaksi.getById(idTransaksi);

        if (transaksiData == null) {
            JOptionPane.showMessageDialog(owner, "Data transaksi ID " + idTransaksi + " tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        String tglStr = transaksiData.getCreatedAt() != null ?
                new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(transaksiData.getCreatedAt()) :
                "N/A";
        String userStr = transaksiData.getNama() + " (ID: " + transaksiData.getUserId() + ")";
        String totalStr = currencyFormat.format(transaksiData.getHargaTotal());
        String bayarStr = currencyFormat.format(transaksiData.getJumlahBayar());
        String kembalianStr = currencyFormat.format(transaksiData.getJumlahKembalian());


        setLayout(new BorderLayout(15, 15));
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        // --- Panel Ringkasan Transaksi ---
        JPanel summaryPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Ringkasan Transaksi"));
        summaryPanel.setBackground(new Color(250, 247, 243));

        summaryPanel.add(new JLabel("ID Transaksi:"));
        summaryPanel.add(new JLabel(idTransaksiStr));
        summaryPanel.add(new JLabel("Waktu:"));
        summaryPanel.add(new JLabel(tglStr));
        summaryPanel.add(new JLabel("Pegawai:"));
        summaryPanel.add(new JLabel(userStr));
        summaryPanel.add(new JLabel("Dibayar:"));
        summaryPanel.add(new JLabel(bayarStr));
        summaryPanel.add(new JLabel("Kembalian:"));
        summaryPanel.add(new JLabel(kembalianStr));

        add(summaryPanel, BorderLayout.NORTH);

        // --- Panel Detail Item ---
        String[] detailColumns = {"Judul Buku", "Harga Satuan", "Qty", "Subtotal"};
        detailModel = new DefaultTableModel(detailColumns, 0);
        detailTable = new JTable(detailModel);

        // 2. Muat Data Detail Item dari Database
        loadItemDetails(idTransaksi);

        detailTable.setRowHeight(25);
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Item Dibeli"));

        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Footer ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(250, 247, 243));

        // Total Pembelian Final
        JLabel totalLabel = new JLabel("TOTAL: " + totalStr, SwingConstants.RIGHT);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        footerPanel.add(totalLabel, BorderLayout.CENTER);

        // Tombol Tutup
        JButton closeBtn = new JButton("Tutup");
        closeBtn.addActionListener(e -> dispose());

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.setBackground(new Color(250, 247, 243));
        buttonWrapper.add(closeBtn);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(250, 247, 243));
        southPanel.add(footerPanel, BorderLayout.NORTH);
        southPanel.add(buttonWrapper, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Pengaturan JDialog
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pack();
        setMinimumSize(new Dimension(550, 450));
        setLocationRelativeTo(owner);
    }

    // Mengambil detail item dari database dan mengisi detailModel.
    private void loadItemDetails(int idTransaksi) {
        detailModel.setRowCount(0);

        List<ItemDetail> items = ItemTransaksi.getDetailByTransaksi(idTransaksi);

        for (ItemDetail item : items) {
            String hargaSatuanStr = currencyFormat.format(item.hargaSatuan);
            String subtotalStr = currencyFormat.format(item.subtotal);

            detailModel.addRow(new Object[]{
                    item.judulBuku,
                    hargaSatuanStr,
                    item.jumlah,
                    subtotalStr
            });
        }
    }

    // Method untuk mengatur padding di JDialog
    private void setBorder(javax.swing.border.Border border) {
        if (getContentPane() instanceof JComponent) {
            ((JComponent) getContentPane()).setBorder(border);
        }
    }
}