package toofficer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddAttendance extends JFrame {
    private JPanel AddAttMain;
    private TextField stu_id;
    private JButton ADDButton, UPDATEButton,DELETEButton,CLEARButton;
    private JLabel c_code_l, c_hourse_L,lec_date_l,att_status_l,c_type_l;
    private JLabel header;
    private JPanel labelPanel;
    private JPanel valuePanel;
    private JComboBox<String> c_code_combo;
    private JComboBox attendance_status_combo;
    private JComboBox c_type_combo;
    private JTextField lec_date;
    private JTextField stu_id_value;
    private JTextField c_hourse_value;
    private Connection conn;

    public AddAttendance() {
       setTitle("Add Attendance");
       setContentPane(AddAttMain);
       setSize(800, 600);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //getContentPane().setBackground(new Color(217,240,250));
       setLocationRelativeTo(null);

       // databaseConnect();
        callCourseCode();

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id_value.getText().equals("") || c_hourse_value.getText().equals("") || lec_date.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "All fields are Required");
                    return;
                }

                try{
                    conn = dbconnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("INSERT INTO attendance_detail (username,c_code,c_hours,lec_date,at_state,c_type) values (?,?,?,?,?,?)");
                    pst.setString(1, stu_id_value.getText() );
                    pst.setString(2, (String) c_code_combo.getSelectedItem() );
                    pst.setString(3, c_hourse_value.getText() );
                    pst.setString(4, lec_date.getText() );
                    pst.setString(5, (String) attendance_status_combo.getSelectedItem() );
                    pst.setString(6, (String)c_type_combo.getSelectedItem() );

                    int row = pst.executeUpdate();
                    if (row > 0) {
                        JOptionPane.showMessageDialog(null, "Attendance Added Successfully");
                    }else {
                        JOptionPane.showMessageDialog(null, "No Records Added");
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Attendance Not Added Successfully");

                }
            }
        });

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    conn = dbconnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement("UPDATE attendance_detail SET lec_date = ? ,at_state = ?  WHERE username = ? AND c_code = ? AND lec_date = ? AND c_type = ?");
                    pst.setString(1, lec_date.getText() );
                    pst.setString(2, (String) attendance_status_combo.getSelectedItem() );
                    pst.setString(3,stu_id_value.getText() );
                    pst.setString(4, (String) c_code_combo.getSelectedItem() );
                    pst.setString(5,lec_date.getText() );
                    pst.setString(6,(String) c_type_combo.getSelectedItem() );


                    int result = pst.executeUpdate();

                    if (result > 0) {
                        JOptionPane.showMessageDialog(null, "Attendance Updated Successfully");

                    }else{
                        JOptionPane.showMessageDialog(null, "No record found");
                    }

                }catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error Updating Attendance. Please try again");
                }
            }
        });

        CLEARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    stu_id_value.setText("");
                    c_code_combo.setSelectedIndex(0);
                    c_hourse_value.setText("");
                    lec_date.setText("");
                    attendance_status_combo.setSelectedItem(0);
                    c_type_combo.setSelectedItem(0);
            }
        });


        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    conn = dbconnection.getConnection();
                    PreparedStatement pst =  conn.prepareStatement("DELETE FROM attendance_detail WHERE at_id = ? ");
                    pst.setString(1, stu_id_value.getText());
                    int delete = pst.executeUpdate();

                    if (delete > 0) {
                        JOptionPane.showMessageDialog(null, "Attendance Deleted Successfully");

                    }else{
                        JOptionPane.showMessageDialog(null, "No record found");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error Deleting Attendance. Please try again");
                }

            }
        });


    }

    private void callCourseCode() {
        try{
            conn = dbconnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT course_code FROM courses;");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                c_code_combo.addItem(rs.getString("course_code"));
            }
            if(c_code_combo.getSelectedItem().equals("")){
                JOptionPane.showMessageDialog(null, "Please enter the course code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }





    public JPanel getAddAttMain() {

        return AddAttMain;
    }


}
