package projet_consultation.ClassesGenerales;

public class ParametreKPI
{
    private String nomKpi;
    private String Kpi;
    private String type;
    private String typekpi;
    private double seuil1;
    private double seuil2;
    private double valInf;
    private double valSup;
    private int pas;
 
    public ParametreKPI()
    {
    }

    public ParametreKPI(String nomKpi, String Kpi, String type, String typekpi, double seuil1, double seuil2, double valInf, double valSup, int pas)
    {
        this.nomKpi = nomKpi;
        this.Kpi = Kpi;
        this.type = type;
        this.typekpi = typekpi;
        this.seuil1 = seuil1;
        this.seuil2 = seuil2;
        this.pas = pas;
        this.valInf = valInf;
        this.valSup = valSup;
    }

    public String getKpi() {
        return Kpi;
    }

    public void setKpi(String Kpi) {
        this.Kpi = Kpi;
    }

    public String getNomKpi() {
        return nomKpi;
    }

    public void setNomKpi(String nomKpi) {
        this.nomKpi = nomKpi;
    }

    public int getPas() {
        return pas;
    }

    public void setPas(int pas) {
        this.pas = pas;
    }

    public double getSeuil1() {
        return seuil1;
    }

    public void setSeuil1(double seuil1) {
        this.seuil1 = seuil1;
    }

    public double getSeuil2() {
        return seuil2;
    }

    public void setSeuil2(double seuil2) {
        this.seuil2 = seuil2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypekpi() {
        return typekpi;
    }

    public void setTypekpi(String typekpi) {
        this.typekpi = typekpi;
    }

    public double getValInf() {
        return valInf;
    }

    public void setValInf(double valInf) {
        this.valInf = valInf;
    }

    public double getValSup() {
        return valSup;
    }

    public void setValSup(double valSup) {
        this.valSup = valSup;
    }

}
