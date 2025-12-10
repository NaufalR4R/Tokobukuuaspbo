package admin;

import admin.buku.KelolaBukuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JPanel rootPanel;
    private JButton kelolaBukuButton;
    private JButton kelolaKategoriButton;

    public Dashboard() {
        setContentPane(rootPanel);
        setTitle("Dashboard Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        kelolaBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Buat Window (Frame) baru
                JFrame frameBuku = new JFrame("Kelola Buku");
                frameBuku.setContentPane(new KelolaBukuPanel());
                frameBuku.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frameBuku.pack();
                frameBuku.setLocationRelativeTo(null);
                frameBuku.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard frame = new Dashboard();
            frame.setVisible(true);
        });
    }
}
