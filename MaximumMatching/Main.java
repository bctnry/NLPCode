import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Main {
    public static void main (String[] args) throws FileNotFoundException {
        FrmMain frmMain = new FrmMain();
        JTextArea jTextArea = frmMain.getOutputPort();
        MatchSession matchSession = new MatchSession(new FrmMainOutput(jTextArea));
        frmMain.pack();
        frmMain.setSession(matchSession);
        frmMain.setVisible(true);
        frmMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
