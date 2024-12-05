public class Patienten {

    private String vorname;
    private String nachname;
    private int geburtsdatum;
    private int svNummer;

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

    public int getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(int geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public int getSvNummer() {
        return svNummer;
    }

    public void setSvNummer(int svNummer) {
        this.svNummer = svNummer;
    }
}
