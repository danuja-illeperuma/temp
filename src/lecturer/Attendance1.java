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

public class Attendance1 extends JFrame {
    private JTextField stu_id;
    private JTextField c_code;
    private JTextField result;
    private JComboBox c_typeBox;
    private JPanel main;
    private JTextField course_code;
    private JComboBox comboBox2;
    private JButton Button1;
    private JButton Button5;
    private JButton Button2;
    private JButton Button3;
    private JButton Button4;
    private JTable table1;
    private JScrollPane table;
    private JButton submitButton1;
    private JButton resetButton1;
    private JButton submitButton;
    private JButton resetButton;
    private JPanel viewatt;
    private JPanel mainAtten1;
    private JPanel attendanceMain2;

    //private Connection conn;

    public Attendance1() {

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = stu_id.getText().trim();
                String code = c_code.getText().trim();
                String selectType = c_typeBox.getSelectedItem().toString().trim();

                if(student.isEmpty() || code.isEmpty()){
                    JOptionPane.showMessageDialog(main, "Please enter student and c_code");
                    return;
                }
                try{
                    double percent=0.0;

                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;

                    if(selectType.equals("Combined")){
                        sql = "SELECT SUM(CASE WHEN at_state = 'Present'  THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? GROUP BY username,c_code,c_type ; ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, student);
                        pst.setString(2, code);


                    }else{
                        sql = "SELECT SUM(CASE WHEN at_state = 'Present'  THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? AND c_type = ? GROUP BY username,c_code,c_type ;";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, student);
                        pst.setString(2, code);
                        pst.setString(3, selectType.equals("Theory") ? "T" : "P");
                    }
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()){
                        percent = rs.getDouble("Percentage");
                        if(percent < 80){
                            result.setText(String.format("%.2f", percent)+"%");

                        }else{
                            JOptionPane.showMessageDialog(main, student+" Higher than 80 OR Invalid Course Type");
                        }

                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(main, ex.getMessage());
                }

            }
        });


        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                c_code.setText("");
                c_typeBox.setSelectedIndex(0);
                result.setText("");

            }
        });


        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(course_code.getText().isEmpty()){
                    JOptionPane.showMessageDialog(main, "Please enter course code");
                    return;
                }

                try{
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;

                    String selectType = comboBox2.getSelectedItem().toString();
                    if(selectType.equals("Combined")){
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present'  THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ?  GROUP BY username,c_code,c_type HAVING Percentage < 80 ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, course_code.getText());

                    }else{
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present'  THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ? AND c_type = ? GROUP BY username,c_code,c_type HAVING Percentage < 80 ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, course_code.getText());
                        pst.setString(2, selectType.equals("Theory") ? "T" : "P");

                    }

                    ResultSet rs = pst.executeQuery();

                    DefaultTableModel model = new DefaultTableModel(new String[]{"Username", "C_code", "Percentage"}, 0);
                    while(rs.next()){
                        model.addRow(new Object[]{
                                rs.getString("username"),
                                rs.getString("c_code"),
                                String.format("%.2f", rs.getDouble("Percentage")) + "%"

                        });

                    }

                    table1.setModel(model);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(main, ex.getMessage());
                }
            }
        });


        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course_code.setText("");
            }
        });
        Button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewAttendance2 view2 = new ViewAttendance2();
                main.setLayout(new BorderLayout());
                main.removeAll();
                main.add(view2.getMain1(), BorderLayout.CENTER);
                main.revalidate();
                main.repaint();
            }
        });
        Button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewAttendance3 view3 = new ViewAttendance3();
                main.setLayout(new BorderLayout());
                main.removeAll();
                main.add(view3.getMainAttendance3(), BorderLayout.CENTER);
                main.revalidate();
                main.repaint();
            }
        });
        Button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewAttendance4 view4 = new ViewAttendance4();
                main.setLayout(new BorderLayout());
                main.removeAll();
                main.add(view4.getMain4(), BorderLayout.CENTER);
                main.revalidate();
                main.repaint();
            }
        });
        Button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewAttendance5 view5 = new ViewAttendance5();
                main.setLayout(new BorderLayout());
                main.removeAll();
                main.add(view5.getMain5(), BorderLayout.CENTER);
                main.revalidate();
                main.repaint();
            }
        });
    }
    public JPanel getMainPanel() {
        return viewatt;
    }


}