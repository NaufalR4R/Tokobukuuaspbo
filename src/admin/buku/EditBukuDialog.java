package admin.buku;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditBukuDialog extends JDialog {

    private final DefaultTableModel model;
    private final int rowIndex;
    private final JTextField judulField;
    private final JTextField pengarangField;
    private final JTextField hargaField;
    private final JTextField stokField;
    private final JComboBox<String> kategoriBox;
    private final JTextArea deskripsiArea;

    public EditBukuDialog(Window owner, DefaultTableModel model, int rowIndex) {
        super(owner, "Edit Detail Buku", ModalityType.APPLICATION_MODAL);
        this.model = model;
        this.rowIndex = rowIndex;

        // --- Ambil Data Buku Saat Ini ---
        String currentId = model.getValueAt(rowIndex, 0).toString();
        String currentJudul = model.getValueAt(rowIndex, 1).toString();
        String currentPengarang = model.getValueAt(rowIndex, 2).toString();
        String currentKategori = model.getValueAt(rowIndex, 3).toString();
        String currentHarga = model.getValueAt(rowIndex, 4).toString().replaceAll("[^\\d]", "").trim();
        String currentStok = model.getValueAt(rowIndex, 5).toString();
        String currentDeskripsi = "";

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel titleLabel = new JLabel("Edit Buku ID: " + currentId);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // --- Input Fields ---
        gbc.gridwidth = 1;

        // Judul
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Judul *"), gbc);
        judulField = new JTextField(currentJudul, 20);
        gbc.gridx = 1;
        add(judulField, gbc);

        // Pengarang
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Pengarang *"), gbc);
        pengarangField = new JTextField(currentPengarang, 20);
        gbc.gridx = 1;
        add(pengarangField, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Kategori"), gbc);
        String[] kategoriOptions = new String[]{"Fiksi", "Non-Fiksi", "Teknologi", "Bisnis", "Lainnya"};
        kategoriBox = new JComboBox<>(kategoriOptions);
        kategoriBox.setSelectedItem(currentKategori); // Pilih kategori saat ini
        gbc.gridx = 1;
        add(kategoriBox, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Harga (Rp) *"), gbc);
        hargaField = new JTextField(currentHarga, 10);
        gbc.gridx = 1;
        add(hargaField, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Stok *"), gbc);
        stokField = new JTextField(currentStok, 10);
        gbc.gridx = 1;
        add(stokField, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Deskripsi (Opsional)"), gbc);
        deskripsiArea = new JTextArea(currentDeskripsi, 3, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(deskripsiArea), gbc);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(250, 247, 243));

        JButton batalBtn = new JButton("Batal");
        JButton simpanBtn = new JButton("Simpan Perubahan");
        simpanBtn.setBackground(new Color(80, 60, 40));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFocusPainted(false);

        btnPanel.add(batalBtn);
        btnPanel.add(simpanBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // --- Action Listeners ---
        batalBtn.addActionListener(e -> dispose());

        simpanBtn.addActionListener((ActionEvent e) -> {
            simpanPerubahanBuku();
        });

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    private void simpanPerubahanBuku() {
        String judul = judulField.getText().trim();
        String pengarang = pengarangField.getText().trim();
        String kategori = kategoriBox.getSelectedItem().toString();
        String hargaText = hargaField.getText().trim().replaceAll("[^\\d]", "");
        String stokText = stokField.getText().trim().replaceAll("[^\\d]", "");

        if (judul.isEmpty() || pengarang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Judul dan Pengarang harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int harga;
        int stok;
        try {
            harga = Integer.parseInt(hargaText);
            stok = Integer.parseInt(stokText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka yang valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setValueAt(judul, rowIndex, 1);
        model.setValueAt(pengarang, rowIndex, 2);
        model.setValueAt(kategori, rowIndex, 3);
        model.setValueAt(String.format("Rp %,d", harga), rowIndex, 4); // Re-format harga
        model.setValueAt(stok, rowIndex, 5);

        JOptionPane.showMessageDialog(this, "Buku '" + judul + "' berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}