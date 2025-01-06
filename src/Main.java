import javax.swing.*;
import java.sql.*;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/datenbank_patienten";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Login loginGUI;
    private Menu mainMenuGUI;
    private Connection connection;

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
    public void showMainMenu() {
        if (mainMenuGUI == null) {
            mainMenuGUI = new Menu(connection);
            mainMenuGUI.setMainController(this);
        }
        mainMenuGUI.setVisible(true);
        if (loginGUI != null) {
            loginGUI.dispose();
        }
    }

    //Patienten-GUI anzeigen
    public void showCreatePatientGUI(){
        new CreatePatientGUI(connection);
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