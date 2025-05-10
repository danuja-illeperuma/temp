package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;

public class UProfile extends JFrame {
    private JPanel mainPanel;
    private JPanel profilePanel;
    private JButton changeProfilePictureButton;
    private JButton saveChangesButton;
    private JLabel image;
    private JTextField update_email;
    private JTextField update_contact;
    private JLabel u_details;
    private JLabel fname;
    private JLabel f_name;
    private JLabel lname;
    private JLabel l_name;
    private JLabel email;
    private JLabel mail;
    private JLabel contact;
    private JLabel num;
    private JLabel update_udetails;
    private JLabel email2;
    private JLabel c_num;

    private int userId;

    public UProfile(int userId) throws SQLException {
        this.userId = userId;


        // Initialize the panel (not creating a new JFrame since we'll add this panel to another container)
        mainPanel = new JPanel();
        profilePanel = new JPanel();

        // Initialize UI components
        initComponents();

        // Set layout for panels
        setupLayout();

        // Load user profile data
        loadProfileDetails();

        // Add action listeners
        setupActionListeners();
        setborder();
    }

    private void setborder() {
        Border border = BorderFactory.createLineBorder(Color.black, 2);
        image.setBorder(border);
    }


    private void initComponents() {
        // Initialize all UI components
        changeProfilePictureButton = new JButton("Change Profile Picture");
        saveChangesButton = new JButton("Save Changes");
        image = new JLabel();
        update_email = new JTextField(20);
        update_contact = new JTextField(20);
        u_details = new JLabel("User Details");
        fname = new JLabel("First Name:");
        f_name = new JLabel();
        lname = new JLabel("Last Name:");
        l_name = new JLabel();
        email = new JLabel("Email:");
        mail = new JLabel();
        contact = new JLabel("Contact:");
        num = new JLabel();
        update_udetails = new JLabel("Update User Details");
        email2 = new JLabel("Email:");
        c_num = new JLabel("Contact Number:");
    }

    private void setupLayout() {
        // Set up the layout for the panels
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        profilePanel.setLayout(null); // Using absolute positioning

        // Set positions and sizes for components
        image.setBounds(50, 50, 100, 100);

        u_details.setBounds(50, 170, 200, 25);
        fname.setBounds(50, 210, 100, 25);
        f_name.setBounds(160, 210, 200, 25);
        lname.setBounds(50, 250, 100, 25);
        l_name.setBounds(160, 250, 200, 25);
        email.setBounds(50, 290, 100, 25);
        mail.setBounds(160, 290, 200, 25);
        contact.setBounds(50, 330, 100, 25);
        num.setBounds(160, 330, 200, 25);

        update_udetails.setBounds(50, 380, 200, 25);
        email2.setBounds(50, 420, 100, 25);
        update_email.setBounds(160, 420, 200, 25);
        c_num.setBounds(50, 460, 120, 25);
        update_contact.setBounds(160, 460, 200, 25);

        changeProfilePictureButton.setBounds(50, 520, 180, 30);
        saveChangesButton.setBounds(250, 520, 120, 30);

        // Add components to profilePanel
        profilePanel.add(image);
        profilePanel.add(u_details);
        profilePanel.add(fname);
        profilePanel.add(f_name);
        profilePanel.add(lname);
        profilePanel.add(l_name);
        profilePanel.add(email);
        profilePanel.add(mail);
        profilePanel.add(contact);
        profilePanel.add(num);
        profilePanel.add(update_udetails);
        profilePanel.add(email2);
        profilePanel.add(update_email);
        profilePanel.add(c_num);
        profilePanel.add(update_contact);
        profilePanel.add(changeProfilePictureButton);
        profilePanel.add(saveChangesButton);

        // Add profilePanel to mainPanel
        mainPanel.add(profilePanel);
    }

    private void setupActionListeners() {
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });

        changeProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeProfilePicture();
            }
        });
    }

    // This method returns the profile panel to be added to another container
    public JPanel getProfile() {
        return mainPanel;
    }

    private void loadProfileDetails() throws SQLException {
        // Get connection from dbconnection class
        Connection conn = dbconnection.getConnection();

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Set personal details
                f_name.setText(rs.getString("first_name"));
                l_name.setText(rs.getString("last_name"));
                mail.setText(rs.getString("email"));
                num.setText(rs.getString("phone"));

                // Set profile picture if available
                String imagePath = rs.getString("profile_picture");
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage()
                            .getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
                    image.setIcon(icon);
                } else {
                    // You can set a default image here if needed
                    image.setIcon(null);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        String newEmail = update_email.getText().trim();
        String newContact = update_contact.getText().trim();

        // Validate input
        if (newEmail.isEmpty() && newContact.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter new email or contact information");
            return;
        }

        Connection conn = null;
        try {
            conn = dbconnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET ");
        boolean needComma = false;

        if (!newEmail.isEmpty()) {
            sqlBuilder.append("email = ?");
            needComma = true;
        }

        if (!newContact.isEmpty()) {
            if (needComma) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("phone = ?");
        }

        sqlBuilder.append(" WHERE user_id = ?");

        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;

            if (!newEmail.isEmpty()) {
                stmt.setString(paramIndex++, newEmail);
            }

            if (!newContact.isEmpty()) {
                stmt.setString(paramIndex++, newContact);
            }

            stmt.setInt(paramIndex, userId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Profile updated successfully!");
                // Clear the input fields
                update_email.setText("");
                update_contact.setText("");
                // Reload profile to show updated information
                loadProfileDetails();
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. Please try again.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void changeProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();

            // Update the profile picture path in the database
            Connection conn = null;
            try {
                conn = dbconnection.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String sql = "UPDATE users SET profile_picture = ? WHERE user_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, imagePath);
                stmt.setInt(2, userId);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    // Update the image in the UI
                    ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage()
                            .getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
                    image.setIcon(icon);

                    JOptionPane.showMessageDialog(null, "Profile picture updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update profile picture.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error updating profile picture: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}


