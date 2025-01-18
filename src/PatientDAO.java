import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
                        System.out.println("Patient erfolgreich gespeichert mit ID: " + patient.getPatientID());
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

    public List<Patient> getAllePatienten() throws SQLException {
        String query =
                "SELECT p.patientenID, p.anrede, p.vorname, p.nachname, p.geburtsdatum, p.sozialversicherungsnummer, " +
                "k.bezeichnung AS versicherung, p.strasse, p.postleitzahl, p.ort, p.telefon, p.mail, b.bezeichnung AS bundesland " +
                "FROM patient p " +
                "LEFT JOIN krankenkasse k ON p.krankenkassenID = k.krankenkassenID " +
                "LEFT JOIN bundesland b ON p.bundeslandID = b.bundeslandID";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Patient> patientenListe = new ArrayList<>();
            while (resultSet.next()) {
                Patient patient = new Patient();
                patient.setPatientID(resultSet.getInt("patientenID"));
                patient.setAnrede(resultSet.getString("anrede"));
                patient.setVorname(resultSet.getString("vorname"));
                patient.setNachname(resultSet.getString("nachname"));
                patient.setGeburtsdatum(resultSet.getDate("geburtsdatum"));
                patient.setSozialversicherungsnummer(resultSet.getInt("sozialversicherungsnummer"));
                patient.setVersicherung(resultSet.getString("versicherung"));
                patient.setStrasse(resultSet.getString("strasse"));
                patient.setPostleitzahl(resultSet.getInt("postleitzahl"));
                patient.setOrt(resultSet.getString("ort"));
                patient.setTelefon(resultSet.getString("telefon"));
                patient.setMail(resultSet.getString("mail"));
                patient.setBundesland(resultSet.getString("bundesland"));

                patientenListe.add(patient);
            }
            return patientenListe;
        }
    }

    public List<Patient> suchePatientenMitRegex(String regex) throws SQLException {
        List<Patient> allePatienten = getAllePatienten();
        List<Patient> gefiltertePatienten = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        for (Patient patient : allePatienten) {
            if (matchesPatientWithRegex(patient, pattern)) {
                gefiltertePatienten.add(patient);
            }
        }
        return gefiltertePatienten;
    }

    private boolean matchesPatientWithRegex(Patient patient, Pattern pattern) {
        return pattern.matcher(String.valueOf(patient.getPatientID())).find() ||
                pattern.matcher(patient.getAnrede() != null ? patient.getAnrede() : "").find() ||
                pattern.matcher(patient.getVorname() != null ? patient.getVorname() : "").find() ||
                pattern.matcher(patient.getNachname() != null ? patient.getNachname() : "").find() ||
                pattern.matcher(patient.getGeburtsdatum() != null ? patient.getGeburtsdatum().toString() : "").find() ||
                pattern.matcher(String.valueOf(patient.getSozialversicherungsnummer())).find() ||
                pattern.matcher(patient.getVersicherung() != null ? patient.getVersicherung() : "").find() ||
                pattern.matcher(patient.getStrasse() != null ? patient.getStrasse() : "").find() ||
                pattern.matcher(String.valueOf(patient.getPostleitzahl())).find() ||
                pattern.matcher(patient.getOrt() != null ? patient.getOrt() : "").find() ||
                pattern.matcher(patient.getTelefon() != null ? patient.getTelefon() : "").find() ||
                pattern.matcher(patient.getMail() != null ? patient.getMail() : "").find() ||
                pattern.matcher(patient.getBundesland() != null ? patient.getBundesland() : "").find();
    }

    public String getPatientDetails(int patientenID) throws SQLException {
        String query = """
            SELECT patient.PatientenID, patient.Vorname, patient.Nachname, patient.Geburtsdatum,
                   patient.Sozialversicherungsnummer, patient.Strasse, patient.Postleitzahl,
                   patient.Ort, patient.Telefon, patient.Mail, krankenkasse.Bezeichnung AS Krankenkasse
            FROM patient
            JOIN krankenkasse ON patient.krankenkassenID = krankenkasse.krankenkassenID
            WHERE patient.PatientenID = ?;
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientenID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return String.format("""
                    Vorname: %s
                    Nachname: %s
                    Geburtsdatum: %s
                    Sozialversicherungsnummer: %s
                    Straße: %s
                    Postleitzahl: %s
                    Ort: %s
                    Telefon: %s
                    Mail: %s
                    Krankenkasse: %s
                    """,
                        resultSet.getString("Vorname"),
                        resultSet.getString("Nachname"),
                        resultSet.getString("Geburtsdatum"),
                        resultSet.getString("Sozialversicherungsnummer"),
                        resultSet.getString("Strasse"),
                        resultSet.getString("Postleitzahl"),
                        resultSet.getString("Ort"),
                        resultSet.getString("Telefon"),
                        resultSet.getString("Mail"),
                        resultSet.getString("Krankenkasse")
                );
            }
        }
        return null;
    }

    public boolean deletePatient(int patientenID) throws SQLException {
        String deleteSQL = "DELETE FROM patient WHERE PatientenID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, patientenID);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; // Gibt true zurück, wenn ein Datensatz gelöscht wurde
        }
    }

    public Patient getPatientById(int patientId) {
        String query = """
    SELECT p.patientenID, p.anrede, p.vorname, p.nachname, p.geburtsdatum, p.sozialversicherungsnummer, 
           k.bezeichnung AS versicherung, p.strasse, p.postleitzahl, p.ort, p.telefon, p.mail, 
           b.bezeichnung AS bundesland
    FROM patient p
    LEFT JOIN krankenkasse k ON p.krankenkassenID = k.krankenkassenID
    LEFT JOIN bundesland b ON p.bundeslandID = b.bundeslandID
    WHERE p.patientenID = ?
""";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Patient patient = new Patient();
                patient.setPatientID(resultSet.getInt("PatientenID"));
                patient.setAnrede(resultSet.getString("Anrede"));
                patient.setVorname(resultSet.getString("Vorname"));
                patient.setNachname(resultSet.getString("Nachname"));
                patient.setGeburtsdatum(resultSet.getDate("Geburtsdatum"));
                patient.setSozialversicherungsnummer(resultSet.getInt("Sozialversicherungsnummer"));
                patient.setVersicherung(resultSet.getString("Versicherung"));
                patient.setTelefon(resultSet.getString("Telefon"));
                patient.setMail(resultSet.getString("Mail"));
                patient.setStrasse(resultSet.getString("Strasse"));
                patient.setPostleitzahl(resultSet.getInt("Postleitzahl"));
                patient.setOrt(resultSet.getString("Ort"));
                patient.setBundesland(resultSet.getString("Bundesland"));
                return patient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Wenn kein Patient gefunden wurde
    }

    public boolean updatePatient(Patient patient) {
        String query = """
        UPDATE patient 
        SET Anrede = ?, Vorname = ?, Nachname = ?, Geburtsdatum = ?, Sozialversicherungsnummer = ?, krankenkassenID = ?, 
            Telefon = ?, Mail = ?, Strasse = ?, Postleitzahl = ?, Ort = ?, bundeslandID = ?
        WHERE PatientenID = ?
    """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patient.getAnrede());
            preparedStatement.setString(2, patient.getVorname());
            preparedStatement.setString(3, patient.getNachname());
            preparedStatement.setDate(4, patient.getGeburtsdatum());
            preparedStatement.setInt(5, patient.getSozialversicherungsnummer());
            preparedStatement.setInt(6, getKrankenkassenID(patient.getVersicherung()));
            preparedStatement.setString(7, patient.getTelefon());
            preparedStatement.setString(8, patient.getMail());
            preparedStatement.setString(9, patient.getStrasse());
            preparedStatement.setInt(10, patient.getPostleitzahl());
            preparedStatement.setString(11, patient.getOrt());
            preparedStatement.setInt(12, getBundeslandID(patient.getBundesland()));
            preparedStatement.setInt(13, patient.getPatientID());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

/*
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
    }*/
}
