import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

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

    private Connection connection;
    private String anrede;
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String svn;
    private String versicherung;

    public RezeptionPatientKontaktdaten(Connection connection, String anrede, String vorname, String nachname, String geburtsdatum, String svn, String versicherung) {
            this.connection = connection;
            this.anrede = anrede;
            this.vorname = vorname;
            this.nachname = nachname;
            this.geburtsdatum = geburtsdatum;
            this.svn = svn;
            this.versicherung = versicherung;
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
            speichernButton.addActionListener(this::actionPerformed);
            zurückButton.addActionListener(e -> returnToRezeptionPatientErstellen());
        }

    private void returnToRezeptionPatientErstellen() {
        dispose();
        RezeptionPatientErstellen patientErstellen = new RezeptionPatientErstellen(connection);
        patientErstellen.setVisible(true);
        patientErstellen.setFields(anrede, vorname, nachname, geburtsdatum, svn, versicherung);
    }

    private void actionPerformed(ActionEvent actionEvent) {
        String telefon = telTextField.getText();
        String mail = mailTextField.getText();
        String strasse = strasseTextField.getText();
        String postleitzahl = pznTextField.getText();
        String ort = ortTextField.getText();
        String bundesland = (String) bundeslandComboBox.getSelectedItem();

        int bundeslandID = getBundeslandID(bundesland);
        if (bundeslandID == -1) {
            JOptionPane.showMessageDialog(this, "Fehler: Bundesland nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO patient (anrede, vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail, bundeslandID, krankenkassenID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, anrede); // Anrede speichern
            preparedStatement.setString(2, vorname);
            preparedStatement.setString(3, nachname);
            preparedStatement.setString(4, geburtsdatum);
            preparedStatement.setString(5, svn);
            preparedStatement.setString(6, strasse);
            preparedStatement.setString(7, postleitzahl);
            preparedStatement.setString(8, ort);
            preparedStatement.setString(9, telefon);
            preparedStatement.setString(10, mail);
            preparedStatement.setInt(11, bundeslandID);
            preparedStatement.setInt(12, getKrankenkassenID(versicherung)); // KrankenkassenID speichern

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Patient erfolgreich gespeichert.");
                dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Daten: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getKrankenkassenID(String versicherung) {
        String query = "SELECT id FROM krankenkasse WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, versicherung);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Krankenkassen-ID: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    private int getBundeslandID(String bundesland) {
        String query = "SELECT bundeslandID FROM bundesland WHERE bezeichnung = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bundesland);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("BundeslandID");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Bundesland-ID: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }
}
