package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class USGPA extends JFrame {
    private JPanel SGPApanel;
    private JButton viewButton;
    private JButton viewButton1;
    private JPanel buttonPanel;
    private JPanel Bpannel;
    private JLabel isgpa;
    private JLabel Bsgpa;
    private JPanel Ipanel;
    private JTextField textresult;
    private JTable tableresult;
    private JButton resetButton;
    private JButton gradeButton;
    private JButton SGPAButton;
    private JButton CGPAButton;
    private JLabel rlabel;

    private String username;

    public USGPA(String username) {
        this.username = username;

        setTitle("SGPA Viewer");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel setups
        SGPApanel = new JPanel(new BorderLayout());

        // Individual Panel
        Ipanel = new JPanel(new GridLayout(3, 2, 10, 10));
        Ipanel.setBorder(BorderFactory.createTitledBorder("View Individual SGPA"));
        isgpa = new JLabel("Your SGPA:");
        rlabel = new JLabel("Result:");
        textresult = new JTextField(10);
        textresult.setEditable(false);
        viewButton = new JButton("View SGPA");
        resetButton = new JButton("Reset");

        Ipanel.add(isgpa);
        Ipanel.add(new JLabel()); // spacing
        Ipanel.add(rlabel);
        Ipanel.add(textresult);
        Ipanel.add(viewButton);
        Ipanel.add(resetButton);

        // Batch Panel
        Bpannel = new JPanel(new BorderLayout());
        Bpannel.setBorder(BorderFactory.createTitledBorder("View All Students' SGPA"));
        Bsgpa = new JLabel("All SGPA Records:");

        tableresult = new JTable();
        JScrollPane scrollPane = new JScrollPane(tableresult);
        viewButton1 = new JButton("View All");

        JPanel topBatch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBatch.add(Bsgpa);
        topBatch.add(viewButton1);

        Bpannel.add(topBatch, BorderLayout.NORTH);
        Bpannel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        buttonPanel = new JPanel(new FlowLayout());
        gradeButton = new JButton("Grade");
        SGPAButton = new JButton("SGPA");
        CGPAButton = new JButton("CGPA");

        buttonPanel.add(gradeButton);
        buttonPanel.add(SGPAButton);
        buttonPanel.add(CGPAButton);

        // Layout combination
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(Ipanel);
        centerPanel.add(Bpannel);

        SGPApanel.add(buttonPanel, BorderLayout.NORTH);
        SGPApanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(SGPApanel);

        // Action listeners
        viewButton.addActionListener(e -> viewIndividualSGPA());
        viewButton1.addActionListener(e -> viewAllSGPA());

        // Reset button clears both textresult and tableresult
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear the text result field
                textresult.setText("");

                // Clear the table by resetting the model
                DefaultTableModel model = (DefaultTableModel) tableresult.getModel();
                model.setRowCount(0); // Clear all rows in the table
            }
        });

        CGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UCGPA cgpa = new UCGPA(username);
                SGPApanel.setLayout(new BorderLayout());
                SGPApanel.removeAll();
                SGPApanel.add(cgpa.getCGPanel());
                SGPApanel.repaint();
                SGPApanel.revalidate();
            }
        });
    }

    private void viewIndividualSGPA() {
        try {
            Connection con = dbconnection.getConnection();
            String sql = "SELECT SGPA FROM sgpa WHERE student_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double sgpa = rs.getDouble("SGPA");
                textresult.setText(String.valueOf(sgpa));
            } else {
                textresult.setText("Not Found");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void viewAllSGPA() {
        try {
            Connection con = dbconnection.getConnection();
            String sql = "SELECT student_id, SGPA FROM sgpa";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            String[] columns = {"Student ID", "SGPA"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            while (rs.next()) {
                String sid = rs.getString("student_id");
                double sgpa = rs.getDouble("SGPA");
                model.addRow(new Object[]{sid, sgpa});
            }

            tableresult.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    public JPanel getSGPanel() {
        return SGPApanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new USGPA("001").setVisible(true));
    }
}


