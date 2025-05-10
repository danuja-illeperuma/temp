package admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class timetable_delete extends JFrame{
    private JTextField timetable_del;
    private JButton deleteButton;
    private JPanel timetable_del_pane;
    admindash parent;

    public timetable_delete(admindash parent) {
        this.parent = parent;
        setTitle("Timetabe_edit");
        setContentPane(timetable_del_pane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(200,200);

        setVisible(true);

        // Add a WindowListener to detect when the window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                parent.timetable();
            }
        });








        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timetable_del.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Enter the timetable_id ");
                    return;
                }

                    try {
                        Connection con = dbconnection.getConnection();
                        PreparedStatement pst= con.prepareStatement("delete from timetables where timetable_id =?");
                        pst.setInt(1, Integer.parseInt(timetable_del.getText()));
                        int rs = pst.executeUpdate();
                        if (rs == 1){
                            JOptionPane.showMessageDialog(null,"Deleted sucess");
                        }
                        else {
                            JOptionPane.showMessageDialog(null,"time table not found");
                        }

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(null,"Enter the numeric timetable_id ");

                    }







            }

        });
    }


}
