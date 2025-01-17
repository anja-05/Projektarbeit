import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class PatientenBearbeiten extends JFrame {
    private JPanel contentPanel;
    private JPanel persönlicheDatenPanel;
    private JPanel kontaktdatenPanel;
    private JPanel befundPanel;
    private JPanel medikationPanel;
    private JComboBox <String>anredeComboBox1;
    private JTextField vornameField;
    private JTextField nachnameField;
    private JTextField geburtstagField;
    private JTextField svnField;
    private JComboBox <String>versicherungComboBox;
    private JTextField telefonField;
    private JTextField strasseField;
    private JTextField mailField;
    private JTextField postleitzahlField;
    private JTextField ortField;
    private JComboBox <String>bundeslandComboBox;
    private JButton änderungenÜbernehmenButton;
    private JButton abbrechenButton;
    private JButton öffnenButton;
    private JButton hinzufügenButton;
    private JButton löschenButton;
    private JButton öffnenButton1;
    private JButton hinzufügenButton1;
    private JButton löschenButton1;

    private Connection connection;

    public PatientenBearbeiten(Connection connection) {
        this.connection = connection;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    private void initializeButtonListeners() {
        änderungenÜbernehmenButton.addActionListener(new SaveButtonListener());
        abbrechenButton.addActionListener(e -> dispose());
    }

    private void initializeProperties() {
        setTitle("Patienten Bearbeiten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
    }

    private void initializeView() {
        setContentPane(contentPanel);
        pack();
    }

    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (validateFields()) {
                saveToDatabase();
                JOptionPane.showMessageDialog(null, "Daten erfolgreich gespeichert!");
            } else {
                JOptionPane.showMessageDialog(null, "Bitte alle Pflichtfelder ausfüllen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateFields() {
        if (vornameField.getText().trim().isEmpty() ||
                nachnameField.getText().trim().isEmpty() ||
                geburtstagField.getText().trim().isEmpty() ||
                svnField.getText().trim().isEmpty()) {
            return false;
        }
        if (anredeComboBox1.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Anrede aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (versicherungComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie eine Versicherung aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (bundeslandComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Bitte wählen Sie ein Bundesland aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveToDatabase() {
        String sql = "INSERT INTO patienten (anrede, vorname, nachname, geburtstag, sozialversicherungsnummer, versicherung, telefon, mail, strasse, postleitzahl, ort, bundesland) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Persönliche Daten
            preparedStatement.setString(1, anredeComboBox1.getSelectedItem().toString());
            preparedStatement.setString(2, vornameField.getText().trim());
            preparedStatement.setString(3, nachnameField.getText().trim());
            preparedStatement.setString(4, geburtstagField.getText().trim());
            preparedStatement.setString(5, svnField.getText().trim());
            preparedStatement.setString(6, versicherungComboBox.getSelectedItem().toString());
            // Kontaktdaten
            preparedStatement.setString(7, telefonField.getText().trim());
            preparedStatement.setString(8, mailField.getText().trim());
            preparedStatement.setString(9, strasseField.getText().trim());
            preparedStatement.setString(10, postleitzahlField.getText().trim());
            preparedStatement.setString(11, ortField.getText().trim());
            preparedStatement.setString(12, bundeslandComboBox.getSelectedItem().toString());

            // Ausführen der SQL-Abfrage
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Daten wurden erfolgreich gespeichert!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Daten.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Daten konnten nicht gespeichert werden: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
