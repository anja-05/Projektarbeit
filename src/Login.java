import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Dieser Code beschreibt eine GUI Anwendung für einen Login Prozess auf die Patientendatenbank.
 * Es prüft die Eingabe und zeigt eine entsprechende Erfolgsnachricht
 */
public class Login extends JFrame {
    private JPanel contentPane;
    private JTextField userField;
    //private JTextField passwortField;
    private JButton OKButton;
    private JButton abbrechenButton;
    private JPasswordField passwordField;

    /**
     * Konstruktor, welcher die Benutzeroberfläche, die Eigenschaften und die EventListener initialisiert
     */
    public Login() {
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
     * Hauptmethode zum Starten der Anwendung.
     * Erstellt eine Instanz von Login und zeigt das Fenster an
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                Login frame = new Login();
                frame.setSize(400, 200); // Fenstergröße festlegen
                frame.setLocationRelativeTo(null); // Zentrieren
                frame.setVisible(true);
            });
        }
    /**
     * Initialisiert die EventListener für die Buttons (OK-Button überprüft Eingabe, AbbrechenButton beendet Anwendung)
     */
    private void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("Arzt") && password.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Login erfolgreich!");
        } else {
            JOptionPane.showMessageDialog(this, "Ungültige Anmeldedaten.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}



