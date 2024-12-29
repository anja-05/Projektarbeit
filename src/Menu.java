import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;

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
    }

    private void initializePropertiesMenu() {
        setTitle("Menü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setVisible(true);
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
/*
    private void initializeToolBar(){
        toolBar = new JToolBar();
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> savePatientData());
        toolBar.add(saveButton);

        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editPatientData());
        toolBar.add(editButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deletePatientData());
        toolBar.add(deleteButton);

        createButton = new JButton("Create");
        createButton.addActionListener(e -> createNewPatient());
        toolBar.add(createButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        toolBar.add(exitButton);

        add(toolBar, BorderLayout.NORTH);
    }
*/
    private void initializeButtonListeners() {
        saveItem.addActionListener(e -> savePatientData());
        exitItem.addActionListener(e -> System.exit(0));
        editItem.addActionListener(e -> editPatientData());
        deleteItem.addActionListener(e -> deletePatientData());
        createItem.addActionListener(e -> createNewPatient());
    }

    private void savePatientData() {
        // Platzhalter für die Datenbankinteraktion
        JOptionPane.showMessageDialog(this, "Änderungen der Patientendaten wurden gespeichert.");
        // Hier: Code für das Speichern in die Datenbank hinzufügen
    }

    private void editPatientData() {
        // Platzhalter für die Datenbankinteraktion
        String patientId = JOptionPane.showInputDialog(this, "Geben Sie die Patient-ID ein, um Daten zu bearbeiten:");
        if (patientId != null) {
            // Hier: Code für das Bearbeiten in der Datenbank hinzufügen
            JOptionPane.showMessageDialog(this, "Patient mit ID " + patientId + " bearbeiten.");
        }
    }

    private void deletePatientData() {
        // Platzhalter für die Datenbankinteraktion
        String patientId = JOptionPane.showInputDialog(this, "Geben Sie die Patient-ID ein, um Daten zu löschen:");
        if (patientId != null) {
            // Hier: Code für das Löschen in der Datenbank hinzufügen
            JOptionPane.showMessageDialog(this, "Patient mit ID " + patientId + " wurde gelöscht.");
        }
    }

    private void createNewPatient() {
        // Platzhalter für die Datenbankinteraktion
        JOptionPane.showMessageDialog(this, "Neuer Patient wird erstellt.");
        // Hier: Code für das Einfügen in die Datenbank hinzufügen
    }
}
