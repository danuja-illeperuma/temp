package admin;//import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.*;
import java.util.Vector;


public class usermanagment extends JPanel {
    private JTable table1;
    private JPanel mainpanel;
    private JPanel tablepanel;
    private JPanel btnpanel;



    private JButton add;
    private JButton backButton;
    private JTextField usename;

    public JPanel getMainpanel() {
        return mainpanel;
    }

    private JComboBox selectbox;
    private JPasswordField password;
    private JTextField firstname;
    private JTextField lastname;



    private JTextField email;
    private JTextField phone;
    private JButton uploadButton;
    private JButton deleteButton;
    private JLabel photolabel;
    private JButton update;
    private JButton delete;


    usermanagment() {



       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // setContentPane(mainpanel);
       //setTitle("User_Managment");
      //  setExtendedState(JFrame.MAXIMIZED_BOTH);
     // setVisible(true);

        createtable();







        tableload();
        setborder();
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Adduser();

            }
        });



        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                image_upload();

            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    con = dbconnection.getConnection();
                    pst = con.prepareStatement("DELETE FROM users WHERE user_id = ?");
                    pst.setInt(1,id);
                    int rs = pst.executeUpdate();

                    if (rs ==1) {
                        showMessage("Deleted success","sucess",JOptionPane.INFORMATION_MESSAGE);
                        tableload();
                    }
                    else {
                        showMessage("click the row want to delete from the table","Error",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel d1 = (DefaultTableModel) table1.getModel();
                int selectIndex = table1.getSelectedRow();
                id = Integer.parseInt(d1.getValueAt(selectIndex, 0).toString());
                usename.setText(d1.getValueAt(selectIndex, 1).toString());
                password.setText(d1.getValueAt(selectIndex, 2).toString());
                firstname.setText(d1.getValueAt(selectIndex, 3).toString());
                lastname.setText(d1.getValueAt(selectIndex, 4).toString());
                email.setText(d1.getValueAt(selectIndex, 5).toString());

                try {
                    phone.setText(d1.getValueAt(selectIndex, 6).toString());
                } catch (Exception ex) {

                    phone.setText("");
                }

                Object imageValue = d1.getValueAt(selectIndex, 7);
                if (imageValue == null || "No image".equals(imageValue.toString())) {
                    photolabel.setIcon(null);
                } else if (imageValue instanceof ImageIcon) {
                    ImageIcon ii = (ImageIcon) imageValue;
                    Image image = ii.getImage().getScaledInstance(134, 172, Image.SCALE_SMOOTH);
                    photolabel.setIcon(new ImageIcon(image));
                    connectdb_to_getimagepath();

                }





            }

            //get the current imagepath when use click updae button without uploading a picture. update the current picture again

            void connectdb_to_getimagepath(){
                try {
                    con = dbconnection.getConnection();
                    pst = con.prepareStatement("select profile_picture from users where user_id = ?");
                    pst.setInt(1,id);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        selectedImagePath = rs.getString("profile_picture");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }





        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    con = dbconnection.getConnection();
                    if (usename.getText().isEmpty() || password.getText().isEmpty() || firstname.getText().isEmpty() || lastname.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty()) {
                        showMessage("Please fill all the fields", "Error",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    pst = con.prepareStatement("UPDATE users set username = ?,password = ?,first_name = ?,last_name = ?,email =?,phone = ?,profile_picture = ?,user_type = ? WHERE user_id = ?");

                    pst.setString(1, usename.getText());
                    pst.setString(2, password.getText());
                    pst.setString(3, firstname.getText());
                    pst.setString(4, lastname.getText());
                    pst.setString(5, email.getText());
                    pst.setString(6, phone.getText());
                    pst.setString(7, selectedImagePath);
                    pst.setString(8, selectbox.getSelectedItem().toString());
                    pst.setInt(9,id);
                    int rs = pst.executeUpdate();
                    if (rs == 1)  {
                        showMessage("Data updated","Success",JOptionPane.INFORMATION_MESSAGE);
                        tableload();
                        usename.setText("");
                        password.setText("");
                        firstname.setText("");
                        lastname.setText("");
                        email.setText("");
                        phone.setText("");
                        photolabel.setIcon(null);

                    }
                    else {
                        showMessage("Data updated","Failed",JOptionPane.ERROR_MESSAGE);
                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                photolabel.setIcon(null);
                selectedImagePath = null;
            }
        });
    }

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String path2; //to get the path
    String selectedImagePath = "";
    int id;


    public void tableload() {
        int count;
        try {
            Connection con = dbconnection.getConnection();
            pst = con.prepareStatement("select * from users");
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            count = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) table1.getModel();
            dtm.setRowCount(0);

            while (rs.next()) {
                Vector v2 = new Vector();
                for (int i = 1; i <= count; i++) {
                    v2.add(rs.getString("user_id"));
                    v2.add(rs.getString("username"));
                    v2.add(rs.getString("password"));
                    v2.add(rs.getString("first_name"));
                    v2.add(rs.getString("last_name"));
                    v2.add(rs.getString("email"));
                    v2.add(rs.getString("phone"));

                    // Handle profile picture column
                    String imagePath = rs.getString("profile_picture");
                    if (imagePath != null && !imagePath.isEmpty()) {
                        try {
                            ImageIcon icon = new ImageIcon(imagePath);
                            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            v2.add(new ImageIcon(img));
                        } catch (Exception e) {
                            v2.add("Image not found");
                        }
                    } else {
                        v2.add("No image");
                    }

                    v2.add(rs.getString("user_type"));
                }
                dtm.addRow(v2);
            }

            // Set row height after data is loaded
            table1.setRowHeight(100);

            // Only set column width if the column exists
            if (table1.getColumnModel().getColumnCount() > 7) {
                table1.getColumnModel().getColumn(7).setPreferredWidth(100);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setIcon((ImageIcon) value);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }


    public void Adduser() {
        String username = usename.getText();
        String pass = new String(password.getPassword());
        String dropmenu = selectbox.getSelectedItem().toString();
        String firstn = firstname.getText();
        String lastn = lastname.getText();
        String mail = email.getText();
        String phnum = phone.getText();


        if (username.isEmpty() || pass.isEmpty() || dropmenu.isEmpty() || firstn.isEmpty() || lastn.isEmpty() || mail.isEmpty() || phnum.isEmpty()) {
            showMessage("Fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }
        try {
            con = dbconnection.getConnection();

            pst = con.prepareStatement("select * from users where username = ? ");
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                showMessage("Username already exists", "Error", JOptionPane.ERROR_MESSAGE);

            } else {

                con = dbconnection.getConnection();
                pst = con.prepareStatement("insert into users(username,password,user_type,first_name,last_name,email,phone,profile_picture) values(?,?,?,?,?,?,?,?)");
                pst.setString(1, username);
                pst.setString(2, pass);
                pst.setString(3, dropmenu);
                pst.setString(4, firstn);
                pst.setString(5, lastn);
                pst.setString(6, mail);
                pst.setString(7, phnum);

                pst.setString(8, selectedImagePath);



                int rs = pst.executeUpdate();
                if (rs == 1) {
                    showMessage("succefully added", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    usename.setText("");
                    password.setText("");
                    firstname.setText("");
                    lastname.setText("");
                    email.setText("");
                    phone.setText("");
                    photolabel.setIcon(null);



                    tableload();


                } else {
                    showMessage("Error adding user", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void image_upload() {



        JFileChooser browseImageFile = new JFileChooser("C:\\Users\\Public\\Pictures");
        //Filter image extensions
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES", "png", "jpg", "jpeg");
        browseImageFile.addChoosableFileFilter(fnef);
        int showOpenDialogue = browseImageFile.showOpenDialog(null);

        if (showOpenDialogue == JFileChooser.APPROVE_OPTION) {
            File selectedImageFile = browseImageFile.getSelectedFile();
            selectedImagePath = selectedImageFile.getAbsolutePath();
            JOptionPane.showMessageDialog(null, selectedImagePath);
            //Display image on jlable
            ImageIcon ii = new ImageIcon(selectedImagePath);
//         Resize image to fit jlabel
            Image image = ii.getImage().getScaledInstance(134, 172, Image.SCALE_SMOOTH);

            photolabel.setIcon(new ImageIcon(image));


        }

    }


    public void createtable() {

        //table1.setModel(new javax.swing.table.DefaultTableModel(null, new String[]{"user_id", "username", "password", "firstname", "lastname", "email", "phone","profile_pic", "usertype"}));

        table1.setModel(new javax.swing.table.DefaultTableModel(
                null,
                new String[]{"user_id", "username", "password", "first_name", "last_name", "email", "phone", "profile_picture", "user_type"}
        ));

        // Set the custom renderer for the image column (column 7)
        table1.getColumnModel().getColumn(7).setCellRenderer(new ImageRenderer());


        // Customize table appearance
        table1.setRowHeight(80); // Set row height for images
        table1.setShowGrid(true);
        table1.setGridColor(new Color(230, 230, 230)); // Light gray grid lines
        table1.setIntercellSpacing(new Dimension(5, 5)); // Cell spacing

        // Set column widths
        table1.getColumnModel().getColumn(0).setPreferredWidth(60);  // User ID
        table1.getColumnModel().getColumn(1).setPreferredWidth(100); // Username
        table1.getColumnModel().getColumn(2).setPreferredWidth(80);  // Password
        table1.getColumnModel().getColumn(3).setPreferredWidth(100); // First Name
        table1.getColumnModel().getColumn(4).setPreferredWidth(100); // Last Name
        table1.getColumnModel().getColumn(5).setPreferredWidth(150); // Email
        table1.getColumnModel().getColumn(6).setPreferredWidth(100); // Phone
        table1.getColumnModel().getColumn(7).setPreferredWidth(120); // Photo
        table1.getColumnModel().getColumn(8).setPreferredWidth(80);  // User T



        // Alternate row colors for better readability
        table1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                return c;
            }
        });
    }
























    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    private void setborder(){
        // Set Border
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        photolabel.setBorder(border);
    }


}










