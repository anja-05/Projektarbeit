import javax.swing.*;
import java.awt.event.ActionEvent;


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


    public ArztPersoenlicheDaten(Patient patient, PatientDAO patientDAO) {
        this.patient = patient;
        this.patientDAO = patientDAO;

        initializeProperties();
        initializeView();
        initializeButtonListerners();
        loadPatientData();
    }


    private void initializeProperties() {
        setTitle("Persönliche Daten bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }


    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }


    private void initializeButtonListerners() {
        speichernButton.addActionListener(this::saveChanges);
        abbrechenButton.addActionListener(e -> dispose());
    }


    private void loadPatientData() {
        try {
            anredeComboBox.setSelectedItem(patient.getAnrede());
            vornameTextField.setText(patient.getVorname());
            nachnameTextField.setText(patient.getNachname());
            geburtsdatumTextField.setText(patient.getGeburtsdatum().toString());
            svnTextField.setText(String.valueOf(patient.getSozialversicherungsnummer()));
            versicherungComboBox.setSelectedItem(patient.getVersicherung());
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Patientendaten: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void saveChanges(ActionEvent e) {
        if (validateFields()) {
            new Thread(new SavePatientTask()).start();
        }
    }


    private boolean validateFields() {
        if (anredeComboBox.getSelectedItem() == null || vornameTextField.getText().isEmpty()
                || nachnameTextField.getText().isEmpty() || geburtsdatumTextField.getText().isEmpty()
                || svnTextField.getText().isEmpty() || versicherungComboBox.getSelectedItem() == null) {
            showErrorDialog("Bitte füllen Sie alle Pflichtfelder aus.");
            return false;
        }
        return true;
    }


    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Runnable-Implementierung für das Speichern der Patientendaten
     */
    private class SavePatientTask implements Runnable {
        @Override
        public void run() {
            try {
                patient.setAnrede((String) anredeComboBox.getSelectedItem());
                patient.setVorname(vornameTextField.getText());
                patient.setNachname(nachnameTextField.getText());
                patient.setGeburtsdatum(java.sql.Date.valueOf(geburtsdatumTextField.getText()));
                patient.setSozialversicherungsnummer(Integer.parseInt(svnTextField.getText()));
                patient.setVersicherung((String) versicherungComboBox.getSelectedItem());

                boolean success = patientDAO.updatePersoenlicheDaten(patient);

                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        JOptionPane.showMessageDialog(ArztPersoenlicheDaten.this, "Daten erfolgreich gespeichert.");
                        dispose();
                    } else {
                        showErrorDialog("Fehler beim Speichern.");
                    }
                });
            } catch (IllegalArgumentException ex) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Ungültige Eingabe: " + ex.getMessage()));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage()));
            }
        }
    }
}
