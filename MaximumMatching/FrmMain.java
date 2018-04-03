import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class FrmMain extends JFrame {
    private JPanel contentPane;
    private JButton loadDictButton;
    private JTextField textField1;
    private JTextArea textArea1;
    private JTextField textField2;
    private JButton mmButton;
    private JButton rmmButton;
    private JButton bmmButton;
    private JCheckBox singleStepDebugCheckBox;
    private JCheckBox displayLookupFailureCheckBox;
    private JCheckBox displayMatchingFailureCheckBox;
    private JCheckBox displayBothMatchingResultCheckBox;
    private JCheckBox displayDecisionLogCheckBox;
    private JCheckBox defaultRMMResultCheckBox;
    private JTextField textField3;
    private MatchSession matchSession;

    public FrmMain() {
        setContentPane(contentPane);
        loadDictButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.showOpenDialog(null);
                String dictFileName = jFileChooser.getSelectedFile().getAbsolutePath();
                textField1.setText(dictFileName);
                matchSession.setDictFileName(dictFileName);
                try {
                    matchSession.loadDictionary();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        mmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(textField3.getText().trim().contentEquals("")){
                    matchSession.displayMatchingResult(
                            matchSession.maximumMatch(textField2.getText().trim())
                    );
                } else {
                    matchSession.displayMatchingResult(
                        matchSession.maximumMatch(textField2.getText().trim(),Integer.parseInt(textField3.getText().trim()))
                    );
                }
            }
        });
        rmmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(textField3.getText().trim().contentEquals("")){
                    matchSession.displayMatchingResult(
                            matchSession.reversedMaximumMatch(textField2.getText().trim())
                    );
                } else {
                    matchSession.displayMatchingResult(
                            matchSession.reversedMaximumMatch(textField2.getText().trim(),
                                    Integer.parseInt(textField3.getText().trim()))
                    );
                }

            }
        });
        bmmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(textField3.getText().trim().contentEquals("")){
                    matchSession.displayMatchingResult(
                            matchSession.bidirectionalMaximumMatch(textField2.getText().trim())
                    );
                } else {
                    matchSession.displayMatchingResult(
                            matchSession.bidirectionalMaximumMatch(textField2.getText().trim(),
                            Integer.parseInt(textField3.getText().trim()))
                    );
                }

            }
        });
        displayDecisionLogCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                matchSession.setDisplayDecisionLog(displayDecisionLogCheckBox.isSelected());
            }
        });
        displayBothMatchingResultCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                matchSession.setDisplayBothMatchingResult(displayBothMatchingResultCheckBox.isSelected());
            }
        });

        defaultRMMResultCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                matchSession.setDefaultRMMResult(defaultRMMResultCheckBox.isSelected());
            }
        });
        singleStepDebugCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                matchSession.setSingleStepDebug(singleStepDebugCheckBox.isSelected());
            }
        });
        displayLookupFailureCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                matchSession.setDisplayLookupFailure(displayLookupFailureCheckBox.isSelected());
            }
        });
        displayMatchingFailureCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                matchSession.setDisplayMatchingFailure(displayMatchingFailureCheckBox.isSelected());
            }
        });
        JScrollPane scroll = new JScrollPane(textArea1,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        this.add(scroll);
    }

    public void setSession(MatchSession matchSession) {
        this.matchSession = matchSession;
        this.displayDecisionLogCheckBox.setSelected(matchSession.isDisplayDecisionLog());
        this.displayBothMatchingResultCheckBox.setSelected(matchSession.isDisplayBothMatchingResult());
        this.displayLookupFailureCheckBox.setSelected(matchSession.isDisplayLookupFailure());
        this.defaultRMMResultCheckBox.setSelected(matchSession.isDefaultRMMResult());
        this.singleStepDebugCheckBox.setSelected(matchSession.isSingleStepDebug());
        this.displayMatchingFailureCheckBox.setSelected(matchSession.isDisplayMatchingFailure());
    }

    public JTextArea getOutputPort() {
        return this.textArea1;
    }
}
