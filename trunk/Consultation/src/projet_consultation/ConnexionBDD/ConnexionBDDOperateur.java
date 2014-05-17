
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
import projet_consultation.creation_dossiers.mes_documents;
 
public class ConnexionBDDOperateur
{
    private String BDDGenerale;
    private String BDDOperateur;
    private String adresseIP;
    private String utilisateur;
    private String password;
    private int port;
    private String pilote;
    private Connection c=null;
    
    public ConnexionBDDOperateur(String bdd) throws  SQLException
    {
        try
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
            BDDOperateur=bdd;
            
            Class.forName(pilote);
            c=DriverManager.getConnection("jdbc:postgresql://"+adresseIP+":"+port+"/"+BDDOperateur, utilisateur, password);
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
            fichier.ecrire("Classe Connexion:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement du driver de connexion à la base de données", "", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            mes_documents mes = new mes_documents();
            String cheminfichierlog = mes.get_CheminLog();
            fichier.ecrire("Classe Connexion:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public ConnexionBDDOperateur()
    {
        try
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
        catch (SQLException ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            mes_documents mes = new mes_documents();
            String cheminfichierlog = mes.get_CheminLog();
            fichier.ecrire("Classe Connexion:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
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
            fichier.ecrire("Classe Connexion:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement du driver de connexion à la base de données", "", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            mes_documents mes = new mes_documents();
            String cheminfichierlog = mes.get_CheminLog();
            fichier.ecrire("Classe Connexion:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection()
    {
        return c;
    }
    
    public ResultSet getResultset(String requete)
    {
        try
        {
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet resultat =st.executeQuery(requete);
            return resultat;
        } catch (SQLException ex)
        {
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    public int ExecuterRequete(String requete)
    {
        try
        {
            int j =-1;
            if (c==null || c.isClosed())
            {
                Class.forName(pilote);
                c = DriverManager.getConnection("jdbc:postgresql://" + adresseIP + ":" + port + "/" + BDDOperateur, utilisateur, password);
                
                Statement st = c.createStatement();
                j = st.executeUpdate(requete);
            }else
            {
                Statement st = c.createStatement();
                j = st.executeUpdate(requete);
            }
            return j;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        catch (Exception ex)
        {
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int getNbreLigneResultset(String requete)
    {
        try
        {
            int n=0;
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet resultat =st.executeQuery(requete);
            while (resultat.next())
            {
                n=resultat.getInt("nbre");
            }
            return n;
        } catch (SQLException ex)
        {
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getNbreHeurePeriode(String datedebut,String datefin)
    {
        try
        {
            int n=0;            
            String requete="select count(*) as nbre from (select distinct(heure) from tableregistre  where date>='"+datedebut+"' and date<='"+datefin+"' order by heure) query ";
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet resultat =st.executeQuery(requete);
            while (resultat.next())
            {
                n=resultat.getInt("nbre");
            }
            return n;
        } catch (SQLException ex)
        {
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getNbreJrsPeriode(String datedebut,String datefin)
    {
        try
        {
            int n=0;
            String requete="select count(*) as nbre from (select distinct(date) from tableregistre  where date>='"+datedebut+"' and date<='"+datefin+"') query";
            Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet resultat =st.executeQuery(requete);
            while (resultat.next())
            {
                n=resultat.getInt("nbre");
            }
            return n;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ConnexionBDDOperateur.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
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

    public String getBDDGenerale() {
        return BDDGenerale;
    }

    public void setBDDGenerale(String BDDGenerale) {
        this.BDDGenerale = BDDGenerale;
    }

    public String getBDDOperateur() {
        return BDDOperateur;
    }

    public void setBDDOperateur(String BDDOperateur) {
        this.BDDOperateur = BDDOperateur;
    }

    public void closeConnection()
    {
        try
        {
            c.commit();
        } catch (Exception e) {
        }
        try
        {
            c.close();
        } catch (Exception e) {
        }
    }

    @Override
    protected void finalize() throws Throwable 
    {
        super.finalize();
    }

}
