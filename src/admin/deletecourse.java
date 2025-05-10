package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class deletecourse extends JFrame {
    private JTextField c_delete;
    private JButton deleteButton;
    private JPanel deletepanel;

    private admindash parent;
    deletecourse(admindash parent) {
        this.parent = parent;

        setTitle("Admin Dashboard");
        setContentPane(deletepanel);
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








        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(c_delete.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter the course name");
                    return;

                }
                try {
                    Connection con= dbconnection.getConnection();
                    PreparedStatement pst = con.prepareStatement("delete from courses where course_code=?");
                    pst.setString(1,c_delete.getText());
                   int result= pst.executeUpdate();
                   if(result == 1 ) {
                       JOptionPane.showMessageDialog(null, "Course has been deleted");
                   }
                   else {
                       JOptionPane.showMessageDialog(null, "Course_id is not found");
                   }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
    }
















}
