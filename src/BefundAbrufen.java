import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class BefundAbrufen {
    private JPanel contentPane;
    private JTextField befundIDField;
    private JTextField patientenIDField;
    private JTextField datumField;
    private JTextArea krankengeschichteArea;


    private Connection connection;

    public BefundAbrufen(Connection connection) {
        this.connection = connection;
    }

    public void befundeAbrufen(JFrame parentFrame, int patientenID) {
        try {
            ArrayList<String> befundeListe = new ArrayList<>();
            String query = "SELECT BefundID, Datum FROM befund WHERE PatientenID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientenID);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Fülle die Liste der Befunde (z. B. "BefundID - Datum")
            while (resultSet.next()) {
                int befundID = resultSet.getInt("BefundID");
                String datum = resultSet.getTimestamp("Datum").toString();
                befundeListe.add(befundID + " - " + datum);
            }
            // Wenn keine Befunde vorhanden sind, zeige eine Nachricht
            if (befundeListe.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Keine Befunde für diesen Patienten gefunden.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Zeige die Auswahl der Befunde in einem Dialog
            String ausgewählterBefund = (String) JOptionPane.showInputDialog(
                    parentFrame,
                    "Wählen Sie einen Befund aus:",
                    "Befund abrufen",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    befundeListe.toArray(),
                    befundeListe.get(0)
            );
            // Wenn kein Befund ausgewählt wurde, breche ab
            if (ausgewählterBefund == null) {
                return;
            }
                // Extrahiere die BefundID aus der Auswahl
                int befundID = Integer.parseInt(ausgewählterBefund.split(" - ")[0]);
                // Details des ausgewählten Befunds abrufen
                query = "SELECT BefundID, Datum, PatientenID, Krankengeschichte FROM befund WHERE BefundID = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, befundID);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    befundIDField = new JTextField(String.valueOf(resultSet.getInt("BefundID")));
                    befundIDField.setEditable(false);

                    datumField = new JTextField(resultSet.getTimestamp("Datum").toString());
                    datumField.setEditable(false);

                    patientenIDField = new JTextField(String.valueOf(resultSet.getInt("PatientenID")));
                    patientenIDField.setEditable(false);

                   // krankengeschichteField = new JTextArea(resultSet.getString("Krankengeschichte"));
                    krankengeschichteArea.setEditable(false);
                    krankengeschichteArea.setLineWrap(true);
                    krankengeschichteArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(krankengeschichteArea);
                    contentPane.add(scrollPane);

                    JOptionPane.showMessageDialog(parentFrame, contentPane, "Befunddetails", JOptionPane.PLAIN_MESSAGE);
                }

            }catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentFrame, "Fehler beim Abrufen der Befunde: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
