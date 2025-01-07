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
        return true;
    }

    private void saveToDatabase() {
        System.out.println("Speichern in die Datenbank...");
        System.out.println("Vorname: " + vornameField.getText());
        System.out.println("Nachname: " + nachnameField.getText());
        System.out.println("Geburtstag: " + geburtstagField.getText());
        System.out.println("Sozialversicherungsnummer: " + svnField.getText());
        System.out.println("Versicherung: " + versicherungComboBox.getSelectedItem());
        System.out.println("Bundesland: " + bundeslandComboBox.getSelectedItem());
    }
}
