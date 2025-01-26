import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 *Ermöglicht das Bearbeiten der Kontaktdaten(Telefon, E-Mail, etc.) eines Patienten
 */
public class RezeptionPatientBearbeitenKontaktdaten extends JFrame {
    private JPanel contentPane;
    private JTextField telefonTextField;
    private JTextField mailTextField;
    private JTextField strasseTextField;
    private JTextField postleitzahlTextField;
    private JTextField ortTextField;
    private JComboBox bundeslandComboBox;
    private JButton zurückButton;
    private JButton speichernButton;

    private Patient patient;
    private PatientDAO patientDAO;

    /**
     * Konstruktor für die Bearbeitung der Kontaktdaten eines Patienten
     *
     * @param patient    Patient, dessen Kontaktdaten bearbeitet werden sollen
     * @param patientDAO DAO-Objekt für den Zugriff auf Patientendaten
     */
    public RezeptionPatientBearbeitenKontaktdaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters (Titel, Größe, Schließverhalten, Position)
     */
    private void initializeProperties() {
        setTitle("Kontaktdaten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
     * Initialisiert die Event-Listener für die Buttons (speichernButton, zurückButton)
     */
    private void initializeButtonListeners() {
        speichernButton.addActionListener(this::speichernPerformed);
        zurückButton.addActionListener(e -> returnToRezeptionPatientBeareiten());
    }

    /**
     * Kehrt zum RezeptionPatientBearbeiten Fenster zurück und speichert die aktuellen Eingaben im Patient-Objekt
     */
    private void returnToRezeptionPatientBeareiten() {
        saveCurrentFieldsToPatient();
        dispose();
        RezeptionPatientErstellen patientErstellenFenster = new RezeptionPatientErstellen(patientDAO.getConnection(), patientDAO);
        patientErstellenFenster.setFields(patient);
        patientErstellenFenster.setVisible(true);
    }

    /**
     * Speichert die aktuellen Werte der Eingabefelder in das Patient-Objekt
     * Falls eine ungültige Postleitzahl eingegeben wird, wird ein Standardwert gesetzt
     */
    public void saveCurrentFieldsToPatient() {
        patient.setTelefon(telefonTextField.getText());
        patient.setMail(mailTextField.getText());
        patient.setStrasse(strasseTextField.getText());
        try {
            patient.setPostleitzahl(Integer.parseInt(postleitzahlTextField.getText()));
        } catch (NumberFormatException e) {
            patient.setPostleitzahl(0);
            JOptionPane.showMessageDialog(this, "Ungültige Postleitzahl. Das Feld wurde auf 0 gesetzt.", "Warnung", JOptionPane.WARNING_MESSAGE);
        }
        patient.setOrt(ortTextField.getText());
        patient.setBundesland((String) bundeslandComboBox.getSelectedItem());
    }

    /**
     * Setzt die Kontaktdaten des Patienten in die GUI-Eingabefelder
     *
     * @param patient Patient, dessen Daten angezeigt werden sollen
     */
    public void setFields(Patient patient) {
        this.patient = patient;

        if (patient != null) {
            telefonTextField.setText(patient.getTelefon());
            mailTextField.setText(patient.getMail());
            strasseTextField.setText(patient.getStrasse());
            postleitzahlTextField.setText(patient.getPostleitzahl() == 0 ? "" : String.valueOf(patient.getPostleitzahl()));
            ortTextField.setText(patient.getOrt());
            bundeslandComboBox.setSelectedItem(patient.getBundesland());
        }
    }

    /**
     * Verarbeitet das Speichern der eingegebenen Daten und zeigt entsprechende Fehlermeldungen/Bestätigungen an
     *
     * @param actionEvent das augelöste Event
     */
    private void speichernPerformed(ActionEvent actionEvent) {
        Object selectedItem = bundeslandComboBox.getSelectedItem();
        if (selectedItem == null || selectedItem.toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Bundesland aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        saveCurrentFieldsToPatient();

        new Thread(new SavePatientDataTask()).start();
    }

    /**
     * Runnable-Implementierung für das Speichern der Patientendaten
     */
    private class SavePatientDataTask implements Runnable {
        @Override
        public void run() {
            try {
                boolean success = patientDAO.updatePatient(patient);
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        JOptionPane.showMessageDialog(RezeptionPatientBearbeitenKontaktdaten.this, "Daten erfolgreich gespeichert.");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(RezeptionPatientBearbeitenKontaktdaten.this, "Fehler beim Speichern des Patienten.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(RezeptionPatientBearbeitenKontaktdaten.this, "Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE)
                );
            }
        }
    }
}
