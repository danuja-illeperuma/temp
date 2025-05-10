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

public class ViewAttendance3 extends JFrame {
    private JTextField cCode;
    private JComboBox ctypebox2;
    private JButton submitButton1;
    private JButton resetButton1;
    private JButton resetButton;
    private JTextField result;
    private JTextField c_code;
    private JTextField stu_id;
    private JTable table1;
    private JScrollPane table;
    private JPanel main3;
    private JButton submitButton;
    private JPanel mainAttendance3;
    private JComboBox c_typebox;

    public ViewAttendance3() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = stu_id.getText().trim();
                String ccode = c_code.getText().trim();
                String ctype = (String) c_typebox.getSelectedItem();

                if(student.isEmpty() || ccode.isEmpty()){
                    JOptionPane.showMessageDialog(main3, "Please enter student and c_code");
                    return;
                }

                try{
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;

                    if(ctype.equals("Combined")) {
                        ctype = "SELECT SUM(CASE WHEN at_state = 'Present'  THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? GROUP BY username,c_code,c_type,";
                        pst = conn.prepareStatement(ctype);
                        pst.setString(1, student);
                        pst.setString(2, ccode);

                    }else {
                        ctype = "SELECT SUM(CASE WHEN at_state = 'Present' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? AND c_type = ? GROUP BY username,c_code,c_type";
                        pst = conn.prepareStatement(ctype);
                        pst.setString(1, student);
                        pst.setString(2, ccode);
                        pst.setString(3, ctype.equals("Theory") ? "T": "P");
                    }
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        double out = rs.getDouble("Percentage");
                        result.setText(String.format("%.2f", out) + "%");
                        if(out > 80 ){
                            result.setBackground(Color.GREEN);
                        }else{
                            result.setBackground(Color.RED);
                        }

                    }else{
                        result.setBackground(Color.WHITE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
        });
        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cCode.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(main3, "Please enter student and c_code");
                }

                try {
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;
                    String selectType = (String) ctypebox2.getSelectedItem();

                    if(selectType.equals("Combined")){
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ? GROUP BY username,c_code,c_type HAVING Percentage > 80";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1,cCode.getText());

                    }else{
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ? AND c_type = ? GROUP BY username,c_code,c_type HAVING Percentage > 80";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1,cCode.getText());
                        pst.setString(2,selectType.equals("Theory") ? "T": "P");

                    }
                    ResultSet rs = pst.executeQuery();
                    DefaultTableModel model = new DefaultTableModel(new String[]{"Username", "C_code", "Percentage"}, 0);
                    while (rs.next()){
                        model.addRow(new Object[]{
                                rs.getString("username"),
                                rs.getString("c_code"),
                                String.format("%.2f", rs.getDouble("Percentage")) + "%"
                        });
                    }

                table1.setModel(model);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                c_code.setText("");
                c_typebox.setSelectedIndex(0);
            }
        });
        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cCode.setText("");
            }
        });
    }


    public JPanel getMainAttendance3() {
        return main3;
    }
}
