package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class course_edit extends JFrame {
    private JTextField c_find;
    private JButton findButton;
    private JTextField c_code;
    private JTextField c_name;
    private JTextField credit;
    private JTextField theory;
    private JTextField phour;
    private JTextField lecname;
    private JButton applyButton;
    private JPanel c_main;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private  admindash parentdashboard;

    public course_edit(admindash parent) {
       this.parentdashboard = parent;
        setTitle("Admin Dashboard");
        setContentPane(c_main);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //  Proper close behavior
        setSize(500,500);

        setVisible(true);


        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parentdashboard.getcourseAsArray();  // Update dashboard when closed
                parentdashboard.showcoursesInTextArea();
            }
        });








        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course_find();

            }
        });
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course_update();


            }
        });
    }


    void course_find(){

        try {
            con = dbconnection.getConnection();
            if(c_find.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please enter the course code");
                c_find.requestFocus();
                return;

            }

            pst = con.prepareStatement("select * from courses where course_code = ?");
            pst.setString(1, c_find.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                c_code.setText(rs.getString("course_code"));
                c_name.setText(rs.getString("course_name"));
                credit.setText(rs.getString("credits"));
                theory.setText(rs.getString("theory_hours"));
                phour.setText(rs.getString("practical_hours"));
                lecname.setText(rs.getString("lecturer_in_charge"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    void course_update(){
        if (c_code.getText().isEmpty() || c_name.getText().isEmpty()|| credit.getText().isEmpty() || theory.getText().isEmpty()||phour.getText().isEmpty() ) {
            JOptionPane.showMessageDialog(null, "Please fill all the fields");
            return;
        } else {
            try {
                con = dbconnection.getConnection();
                pst = con.prepareStatement("update courses set course_code = ?, course_name = ? ,credits = ? , theory_hours = ? ,practical_hours = ?,lecturer_in_charge = ? where course_code = ?");
                pst.setString(1, c_code.getText());
                pst.setString(2, c_name.getText());
                pst.setInt(3, Integer.parseInt(credit.getText()));

                pst.setInt(4, Integer.parseInt(theory.getText()));
                pst.setInt(5, Integer.parseInt(phour.getText()));
                pst.setString(6, lecname.getText());
                pst.setString(7, c_find.getText());
                int rs = pst.executeUpdate();

                if (rs == 1) {
                    JOptionPane.showMessageDialog(null, "User successfully updated");

                } else {
                    JOptionPane.showMessageDialog(null, "User not updated");
                }
            } catch(SQLException ex) {
                ex.printStackTrace();
            }

            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "Enter the valid data type");
            }

        }

    }



}
