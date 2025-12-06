import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;


public class LoginForm {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton loginButton;
    private JPanel rootPanel;

    public LoginForm() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Connection kon = null;
                try {
                    kon = Koneksi.getConnection();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Statement st = kon.createStatement();
                    String sql = "SELECT * FROM users WHERE username ='" + txtUsername.getText() + "' AND password ='" + txtPassword.getText() + "'";

                    ResultSet rs = st.executeQuery(sql);
                    if (rs.next()) {
                        if (rs.getString("role").equals("admin")) {
                            JOptionPane.showMessageDialog(null, "login berhasil");
                            txtUsername.setText("");
                            txtPassword.setText("");
                            txtUsername.requestFocus();
//                            dashboardAdmin da = new dashboardAdmin();
//
//                            da.show();
//                            this.dispose();
                        }else if(rs.getString("eole").equals("staff")){
//                            dashboardStaff ds = new dashboardStaff();
//
//                            ds.show();
//                            this.dispose();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Maaf login gagal, password atau username salah");
                        txtUsername.setText("");
                        txtPassword.setText("");
                        txtUsername.requestFocus();


                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginForm");
        frame.setContentPane(new LoginForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
