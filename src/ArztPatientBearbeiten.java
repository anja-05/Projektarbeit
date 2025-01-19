import javax.swing.*;
import java.sql.Connection;

public class ArztPatientBearbeiten extends JFrame {
    private JMenuBar menuBar;
    private JMenu datenMenu, medikamenteMenu;
    private JMenuItem persoenlicheDatenItem, kontaktdatenItem;
    private JMenuItem medikamenteNeuItem, medikamenteLoeschenItem, medikamenteAlleAnzeigenItem;

    private Connection connection;
    private PatientDAO patientDAO;
    private Patient patient;
    private JPanel jLabel;

    private ArztPersoenlicheDaten persoenlicheDatenFenster;
    private ArztKontaktdaten kontaktdatenFenster;

    public ArztPatientBearbeiten(Connection connection, PatientDAO patientDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.patient = new Patient(); // Platzhalter-Patient

        initializeFrame();
        initializeMenuBar();
        promptForPatientId();
        initializeMenuListeners();
    }

    private void initializeFrame() {
        setTitle("Arzt - Patient Bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void initializeMenuBar() {
        menuBar = new JMenuBar();

        // Menü für Daten
        datenMenu = new JMenu("Daten");
        persoenlicheDatenItem = new JMenuItem("Persönliche Daten");
        kontaktdatenItem = new JMenuItem("Kontaktdaten");
        datenMenu.add(persoenlicheDatenItem);
        datenMenu.add(kontaktdatenItem);

        // Menü für Medikamente
        medikamenteMenu = new JMenu("Medikation");
        medikamenteNeuItem = new JMenuItem("Neu erstellen");
        medikamenteLoeschenItem = new JMenuItem("Löschen");
        medikamenteAlleAnzeigenItem = new JMenuItem("Alle anzeigen");
        medikamenteMenu.add(medikamenteNeuItem);
        medikamenteMenu.add(medikamenteLoeschenItem);
        medikamenteMenu.add(medikamenteAlleAnzeigenItem);

        menuBar.add(datenMenu);
        menuBar.add(medikamenteMenu);

        setJMenuBar(menuBar);

        // Event Listener für die Menü-Items
        initializeMenuListeners();
    }

    private void initializeMenuListeners() {
        persoenlicheDatenItem.addActionListener(e -> showPersoenlicheDaten());
        kontaktdatenItem.addActionListener(e -> showKontaktdaten());
        medikamenteNeuItem.addActionListener(e -> createMedikation());
        medikamenteLoeschenItem.addActionListener(e -> deleteMedikation());
        medikamenteAlleAnzeigenItem.addActionListener(e -> showAllMedikation());
    }

    private void promptForPatientId() {
        SwingUtilities.invokeLater(() -> {
            String patientenIdInput = JOptionPane.showInputDialog(this, "Bitte geben Sie die Patienten-ID ein:");
            if (patientenIdInput == null || patientenIdInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine gültige ID eingegeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            try {
                int patientId = Integer.parseInt(patientenIdInput);
                patient = patientDAO.getPatientById(patientId);
                if (patient == null) {
                    JOptionPane.showMessageDialog(this, "Patient mit der ID " + patientId + " wurde nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Patient gefunden: " + patient.getVorname() + " " + patient.getNachname());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Die ID muss eine Zahl sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        });
    }

    private void showPersoenlicheDaten() {
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Kein Patient geladen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (persoenlicheDatenFenster == null || !persoenlicheDatenFenster.isVisible()) {
            persoenlicheDatenFenster = new ArztPersoenlicheDaten(patient, patientDAO);
            persoenlicheDatenFenster.setVisible(true);
        } else {
            persoenlicheDatenFenster.toFront();
        }
    }

    private void showKontaktdaten() {
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Kein Patient geladen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (kontaktdatenFenster == null || !kontaktdatenFenster.isVisible()) {
            kontaktdatenFenster = new ArztKontaktdaten(patient, patientDAO);
            kontaktdatenFenster.setVisible(true);
        } else {
            kontaktdatenFenster.toFront();
        }
    }

    private void createMedikation() {

    }

    private void deleteMedikation() {

    }

    private void showAllMedikation() {

    }
}


