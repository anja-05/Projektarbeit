import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;

public class RezeptionPatientErstellen extends JFrame {
    private JPanel contentPane;
    private JComboBox AnredeComboBox;
    private JTextField vornameTextField;
    private JTextField nachnameTextField;
    private JTextField geburtsdatumTextField;
    private JTextField svnTextField;
    private JComboBox versicherungComboBox;
    private JButton weiterButton;
    private JButton abbrechenButton;

    private Connection connection;

    private String anrede;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String svn;
    private String versicherung;

    private PatientDAO patientDAO;
    
    public RezeptionPatientErstellen(Connection connection, PatientDAO patientDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
        resetFields();
    }

    private void initializeProperties() {
        setTitle("Persönliche Daten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }
    private void initializeButtonListeners() {
        weiterButton.addActionListener(this::actionPerformed);
        abbrechenButton.addActionListener(e -> returnToRezeptionMenu());
    }

    private void returnToRezeptionMenu() {
        this.dispose();
        RezeptionMenu rezeptionMenu = new RezeptionMenu(connection);
        rezeptionMenu.setVisible(true);
    }

    private void actionPerformed(ActionEvent actionEvent) {
        anrede = (String) AnredeComboBox.getSelectedItem();
        vorname = vornameTextField.getText();
        nachname = nachnameTextField.getText();
        geburtsdatum = geburtsdatumTextField.getText();
        svn = svnTextField.getText();
        versicherung = (String) versicherungComboBox.getSelectedItem();

        Patient patient = new Patient();
        patient.setAnrede(anrede);
        patient.setVorname(vorname);
        patient.setNachname(nachname);
        patient.setGeburtsdatum(geburtsdatum);
        patient.setSozialversicherungsnummer(Integer.parseInt(svn));
        patient.setVersicherung(versicherung);

        this.dispose();
        RezeptionPatientKontaktdaten kontaktFenster = new RezeptionPatientKontaktdaten(patient, patientDAO);
        kontaktFenster.setVisible(true);
    }

    public void setFields(String anrede, String vorname, String nachname, String geburtsdatum, String svn, String versicherung) {
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.svn = svn;
        this.versicherung = versicherung;

        AnredeComboBox.setSelectedItem(anrede);
        vornameTextField.setText(vorname);
        nachnameTextField.setText(nachname);
        geburtsdatumTextField.setText(geburtsdatum);
        svnTextField.setText(svn);
        versicherungComboBox.setSelectedItem(versicherung);
    }

    private void resetFields() {
        anrede = "";
        vorname = "";
        nachname = "";
        geburtsdatum = "";
        svn = "";
        versicherung = "";

        AnredeComboBox.setSelectedItem(null);
        vornameTextField.setText("");
        nachnameTextField.setText("");
        geburtsdatumTextField.setText("");
        svnTextField.setText("");
        versicherungComboBox.setSelectedItem(null);
    }
}
