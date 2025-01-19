import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;

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

    private Connection connection;
    private Patient patient;
    private PatientDAO patientDAO;

    public ArztKontaktdaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;

        initializeProperties();
        initializeView();
        initializeButtonListeners();
        loadPatientData();

    }

    private void initializeProperties(){
        setTitle("Kontaktdaten bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setLocationRelativeTo(null);
    }

    private void initializeView(){
        setContentPane(contentPane);
        pack();
    }

    private void initializeButtonListeners() {
        speichernButton.addActionListener(this::saveChanges);
        abbrechenButton.addActionListener(e -> dispose());
    }

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

    private void saveChanges(ActionEvent e) {
        try {
            // Speichere die eingegebenen Daten in das Patient-Objekt
            patient.setTelefon(telTextField.getText());
            patient.setMail(mailTextField.getText());
            patient.setStrasse(strasseTextField.getText());
            patient.setPostleitzahl(Integer.parseInt(pznTextField.getText()));
            patient.setOrt(ortTextField.getText());
            patient.setBundesland((String) bundeslandComboBox.getSelectedItem());

            // Speichere die Änderungen in der Datenbank
            if (patientDAO.updatePatient(patient)) {
                JOptionPane.showMessageDialog(this, "Kontaktdaten erfolgreich gespeichert.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Kontaktdaten.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Die Postleitzahl muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
