import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


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

        // Menü für Diagnosen
        diagnoseMenu = new JMenu("Diagnose");
        diagnoseNeuItem = new JMenuItem("Neu erstellen");
        diagnoseBearbeitenItem = new JMenuItem("Bearbeiten");
        diagnoseAlleAnzeigenItem = new JMenuItem("Alle anzeigen");
        diagnoseMenu.add(diagnoseNeuItem);
        diagnoseMenu.add(diagnoseBearbeitenItem);
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

    private Image scaleIcon(String path){
            ImageIcon icon = new ImageIcon(path);
            return icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    }

    private boolean menuInitialized = false;
    private void initializeMenuListeners() {
        if (menuInitialized) {
            return;
        }
        persoenlicheDatenItem.addActionListener(e -> showPersoenlicheDaten());
        kontaktdatenItem.addActionListener(e -> showKontaktdaten());
        diagnoseNeuItem.addActionListener(e -> createDiagnosis());
        diagnoseBearbeitenItem.addActionListener(e -> editDiagnosis());
        diagnoseAlleAnzeigenItem.addActionListener(e -> showAllDiagnosis());
        menuInitialized = true;
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
        SwingUtilities.invokeLater(() -> {
        if (persoenlicheDatenFenster == null || !persoenlicheDatenFenster.isVisible()) {
            persoenlicheDatenFenster = new ArztPersoenlicheDaten(patient, patientDAO);
            persoenlicheDatenFenster.setVisible(true);
        } else {
            persoenlicheDatenFenster.toFront();
        }
        });
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
        SwingUtilities.invokeLater(() -> {
        if (diagnoseErstellenFenster == null || !diagnoseErstellenFenster.isVisible()) {
            diagnoseErstellenFenster = new DiagnoseErstellen(connection, new DiagnoseDAO(connection), patientenID);
            diagnoseErstellenFenster.setVisible(true);
        } else {
            diagnoseErstellenFenster.toFront();
        }
        });
    }


    private void editDiagnosis(){
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Kein Patient ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientId = patient.getPatientID();

        String diagnoseListQuery = "SELECT DiagnoseID, Diagnose FROM diagnose WHERE PatientenID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(diagnoseListQuery)) {
            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();
            StringBuilder diagnoseOptions = new StringBuilder();
            while (rs.next()) {
                int diagnoseID = rs.getInt("DiagnoseID");
                String diagnoseName = rs.getString("Diagnose");
                diagnoseOptions.append(diagnoseID).append(": ").append(diagnoseName).append("\n");
            }

            if (diagnoseOptions.length() == 0) {
                JOptionPane.showMessageDialog(this, "Keine Diagnosen für diesen Patienten gefunden.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String diagnoseIDInput = JOptionPane.showInputDialog(this,
                    "Bitte geben Sie die Diagnose-ID ein, die Sie bearbeiten möchten:\n\n" + diagnoseOptions.toString(),
                    "Diagnose bearbeiten",
                    JOptionPane.QUESTION_MESSAGE);

            if (diagnoseIDInput == null || diagnoseIDInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keine Diagnose-ID eingegeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int diagnoseID = Integer.parseInt(diagnoseIDInput.trim());
                DiagnoseBearbeiten diagnoseBearbeitenFenster = new DiagnoseBearbeiten(connection, diagnoseDAO, diagnoseID, patientId);
                diagnoseBearbeitenFenster.setVisible(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Die eingegebene Diagnose-ID ist ungültig.", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Diagnosen: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAllDiagnosis() {
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Kein Patient ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
        if (alleDiagnosenFenster == null || !alleDiagnosenFenster.isVisible()) {
            alleDiagnosenFenster = new AlleDiagnosenAnzeigen(connection, patient.getPatientID(), patient.getNachname(), patient.getVorname());
            alleDiagnosenFenster.setVisible(true);
        } else {
            alleDiagnosenFenster.toFront();
        }
        });
    }
}


