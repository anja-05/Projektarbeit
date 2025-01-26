import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Die Klasse stellt ein GUI-Fenster zur Bearbeitung der Kontaktdaten eines Patienten bereit.
 * Die Änderungen können gespeichert oder verworfen werden
 */
public class ArztKontaktdaten extends JFrame {

    private JPanel contentPane;
    private JTextField telTextField;
    private JTextField mailTextField;
    private JTextField strasseTextField;
    private JTextField pznTextField;
    private JTextField ortTextField;
    private JComboBox bundeslandComboBox;
    private JButton abbrechenButton;
    private JButton speichernButton;

    private Patient patient;
    private PatientDAO patientDAO;

    /**
     * Konstruktor: Initialisiert die GUI und benötigte Parameter
     * @param patient Patienten-Objekt, dessen Kontaktdaten bearbeitet werden sollen
     * @param patientDAO DAO-Objekt, das die Datenbankopertationen für Patienten unterstützt
     */
    public ArztKontaktdaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;

        initializeProperties();
        initializeView();
        initializeButtonListeners();
        loadPatientData();

    }

    /**
     * Initialisiert die Eigenschaften des Fensters
     */
    private void initializeProperties(){
        setTitle("Kontaktdaten bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setLocationRelativeTo(null);
    }

    /**
     * Initialisiert das GUI Layout und setzt das Hauptinhaltspanel
     */
    private void initializeView(){
        setContentPane(contentPane);
        pack();
    }

    /**
     * Initialisert die ActionListener für die Buttons "Speichern" und "Abbrechen"
     */
    private void initializeButtonListeners() {
        speichernButton.addActionListener(this::saveChanges);
        abbrechenButton.addActionListener(e -> dispose());
    }

    /**
     * Lädt die aktuellen Kontaktdaten in die Eingabefelder des Formulars
     */
    private void loadPatientData() {
        if (patient != null) {
            telTextField.setText(patient.getTelefon());
            mailTextField.setText(patient.getMail());
            strasseTextField.setText(patient.getStrasse());
            pznTextField.setText(patient.getPostleitzahl() == 0 ? "" : String.valueOf(patient.getPostleitzahl()));
            ortTextField.setText(patient.getOrt());
            bundeslandComboBox.setSelectedItem(patient.getBundesland());
        }
    }

    /**
     * Speichert die Änderungen der Kontaktdaten des Patienten in der Datenbank.
     * Überprüft die Eingaben und aktualisiert das Patient-Objekt sowie die Datenbank.
     * @param e Das Action-Event, das durch das Drücken des "Speichern" Buttons ausgelöst wurde.
     */
    private void saveChanges(ActionEvent e) {
        new Thread(() -> {
            try {
                // Speichere die eingegebenen Daten in das Patient-Objekt
                patient.setTelefon(telTextField.getText());
                patient.setMail(mailTextField.getText());
                patient.setStrasse(strasseTextField.getText());
                patient.setPostleitzahl(Integer.parseInt(pznTextField.getText()));
                patient.setOrt(ortTextField.getText());
                Object selectedItem = bundeslandComboBox.getSelectedItem();
                if (selectedItem == null || selectedItem.toString().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Bundesland aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                patient.setBundesland(selectedItem.toString());

                // Speichere die Änderungen in der Datenbank
                boolean success = patientDAO.updateKontaktdaten(patient);
                SwingUtilities.invokeLater(() -> {
                if (success) {
                    JOptionPane.showMessageDialog(this, "Kontaktdaten erfolgreich gespeichert.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Kontaktdaten.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                });
            } catch (NumberFormatException ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Die Postleitzahl muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE)
                );
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }
}
