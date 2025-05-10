package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UViewCourse extends JFrame {
    private JPanel CoursePanel;
    private JTextField c_code;
    private JTable CourseTable;
    private JButton viewButton;
    private JButton resetButton;
    private JLabel ccode;

    public UViewCourse() {
        setTitle("View Courses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 550);
        setLocationRelativeTo(null); // Center the frame

        // ===== Main Panel Setup =====
        CoursePanel = new JPanel(new BorderLayout(10, 10));
        CoursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Top Input Panel (Search Field) =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        ccode = new JLabel("Course Code:");
        ccode.setFont(new Font("Arial", Font.BOLD, 14));

        c_code = new JTextField(20);
        c_code.setFont(new Font("Arial", Font.PLAIN, 14));

        viewButton = new JButton("Course Details");
        resetButton = new JButton("Reset");

        viewButton.setPreferredSize(new Dimension(140, 30));
        resetButton.setPreferredSize(new Dimension(100, 30));

        // Adding components to topPanel
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

        // ===== Center Table Panel =====
        CourseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(CourseTable);
        scrollPane.setPreferredSize(new Dimension(850, 400));

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] {
                "Course ID", "Course Code", "Course Name", "Credits",
                "Theory Hours", "Practical Hours", "Lecturer In Charge"
        });
        CourseTable.setModel(model);
        CourseTable.setRowHeight(25);
        CourseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        CourseTable.setFont(new Font("Arial", Font.PLAIN, 13));

        // ===== Adding Panels to Main CoursePanel =====
        CoursePanel.add(topPanel, BorderLayout.NORTH);
        CoursePanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(CoursePanel);

        // ===== Action Listeners =====
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCourses();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) CourseTable.getModel();
                model.setRowCount(0); // Clear table
                c_code.setText("");   // Clear search field
            }
        });
    }

    private void loadCourses() {
        DefaultTableModel model = (DefaultTableModel) CourseTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String codeFilter = c_code.getText().trim(); // Get the course code entered
        String query = "SELECT * FROM courses";

        if (!codeFilter.isEmpty()) {
            query += " WHERE course_code LIKE ?";
        }

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (!codeFilter.isEmpty()) {
                stmt.setString(1, "%" + codeFilter + "%"); // Use LIKE to filter course code
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("course_id");
                    String code = rs.getString("course_code");
                    String name = rs.getString("course_name");
                    int credits = rs.getInt("credits");
                    int theoryHours = rs.getInt("theory_hours");
                    int practicalHours = rs.getInt("practical_hours");
                    String lecturer = rs.getString("lecturer_in_charge");

                    model.addRow(new Object[] {
                            id, code, name, credits, theoryHours, practicalHours, lecturer
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage());
        }
    }

    public JPanel getViewCourses() {
        return CoursePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UViewCourse().setVisible(true));
    }
}



