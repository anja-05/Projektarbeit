import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Locale;

/**
 * Klasse zum erstellen eines Patientens
 * Ermöglicht die Eingabe von persönlichen Daten eines Patienten und leitet den Nutzer zur Eingabe der Kontaktdaten weiter
 *
 */
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

    /**
     * Konstruktor zur Initialisierung der GUI und der Daten
     * @param connection Verbindung zur Datenbank
     * @param patientDAO DAO-Objekt zur Patientenverwaltung
     */
    public RezeptionPatientErstellen(Connection connection, PatientDAO patientDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.patient = new Patient();
        initializeProperties();
        initializeView();
        initializeButtonListeners();
        resetFields();
    }

    /**
     * Initialsiert die Eigenschaften des Fensters (Titel, Größe, Schließverhalten, Position)
     */
    private void initializeProperties() {
        setTitle("Persönliche Daten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    /**
     * Initialisiert die grafische Benutzeroberfläche
     */
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }

    /**
     * Initialisiert die Action Listeners für die Buttons (weiterButton, abbrechenButton)
     */
    private void initializeButtonListeners() {
        weiterButton.addActionListener(this::actionPerformed);
        abbrechenButton.addActionListener(e -> returnToRezeptionMenu());
    }

    /**
     * Schließt das aktuelle Fenster und kehrt zum Hauptmenü der Rezeption zurück
     */
    private void returnToRezeptionMenu() {
        this.dispose();
        RezeptionMenu rezeptionMenu = new RezeptionMenu(connection, patientDAO);
        rezeptionMenu.setVisible(true);
    }

    /**
     * Verarbeitet die Aktion beim Klicken auf den weiterButton
     * Validiert die Felder und leitet den Nutzer zur nächsten Eingabe weiter.
     * @param actionEvent das actionEvent das ausgelöst wurde
     */
    private void actionPerformed(ActionEvent actionEvent) {
        if (validateFields()) {
            try {
                patient.setAnrede((String) AnredeComboBox.getSelectedItem());
                patient.setVorname(vornameTextField.getText());
                patient.setNachname(nachnameTextField.getText());
                String geburtsdatumString = geburtsdatumTextField.getText();
                try {
                    Date geburtsdatum = Date.valueOf(geburtsdatumString); // java.sql.Date erstellen
                    patient.setGeburtsdatum(geburtsdatum); // Funktioniert, wenn setGeburtsdatum(java.sql.Date) erwartet
                } catch (IllegalArgumentException e) {
                    showErrorDialog("Ungültiges Datum. Bitte ein Datum im Format yyyy-MM-dd eingeben.");
                    return;
                }
                try {
                    patient.setSozialversicherungsnummer(Integer.parseInt(svnTextField.getText()));
                } catch (NumberFormatException ex) {
                    showErrorDialog("Die Sozialversicherungsnummer muss eine Zahl sein.");
                    return;
                }
                patient.setVersicherung((String) versicherungComboBox.getSelectedItem());

                // Weiterleitung mit demselben Patient-Objekt
                dispose();
                RezeptionPatientKontaktdaten kontaktFenster = new RezeptionPatientKontaktdaten(patient, patientDAO);
                kontaktFenster.setFields(patient);
                kontaktFenster.setVisible(true);
            }catch (Exception ex) {
                showErrorDialog("Ein unerwarteter Fehler ist aufgetreten. Bitte versuchen Sie es erneut.");
            }
        }
    }

    /**
     * validiert die Pflichtfelder
     * @return true wenn alle Felder richtig eingeben wurde, sonst return false
     */
    private boolean validateFields() {
        if (AnredeComboBox.getSelectedItem() == null || vornameTextField.getText().isEmpty()
                || nachnameTextField.getText().isEmpty() || geburtsdatumTextField.getText().isEmpty()
                || svnTextField.getText().isEmpty() || versicherungComboBox.getSelectedItem() == null) {
            showErrorDialog("Bitte füllen Sie alle Pflichtfelder aus.");
            return false;
        }
        return true;
    }

    /**
     * Zeigt eine Fehlermeldung in einem Dialog an
     * @param message jeweilige Fehlermeldung, die angezeogt werden soll
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Setzt die Felder im Formular basierend auf den Patientendaten
     * @param patient Patientenobjekt mit den zu setzenden Daten
     */
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

    /**
     * Setzt alle Felder im Fenster persönliche Daten wieder zurück
     */
    private void resetFields() {
        AnredeComboBox.setSelectedItem(null);
        vornameTextField.setText("");
        nachnameTextField.setText("");
        geburtsdatumTextField.setText("");
        svnTextField.setText("");
        versicherungComboBox.setSelectedItem(null);
    }
}
