package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addcourse extends JFrame{
    private JPanel c_add;
    private JTextField c_code;
    private JTextField c_name;
    private JTextField credits;
    private JTextField theory;
    private JTextField practical;
    private JTextField lec_name;
    private JButton addButton;
    Connection con;
    PreparedStatement pst;
    private admindash parent;


    public addcourse(admindash parent) {
        this.parent = parent;

        setTitle("Admin Dashboard");
        setContentPane(c_add);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //  Proper close behavior
        setSize(300,300);
        setVisible(true);


        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.getcourseAsArray();  // Update dashboard when closed
                parent.showcoursesInTextArea();
            }
        });











        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (c_code.getText().equals("")|| c_name.getText().equals("")|| credits.getText().equals("")||theory.getText().equals("")||practical.getText().equals("")) {
                    JOptionPane.showMessageDialog(c_add, "Please fill all the fields");
                    return;
                }
                try {
                    con = dbconnection.getConnection();
                    pst = con.prepareStatement("INSERT INTO `courses` (`course_code`, `course_name`, `credits`, `theory_hours`, `practical_hours`, `lecturer_in_charge`) VALUES (?,?,?,?,?,?)");
                    pst.setString(1, c_code.getText());
                    pst.setString(2, c_name.getText());
                    pst.setInt(3, Integer.parseInt(credits.getText()));
                    pst.setInt(4, Integer.parseInt(theory.getText()));
                    pst.setInt(5, Integer.parseInt(practical.getText()));
                    pst.setString(6, lec_name.getText());
                    int rs = pst.executeUpdate();
                    if(rs ==1){
                        JOptionPane.showMessageDialog(c_add, "Course Added");
                    }
                    else {
                        JOptionPane.showMessageDialog(c_add, "Course Not Added");
                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(c_add, "Enter valid data type");
                }



            }
        });
    }
}
