package admin;

import student.Undergraduate;
import toofficer.TechOfficer;
import lecturer.Lecturer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginform extends JFrame {


    private JButton loginButton;
    private JTextField Username;
    private JPanel loginpanel;
    private JPasswordField Password;


    public loginform() {


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(loginpanel);
        setTitle("User_login");
        setSize(450, 450);
        setVisible(true);


        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });


        // Allow pressing Enter in password field to log
        Password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = Username.getText().trim();
        String password = new String(Password.getPassword());

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check credentials against database
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND BINARY password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password); // In real application, use hashed passwords

            ResultSet rs = stmt.executeQuery();


            // 4. Check if user exists
            if (rs.next()) {
                String userType = rs.getString("user_type");
                int userId = rs.getInt("user_id");

                String user_name = rs.getString(("username"));
                showMessage("Welcome, " + username.toLowerCase() + "! You are logged in as " + userType,
                        "Login Successful", JOptionPane.INFORMATION_MESSAGE);

                // Here we would normally open the appropriate dashboard
                // For now, we'll just show a success message

                // Clear fields after successful login
                Username.setText("");
                Password.setText("");
                switch (userType.toLowerCase()) {
                    case "admin":
                        dispose();
                        new admindash(userId);
                        break;

                    case "student":
                        dispose();
                        new Undergraduate(user_name,userId);
                        break;

                    case "technical_officer":
                        dispose();
                        new TechOfficer(userId);
                        break;

                    case "lecturer":
                        dispose();
                        new Lecturer(userId);
                        break;




                    default:
                        showMessage("Invalid user type", "Login Error", JOptionPane.ERROR_MESSAGE);
                        new loginform().setVisible(true);


                }


            } else {


                showMessage("Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);

            }

            // 5. Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }


}