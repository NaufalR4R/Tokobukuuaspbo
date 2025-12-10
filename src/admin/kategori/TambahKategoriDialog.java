package admin.kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TambahKategoriDialog extends JDialog {

    private final DefaultTableModel model;
    private final JTextField namaKategoriField;

    public TambahKategoriDialog(Window owner, DefaultTableModel model) {
        super(owner, "Tambah Kategori Baru", ModalityType.APPLICATION_MODAL);
        this.model = model;

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel titleLabel = new JLabel("Tambah Kategori Baru");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // --- Input Field ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nama Kategori"), gbc);

        namaKategoriField = new JTextField(20);
        namaKategoriField.setText(""); // Pastikan field kosong saat dibuka
        gbc.gridx = 1;
        add(namaKategoriField, gbc);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(250, 247, 243));

        JButton batalBtn = new JButton("Batal");
        JButton tambahBtn = new JButton("Tambah Kategori");
        tambahBtn.setBackground(new Color(80, 60, 40));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFocusPainted(false);

        btnPanel.add(batalBtn);
        btnPanel.add(tambahBtn);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        // --- Action Listeners ---
        batalBtn.addActionListener(e -> dispose());

        tambahBtn.addActionListener((ActionEvent e) -> {
            tambahKategoriBaru();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    private void tambahKategoriBaru() {
        String newNama = namaKategoriField.getText().trim();

        if (newNama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kategori harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Generate ID Baru (Mencari ID terbesar dari tabel kategori)
        int nextId = getNextKategoriId(model);

        // 2. Tambahkan ke Tabel
        // Kolom: {"ID", "Nama Kategori", "Jumlah Buku", "Aksi"}

        model.addRow(new Object[]{
                nextId,
                newNama,
                "0 buku", // Kategori baru, jumlah buku default 0
                "" // Kolom Aksi
        });

        JOptionPane.showMessageDialog(this, "Kategori '" + newNama + "' berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    // Method untuk mendapatkan ID kategori berikutnya (ID terbesar + 1)
    private int getNextKategoriId(DefaultTableModel model) {
        int maxId = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                // Kolom ID adalah indeks 0
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