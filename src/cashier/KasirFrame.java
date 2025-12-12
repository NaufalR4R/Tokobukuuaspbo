package cashier; // Buat package baru untuk Kasir

import javax.swing.*;
import auth.LoginForm;

public class KasirFrame extends JFrame {

    public KasirFrame(int userId) {
        setTitle("Sistem Kasir - Toko Buku ");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Muat KasirPanel sebagai konten utama
        KasirPanel kasirPanel = new KasirPanel(this, userId);
        setContentPane(kasirPanel);

        // Membuat menu bar sederhana (Opsional: untuk Keluar/Logout)
        JMenuBar menuBar = new JMenuBar();
        JMenu menuAksi = new JMenu("Aksi");
        JMenuItem logoutItem = new JMenuItem("Logout");

        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Anda yakin ingin Logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                LoginForm lf = new LoginForm();
                lf.setVisible(true);
                dispose();
            }
        });

        menuAksi.add(logoutItem);
        menuBar.add(menuAksi);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Jalankan aplikasi di Event Dispatch Thread (rekomendasi Swing)
        SwingUtilities.invokeLater(() -> {
            new KasirFrame(1);
        });
    }
}