import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    private JButton druckenButton;
    private JButton allButton;
    private JLabel date;

    private static Connection connection;

    public ArztMenu(Connection connection) {
        this.connection = connection;
        initializePropertiesMenu();
        initializeMenu();
        initializeButtonListeners();
        initializeToolBar();
        initializeContentPane();
    }

    private void initializePropertiesMenu() {
        setTitle("Patienten DB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
    }

    //Menu
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

        //Help
        helpMenu = new JMenu("Help");
        helpItem = new JMenuItem("Contact Support");
        helpMenu.add(helpItem);
        MenuBar.add(helpMenu);

        setJMenuBar(MenuBar);
    }

    //Toolbar
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

        druckenButton = new JButton();
        ImageIcon imageIcon5 = new ImageIcon("Icons/printer_6932375.png");
        Image image5 = imageIcon5.getImage();
        Image scaledImage5 = image5.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon5 = new ImageIcon(scaledImage5);
        druckenButton.setIcon(scaledIcon5);
        druckenButton.setToolTipText("Drucken");
        druckenButton.addActionListener(e -> drucken());
        toolBar.add(druckenButton);

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

    private void initializeContentPane(){
        contentPane = new JPanel(new CardLayout());
        add(contentPane, BorderLayout.CENTER);
    }

    private Image scaleIcon(String path){
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
    }

    private void initializeButtonListeners() {
        exitItem.addActionListener(e -> System.exit(0));
        editItem.addActionListener(e -> patientBearbeiten());
        allItem.addActionListener(e -> allePatienten());
        helpItem.addActionListener(e-> {
            JOptionPane.showMessageDialog(this,
                    "Bei Problemen kontaktieren Sie uns:\nTelefon: +49 123 456 789\nWebsite: https://www.support-website.com",
                    "Kontakt Support",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void allePatienten() {
        new Thread(() -> {
            try {
                PatientDAO patientDAO = new PatientDAO(connection);
                AllePatientenAnzeigen fenster = new AllePatientenAnzeigen(patientDAO);
                SwingUtilities.invokeLater(() -> fenster.setVisible(true));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Fehler beim Laden der Patienten: " + e.getMessage(),
                                "Fehler", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    private void patientBearbeiten(){
        new Thread(() -> {
            try {
                PatientDAO patientDAO = new PatientDAO(connection);
                SwingUtilities.invokeLater(() -> {
                    ArztPatientBearbeiten arztPatientBearbeiten = new ArztPatientBearbeiten(connection, patientDAO);
                    arztPatientBearbeiten.setVisible(true);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Fehler beim Bearbeiten des Patienten: " + e.getMessage(),
                                "Fehler", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    private void drucken(){

    }
}
