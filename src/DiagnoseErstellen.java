import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DiagnoseErstellen extends JFrame {
    private JPanel contentPane;
    private JTextField icdTextField;
    private JTextField diagnoseTextField;
    private JTextArea beschreibungTextArea;
    private JTextField datumTextField;
    private JButton abbrechenButton;
    private JButton speichernButton;
    private JList<String> diagnoseList;
    private DefaultListModel<String> listModel;

    private Connection connection;
    private DiagnoseDAO diagnoseDAO;
    private PatientDAO patientDAO;
    private int patientenID;

    public DiagnoseErstellen(Connection connection, DiagnoseDAO diagnoseDAO, int patientenID) {
        this.connection = connection;
        this.diagnoseDAO = diagnoseDAO;
        this.patientenID = patientenID;

        initializeProperties();
        initializeButtonListeners();
    }

    private void initializeProperties() {
        setTitle("Diagnose erstellen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        // Hauptpanel mit Layout-Manager
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // Inneres Panel für das Formular
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        contentPane.add(formPanel, BorderLayout.CENTER);

        // Diagnose-Suchfeld
        JPanel diagnosePanel = new JPanel(new BorderLayout());
        JLabel diagnoseLabel = new JLabel("Diagnose: *");
        diagnoseTextField = new JTextField();
        diagnosePanel.add(diagnoseLabel, BorderLayout.NORTH);
        diagnosePanel.add(diagnoseTextField, BorderLayout.CENTER);
        formPanel.add(diagnosePanel);

        // Diagnose-Liste für Vorschläge
        listModel = new DefaultListModel<>();
        diagnoseList = new JList<>(listModel);
        diagnoseList.setVisible(false); // Standardmäßig ausgeblendet
        JScrollPane diagnoseScrollPane = new JScrollPane(diagnoseList);
        formPanel.add(diagnoseScrollPane);

        // ICD-Code
        JPanel icdPanel = new JPanel(new BorderLayout());
        JLabel icdLabel = new JLabel("ICD-Code: *");
        icdTextField = new JTextField();
        icdTextField.setEditable(false);
        icdPanel.add(icdLabel, BorderLayout.NORTH);
        icdPanel.add(icdTextField, BorderLayout.CENTER);
        formPanel.add(icdPanel);

        // Beschreibung
        JPanel beschreibungPanel = new JPanel(new BorderLayout());
        JLabel beschreibungLabel = new JLabel("Beschreibung:");
        beschreibungTextArea = new JTextArea(4, 20);
        JScrollPane beschreibungScrollPane = new JScrollPane(beschreibungTextArea);
        beschreibungPanel.add(beschreibungLabel, BorderLayout.NORTH);
        beschreibungPanel.add(beschreibungScrollPane, BorderLayout.CENTER);
        formPanel.add(beschreibungPanel);

        // Datum
        JPanel datumPanel = new JPanel(new BorderLayout());
        JLabel datumLabel = new JLabel("Datum (TT.MM.JJJJ):");
        datumTextField = new JTextField();
        datumTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))); // Heute als Standardwert
        datumPanel.add(datumLabel, BorderLayout.NORTH);
        datumPanel.add(datumTextField, BorderLayout.CENTER);
        formPanel.add(datumPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        speichernButton = new JButton("Speichern");
        abbrechenButton = new JButton("Abbrechen");
        buttonPanel.add(speichernButton);
        buttonPanel.add(abbrechenButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initializeButtonListeners() {
        speichernButton.addActionListener(this::savePerformed);
        abbrechenButton.addActionListener(e -> returnToArztMenu());

        diagnoseTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = diagnoseTextField.getText().trim();
                searchDiagnose(searchText);
                if (searchText.isEmpty()) {
                    icdTextField.setText("");
                }
            }
        });

        // Auswahl aus der Liste
        diagnoseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && diagnoseList.getSelectedValue() != null) {
                String selectedValue = diagnoseList.getSelectedValue();
                String[] parts = selectedValue.split(":");
                if(parts.length == 2) {
                    String icd = parts[0].trim();
                    String diagnose = parts[1].trim();
                    icdTextField.setText(icd);  // Füllt das ICD-Feld
                    diagnoseTextField.setText(diagnose);  // Füllt das Diagnose-Feld
                }
                diagnoseList.setVisible(false);
                diagnoseTextField.requestFocus();
            }
        });
    }

    private void returnToArztMenu() {
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> dispose());
                ArztPatientBearbeiten arztPatientBearbeiten = new ArztPatientBearbeiten(connection, patientDAO,diagnoseDAO);
                SwingUtilities.invokeLater(() -> arztPatientBearbeiten.setVisible(true));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                                "Fehler beim Laden des Arztmenüs: " + ex.getMessage(),
                                "Fehler", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void searchDiagnose(String searchText) {
        if (searchText.isEmpty()) {
            listModel.clear();
            diagnoseList.setVisible(false);
            return;
        }

        String sql = "SELECT ICD, Diagnose FROM icddiagnose WHERE Diagnose LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            listModel.clear(); // Liste zurücksetzen
            while (rs.next()) {
                String icd = rs.getString("ICD");
                String diagnose = rs.getString("Diagnose");
                listModel.addElement(icd + ": " + diagnose); // ICD und Diagnose zusammen anzeigen
            }
            diagnoseList.setVisible(!listModel.isEmpty());
            diagnoseList.revalidate();
            diagnoseList.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Fehler bei der Diagnose-Suche: " + e.getMessage(),
                    "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePerformed(ActionEvent actionEvent) {
        String datumText = datumTextField.getText().trim(); // Datum aus dem Textfeld
        String icdCode = icdTextField.getText().trim(); // ICD-Code
        String diagnose = diagnoseTextField.getText().trim(); // Diagnose
        String beschreibung = beschreibungTextArea.getText().trim(); // Beschreibung (optional)

        if (datumText.isEmpty() || icdCode.isEmpty() || diagnose.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte alle Pflichtfelder ausfüllen (Datum, ICD-Code, Diagnose)!", "Fehler", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = LocalDate.parse(datumText, inputFormatter).format(outputFormatter);
            // Datum in java.sql.Date umwandeln
            java.sql.Date datum = java.sql.Date.valueOf(formattedDate); // yyyy-MM-dd Format erforderlich

            int patientenID = this.patientenID;
            String icd = icdCode;
            // Diagnose speichern
            Diagnose neueDiagnose = new Diagnose(0, datum, beschreibung, patientenID, icd, diagnose);

            diagnoseDAO.addDiagnose(neueDiagnose);

            JOptionPane.showMessageDialog(this, "Diagnose erfolgreich gespeichert!");
            dispose(); // Fenster schließen
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Ungültiges Datum! Bitte im Format yyyy-MM-dd eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}

