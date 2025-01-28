import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Die Klasse DiagnoseErstellen stellt eine Benutzeroberfläche zum Erstellen einer neuen Diagnose für einen Patienten bereit.
 * Benutzer können die Diagnoseinformationen eingeben, Diagnosen/ICD Codes durchsuchen und die neue Diagnose speichern.
 */
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

    /**
     * Konstruktor, der das Fenster zum Erstellen einer neuen Diagnose initialisiert.
     *
     * @param connection  Die Datenbankverbindung.
     * @param diagnoseDAO Das DAO-Objekt, das Datenbankoperationen für Diagnosen unterstützt.
     * @param patientenID Die ID des Patienten, für den die Diagnose erstellt wird.
     */
    public DiagnoseErstellen(Connection connection, DiagnoseDAO diagnoseDAO, int patientenID) {
        this.connection = connection;
        this.diagnoseDAO = diagnoseDAO;
        this.patientenID = patientenID;

        initializeProperties();
        initializeButtonListeners();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters wie Titel, Größe und Layout.
     */
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
        datumTextField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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

    /**
     * Initialisiert die ActionListener für die Buttons und das Diagnose-Suchfeld.
     */
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

        diagnoseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && diagnoseList.getSelectedValue() != null) {
                String selectedValue = diagnoseList.getSelectedValue();
                String[] parts = selectedValue.split(":");
                if (parts.length == 2) {
                    String icd = parts[0].trim();
                    String diagnose = parts[1].trim();
                    icdTextField.setText(icd);
                    diagnoseTextField.setText(diagnose);
                }
                diagnoseList.setVisible(false);
                diagnoseTextField.requestFocus();
            }
        });
    }

    /**
     * Führt eine Diagnose-Suche basierend auf dem eingegebenen Text durch und zeigt die Ergebnisse an (ICD-Codes).
     *
     * @param searchText Der eingegebene Suchtext.
     */
    private void searchDiagnose(String searchText) {
        if (searchText.isEmpty()) {
            listModel.clear();
            diagnoseList.setVisible(false);
            return;
        }

        class SearchDiagnoseTask implements Runnable {
            private final String searchText;

            SearchDiagnoseTask(String searchText) {
                this.searchText = searchText;
            }

            @Override
            public void run() {
                try {
                    List<String> result = diagnoseDAO.searchDiagnose(searchText);
                    SwingUtilities.invokeLater(() -> {
                        listModel.clear();
                        for (String entry : result) {
                            listModel.addElement(entry);
                        }
                        diagnoseList.setVisible(!result.isEmpty());
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            DiagnoseErstellen.this,
                            "Fehler bei der Diagnose-Suche: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
            }
        }
        new Thread(new SearchDiagnoseTask(searchText)).start();
    }

    /**
     * Speichert die eingegebene Diagnose in der Datenbank.
     *
     * @param actionEvent Das ActionEvent, das durch das Drücken des "Speichern"-Buttons ausgelöst wurde.
     */
    private void savePerformed(ActionEvent actionEvent) {
        String datumText = datumTextField.getText().trim();
        String icdCode = icdTextField.getText().trim();
        String diagnose = diagnoseTextField.getText().trim();
        String beschreibung = beschreibungTextArea.getText().trim();

        if (datumText.isEmpty() || icdCode.isEmpty() || diagnose.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte alle Pflichtfelder ausfüllen!", "Fehler", JOptionPane.WARNING_MESSAGE);
            return;
        }

        class SaveDiagnoseTask implements Runnable {
            private final String datumText;
            private final String icdCode;
            private final String diagnose;
            private final String beschreibung;

            public SaveDiagnoseTask(String datumText, String icdCode, String diagnose, String beschreibung) {
                this.datumText = datumText;
                this.icdCode = icdCode;
                this.diagnose = diagnose;
                this.beschreibung = beschreibung;
            }

            @Override
            public void run() {
                try {
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = LocalDate.parse(datumText, inputFormatter).format(outputFormatter);
                    java.sql.Date datum = java.sql.Date.valueOf(formattedDate);

                    Diagnose neueDiagnose = new Diagnose(0, datum, beschreibung, patientenID, icdCode, diagnose);
                    diagnoseDAO.addDiagnose(neueDiagnose);

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(DiagnoseErstellen.this, "Diagnose erfolgreich gespeichert!");
                        dispose();
                    });
                } catch (IllegalArgumentException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            DiagnoseErstellen.this, "Ungültiges Datum! Bitte im Format dd.MM.yyyy eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE
                    ));
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            DiagnoseErstellen.this, "Fehler beim Speichern der Diagnose: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE
                    ));
                }
            }
        }
        Thread thread = new Thread(new SaveDiagnoseTask(datumText, icdCode, diagnose, beschreibung));
        thread.start();
    }
}