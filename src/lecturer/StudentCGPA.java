package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentCGPA extends JFrame{
    private JButton resetButton;
    private JButton submitButton1;
    private JButton submitButton;
    private JTextField stu_id;
    private JTextField result;
    private JPanel cgpa;
    private JTable table1;
    private JScrollPane table;

    public StudentCGPA() {
        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter Student ID");
                }

                try{
                    double results=0.0;

                    Connection con=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM cgpa WHERE student_id=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, stu_id.getText());

                    ResultSet rs=ps.executeQuery();

                    if(rs.next()){
                        results=rs.getDouble("CGPA");
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

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double results=0.0;
                    String ID="";

                    Connection conn=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM cgpa";
                    PreparedStatement ps=conn.prepareStatement(sql);


                    ResultSet rs=ps.executeQuery();

                    String[] columnNames = {"Student ID", "CGPA"};
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                    while(rs.next()){
                        ID=rs.getString("student_id");
                        results=rs.getDouble("CGPA");
                        model.addRow(new Object[]{ID, results});
                    }
                    table1.setModel(model);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        });
    }

    public JPanel getCgpa() {
        return cgpa;
    }
}
