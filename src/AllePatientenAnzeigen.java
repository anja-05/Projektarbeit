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

        // Lade Patienten im Hintergrund
        new PatientenLadenThread().start();
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
        zurückButton.addActionListener(e -> dispose());

        suchenButton.addActionListener(e -> {
            String regex = suchenField.getText().trim();
            if (!regex.isEmpty()) {
                new PatientenSuchenThread(regex).start();
            } else {
                new PatientenLadenThread().start();
            }
        });
        allePatientenButton.addActionListener(e -> {new PatientenLadenThread().start();
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

    /**
     * Thread für das Laden aller Patienten
     */
    private class PatientenLadenThread extends Thread {
        @Override
        public void run() {
            try {
                List<Patient> patientenListe = patientDAO.getAllePatienten();
                SwingUtilities.invokeLater(() -> aktualisiereTabelle(patientenListe));
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Fehler beim Laden der Patienten: " + e.getMessage()));
            }
        }
    }

    /**
     * Thread für die Regex-Suche von Patienten
     */
    private class PatientenSuchenThread extends Thread {
        private String regex;

        public PatientenSuchenThread(String regex) {
            this.regex = regex;
        }

        @Override
        public void run() {
            try {
                List<Patient> gefiltertePatienten = patientDAO.suchePatientenMitRegex(regex);
                SwingUtilities.invokeLater(() -> aktualisiereTabelle(gefiltertePatienten));
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Fehler bei der Suche: " + e.getMessage()));
            }
        }
    }
}
