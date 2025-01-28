import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * Die Klasse stellt ein GUI-Fenster zur Bearbeitung der persönlichen Daten eines Patienten bereit.
 * Benutzer können Änderungen vornehmen und die aktualisierten Daten speichern oder die Bearbeitung abbrechen.
 */
public class ArztPersoenlicheDaten extends JFrame {

    private JPanel contentPane;
    private JComboBox anredeComboBox;
    private JTextField vornameTextField;
    private JTextField nachnameTextField;
    private JTextField geburtsdatumTextField;
    private JTextField svnTextField;
    private JComboBox versicherungComboBox;
    private JButton abbrechenButton;
    private JButton speichernButton;

    private PatientDAO patientDAO;
    private Patient patient;

    /**
     * Konstruktor, der das Fenster zur Bearbeitung der persönlichen Daten eines Patienten erstellt.
     *
     * @param patient    Das Patient-Objekt, dessen persönliche Daten bearbeitet werden sollen.
     * @param patientDAO Das DAO-Objekt, das die Datenbankoperationen für Patienten unterstützt.
     */
    public ArztPersoenlicheDaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;

        initializeProperties();
        initializeView();
        initializeButtonListerners();
        loadPatientData();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters, wie Titel, Größe und Schließen-Verhalten.
     */
    private void initializeProperties() {
        setTitle("Persönliche Daten bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    /**
     * Initialisiert das GUI-Layout und setzt das Hauptinhaltspanel.
     */
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }
    /**
     * Initialisiert die ActionListener für die Buttons "Speichern" und "Abbrechen".
     */
    private void initializeButtonListerners() {
        speichernButton.addActionListener(this::saveChanges);
        abbrechenButton.addActionListener(e -> dispose());
    }
    /**
     * Lädt die aktuellen persönlichen Daten des Patienten in die Eingabefelder.
     */
    private void loadPatientData() {
        anredeComboBox.setSelectedItem(patient.getAnrede());
        vornameTextField.setText(patient.getVorname());
        nachnameTextField.setText(patient.getNachname());
        geburtsdatumTextField.setText(patient.getGeburtsdatum().toString());
        svnTextField.setText(String.valueOf(patient.getSozialversicherungsnummer()));
        versicherungComboBox.setSelectedItem(patient.getVersicherung());
    }
    /**
     * Validiert die Eingabefelder und speichert die Änderungen an den persönlichen Daten des Patienten.
     *
     * @param e Das ActionEvent, das durch das Drücken der "Speichern"-Schaltfläche ausgelöst wurde.
     */
    private void saveChanges(ActionEvent e) {
        if (validateFields()) {
            class SaveChangesTask implements Runnable {
                @Override
                public void run() {
                    try {
                        patient.setAnrede((String) anredeComboBox.getSelectedItem());
                        patient.setVorname(vornameTextField.getText());
                        patient.setNachname(nachnameTextField.getText());
                        patient.setGeburtsdatum(java.sql.Date.valueOf(geburtsdatumTextField.getText()));
                        patient.setSozialversicherungsnummer(Long.parseLong(svnTextField.getText()));
                        patient.setVersicherung((String) versicherungComboBox.getSelectedItem());

                        boolean success = patientDAO.updatePersoenlicheDaten(patient);
                        SwingUtilities.invokeLater(() -> {
                            if (success) {
                                JOptionPane.showMessageDialog(ArztPersoenlicheDaten.this, "Daten erfolgreich gespeichert.");
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(ArztPersoenlicheDaten.this, "Fehler beim Speichern.", "Fehler", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                ArztPersoenlicheDaten.this,
                                "Ungültige Eingabe: " + ex.getMessage(),
                                "Fehler",
                                JOptionPane.ERROR_MESSAGE
                        ));
                    }
                }
            }
            Thread thread = new Thread(new SaveChangesTask());
            thread.start();
        }
    }
    /**
     * Validiert die Eingabefelder, um sicherzustellen, dass keine erforderlichen Felder leer sind.
     *
     * @return true, wenn alle Felder gültig sind; false, wenn ein Feld ungültig ist.
     */
    private boolean validateFields() {
        if (anredeComboBox.getSelectedItem() == null || vornameTextField.getText().isEmpty()
                || nachnameTextField.getText().isEmpty() || geburtsdatumTextField.getText().isEmpty()
                || svnTextField.getText().isEmpty() || versicherungComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Pflichtfelder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
