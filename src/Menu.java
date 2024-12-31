import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.*;

public class Menu extends JFrame {
    private JPanel contentPane;
    private JMenuBar MenuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem saveItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;
    private JMenuItem createItem;
    private JMenuItem exitItem;
    private JMenuItem helpItem;
    private JToolBar toolBar;
    private JButton saveButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton createButton;
    private JButton exitButton;
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
        setTitle("Menü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        //setVisible(true);
        setLocationRelativeTo(null);
    }

    public void initializeMenu(){
        MenuBar= new JMenuBar();

        fileMenu = new JMenu("File");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        fileMenu.add(saveItem);
        saveItem.setMnemonic(KeyEvent.VK_S);
        fileMenu.add(exitItem);
        exitItem.setMnemonic(KeyEvent.VK_E);

        editMenu = new JMenu("Edit");
        editItem = new JMenuItem("Edit");
        deleteItem = new JMenuItem("Delete");
        createItem = new JMenuItem("Create");
        editMenu.add(editItem);
        editItem.setMnemonic(KeyEvent.VK_E);
        editMenu.add(deleteItem);
        deleteItem.setMnemonic(KeyEvent.VK_D);
        editMenu.add(createItem);
        createItem.setMnemonic(KeyEvent.VK_C);

        helpMenu = new JMenu("Help");
        helpItem = new JMenuItem("Contact Support");
        helpMenu.add(helpItem);

        MenuBar.add(fileMenu);
        MenuBar.add(editMenu);
        MenuBar.add(helpMenu);

        setJMenuBar(MenuBar);
    }

