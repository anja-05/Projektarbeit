import javax.swing.*;
import java.awt.event.ActionEvent;

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

    public RezeptionPatientBearbeitenKontaktdaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    private void initializeProperties() {
        setTitle("Kontaktdaten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }
    private void initializeButtonListeners() {
        speichernButton.addActionListener(this::speichernPerformed);
        zurückButton.addActionListener(e -> returnToRezeptionPatientBeareiten());
    }

    private void returnToRezeptionPatientBeareiten() {
        saveCurrentFieldsToPatient();
        dispose();
        RezeptionPatientErstellen patientErstellenFenster = new RezeptionPatientErstellen(patientDAO.getConnection(), patientDAO);
        patientErstellenFenster.setFields(patient);
        patientErstellenFenster.setVisible(true);
    }

    public void saveCurrentFieldsToPatient() {
        patient.setTelefon(telefonTextField.getText());
        patient.setMail(mailTextField.getText());
        patient.setStrasse(strasseTextField.getText());
        try {
            patient.setPostleitzahl(Integer.parseInt(postleitzahlTextField.getText()));
        } catch (NumberFormatException e) {
            patient.setPostleitzahl(0); // Standardwert bei ungültiger Eingabe
        }
        patient.setOrt(ortTextField.getText());
        patient.setBundesland((String) bundeslandComboBox.getSelectedItem());
    }

    public void setFields(Patient patient) {
        this.patient = patient;

        if (patient != null) {
            telefonTextField.setText(patient.getTelefon());
            mailTextField.setText(patient.getMail());
            strasseTextField.setText(patient.getStrasse());
            postleitzahlTextField.setText(patient.getPostleitzahl() == 0 ? "" : String.valueOf(patient.getPostleitzahl())); // Leeres Feld für 0
            ortTextField.setText(patient.getOrt());
            bundeslandComboBox.setSelectedItem(patient.getBundesland());
        }
    }

    private void speichernPerformed(ActionEvent actionEvent) {
        Object selectedItem = bundeslandComboBox.getSelectedItem();
        if (selectedItem == null || selectedItem.toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Bundesland aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        patient.setTelefon(telefonTextField.getText());
        patient.setMail(mailTextField.getText());
        patient.setStrasse(strasseTextField.getText());
        try {
            patient.setPostleitzahl(Integer.parseInt(postleitzahlTextField.getText()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Die Postleitzahl muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        patient.setOrt(ortTextField.getText());
        patient.setBundesland((String) bundeslandComboBox.getSelectedItem());

        saveCurrentFieldsToPatient();
        boolean success = patientDAO.updatePatient(patient);
        if (success) {
            JOptionPane.showMessageDialog(this, "Daten erfolgreich gespeichert.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern des Patienten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
