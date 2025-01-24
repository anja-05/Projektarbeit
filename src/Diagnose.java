import java.sql.Date;

public class Diagnose {
    private int diagnoseID;
    private Date datum;
    private String beschreibung;
    private int patientenID;
    private String icd;
    private String diagnose;

    public Diagnose(int diagnoseID, Date datum, String beschreibung, int patientenID, String icd, String diagnose) {
        this.diagnoseID = diagnoseID;
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.patientenID = patientenID;
        this.icd = icd;
        this.diagnose = diagnose;
    }

    public int getDiagnoseID() {
        return diagnoseID;
    }

    public void setDiagnoseID(int diagnoseID) {
        this.diagnoseID = diagnoseID;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getPatientenID() {
        return patientenID;
    }

    public void setPatientenID(int patientenID) {
        this.patientenID = patientenID;
    }

    public String getIcd() {
        return icd;
    }

    public void setIcd(String icd) {
        this.icd = icd;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }
}
