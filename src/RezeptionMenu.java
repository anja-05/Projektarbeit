import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Die Klasse stellt eine Benutzeroberfläche, mit Menü und Toolbar, für die Rezeption dar
 * Es können Patientendaten (bestehend aus Persönlichen- und Kontaktdaten) erstellt, bearbeiten und gelöscht werden
 * Zusätzlichen können aus alle Pateinten die in der Datenbank gespeichert sind ausgelesen werden
 */
public class RezeptionMenu extends JFrame {
        private JPanel contentPane;
        //Bausteine für RezeptionMenuBar
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
        //Bausteine für RezeptionToolBar
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

    /**
     * Konstruktor
     * @param connection für die Datenbankverbindung
     * @param patientDAO patientDAO Klasse für die Patientenverwaltung
     */
        public RezeptionMenu(Connection connection, PatientDAO patientDAO) {
            this.connection = connection;
            this.patientDAO = patientDAO;
                initializePropertiesMenu();
                initializeMenu();
                initializeButtonListeners();
                initializeToolBar();
                initializeContentPane();
        }

    /**
     * Initialisiert die Grundeigenschaften des Fensters (Titel, Größe, Schließverhalten, Position)
     */
        private void initializePropertiesMenu() {
            setTitle("Patienten DB");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(500,500);
            setLocationRelativeTo(null);
        }

    /**
     * Erstellt/konfiguriert die Menüleiste
     */
        public void initializeMenu(){
                MenuBar = new JMenuBar();

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
                deleteItem = new JMenuItem("Patient löschen");
                createItem = new JMenuItem("Patient erstellen");
                optionMenu.add(editItem);
                editItem.setMnemonic(KeyEvent.VK_E);
                optionMenu.add(deleteItem);
                deleteItem.setMnemonic(KeyEvent.VK_D);
                optionMenu.add(createItem);
                createItem.setMnemonic(KeyEvent.VK_C);
                MenuBar.add(optionMenu);

                helpMenu = new JMenu("Help");
                helpItem = new JMenuItem("Contact Support");
                helpMenu.add(helpItem);
                MenuBar.add(helpMenu);

                setJMenuBar(MenuBar);
        }

    /**
     * Initialisiert die Toolbar mit den Buttons und dazugehörigen Aktionen
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

                createButton = new JButton();
                ImageIcon imageIcon2 = new ImageIcon("Icons/receptionist_17626336.png");
                Image image2 = imageIcon2.getImage();
                Image scaledImage2 = image2.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
                createButton.setIcon(scaledIcon2);
                createButton.setToolTipText("Neuen Patienten erstellen");
                createButton.addActionListener(e -> patientErstellen());
                toolBar.add(createButton);

                editButton = new JButton();
                ImageIcon imageIcon3 = new ImageIcon("Icons/pencil_5465509.png");
                Image image3 = imageIcon3.getImage();
                Image scaledImage3 = image3.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
                editButton.setIcon(scaledIcon3);
                editButton.setToolTipText("Patient bearbeiten");
                editButton.addActionListener(e -> patientBearbeiten());
                toolBar.add(editButton);

                deleteButton = new JButton();
                ImageIcon imageIcon4 = new ImageIcon("Icons/trash-bin_9545759.png");
                Image image4 = imageIcon4.getImage();
                Image scaledImage4 = image4.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon4 = new ImageIcon(scaledImage4);
                deleteButton.setIcon(scaledIcon4);
                deleteButton.setToolTipText("Patient löschen");
                deleteButton.addActionListener(e -> patientLöschen());
                toolBar.add(deleteButton);

                toolBar.addSeparator();

                allButton = new JButton();
                ImageIcon imageIcon6 = new ImageIcon("Icons/menu_7699137.png");
                Image image6 = imageIcon6.getImage();
                Image scaledImage6 = image6.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon6 = new ImageIcon(scaledImage6);
                allButton.setIcon(scaledIcon6);
                allButton.setToolTipText("Alle Patienten");
                allButton.addActionListener(e -> allePatientenAnzeigen());
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
     * Initialisiert das Haupt-Menü-Panel
     */
        private void initializeContentPane(){
            contentPane = new JPanel(new CardLayout());
            add(contentPane, BorderLayout.CENTER);
        }

    /**
     * Selektiert ein Bild mit einem fixen Maßstabd
     * @param path der Pfad zur Bilddatei
     * @return das skalierte Bild
     */
        private Image scaleIcon(String path){
                ImageIcon icon = new ImageIcon(path);
                return icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        }

