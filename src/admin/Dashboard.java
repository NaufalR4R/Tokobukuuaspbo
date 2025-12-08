package admin;

import javax.swing.*;

public class Dashboard extends JFrame{
    private JButton kelolaKategoriButton;
    private JPanel rootPanel;

    public Dashboard(){
        setContentPane(rootPanel);

        setTitle("Dashboard Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dashboard");
        frame.setContentPane(new Dashboard().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
