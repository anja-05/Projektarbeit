import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AllePatientenAnzeigen extends JFrame {
    private JPanel contentPane;
    private JTable patientenTable;
    private JButton zurückButton;

    private PatientDAO patientDAO;

    public AllePatientenAnzeigen(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
        initializeProperties();
        initializeView();
        initializeButtonListeners();

        try {
            List<Patient> patientenListe = patientDAO.getAllePatienten();
            aktualisiereTabelle(patientenListe);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Patienten: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void initializeProperties() {
        setTitle("Alle Patienten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initializeView() {
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        patientenTable = new JTable();
        patientenTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(patientenTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        zurückButton = new JButton("Zurück");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(zurückButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeButtonListeners() {
        zurückButton.addActionListener(e -> {
            dispose();
        });
    }

    private void aktualisiereTabelle(List<Patient> patientenListe) {
        String[] spalten = {"PatientenID", "Anrede", "Vorname", "Nachname", "Geburtsdatum", "Sozialversicherungsnummer", "Versicherung", "Strasse", "Postleitzahl", "Ort", "Telefon", "Mail", "Bundesland"};
        Object[][] daten = new Object[patientenListe.size()][spalten.length];

        for (int i = 0; i < patientenListe.size(); i++) {
            Patient patient = patientenListe.get(i);
            daten[i][0] = patient.getpatientID();
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
}
