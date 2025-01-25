import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class ArztPatientBearbeiten extends JFrame {
    private JMenuBar menuBar;
    private JMenu datenMenu, diagnoseMenu, fileMenu;
    private JMenuItem persoenlicheDatenItem, kontaktdatenItem;
    private JMenuItem diagnoseNeuItem, /*medikamenteLoeschenItem, */
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


    public ArztPatientBearbeiten(Connection connection, PatientDAO patientDAO, DiagnoseDAO diagnoseDAO) {
        this.connection = connection;
        this.patientDAO = patientDAO;
        this.patient = new Patient();

        initializeFrame();
        initializeMenuBar();
        promptForPatientId();
        initializeMenuListeners();
        initializeToolBar();
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
        diagnoseMenu = new JMenu("Diagnose");
        diagnoseNeuItem = new JMenuItem("Neu erstellen");
        //medikamenteLoeschenItem = new JMenuItem("Löschen");
        diagnoseAlleAnzeigenItem = new JMenuItem("Alle anzeigen");
        diagnoseMenu.add(diagnoseNeuItem);
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

    private Image scaleIcon(String path){
            ImageIcon icon = new ImageIcon(path);
            return icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    }

    private void initializeMenuListeners() {
        persoenlicheDatenItem.addActionListener(e -> showPersoenlicheDaten());
        kontaktdatenItem.addActionListener(e -> showKontaktdaten());
        diagnoseNeuItem.addActionListener(e -> createDiagnosis());
        diagnoseAlleAnzeigenItem.addActionListener(e -> showAllDiagnosis());
    }

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

    private void showAllDiagnosis() {
        if (alleDiagnosenFenster == null || !alleDiagnosenFenster.isVisible()) {
            alleDiagnosenFenster = new AlleDiagnosenAnzeigen(connection, patient.getPatientID(), patient.getNachname(), patient.getVorname());
            alleDiagnosenFenster.setVisible(true);
        } else {
            alleDiagnosenFenster.toFront();
        }
    }
}


