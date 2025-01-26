import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Die Klasse stellt eine Benutzeroberfläche zur Verwaltung eines einzelnen Patienten bereit.
 * Sie bietet Funktionen wie das Bearbeiten persönlicher Daten und Kontaktdaten und das Erstellen und Bearbeiten von Diagnosen
 * sowie das Anzeigen aller Diagnosen eines Patienten.
 */
public class ArztPatientBearbeiten extends JFrame {
    private JMenuBar menuBar;
    private JMenu datenMenu, diagnoseMenu, fileMenu;
    private JMenuItem persoenlicheDatenItem, kontaktdatenItem;
    private JMenuItem diagnoseNeuItem, diagnoseBearbeitenItem,
            diagnoseAlleAnzeigenItem;
    private JMenuItem exitItem;
    private JLabel dateLabel;

    //Bausteine für ToolBar
    private JToolBar toolBar;
    private JButton editButton;
    private JButton deleteButton;
    private JButton createButton;
    private JButton exitButton;
    private JButton druckenButton;
    private JButton allButton;
    private JLabel date;

    private Connection connection;
    private PatientDAO patientDAO;
    private Patient patient;
    private JPanel jLabel;

    private ArztPersoenlicheDaten persoenlicheDatenFenster;
    private ArztKontaktdaten kontaktdatenFenster;
    private AlleDiagnosenAnzeigen alleDiagnosenFenster;
    private DiagnoseErstellen diagnoseErstellenFenster;
    private DiagnoseDAO diagnoseDAO;

