package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UViewTimetables extends JFrame {
    private JPanel TimetablePanel;
    private JButton viewTimetableButton;
    private JTable TimetableTable;
    private JButton resetButton;
    private JPanel Timetablepanel;

    public UViewTimetables() {
        setTitle("View Timetables");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 500);
        setLocationRelativeTo(null); // Center window

        // ===== Main Panel =====
        TimetablePanel = new JPanel(new BorderLayout(10, 10));
        TimetablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(TimetablePanel);

        // ===== Top Button Panel =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        viewTimetableButton = new JButton("View Timetable");
        viewTimetableButton.setPreferredSize(new Dimension(150, 35));
        viewTimetableButton.setFont(new Font("Arial", Font.BOLD, 13));
        topPanel.add(viewTimetableButton);

        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 35));
        resetButton.setFont(new Font("Arial", Font.BOLD, 13));
        topPanel.add(resetButton);

        TimetablePanel.add(topPanel, BorderLayout.NORTH);

        // ===== Timetable Table =====
        TimetableTable = new JTable();
        TimetableTable.setFont(new Font("Arial", Font.PLAIN, 13));
        TimetableTable.setRowHeight(25);
        TimetableTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Timetable ID", "Course ID", "Lecturer ID",
                "Day", "Start Time", "End Time", "Location", "Session Type"
        });
        TimetableTable.setModel(model);

        JScrollPane tableScrollPane = new JScrollPane(TimetableTable);
        TimetablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // ===== Button Actions =====
        viewTimetableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTimetables();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0); // Clear table
            }
        });
    }

    private void loadTimetables() {
        DefaultTableModel model = (DefaultTableModel) TimetableTable.getModel();
        model.setRowCount(0); // Clear old data

        String query = "SELECT * FROM timetables";

        try (Connection conn = dbconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int timetableId = rs.getInt("timetable_id");
                int courseId = rs.getInt("course_id");
                int lecturerId = rs.getInt("lecturer_id");
                String day = rs.getString("day_of_week");
                Time start = rs.getTime("start_time");
                Time end = rs.getTime("end_time");
                String location = rs.getString("location");
                String type = rs.getString("session_type");

                model.addRow(new Object[]{
                        timetableId, courseId, lecturerId, day, start, end, location, type
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading timetables: " + ex.getMessage());
        }
    }

    public JPanel getViewTimetables() {
        return TimetablePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UViewTimetables().setVisible(true));
    }
}


