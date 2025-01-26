import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnoseDAO {
    private Connection connection;


    public DiagnoseDAO(Connection connection) {
        this.connection = connection;
    }


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
            e.printStackTrace();
        }
    }


    public List<String> searchDiagnose(String searchText) throws SQLException {
        String sql = "SELECT ICD, Diagnose FROM icddiagnose WHERE Diagnose LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchText + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                List<String> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rs.getString("ICD") + ": " + rs.getString("Diagnose"));
                }
                return results;
            }
        }
    }


    public String getDiagnoseListForPatient(int patientId) throws SQLException {
        String sql = "SELECT DiagnoseID, Diagnose FROM diagnose WHERE PatientenID = ?";
        StringBuilder diagnoseList = new StringBuilder();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int diagnoseId = rs.getInt("DiagnoseID");
                String diagnoseName = rs.getString("Diagnose");
                diagnoseList.append(diagnoseId).append(": ").append(diagnoseName).append("\n");
            }
        }
        return diagnoseList.toString();
    }


   public List<Object[]> getDiagnosenByPatientId(int patientenID) throws SQLException {
        String query = "SELECT DiagnoseID, Diagnose, ICD, Beschreibung, Datum FROM diagnose WHERE PatientenID = ?";
        List<Object[]> diagnosenListe = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, patientenID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[5];
                    row[0] = rs.getInt("DiagnoseID");
                    row[1] = rs.getString("Diagnose");
                    row[2] = rs.getString("ICD");
                    row[3] = rs.getString("Beschreibung");
                    row[4] = rs.getDate("Datum");
                    diagnosenListe.add(row);
                }
            }
        }
        return diagnosenListe;
    }
}
