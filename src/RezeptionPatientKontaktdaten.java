import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
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
    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String svn;

    public RezeptionPatientKontaktdaten(Connection connection, String vorname, String nachname, String geburtsdatum, String svn) {
            this.connection = connection;
            this.vorname = vorname;
            this.nachname = nachname;
            this.geburtsdatum = geburtsdatum;
            this.svn = svn;
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
        RezeptionPatientErstellen patientErstellen = new RezeptionPatientErstellen(
                connection,
                vorname,
                nachname,
                geburtsdatum,
                svn,
                telTextField.getText(),
                mailTextField.getText(),
                strasseTextField.getText(),
                pznTextField.getText(),
                ortTextField.getText(),
                (String) bundeslandComboBox.getSelectedItem()
        );
        patientErstellen.setVisible(true);
    }

    private void actionPerformed(ActionEvent actionEvent) {
        String telefon = telTextField.getText();
        String mail = mailTextField.getText();
        String strasse = strasseTextField.getText();
        String postleitzahl = pznTextField.getText();
        String ort = ortTextField.getText();
        String bundesland = (String) bundeslandComboBox.getSelectedItem();

        String query = "INSERT INTO patient (vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail, bundeslandID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, vorname);
            preparedStatement.setString(2, nachname);
            preparedStatement.setString(3, geburtsdatum);
            preparedStatement.setString(4, svn);
            preparedStatement.setString(5, strasse);
            preparedStatement.setString(6, postleitzahl);
            preparedStatement.setString(7, ort);
            preparedStatement.setString(8, telefon);
            preparedStatement.setString(9, mail);
            preparedStatement.setString(10, bundesland);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Patient erfolgreich gespeichert.");
                dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Daten: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
