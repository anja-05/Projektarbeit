import javax.swing.*;
import java.sql.*;

public class befundHinzufuegen {
    private JPanel contentPane;
    private JTextField befundIDField;
    private JTextField patientenIDField;
    private JTextField krankengeschichteField;
    private JTextField datumField;

    private Connection connection;

    public befundHinzufuegen(Connection connection) {
        this.connection = connection;
    }

    public void befundeHinzufuegen(JFrame parentFrame, int patientenID) {
        try {
            datumField.setText(java.time.LocalDateTime.now().toString());
            datumField.setEditable(false);

            patientenIDField.setText(String.valueOf(patientenID));
            patientenIDField.setEditable(false);

            int befundID = generateUniqueBefundID();
            befundIDField.setText(String.valueOf(befundID));
            befundIDField.setEditable(false);

            int result = JOptionPane.showConfirmDialog(parentFrame, contentPane, "Neuen Befund hinzufügen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                // Krankengeschichte auslesen
                String krankengeschichte = krankengeschichteField.getText();
                if (krankengeschichte == null || krankengeschichte.isEmpty()) {
                    JOptionPane.showMessageDialog(parentFrame, "Krankengeschichte darf nicht leer sein!", "Warnung", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String datum = datumField.getText();

                String query = "INSERT INTO befund (BefundID, Krankengeschichte, Datum, PatientenID) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, befundID);
                preparedStatement.setString(2, krankengeschichte);
                preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(datum));
                preparedStatement.setInt(4, patientenID);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(parentFrame, "Befund erfolgreich hinzugefügt.");
            }
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Fehler beim Hinzufügen des Befunds: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int generateUniqueBefundID() {
        try {
            String query = "SELECT MAX(BefundID) AS maxID FROM befund";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return resultSet.getInt("maxID") + 1;
            } else {
                return 1; // Falls keine Einträge vorhanden sind, beginnt die ID bei 1
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Fehler beim Generieren der BefundID: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            return -1; // Rückgabewert im Fehlerfall
        }
    }
}
