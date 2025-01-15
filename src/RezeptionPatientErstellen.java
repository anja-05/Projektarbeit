import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Locale;

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

    private PatientDAO patientDAO;
    private Patient patient;


    public RezeptionPatientErstellen(Connection connection, PatientDAO patientDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.patient = new Patient();
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
        if (validateFields()) {
            patient.setAnrede((String) AnredeComboBox.getSelectedItem());
            patient.setVorname(vornameTextField.getText());
            patient.setNachname(nachnameTextField.getText());
            try {
                String geburtsdatumString = geburtsdatumTextField.getText();
                Date geburtsdatum = Date.valueOf(geburtsdatumString); // java.sql.Date erstellen
                patient.setGeburtsdatum(geburtsdatum); // Funktioniert, wenn setGeburtsdatum(java.sql.Date) erwartet
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Ungültiges Datum. Bitte ein Datum im Format yyyy-MM-dd eingeben.");
                return;
            }
            try {
                patient.setSozialversicherungsnummer(Integer.parseInt(svnTextField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Die Sozialversicherungsnummer muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            patient.setVersicherung((String) versicherungComboBox.getSelectedItem());

            // Weiterleitung mit demselben Patient-Objekt
            dispose();
            RezeptionPatientKontaktdaten kontaktFenster = new RezeptionPatientKontaktdaten(patient, patientDAO);
            kontaktFenster.setFields(patient);
            kontaktFenster.setVisible(true);
        }
    }

    private boolean validateFields() {
        if (AnredeComboBox.getSelectedItem() == null || vornameTextField.getText().isEmpty()
                || nachnameTextField.getText().isEmpty() || geburtsdatumTextField.getText().isEmpty()
                || svnTextField.getText().isEmpty() || versicherungComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Pflichtfelder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void setFields(Patient patient) {
        this.patient = patient;

        if (patient != null) {
            AnredeComboBox.setSelectedItem(patient.getAnrede());
            vornameTextField.setText(patient.getVorname());
            nachnameTextField.setText(patient.getNachname());

            java.sql.Date geburtsdatum = patient.getGeburtsdatum();
            if (geburtsdatum != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                geburtsdatumTextField.setText(formatter.format(geburtsdatum)); // Formatieren und setzen
            }
            svnTextField.setText(String.valueOf(patient.getSozialversicherungsnummer()));
            versicherungComboBox.setSelectedItem(patient.getVersicherung());
        } else {
            resetFields();
        }
    }

    private void resetFields() {
        AnredeComboBox.setSelectedItem(null);
        vornameTextField.setText("");
        nachnameTextField.setText("");
        geburtsdatumTextField.setText("");
        svnTextField.setText("");
        versicherungComboBox.setSelectedItem(null);
    }
}
