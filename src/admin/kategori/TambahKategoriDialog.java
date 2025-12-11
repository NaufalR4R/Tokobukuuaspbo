package admin.kategori;

import model.Kategori;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TambahKategoriDialog extends JDialog {

    private final KelolaKategoriPanel parentPanel;
    private final JTextField namaKategoriField;
    private final JTextArea deskripsiArea;

    public TambahKategoriDialog(Window owner, KelolaKategoriPanel parentPanel) {
        super(owner, "Tambah Kategori Baru", ModalityType.APPLICATION_MODAL);
        this.parentPanel = parentPanel;

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

        // --- Input Field Nama Kategori ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nama Kategori *"), gbc);

        namaKategoriField = new JTextField(20);
        namaKategoriField.setText("");
        gbc.gridx = 1;
        add(namaKategoriField, gbc);

        // --- Input Field Deskripsi ---
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Deskripsi (Opsional)"), gbc);

        deskripsiArea = new JTextArea(3, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(deskripsiArea), gbc);

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
        String newDeskripsi = deskripsiArea.getText().trim();

        if (newNama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kategori harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Buat objek Kategori
        Kategori newKategori = new Kategori();
        newKategori.setNama(newNama);
        newKategori.setDeskripsi(newDeskripsi);

        // 2. Simpan ke Database
        if (newKategori.createKategori()) {
            JOptionPane.showMessageDialog(this, "Kategori '" + newNama + "' berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            parentPanel.loadDataFromDatabase();

            dispose();
        }
    }
}