public class Patient {

    private int id;
    private String vorname;
    private String nachname;
    private String gebdatum;
    private int svNummer;
    private String strasse;
    private int PLZ;
    private String Ort;
    private String Telefon;
    private String Mail;

    public Patient(int id, String vorname, String nachname, String gebdatum, int sozialversicherungsnummer, String strasse, int postleitahl, String adresse, String telefon, String email) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gebdatum = gebdatum;
        this.svNummer = svNummer;
        this.strasse = this.strasse;
        this.PLZ = PLZ;
        this.Ort = Ort;
        this.Telefon = Telefon;
        this.Mail = Mail;

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

    public int getPLZ() {
        return PLZ;
    }
    public void setPLZ(int PLZ) {
        this.PLZ = PLZ;
    }

    public String getOrt() {
        return Ort;
    }
    public void setOrt(String ort) {
        Ort = ort;
    }

    public String getTelefon() {
        return Telefon;
    }
    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getMail() {
        return Mail;
    }
    public void setMail(String mail) {
        Mail = mail;
    }
}
