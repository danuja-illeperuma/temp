package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UViewAttendance extends JFrame {

    private JPanel AttendancePanel;
    private JTextField c_code;
    private JTable AttendanceTable;
    private JButton viewButton;
    private JButton resetButton;
    private JLabel ccode;
    private JTextField textpercentageT;
    private JLabel TPercentagelabel;
    private JTextField textPercentageP;
    private JLabel Ppercentagelabel;
    int userId;
    String username;

    public UViewAttendance(String username) {
        this.username = username;
        setTitle("View Attendance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null); // Center the frame

        // ===== Main Panel =====
        AttendancePanel = new JPanel(new BorderLayout(10, 10));
        AttendancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(AttendancePanel);

        // ===== Top Input Panel =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding between components

        ccode = new JLabel("Course Code:");
        ccode.setFont(new Font("Arial", Font.BOLD, 14));

        c_code = new JTextField(20);
        c_code.setFont(new Font("Arial", Font.PLAIN, 14));

        viewButton = new JButton("View Attendance");
        resetButton = new JButton("Reset");

        viewButton.setPreferredSize(new Dimension(160, 30));
        resetButton.setPreferredSize(new Dimension(100, 30));

        // Add components to topPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(ccode, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        topPanel.add(c_code, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        topPanel.add(viewButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        topPanel.add(resetButton, gbc);

        // ===== Percentage Panel =====
        TPercentagelabel = new JLabel("Theory Attendance %:");
        TPercentagelabel.setFont(new Font("Arial", Font.BOLD, 14));

        textpercentageT = new JTextField(10);
        textpercentageT.setFont(new Font("Arial", Font.PLAIN, 14));
        textpercentageT.setEditable(false);

        Ppercentagelabel = new JLabel("Practical Attendance %:");
        Ppercentagelabel.setFont(new Font("Arial", Font.BOLD, 14));

        textPercentageP = new JTextField(10);
        textPercentageP.setFont(new Font("Arial", Font.PLAIN, 14));
        textPercentageP.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(TPercentagelabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        topPanel.add(textpercentageT, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        topPanel.add(Ppercentagelabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        topPanel.add(textPercentageP, gbc);

        AttendancePanel.add(topPanel, BorderLayout.NORTH);

        // ===== Attendance Table =====
        AttendanceTable = new JTable();
        AttendanceTable.setFont(new Font("Arial", Font.PLAIN, 13));
        AttendanceTable.setRowHeight(25);
        AttendanceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(AttendanceTable);
        scrollPane.setPreferredSize(new Dimension(850, 400));
        AttendancePanel.add(scrollPane, BorderLayout.CENTER);

        // ===== Button Actions =====
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = c_code.getText().trim();

                if (courseCode.isEmpty()) {
                    JOptionPane.showMessageDialog(UViewAttendance.this, "Please enter a course code.");
                    return;
                }

                loadAttendance(courseCode);
                calculateAttendancePercentage(courseCode);
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c_code.setText("");
                textpercentageT.setText("");
                textPercentageP.setText("");
                AttendanceTable.setModel(new DefaultTableModel());
            }
        });
    }

    private void loadAttendance(String courseCode) {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Username", "Course Code", "Lec_Hours", "Lecture Date", "Attendance State", "Lec_Type"}, 0
        );

        try (Connection con = dbconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT username, c_code, c_hours, lec_date, at_state, c_type FROM attendance_detail WHERE c_code = ? AND username = ?"
             )) {

            ps.setString(1, courseCode);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("c_code"),
                        rs.getInt("c_hours"),
                        rs.getDate("lec_date"),
                        rs.getString("at_state"),
                        rs.getString("c_type")
                });
            }

            AttendanceTable.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching attendance records.");
        }
    }

    private void calculateAttendancePercentage(String courseCode) {
        try (Connection con = dbconnection.getConnection()) {

            // Calculate Theory Percentage
            PreparedStatement psTheory = con.prepareStatement(
                    "SELECT SUM(CASE WHEN at_state = 'Present' THEN c_hours ELSE 0 END) * 100.0 / SUM(c_hours) AS Percentage " +
                            "FROM attendance_detail WHERE c_code = ? AND username = ? AND c_type = 'T'"
            );
            psTheory.setString(1, courseCode);
            psTheory.setString(2, username);
            ResultSet rsTheory = psTheory.executeQuery();

            if (rsTheory.next()) {
                double theoryPercentage = rsTheory.getDouble("Percentage");
                if (rsTheory.wasNull()) {
                    textpercentageT.setText("0.00%");
                } else {
                    textpercentageT.setText(String.format("%.2f%%", theoryPercentage));
                }
            } else {
                textpercentageT.setText("0.00%");
            }

            // Calculate Practical Percentage
            PreparedStatement psPractical = con.prepareStatement(
                    "SELECT SUM(CASE WHEN at_state = 'Present' THEN c_hours ELSE 0 END) * 100.0 / SUM(c_hours) AS Percentage " +
                            "FROM attendance_detail WHERE c_code = ? AND username = ? AND c_type = 'P'"
            );
            psPractical.setString(1, courseCode);
            psPractical.setString(2, username);
            ResultSet rsPractical = psPractical.executeQuery();

            if (rsPractical.next()) {
                double practicalPercentage = rsPractical.getDouble("Percentage");
                if (rsPractical.wasNull()) {
                    textPercentageP.setText("0.00%");
                } else {
                    textPercentageP.setText(String.format("%.2f%%", practicalPercentage));
                }
            } else {
                textPercentageP.setText("0.00%");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating attendance percentages.");
        }
    }

    public JPanel getViewAttendance() {
        return AttendancePanel;
    }
}





