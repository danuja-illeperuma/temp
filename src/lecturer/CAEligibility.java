package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CAEligibility extends JFrame {
    private JPanel CA;
    private JButton submitButton;
    private JButton resetButton;
    private JButton submitButton1;
    private JButton resetButton1;
    private JTextField stu_id;
    private JTextField c_code;
    private JTextField result;
    private JTextField course_code;
    private JTable table1;
    private JScrollPane table;

    public CAEligibility() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id.getText().equals("") || c_code.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }

                try{
                    String s_id="";
                    String results="";
                    String course_code="";

                    Connection con=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM ca_eligibility_new WHERE stu_id=? AND c_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1,stu_id.getText());
                    ps.setString(2, c_code.getText());

                    ResultSet rs=ps.executeQuery();

                    if(rs.next()){
                        s_id=rs.getString("stu_id");
                        course_code=rs.getString("c_code");
                        results=rs.getString("CA_eligibility");
                    }
                    result.setText(results);
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
                result.setText("");
            }
        });
        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(course_code.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Enter Course Code");
                }

                try{
                    String s_id="";
                    String results="";
                    String c_code="";

                    Connection con=DatabaseConnection.getConnection();
                    String sql="SELECT * FROM ca_eligibility_new WHERE c_code=?";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1, course_code.getText());

                    ResultSet rs=ps.executeQuery();

                    String[] columnNames = {"Student ID", "Eligibility Status"};
                    DefaultTableModel model = new DefaultTableModel(columnNames,0);
                    model.setRowCount(0);

                    while(rs.next()){
                        s_id=rs.getString("stu_id");
                        c_code=rs.getString("c_code");
                        results=rs.getString("CA_eligibility");

                        model.addRow(new Object[]{s_id, results});
                    }
                    table1.setModel(model);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course_code.setText("");
                table1.setModel(new DefaultTableModel());
            }
        });
    }

    public JPanel getCA() {

        return CA;
    }
}
