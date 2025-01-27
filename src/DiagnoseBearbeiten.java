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
import java.util.List;

/**
 * Die Klasse stellt eine Benutzeroberfläche zum Bearbeiten einer bestehenden Diagnose bereit.
 * Benutzer können die Felder bearbeiten und die Änderungen in der Datenbank speichern.
 */
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

    /**
     * Konstruktor, der das Fenster zum Bearbeiten einer Diagnose erstellt.
     *
     * @param connection  Die Datenbankverbindung.
     * @param diagnoseDAO Das DAO-Objekt, das Datenbankoperationen für Diagnosen unterstützt.
     * @param diagnoseID  Die ID der Diagnose, die bearbeitet werden soll.
     * @param patientenID Die ID des zugehörigen Patienten.
     */
    public DiagnoseBearbeiten(Connection connection, DiagnoseDAO diagnoseDAO, int diagnoseID, int patientenID) {
        this.connection = connection;
        this.diagnoseDAO = diagnoseDAO;
        this.diagnoseID = diagnoseID;

        initializeProperties();
        loadDiagnoseData();
        initializeButtonListeners();
    }

    /**
     * Initialisiert die Eigenschaften des Fensters wie Titel, Größe und Layout.
     */
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

    /**
     * Lädt die Daten der Diagnose basierend auf der Diagnose-ID aus der Datenbank.
     */
    private void loadDiagnoseData() {
        class LoadDiagnoseTask implements Runnable {
            @Override
            public void run() {
                try {
                    Diagnose diagnose = diagnoseDAO.getDiagnoseById(diagnoseID);
                    SwingUtilities.invokeLater(() -> {
                        if (diagnose != null) {
                            diagnoseTextField.setText(diagnose.getDiagnose());
                            icdTextField.setText(diagnose.getIcd());
                            beschreibungTextArea.setText(diagnose.getBeschreibung());
                            datumTextField.setText(
                                    diagnose.getDatum().toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                            );
                        } else {
                            JOptionPane.showMessageDialog(DiagnoseBearbeiten.this,
                                    "Keine Diagnose gefunden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            DiagnoseBearbeiten.this,
                            "Fehler beim Laden der Diagnose: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
            }
        }
        Thread thread = new Thread(new LoadDiagnoseTask());
        thread.start();
    }

    /**
     * Initialisiert die Listener für die Butons und das Diagnose-Suchfeld.
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
     * Führt eine Diagnose-Suche basierend auf dem eingegebenen Text durch.
     * Durchsucht dabei die Datenbank und listet die ICD Codes und Diagnosen auf
     *
     * @param searchText Der Suchtext für die Diagnose.
     */
    private void searchDiagnose(String searchText) {
        if (searchText.isEmpty()) {
            listModel.clear();
            diagnoseList.setVisible(false);
            return;
        }

        class SearchDiagnoseTask implements Runnable {
            private final String searchText;

            public SearchDiagnoseTask(String searchText) {
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
                        diagnoseList.revalidate();
                        diagnoseList.repaint();
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            DiagnoseBearbeiten.this,
                            "Fehler bei der Diagnose-Suche: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
            }

        }
        Thread thread = new Thread(new SearchDiagnoseTask(searchText));
        thread.start();
    }

    /**
     * Speichert die bearbeiteten Diagnose-Daten in der Datenbank.
     *
     * @param actionEvent Das ActionEvent, das durch das Drücken des "Speichern"-Buttons ausgelöst wurde.
     */
    private void savePerformed(ActionEvent actionEvent) {
        String datumText = datumTextField.getText().trim();
        String icdCode = icdTextField.getText().trim();
        String diagnose = diagnoseTextField.getText().trim();
        String beschreibung = beschreibungTextArea.getText().trim();

        if (datumText.isEmpty() || icdCode.isEmpty() || diagnose.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte alle Pflichtfelder ausfüllen (Datum, ICD-Code, Diagnose)!", "Fehler", JOptionPane.WARNING_MESSAGE);
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
                    LocalDate datum = LocalDate.parse(datumText, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    java.sql.Date sqlDatum = java.sql.Date.valueOf(datum);

                    Diagnose updatedDiagnose = new Diagnose(diagnoseID, sqlDatum, beschreibung, 0, icdCode, diagnose);

                    diagnoseDAO.updateDiagnose(updatedDiagnose);

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(DiagnoseBearbeiten.this, "Diagnose erfolgreich gespeichert!");
                        dispose();
                    });
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            DiagnoseBearbeiten.this,
                            "Fehler beim Speichern der Diagnose: " + e.getMessage(),
                            "Fehler",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
            }
        }
        Thread thread = new Thread(new SaveDiagnoseTask(datumText, icdCode, diagnose, beschreibung));
        thread.start();
    }
}