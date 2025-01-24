public class DiagnosenIcd {
        private int diagnoseID;
        private String icd;
        private String diagnose;

        // Konstruktor
        public DiagnosenIcd(int diagnoseID, String icd, String diagnose) {
            this.diagnoseID = diagnoseID;
            this.icd = icd;
            this.diagnose = diagnose;
        }

        // Getter und Setter
        public int getDiagnoseID() {
            return diagnoseID;
        }

        public void setDiagnoseID(int diagnoseID) {
            this.diagnoseID = diagnoseID;
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

        @Override
        public String toString() {
            return icd + " - " + diagnose;
        }
    }

