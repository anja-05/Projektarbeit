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
            e.printStackTrace();
            return;
        }

        Connection finalConnection = connection;
        SwingUtilities.invokeLater(() -> {
            Main controller = new Main(finalConnection);
            controller.showLogin();
        });

    }

    public void showLogin() {
        if (loginGUI == null) {
            loginGUI = new Login(this);
        }
        loginGUI.setVisible(true);
    }

    public void showMainMenu() {
        if (mainMenuGUI == null) {
            mainMenuGUI = new Menu(connection);
        }
        mainMenuGUI.setVisible(true);
        if (loginGUI != null) {
            loginGUI.dispose();
        }
    }

}