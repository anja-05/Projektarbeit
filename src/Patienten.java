import java.util.Date;

public class Patienten {

    private int id;
    private String vorname;
    private String nachname;
    private Date gebdatum;
    private int svNummer;
    private String strasse;
    private int PLZ;
    private String Ort;
    private int Telefon;
    private String Mail;

    public Patienten(int id, String vorname, String nachname, Date gebdatum) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.gebdatum = gebdatum;
        this.svNummer = svNummer;
        this.strasse = strasse;
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

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public Date getGeburtsdatum() {
        return gebdatum;
    }

    public void setGeburtsdatum(int geburtsdatum) {
        this.gebdatum = gebdatum;
    }

    public int getSvNummer() {
        return svNummer;
    }

    public void setSvNummer(int svNummer) {
        this.svNummer = svNummer;
    }
}
