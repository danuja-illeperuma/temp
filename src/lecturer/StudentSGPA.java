package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.*;

public class StudentSGPA extends JFrame {
    private JButton resetButton;
    private JButton submitButton;
    private JTextField stu_id;
    private JTextField result;
    private JButton submitButton1;
    private JPanel sgpa;
    private JTable table1;
    private JScrollPane table;

    public StudentSGPA() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter student ID");
                }
                try{
                    double results=0.0;

                    Connection con=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM sgpa WHERE student_id=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, stu_id.getText());

                    ResultSet rs=ps.executeQuery();

                    if(rs.next()){
                        results=rs.getDouble("SGPA");
                    }
                    result.setText(String.valueOf(results));
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                result.setText("");
            }
        });
        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    double results=0.0;
                    String ID="";

                    Connection conn=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM sgpa";
                    PreparedStatement ps=conn.prepareStatement(sql);


                    ResultSet rs=ps.executeQuery();

                    String[] columnNames = {"Student ID", "SGPA"};
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                    while(rs.next()){
                        ID=rs.getString("student_id");
                        results=rs.getDouble("SGPA");
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
                stu_id.setText("");
                result.setText("");
            }
        });
    }

    public JPanel getSgpa() {
        return sgpa;
    }
}
