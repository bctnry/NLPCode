import javax.swing.*;

public class FrmMainOutput implements DebugOutput {
    private JTextArea frame;
    public FrmMainOutput(JTextArea frmMain) {
        this.frame = frmMain;
    }
    public void print(String string) {
        frame.setText(frame.getText() + string);
    }
    public void println(String string) {
        this.print(string + "\n");
    }
}
