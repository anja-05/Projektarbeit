import java.sql.*;

public class CRUD_Operationen {
    private final Connection con;

    public CRUD_Operationen(Connection con) {
        this.con = con;
    }

    public void neuenPatientenAnlegen(Patient patient) {
       String daten = "INSERT INTO patienten (vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(daten, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getVorname());
            statement.setString(2, patient.getNachname());
            statement.setString(3, patient.getGeburtsdatum());
            statement.setInt(4, patient.getSvNummer());
            statement.setString(5, patient.getStrasse());
            statement.setInt(6, patient.getPLZ());
            statement.setString(7, patient.getOrt());
            statement.setString(8, patient.getTelefon());
            statement.setString(9, patient.getMail());
            statement.executeUpdate();
            System.out.println("Patient erfolgreich eingefügt.");

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                patient.setId(generatedId);
                System.out.println("Patient wurde erfolgreich eingefügt. ID lautet: " + generatedId);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Einfügen: " + e.getMessage());
        }
    }

    public void aktualisierePatient(Patient patient) {
        String daten = "UPDATE patienten SET vorname = ?, nachname = ?, geburtsdatum = ?, sozialversicherungsnummer = ?, strasse = ?, postleitzahl = ?, ort = ?, telefon = ?, mail = ?, WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(daten)) {
            statement.setString(1, patient.getVorname());
            statement.setString(2, patient.getNachname());
            statement.setString(3, patient.getGeburtsdatum());
            statement.setInt(4, patient.getSvNummer());
            statement.setString(5, patient.getStrasse());
            statement.setInt(6, patient.getPLZ());
            statement.setString(7, patient.getOrt());
            statement.setString(8, patient.getTelefon());
            statement.setString(9, patient.getMail());
            statement.executeUpdate();
            System.out.println("Patient erfolgreich aktualisiert.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Aktualisieren: " + e.getMessage());
        }
    }

    public void loeschePatient(int id) {
        String daten = "DELETE FROM patienten WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(daten)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Patient erfolgreich gelöscht.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen: " + e.getMessage());
        }
    }

    public void patientSpeichern(Patient patient) {
        if (patient.getId() == 0) {
            neuenPatientenAnlegen(patient);
        } else {
            aktualisierePatient(patient);
        }
    }
}