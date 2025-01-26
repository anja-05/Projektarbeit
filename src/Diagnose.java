import java.sql.Date;
/**
 * Die Klasse repräsentiert eine Diagnose für einen Patienten.
 * Sie enthält alle relevanten Informationen zur Diagnose.
 */
public class Diagnose {
    private int diagnoseID;
    private Date datum;
    private String beschreibung;
    private int patientenID;
    private String icd;
    private String diagnose;

    /**
     * Konstruktor, der eine neue Diagnose mit den angegebenen Parametern erstellt.
     *
     * @param diagnoseID   Die eindeutige ID der Diagnose.
     * @param datum        Das Datum der Diagnose.
     * @param beschreibung Die Beschreibung der Diagnose (optional).
     * @param patientenID  Die ID des Patienten, dem die Diagnose zugeordnet ist.
     * @param icd          Der ICD-Code der Diagnose.
     * @param diagnose     Der Name oder die Bezeichnung der Diagnose.
     */
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
