import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class CreatePatientGUI {
    private Connection connection;

    public CreatePatientGUI(Connection connection) {
        this.connection = connection;
        initialize();
    }

    private void initialize() {
        JFrame frame = new JFrame("Patient erstellen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Persönliche Daten
        JPanel personalDataPanel = new JPanel(new GridLayout(0, 2));
        personalDataPanel.setBorder(BorderFactory.createTitledBorder("Persönliche Daten"));

        JTextField vornameField = new JTextField();
        JTextField nachnameField = new JTextField();
        JTextField geburtsdatumField = new JTextField();
        JTextField svNummerField = new JTextField();

        personalDataPanel.add(new JLabel("Vorname: *"));
        personalDataPanel.add(vornameField);

        personalDataPanel.add(new JLabel("Nachname: *"));
        personalDataPanel.add(nachnameField);

        personalDataPanel.add(new JLabel("Geburtsdatum: *"));
        personalDataPanel.add(geburtsdatumField);

        personalDataPanel.add(new JLabel("Sozialversicherungsnummer: *"));
        personalDataPanel.add(svNummerField);

        mainPanel.add(personalDataPanel);

        // Kontaktdaten
        JPanel contactDataPanel = new JPanel(new GridLayout(0, 2));
        contactDataPanel.setBorder(BorderFactory.createTitledBorder("Kontaktdaten"));

        JTextField strasseField = new JTextField();
        JTextField postleitzahlField = new JTextField();
        JTextField ortField = new JTextField();
        JTextField telefonField = new JTextField();
        JTextField mailField = new JTextField();

        contactDataPanel.add(new JLabel("Straße:"));
        contactDataPanel.add(strasseField);

        contactDataPanel.add(new JLabel("Postleitzahl: *"));
        contactDataPanel.add(postleitzahlField);

        contactDataPanel.add(new JLabel("Ort: *"));
        contactDataPanel.add(ortField);

        contactDataPanel.add(new JLabel("Telefon:"));
        contactDataPanel.add(telefonField);

        contactDataPanel.add(new JLabel("E-Mail:"));
        contactDataPanel.add(mailField);

        mainPanel.add(contactDataPanel);

        // Speicher-Button
        JButton saveButton = new JButton("Speichern");
        saveButton.addActionListener(e -> {
            try {
                // Validierung der Eingabefelder
                if (vornameField.getText().isEmpty() || nachnameField.getText().isEmpty() || geburtsdatumField.getText().isEmpty() ||
                        svNummerField.getText().isEmpty() || postleitzahlField.getText().isEmpty() ||
                        ortField.getText().isEmpty()) {
                    throw new IllegalArgumentException("Verpflichtende Felder müssen ausgefüllt werden.");
                }

                // Patient-Objekt erstellen
                Patient patient = new Patient(
                        0,
                        vornameField.getText(),
                        nachnameField.getText(),
                        geburtsdatumField.getText(),
                        Integer.parseInt(svNummerField.getText()),
                        strasseField.getText(),
                        Integer.parseInt(postleitzahlField.getText()),
                        ortField.getText(),
                        telefonField.getText(),
                        mailField.getText()
                );

                // Patient in die Datenbank speichern
                PatientDAO patientDAO = new PatientDAO(connection);
                patientDAO.createPatient(patient);

                JOptionPane.showMessageDialog(frame, "Patient erfolgreich gespeichert!");
                frame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Fehler beim Speichern: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ungültige Eingabe bei SVNR oder Postleitzahl", "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Fehler", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(saveButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}