import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Zeigt alle Patienten aus der Datenbank an
 * Suchfunktion mit Regex und Möglichkeit danach nochmals alle Patienten laden zu können
 */
public class AllePatientenAnzeigen extends JFrame {
    private JPanel contentPane;
    private JTable patientenTable;
    private JButton zurückButton;
    private JTextField suchenField;
    private JButton suchenButton;
    private JButton allePatientenButton;

    private PatientDAO patientDAO;

    /**
     * Kondtruktor für die GUI zur Anzeige aller Patienten
     * @param patientDAO DAO-Objekt für den Zugriff auf Patientendaten in der Datenbank
     */
    public AllePatientenAnzeigen(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
        initializeProperties();
        initializeView();
        initializeButtonListeners();

        try {
            List<Patient> patientenListe = patientDAO.getAllePatienten();
            aktualisiereTabelle(patientenListe);
        } catch (SQLException e) {
            showErrorDialog("Fehler beim Abrufen der Patienten: " + e.getMessage());
        }
    }

    /**
     * Initialisiert alle Eigenschaften des Fensters (Größe, Titel, Schließverhalten, Position)
     */
    private void initializeProperties() {
        setTitle("Alle Patienten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Initialisiert grafische Benutzeroberfläche
     */
    private void initializeView() {
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        patientenTable = new JTable();
        patientenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(patientenTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel suchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        suchenField = new JTextField(20);
        suchenButton = new JButton("Suchen");
        allePatientenButton = new JButton("Alle Patienten anzeigen");
        suchPanel.add(new JLabel("Suche: "));
        suchPanel.add(suchenField);
        suchPanel.add(suchenButton);
        suchPanel.add(allePatientenButton);
        contentPane.add(suchPanel, BorderLayout.NORTH);

        zurückButton = new JButton("Zurück");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(zurückButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Initialisiert Action Listeners für die Buttons (zurückButton, suchenButton, allePatientenButton)
     */
    private void initializeButtonListeners() {
        zurückButton.addActionListener(e -> {
            dispose();
        });

        suchenButton.addActionListener(e -> {
            String regex = suchenField.getText().trim();
            if (!regex.isEmpty()) {
                try {
                    List<Patient> gefiltertePatienten = patientDAO.suchePatientenMitRegex(regex);
                    aktualisiereTabelle(gefiltertePatienten);
                } catch (SQLException ex) {
                    showErrorDialog("Fehler bei der Suche: " + ex.getMessage());
                } catch (Exception ex) {
                showErrorDialog("Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage());
                }
            } else {
                try {
                    // Zeigt alle Patienten an, wenn das Suchfeld leer ist
                    aktualisiereTabelle(patientDAO.getAllePatienten());
                } catch (SQLException ex) {
                    showErrorDialog("Fehler beim Abrufen der Patienten: " + ex.getMessage());
                } catch (Exception ex) {
                    showErrorDialog("Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage());
                }
            }
        });
        allePatientenButton.addActionListener(e -> {
            try {
                List<Patient> allePatienten = patientDAO.getAllePatienten();
                aktualisiereTabelle(allePatienten);
                suchenField.setText("");
            } catch (SQLException ex) {
                showErrorDialog("Fehler beim Abrufen der Patienten: " + ex.getMessage());
            } catch (Exception ex) {
                showErrorDialog("Ein unerwarteter Fehler ist aufgetreten: " + ex.getMessage());
            }
        });
    }

    /**
     * Aktualisiert die Tabelle mit den übergebenen Patientendaten
     * @param patientenListe Eine Liste von Patienten, die in der Tabelle angezeigt werden sollen
     */
    private void aktualisiereTabelle(List<Patient> patientenListe) {
        String[] spalten = {"PatientenID", "Anrede", "Vorname", "Nachname", "Geburtsdatum", "Sozialversicherungsnummer", "Versicherung", "Strasse", "Postleitzahl", "Ort", "Telefon", "Mail", "Bundesland"};
        Object[][] daten = new Object[patientenListe.size()][spalten.length];

        for (int i = 0; i < patientenListe.size(); i++) {
            Patient patient = patientenListe.get(i);
            daten[i][0] = patient.getPatientID();
            daten[i][1] = patient.getAnrede();
            daten[i][2] = patient.getVorname();
            daten[i][3] = patient.getNachname();
            daten[i][4] = patient.getGeburtsdatum();
            daten[i][5] = patient.getSozialversicherungsnummer();
            daten[i][6] = patient.getVersicherung();
            daten[i][7] = patient.getStrasse();
            daten[i][8] = patient.getPostleitzahl();
            daten[i][9] = patient.getOrt();
            daten[i][10] = patient.getTelefon();
            daten[i][11] = patient.getMail();
            daten[i][12] = patient.getBundesland();
        }
        patientenTable.setModel(new javax.swing.table.DefaultTableModel(daten, spalten));
    }

    /**
     * Zeigt eine Fehlermeldung in einem Diolog an
     * @param message die jeweilige Fehlermeldung die angezeigt werden soll
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}
