package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;

public class timetable_add extends JFrame {
    private JTextField c_id;
    private JTextField lec_id;

    private JTextField s_time;
    private JTextField e_time;
    private JTextField location;

    private JButton addButton;
    private JPanel timetablepanel;
    private JComboBox session;
    private JComboBox day_of_week;
    admindash parent;

    public timetable_add(admindash parent) {
        this.parent =parent;

        setTitle("Timetabe_edit");
        setContentPane(timetablepanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300,300);

        setVisible(true);

        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.timetable();
            }
        });






        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (c_id.getText().isEmpty() || lec_id.getText().isEmpty() || day_of_week.getSelectedItem() == null || s_time.getText().isEmpty() || e_time.getText().isEmpty() || location.getText().isEmpty() || session.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "please fill the all fields");
                    return;
                }
                try {
                    Connection con = dbconnection.getConnection();
                    PreparedStatement pst = con.prepareStatement("insert into timetables(course_id,lecturer_id,day_of_week,start_time,end_time,location,session_type) values(?,?,?,?,?,?,?)");
                    pst.setInt(1, Integer.parseInt(c_id.getText()));
                    pst.setInt(2, Integer.parseInt(lec_id.getText()));
                    pst.setString(3, (String) day_of_week.getSelectedItem());
                    pst.setTime(4, Time.valueOf(s_time.getText()));
                    pst.setTime(5, Time.valueOf(e_time.getText()));
                    pst.setString(6, location.getText());
                    pst.setString(7, (String) session.getSelectedItem());
                    int rs = pst.executeUpdate();

                    if (rs == 1) {
                        JOptionPane.showMessageDialog(null, "data added sucessfully added");
                    } else
                        JOptionPane.showMessageDialog(null, "data added fail");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Course ID, Lecturer ID must be numbers",
                            "Error", JOptionPane.ERROR_MESSAGE);

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid time format. Use HH:MM:SS",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }





            }
        });
    }



}
