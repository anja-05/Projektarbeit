import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Menu extends JFrame {
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
    private JButton suchenButton;
    private JButton druckenButton;
    private JButton allButton;
    private JLabel date;

    private static Connection connection;

    public Menu(Connection connection) {
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

        suchenButton = new JButton();
        ImageIcon imageIcon1 = new ImageIcon("Icons/search_11349253.png");
        Image image1 = imageIcon1.getImage();
        Image scaledImage1 = image1.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);
        suchenButton.setIcon(scaledIcon1);
        suchenButton.setToolTipText("Suchen");
        suchenButton.addActionListener(e -> suchePatient());
        toolBar.add(suchenButton);

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

        druckenButton = new JButton();
        ImageIcon imageIcon5 = new ImageIcon("Icons/printer_6932375.png");
        Image image5 = imageIcon5.getImage();
        Image scaledImage5 = image5.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon5 = new ImageIcon(scaledImage5);
        druckenButton.setIcon(scaledIcon5);
        druckenButton.setToolTipText("Drucken");
        druckenButton.addActionListener(e -> drucken());
        toolBar.add(druckenButton);

        allButton = new JButton();
        ImageIcon imageIcon6 = new ImageIcon("Icons/menu_7699137.png");
        Image image6 = imageIcon6.getImage();
        Image scaledImage6 = image6.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon6 = new ImageIcon(scaledImage6);
        allButton.setIcon(scaledIcon6);
        allButton.setToolTipText("Alle Patienten");
        allButton.addActionListener(e -> allePatienten());
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
        allItem.addActionListener(e ->allePatienten());
        helpItem.addActionListener(e-> {
            JOptionPane.showMessageDialog(this,
                    "Bei Problemen kontaktieren Sie uns:\nTelefon: +49 123 456 789\nWebsite: https://www.support-website.com",
                    "Kontakt Support",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void patientBearbeiten(){
        SwingUtilities.invokeLater(() -> {
            PatientenBearbeiten patientBearbeiten = new PatientenBearbeiten(connection);
            patientBearbeiten.setVisible(true);
        });
    }

    private void createNewPatient() {
        JPanel panel = new JPanel(new GridLayout(11,1,5,5));

        panel.add(new JLabel("Vorname:"));
        JTextField vornameField = new JTextField();
        panel.add(vornameField);

        panel.add(new JLabel("Nachname:"));
        JTextField nachnameField = new JTextField();
        panel.add(nachnameField);

        panel.add(new JLabel("Geburtsdatum:"));
        JTextField geburtsdatumField = new JTextField();
        panel.add(geburtsdatumField);

        panel.add(new JLabel("Sozialversicherungsnummer:"));
        JTextField sozialversicherungsnummerField = new JTextField();
        panel.add(sozialversicherungsnummerField);

        panel.add(new JLabel("Straße:"));
        JTextField strasseField = new JTextField();
        panel.add(strasseField);

        panel.add(new JLabel("Postleitzahl:"));
        JTextField postleitzahlField = new JTextField();
        panel.add(postleitzahlField);

        panel.add(new JLabel("Ort:"));
        JTextField ortField = new JTextField();
        panel.add(ortField);

        panel.add(new JLabel("Telefon:"));
        JTextField telefonField = new JTextField();
        panel.add(telefonField);

        panel.add(new JLabel("Mail:"));
        JTextField mailField = new JTextField();
        panel.add(mailField);

        panel.add(new JLabel("Krankenkasse:"));
        JComboBox<String> krankenkassaCheckBox = new JComboBox<>(new String []{"ÖGK", "SVS", "BVAEB"});
        panel.add(krankenkassaCheckBox);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try{
                String vorname = vornameField.getText();
                String nachname = nachnameField.getText();
                String gebdatum = geburtsdatumField.getText();
                String svNummer = sozialversicherungsnummerField.getText();
                String strasse = strasseField.getText();
                int postleitzahl = Integer.parseInt(postleitzahlField.getText());
                String ort = ortField.getText();
                String telefon = telefonField.getText();
                String mail = mailField.getText();
                String krankenkasse = (String) krankenkassaCheckBox.getSelectedItem();

                int krankenkassenID = getKrankenkassaID(krankenkasse);

                String daten = "INSERT INTO patient (vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail, krankenkassenID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                try (PreparedStatement statement = connection.prepareStatement(daten, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, vornameField.getText());
                    statement.setString(2, nachnameField.getText());
                    statement.setString(3, geburtsdatumField.getText());
                    statement.setString(4, sozialversicherungsnummerField.getText());
                    statement.setString(5, strasseField.getText());
                    statement.setInt(6, Integer.parseInt(postleitzahlField.getText()));
                    statement.setString(7, ortField.getText());
                    statement.setString(8, telefonField.getText());
                    statement.setString(9, mailField.getText());
                    statement.setInt(10, krankenkassenID);
                    statement.executeUpdate();

                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        int patientenID = rs.getInt(1);
                        JOptionPane.showMessageDialog(this, "Neuer Patient mit der ID " + patientenID + " wurde erstellt.");
                    }
                }
                resetToDefaultView();
            }catch (SQLException sql){
                JOptionPane.showMessageDialog(this, "Fehler:" + sql.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Fehler: Ungültige Eingabe","Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(saveButton);
        updateContentPane(panel);
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
    private void deletePatientData(){
        String patientenID = JOptionPane.showInputDialog(this,"Geben Sie die Patienten ID ein:");
        if(patientenID == null || patientenID.isEmpty()) return;

        try{
            String query = """
                SELECT patient.PatientenID, patient.Vorname, patient.Nachname, patient.Geburtsdatum,
                       patient.Sozialversicherungsnummer, patient.Strasse, patient.Postleitzahl,
                       patient.Ort, patient.Telefon, patient.Mail, krankenkasse.Bezeichnung AS Krankenkasse
                FROM patient
                JOIN krankenkasse ON patient.krankenkassenID = krankenkasse.krankenkassenID
                WHERE patient.PatientenID = ?;
                """;
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1, Integer.parseInt(patientenID));
                ResultSet set = preparedStatement.executeQuery();

                if(set.next()){
                    String patientDaten = String.format("""
                        Vorname: %s
                        Nachname: %s
                        Geburtsdatum: %s
                        Sozialversicherungsnummer: %s
                        Straße: %s
                        Postleitzahl: %s
                        Ort: %s
                        Telefon: %s
                        Mail: %s
                        Krankenkasse: %s
                        """,
                            set.getString("Vorname"),
                            set.getString("Nachname"),
                            set.getString("Geburtsdatum"),
                            set.getString("Sozialversicherungsnummer"),
                            set.getString("Strasse"),
                            set.getString("Postleitzahl"),
                            set.getString("Ort"),
                            set.getString("Telefon"),
                            set.getString("Mail"),
                            set.getString("Krankenkasse")
                    );
                    int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie den Patienten wirklich löschen?\n\n" + patientDaten, "Bestätigung", JOptionPane.YES_NO_OPTION);

                    if(confirm == JOptionPane.YES_OPTION){
                        String deleteSQL = "DELETE FROM patient WHERE PatientenID = ?";
                        try(PreparedStatement delete = connection.prepareStatement(deleteSQL)){
                            delete.setInt(1, Integer.parseInt(patientenID));
                            delete.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Patientdaten wurde gelöscht.");
                        }
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "Fehler: Der Patient mit der Id " + patientenID + " existiert nicht.");
                }
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(this, "Fehler:" + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (HeadlessException e) {
            throw new RuntimeException(e);
        }
    }

    private void allePatienten(){
        String query = "SELECT patient.PatientenID, patient.Vorname, patient.Nachname, patient.Geburtsdatum, patient.Sozialversicherungsnummer, patient.Strasse, patient.Postleitzahl, patient.Ort, patient.Telefon, patient.Mail, krankenkasse.Bezeichnung AS Krankenkasse " +
                "FROM patient " +
                "Left JOIN krankenkasse ON patient.krankenkassenID = krankenkasse.krankenkassenID";
        try(Statement statement = connection.createStatement()){
            ResultSet set = statement.executeQuery(query);
            String [] spalten = {"PatientenID", "Vorname", "Nachname", "Geburtsdatum", "Sozialversicherungsnummer", "Strasse", "Postleitzahl", "Ort", "Telefon", "Mail", "Krankenkasse"};

            List<String []> list = new ArrayList<>();
            while (set.next()) {
                String[] row = new String[11];
                row[0] = String.valueOf(set.getInt("PatientenID"));
                row[1] = set.getString("Vorname");
                row[2] = set.getString("Nachname");
                row[3] = set.getString("Geburtsdatum");
                row[4] = set.getString("Sozialversicherungsnummer");
                row[5] = set.getString("Strasse");
                row[6] = String.valueOf(set.getInt("Postleitzahl"));
                row[7] = set.getString("Ort");
                row[8] = set.getString("Telefon");
                row[9] = set.getString("Mail");
                row[10] = set.getString("Krankenkasse");
                list.add(row);
            }

            String[][] data = list.toArray(new String[0][0]);
            JTable table = new JTable(new DefaultTableModel(data, spalten){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            JScrollPane scrollPane = new JScrollPane(table);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            updateContentPane(panel);
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, "Fehler beim Abrufen der Patienten " + ex.getMessage(), "Fehler ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suchePatient(){

    }

    private void drucken(){

    }

    private int getKrankenkassaID(String krankenkasse){
        switch (krankenkasse){
            case "ÖGK":
                return 1;
            case "SVS":
                return 2;
            case "BVAEB":
                return 3;
                default:
                    throw new IllegalArgumentException("Ungültige Krankenkasse ausgewählt: " + krankenkasse);
        }
    }

    private String getKrankenkassaName(int krankenkassenID) {
        String krankenkasseName = "";
        String query = "SELECT bezeichnung FROM krankenkasse WHERE krankenkassenID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, krankenkassenID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                krankenkasseName = resultSet.getString("Bezeichnung");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return krankenkasseName;
    }

    private void resetToDefaultView(){
        contentPane.removeAll();
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void updateContentPane(JPanel panel){
        contentPane.removeAll();
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

}
