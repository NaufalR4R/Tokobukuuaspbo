package admin.Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

import model.User; // Import model User

public class EditPenggunaDialog extends JDialog {

    private final DefaultTableModel model;
    private final int rowIndex;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField namaLengkapField;
    private JComboBox<String> roleBox;
    private User currentUser; // Objek User untuk menyimpan data lengkap (termasuk password lama)
    private final int currentId;

    public EditPenggunaDialog(Window owner, DefaultTableModel model, int rowIndex) {
        super(owner, "Edit Detail Pengguna", ModalityType.APPLICATION_MODAL);
        this.model = model;
        this.rowIndex = rowIndex;

        // Ambil ID dari JTable
        String idStr = model.getValueAt(rowIndex, 0).toString();
        this.currentId = Integer.parseInt(idStr);

        // 1. Ambil Data Pengguna Lengkap dari Database
        this.currentUser = User.getUserById(currentId);

        if (currentUser == null) {
            JOptionPane.showMessageDialog(owner, "Gagal memuat data pengguna ID: " + currentId + ". Data mungkin sudah dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // --- Setup UI ---
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

        // 1. Username
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Username *"), gbc);
        // Menggunakan data dari objek currentUser
        usernameField = new JTextField(currentUser.getUsername(), 20);
        usernameField.setEditable(false);
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
        namaLengkapField = new JTextField(currentUser.getNamaLengkap(), 20);
        gbc.gridx = 1;
        add(namaLengkapField, gbc);

        // 4. Role
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Role *"), gbc);
        String[] roles = new String[]{"admin", "cashier"};
        roleBox = new JComboBox<>(roles);
        roleBox.setSelectedItem(currentUser.getRole());
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
        String newPassword = new String(passwordField.getPassword()).trim();
        String newNamaLengkap = namaLengkapField.getText().trim();
        String newRole = roleBox.getSelectedItem().toString();

        // 2. Validasi Input Dasar
        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password baru minimal 6 karakter.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Update Objek User
        currentUser.setNamaLengkap(newNamaLengkap.isEmpty() ? currentUser.getUsername() : newNamaLengkap);
        currentUser.setRole(newRole);

        if (!newPassword.isEmpty()) {
            currentUser.setPassword(newPassword);
        }

        // 4. Simpan ke Database
        if (currentUser.updateUser()) {

            // 5. Perbarui Baris di Tabel (JTable) jika update DB berhasil
            model.setValueAt(currentUser.getUsername(), rowIndex, 1);
            model.setValueAt(currentUser.getNamaLengkap(), rowIndex, 2);
            model.setValueAt(currentUser.getRole(), rowIndex, 3);

            JOptionPane.showMessageDialog(this, "Pengguna '" + currentUser.getUsername() + "' berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }
}