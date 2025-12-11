package admin.Pengguna;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.User; // Import model User

public class TambahPenggunaDialog extends JDialog {

    private final DefaultTableModel model;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField namaLengkapField;
    private final JComboBox<String> roleBox;

    public TambahPenggunaDialog(Window owner, DefaultTableModel model) {
        super(owner, "Tambah Pengguna Baru", ModalityType.APPLICATION_MODAL);
        this.model = model;

        setLayout(new GridBagLayout());
        if (getContentPane() instanceof JPanel) {
            getContentPane().setBackground(new Color(250, 247, 243));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Header ---
        JLabel titleLabel = new JLabel("Masukkan Detail Pengguna Baru");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // --- Input Fields ---
        gbc.gridwidth = 1;

        // 1. Username
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Username *"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // 2. Password
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password *"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // 3. Nama Lengkap
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Nama Lengkap"), gbc);
        namaLengkapField = new JTextField(20);
        gbc.gridx = 1;
        add(namaLengkapField, gbc);

        // 4. Role
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Role *"), gbc);
        String[] roles = new String[]{"admin", "cashier"};
        roleBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        add(roleBox, gbc);

        // --- Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(250, 247, 243));

        JButton batalBtn = new JButton("Batal");
        JButton tambahBtn = new JButton("Tambah Pengguna");
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
            tambahPenggunaBaru();
        });

        pack();
        setLocationRelativeTo(owner);
    }

    private void tambahPenggunaBaru() {
        // 1. Ambil Data
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String namaLengkap = namaLengkapField.getText().trim();
        String role = roleBox.getSelectedItem().toString();

        // Jika nama kosong, gunakan username
        if (namaLengkap.isEmpty()) {
            namaLengkap = username;
        }

        // 2. Validasi Input Dasar
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password harus diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password minimal 6 karakter.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Buat objek User
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setNamaLengkap(namaLengkap);
        newUser.setRole(role);

        // 4. Simpan ke Database
        boolean success = newUser.createUser();

        if (success) {
            JOptionPane.showMessageDialog(this, "Pengguna '" + username + "' berhasil ditambahkan sebagai " + role + "!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}