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
}
