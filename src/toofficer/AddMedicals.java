package toofficer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.sql.*;

public class AddMedicals extends JFrame {
    private JTextField m_stu_id,s_description,sub_date,m_c_code;
    private JButton DELETEButton,ADDButton,CLEARButton,UPDATEButton;
    private JLabel medi_photo;
    private JPanel add_panel;
    private JComboBox c_type_combo;
    private JComboBox m_status_combo;


    private Connection conn;

    public AddMedicals() {
        setTitle("Add Medicals");
        setContentPane(add_panel);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
       // getContentPane().setBackground(new Color(217, 240, 250));

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = dbconnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("DELETE FROM medical WHERE medical_id = ? ");
                    pst.setString(1, m_stu_id.getText());
                    int remove = pst.executeUpdate();

                    if (remove > 0) {
                        JOptionPane.showMessageDialog(null, "Medical has been deleted successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "No deleted medical found");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error deleting medical..");
                }
            }
        });


        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student_id = m_stu_id.getText().trim();
                String code = m_c_code.getText().trim();
                String date = sub_date.getText().trim();

                if (student_id.isEmpty() || s_description.getText().isEmpty() || date.isEmpty() || code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the required fields");
                    return;
                }


                try {
                    conn = dbconnection.getConnection();
                    String findAttendanceID = "SELECT at_id FROM attendance_detail WHERE username = ? AND c_code = ? AND lec_date = ? AND c_type = ?";
                    PreparedStatement ps = conn.prepareStatement(findAttendanceID);
                    ps.setString(1, student_id);
                    ps.setString(2, code);
                    ps.setString(3, date);
                    ps.setString(4, c_type_combo.getSelectedItem().toString());
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        int a_id = rs.getInt("at_id");
                        String updateAttendance = "UPDATE attendance_detail SET at_state = 'Medical' WHERE at_id = ?";
                        PreparedStatement ps2 = conn.prepareStatement(updateAttendance);
                        ps2.setInt(1, a_id);
                        ps2.executeUpdate();

                        String addMedical = "INSERT INTO medical (attendance_id ,username,description,sub_date,state,c_code,c_type) VALUES (?,?,?,?,?,?,?)";
                        PreparedStatement ps3 = conn.prepareStatement(addMedical);
                        ps3.setInt(1, a_id);
                        ps3.setString(2, student_id);
                        ps3.setString(3, s_description.getText());
                        ps3.setString(4, date);
                        ps3.setString(5, m_status_combo.getSelectedItem().toString());
                        ps3.setString(6, m_c_code.getText());
                        ps3.setString(7, c_type_combo.getSelectedItem().toString());

                        int add = ps3.executeUpdate();
                        if (add > 0) {
                            JOptionPane.showMessageDialog(null, "Medical has been added successfully");
                        } else {
                            JOptionPane.showMessageDialog(null, "No added medical found");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No matching attendance found");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding medical..");
                }

            }
        });
        CLEARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                m_stu_id.setText("");
                s_description.setText("");
                sub_date.setText("");
                m_status_combo.setSelectedIndex(0);
                m_c_code.setText("");
                c_type_combo.setSelectedIndex(0);
                medi_photo.setIcon(null);
                medi_photo.setText("No Photo");

            }
        });


        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conn = dbconnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("UPDATE medical SET state = ?  WHERE c_code = ? AND username = ? ");
                    pst.setString(1, m_status_combo.getSelectedItem().toString());

                    pst.setString(2, m_c_code.getText());
                    pst.setString(3, m_stu_id.getText());

                    int update = pst.executeUpdate();
                    if (update > 0) {
                        JOptionPane.showMessageDialog(null, "Medical updated successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "No updated medical found");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error updating medical..");
                }
            }
        });



        /*UPLOADIMAGEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser("C:\\Users\\danuja\\Pictures");
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("Image File", "jpg", "png");
                chooser.addChoosableFileFilter(fnef);
                int showOpenDialog = chooser.showOpenDialog(null);

                if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                    File selectedImageFile = chooser.getSelectedFile();
                    imgpath = selectedImageFile.getAbsolutePath();
                    JOptionPane.showMessageDialog(null, imgpath);

                    ImageIcon ii = new ImageIcon(imgpath);
                    Image image = ii.getImage().getScaledInstance(135, 172, Image.SCALE_SMOOTH);
                    medi_photo.setIcon(new ImageIcon(image));
                }
            }

        });*/
    }


    public JPanel getAdd_panel() {

        return add_panel;
    }
}

