package admin.Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditPenggunaDialog extends JDialog {

    private final DefaultTableModel model;
    private final int rowIndex;
    private final JTextField usernameField;
    private final JPasswordField passwordField; // Opsional diisi
    private final JTextField namaLengkapField;
    private final JComboBox<String> roleBox;

    public EditPenggunaDialog(Window owner, DefaultTableModel model, int rowIndex) {
        super(owner, "Edit Detail Pengguna", ModalityType.APPLICATION_MODAL);
        this.model = model;
        this.rowIndex = rowIndex;

        // --- Ambil Data Pengguna Saat Ini ---
        String currentId = model.getValueAt(rowIndex, 0).toString();
        String currentUsername = model.getValueAt(rowIndex, 1).toString();
        String currentNamaLengkap = model.getValueAt(rowIndex, 2).toString();
        String currentRole = model.getValueAt(rowIndex, 3).toString();

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel titleLabel = new JLabel("Edit Pengguna ID: " + currentId);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // --- Input Fields ---
        gbc.gridwidth = 1;

        // 1. Username (Biasanya tidak diizinkan diubah, atau butuh validasi unik)
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Username *"), gbc);
        usernameField = new JTextField(currentUsername, 20);
        usernameField.setEditable(false); // Opsional: Jadikan Username tidak bisa diubah
        gbc.gridx = 1;
        add(usernameField, gbc);

        // 2. Password (Dibiarkan kosong jika tidak ingin diubah)
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password (kosongkan jika tidak diubah)"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // 3. Nama Lengkap
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nama Lengkap"), gbc);
        namaLengkapField = new JTextField(currentNamaLengkap, 20);
        gbc.gridx = 1;
        add(namaLengkapField, gbc);

        // 4. Role
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Role *"), gbc);
        String[] roles = new String[]{"admin", "cashier"};
        roleBox = new JComboBox<>(roles);
        roleBox.setSelectedItem(currentRole); // Pilih role pengguna saat ini
        gbc.gridx = 1;
        add(roleBox, gbc);

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
            simpanPerubahanPengguna();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    private void simpanPerubahanPengguna() {
        // 1. Ambil Data
        String newUsername = usernameField.getText().trim(); // Tidak diubah jika setEditable(false)
        String newPassword = new String(passwordField.getPassword()).trim();
        String newNamaLengkap = namaLengkapField.getText().trim();
        String newRole = roleBox.getSelectedItem().toString();

        // 2. Validasi Input Dasar
        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username tidak boleh kosong.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi Password hanya jika diisi
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password baru minimal 6 karakter.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Perbarui Baris di Tabel (Update Row in Model)
        // Kolom: {"ID", "Username", "Nama Lengkap", "Role", "Tanggal Buat", "Aksi"}

        // ID (Kolom 0) tidak diubah
        model.setValueAt(newUsername, rowIndex, 1);
        model.setValueAt(newNamaLengkap.isEmpty() ? newUsername : newNamaLengkap, rowIndex, 2);
        model.setValueAt(newRole, rowIndex, 3);
        // Kolom 4 (Tanggal Buat) tidak diubah

        // Catatan: Password tidak di-update di JTable karena JTable tidak menyimpan password

        JOptionPane.showMessageDialog(this, "Pengguna '" + newUsername + "' berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        // 4. Tutup dialog setelah berhasil
        dispose();

        // Logika database: Panggil fungsi untuk update database (termasuk hash password baru jika diisi)
    }
}