    /**
     * Konstruktor, der das Fenster zur Bearbeitung eines Patienten initialisiert.
     *
     * @param connection   Verbindung zur Datenbank.
     * @param patientDAO   Datenzugriffsschicht für Patienten.
     * @param diagnoseDAO  Datenzugriffsschicht für Diagnosen.
     * */
    public ArztPatientBearbeiten(Connection connection, PatientDAO patientDAO, DiagnoseDAO diagnoseDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.diagnoseDAO = diagnoseDAO;
        this.patient = new Patient();

        initializeFrame();
        initializeMenuBar();
        promptForPatientId();
        initializeMenuListeners();
        initializeToolBar();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters wie Titel, Größe und Schließverhalten
     */
    private void initializeFrame() {
        setTitle("Arzt - Patient Bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    /**
     * Erstellt und initialisiert die Menüleiste
     */
    private void initializeMenuBar() {
        menuBar = new JMenuBar();

        // Menü für Daten
        datenMenu = new JMenu("Daten");
        persoenlicheDatenItem = new JMenuItem("Persönliche Daten");
        kontaktdatenItem = new JMenuItem("Kontaktdaten");
        datenMenu.add(persoenlicheDatenItem);
        datenMenu.add(kontaktdatenItem);

        // Menü für Diagnosen
        diagnoseMenu = new JMenu("Diagnose");
        diagnoseNeuItem = new JMenuItem("Neu erstellen");
        //medikamenteLoeschenItem = new JMenuItem("Löschen");
        diagnoseBearbeitenItem = new JMenuItem("Bearbeiten");
        diagnoseAlleAnzeigenItem = new JMenuItem("Alle anzeigen");
        diagnoseMenu.add(diagnoseNeuItem);
        diagnoseMenu.add(diagnoseBearbeitenItem);
       // medikamenteMenu.add(medikamenteLoeschenItem);
        diagnoseMenu.add(diagnoseAlleAnzeigenItem);

        // Menü für File
        fileMenu = new JMenu("File");
        exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> dispose());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(datenMenu);
        menuBar.add(diagnoseMenu);

        setJMenuBar(menuBar);

        initializeMenuListeners();
    }

    /**
     * Erstellt und initialisiert die Toolbar mit Symbolen und Aktionen.
     */
    private void initializeToolBar(){
            toolBar = new JToolBar();
            toolBar.setFloatable(false);

            toolBar.addSeparator();

            exitButton = new JButton();
            ImageIcon imageIcon = new ImageIcon("Icons/multiple_17253027.png");
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            exitButton.setIcon(scaledIcon);
            exitButton.setToolTipText("Exit");
            exitButton.addActionListener(e -> dispose());
            toolBar.add(exitButton);

            toolBar.addSeparator();

            createButton = new JButton();
            ImageIcon imageIcon2 = new ImageIcon("Icons/receptionist_17626336.png");
            Image image2 = imageIcon2.getImage();
            Image scaledImage2 = image2.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
            createButton.setIcon(scaledIcon2);
            createButton.setToolTipText("Neue Diagnose erstellen");
            createButton.addActionListener(e -> createDiagnosis());
            toolBar.add(createButton);

            editButton = new JButton();
            ImageIcon imageIcon3 = new ImageIcon("Icons/pencil_5465509.png");
            Image image3 = imageIcon3.getImage();
            Image scaledImage3 = image3.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
            editButton.setIcon(scaledIcon3);
            editButton.setToolTipText("Diagnose bearbeiten");
            editButton.addActionListener(e -> editDiagnosis());
            toolBar.add(editButton);

            toolBar.addSeparator();

            allButton = new JButton();
            ImageIcon imageIcon6 = new ImageIcon("Icons/menu_7699137.png");
            Image image6 = imageIcon6.getImage();
            Image scaledImage6 = image6.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon6 = new ImageIcon(scaledImage6);
            allButton.setIcon(scaledIcon6);
            allButton.setToolTipText("Alle Patienten");
            allButton.addActionListener(e -> showAllDiagnosis());
            toolBar.add(allButton);

            JLabel dateLabel = new JLabel();
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            toolBar.add(Box.createHorizontalGlue());
            toolBar.add(dateLabel);

            Timer timer = new Timer(1000, e -> {
                dateLabel.setText(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            });
            timer.start();

            add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Skaliert ein Bild basierend auf dem angegebenen Pfad.
     *
     * @param path Der Pfad zum Bild.
     * @return Das skalierte Bild.
     */
    private Image scaleIcon(String path){
            ImageIcon icon = new ImageIcon(path);
            return icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    }

    private boolean menuInitialized = false;
    /**
     * Initialisiert die Listener für die Menüpunkte.
     */
    private void initializeMenuListeners() {
        if (menuInitialized) {
            return; // Verhindert, dass die Methode mehrfach ausgeführt wird
        }
        persoenlicheDatenItem.addActionListener(e -> showPersoenlicheDaten());
        kontaktdatenItem.addActionListener(e -> showKontaktdaten());
        diagnoseNeuItem.addActionListener(e -> createDiagnosis());
        diagnoseBearbeitenItem.addActionListener(e -> editDiagnosis());
        diagnoseAlleAnzeigenItem.addActionListener(e -> showAllDiagnosis());
        menuInitialized = true;
    }

    /**
     * Fordert den Benutzer auf, eine Patienten-ID einzugeben und lädt die Patientendaten.
     */
    private void promptForPatientId() {
        SwingUtilities.invokeLater(() -> {
            String patientenIdInput = JOptionPane.showInputDialog(this, "Bitte geben Sie die Patienten-ID ein:");
            if (patientenIdInput == null) {
                //JOptionPane.showMessageDialog(this, "Keine gültige ID eingegeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            if(patientenIdInput.trim().isEmpty()){
                JOptionPane.showMessageDialog(this, "Keine gültige ID eingegeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
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

    /**
     * Zeigt ein Fenster mit den persönlichen Daten des Patienten an.
     */
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

    /**
     * Zeigt ein Fenster mit den Kontaktdaten des Patienten an.
     */
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

    /**
     * Öffnet ein Fenster zum Erstellen einer neuen Diagnose für den Patienten.
     */
    private void createDiagnosis() {
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Kein Patient ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientenID = patient.getPatientID();
        if (diagnoseErstellenFenster == null || !diagnoseErstellenFenster.isVisible()) {
            diagnoseErstellenFenster = new DiagnoseErstellen(connection, new DiagnoseDAO(connection), patientenID);
            diagnoseErstellenFenster.setVisible(true);
        } else {
            diagnoseErstellenFenster.toFront();
        }
    }

    /**
     * Listet alle Diagnosen für den jeweiligen Patienten mit DiagnoseID auf
     * Fordert den Benutzer auf eine DiagnosenID auszuwählen
     * Öffnet ein Fenster zur Bearbeitung der Diagnose
      */
    private void editDiagnosis() {
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Kein Patient ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientId = patient.getPatientID();

        try {
            // Abrufen aller Diagnosen für den ausgewählten Patienten
            List<Diagnose> diagnoseList = diagnoseDAO.getDiagnoseByPatientId(patientId);

            if (diagnoseList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine Diagnosen für diesen Patienten gefunden.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Diagnosen als Auswahl anzeigen
            StringBuilder diagnoseOptions = new StringBuilder();
            for (Diagnose diagnose : diagnoseList) {
                diagnoseOptions.append(diagnose.getDiagnoseID())
                        .append(": ")
                        .append(diagnose.getDiagnose())
                        .append("\n");
            }

            String diagnoseIDInput = JOptionPane.showInputDialog(this,
                    "Bitte geben Sie die Diagnose-ID ein, die Sie bearbeiten möchten:\n\n" + diagnoseOptions.toString(),
                    "Diagnose bearbeiten",
                    JOptionPane.QUESTION_MESSAGE);

            if (diagnoseIDInput == null || diagnoseIDInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine Diagnose-ID eingegeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validierung der eingegebenen Diagnose-ID
            int diagnoseID = Integer.parseInt(diagnoseIDInput.trim());

            boolean validDiagnoseID = diagnoseList.stream()
                    .anyMatch(diagnose -> diagnose.getDiagnoseID() == diagnoseID);

            if (!validDiagnoseID) {
                JOptionPane.showMessageDialog(this, "Die eingegebene Diagnose-ID ist ungültig.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Öffne das Fenster zur Bearbeitung der Diagnose
            DiagnoseBearbeiten diagnoseBearbeitenFenster = new DiagnoseBearbeiten(connection, diagnoseDAO, diagnoseID, patientId);
            diagnoseBearbeitenFenster.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Die eingegebene Diagnose-ID ist ungültig.", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Diagnosen: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Zeigt ein Fenster an, das alle Diagnosen des aktuell ausgewählten Patienten anzeigt.
     */
    private void showAllDiagnosis() {
        if (alleDiagnosenFenster == null || !alleDiagnosenFenster.isVisible()) {
            alleDiagnosenFenster = new AlleDiagnosenAnzeigen(connection, diagnoseDAO, patient.getPatientID(), patient.getNachname(), patient.getVorname());
            alleDiagnosenFenster.setVisible(true);
        } else {
            alleDiagnosenFenster.toFront();
        }
    }
}


