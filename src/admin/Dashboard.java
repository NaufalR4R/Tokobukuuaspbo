package admin;

import admin.buku.KelolaBukuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel rootPanel;
    private JButton kelolaBukuButton;
    private JButton kelolaKategoriButton;
    private JButton lihatTransaksiButton;

    public Dashboard() {
        setContentPane(rootPanel);
        setTitle("Dashboard Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        if (kelolaKategoriButton != null) {
            kelolaKategoriButton.addActionListener(e -> {
                JFrame frameKategori = new JFrame("Kelola Kategori");
                frameKategori.setContentPane(new admin.kategori.KelolaKategoriPanel());
                frameKategori.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameKategori.pack();
                frameKategori.setLocationRelativeTo(null);
                frameKategori.setVisible(true);
            });
        } else {
            System.err.println("Peringatan: kelolaKategoriButton belum terhubung ke GUI.");
        }

        lihatTransaksiButton.addActionListener(e -> {
            JFrame frameTransaksi = new JFrame("Lihat Transaksi");
            frameTransaksi.setContentPane(new admin.Transaksi.LihatTransaksiPanel());
            frameTransaksi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frameTransaksi.pack();
            frameTransaksi.setLocationRelativeTo(null);
            frameTransaksi.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard frame = new Dashboard();
            frame.setVisible(true);
        });
    }
}
