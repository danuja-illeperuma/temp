package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAttendance4 {
    private JTextField stu_id;
    private JTextField c_code;
    private JTextField result;
    private JComboBox comboBox1;
    private JButton submitButton1;
    private JButton resetButton1;
    private JTextField cCode;
    private JComboBox comboBox2;
    private JButton submitButton;
    private JButton resetButton;
    private JTable table1;
    private JScrollPane table;
    private JPanel main4;

    public ViewAttendance4() {

        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = stu_id.getText().trim();
                String ccode = c_code.getText().trim();
                String select = comboBox1.getSelectedItem().toString().trim();

                if(student.isEmpty() || ccode.isEmpty() ){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                try{
                    double percent=0.0;

                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;

                    if(select.equals("Combined")){
                        sql = "SELECT SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? GROUP BY username,c_code,c_type ; ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, student);
                        pst.setString(2, ccode);


                    }else{
                        sql = "SELECT SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? AND c_type = ? GROUP BY username,c_code,c_type ;";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, student);
                        pst.setString(2, ccode);
                        pst.setString(3, select.equals("Theory") ? "T" : "P");
                    }
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()){
                        percent = rs.getDouble("Percentage");
                        if(percent < 80){
                            result.setText(String.format("%.2f", percent)+"%");

                        }else{
                            JOptionPane.showMessageDialog(null, student+" Higher than 80 OR Invalid Course Type");
                        }

                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                c_code.setText("");
                comboBox1.setSelectedIndex(0);
                result.setText("");
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ccode = c_code.getText().trim();
                if(ccode.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }

                try{
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;

                    String selectType = comboBox2.getSelectedItem().toString();
                    if(selectType.equals("Combined")){
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ?  GROUP BY username,c_code,c_type HAVING Percentage < 80 ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, cCode.getText());

                    }else{
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ? AND c_type = ? GROUP BY username,c_code,c_type HAVING Percentage < 80 ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, cCode.getText());
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
                    System.out.println(ex.getMessage());
                }
            }
        });
    }


    public JPanel getMain4() {
        return main4;
    }
}
