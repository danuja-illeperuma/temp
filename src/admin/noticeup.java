package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class noticeup extends JFrame {
    private JTextField notice_find;
    private JButton findButton;
    private JTextField title;
    private JTextField content;
    private JTextField publishdate;
    private JButton updateButton;
    private JPanel noticuppanel;
    private JTextField publisherid;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    admindash parent;

    public noticeup(admindash parent) {
        this.parent = parent;

        setTitle("Notice update");
        setContentPane(noticuppanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //  Proper close behavior
        setSize(300,300);

        setVisible(true);

        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.noticedetail();  // Update dashboard when closed
                parent.displaynotice();
            }
        });






        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               notice_update();




            }


        });


        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notice_find();
            }
        });
    }





    void notice_find(){

        try {
            con = dbconnection.getConnection();
            if(notice_find.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please enter the notice id");
                notice_find.requestFocus();
                return;

            }

            pst = con.prepareStatement("select * from notices where notice_id = ?");
            pst.setInt(1, Integer.parseInt(notice_find.getText()));
            rs = pst.executeQuery();
            if(rs.next()){
                title.setText(rs.getString("title"));
                content.setText(rs.getString("content"));
                publishdate.setText(rs.getString("publish_date"));


            }
            else {
                JOptionPane.showMessageDialog(null, "invalid notice id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "invalid notice id");
        }

    }

    void notice_update() {
        if (title.getText().equals("") || content.getText().equals("") || publishdate.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill all the fields");
            return;
        }

        // Validate date format before database operation
        if (!isValidDateTime(publishdate.getText())) {
            JOptionPane.showMessageDialog(null,
                    "Invalid date format. Please use YYYY-MM-DD HH:MM:SS",
                    "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            try {
                con = dbconnection.getConnection();
                pst = con.prepareStatement("update notices set title = ?, content = ? ,publish_date = ?  where notice_id = ?");
                pst.setString(1, title.getText());
                pst.setString(2, content.getText());
                pst.setString(3, publishdate.getText());
                pst.setInt(4, Integer.parseInt(notice_find.getText()));
                int rs = pst.executeUpdate();

                if (rs == 1) {
                    JOptionPane.showMessageDialog(null, "notices successfully updated");

                } else {
                    JOptionPane.showMessageDialog(null, "notice not updated");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Enter the valid data type");
            }

        }


    }


    // Date validation method
    private boolean isValidDateTime(String dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false); // Strict parsing
        try {
            sdf.parse(dateTime);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}