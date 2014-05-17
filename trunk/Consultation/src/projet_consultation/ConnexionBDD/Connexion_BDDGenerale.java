
package projet_consultation.ConnexionBDD;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.creation_dossiers.mes_documents;

public class Connexion_BDDGenerale
{  
    private String BDDGenerale;
    private String adresseIP;
    private String utilisateur;
    private String password;
    private int port;

    private String pilote;

    private static Connexion_BDDGenerale objCn=null;
    private static Connection c;
    
    private Connexion_BDDGenerale() throws SQLException, ClassNotFoundException
    {
        ParamBDD paramBDD=ParamBDD.getInstance();
        pilote=paramBDD.getPilote();
        utilisateur=paramBDD.getUtilisateur();
        password=paramBDD.getPassword();
        adresseIP=paramBDD.getAdresseIP();
        try
        {
            port = Integer.parseInt(paramBDD.getPort());
        } catch (Exception ex)
        {
            port=21;
        }
        BDDGenerale=paramBDD.getBDDGenerale();

        Class.forName(pilote);
        c=DriverManager.getConnection("jdbc:postgresql://"+adresseIP+":"+port+"/"+BDDGenerale, utilisateur, password);
    }

    public String getBDDGenerale() {
        return BDDGenerale;
    }

    public void setBDDGenerale(String BDDGenerale) {
        this.BDDGenerale = BDDGenerale;
    }

    public String getAdresseIP() { return adresseIP; }

    public String getPassword() { return password;}

    public String getPilote() { return pilote; }

    public int getPort() { return port; }

    public String getUtilisateur() { return utilisateur; }

    public void setAdresseIP_BDD(String adresse)
    {
        adresseIP=adresse;
    }
    
    public void set_Port_BDD(int portBDD)
    {
        port=portBDD;
    }

    public static Connexion_BDDGenerale getInstance() throws SQLException
    {
        try
        {
            if (objCn == null)
            {
                objCn = new Connexion_BDDGenerale();
                return objCn;
            }
            else if(c.isClosed())
            {
                objCn = new Connexion_BDDGenerale();
                return objCn;
            }
        }
        catch (ClassNotFoundException ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            mes_documents mes = new mes_documents();
            String cheminfichierlog = mes.get_CheminLog();
            fichier.ecrire("Classe ConnexionBDDGénérale,méthode:connexion_BDDGenerale:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement du driver de connexion à la base de données", "", JOptionPane.ERROR_MESSAGE);
        }
        return objCn;
    }
    
    public Connection getConnection()
    {
        return c;
    }
    
    public ResultSet getResultset(String requete)
    {
        try
        {
            Statement st = c.createStatement();
            ResultSet resultat =st.executeQuery(requete);
            return resultat;
        } catch (SQLException ex)
        {
            Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
    public String getBDDOperateur(String operateur) throws ClassNotFoundException
    {
        String bd=null;
        try 
        {
            Connexion_BDDGenerale cn = Connexion_BDDGenerale.getInstance();
            String requete = "select nom_bdd from operateur where'"+operateur.trim()+"'";
            ResultSet resultat = cn.getResultset(requete);
            while (resultat.next())
            {
                bd=resultat.getString("nom_bdd").trim();
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
        }
    return bd;
    }

    public String getType(String nomkpi) throws ClassNotFoundException, SQLException
    {
        String type = null;
        Connexion_BDDGenerale cn = Connexion_BDDGenerale.getInstance();
        String requeteCkpi = "select seuil1 , seuil2 ,type,kpi from tableparametrekpi where nomkpi = '" + nomkpi + "'";//tunisie_coordonnees
        ResultSet resultCkpi = cn.getResultset(requeteCkpi);
        while (resultCkpi.next())
        {
            type = resultCkpi.getString("type");
        }
        return type;
    }

    public ParametreKPI getParametresKPIFromNomKPI(String nomkpi) throws SQLException
    {
        String nomKpi = null;
        String Kpi = null;
        String type = null;
        String typekpi = null;
        double seuil1 = 0;
        double seuil2 = 0;
        int pas = 0;
        double valInf = 0;
        double valSup = 0;
        ParametreKPI paramKpi = null;
        Connexion_BDDGenerale cn = Connexion_BDDGenerale.getInstance();
        String requeteCkpi = "select * from tableparametrekpi where nomkpi ='" + nomkpi + "'";//tunisie_coordonnees
        ResultSet resultCkpi = cn.getResultset(requeteCkpi);
        while (resultCkpi.next())
        {
            seuil1 = resultCkpi.getDouble("seuil1");
            seuil2 = resultCkpi.getDouble("seuil2");
            pas = resultCkpi.getInt("pas");
            valInf = resultCkpi.getDouble("valinf");
            valSup = resultCkpi.getDouble("valsup");
            nomKpi = resultCkpi.getString("nomkpi").trim();
            Kpi = resultCkpi.getString("kpi").toLowerCase().trim();
            type = resultCkpi.getString("type").trim();
            typekpi = resultCkpi.getString("typekpi").trim();
        }
        try
        {
            resultCkpi.close();
        } catch (Exception ex)
        {
        }
        paramKpi = new ParametreKPI(nomKpi, Kpi, type, typekpi, seuil1, seuil2, valInf, valSup, pas);
        return paramKpi;
    }

    public ParametreKPI getParametresKPIFromKPI(String kpi)
    {
        String nomKpi = null;
        String Kpi=kpi.toLowerCase();
        String type=null;
        String typekpi=null;
        double seuil1=0;
        double seuil2=0;
        int pas=0;
        double valInf=0;
        double valSup=0;
        ParametreKPI paramKpi=null;
        try
        {
            Connexion_BDDGenerale cn=Connexion_BDDGenerale.getInstance();
            String requeteCkpi="select * from tableparametrekpi where lower(trim(kpi))='"+kpi.toLowerCase()+"'";
            ResultSet resultCkpi = cn.getResultset(requeteCkpi);
            while(resultCkpi.next())
            {
                try
                {
                    seuil1 = resultCkpi.getDouble("seuil1");
                } catch (Exception ex)
                {
                    seuil1=0;
                    Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
                }
                try
                {
                    seuil2 = resultCkpi.getDouble("seuil2");
                } catch (Exception ex)
                {
                    seuil1=0;
                    Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
                }
                try
                {
                    pas = resultCkpi.getInt("pas");
                } catch (Exception ex)
                {
                    pas=1;
                    Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
                }
                try
                {
                    valInf = resultCkpi.getDouble("valinf");
                } catch (Exception ex)
                {
                    valInf=0;
                    Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
                }
                try
                {
                    valSup = resultCkpi.getDouble("valsup");
                } catch (Exception ex)
                {
                    valSup=0;
                    Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try
                {
                    nomKpi = resultCkpi.getString("nomkpi").trim();
                } catch (SQLException sQLException)
                {
                    nomKpi="";
                }

                try
                {
                    type = resultCkpi.getString("type").trim();
                } catch (Exception ex)
                {
                    type="";
                }
                try
                {
                    typekpi = resultCkpi.getString("typekpi").trim();
                } catch (Exception ex)
                {
                    typekpi="";
                    Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            paramKpi=new ParametreKPI(nomKpi, Kpi, type, typekpi, seuil1, seuil2, valInf, valSup, pas);

            try {
                resultCkpi.close();
            } catch (Exception ex) {
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Connexion_BDDGenerale.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paramKpi;
    }

    public void StopConn()
    {
        try
        {
            c.commit();
            c.close();
            objCn=null;
        }
        catch (Exception ex)
        {}
    }

}
