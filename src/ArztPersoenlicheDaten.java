import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    private Connection connection;
    private PatientDAO patientDAO;
    private Patient patient;


    public ArztPersoenlicheDaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;

        initializeProperties();
        initializeView();
        initializeButtonListerners();
        loadPatientData();
    }

    private void initializeProperties(){
        setTitle("Persönliche Daten bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initializeView(){
        setContentPane(contentPane);
        pack();
    }

    private void initializeButtonListerners(){
        speichernButton.addActionListener(this::saveChanges);
        abbrechenButton.addActionListener(e -> dispose());
    }

    private void loadPatientData() {
        anredeComboBox.setSelectedItem(patient.getAnrede());
        vornameTextField.setText(patient.getVorname());
        nachnameTextField.setText(patient.getNachname());
        geburtsdatumTextField.setText(patient.getGeburtsdatum().toString());
        svnTextField.setText(String.valueOf(patient.getSozialversicherungsnummer()));
        versicherungComboBox.setSelectedItem(patient.getVersicherung());
    }

    private void saveChanges(ActionEvent e) {
        try {
            // Patient-Daten aktualisieren
            patient.setAnrede((String) anredeComboBox.getSelectedItem());
            patient.setVorname(vornameTextField.getText());
            patient.setNachname(nachnameTextField.getText());
            patient.setGeburtsdatum(java.sql.Date.valueOf(geburtsdatumTextField.getText()));
            patient.setSozialversicherungsnummer(Integer.parseInt(svnTextField.getText()));
            patient.setVersicherung((String) versicherungComboBox.getSelectedItem());

            // Daten speichern
            if (patientDAO.updatePatient(patient)) {
                JOptionPane.showMessageDialog(this, "Daten erfolgreich gespeichert.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}

