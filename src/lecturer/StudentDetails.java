package lecturer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentDetails {
    private JLabel studentId;
    private JLabel studentName;
    private JLabel pNo;
    private JTextField stuId;
    private JTextField stuName;
    private JTextField number;
    private JButton submit;
    private JLabel studentEmail;
    private JTextField stuEmail;
    private JPanel studentDetails;
    private JButton resetButton;

    public StudentDetails() {
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String name1="",name2="",contact="",email="";

                    Connection conn=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM users WHERE username=?";
                    PreparedStatement ps=conn.prepareStatement(sql);

                    ps.setString(1,stuId.getText());
                    ResultSet rs=ps.executeQuery();

                    if(rs.next()){
                        name1=rs.getString("first_name");
                        name2=rs.getString("last_name");
                        contact=rs.getString("phone");
                        email=rs.getString("email");
                    }
                    stuName.setText(name1+" "+name2);
                    number.setText(contact);
                    stuEmail.setText(email);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });


        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stuId.setText("");
                stuName.setText("");
                number.setText("");
                stuEmail.setText("");
            }
        });
    }

    public JPanel getStudentDetails() {
        return studentDetails;
    }
}
