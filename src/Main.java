import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/datenbank_patienten";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    public static void main(String[] args) throws SQLException {

        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}