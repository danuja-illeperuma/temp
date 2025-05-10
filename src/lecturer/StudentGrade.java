package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentGrade extends JFrame {
    private JButton resetButton1;
    private JButton submitButton1;
    private JTextField c_code;
    private JTextField stu_id;
    private JButton resetButton;
    private JButton submitButton;
    private JTextField code;
    private JTextField result;
    private JPanel grade;
    private JPanel GradeMain;
    private JTable table1;
    private JScrollPane table;

    public StudentGrade() {
        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id.getText().equals("") || c_code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }
                try{
                    String results="";

                    Connection con=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM student_grades WHERE student_id=? AND course_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, stu_id.getText());
                    ps.setString(2, c_code.getText());

                    ResultSet rs=ps.executeQuery();

                    if(rs.next()){
                        results=rs.getString("student_grade");
                    }
                    result.setText(results);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                c_code.setText("");
                result.setText("");
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter the course code");
                }
                try{
                    String results="";
                    String ID="";

                    Connection conn=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM student_grades WHERE course_code=?";
                    PreparedStatement ps=conn.prepareStatement(sql);

                    ps.setString(1, code.getText());
                    ResultSet rs=ps.executeQuery();

                    String[] columnNames = {"Student ID", "Grade"};
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                    while(rs.next()){
                        ID=rs.getString("student_id");
                        results=rs.getString("student_grade");
                        model.addRow(new Object[]{ID, results});
                    }
                    table1.setModel(model);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table1.setModel(new DefaultTableModel());
                code.setText("");
            }
        });
    }

    public JPanel getGrade() {
        return GradeMain;
    }
}
