import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Die Klasse ermöglicht die Eingabe von den Konatktdaten eines Patienten in eime GUI-Fesnter
 * Ermöglicht auch die Speicherung der Daten eines Patietens in die Datenbank
 */
public class RezeptionPatientKontaktdaten extends JFrame {
    private JPanel contentPane;
    private JTextField telTextField;
    private JTextField mailTextField;
    private JTextField strasseTextField;
    private JTextField pznTextField;
    private JTextField ortTextField;
    private JComboBox bundeslandComboBox;
    private JButton zurückButton;
    private JButton speichernButton;

    private Patient patient;
    private PatientDAO patientDAO;

    /**
     * Konstruktor: Initialisiert die GUI und die benötigten Parameter
     * @param patient Patient, dessen Daten einegben werden
     * @param patientDAO PatientDAO-Objekt für Zugriff auf Patientendaten
     */
    public RezeptionPatientKontaktdaten(Patient patient, PatientDAO patientDAO) {
            this.patient = patient;
            this.patientDAO = patientDAO;
            initializeProperties();
            initializeView();
            initializeButtonListeners();
        }

    /**
     * Initialisiert die Eigenschaften des Fenster (Größe, Titel, Schließverhalten, Position)
     */
        private void initializeProperties() {
                setTitle("Kontaktdaten");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setSize(400, 300);
                setLocationRelativeTo(null);
        }

    /**
     * Setzt die Inhalte/Komponenten des GUI-Fensters
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
            zurückButton.addActionListener(e -> returnToRezeptionPatientErstellen());
        }

    /**
     *Kehrt zu Fenster RezeptionPatientErstellen zurpck und übergibt die aktuellen Patientendaten
     */
    private void returnToRezeptionPatientErstellen() {
        try {
            saveCurrentFieldsToPatient();
            dispose();
            RezeptionPatientErstellen patientErstellenFenster = new RezeptionPatientErstellen(patientDAO.getConnection(), patientDAO);
            patientErstellenFenster.setFields(patient);
            patientErstellenFenster.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ein Fehler ist aufgetreten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Speichert die Daten aus den Eingabefeldern in das Patient-Objekt
     */
    public void saveCurrentFieldsToPatient() {
        try {
            patient.setTelefon(telTextField.getText());
            patient.setMail(mailTextField.getText());
            patient.setStrasse(strasseTextField.getText());
            try {
                patient.setPostleitzahl(Integer.parseInt(pznTextField.getText()));
            } catch (NumberFormatException e) {
                patient.setPostleitzahl(0); // Standardwert bei ungültiger Eingabe
            }
            patient.setOrt(ortTextField.getText());
            patient.setBundesland((String) bundeslandComboBox.getSelectedItem());
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Felder im Patient-Objekt.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Setzt die Felder mit den Daten des Patienten
     * @param patient patient, dessen Daten angezeigt werden sollen
     */
    public void setFields(Patient patient) {
        try{
        this.patient = patient;

        if (patient != null) {
            telTextField.setText(patient.getTelefon());
            mailTextField.setText(patient.getMail());
            strasseTextField.setText(patient.getStrasse());
            pznTextField.setText(patient.getPostleitzahl() == 0 ? "" : String.valueOf(patient.getPostleitzahl())); // Leeres Feld für 0
            ortTextField.setText(patient.getOrt());
            bundeslandComboBox.setSelectedItem(patient.getBundesland());
        }
        else {
            resetFields();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Patientendaten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *Setzt die Eingabefelder auf die Standardwerte zurück
     */
    private void resetFields() {
        try {
        telTextField.setText("");
        mailTextField.setText("");
        strasseTextField.setText("");
        pznTextField.setText("");
        ortTextField.setText("");
        bundeslandComboBox.setSelectedItem(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Zurücksetzen der Felder.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Event für das Speichern von Patientendaten
     * @param actionEvent ausgelöstetes actionEvent
     */
    private void speichernPerformed(ActionEvent actionEvent) {
        try {
            Object selectedItem = bundeslandComboBox.getSelectedItem();
            if (selectedItem == null || selectedItem.toString().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Bundesland aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            patient.setTelefon(telTextField.getText());
            patient.setMail(mailTextField.getText());
            patient.setStrasse(strasseTextField.getText());
            try {
                patient.setPostleitzahl(Integer.parseInt(pznTextField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Die Postleitzahl muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            patient.setOrt(ortTextField.getText());
            patient.setBundesland((String) bundeslandComboBox.getSelectedItem());

            boolean success = patientDAO.savePatient(patient);
            if (success) {
                JOptionPane.showMessageDialog(this, "Patient erfolgreich gespeichert.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern des Patienten.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ein unerwarteter Fehler ist aufgetreten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
