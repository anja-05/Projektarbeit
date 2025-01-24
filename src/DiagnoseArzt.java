import java.util.Date;

public class DiagnoseArzt {
    private int arztDiagnoseID;
    private int patientenID;
    private int diagnoseID;
    private Date datum;
    private String beschreibung;

    // Zusätzliche Felder für Anzeigezwecke
    private String icd;
    private String diagnose;

    public DiagnoseArzt(int arztDiagnoseID, int patientenID, int diagnoseID, Date datum, String beschreibung, String icd, String diagnose) {
        this.arztDiagnoseID = arztDiagnoseID;
        this.patientenID = patientenID;
        this.diagnoseID = diagnoseID;
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.icd = icd;
        this.diagnose = diagnose;
    }

    public int getArztDiagnoseID() {
        return arztDiagnoseID;
    }

    public int getPatientenID() {
        return patientenID;
    }

    public int getDiagnoseID() {
        return diagnoseID;
    }

    public Date getDatum() {
        return datum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getIcd() {
        return icd;
    }

    public String getDiagnose() {
        return diagnose;
    }
}

