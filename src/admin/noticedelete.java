package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class noticedelete extends JFrame{
    private JTextField notice_del;
    private JButton deleteButton;
    private JPanel notice_delpanel;
    admindash parent;

    noticedelete(admindash parent){
        this.parent = parent;

        setTitle("Notice delete");
        setContentPane(notice_delpanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //  Proper close behavior
        setSize(200,200);

        setVisible(true);


        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.noticedetail();  // Update dashboard when closed
                parent.displaynotice();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(notice_del.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a notice_id");
                }
                else{
                    Connection con = null;
                    try {
                        con = dbconnection.getConnection();
                        PreparedStatement ps = con.prepareStatement("delete from notices where notice_id = ?");
                        ps.setInt(1, Integer.parseInt(notice_del.getText()));
                        int rs = ps.executeUpdate();
                        if (rs ==1) {
                            JOptionPane.showMessageDialog(null, "Notices have been deleted");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Invalid notice_id");
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "Invalid notice_id");
                    }

                }
            }
        });
    }




}
