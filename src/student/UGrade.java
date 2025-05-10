package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UGrade extends JFrame {
    private JPanel gradePanel;
    private JPanel IndividualPanel;
    private JPanel BatchPanel;
    private JPanel buttonPanel;

    private JLabel iccode;
    private JTextField textField2;
    private JLabel rlabel;
    private JTextField textresult;
    private JButton viewButton;
    private JButton resetButton;

    private JLabel Bccode;
    private JTextField textBccode;
    private JButton viewButton1;
    private JButton resetButton1;
    private JTable resultTable;

    private JButton gradeButton;
    private JButton SGPAButton;
    private JButton CGPAButton;
    private JLabel Igrades;
    private JTable resulttable;
    private JPanel buttonPannel;

    private int userId;
    String username;

    public UGrade(String username) {
        this.username = username;
        setTitle("Student Grades");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        gradePanel = new JPanel(new BorderLayout());

        // ---- Individual Panel ----
        IndividualPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        IndividualPanel.setBorder(BorderFactory.createTitledBorder("View Individual Grade"));

        iccode = new JLabel("Course Code:");
        textField2 = new JTextField(10);
        rlabel = new JLabel("Grade:");
        textresult = new JTextField(10);
        textresult.setEditable(false);
        viewButton = new JButton("View");
        resetButton = new JButton("Reset");

        IndividualPanel.add(iccode);
        IndividualPanel.add(textField2);
        IndividualPanel.add(rlabel);
        IndividualPanel.add(textresult);
        IndividualPanel.add(viewButton);
        IndividualPanel.add(resetButton);

        // ---- Batch Panel ----
        BatchPanel = new JPanel(new BorderLayout());
        BatchPanel.setBorder(BorderFactory.createTitledBorder("View Batch Grades"));

        JPanel topBatchPanel = new JPanel(new FlowLayout());
        Bccode = new JLabel("Course Code:");
        textBccode = new JTextField(10);
        viewButton1 = new JButton("View");
        resetButton1 = new JButton("Reset");

        topBatchPanel.add(Bccode);
        topBatchPanel.add(textBccode);
        topBatchPanel.add(viewButton1);
        topBatchPanel.add(resetButton1);

        resultTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        BatchPanel.add(topBatchPanel, BorderLayout.NORTH);
        BatchPanel.add(tableScrollPane, BorderLayout.CENTER);

        // ---- Button Panel ----
        buttonPanel = new JPanel(new FlowLayout());
        gradeButton = new JButton("Grade");
        SGPAButton = new JButton("SGPA");
        CGPAButton = new JButton("CGPA");

        buttonPanel.add(gradeButton);
        buttonPanel.add(SGPAButton);
        buttonPanel.add(CGPAButton);

        // ---- Layout Composition ----
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(IndividualPanel);
        centerPanel.add(BatchPanel);

        gradePanel.add(buttonPanel, BorderLayout.NORTH);
        gradePanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(gradePanel);

        // ---- Action Listeners ----
        viewButton.addActionListener(e -> fetchIndividualGrade());
        resetButton.addActionListener(e -> {
            textField2.setText("");
            textresult.setText("");
        });

        viewButton1.addActionListener(e -> fetchBatchGrades());
        resetButton1.addActionListener(e -> {
            textBccode.setText("");
            resultTable.setModel(new DefaultTableModel());
        });



        gradeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Grade feature clicked!"));
        //SGPAButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "SGPA feature coming soon!"));
        //CGPAButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "CGPA feature coming soon!"));
        SGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                USGPA sgpa=new USGPA(username);
                gradePanel.setLayout(new BorderLayout());
                gradePanel.removeAll();
                gradePanel.add(sgpa.getSGPanel());
                gradePanel.repaint();
                gradePanel.revalidate();
            }
        });
        CGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UCGPA cgpa=new UCGPA(username);
                gradePanel.setLayout(new BorderLayout());
                gradePanel.removeAll();
                gradePanel.add(cgpa.getCGPanel());
                gradePanel.repaint();
                gradePanel.revalidate();
            }
        });
    }



private void fetchIndividualGrade() {
        try {
            Connection con = dbconnection.getConnection();
            String sql = "SELECT student_grade FROM student_grades WHERE student_id=? AND course_code=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, textField2.getText().trim());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                textresult.setText(rs.getString("student_grade"));
            } else {
                textresult.setText("Not Found");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void fetchBatchGrades() {
        try {
            Connection con = dbconnection.getConnection();
            String sql = "SELECT student_id, student_grade FROM student_grades WHERE course_code=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, textBccode.getText().trim());

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Student ID", "Grade"}, 0);

            while (rs.next()) {
                String sid = rs.getString("student_id");
                String grade = rs.getString("student_grade");
                model.addRow(new Object[]{sid, grade});
            }
            resultTable.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public JPanel getViewGrades() {
        return gradePanel;
    }
   /* public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UGrade("001").setVisible(true));
    }*/

}



