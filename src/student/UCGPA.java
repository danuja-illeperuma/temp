package student;/*import javax.swing.*;
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UCGPA extends JFrame {
    private JPanel CGPAPanel;
    private JPanel Ipanel;
    private JPanel Bpanel;
    private JPanel Buttonpanel;
    private JButton viewButton;
    private JButton viewButton1;
    private JTextField textFieldresult;
    private JTable tableresult;
    private JButton resetButton;
    private JButton gradeButton;
    private JButton SGPAButton;
    private JButton CGPAButton;
    private JLabel cppal;
    private JLabel rlabel;

    private String username;

    public UCGPA(String username) {
        this.username = username;

        setTitle("CGPA Viewer");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel setup
        CGPAPanel = new JPanel(new BorderLayout());

        // ===== Individual Panel =====
        Ipanel = new JPanel(new GridLayout(3, 2, 10, 10));
        Ipanel.setBorder(BorderFactory.createTitledBorder("View Individual CGPA"));

        cppal = new JLabel("Your CGPA:");
        rlabel = new JLabel("Result:");
        textFieldresult = new JTextField(10);
        textFieldresult.setEditable(false);
        viewButton = new JButton("View CGPA");
        resetButton = new JButton("Reset");

        Ipanel.add(cppal);
        Ipanel.add(new JLabel());
        Ipanel.add(rlabel);
        Ipanel.add(textFieldresult);
        Ipanel.add(viewButton);
        Ipanel.add(resetButton);

        // ===== Batch Panel =====
        Bpanel = new JPanel(new BorderLayout());
        Bpanel.setBorder(BorderFactory.createTitledBorder("All Students' CGPA"));

        tableresult = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableresult);
        viewButton1 = new JButton("View All");

        JPanel topBatch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBatch.add(viewButton1);

        Bpanel.add(topBatch, BorderLayout.NORTH);
        Bpanel.add(scrollPane, BorderLayout.CENTER);

        // ===== Button Panel =====
        Buttonpanel = new JPanel(new FlowLayout());
        gradeButton = new JButton("Grade");
        SGPAButton = new JButton("SGPA");
        CGPAButton = new JButton("CGPA");

        Buttonpanel.add(gradeButton);
        Buttonpanel.add(SGPAButton);
        Buttonpanel.add(CGPAButton);

        // Layout assembly
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(Ipanel);
        centerPanel.add(Bpanel);

        CGPAPanel.add(Buttonpanel, BorderLayout.NORTH);
        CGPAPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(CGPAPanel);

        // ===== Actions =====
        viewButton.addActionListener(e -> viewIndividualCGPA());
        viewButton1.addActionListener(e -> viewAllCGPA());

        // Reset button clears both textFieldresult and tableresult
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the text result field
                textFieldresult.setText("");

                // Clear the table by resetting the model
                DefaultTableModel model = (DefaultTableModel) tableresult.getModel();
                model.setRowCount(0); // Clear all rows in the table
            }
        });

        gradeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Grade feature clicked!"));
        SGPAButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Redirecting to SGPA..."));
        CGPAButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "You are already viewing CGPA."));
    }

    private void viewIndividualCGPA() {
        try {
            Connection con = dbconnection.getConnection();
            String sql = "SELECT CGPA FROM cgpa WHERE student_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double cgpa = rs.getDouble("CGPA");
                textFieldresult.setText(String.valueOf(cgpa));
            } else {
                textFieldresult.setText("Not Found");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void viewAllCGPA() {
        try {
            Connection con = dbconnection.getConnection();
            String sql = "SELECT student_id, CGPA FROM cgpa";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            String[] columns = {"Student ID", "CGPA"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            while (rs.next()) {
                String sid = rs.getString("student_id");
                double cgpa = rs.getDouble("CGPA");
                model.addRow(new Object[]{sid, cgpa});
            }

            tableresult.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public JPanel getCGPanel() {
        return CGPAPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UCGPA("001").setVisible(true));
    }
}


