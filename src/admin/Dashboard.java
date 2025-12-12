package admin;

import admin.buku.KelolaBukuPanel;
import auth.LoginForm;
import model.Buku;
import model.Kategori;
import model.Transaksi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel rootPanel;
    private JButton kelolaBukuButton;
    private JButton kelolaKategoriButton;
    private JButton lihatTransaksiButton;
    private JButton kelolaPenggunaButton;
    private JLabel tfCountBuku;
    private JLabel tfSumStok;
    private JLabel tfCountKategori;
    private JLabel tfCountTransaksi;
    private JLabel tfPendapatan;
    private JButton logOutButton;

    public Dashboard() {
        setContentPane(rootPanel);
        setTitle("Dashboard Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadDashboardData();

        pack();
        setLocationRelativeTo(null);

        kelolaBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frameBuku = new JFrame("Kelola Buku");
                frameBuku.setContentPane(new KelolaBukuPanel());
                frameBuku.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameBuku.pack();
                frameBuku.setLocationRelativeTo(null);
                frameBuku.setVisible(true);
            }
        });
        kelolaKategoriButton.addActionListener(e -> {
            JFrame frameKategori = new JFrame("Kelola Kategori");
            frameKategori.setContentPane(new admin.kategori.KelolaKategoriPanel());
            frameKategori.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameKategori.pack();
            frameKategori.setLocationRelativeTo(null);
            frameKategori.setVisible(true);
        });


        lihatTransaksiButton.addActionListener(e -> {
            JFrame frameTransaksi = new JFrame("Lihat Transaksi");
            frameTransaksi.setContentPane(new admin.Transaksi.LihatTransaksiPanel());
            frameTransaksi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameTransaksi.pack();
            frameTransaksi.setLocationRelativeTo(null);
            frameTransaksi.setVisible(true);
        });

        kelolaPenggunaButton.addActionListener(e -> {
                JFrame framePengguna = new JFrame("Kelola Pengguna");
                framePengguna.setContentPane(new admin.Pengguna.KelolaPenggunaPanel());
                framePengguna.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                framePengguna.pack();
                framePengguna.setLocationRelativeTo(null);
                framePengguna.setVisible(true);;
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int confirm = JOptionPane.showConfirmDialog(Dashboard.this,
                        "Anda yakin ingin Logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    LoginForm lf = new LoginForm();
                    lf.setVisible(true);
                    dispose();
                }
            }
        });
    }

    private void loadDashboardData() {
        // Ambil data dari class Buku
        int countBuku = Buku.countBuku();
        int sumStok = Buku.sumStok();
        int countKategori = Kategori.countKategori();
        int countTransaksi = Transaksi.countTransaksi();
        int sumPendapatan = Transaksi.sumPendapatan();

        // Set teks pada JLabel
        // Periksa apakah tfCountBuku tidak null (penting jika menggunakan designer)
        if (tfCountBuku != null) {
            tfCountBuku.setText(String.valueOf(countBuku));
            tfSumStok.setText(String.valueOf(sumStok));
            tfCountKategori.setText(String.valueOf(countKategori));
            tfCountTransaksi.setText(String.valueOf(countTransaksi));
            tfPendapatan.setText(String.valueOf(sumPendapatan));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard frame = new Dashboard();
            frame.setVisible(true);
        });
    }
}
