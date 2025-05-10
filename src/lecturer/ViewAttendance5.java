package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAttendance5 {
    private JButton submitButtonSt;
    private JButton resetButton1;
    private JTextField reseult;
    private JTextField c_code;
    private JTextField stu_id;
    private JComboBox comboBox1;
    private JTextField cCode;
    private JComboBox comboBox2;
    private JTable table1;
    private JButton submitButtonBA;
    private JButton resetButton;
    private JPanel main5;

    public ViewAttendance5() {

        submitButtonSt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = c_code.getText();
                String student = stu_id.getText().trim();
                String select = comboBox1.getSelectedItem().toString();

                if(code.isEmpty() || student.isEmpty() ){
                    JOptionPane.showMessageDialog(main5, "Please fill all the fields");
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
                        pst.setString(2, code);


                    }else{
                        sql = "SELECT SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE username = ? AND c_code = ? AND c_type = ? GROUP BY username,c_code,c_type ;";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, student);
                        pst.setString(2, code);
                        pst.setString(3, select.equals("Theory") ? "T" : "P");
                    }
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()){
                        percent = rs.getDouble("Percentage");
                        if(percent > 80){
                            reseult.setText(String.format("%.2f", percent)+"%");

                        }else{
                            JOptionPane.showMessageDialog(null, student+" Lower than 80 OR Invalid Course Type");
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
                reseult.setText("");
                c_code.setText("");
                comboBox1.setSelectedIndex(0);
            }
        });
        submitButtonBA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = cCode.getText().trim();
                String select = comboBox2.getSelectedItem().toString();


                if(code.isEmpty()){
                    JOptionPane.showMessageDialog(main5, "Please fill the c_code fields");
                }

                try{
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst;
                    String sql;

                    String selectType = comboBox2.getSelectedItem().toString();
                    if(selectType.equals("Combined")){
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ?  GROUP BY username,c_code,c_type HAVING Percentage > 80 ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, cCode.getText());

                    }else{
                        sql = "SELECT username,c_code, SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 /(SUM(c_hours)) AS Percentage FROM attendance_detail WHERE  c_code = ? AND c_type = ? GROUP BY username,c_code,c_type HAVING Percentage > 80 ";
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
    public JPanel getMain5() {
        return main5;
    }
}