    /**
     * Initialisiert die Button-Listener
     */
        private void initializeButtonListeners() {
            exitItem.addActionListener(e -> System.exit(0));
            editItem.addActionListener(e -> patientBearbeiten());
            deleteItem.addActionListener(e -> patientLöschen());
            createItem.addActionListener(e -> patientErstellen());
            allItem.addActionListener(e -> allePatientenAnzeigen());
            helpItem.addActionListener(e-> {
                JOptionPane.showMessageDialog(this,
                        "Bei Problemen kontaktieren Sie uns:\nTelefon: +49 123 456 789\nWebsite: https://www.support-website.com",
                        "Kontakt Support",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }

    /**
     * Bearbeitet die persönlichen- und Kontakdaten eines bestehenden Patientens
     */
        private void patientBearbeiten() {
            new Thread(new DatabaseTask(() -> {
            String patientenIDString = JOptionPane.showInputDialog(this, "Geben Sie die Patienten-ID ein, die Sie bearbeiten möchten:");
            if (patientenIDString == null || patientenIDString.isEmpty()) return;
                try{
                int patientenID = Integer.parseInt(patientenIDString);
                Patient patient = patientDAO.getPatientById(patientenID);
                if (patient == null) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Kein Patient mit der ID " + patientenID + " gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE));
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                RezeptionPatientBearbeiten bearbeitenFenster = new RezeptionPatientBearbeiten(connection, patientDAO);
                bearbeitenFenster.setFields(patient);
                bearbeitenFenster.setVisible(true);
                });
            } catch (NumberFormatException e) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Ungültige Patienten-ID. Bitte geben Sie eine gültige Zahl ein."));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Fehler beim Bearbeiten des Patienten: " + e.getMessage()));
            }
            })).start();
        }

    /**
     * Löscht einen Patienten aus der Datenbank
     */
    private void patientLöschen() {
        new Thread(new DatabaseTask(() -> {
        String patientenIDString = JOptionPane.showInputDialog(this, "Geben Sie die Patienten ID ein:");
        if (patientenIDString == null || patientenIDString.isEmpty()) return;
            try{
            int patientenID = Integer.parseInt(patientenIDString);

            String patientDaten = patientDAO.getPatientDetails(patientenID);
            if (patientDaten == null) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Kein Patient mit der ID " + patientenID + " gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE));
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie den Patienten wirklich löschen?\n\n" + patientDaten, "Bestätigung", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = patientDAO.deletePatient(patientenID);
                SwingUtilities.invokeLater(() -> {
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Patient wurde erfolgreich gelöscht.");
                } else {
                    showErrorDialog("Fehler beim Löschen des Patienten.");
                }
                });
            }
        } catch (NumberFormatException e) {
            SwingUtilities.invokeLater(() ->  showErrorDialog("Ungültige Patienten-ID. Bitte geben Sie eine gültige Zahl ein."));
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> showErrorDialog("Fehler beim Löschen des Patienten: " + e.getMessage()));
        }
        })).start();
    }

    /**
     * Öffnet ein Fenster zum Erstellen eines Patienten
     */
    private void patientErstellen() {
        try {
            PatientDAO patientDAO = new PatientDAO(connection);
            RezeptionPatientErstellen patientErstellen = new RezeptionPatientErstellen(connection, patientDAO);
            patientErstellen.setVisible(true);
        }catch (NullPointerException ex) {
            throw new IllegalStateException("Das Fenster konnte nicht geöffnet werden. Möglicherweise sind wichtige Objekte nicht initialisiert.", ex);
        } catch (Exception ex) {
            showErrorDialog("Ein unbekannter Fehler ist aufgetreten: " + ex.getMessage());
        }
    }

    /**
     * Zeigt alle Patienten die in der Datenbank gespeichert sind an
     */
    private void allePatientenAnzeigen() {
        if (patientDAO == null) {
            showErrorDialog("Fehler: PatientDAO ist nicht initialisiert. Die Patienten können nicht angezeigt werden.");
            return;
        }
        new Thread(new DatabaseTask(() -> {
        try {
            AllePatientenAnzeigen fenster = new AllePatientenAnzeigen(patientDAO);
            SwingUtilities.invokeLater(() -> fenster.setVisible(true));
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> showErrorDialog("Ein unbekannter Fehler ist aufgetreten: " + ex.getMessage()));
        }
        })).start();
    }

    /**
     * Zeigt einen Fehlerdialog an
     * @param message die jeweilige Nachricht die angezeigt werden soll
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Runnable-Klasse für allgemeine Datenbankoperationen
     */
    private class DatabaseTask implements Runnable {
        private final Runnable operation;

        public DatabaseTask(Runnable operation) {
            this.operation = operation;
        }

        @Override
        public void run() {
            try {
                operation.run();
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> showErrorDialog("Fehler: " + e.getMessage()));
            }
        }
    }
}
