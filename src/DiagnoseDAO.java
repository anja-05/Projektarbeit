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

    /**
     * Lädt eine Diagnose aus der Datenbank basierend auf der Diagnose-ID.
     *
     * @param diagnoseID Die ID der Diagnose.
     * @return Das gefundene Diagnose-Objekt oder null, wenn keine Diagnose gefunden wurde.
     */
    public Diagnose getDiagnoseById(int diagnoseID) {
        String sql = "SELECT Diagnose, ICD, Beschreibung, Datum FROM Diagnose WHERE DiagnoseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, diagnoseID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Diagnose(
                            diagnoseID,
                            rs.getDate("Datum"),
                            rs.getString("Beschreibung"),
                            0, // Patienten-ID nicht in der Abfrage enthalten
                            rs.getString("ICD"),
                            rs.getString("Diagnose")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Laden der Diagnose: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Führt eine Suche nach Diagnosen durch basierend auf dem Suchtext.
     *
     * @param searchText Der Suchtext für die Diagnose.
     * @return Eine Liste von Diagnosen, die den Suchkriterien entsprechen.
     */
    public List<String> searchDiagnose(String searchText) {
        List<String> result = new ArrayList<>();
        String sql = "SELECT ICD, Diagnose FROM icddiagnose WHERE Diagnose LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String icd = rs.getString("ICD");
                    String diagnose = rs.getString("Diagnose");
                    result.add(icd + ": " + diagnose);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler bei der Diagnose-Suche: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Aktualisiert eine vorhandene Diagnose in der Datenbank.
     *
     * @param diagnose Das Diagnose-Objekt mit den aktualisierten Daten.
     */
    public void updateDiagnose(Diagnose diagnose) {
        String sql = "UPDATE Diagnose SET Diagnose = ?, ICD = ?, Beschreibung = ?, Datum = ? WHERE DiagnoseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, diagnose.getDiagnose());
            stmt.setString(2, diagnose.getIcd());
            stmt.setString(3, diagnose.getBeschreibung());
            stmt.setDate(4, diagnose.getDatum());
            stmt.setInt(5, diagnose.getDiagnoseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren der Diagnose: " + e.getMessage(), e);
        }
    }

    /**
     * Ruft eine Liste aller Diagnosen eines bestimmten Patienten aus der Datenbank ab.
     *
     * @param patientId Die ID des Patienten, für den die Diagnosen abgerufen werden sollen.
     * @return Eine Liste von Diagnose-Objekten, die die Diagnosen des Patienten repräsentieren.
     * @throws RuntimeException Wenn ein Fehler beim Datenbankzugriff auftritt.
     */
    public List<Diagnose> getDiagnoseByPatientId(int patientId) {
        List<Diagnose> diagnoseList = new ArrayList<>();
        String sql = "SELECT DiagnoseID, Datum, Beschreibung, ICD, Diagnose FROM diagnose WHERE PatientenID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Diagnose diagnose = new Diagnose(
                            rs.getInt("DiagnoseID"),
                            rs.getDate("Datum"),
                            rs.getString("Beschreibung"),
                            patientId,
                            rs.getString("ICD"),
                            rs.getString("Diagnose")
                    );
                    diagnoseList.add(diagnose);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Abrufen der Diagnosen: " + e.getMessage(), e);
        }
        return diagnoseList;
    }

}
