package admin.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailTransaksiDialog extends JDialog {

    private final DefaultTableModel detailModel;
    private final JTable detailTable;

    public DetailTransaksiDialog(Window owner, String idTransaksi) {
        super(owner, "Detail Transaksi #" + idTransaksi, ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout(15, 15));
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        // --- 1. Panel Ringkasan Transaksi (NORTH) ---
        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Ringkasan Transaksi"));
        summaryPanel.setBackground(new Color(250, 247, 243));

        // Data Dummy Ringkasan (Anda akan mengganti ini dengan data aktual dari database)
        String tgl = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date());
        String user = "Admin (ID: 1)";
        String total = "Rp 160.000";
        String kembalian = "Rp 40.000";

        summaryPanel.add(new JLabel("ID Transaksi:"));
        summaryPanel.add(new JLabel(idTransaksi));
        summaryPanel.add(new JLabel("Waktu:"));
        summaryPanel.add(new JLabel(tgl));
        summaryPanel.add(new JLabel("User:"));
        summaryPanel.add(new JLabel(user));
        summaryPanel.add(new JLabel("Kembalian:"));
        summaryPanel.add(new JLabel(kembalian));

        add(summaryPanel, BorderLayout.NORTH);

        // --- 2. Panel Detail Item (CENTER) ---

        // Model untuk detail item
        String[] detailColumns = {"Judul Buku", "Harga Satuan", "Qty", "Subtotal"};
        detailModel = new DefaultTableModel(detailColumns, 0);
        detailTable = new JTable(detailModel);

        // Data Dummy Item (Anda akan mengganti ini dengan hasil query detail transaksi)
        detailModel.addRow(new Object[]{"Laskar Pelangi", "Rp 85.000", 1, "Rp 85.000"});
        detailModel.addRow(new Object[]{"Bumi Manusia", "Rp 75.000", 1, "Rp 75.000"});
        // Total: 160.000

        detailTable.setRowHeight(25);
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Item Dibeli"));

        add(scrollPane, BorderLayout.CENTER);

        // --- 3. Panel Footer (SOUTH) ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(250, 247, 243));

        // Total Pembelian Final
        JLabel totalLabel = new JLabel("TOTAL: " + total, SwingConstants.RIGHT);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        footerPanel.add(totalLabel, BorderLayout.CENTER);

        // Tombol Tutup
        JButton closeBtn = new JButton("Tutup");
        closeBtn.addActionListener(e -> dispose());

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonWrapper.setBackground(new Color(250, 247, 243));
        buttonWrapper.add(closeBtn);

        // Gabungkan total dan tombol
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

    // Method untuk mengatur padding di JDialog
    private void setBorder(javax.swing.border.Border border) {
        if (getContentPane() instanceof JComponent) {
            ((JComponent) getContentPane()).setBorder(border);
        }
    }
}