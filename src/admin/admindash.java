package admin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class admindash extends JFrame {
    private JButton userButton;
    private JPanel adminpanel;
    private JTabbedPane tabbedPane1;
    private JPanel users;
    private JButton updateButton;
    private JLabel photolabel;
    private JTextField uname;
    private JTextField pass;
    private JTextField fname;
    private JTextField laname;
    private JTextField email;
    private JTextField phone;
    private JLabel username;
    private JButton uploadButton;
    private JButton deleteButton;
    private JButton logOutButton;
    private JTextArea textArea1;
    private JButton addButton;
    private JButton c_editButton;
    private JButton deleteButton1;
    private JTextArea textArea2;
    private JButton addButton1;
    private JButton updateButton1;
    private JButton deleteButton3;
    private JTable table1;
    private JButton ADDButton;
    private JButton editButton;
    private JButton deleteButton2;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String selectedImagePath = "";
    int userid;


    public admindash(int userid) {
        this.userid = userid;

        setTitle("Admin Dashboard");
        setContentPane(adminpanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);


        initializeUserManagementTab();// Initialize the user management tab
        setborder();
        profileshow();
        getcourseAsArray();
        showcoursesInTextArea();
        noticedetail();
        displaynotice();
        createtable();
        timetable();

        setVisible(true);


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (email.getText().isEmpty() || uname.getText().isEmpty() || fname.getText().isEmpty() || laname.getText().isEmpty() || pass.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                } else {
                    try {
                        con = dbconnection.getConnection();
                        pst = con.prepareStatement("update users set email = ?, password = ? ,phone = ? , profile_picture = ? ,first_name = ?,last_name = ? where user_id = ?");
                        pst.setString(1, email.getText());
                        pst.setString(2, pass.getText());
                        pst.setString(3, phone.getText());

                        pst.setString(4, selectedImagePath);
                        pst.setString(5, fname.getText());
                        pst.setString(6, laname.getText());
                        pst.setInt(7, userid);
                        int rs = pst.executeUpdate();

                        if (rs == 1) {
                            JOptionPane.showMessageDialog(null, "User successfully updated");
                        } else {
                            JOptionPane.showMessageDialog(null, "User not updated");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }

            }
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


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
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                photolabel.setIcon(null);
                selectedImagePath = "";
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        c_editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new course_edit(admindash.this); //Pass the CURRENT dashboard
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addcourse(admindash.this);
            }
        });
        deleteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new deletecourse(admindash.this);
            }
        });
        addButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new noticeadd(admindash.this);
            }
        });
        updateButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new noticeup(admindash.this);
            }
        });
        deleteButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new noticedelete(admindash.this);
            }
        });
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new timetable_add(admindash.this);

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new timetable_edit(admindash.this);
            }
        });
        deleteButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new timetable_delete(admindash.this);
            }
        });
    }

    private void setborder() {
        // Set Border
        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        photolabel.setBorder(border);
    }


    private void initializeUserManagementTab() {
        // Remove all existing components
        users.removeAll();

        // Set a proper layout manager (BorderLayout is most common for tab content)
        users.setLayout(new BorderLayout());

        // Add the user management panel
        usermanagment userPanel = new usermanagment();
        users.add(userPanel.getMainpanel(), BorderLayout.CENTER);

        // Refresh the UI
        users.revalidate();
        users.repaint();
    }


    void profileshow() {

        try {
            con = dbconnection.getConnection();
            pst = con.prepareStatement("select * from users where user_id = ?");
            pst.setInt(1, userid);
            rs = pst.executeQuery();
            if (rs.next()) {
                uname.setText(rs.getString("username"));
                pass.setText(rs.getString("password"));
                fname.setText(rs.getString("first_name"));
                laname.setText(rs.getString("last_name"));
                email.setText(rs.getString("email"));
                phone.setText(rs.getString("phone"));
                selectedImagePath = rs.getString("profile_picture");
                //Display image on jlable
                ImageIcon ii = new ImageIcon(selectedImagePath);
//         Resize image to fit jlabel
                Image image = ii.getImage().getScaledInstance(134, 172, Image.SCALE_SMOOTH);

                photolabel.setIcon(new ImageIcon(image));

                //startup message

                String unamestarup = rs.getString("first_name");
                username.setText("Hello "+unamestarup);


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    //courses


    public ArrayList<String> getcourseAsArray() {
        ArrayList<String> courseArray = new ArrayList<>();

        try {
            con = dbconnection.getConnection();
            String sql = "SELECT course_code, course_name, lecturer_in_charge FROM courses";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String course_code = rs.getString("course_code");
                String course_name = rs.getString("course_name");
                String lecture = rs.getString("lecturer_in_charge");

                String formattedcourse = course_code + "  " + course_name + "  " + lecture + "\n-----------------------------\n";
                courseArray.add(formattedcourse);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courseArray;
    }

    public void showcoursesInTextArea() {
        ArrayList<String> courses = getcourseAsArray();
        StringBuilder allcourses = new StringBuilder();

        // Add a header if needed
        allcourses.append("=== Course BOARD ===\n\n");

        for (String course : courses) {
            allcourses.append(course).append("\n");
        }

        textArea1.setText(allcourses.toString());
    }


    //notice


    public ArrayList<String> noticedetail() {
        ArrayList<String> notice = new ArrayList<>();


        try {
            con = dbconnection.getConnection();
            String query = "SELECT * FROM notices ORDER BY publish_date DESC ";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {

                String title = rs.getString("title");
                String n_id = rs.getString("notice_id");
                String content = rs.getString("content");
                String publish_date = rs.getString("publish_date");


                String dataEntry = n_id + "-  " + publish_date + "   " + content + "  " + title + "  " + "\n*******************************\n";
                notice.add(dataEntry);
            }
        } catch (SQLException e) {
            System.err.println("Database error while fetching courses: " + e.getMessage());
        } finally {
            // Ensure resources are closed
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }

        return notice;
    }


    public void displaynotice() {
        ArrayList<String> noticedisplay = noticedetail();
        StringBuilder displayText = new StringBuilder();

        // Add a header if needed
        displayText.append("=== NOTICE BOARD ===\n\n");

        for (String notice : noticedisplay) {
            displayText.append(notice).append("\n");
            ;
        }
        textArea2.setText(displayText.toString()); // Trim to remove extra newlines
    }


    //timetable

    void createtable() {
        table1.setModel(new javax.swing.table.DefaultTableModel(null, new String[]{"Timetable_id","Course_id", "course_name", "Lecture_id", "Lecture_name", "day_of_week", "start_time", "end_time", "location", "session_type"}));


    }


    void timetable() {
        try {
            con = dbconnection.getConnection();
            pst = con.prepareStatement("SELECT \n" +
                    "    t.timetable_id,\n" +
                    "    t.course_id,\n" +
                    "    c.course_name,\n" +
                    "    t.lecturer_id,\n" +
                    "    u.first_name AS lecturer_name,\n" +
                    "    t.day_of_week,\n" +
                    "    t.start_time,\n" +
                    "    t.end_time,\n" +
                    "    t.location,\n" +
                    "    t.session_type\n" +
                    "FROM \n" +
                    "    timetables t\n" +
                    "INNER JOIN \n" +
                    "    courses c ON t.course_id = c.course_id\n" +
                    "INNER JOIN \n" +
                    "    users u ON t.lecturer_id = u.user_id\n" +
                    "ORDER BY \n" +
                    "    t.day_of_week, t.start_time;");


            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            DefaultTableModel dtm = (DefaultTableModel) table1.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();
                for (int i = 1; i <= count; i++) {
                    v2.add(rs.getString("timetable_id"));
                    v2.add(rs.getString("course_id"));
                    v2.add(rs.getString("course_name"));
                    v2.add(rs.getString("lecturer_id"));
                    v2.add(rs.getString("lecturer_name"));
                    v2.add(rs.getString("day_of_week"));
                    v2.add(rs.getString("start_time"));
                    v2.add(rs.getString("end_time"));
                    v2.add(rs.getString("location"));
                    v2.add(rs.getString("session_type"));
                }
                dtm.addRow(v2);

            }
            table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto-resize
            table1.getColumnModel().getColumn(0).setPreferredWidth(80);
            table1.getColumnModel().getColumn(1).setPreferredWidth(80);  // course_id
            table1.getColumnModel().getColumn(2).setPreferredWidth(200); // course_name
            table1.getColumnModel().getColumn(3).setPreferredWidth(80);  // lecturer_id
            table1.getColumnModel().getColumn(4).setPreferredWidth(150); // lecturer_name
            table1.getColumnModel().getColumn(5).setPreferredWidth(100); // day_of_week
            table1.getColumnModel().getColumn(6).setPreferredWidth(100); // start_time
            table1.getColumnModel().getColumn(7).setPreferredWidth(100); // end_time
            table1.getColumnModel().getColumn(8).setPreferredWidth(150); // location
            table1.getColumnModel().getColumn(9).setPreferredWidth(100); // session_type


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}































