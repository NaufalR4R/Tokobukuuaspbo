package admin.kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditKategoriDialog extends JDialog {

    private final DefaultTableModel model;
    private final int rowIndex;
    private final JTextField namaKategoriField;

    public EditKategoriDialog(Window owner, DefaultTableModel model, int rowIndex) {
        super(owner, "Edit Detail Kategori", ModalityType.APPLICATION_MODAL);
        this.model = model;
        this.rowIndex = rowIndex;

        // --- Ambil Data Kategori Saat Ini ---
        String currentId = model.getValueAt(rowIndex, 0).toString();
        String currentNama = model.getValueAt(rowIndex, 1).toString();

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel titleLabel = new JLabel("Edit Kategori ID: " + currentId);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // --- Input Field ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nama Kategori"), gbc);

        namaKategoriField = new JTextField(currentNama, 20);
        gbc.gridx = 1;
        add(namaKategoriField, gbc);

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
            simpanPerubahanKategori();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    private void simpanPerubahanKategori() {
        String newNama = namaKategoriField.getText().trim();

        if (newNama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kategori harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kolom Nama Kategori adalah indeks 1
        model.setValueAt(newNama, rowIndex, 1);

        JOptionPane.showMessageDialog(this, "Kategori '" + newNama + "' berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}