import java.sql.Date;
/**
 * Die Klasse Patient repräsentiert einen Patienten.
 * Sie enthält alle relevanten Informationen über einen Patienten.
 */
public class Patient {

    private int patientID;
    private String anrede;
    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private int sozialversicherungsnummer;
    private String versicherung;
    private String strasse;
    private int postleitzahl;
    private String ort;
    private String telefon;
    private String mail;
    private String bundesland;

    /**
     * Standardkonstruktor, der ein leeres Patient-Objekt erstellt.
     * Kann verwendet werden, wenn die Attribute später gesetzt werden sollen.
     */
    public Patient() {
    }
    /**
     * Konstruktor, der ein Patient-Objekt mit den angegebenen Parametern erstellt.
     *
     * @param patientenID               Die eindeutige ID des Patienten.
     * @param anrede                    Die Anrede des Patienten (z.B. "Herr", "Frau").
     * @param vorname                   Der Vorname des Patienten.
     * @param nachname                  Der Nachname des Patienten.
     * @param geburtsdatum              Das Geburtsdatum des Patienten.
     * @param sozialversicherungsnummer Die Sozialversicherungsnummer des Patienten.
     * @param versicherung              Die Krankenversicherung des Patienten.
     * @param strasse                   Die Straßenadresse des Patienten.
     * @param postleitzahl              Die Postleitzahl der Adresse.
     * @param ort                       Der Wohnort des Patienten.
     * @param telefon                   Die Telefonnummer des Patienten.
     * @param mail                      Die E-Mail-Adresse des Patienten.
     * @param bundesland                Das Bundesland, in dem der Patient lebt.
     */
    public Patient(int patientenID, String anrede, String vorname, String nachname, Date geburtsdatum, int sozialversicherungsnummer, String versicherung, String strasse, int postleitzahl, String ort, String telefon, String mail, String bundesland) {
        this.patientID = patientenID;
        this.anrede = anrede;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.sozialversicherungsnummer = sozialversicherungsnummer;
        this.versicherung = versicherung;
        this.strasse = strasse;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.telefon = telefon;
        this.mail = mail;
        this.bundesland = bundesland;
    }

    public int getPatientID() {
        return patientID;
    }
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getAnrede() {
        return anrede;
    }
    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }
    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public int getSozialversicherungsnummer() {
        return sozialversicherungsnummer;
    }
    public void setSozialversicherungsnummer(int sozialversicherungsnummer) {
        this.sozialversicherungsnummer = sozialversicherungsnummer;
    }

    public String getVersicherung() {
        return versicherung;
    }
    public void setVersicherung(String versicherung) {
        this.versicherung = versicherung;
    }

    public String getStrasse() {
        return strasse;
    }
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public int getPostleitzahl() {
        return postleitzahl;
    }
    public void setPostleitzahl(int postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getOrt() {
        return ort;
    }
    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getTelefon() {
        return telefon;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBundesland() {
        return bundesland;
    }
    public void setBundesland(String bundesland) {
        this.bundesland = bundesland;
    }
}
