package admin.kategori;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditKategoriDialog extends JDialog {

    private KelolaKategoriPanel parentPanel;
    private int idKategori;
    private int rowIndex;
    private Kategori currentKategori;
    private JTextField namaKategoriField;
    private JTextArea deskripsiArea;

    public EditKategoriDialog(Window owner, KelolaKategoriPanel parentPanel, int idKategori){
        super(owner, "Edit Detail Kategori", ModalityType.APPLICATION_MODAL);
        this.parentPanel = parentPanel;
        this.idKategori = idKategori;

        this.currentKategori = Kategori.getKategoriById(idKategori);

        if (currentKategori == null) {
            JOptionPane.showMessageDialog(owner, "Gagal memuat data kategori ID: " + idKategori, "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // --- Inisialisasi Data dari Objek Kategori ---
        String currentNama = currentKategori.getNama();
        String currentDeskripsi = currentKategori.getDeskripsi() != null ? currentKategori.getDeskripsi() : "";

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel titleLabel = new JLabel("Edit Kategori ID: " + idKategori);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // --- Input Field Nama ---
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nama Kategori"), gbc);

        // Inisialisasi field
        namaKategoriField = new JTextField(currentNama, 20);
        gbc.gridx = 1;
        add(namaKategoriField, gbc);

        // --- Input Field Deskripsi ---
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Deskripsi (Opsional)"), gbc);

        // Inisialisasi field
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
            simpanPerubahanKategori();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    private void simpanPerubahanKategori() {
        String newNama = namaKategoriField.getText().trim();
        String newDeskripsi = deskripsiArea.getText().trim();

        if (newNama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kategori harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Update Objek Kategori
        currentKategori.setNama(newNama);
        currentKategori.setDeskripsi(newDeskripsi);

        // 2. Panggil method UPDATE database
        if (currentKategori.updateKategori()) {
            JOptionPane.showMessageDialog(this, "Kategori '" + newNama + "' berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // 3. Perbarui JTable model
            parentPanel.loadDataFromDatabase();

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui kategori di database. Periksa koneksi atau log sistem.", "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}