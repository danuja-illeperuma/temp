package lecturer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateMarks {
    private JTextField cCode;
    private JTextField stuId;
    private JTextField stuMarks;
    private JLabel marksTitle;
    private JLabel courceCode;
    private JLabel studentId;
    private JLabel examType;
    private JLabel marks;
    private JComboBox eType;
    private JPanel markPanel;
    private JButton submitButton;
    private JButton resetButton;

    public UpdateMarks() {

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double stuMark = Double.parseDouble(stuMarks.getText());
                if(stuMark <0 && stuMark>100) {
                    JOptionPane.showMessageDialog(null, "Enter valid marks");
                }

            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double results=0.0;
                    String examType=(String)eType.getSelectedItem();
                    String columnName="";
                    double newMark = Double.parseDouble(stuMarks.getText());

                    if (cCode.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter a course code");
                        return;
                    }

                    if (stuId.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter a student ID");
                        return;
                    }
                    if (newMark < 0 || newMark > 100) {
                        JOptionPane.showMessageDialog(null, "Enter valid marks (0-100)");
                        return;
                    }

                    switch(examType){
                        case "Quiz 1":
                            columnName="Q1_mark";
                            break;
                        case "Quiz 2":
                            columnName="Q2_mark";
                            break;
                        case "Quiz 3":
                            columnName="Q3_mark";
                            break;
                        case "Quiz 4":
                            columnName="Q4_mark";
                            break;
                        case "Assesment 1":
                            columnName="as1_mark";
                            break;
                        case "Assesment 2":
                            columnName="as2_mark";
                            break;
                        case "Mid term":
                            columnName="mid_term";
                            break;
                        case "End_theory":
                            columnName="end_theory";
                            break;
                        case "End_practical":
                            columnName="end_practical";
                            break;
                    }

                    Connection con=DatabaseConnection.getConnection();
                    String sql="UPDATE marks SET "+columnName+"=? WHERE stu_id=? AND c_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setDouble(1,newMark);
                    ps.setString(2,stuId.getText());
                    ps.setString(3, cCode.getText());


                    int rs=ps.executeUpdate();

                    if(rs > 0) {
                        JOptionPane.showMessageDialog(null, "Marks Updated Successfully");
                    }else{
                        JOptionPane.showMessageDialog(null, "Marks Updated Failed");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            };
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cCode.setText("");
                stuId.setText("");
                stuMarks.setText("");

            }
        });
    }

    public JPanel getMarks() {
        return markPanel;
    }


}
