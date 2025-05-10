package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinalEligibility extends JFrame {
    private JButton submitButton;
    private JButton Reset;
    private JButton submitButton1;
    private JButton resetButton;
    private JTable table1;
    private JTextField course_code;
    private JTextField stu_id;
    private JTextField c_code;
    private JTextField result;
    private JScrollPane table;
    private JPanel main;

    public FinalEligibility() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id.getText().isEmpty() || c_code.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }
                try {
                    String results = "";

                    Connection con = DatabaseConnection.getConnection();

                    String sql = "SELECT * from final_eligibility WHERE stu_id = ? AND c_code=?" ;

                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, c_code.getText());
                    pst.setString(2, stu_id.getText());

                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        results = rs.getString("Eligibility");
                    }

                    result.setText(results);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });
        Reset.addActionListener(new ActionListener() {
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
                    JOptionPane.showMessageDialog(null, "Please enter the course code");
                }
                try {
                    String results = "";
                    String ID="";

                    Connection con = DatabaseConnection.getConnection();

                    String sql = "SELECT * from final_eligibility WHERE c_code=?";

                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, course_code.getText());

                    ResultSet rs = pst.executeQuery();
                    String[] columnNames = {"Student ID", "Eligibility"};
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                    while (rs.next()) {
                        ID = rs.getString("username");
                        results = rs.getString("Eligibility");
                        model.addRow(new Object[]{ID, results});
                    }
                    table1.setModel(model);
                } catch (SQLException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                course_code.setText("");
            }
        });
    }

    public JPanel getMain() {
        return main;
    }

}
