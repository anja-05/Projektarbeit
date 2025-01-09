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
    private JComboBox<String> roleComboBox;
    private JButton OKButton;
    private JButton abbrechenButton;

    private Main main;

    public Login(Main main) {
        this.main = main;
        initializeProperties();
        initializeComponents();
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
        setLocationRelativeTo(null); // Fenster zentrieren
    }

    /**
     * Initialisiert die Swing-Komponenten.
     */
    private void initializeComponents() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());

        roleComboBox = new JComboBox<>();
        roleComboBox.addItem("Arzt");
        roleComboBox.addItem("Rezeption");

        userField = new JTextField(15);
        passwordField = new JPasswordField(15);

        OKButton = new JButton("OK");
        abbrechenButton = new JButton("Abbrechen");
    }

    /**
     * Initialisiert die Benutzeroberfläche und das Layout.
     */
    private void initializeView() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Rolle
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(new JLabel("Rolle:"), gbc);

        gbc.gridx = 1;
        contentPane.add(roleComboBox, gbc);

        // Benutzername
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(new JLabel("Benutzername:"), gbc);

        gbc.gridx = 1;
        contentPane.add(userField, gbc);

        // Passwort
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(new JLabel("Passwort:"), gbc);

        gbc.gridx = 1;
        contentPane.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(OKButton);
        buttonPanel.add(abbrechenButton);
        contentPane.add(buttonPanel, gbc);

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
        String selectedRole = (String) roleComboBox.getSelectedItem();
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        if ("Arzt".equals(selectedRole) && "Arzt".equals(username) && "1234".equals(password)) {
            JOptionPane.showMessageDialog(this, "Login erfolgreich! Willkommen, Arzt.");
            main.showArztMenu(); // Weiterleitung zum Arzt-Menü
        } else if ("Rezeption".equals(selectedRole) && "Rezeption".equals(username) && "5678".equals(password)) {
            JOptionPane.showMessageDialog(this, "Login erfolgreich! Willkommen, Rezeption.");
            main.showRezeptionMenu(); // Weiterleitung zum Rezeption-Menü
        } else {
            JOptionPane.showMessageDialog(this, "Ungültige Anmeldedaten oder Rolle.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


}


