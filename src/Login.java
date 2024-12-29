import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Dieser Code beschreibt eine GUI Anwendung für einen Login Prozess auf die Patientendatenbank.
 * Es prüft die Eingabe und zeigt eine entsprechende Erfolgsnachricht
 */
public class Login extends JFrame {
    private JPanel contentPane;
    private JTextField userField;
    private JButton OKButton;
    private JButton abbrechenButton;
    private JPasswordField passwordField;

    private Main main;

    /**
     * Konstruktor, welcher die Benutzeroberfläche, die Eigenschaften und die EventListener initialisiert
     */
    public Login(Main main) {
        this.main = main;
        initializeProperties();

        initializeButtonListeners();

        initializeView();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters (Titel und wie es sich schließt)
     */
        private void initializeProperties() {
            setTitle("Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500,500);
        }

    /**
     * Initialisiert die ActionListener für die Buttons
     */
        private void initializeButtonListeners() {
            // OKButton Listener
            OKButton.addActionListener(this::actionPerformed);

            // AbbrechenButton Listener
            abbrechenButton.addActionListener(e -> System.exit(0)); // Beendet die Anwendung
        }

    /**
     * Initialisiert die Benutzeroberfläche und fügt contentPane zum Fenster hinzu
     */
        private void initializeView() {
            setContentPane(contentPane); // Das Panel aus der Swing UI Designer Datei setzen
            pack(); // Fenstergröße anpassen
        }

    /**
     * Initialisiert die EventListener für die Buttons (OK-Button überprüft Eingabe, AbbrechenButton beendet Anwendung)
     */
    private void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("Arzt") && password.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Login erfolgreich!");
            main.showMainMenu();
        } else {
            JOptionPane.showMessageDialog(this, "Ungültige Anmeldedaten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}



