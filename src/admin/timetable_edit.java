package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;

public class timetable_edit extends JFrame {
    private JTextField timetable_find;
    private JButton findButton;
    private JTextField end_time;
    private JTextField location;

    private JTextField lec_id;
    private JTextField c_id;
    private JButton updateButton;

    private JLabel start_time;
    private JTextField s_time;
    private JPanel timetableedit;
    private JComboBox session_type;
    private JComboBox day_of_week;
    Connection con;
    ResultSet rs;
    PreparedStatement pst;
    admindash parent;


    public timetable_edit(admindash parent) {
        this.parent = parent;

        setTitle("Timetabe_edit");
        setContentPane(timetableedit);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500,500);

        setVisible(true);



        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.timetable();
            }
        });


        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timetable_find();

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timetable_update();
            }
        });
    }


    void timetable_find() {

        try {
            con = dbconnection.getConnection();
            if (timetable_find.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter time table_id");
                timetable_find.requestFocus();
                return;

            }

            pst = con.prepareStatement("select * from timetables where timetable_id = ?");
            pst.setString(1, timetable_find.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                c_id.setText(rs.getString("course_id"));
                lec_id.setText(rs.getString("lecturer_id"));
                day_of_week.setSelectedItem(rs.getString("day_of_week"));
                s_time.setText(rs.getString("start_time"));
                end_time.setText(rs.getString("end_time"));
                location.setText(rs.getString("location"));
                session_type.setSelectedItem(rs.getString("session_type"));

            } else {
                JOptionPane.showMessageDialog(this, "Timetable not found",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    void timetable_update() {
        if (c_id.getText().isEmpty() || lec_id.getText().isEmpty() || day_of_week.getSelectedItem() == null || s_time.getText().isEmpty() || end_time.getText().isEmpty() || location.getText().isEmpty() || session_type.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please fill all the required fields",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;

        } else {
            try {
                con = dbconnection.getConnection();
                pst = con.prepareStatement("update timetables set course_id = ?, lecturer_id= ? ,day_of_week = ? , start_time = ? ,end_time = ?,location = ? ,session_type = ? where timetable_id = ?;");
                pst.setInt(1, Integer.parseInt(c_id.getText()));
                pst.setInt(2, Integer.parseInt(lec_id.getText()));
                pst.setString(3, (String) day_of_week.getSelectedItem());

                pst.setTime(4, Time.valueOf(s_time.getText()));
                pst.setTime(5, Time.valueOf(end_time.getText()));
                pst.setString(6, location.getText());
                pst.setString(7, (String) session_type.getSelectedItem());
                pst.setInt(8, Integer.parseInt(timetable_find.getText()));
                int rs = pst.executeUpdate();

                if (rs == 1) {
                    JOptionPane.showMessageDialog(null, "timetable successfully updated");

                } else {
                    JOptionPane.showMessageDialog(null, "timetable not updated");
                }


            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Course ID, Lecturer ID and Timetable ID must be numbers",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Invalid time format. Use HH:MM:SS",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }





        }
    }













    }
