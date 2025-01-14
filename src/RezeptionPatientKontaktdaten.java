import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public RezeptionPatientKontaktdaten(Patient patient, PatientDAO patientDAO) {
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
            zurückButton.addActionListener(e -> returnToRezeptionPatientErstellen());
        }

    private void returnToRezeptionPatientErstellen() {
        dispose();
    }

    private void speichernPerformed(ActionEvent actionEvent) {
        patient.setTelefon(telTextField.getText());
        patient.setMail(mailTextField.getText());
        patient.setStrasse(strasseTextField.getText());
        patient.setPostleitzahl(Integer.parseInt(pznTextField.getText()));
        patient.setOrt(ortTextField.getText());
        patient.setBundesland((String) bundeslandComboBox.getSelectedItem());

        boolean success = patientDAO.savePatient(patient);
        if (success) {
            JOptionPane.showMessageDialog(this, "Patient erfolgreich gespeichert.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern des Patienten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
