import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientDAO {
    private Connection connection;

    public PatientDAO(Connection connection){
        this.connection = connection;
    }

    //Methode zum Erstellen eines neuen Patienten
    public void createPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO patient (vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getVorname());
            stmt.setString(2, patient.getNachname());
            stmt.setString(3, patient.getGeburtsdatum());
            stmt.setInt(4, patient.getSvNummer());
            stmt.setString(5, patient.getStrasse());
            stmt.setInt(6, patient.getPostleitzahl());
            stmt.setString(7, patient.getOrt());
            stmt.setString(8, patient.getTelefon());
            stmt.setString(9, patient.getMail());
            stmt.executeUpdate();
        }
    }
}
