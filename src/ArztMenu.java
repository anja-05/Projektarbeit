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
        allItem.addActionListener(e ->{
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

    private void patientBearbeiten(){
        SwingUtilities.invokeLater(() -> {
            ArztPatientBearbeiten arztPatientBearbeiten = new ArztPatientBearbeiten();
            arztPatientBearbeiten.setVisible(true);
        });
    }
/*
    public void editPatientData(){
        String patientenID = JOptionPane.showInputDialog(this, "Geben Sie die Patienten ID ein:");

        if(patientenID == null || patientenID.isEmpty()) return;
        try{
            String query = "SELECT * FROM patient WHERE PatientenID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Integer.parseInt(patientenID));
                ResultSet set = preparedStatement.executeQuery();

                if(set.next()){
                    JPanel panel = new JPanel(new GridLayout(11,2,5,5));
                    panel.add(new JLabel("Vorname:"));
                    JTextField vornameField = new JTextField(set.getString("Vorname"));
                    panel.add(vornameField);

                    panel.add(new JLabel("Nachname:"));
                    JTextField nachnameField = new JTextField(set.getString("Nachname"));
                    panel.add(nachnameField);

                    panel.add(new JLabel("Geburtsdatum:"));
                    JTextField geburtsdatumField = new JTextField(set.getString("Geburtsdatum"));
                    panel.add(geburtsdatumField);

                    panel.add(new JLabel("Sozialversicherungsnummer:"));
                    JTextField sozialversicherungsnummerField = new JTextField(set.getString("Sozialversicherungsnummer"));
                    panel.add(sozialversicherungsnummerField);

                    panel.add(new JLabel("Straße:"));
                    JTextField strasseField = new JTextField(set.getString("strasse"));
                    panel.add(strasseField);

                    panel.add(new JLabel("Postleitzahl:"));
                    JTextField postleitzahlField = new JTextField(set.getString("Postleitzahl"));
                    panel.add(postleitzahlField);

                    panel.add(new JLabel("Ort:"));
                    JTextField ortField = new JTextField(set.getString("Ort"));
                    panel.add(ortField);

                    panel.add(new JLabel("Telefon:"));
                    JTextField telefonField = new JTextField(set.getString("Telefon"));
                    panel.add(telefonField);

                    panel.add(new JLabel("Mail:"));
                    JTextField mailField = new JTextField(set.getString("Mail"));
                    panel.add(mailField);

                    panel.add(new JLabel("Krankenkasse:"));
                    JComboBox<String> krankenkassaComboBox = new JComboBox<>(new String[]{"ÖGK", "SVS", "BVAEB"});
                    krankenkassaComboBox.setSelectedItem(getKrankenkassaName(set.getInt("krankenkassenID")));
                    panel.add(krankenkassaComboBox);

                    JButton saveButton = new JButton("Änderungen gespeichert");
                    saveButton.addActionListener(e -> {
                        try{
                            String updateSql = "UPDATE patient SET vorname = ?, nachname = ?, geburtsdatum = ?, sozialversicherungsnummer = ?, strasse = ?, postleitzahl = ?, ort = ?, telefon = ?, mail = ?, krankenkassenID = ? WHERE PatientenID = ?";
                            try(PreparedStatement update = connection.prepareStatement(updateSql)) {
                                update.setString(1, vornameField.getText());
                                update.setString(2, nachnameField.getText());
                                update.setString(3, geburtsdatumField.getText());
                                update.setString(4, sozialversicherungsnummerField.getText());
                                update.setString(5, strasseField.getText());
                                update.setInt(6, Integer.parseInt(postleitzahlField.getText()));
                                update.setString(7, ortField.getText());
                                update.setString(8, telefonField.getText());
                                update.setString(9, mailField.getText());
                                update.setInt(10, getKrankenkassaID((String) krankenkassaComboBox.getSelectedItem()));
                                update.setInt(11, Integer.parseInt(patientenID));
                                update.executeUpdate();

                                JOptionPane.showMessageDialog(this, "Patientdaten wurde aktualisiert.");
                            }
                            resetToDefaultView();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(this, "Fehler:" + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    panel.add(saveButton);
                    updateContentPane(panel);
                } else{
                    JOptionPane.showMessageDialog(this, "Fehler: Der Patient existiert nicht.");
                }
            }

        } catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Fehler:" + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch(NullPointerException ex){
            JOptionPane.showMessageDialog(this, "Fehler: Der Patient existiert nicht.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Fehler: Ungültige Patienten Id", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
*/
    private void drucken(){

    }
}
