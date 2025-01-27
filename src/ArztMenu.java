import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt das Hauptmenü für Ärzte dar.
 * Sie bietet verschiedene Optionen wie das Anzeigen aller Patienten, Bearbeiten von Patienten und den Zugriff auf Diagnosen.
 */
public class ArztMenu extends JFrame {
    private JPanel contentPane;
    //Bausteine für MenuBar
    private JMenuBar MenuBar;
    private JMenu fileMenu;
    private JMenu optionMenu;
    private JMenu helpMenu;
    private JMenuItem editItem;
    private JMenuItem deleteItem;
    private JMenuItem createItem;
    private JMenuItem exitItem;
    private JMenuItem helpItem;
    private JMenuItem allItem;
    //Bausteine für ToolBar
    private JToolBar toolBar;
    private JButton editButton;
    private JButton deleteButton;
    private JButton createButton;
    private JButton exitButton;
    //private JButton druckenButton;
    private JButton allButton;
    private JLabel date;

    private static Connection connection;

    /**
     * Konstruktor der Klasse
     * @param connection Verbindung zur Datenbank
     */
    public ArztMenu(Connection connection) {
        this.connection = connection;
        initializePropertiesMenu();
        initializeMenu();
        initializeButtonListeners();
        initializeToolBar();
        initializeContentPane();
    }

    /**
     * Initialisiert die Eigenschaften des Menüs
     */
    private void initializePropertiesMenu() {
        setTitle("Patienten DB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
    }

    /**
     * Erstellt und initialisiert die Menüleiste
     */
    public void initializeMenu(){
        MenuBar= new JMenuBar();

        fileMenu = new JMenu("Datei");
        exitItem = new JMenuItem("Beenden");
        allItem = new JMenuItem("Alle Patienten");
        fileMenu.add(exitItem);
        exitItem.setMnemonic(KeyEvent.VK_E);
        fileMenu.add(allItem);
        allItem.setMnemonic(KeyEvent.VK_A);
        MenuBar.add(fileMenu);

        optionMenu = new JMenu("Optionen");
        editItem = new JMenuItem("Patient bearbeiten");
        optionMenu.add(editItem);
        editItem.setMnemonic(KeyEvent.VK_E);
        MenuBar.add(optionMenu);

        helpMenu = new JMenu("Help");
        helpItem = new JMenuItem("Contact Support");
        helpMenu.add(helpItem);
        MenuBar.add(helpMenu);

        setJMenuBar(MenuBar);
    }

    /**
     * Initialisiert die Toolbar mit den Buttons und zugehörigen Aktionen
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
        exitButton.addActionListener(e -> System.exit(0));
        toolBar.add(exitButton);

        toolBar.addSeparator();

        editButton = new JButton();
        ImageIcon imageIcon3 = new ImageIcon("Icons/pencil_5465509.png");
        Image image3 = imageIcon3.getImage();
        Image scaledImage3 = image3.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
        editButton.setIcon(scaledIcon3);
        editButton.setToolTipText("Patient bearbeiten");
        editButton.addActionListener(e -> patientBearbeiten());
        toolBar.add(editButton);

        toolBar.addSeparator();

        allButton = new JButton();
        ImageIcon imageIcon6 = new ImageIcon("Icons/menu_7699137.png");
        Image image6 = imageIcon6.getImage();
        Image scaledImage6 = image6.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon6 = new ImageIcon(scaledImage6);
        allButton.setIcon(scaledIcon6);
        allButton.setToolTipText("Alle Patienten");
        allButton.addActionListener(e -> {
            PatientDAO patientDAO = new PatientDAO(connection);
            AllePatientenAnzeigen fenster = new AllePatientenAnzeigen(patientDAO);
            fenster.setVisible(true);
        });
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
     * Initialisiert das Hauptinhaltspanel
     */
    private void initializeContentPane(){
        contentPane = new JPanel(new CardLayout());
        add(contentPane, BorderLayout.CENTER);
    }

    /**
     * Selektiert ein Bild mit einem fixen Maßstab
     * @param path Der Pfad zur Bilddatei
     * @return Das skalierte Bild
     */
    private Image scaleIcon(String path){
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    }

    /**
     * Initialisiert die Button Listener
     */
    private void initializeButtonListeners() {
        exitItem.addActionListener(e -> System.exit(0));
        editItem.addActionListener(e -> patientBearbeiten());
        allItem.addActionListener(e -> allePatienten());
        helpItem.addActionListener(e-> showSupportInfo());
    }


    private void showSupportInfo() {
        JOptionPane.showMessageDialog(this,
                "Bei Problemen kontaktieren Sie uns:\nTelefon: +49 123 456 789\nWebsite: https://www.support-website.com",
                "Kontakt Support",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Zeigt ein neues Fenster mit einer Liste aller Patienten an
     */
    private void allePatienten() {
        class LoadAllPatientsTask implements Runnable {
            @Override
            public void run() {
                try {
                    PatientDAO patientDAO = new PatientDAO(connection);
                    AllePatientenAnzeigen fenster = new AllePatientenAnzeigen(patientDAO);
                    SwingUtilities.invokeLater(() -> fenster.setVisible(true));
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            ArztMenu.this,
                            "Fehler beim Laden der Patienten: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
            }
        }Thread thread = new Thread(new LoadAllPatientsTask());
        thread.start();
    }

    /**
     * Öffnet ein Fenster zum Bearbeiten eines Patienten (Persönliche Daten und Kontaktdaten) und zum Erstellen und Bearbeiten von Diagnosen
     */
    private void patientBearbeiten(){
        class EditPatientTask implements Runnable {
            @Override
            public void run() {
                try {
                    PatientDAO patientDAO = new PatientDAO(connection);
                    DiagnoseDAO diagnoseDAO = new DiagnoseDAO(connection);
                    SwingUtilities.invokeLater(() -> {
                        ArztPatientBearbeiten arztPatientBearbeiten = new ArztPatientBearbeiten(connection, patientDAO, diagnoseDAO);
                        arztPatientBearbeiten.setVisible(true);
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            ArztMenu.this, "Fehler beim Bearbeiten des Patienten: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE
                    ));
                }
            }
        }Thread thread = new Thread(new EditPatientTask());
        thread.start();
    }
}
