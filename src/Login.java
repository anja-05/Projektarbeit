import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    public Login() {
        // Titel des Fensters
        setTitle("Log In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new BorderLayout(10, 10)); // Hauptlayout mit Abständen

        // Überschrift oben
        JLabel titleLabel = new JLabel("Die Patientendatenbank", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // Panel für Eingabe-Felder (GridLayout mit 2 Zeilen und 1 Spalte)
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Benutzername (Label + Textfeld in einem separaten Panel)
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        JLabel userLabel = new JLabel("User:");
        JTextField userField = new JTextField();
        userPanel.add(userLabel, BorderLayout.WEST);
        userPanel.add(userField, BorderLayout.CENTER);
        inputPanel.add(userPanel);

        // Passwort (Label + Textfeld in einem separaten Panel)
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        JLabel passLabel = new JLabel("Passwort:");
        JPasswordField passField = new JPasswordField();
        passPanel.add(passLabel, BorderLayout.WEST);
        passPanel.add(passField, BorderLayout.CENTER);
        inputPanel.add(passPanel);

        add(inputPanel, BorderLayout.CENTER);

        // Panel für Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");

        // ActionListener für Login-Button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Benutzereingaben abrufen
                String username = userField.getText();
                String password = new String(passField.getPassword());

                // Überprüfung der Anmeldedaten
                if (username.equals("Projektarbeit") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(null, "Login erfolgreich! Zugriff gewährt.");
                    // Hier könntest du den Zugriff auf die Datenbank initialisieren
                } else {
                    JOptionPane.showMessageDialog(null, "Benutzername oder Passwort falsch!");
                }
            }
        });

        // ActionListener für Cancel-Button
        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // GUI erstellen und sichtbar machen
        Login login = new Login();
        login.setVisible(true);
    }
}