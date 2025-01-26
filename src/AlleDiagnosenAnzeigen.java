import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Zeigt alle Diagnosen eines bestimmten Patienten in einer Tabelle an.
 * Die Diagnosen werden aus der Datenbank über die DiagnoseDAO-Klasse abgerufen.
 */
public class AlleDiagnosenAnzeigen extends JFrame {

    private JTable diagnoseTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    private int patientenID;
    private String vorname;
    private String nachname;
    private DiagnoseDAO diagnoseDAO;

    /**
     * Konstruktor der Klasse.
     *
     * @param connection   Verbindung zur Datenbank.
     * @param diagnoseDAO  Die DiagnoseDAO-Instanz zum Abrufen der Diagnosen.
     * @param patientenID  Die ID des Patienten, für den die Diagnosen angezeigt werden sollen.
     * @param nachname     Der Nachname des Patienten.
     * @param vorname      Der Vorname des Patienten.
     */
    public AlleDiagnosenAnzeigen(Connection connection, DiagnoseDAO diagnoseDAO, int patientenID, String nachname, String vorname) {
        this.connection = connection;
        this.diagnoseDAO = diagnoseDAO;
        this.patientenID = patientenID;
        this.nachname = nachname;
        this.vorname = vorname;

        setTitle("Diagnosen des Patienten anzeigen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        loadDiagnosen();
    }

    /**
     * Initialisiert die GUI-Komponenten, einschließlich der Tabelle und ihrer Kopfzeilen.
     */
    private void initializeComponents() {
        // Überschrift hinzufügen
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

        // Customize table header
        JTableHeader header = diagnoseTable.getTableHeader();
        header.setBackground(new Color(169, 169, 169)); // Dark gray color
        header.setFont(header.getFont().deriveFont(Font.BOLD));

        JScrollPane scrollPane = new JScrollPane(diagnoseTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Lädt die Diagnosen des Patienten aus der Datenbank über DiagnoseDAO
     * und fügt sie der Tabelle hinzu.
     */
    private void loadDiagnosen() {
        try {
            // Diagnosen über DiagnoseDAO abrufen
            List<Diagnose> diagnosen = diagnoseDAO.getDiagnoseByPatientId(patientenID);

            for (Diagnose diagnose : diagnosen) {
                // Erstelle eine Zeile aus den Diagnose-Werten
                Object[] row = {
                        diagnose.getDiagnoseID(),
                        diagnose.getDiagnose(),
                        diagnose.getIcd(),
                        diagnose.getBeschreibung(),
                        diagnose.getDatum()
                };
                tableModel.addRow(row); // Zeile zur Tabelle hinzufügen
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Diagnosen: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }


}