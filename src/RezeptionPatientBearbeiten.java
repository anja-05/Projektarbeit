import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RezeptionPatientBearbeiten extends JFrame {
    private JPanel contentPane;
    private JComboBox AnredeComboBox;
    private JTextField VornameTextField;
    private JTextField nachnameTextField;
    private JTextField geburtsdatumTextField;
    private JTextField svnTextField;
    private JComboBox VersicherungComboBox;
    private JButton abbrechenButton;
    private JButton weiterButton;

    private Connection connection;
    private PatientDAO patientDAO;
    private Patient patient;

    public RezeptionPatientBearbeiten(Connection connection, PatientDAO patientDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.patient = new Patient();
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    private void initializeProperties() {
        setTitle("Persönliche Daten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }

    private void initializeButtonListeners() {
        weiterButton.addActionListener(this::actionPerformed);
        abbrechenButton.addActionListener(e -> returnToRezeptionMenu());
    }

    private void returnToRezeptionMenu() {
        this.dispose();
        RezeptionMenu rezeptionMenu = new RezeptionMenu(connection, patientDAO);
        rezeptionMenu.setVisible(true);
    }

    private void actionPerformed(ActionEvent actionEvent) {
        if (validateFields()) {
            patient.setAnrede((String) AnredeComboBox.getSelectedItem());
            patient.setVorname(VornameTextField.getText());
            patient.setNachname(nachnameTextField.getText());
            try {
                String geburtsdatumString = geburtsdatumTextField.getText();
                Date geburtsdatum = Date.valueOf(geburtsdatumString); // java.sql.Date erstellen
                patient.setGeburtsdatum(geburtsdatum); // Funktioniert, wenn setGeburtsdatum(java.sql.Date) erwartet
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Ungültiges Datum. Bitte ein Datum im Format yyyy-MM-dd eingeben.");
                return;
            }
            try {
                patient.setSozialversicherungsnummer(Integer.parseInt(svnTextField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Die Sozialversicherungsnummer muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            patient.setVersicherung((String) VersicherungComboBox.getSelectedItem());

            dispose();
            RezeptionPatientBearbeitenKontaktdaten kontaktFenster = new RezeptionPatientBearbeitenKontaktdaten(patient, patientDAO);
            kontaktFenster.setFields(patient);
            kontaktFenster.setVisible(true);
        }
    }

    private boolean validateFields() {
        if (AnredeComboBox.getSelectedItem() == null || VornameTextField.getText().isEmpty()
                || nachnameTextField.getText().isEmpty() || geburtsdatumTextField.getText().isEmpty()
                || svnTextField.getText().isEmpty() || VersicherungComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Bitte füllen Sie alle Pflichtfelder aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void setFields(Patient patient) {
        this.patient = patient;

        if (patient != null) {
            AnredeComboBox.setSelectedItem(patient.getAnrede());
            VornameTextField.setText(patient.getVorname());
            nachnameTextField.setText(patient.getNachname());

            java.sql.Date geburtsdatum = patient.getGeburtsdatum();
            if (geburtsdatum != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                geburtsdatumTextField.setText(formatter.format(geburtsdatum)); // Formatieren und setzen
            }
            svnTextField.setText(String.valueOf(patient.getSozialversicherungsnummer()));
            VersicherungComboBox.setSelectedItem(patient.getVersicherung());
        } else {
            resetFields();
        }
    }

    private void resetFields() {
        AnredeComboBox.setSelectedItem(null);
        VornameTextField.setText("");
        nachnameTextField.setText("");
        geburtsdatumTextField.setText("");
        svnTextField.setText("");
        VersicherungComboBox.setSelectedItem(null);
    }

}
