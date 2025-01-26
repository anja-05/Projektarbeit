import java.sql.Date;

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

    public Patient() {
    }

    /*public Patient(int patientenID, String anrede, String vorname, String nachname, Date geburtsdatum, int sozialversicherungsnummer, String versicherung, String strasse, int postleitzahl, String ort, String telefon, String mail, String bundesland) {
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
    }*/

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
