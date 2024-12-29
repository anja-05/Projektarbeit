import javax.swing.*;
import java.sql.*;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/datenbank_patienten";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Login loginGUI;
    private Menu mainMenuGUI;
    private Connection connection;


    public static void main(String[] args) throws SQLException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(() -> {
            Main controller = new Main();
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
/*
    public void showPatientForm(String action) {
        PatientGUI patientFormGUI = new PatientGUI(this, action);
        patientFormGUI.setVisible(true);
        if (mainMenuGUI != null) {
            mainMenuGUI.setVisible(false);
        }
    }
*/
}