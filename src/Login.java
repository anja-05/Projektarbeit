import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Login-Fenster mit stabiler Größe für die Eingabe von Rolle, Benutzername und Passwort.
 */
public class Login extends JFrame {
    private JPanel contentPane;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton OKButton;
    private JButton abbrechenButton;
    private JComboBox rolleComboBox1;
    private JLabel rolleLabel;
    private JLabel userLabel;
    private JLabel passwortLabel;

    private Main main;

    public Login(Main main) {
        this.main = main;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters (Titel und Schließverhalten).
     */
    private void initializeProperties() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    /**
     * Initialisiert die Benutzeroberfläche und das Layout.
     */
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }

    /**
     * Initialisiert die Button-Listener.
     */
    private void initializeButtonListeners() {
        OKButton.addActionListener(this::actionPerformed);
        abbrechenButton.addActionListener(e -> System.exit(0));
    }

    /**
     * Aktion für den OK-Button: Überprüft die Eingabe und leitet weiter.
     */
    private void actionPerformed(ActionEvent e) {
        String selectedRole = (String) rolleComboBox1.getSelectedItem();
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        if ("Arzt".equals(selectedRole) && "Arzt".equals(username) && "1234".equals(password)) {
            JOptionPane.showMessageDialog(this, "Login erfolgreich! Willkommen, Arzt/Ärztin.");
            main.showArztMenu(); // Weiterleitung zum Arzt-Menü
        } else if ("Rezeption".equals(selectedRole) && "Rezeption".equals(username) && "5678".equals(password)) {
            JOptionPane.showMessageDialog(this, "Login erfolgreich! Willkommen, RezeptionistIn.");
            main.showRezeptionMenu(); // Weiterleitung zum Rezeption-Menü
        } else {
            JOptionPane.showMessageDialog(this, "Ungültige Anmeldedaten oder Rolle.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}


