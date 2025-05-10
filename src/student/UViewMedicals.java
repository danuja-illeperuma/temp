package student;/*import javax.swing.*;
*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UViewMedicals extends JFrame {
    private JPanel MedicalPanel;
    private JTextField c_code;
    private JButton viewMedicalsButton;
    private JTable MedicalTable;
    private JButton resetButton;
    private JLabel ccode;
    String username;

    public UViewMedicals(String username) {
        this.username = username;
        setTitle("View Medical Submissions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 550);
        setLocationRelativeTo(null); // Center window

        // ===== Main Panel =====
        MedicalPanel = new JPanel(new BorderLayout(10, 10));
        MedicalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(MedicalPanel);

        // ===== Top Input Panel =====
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JLabel ccode = new JLabel("Course Code:");
        ccode.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(ccode);

        c_code = new JTextField(10);
        c_code.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(c_code);

        viewMedicalsButton = new JButton("View Medicals");
        viewMedicalsButton.setPreferredSize(new Dimension(140, 35));
        viewMedicalsButton.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(viewMedicalsButton);

        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 35));
        resetButton.setFont(new Font("Arial", Font.BOLD, 13));
        inputPanel.add(resetButton);

        MedicalPanel.add(inputPanel, BorderLayout.NORTH);

        // ===== Table =====
        MedicalTable = new JTable();
        MedicalTable.setFont(new Font("Arial", Font.PLAIN, 13));
        MedicalTable.setRowHeight(25);
        MedicalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane tableScrollPane = new JScrollPane(MedicalTable);
        tableScrollPane.setPreferredSize(new Dimension(900, 400));
        MedicalPanel.add(tableScrollPane, BorderLayout.CENTER);

        // ===== Button Actions =====
        viewMedicalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = c_code.getText().trim();

                if (courseCode.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a course code.");
                    return;
                }

                try {
                    Connection con = dbconnection.getConnection();
                    String sql = "SELECT medical_id, username, Description, Sub_date, State, c_code, c_type, cut_lec_hour FROM Medical WHERE c_code = ? AND username = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, courseCode);
                    ps.setString(2,username);

                    ResultSet rs = ps.executeQuery();

                    DefaultTableModel model = new DefaultTableModel(
                            new String[]{"Medical ID", "Username", "Description", "Submitted Date", "State", "Course Code", "Course Type", "Cut Lecture Hours"}, 0
                    );

                    while (rs.next()) {
                        model.addRow(new Object[]{
                                rs.getInt("medical_id"),
                                rs.getString("username"),
                                rs.getString("Description"),
                                rs.getDate("Sub_date"),
                                rs.getString("State"),
                                rs.getString("c_code"),
                                rs.getString("c_type"),
                                rs.getInt("cut_lec_hour")
                        });
                    }

                    MedicalTable.setModel(model);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error fetching medical records.");
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c_code.setText("");
                MedicalTable.setModel(new DefaultTableModel());
            }
        });
    }

    public JPanel getViewMedicals() {
        return MedicalPanel;
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UViewMedicals().setVisible(true));
    }*/
}


