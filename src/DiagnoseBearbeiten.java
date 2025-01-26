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

public class DiagnoseBearbeiten extends JFrame {
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
    private int diagnoseID;

    public DiagnoseBearbeiten(Connection connection, DiagnoseDAO diagnoseDAO, int diagnoseID, int patientenID) {
        this.connection = connection;
        this.diagnoseDAO = diagnoseDAO;
        this.diagnoseID = diagnoseID;

        initializeProperties();
        loadDiagnoseData();
        initializeButtonListeners();
    }

    private void initializeProperties() {
        setTitle("Diagnose bearbeiten");
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
        JLabel diagnoseLabel = new JLabel("Diagnose:");
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
        JLabel icdLabel = new JLabel("ICD-Code:");
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

    private void loadDiagnoseData() {
        String query = "SELECT Diagnose, ICD, Beschreibung, Datum FROM diagnose WHERE DiagnoseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, diagnoseID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    diagnoseTextField.setText(rs.getString("Diagnose"));
                    icdTextField.setText(rs.getString("ICD"));
                    beschreibungTextArea.setText(rs.getString("Beschreibung"));
                    datumTextField.setText(rs.getDate("Datum").toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Diagnose: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeButtonListeners() {
        speichernButton.addActionListener(this::savePerformed);
        abbrechenButton.addActionListener(e -> dispose());

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
                if (parts.length == 2) {
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

            listModel.clear();
            while (rs.next()) {
                String icd = rs.getString("ICD");
                String diagnose = rs.getString("Diagnose");
                listModel.addElement(icd + ": " + diagnose);
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
        String datumText = datumTextField.getText().trim();
        String icdCode = icdTextField.getText().trim();
        String diagnose = diagnoseTextField.getText().trim();
        String beschreibung = beschreibungTextArea.getText().trim();

        if (datumText.isEmpty() || icdCode.isEmpty() || diagnose.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte alle Pflichtfelder ausfüllen (Datum, ICD-Code, Diagnose)!");
            return;
        }

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = LocalDate.parse(datumText, inputFormatter).format(outputFormatter);
            java.sql.Date datum = java.sql.Date.valueOf(formattedDate);

            String query = "UPDATE diagnose SET Diagnose = ?, ICD = ?, Beschreibung = ?, Datum = ? WHERE DiagnoseID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, diagnose);
                stmt.setString(2, icdCode);
                stmt.setString(3, beschreibung);
                stmt.setDate(4, datum);
                stmt.setInt(5, diagnoseID);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Diagnose erfolgreich gespeichert!");
                dispose(); // Fenster schließen
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Diagnose: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
