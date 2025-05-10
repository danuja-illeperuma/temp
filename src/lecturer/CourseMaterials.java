package lecturer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseMaterials {
    private JTextField textField1;
    private JButton uploadButton;
    private JPanel courseMaterials;
    private JTextField name;
    private JTextField c_code;
    private JTextField link;
    private JTextField textField2;

    public CourseMaterials() {
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(c_code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Course Code");
                }
                try{
                    Connection con=DatabaseConnection.getConnection();
                    String sql="UPDATE course_materials SET Material_Name=? , Driver_link=? WHERE course_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);

                    ps.setString(1,name.getText());
                    ps.setString(2,link.getText());
                    ps.setString(3,c_code.getText());

                    int rs=ps.executeUpdate();

                    if(rs > 0) {
                        JOptionPane.showMessageDialog(null, name.getText() + " has been uploaded successfully");
                    }else{
                        JOptionPane.showMessageDialog(null, "Upload Failed");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        });
    }

    public JPanel getCourseMaterials() {

        return courseMaterials;
    }
}
