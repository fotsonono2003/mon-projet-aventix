
package projet_consultation.ClassesGenerales;

public class Operateur
{ 
    private String nomOperateur;
    private String bddOperateur;
    private String nomMV;
    private String AdresseMV;
    private String LoginOperateur;
    private String passwdOperateur;
    private String CodeOperateur;
    private String AdresseBDDOperateur;
    private String Loginmv;
    private String Passwdmv;
    private int Portmv;
    private String Logindcs;
    private String Passwddcs;
    private int Portdcs;
    private String adressemv;
    private int IdOperateur;
    private String equipement;
    private int code_eq;
    private String generation;

    public Operateur(String nomOperateur, String bddOperateur, String nomMV, String AdresseMV, String LoginOperateur, String passwdOperateur, String CodeOperateur, String AdresseBDDOperateur, String Loginsc, String Passwdsc, int Portsc, String Loginsi, String Passwdsi, int Portsi, String adressesc, int IdOperateur,String generation) {
        this.nomOperateur = nomOperateur;
        this.bddOperateur = bddOperateur;
        this.nomMV = nomMV;
        this.AdresseMV = AdresseMV;
        this.LoginOperateur = LoginOperateur;
        this.passwdOperateur = passwdOperateur;
        this.CodeOperateur = CodeOperateur;
        this.AdresseBDDOperateur = AdresseBDDOperateur;
        this.Loginmv = Loginsc;
        this.Passwdmv = Passwdsc;
        this.Portmv = Portsc;
        this.Logindcs = Loginsi;
        this.Passwddcs = Passwdsi;
        this.Portdcs = Portsi;
        this.adressemv = adressesc;
        this.IdOperateur = IdOperateur;
        this.generation=generation;
    }

    public String getEquipement() {
        return equipement;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public void setEquipement(String equipement) {
        this.equipement = equipement;
    }

    public String getAdressesc() {
        return adressemv;
    }

    public void setAdressesc(String adressemv) {
        this.adressemv = adressemv;
    }

    public Operateur() { }

    public String getAdresseMV() {
        return AdresseMV;
    }

    public void setAdresseMV(String AdresseMV) {
        this.AdresseMV = AdresseMV;
    }

    public String getLoginmv() {
        return Loginmv;
    }

    public void setLoginmv(String Loginmv) {
        this.Loginmv = Loginmv;
    }

    public String getLogindcs() {
        return Logindcs;
    }

    public void setLogindcs(String Logindcs) {
        this.Logindcs = Logindcs;
    }

    public String getPasswdmv() {
        return Passwdmv;
    }

    public void setPasswdmv(String Passwdmv) {
        this.Passwdmv = Passwdmv;
    }

    public String getPasswddcs() {
        return Passwddcs;
    }

    public void setPasswddcs(String Passwdsi) {
        this.Passwddcs = Passwdsi;
    }

    public int getPortmv() {
        return Portmv;
    }

    public void setPortmv(int Portmv) {
        this.Portmv = Portmv;
    }

    public int getPortdcs() {
        return Portdcs;
    }

    public void setPortdcs(int Portdcs) {
        this.Portdcs = Portdcs;
    }

    public String getNomMV() {
        return nomMV;
    }

    public void setNomMV(String nomMV) {
        this.nomMV = nomMV;
    }

    public String getAdresseBDDOperateur() {
        return AdresseBDDOperateur;
    }

    public void setAdresseBDDOperateur(String AdresseBDDOperateur) {
        this.AdresseBDDOperateur = AdresseBDDOperateur;
    }

    public String getCodeOperateur() {
        return CodeOperateur;
    }

    public void setCodeOperateur(String CodeOperateur) {
        this.CodeOperateur = CodeOperateur;
    }

    public int getIdOperateur() {
        return IdOperateur;
    }

    public void setIdOperateur(int IdOperateur) {
        this.IdOperateur = IdOperateur;
    }

    public String getLoginOperateur() {
        return LoginOperateur;
    }

    public void setLoginOperateur(String LoginOperateur) {
        this.LoginOperateur = LoginOperateur;
    }

    public String getBddOperateur() {
        return bddOperateur;
    }

    public void setBddOperateur(String bddOperateur) {
        this.bddOperateur = bddOperateur;
    }

    public String getNomOperateur() {
        return nomOperateur;
    }

    public void setNomOperateur(String nomOperateur) {
        this.nomOperateur = nomOperateur;
    }

    public String getPasswdOperateur() {
        return passwdOperateur;
    }

    public void setPasswdOperateur(String passwdOperateur) {
        this.passwdOperateur = passwdOperateur;
    }

    public String getAdressemv() {
        return adressemv;
    }

    public void setAdressemv(String adressemv) {
        this.adressemv = adressemv;
    }

    public int getCode_eq() {
        return code_eq;
    }

    public void setCode_eq(int code_eq) {
        this.code_eq = code_eq;
    }


}
