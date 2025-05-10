package toofficer;
import student.*;
import admin.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class TechOfficer extends JFrame {
    private JPanel MainPanel;
    private JButton myProfileButton;
    private JButton addAttendanceButton;
    private JButton addMedicalButton;
    private JButton viewNoticeButton;
    private JButton viewTimetableButton;
    private JButton logOutButton;
    private JPanel btnPanal;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    int userid;


    public TechOfficer(int userid) {
        this.userid = userid;
        setTitle("Tech Officer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        setContentPane(MainPanel);
        setVisible(true);

       /* editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditProfile eProfile = new EditProfile("TO1005");
                contentPanel.removeAll();
                contentPanel.add(eProfile.getEditprofile());
                contentPanel.revalidate();
                contentPanel.repaint();

        }); }*/
        addAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddAttendance eAttendance = new AddAttendance();
                contentPanel.removeAll();
                contentPanel.add(eAttendance.getAddAttMain());
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
       myProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UProfile mProfile = null;
                try {
                    mProfile = new UProfile(userid);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                contentPanel.removeAll();
                contentPanel.add(mProfile.getProfile());
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
       /* viewMedicalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMedical vMedical = new ViewMedical();
                contentPanel.removeAll();
                contentPanel.add(vMedical.getMainPanel());
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });*/
        addMedicalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMedicals aMedical = new AddMedicals();
                contentPanel.removeAll();
                contentPanel.add(aMedical.getAdd_panel());
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        });
        viewNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewNotices notices = new UViewNotices();
                contentPanel.removeAll();
                contentPanel.add(notices.getViewNotices());
                contentPanel.revalidate();
                contentPanel.repaint();


            }
        });
        viewTimetableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UViewTimetables timetable = new UViewTimetables();
                contentPanel.removeAll();
                contentPanel.add(timetable.getViewTimetables());
                contentPanel.revalidate();
                contentPanel.repaint();

            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();


            }
        });
    }

    /*public static void main(String[] args) {
        new TechOfficer();
    }*/


}


