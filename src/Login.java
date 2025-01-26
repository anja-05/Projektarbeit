import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Login-Fenster für die Eingabe von Rolle, Benutzername und Passwort
 * Das Fenster leitet dann je nach Rolle den Benutzer zum passenden Menü weiter
 */
public class Login extends JFrame {
    private JPanel contentPane;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton OKButton;
    private JButton abbrechenButton;
    private JComboBox rolleComboBox1;

    private Main main;

    /**
     * Konstruktor: initialisiert die Oberfläche, die Eigenschaften des Festers und die Event-Listeners
     * @param main ist eine Referenz zu Main Klasse um Menü-Funktionen aufzurufen
     */
    public Login(Main main) {
        this.main = main;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters (Titel, Größe, Schließverhalten, Position)
     */
    private void initializeProperties() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    /**
     * Initialisiert die Benutzeroberfläche und das Layout
     * Swing Komponenten werden an das JPanel gebunden
     */
    private void initializeView() {
            setContentPane(contentPane);
            pack();
    }

    /**
     * Initialisiert die Button-Listener (OKButton, abbrechenButton).
     */
    private void initializeButtonListeners() {
            OKButton.addActionListener(this::actionPerformed);
            abbrechenButton.addActionListener(e -> System.exit(0));
    }

    /**
     * Aktion für den OK-Button: Überprüft die Eingabe (Rolle, User, Passwort) und leitet bei richtiger Eingabe an das entsprechnde Menü weiter
     */
    private void actionPerformed(ActionEvent e) {
        try {
            String selectedRole = (String) rolleComboBox1.getSelectedItem();
            String username = userField.getText();
            String password = new String(passwordField.getPassword());

            if (selectedRole == null || username.isBlank() || password.isBlank()) {
                throw new IllegalArgumentException("Bitte füllen Sie alle Felder aus.");
            }
            if ("Arzt".equals(selectedRole) && "Arzt".equals(username) && "1234".equals(password)) {
                JOptionPane.showMessageDialog(this, "Login erfolgreich! Willkommen, Arzt/Ärztin.");
                main.showArztMenu();
            } else if ("Rezeption".equals(selectedRole) && "Rezeption".equals(username) && "5678".equals(password)) {
                JOptionPane.showMessageDialog(this, "Login erfolgreich! Willkommen, Rezeptionist/Rezeptionistin.");
                main.showRezeptionMenu();
            } else {
                throw new SecurityException("Ungültige Anmeldedaten oder Rolle.");
            }
        }catch (IllegalArgumentException ex){
            showWarningDialog(ex.getMessage());
        }catch (SecurityException ex) {
            showErrorDialog(ex.getMessage());
        } catch (Exception ex) {
            showErrorDialog("Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage());
        }
    }

    /**
     * Zeigt eine Warnung bei Benutzereingabefehlern an
     * @param message Warnmeldung, die angezeigt werden soll
     */
    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(
                this, message, "Eingabefehler", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Zeigt eine Fehlermeldung bei Fehlern an
     * @param message Fehlermeldung, die angezeigt werden soll
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}