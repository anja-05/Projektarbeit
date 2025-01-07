import javax.swing.*;
import java.sql.*;

public class befundLoeschen {
    private JPanel contentPanel;
    private JComboBox befundAuswahlcomboBox;

    private Connection connection;

    public befundLoeschen(Connection connection) {
        this.connection = connection;
    }

    public void befundeLoeschen(JFrame parentFrame, int patientenID) {
        try {
            String query = "SELECT BefundID, Krankengeschichte, Datum FROM Befund WHERE PatientenID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientenID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(parentFrame, "Keine Befunde für diesen Patienten gefunden.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            resultSet.beforeFirst(); // Setze den ResultSet-Cursor zurück
            while (resultSet.next()) {
                String befundID = resultSet.getString("BefundID");
                String datum = resultSet.getString("Datum");
                String befundKombiniert = "Befund ID: " + befundID + " | Datum: " + datum;
                befundAuswahlcomboBox.addItem(befundKombiniert);
            }
            int result = JOptionPane.showConfirmDialog(parentFrame, contentPanel, "Befund löschen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                // Extrahiere die ausgewählte Befund-ID
                String ausgewählterBefund = (String) befundAuswahlcomboBox.getSelectedItem();
                String befundID = ausgewählterBefund.split("\\|")[0].split(":")[1].trim();
                // Zeige Details zum Befund und frage nach Bestätigung
                String detailsQuery = "SELECT BefundID, Krankengeschichte, Datum FROM Befund WHERE BefundID = ?";
                PreparedStatement detailsStatement = connection.prepareStatement(detailsQuery);
                detailsStatement.setInt(1, Integer.parseInt(befundID));
                ResultSet detailsResultSet = detailsStatement.executeQuery();

                if (detailsResultSet.next()) {
                    String befundDetails = "Befund ID: " + detailsResultSet.getInt("BefundID") + "\n"
                            + "Datum: " + detailsResultSet.getString("Datum") + "\n"
                            + "Krankengeschichte:\n" + detailsResultSet.getString("Krankengeschichte");

                    int deleteConfirmation = JOptionPane.showConfirmDialog(parentFrame, befundDetails + "\n\nWollen Sie diesen Befund wirklich löschen?", "Befund löschen", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (deleteConfirmation == JOptionPane.YES_OPTION) {
                        // Befund aus der Datenbank löschen
                        String deleteQuery = "DELETE FROM Befund WHERE BefundID = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, Integer.parseInt(befundID));
                        int rowsAffected = deleteStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(parentFrame, "Befund erfolgreich gelöscht.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(parentFrame, "Fehler beim Löschen des Befunds.", "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    }
            }
        }catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Datenbankfehler: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
