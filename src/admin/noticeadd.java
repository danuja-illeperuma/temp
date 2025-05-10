package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class noticeadd extends JFrame{
    private JButton ADDButton;
    private JTextField title;
    private JTextField content;
    private JTextField publishdate;
    private JTextField publishid;
    private JPanel noitcmainpanel;
    Connection con;
    PreparedStatement pst;
    int rs;
    int userid;
    admindash parent;
    public noticeadd(admindash parent) {

        this.parent = parent;


        setTitle("Notice add");
        setContentPane(noitcmainpanel);
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






        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(title.getText().equals("") || content.getText().equals("") || publishdate.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all the fields");
                    return;
                }

                // Validate date format before database operation
                if (!isValidDateTime(publishdate.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid date format. Please use YYYY-MM-DD HH:MM:SS",
                            "Date Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                try {
                    con = dbconnection.getConnection();
                    pst = con.prepareStatement("insert into notices(title,content,publish_date) values(?,?,?)");
                    pst.setString(1, title.getText());
                    pst.setString(2, content.getText());
                    pst.setString(3, publishdate.getText());


                    rs = pst.executeUpdate();
                    if(rs ==1) {
                        JOptionPane.showMessageDialog(null, "Added Successfully");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Not Added Successfully");
                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Please enter a number");
                }

            }
        });







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
