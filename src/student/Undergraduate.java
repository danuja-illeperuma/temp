package student;/*import javax.swing.*;
*/


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Undergraduate extends JFrame {
    private JPanel undergraduatePanel;
    private JButton profileButton;
    private JButton viewAttendanceButton;
    private JButton viewMedicalsButton;
    private JButton viewCoursesButton;
    private JButton viewGradesButton;
    private JButton viewTimetablesButton;
    private JButton viewNoticesButton;
    private JButton logoutButton;
    private JPanel displayContent;
    String username;
    int userId;

    public Undergraduate(String username,int userId) {
        this.username = username;
        this.userId = userId;
        setContentPane(undergraduatePanel);
        setVisible(true);
        setTitle("Undergraduate");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);



        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UProfile updateProfile = null; // ✅ Correct usage
                try {
                    updateProfile = new UProfile(userId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(updateProfile.getProfile(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewAttendance viewAttendance = new UViewAttendance(username);
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewAttendance.getViewAttendance(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewMedicalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewMedicals viewMedical = new UViewMedicals(username);
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewMedical.getViewMedicals(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewCoursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewCourse UViewCourses = new UViewCourse();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(UViewCourses.getViewCourses(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UGrade viewGrades = new UGrade(username); // ✅ Pass userId
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewGrades.getViewGrades(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewTimetablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewTimetables viewTimetables = new UViewTimetables();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewTimetables.getViewTimetables(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewNoticesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewNotices viewNotices = new UViewNotices();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewNotices.getViewNotices(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Undergraduate("U001")); // ✅ Dummy userId
    }*/
}



