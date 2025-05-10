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

public class Eligibility {
    private JPanel eligibility;
    private JButton finalExamEligibilityButton;
    private JButton CAEligibilityButton;
    private JButton submitButton1;
    private JButton resetButton1;
    private JTextField stu_id;
    private JTextField c_code;
    private JTextField result;
    private JButton submitButton;
    private JButton resetButton;
    private JTable table1;
    private JTextField course_code;
    private JPanel content;
    private JScrollPane table;
    private JButton attentanceEligibilityButton;


    public Eligibility() {
        CAEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CAEligibility ca = new CAEligibility();
                content.setLayout(new BorderLayout());
                content.removeAll();
                content.add(ca.getCA(),BorderLayout.CENTER);
                content.repaint();
                content.revalidate();
            }
        });
        finalExamEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FinalEligibility f = new FinalEligibility();
                content.setLayout(new BorderLayout());
                content.removeAll();
                content.add(f.getMain(),BorderLayout.CENTER);
                content.repaint();
                content.revalidate();
            }
        });
        attentanceEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Eligibility eligibility = new Eligibility();
                content.setLayout(new BorderLayout());
                content.removeAll();
                content.add(eligibility.getcontent(),BorderLayout.CENTER);
                content.repaint();
                content.revalidate();
            }
        });
        submitButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stu_id.getText().equals("") || c_code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                }
                try {
                        String results = "";

                        Connection con = DatabaseConnection.getConnection();

                        String sql = "SELECT " +
                                "CASE " +
                                "    WHEN (SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 / SUM(c_hours)) >= 80 " +
                                "    THEN 'Eligible' " +
                                "    ELSE 'Not_Eligible' " +
                                "END AS Eligibility " +
                                "FROM attendance_detail " +
                                "WHERE c_code = ? AND username = ?" +
                                "GROUP BY username, c_code";

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
        resetButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stu_id.setText("");
                c_code.setText("");
                result.setText("");
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String results = "";
                    String ID="";
                    String code="";

                    Connection con = DatabaseConnection.getConnection();

                    String sql = "SELECT username,c_code," +
                            "CASE " +
                            "    WHEN (SUM(CASE WHEN at_state = 'Present' OR at_state = 'Medical' THEN c_hours ELSE 0 END) * 100.0 / SUM(c_hours)) >= 80 " +
                            "    THEN 'Eligible' " +
                            "    ELSE 'Not_Eligible' " +
                            "END AS Eligibility " +
                            "FROM attendance_detail " +
                            "WHERE c_code = ?" +
                            "GROUP BY username,c_code";

                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, course_code.getText());

                    ResultSet rs = pst.executeQuery();
                    String[] columnNames = {"Student ID", "Eligibility"};
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                    while (rs.next()) {
                        ID = rs.getString("username");
                        code=rs.getString("c_code");
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
                table1.setModel(new DefaultTableModel());
            }
        });
    }

    public JPanel getEligibility() {
        return eligibility;
    }
    public JPanel getcontent() {
        return content;
    }
}