    private void initializeToolBar(){
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        saveButton = new JButton();
        ImageIcon imageIcon = new ImageIcon("Icons/save_14959110.png");
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        saveButton.setIcon(scaledIcon);
        saveButton.setToolTipText("Save Patient");
        saveButton.addActionListener(e -> savePatientData());
        toolBar.add(saveButton);

        editButton = new JButton();
        ImageIcon imageIcon2 = new ImageIcon("Icons/pencil_5465509.png");
        Image image2 = imageIcon2.getImage();
        Image scaledImage2 = image2.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
        editButton.setIcon(scaledIcon2);
        editButton.setToolTipText("Edit Patient");
        editButton.addActionListener(e -> editPatientData());
        toolBar.add(editButton);

        deleteButton = new JButton();
        ImageIcon imageIcon3 = new ImageIcon("Icons/trash-bin_9545759.png");
        Image image3 = imageIcon3.getImage();
        Image scaledImage3 = image3.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
        deleteButton.setIcon(scaledIcon3);
        deleteButton.setToolTipText("Delete Patient");
        deleteButton.addActionListener(e -> deletePatientData());
        toolBar.add(deleteButton);

        createButton = new JButton();
        ImageIcon imageIcon4 = new ImageIcon("Icons/create_6520027.png");
        Image image4 = imageIcon4.getImage();
        Image scaledImage4 = image4.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon4 = new ImageIcon(scaledImage4);
        createButton.setIcon(scaledIcon4);
        createButton.setToolTipText("Create new Patient");
        createButton.addActionListener(e -> createNewPatient());
        toolBar.add(createButton);

        exitButton = new JButton();
        ImageIcon imageIcon5 = new ImageIcon("Icons/multiple_17253027.png");
        Image image5 = imageIcon5.getImage();
        Image scaledImage5 = image5.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon5 = new ImageIcon(scaledImage5);
        exitButton.setIcon(scaledIcon5);
        exitButton.setToolTipText("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        toolBar.add(exitButton);

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
        saveItem.addActionListener(e -> savePatientData());
        exitItem.addActionListener(e -> System.exit(0));
        editItem.addActionListener(e -> editPatientData());
        deleteItem.addActionListener(e -> deletePatientData());
        createItem.addActionListener(e -> createNewPatient());
    }

    private void createNewPatient() {
        JPanel panel = new JPanel(new GridLayout(10,2,5,5));

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

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try{
                String vorname = vornameField.getText();
                String nachname = nachnameField.getText();
                String gebdatum = geburtsdatumField.getText();
                int svNummer = Integer.parseInt(sozialversicherungsnummerField.getText());
                String strasse = strasseField.getText();
                int postleitzahl = Integer.parseInt(postleitzahlField.getText());
                String ort = ortField.getText();
                String telefon = telefonField.getText();
                String mail = mailField.getText();

                String daten = "INSERT INTO patient (vorname, nachname, geburtsdatum, sozialversicherungsnummer, strasse, postleitzahl, ort, telefon, mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(daten, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, vornameField.getText());
                    statement.setString(2, nachnameField.getText());
                    statement.setString(3, geburtsdatumField.getText());
                    statement.setInt(4, Integer.parseInt(sozialversicherungsnummerField.getText()));
                    statement.setString(5, strasseField.getText());
                    statement.setInt(6, Integer.parseInt(postleitzahlField.getText()));
                    statement.setString(7, ortField.getText());
                    statement.setString(8, telefonField.getText());
                    statement.setString(9, mailField.getText());
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Neuer Patient wurde erstellt.");
                }
            }catch (SQLException sql){
                JOptionPane.showMessageDialog(this, "Fehler:" + sql.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(saveButton);
        updateContentPane(panel);
    }

    public void editPatientData(){
        String patientenID = JOptionPane.showInputDialog(this, "Geben Sie die Patienten ID ein:");
        if(patientenID == null || patientenID.isEmpty()) return;
        try{
            String query = "SELECT * FROM patient WHERE PatientenID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Integer.parseInt(patientenID));
                ResultSet set = preparedStatement.executeQuery();

                if(set.next()){
                    JPanel panel = new JPanel(new GridLayout(10,2,5,5));
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

                    JButton saveButton = new JButton("Änderungen gespeichert");
                    saveButton.addActionListener(e -> {
                        try{
                            String updateSql = "UPDATE patient SET vorname = ?, nachname = ?, geburtsdatum = ?, sozialversicherungsnummer = ?, strasse = ?, postleitzahl = ?, ort = ?, telefon = ?, mail = ? WHERE PatientenID = ?";
                            try(PreparedStatement update = connection.prepareStatement(updateSql)) {
                                update.setString(1, vornameField.getText());
                                update.setString(2, nachnameField.getText());
                                update.setString(3, geburtsdatumField.getText());
                                update.setInt(4, Integer.parseInt(sozialversicherungsnummerField.getText()));
                                update.setString(5, strasseField.getText());
                                update.setInt(6, Integer.parseInt(postleitzahlField.getText()));
                                update.setString(7, ortField.getText());
                                update.setString(8, telefonField.getText());
                                update.setString(9, mailField.getText());
                                update.executeUpdate();

                                JOptionPane.showMessageDialog(this, "Patientdaten wurde aktualisiert.");
                            }
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
        }
    }

    private void deletePatientData(){
        String patientenID = JOptionPane.showInputDialog(this,"geben Sie die Patienten ID ein:");
        if(patientenID == null || patientenID.isEmpty()) return;

        try{
            String query = "SELECT * FROM patient WHERE PatientenID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setInt(1, Integer.parseInt(patientenID));
                ResultSet set = preparedStatement.executeQuery();

                if(set.next()){
                    int confirm = JOptionPane.showConfirmDialog(this, "Möchten Sie den Patienten wirklich löschen?\n" + "Vorname: " + set.getString("vorname") + "\nNachname: " +set.getString("nachname") + "\nGeburtsdatum: " +set.getString("geburtsdatum") + "\nSozialversicherungsnummer: " +set.getString("sozialversicherungsnummer") + "\nStraße: " +set.getString("strasse") + "\nPostleitzahl: " +set.getString("postleitzahl") + "\nOrt: " +set.getString("ort") + "\nTelefon: " +set.getString("telefon") + "\nMail: " +set.getString("mail"), "Bestätigung", JOptionPane.YES_NO_OPTION);

                    if(confirm == JOptionPane.YES_OPTION){
                        String deleteSQL = "DELETE FROM patient WHERE PatientenID = ?";
                        try(PreparedStatement delete = connection.prepareStatement(deleteSQL)){
                            delete.setInt(1, Integer.parseInt(patientenID));
                            delete.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Patientdaten wurde gelöscht.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "Fehler: Der Patient existiert nicht.");
                    }
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
    private void savePatientData(){
        JOptionPane.showMessageDialog(this, "Gespeichert");
    }

    private void updateContentPane(JPanel panel){
        contentPane.removeAll();
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

}
