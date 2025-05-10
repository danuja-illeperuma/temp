package lecturer;
import student.UProfile;
import student.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Lecturer extends JFrame {
    private JButton profileBtn;
    private JButton courseBtn;
    private JButton uploadMarksBtn;
    private JButton viewStudentBtn;
    private JButton EligibilityBtn;
    private JButton AttendanceBtn;
    private JButton viewMarkBtn;
    private JButton viewMedicalBtn;
    private JButton viewNoticeBtn;
    private JButton logOutButton;
    private JPanel displayContent;
    private JPanel lecturerGui;
    private JPanel content;
    int userid;



    public Lecturer(int userid) {
        this.userid=userid;
        setContentPane(lecturerGui);
        setVisible(true);
        setTitle("Lecturer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setContentPane(lecturerGui);
        profile();





        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profile();
            }


        });


        courseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CourseMaterials courseMaterials = new CourseMaterials();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(courseMaterials.getCourseMaterials(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        uploadMarksBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateMarks updateMarks = new UpdateMarks();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(updateMarks.getMarks(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();

            }
        });

        viewStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentDetails studentDetails = new StudentDetails();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(studentDetails.getStudentDetails(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();

            }
        });

        EligibilityBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Eligibility eligibility = new Eligibility();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(eligibility.getEligibility(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        AttendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attendance1 viewAttendance = new Attendance1();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewAttendance.getMainPanel(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewMarkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMarks viewMarks = new ViewMarks();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewMarks.getViewMarks(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewMedicalBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMedical viewMedicals = new ViewMedical();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(viewMedicals.getMedical(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();
            }
        });

        viewNoticeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewNotices notices = new UViewNotices();
                displayContent.setLayout(new BorderLayout());
                displayContent.removeAll();
                displayContent.add(notices.getViewNotices(), BorderLayout.CENTER);
                displayContent.revalidate();
                displayContent.repaint();


            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
    }

    void profile(){
        try {
            UProfile newprofile = new UProfile(userid);
            displayContent.setLayout(new BorderLayout());
            displayContent.removeAll();
            displayContent.add(newprofile.getProfile());
            displayContent.repaint();
            displayContent.revalidate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



   /* public static void main(String[] args) {
        new Lecturer();
    }*/
}
