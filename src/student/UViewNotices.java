package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UViewNotices extends JFrame {
    private JPanel NoticePanel;
    private JButton viewNoticesButton;
    private JTable NoticeTable;
    private JButton resetButton;

    public UViewNotices() {
        setTitle("View Notices");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 550);
        setLocationRelativeTo(null); // Center the frame

        // ===== Main Panel =====
        NoticePanel = new JPanel(new BorderLayout(10, 10));
        NoticePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(NoticePanel);

        // ===== Top Buttons Panel =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        viewNoticesButton = new JButton("View Notices");
        resetButton = new JButton("Reset");

        viewNoticesButton.setPreferredSize(new Dimension(150, 35));
        resetButton.setPreferredSize(new Dimension(100, 35));

        viewNoticesButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(viewNoticesButton);
        buttonPanel.add(resetButton);

        NoticePanel.add(buttonPanel, BorderLayout.NORTH);

        // ===== Table for Notices =====
        NoticeTable = new JTable();
        NoticeTable.setFont(new Font("Arial", Font.PLAIN, 13));
        NoticeTable.setRowHeight(25);
        NoticeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane tableScrollPane = new JScrollPane(NoticeTable);
        tableScrollPane.setPreferredSize(new Dimension(850, 400));
        NoticePanel.add(tableScrollPane, BorderLayout.CENTER);

        // ===== Table model setup =====
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Content", "Publish Date"});
        NoticeTable.setModel(model);

        // ===== Button Actions =====
        viewNoticesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNotices();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0); // Clear all rows
            }
        });
    }

    private void loadNotices() {
        DefaultTableModel model = (DefaultTableModel) NoticeTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String query = "SELECT * FROM notices\n" +
                "ORDER BY publish_date DESC;\n";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("notice_id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                Timestamp date = rs.getTimestamp("publish_date");


                model.addRow(new Object[]{id, title, content, date});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading notices: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public JPanel getViewNotices() {
        return NoticePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UViewNotices().setVisible(true));
    }
}


