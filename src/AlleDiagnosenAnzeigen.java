import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlleDiagnosenAnzeigen extends JFrame {
    private JTable diagnoseTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    private int patientenID;
    private String vorname;
    private String nachname;

    public AlleDiagnosenAnzeigen(Connection connection, int patientenID, String nachname, String vorname) {
        this.connection = connection;
        this.patientenID = patientenID;
        this.nachname = nachname;
        this.vorname = vorname;

        initializeProperties();
        initializeComponents();
        loadDiagnosen();
    }

    private void initializeProperties() {
        setTitle("Diagnosen des Patienten anzeigen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        JLabel titleLabel = new JLabel("Alle Diagnosen: " + nachname + " " + vorname, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Diagnose ID");
        tableModel.addColumn("Diagnose");
        tableModel.addColumn("ICD");
        tableModel.addColumn("Beschreibung");
        tableModel.addColumn("Datum");

        diagnoseTable = new JTable(tableModel);
        diagnoseTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = diagnoseTable.getTableHeader();
        header.setBackground(new Color(169, 169, 169)); // Dark gray color
        header.setFont(header.getFont().deriveFont(Font.BOLD));

        JScrollPane scrollPane = new JScrollPane(diagnoseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadDiagnosen() {
        String query = "SELECT DiagnoseID, Diagnose, ICD, Beschreibung, Datum FROM diagnose WHERE PatientenID = ?";

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
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Diagnosen: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}