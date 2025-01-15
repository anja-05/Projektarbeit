import javax.swing.*;
import java.sql.*;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/datenbank_patienten";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Login loginGUI;
    private ArztMenu arztMenuGUI;
    private RezeptionMenu rezeptionMenuGUI;
    private Connection connection;
    private PatientDAO patientDAO;

    //Konstruktor
    public Main(Connection connection) {
        this.connection = connection;
    }

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
        SwingUtilities.invokeLater(() -> {
            Main controller = new Main(finalConnection);
            controller.showLogin();
        });

    }

    //Login
    public void showLogin() {
        if (loginGUI == null) {
            loginGUI = new Login(this);
        }
        loginGUI.setVisible(true);
    }

    //Menu
    public void showArztMenu() {
        if (arztMenuGUI == null) {
            arztMenuGUI = new ArztMenu(connection);
        }
        arztMenuGUI.setVisible(true);
        if (loginGUI != null) {
            loginGUI.dispose();
        }
    }

    public void showRezeptionMenu() {
        if (rezeptionMenuGUI == null) {
            rezeptionMenuGUI = new RezeptionMenu(connection, patientDAO);
        }
        rezeptionMenuGUI.setVisible(true);
        if (loginGUI != null) {
            loginGUI.dispose();
        }
    }

    //Verbindung schließen
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Datenbankverbindung geschlossen.");
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Schließen der Datenbankverbindung.");
            e.printStackTrace();
        }
    }
}