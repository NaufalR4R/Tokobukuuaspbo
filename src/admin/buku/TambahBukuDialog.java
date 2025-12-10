package admin.buku;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TambahBukuDialog extends JDialog {

    private final DefaultTableModel model; // Simpan model sebagai field

    public TambahBukuDialog(Window owner, DefaultTableModel model) {
        super(owner, "Tambah Buku Baru", ModalityType.APPLICATION_MODAL);
        this.model = model;

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel titleLabel = new JLabel("Tambah Buku Baru");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        gbc.gridwidth = 1;

        // Judul
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Judul *"), gbc);
        JTextField judulField = new JTextField(20);
        gbc.gridx = 1;
        add(judulField, gbc);

        // Pengarang
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Pengarang *"), gbc);
        JTextField pengarangField = new JTextField(20);
        gbc.gridx = 1;
        add(pengarangField, gbc);

        // Kategori
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Kategori"), gbc);
        JComboBox<String> kategoriBox = new JComboBox<>(new String[]{"Fiksi", "Non-Fiksi", "Teknologi", "Bisnis", "Lainnya"});
        gbc.gridx = 1;
        add(kategoriBox, gbc);

        // Harga
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Harga (Rp) *"), gbc);
        JTextField hargaField = new JTextField("0", 10);
        gbc.gridx = 1;
        add(hargaField, gbc);

        // Stok
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Stok *"), gbc);
        JTextField stokField = new JTextField("0", 10);
        gbc.gridx = 1;
        add(stokField, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Deskripsi (Opsional)"), gbc);
        JTextArea deskripsiArea = new JTextArea(3, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(deskripsiArea), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(250, 247, 243));

        JButton batalBtn = new JButton("Batal");
        JButton tambahBtn = new JButton("Tambah Buku");
        tambahBtn.setBackground(new Color(80, 60, 40));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFocusPainted(false);

        btnPanel.add(batalBtn);
        btnPanel.add(tambahBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        batalBtn.addActionListener(e -> dispose());
        tambahBtn.addActionListener((ActionEvent e) -> {
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
            int nextId = getNextBookId(model);

            model.addRow(new Object[]{nextId, judul, pengarang, kategori, String.format("Rp %,d", harga), stok, ""});
            JOptionPane.showMessageDialog(this, "Buku '" + judul + "' berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    // Method untuk mendapatkan ID buku berikutnya (ID terbesar + 1)
    private int getNextBookId(DefaultTableModel model) {
        int maxId = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                // Kolom ID (indeks 0) adalah Integer (berdasarkan KelolaBukuPanel)
                Object idValue = model.getValueAt(i, 0);
                if (idValue instanceof Integer) {
                    int currentId = (Integer) idValue;
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                }
            } catch (Exception ignore) {
                // Abaikan jika ada nilai non-Integer di kolom ID
            }
        }
        return maxId + 1;
    }
}

