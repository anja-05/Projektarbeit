import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;

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
        private JButton suchenButton;
        private JButton druckenButton;
        private JButton allButton;
        private JLabel date;

        private Connection connection;
        private PatientDAO patientDAO;

        public RezeptionMenu(Connection connection, PatientDAO patientDAO) {
            this.connection = connection;
            this.patientDAO = patientDAO;
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

            /*suchenButton = new JButton();
            ImageIcon imageIcon1 = new ImageIcon("Icons/search_11349253.png");
            Image image1 = imageIcon1.getImage();
            Image scaledImage1 = image1.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);
            suchenButton.setIcon(scaledIcon1);
            suchenButton.setToolTipText("Suchen");
            suchenButton.addActionListener(e -> suchePatient());
            toolBar.add(suchenButton);*/

            toolBar.addSeparator();

            createButton = new JButton();
            ImageIcon imageIcon2 = new ImageIcon("Icons/receptionist_17626336.png");
            Image image2 = imageIcon2.getImage();
            Image scaledImage2 = image2.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
            createButton.setIcon(scaledIcon2);
            createButton.setToolTipText("Neuen Patienten erstellen");
            createButton.addActionListener(e -> createNewPatient());
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
            deleteButton.addActionListener(e -> deletePatientData());
            toolBar.add(deleteButton);

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
            deleteItem.addActionListener(e -> deletePatientData());
            createItem.addActionListener(e -> createNewPatient());
            allItem.addActionListener(e -> {
                PatientDAO patientDAO = new PatientDAO(connection);
                AllePatientenAnzeigen fenster = new AllePatientenAnzeigen(patientDAO);
                fenster.setVisible(true);
            });
            helpItem.addActionListener(e-> {
                JOptionPane.showMessageDialog(this,
                        "Bei Problemen kontaktieren Sie uns:\nTelefon: +49 123 456 789\nWebsite: https://www.support-website.com",
                        "Kontakt Support",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }

    private void patientBearbeiten() {
    }

    private void deletePatientData() {

    }

    private void createNewPatient() {
        PatientDAO patientDAO = new PatientDAO(connection);
        RezeptionPatientErstellen patientErstellen = new RezeptionPatientErstellen(connection, patientDAO);
        patientErstellen.setVisible(true);
    }
}
