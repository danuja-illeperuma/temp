package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ViewMarks extends JFrame {
    private JPanel viewMarks;
    private JTextField code;
    private JTextField result;
    private JComboBox comboBox1;
    private JLabel courseCode;
    private JTextField stu_id;
    private JTextField c_code;
    private JTextField stu_grade;
    private JTextField stu_sgpa;
    private JTextField stu_cgpa;
    private JButton submitButton;
    private JButton resetButton;
    private JButton Submit;
    private JComboBox comboBox2;
    private JButton Reset;
    private JPanel batch;
    private JPanel Individual;
    private JPanel marks;
    private JButton CGPAButton;
    private JButton gradeButton;
    private JButton SGPAButton;
    private JTable table1;
    private JScrollPane table;
    private JButton marksButton;
    private JButton submitButton2;
    private JTextField stu_id2;

    public ViewMarks() {

        gradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentGrade grade = new StudentGrade();
                marks.setLayout(new BorderLayout());
                marks.removeAll();
                marks.add(grade.getGrade());
                marks.repaint();
                marks.revalidate();
            }
        });
        SGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentSGPA sgpa = new StudentSGPA();
                marks.setLayout(new BorderLayout());
                marks.removeAll();
                marks.add(sgpa.getSgpa());
                marks.repaint();
                marks.revalidate();
            }
        });
        CGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentCGPA cgpa = new StudentCGPA();
                marks.setLayout(new BorderLayout());
                marks.removeAll();
                marks.add(cgpa.getCgpa());
                marks.repaint();
                marks.revalidate();
            }
        });


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double results=0.0;
                    String examType=(String)comboBox1.getSelectedItem();
                    String columnName="";

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
                    //String stuId=stu_id.getText();
                    //String course_code=code.getText();

                    Connection con=DatabaseConnection.getConnection();
                    String sql="SELECT "+columnName+"  FROM marks WHERE stu_id=? AND c_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, stu_id.getText());
                    ps.setString(2, code.getText());

                    ResultSet rs=ps.executeQuery();

                    if(rs.next()){
                        results=rs.getInt(columnName);
                        result.setText(String.valueOf(results));
                    }
                    else{
                        result.setText("No data found");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                code.setText("");
                result.setText("");
            }
        });
        Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double results=0.0;
                    String s_id="";
                    String course_code="";
                    String examType=(String)comboBox2.getSelectedItem();
                    String columnName="";

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
                    String sql="SELECT * FROM marks WHERE c_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, c_code.getText());

                    ResultSet rs=ps.executeQuery();

                    String[] columnNames = {"Student ID", columnName};
                    DefaultTableModel model = new DefaultTableModel(columnNames,0);
                    model.setRowCount(0);

                    while(rs.next()){
                        s_id=rs.getString("stu_id");
                        course_code=rs.getString("c_code");
                        results=rs.getInt(columnName);

                            model.addRow(new Object[]{s_id, results});


                    }
                    table1.setModel(model);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        Reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c_code.setText("");
                comboBox2.setSelectedIndex(0);
                DefaultTableModel model = (DefaultTableModel)table1.getModel();
                model.setRowCount(0);
            }
        });
        marksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMarks viewMarks = new ViewMarks();
                marks.setLayout(new BorderLayout());
                marks.removeAll();
                marks.add(viewMarks.getMarks(),BorderLayout.CENTER);
                marks.repaint();
                marks.revalidate();
            }
        });
    }

    public JPanel getViewMarks() {
        return viewMarks;
    }
    public JPanel getMarks(){
        return marks;
    }
}

