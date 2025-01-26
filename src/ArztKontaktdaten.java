import javax.swing.*;
import java.awt.event.ActionEvent;

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
        new Thread(new SaveKontaktdatenTask()).start();
    }

    /**
     * Runnable-Implementierung für das Speichern der Kontaktdaten
     */
    private class SaveKontaktdatenTask implements Runnable {
        @Override
        public void run() {
            try {
                patient.setTelefon(telTextField.getText());
                patient.setMail(mailTextField.getText());
                patient.setStrasse(strasseTextField.getText());

                try {
                    patient.setPostleitzahl(Integer.parseInt(pznTextField.getText()));
                } catch (NumberFormatException ex) {
                    showErrorDialog("Die Postleitzahl muss eine Zahl sein.");
                    return;
                }

                patient.setOrt(ortTextField.getText());

                Object selectedItem = bundeslandComboBox.getSelectedItem();
                if (selectedItem == null || selectedItem.toString().isEmpty()) {
                    showErrorDialog("Bitte wählen Sie ein Bundesland aus.");
                    return;
                }
                patient.setBundesland(selectedItem.toString());

                boolean success = patientDAO.updateKontaktdaten(patient);
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        JOptionPane.showMessageDialog(ArztKontaktdaten.this, "Kontaktdaten erfolgreich gespeichert.");
                        dispose();
                    } else {
                        showErrorDialog("Fehler beim Speichern der Kontaktdaten.");
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Fehler: " + ex.getMessage()));
            }
        }

    }

    /**
     * Zeigt eine Fehlermeldung in einem Dialog an
     * @param message Fehlermeldung
     */
    private void showErrorDialog(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE)
        );
    }
}
