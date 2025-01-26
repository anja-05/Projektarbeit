import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Die Klasse ermöglicht die Bearbeitung der persönlichen Daten eines Patienten
 */
public class RezeptionPatientBearbeiten extends JFrame {
    private JPanel contentPane;
    private JComboBox AnredeComboBox;
    private JTextField VornameTextField;
    private JTextField nachnameTextField;
    private JTextField geburtsdatumTextField;
    private JTextField svnTextField;
    private JComboBox VersicherungComboBox;
    private JButton abbrechenButton;
    private JButton weiterButton;

    private Connection connection;
    private PatientDAO patientDAO;
    private Patient patient;

    /**
     * Konstruktor
     * @param connection datenbankverbindung
     * @param patientDAO DAO-Objekt für den Zugriff auf Patientendaten
     */
    public RezeptionPatientBearbeiten(Connection connection, PatientDAO patientDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.patient = new Patient();
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters (Größe, Titel, Schliesverhalten, Position)
     */
    private void initializeProperties() {
        setTitle("Persönliche Daten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    /**
     * Setzt den Inhalt des Fensters
     */
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }

    /**
     * Initialisiert die Event-Listener für die Buttons (weiterButton, abbrechenButton)
     */
    private void initializeButtonListeners() {
        weiterButton.addActionListener(this::actionPerformed);
        abbrechenButton.addActionListener(e -> returnToRezeptionMenu());
    }

    /**
     * Wechselt zurück zum Hauptmenü der Rezeption und schließt das aktuelle Fenster
     */
    private void returnToRezeptionMenu() {
        new Thread(new ReturnToMenuTask()).start();
    }

    /**
     * Runnable-Klasse zur Navigation zurück zum Hauptmenü
     */
    private class ReturnToMenuTask implements Runnable {
        @Override
        public void run() {
            try {
                SwingUtilities.invokeLater(() -> dispose());
                RezeptionMenu rezeptionMenu = new RezeptionMenu(connection, patientDAO);
                SwingUtilities.invokeLater(() -> rezeptionMenu.setVisible(true));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(RezeptionPatientBearbeiten.this,
                                "Fehler beim Laden des Hauptmenüs: " + ex.getMessage(),
                                "Fehler", JOptionPane.ERROR_MESSAGE));
            }
        }
    }

    /**
     * Speichert die eingegebenen Daten und wechselt zum Fenster RezeptionPatientBearbeitenKontaktdaten
     * @param actionEvent ausgelöstes Event
     */
    private void actionPerformed(ActionEvent actionEvent) {
        if (validateFields()) {
            new Thread(new SaveAndNavigateTask()).start();
        }
    }

    /**
     * Runnable-Klasse zum Speichern der Daten und Weiterleitung
     */
    private class SaveAndNavigateTask implements Runnable {
        @Override
        public void run() {
            try {
                patient.setAnrede((String) AnredeComboBox.getSelectedItem());
                patient.setVorname(VornameTextField.getText());
                patient.setNachname(nachnameTextField.getText());
                try {
                    String geburtsdatumString = geburtsdatumTextField.getText();
                    Date geburtsdatum = Date.valueOf(geburtsdatumString);
                    patient.setGeburtsdatum(geburtsdatum);
                } catch (IllegalArgumentException e) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(RezeptionPatientBearbeiten.this,
                                    "Ungültiges Datum. Bitte ein Datum im Format yyyy-MM-dd eingeben.",
                                    "Fehler", JOptionPane.ERROR_MESSAGE));
                    return;
                }
                try {
                    patient.setSozialversicherungsnummer(Integer.parseInt(svnTextField.getText()));
                } catch (NumberFormatException ex) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(RezeptionPatientBearbeiten.this,
                                    "Die Sozialversicherungsnummer muss eine Zahl sein.",
                                    "Fehler", JOptionPane.ERROR_MESSAGE));
                    return;
                }
                patient.setVersicherung((String) VersicherungComboBox.getSelectedItem());

                SwingUtilities.invokeLater(() -> {
                    dispose();
                    RezeptionPatientBearbeitenKontaktdaten kontaktFenster = new RezeptionPatientBearbeitenKontaktdaten(patient, patientDAO);
                    kontaktFenster.setFields(patient);
                    kontaktFenster.setVisible(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(RezeptionPatientBearbeiten.this,
                                "Ein unbekannter Fehler ist aufgetreten: " + ex.getMessage(),
                                "Fehler", JOptionPane.ERROR_MESSAGE));
            }
        }
    }

    /**
     * Validiert die Eingabefelder
     * @return true, wenn alle Felder gültig sind, sonst false
     */
    private boolean validateFields() {
        if (AnredeComboBox.getSelectedItem() == null || VornameTextField.getText().isEmpty()
                || nachnameTextField.getText().isEmpty() || geburtsdatumTextField.getText().isEmpty()
                || svnTextField.getText().isEmpty() || VersicherungComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Pflichtfelder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Setzt die Daten des Patientens in die entsprechenden GUI-Felder
     * @param patient patient, dessen Daten angezeigt werden sollen
     */
    public void setFields(Patient patient) {
        this.patient = patient;

        if (patient != null) {
            try{
            AnredeComboBox.setSelectedItem(patient.getAnrede());
            VornameTextField.setText(patient.getVorname());
            nachnameTextField.setText(patient.getNachname());

            java.sql.Date geburtsdatum = patient.getGeburtsdatum();
            if (geburtsdatum != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                geburtsdatumTextField.setText(formatter.format(geburtsdatum));
            }
            svnTextField.setText(String.valueOf(patient.getSozialversicherungsnummer()));
            VersicherungComboBox.setSelectedItem(patient.getVersicherung());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Setzen der Felder: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            resetFields();
        }
    }

    /**
     * Setzt die Eingabefelder auf ihre Standardwerte zurück
     */
    private void resetFields() {
        try{
        AnredeComboBox.setSelectedItem(null);
        VornameTextField.setText("");
        nachnameTextField.setText("");
        geburtsdatumTextField.setText("");
        svnTextField.setText("");
        VersicherungComboBox.setSelectedItem(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Zurücksetzen der Felder: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
