import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import welcome.*;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            //UIManager.setLookAndFeel(new FlatDarkPurpleIJTheme());
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

            new WelcomePage();
        }

}