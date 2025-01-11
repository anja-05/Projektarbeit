import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;

public class RezeptionPatientErstellen extends JFrame {
    private JPanel contentPane;
    private JComboBox AnredeComboBox;
    private JTextField vornameTextField;
    private JTextField nachnameTextField;
    private JTextField geburtsdatumTextField;
    private JTextField svnTextField;
    private JComboBox versicherungComboBox;
    private JButton weiterButton;
    private JButton abbrechenButton;

    private Connection connection;
    
    public RezeptionPatientErstellen(Connection connection) {
        this.connection = connection;
        initializeProperties();
        initializeView();
        initializeButtonListeners();
    }
    
    private void initializeProperties() {
        setTitle("Persönliche Daten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    private void initializeView() {
        setContentPane(contentPane);
        pack();
    }
    private void initializeButtonListeners() {
        weiterButton.addActionListener(this::actionPerformed);
        abbrechenButton.addActionListener(e -> returnToRezeptionMenu());
    }

    private void returnToRezeptionMenu() {
        this.dispose();
        RezeptionMenu rezeptionMenu = new RezeptionMenu(connection);
        rezeptionMenu.setVisible(true);
    }

    private void actionPerformed(ActionEvent actionEvent) {
        this.dispose();

        String vorname = vornameTextField.getText();
        String nachname = nachnameTextField.getText();
        String geburtsdatum = geburtsdatumTextField.getText();
        String svn = svnTextField.getText();

        RezeptionPatientKontaktdaten kontaktFenster = new RezeptionPatientKontaktdaten(connection, vorname, nachname, geburtsdatum, svn);
        kontaktFenster.setVisible(true);
    }
}
