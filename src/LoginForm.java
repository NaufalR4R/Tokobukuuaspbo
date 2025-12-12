import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import admin.Dashboard;
import cashier.KasirFrame;
import utils.Koneksi;


public class LoginForm extends JFrame{
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
                        String role = rs.getString("role");
                        int userId = rs.getInt("id");
                        if (rs.getString("role").equals("admin")) {
//                            JOptionPane.showMessageDialog(null, "login berhasil");
                            txtUsername.setText("");
                            txtPassword.setText("");
                            txtUsername.requestFocus();
                            Dashboard da = new Dashboard();
                            da.setVisible(true);
                            LoginForm.this.dispose();

                        }else if(rs.getString("role").equals("cashier")){
                            txtUsername.setText("");
                            txtPassword.setText("");
                            txtUsername.requestFocus();
                            KasirFrame kf = new KasirFrame(userId);
                            kf.setVisible(true);
                            LoginForm.this.dispose();
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
        LoginForm login = new LoginForm();
        login.setTitle("Login Form");
        login.setContentPane(login.rootPanel);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.pack();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
}
