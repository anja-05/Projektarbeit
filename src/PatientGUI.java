import javax.swing.*;
import java.awt.event.ActionEvent;

public class PatientGUI extends JFrame {
    private JPanel contentPane;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JTextField gebField;
    private JTextField svnField;
    private JTextField strasseField;
    private JTextField pznField;
    private JTextField ortField;
    private JTextField telefonField;
    private JTextField mailField;
    private JButton bestätigenButton;

    private Main main;

    public PatientGUI(Main main, String action) {
        this.main = main;
        initializeView();
        initializeProperties();
        initializeButtonListeners(action);
    }

    private void initializeProperties() {
        setTitle("Patientendaten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }

    private void initializeButtonListeners(String action) {
       // bestätigenButton.addActionListener(this::actionPerformed);
        bestätigenButton.addActionListener(e -> actionPerformed(action));
    }

    private void actionPerformed(String action) {
        String vorname = vornameField.getText();
        String nachname = nachnameField.getText();
        String geb = gebField.getText();
        String svn = svnField.getText();
        String strasse = strasseField.getText();
        String pzn = pznField.getText();
        String ort = ortField.getText();
        String telefon = telefonField.getText();
        String mail = mailField.getText();

        JOptionPane.showMessageDialog(this, action + " erfolgreich!\nVorname: " + vorname + "\nNachname: " + nachname + "\nGeburtsdatum: " + geb + "\nSozialversicherungsnummer: " + svn + "\nStraße: " + strasse + "\nPostleitzahl: " + pzn + "\nOrt: " + ort + "\nTelefonnummer: " + telefon + "\nE-Mail: " + mail);
       // main.showMainMenu();
    }

    /*private void actionPerformed(ActionEvent e) {

    }*/
}
