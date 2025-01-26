import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die DAO-Klasse für die Verwaltung von Diagnosen in der Datenbank.
 * Diese Klasse kapselt alle Operationen, die mit Diagnosen in der Datenbank verbunden sind.
 */
public class DiagnoseDAO {
    private Connection connection;

    /**
     * Konstruktor, der die Datenbankverbindung setzt.
     *
     * @param connection Die Verbindung zur Datenbank.
     */
    public DiagnoseDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Fügt eine neue Diagnose in die Datenbank ein.
     *
     * @param diagnose Das Diagnose-Objekt, das eingefügt werden soll.
     */
    public void addDiagnose(Diagnose diagnose) {
        String sql = "INSERT INTO Diagnose (Datum, Beschreibung, PatientenID, ICD, Diagnose) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, diagnose.getDatum());
            stmt.setString(2, diagnose.getBeschreibung());
            stmt.setInt(3, diagnose.getPatientenID());
            stmt.setString(4, diagnose.getIcd());
            stmt.setString(5, diagnose.getDiagnose());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Fehler beim Hinzufügen der Diagnose: " + e.getMessage());
        }
    }

}
