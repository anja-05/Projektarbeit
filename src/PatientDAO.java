import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAO {
    private final Connection connection;

    public PatientDAO(Connection connection){
        this.connection = connection;
    }

    public int getBundeslandID(String bundesland) throws SQLException {
        String query = "SELECT bundeslandID FROM bundesland WHERE bezeichnung = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, bundesland);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("bundeslandID");
            }
        }
        return -1; // Wenn nicht gefunden
    }

    public int getKrankenkassenID(String versicherung) throws SQLException {
        String query = "SELECT krankenkassenID FROM krankenkasse WHERE bezeichnung = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, versicherung);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("krankenkassenID");
            }
        }
        return -1; // Wenn nicht gefunden
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean savePatient(Patient patient) {
        String query = "INSERT INTO patient (anrede, vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail, bundeslandID, krankenkassenID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, patient.getAnrede());
            preparedStatement.setString(2, patient.getVorname());
            preparedStatement.setString(3, patient.getNachname());
            preparedStatement.setDate(4, patient.getGeburtsdatum());
            preparedStatement.setInt(5, patient.getSozialversicherungsnummer());
            preparedStatement.setString(6, patient.getStrasse());
            preparedStatement.setInt(7, patient.getPostleitzahl());
            preparedStatement.setString(8, patient.getOrt());
            preparedStatement.setString(9, patient.getTelefon());
            preparedStatement.setString(10, patient.getMail());
            preparedStatement.setInt(11, getBundeslandID(patient.getBundesland()));
            preparedStatement.setInt(12, getKrankenkassenID(patient.getVersicherung()));

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                // Abrufen der automatisch generierten ID
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        patient.setPatientID(generatedKeys.getInt(1)); // Setze die generierte ID im Patient-Objekt
                        System.out.println("Patient erfolgreich gespeichert mit ID: " + patient.getpatientID());
                    } else {
                        throw new SQLException("Fehler beim Abrufen der generierten ID.");
                    }
                }
                return true; // Speichern war erfolgreich
            }
            return false; // Kein Datensatz eingefügt
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Fehler beim Speichern
        }
    }

    //Methode zum Erstellen eines neuen Patienten
    public void createPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO patient (vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getVorname());
            stmt.setString(2, patient.getNachname());
            stmt.setDate(3, patient.getGeburtsdatum());
            stmt.setInt(4, patient.getSozialversicherungsnummer());
            stmt.setString(5, patient.getStrasse());
            stmt.setInt(6, patient.getPostleitzahl());
            stmt.setString(7, patient.getOrt());
            stmt.setString(8, patient.getTelefon());
            stmt.setString(9, patient.getMail());
            stmt.executeUpdate();
        }
    }
}
