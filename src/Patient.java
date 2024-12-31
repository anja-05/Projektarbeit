public class Patient {

    private int id;
    private String vorname;
    private String nachname;
    private String gebdatum;
    private int svNummer;
    private String strasse;
    private int postleitzahl;
    private String ort;
    private String telefon;
    private String mail;

    public Patient(int id, String vorname, String nachname, String gebdatum, int sozialversicherungsnummer, String strasse, int postleitahl, String adresse, String telefon, String email) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gebdatum = gebdatum;
        this.svNummer = svNummer;
        this.strasse = this.strasse;
        this.postleitzahl = postleitzahl;
        this.ort = ort;
        this.telefon = this.telefon;
        this.mail = mail;

    }

    @Override
    public String toString(){
        return "Patient{" +
                "id=" + id +
                ", vorname=" + vorname + '\'' +
                ", nachname=" + nachname + '\'' +
                ", gebdatum=" + gebdatum +
                '}';
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public String getGeburtsdatum() {
        return gebdatum;
    }
    public void setGeburtsdatum(String geburtsdatum) {
        this.gebdatum = gebdatum;
    }

    public int getSvNummer() {
        return svNummer;
    }
    public void setSvNummer(int svNummer) {
        this.svNummer = svNummer;
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
        mail = mail;
    }
}
