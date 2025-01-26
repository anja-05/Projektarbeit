import javax.swing.*;
import java.sql.*;

/**
 * Die Main-Klasse ist der Einstiegspunkt für die Anwendung.
 * Sie stellt die Verbindung zur Datenbank her und verwaltet die Benutzeroberflächen
 * für den Login, das Arzt-Menü und das Rezeption-Menü.
 */
public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/datenbank_patienten";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Login loginGUI;
    private ArztMenu arztMenuGUI;
    private RezeptionMenu rezeptionMenuGUI;
    private Connection connection;
    private PatientDAO patientDAO;

    /**
     * Konstruktor
     *
     * @param connection Die Datenbankverbindung, die für die Anwendung verwendet wird.
     */
    public Main(Connection connection) {
        this.connection = connection;
    }

    /**
     * Der Einstiegspunkt der Anwendung.
     * Verbindet sich mit der Datenbank, registriert einen Shutdown-Hook und zeigt die Login-Oberfläche an.
     *
     * @param args Die Kommandozeilenargumente.
     * @throws SQLException Wenn ein Fehler beim Arbeiten mit der Datenbank auftritt.
     */
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            System.out.println("Fehler beim Herstellen der Datenbankverbindung");
            e.printStackTrace();
            return;
        }

        Connection finalConnection = connection;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (finalConnection != null && !finalConnection.isClosed()) {
                    finalConnection.close();
                    System.out.println("Datenbankverbindung geschlossen.");
                }
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Datenbankverbindung.");
                e.printStackTrace();
            }
        }));

        SwingUtilities.invokeLater(() -> {
            Main controller = new Main(finalConnection);
            controller.showLogin();
        });

    }

    /**
     * Zeigt die Login-Oberfläche an.
     * Erstellt die Login-GUI, falls sie noch nicht existiert, und setzt sie sichtbar.
     */
    public void showLogin() {
        if (loginGUI == null) {
            loginGUI = new Login(this);
        }
        loginGUI.setVisible(true);
    }

    /**
     * Zeigt die Benutzeroberfläche für Ärzte an.
     * Erstellt das Arzt-Menü, falls es noch nicht existiert, und setzt es sichtbar.
     * Schließt die Login-Oberfläche, falls sie geöffnet ist.
     */
    public void showArztMenu() {
        if (arztMenuGUI == null) {
            arztMenuGUI = new ArztMenu(connection);
        }
        arztMenuGUI.setVisible(true);
        if (loginGUI != null) {
            loginGUI.dispose();
        }
    }

    /**
     * Zeigt die Benutzeroberfläche für die Rezeption an.
     * Erstellt das Rezeption-Menü, falls es noch nicht existiert, und setzt es sichtbar.
     * Schließt die Login-Oberfläche, falls sie geöffnet ist.
     */
    public void showRezeptionMenu() {
        if (rezeptionMenuGUI == null) {
            patientDAO = new PatientDAO(connection);
            rezeptionMenuGUI = new RezeptionMenu(connection, patientDAO);
        }
        rezeptionMenuGUI.setVisible(true);
        if (loginGUI != null) {
            loginGUI.dispose();
        }
    }
}