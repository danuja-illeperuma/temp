package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewMedical extends JFrame {
    private JTextField stu_id;
    private JTextField c_code;
    private JButton submitButton;
    private JTable table1;
    private JButton resetButton;
    private JScrollPane table;
    private JPanel medical;

    public ViewMedical() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = c_code.getText().trim();
                String stuId = stu_id.getText().trim();

                if (courseCode.isEmpty() || stuId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields.");
                    return;
                }

                try {
                    Connection con = DatabaseConnection.getConnection();
                    String sql = "SELECT * FROM Medical WHERE c_code = ? AND username = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, courseCode);
                    ps.setString(2,stuId);

                    ResultSet rs = ps.executeQuery();

                    DefaultTableModel model = new DefaultTableModel(
                            new String[]{"Medical ID", "Username", "Description", "Submitted Date", "State", "Course Code", "Course Type", "Cut Lecture Hours"}, 0
                    );

                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getInt("medical_id"),
                                rs.getString("username"),
                                rs.getString("Description"),
                                rs.getDate("Sub_date"),
                                rs.getString("State"),
                                rs.getString("c_code"),
                                rs.getString("c_type"),
                                rs.getInt("cut_lec_hour")
                        });
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
                stu_id.setText("");
                c_code.setText("");
                table1.setModel(new DefaultTableModel());
            }
        });
    }

    public JPanel getMedical() {

        return medical;
    }
}
