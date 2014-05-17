
package projet_consultation.ConnexionBDD;

import java.io.File; 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.creation_dossiers.mes_documents;
 
public class ParamBDD
{
    private String BDDGenerale;
    private String adresseIP;
    private String utilisateur;
    private String password;
    private String port;
    private String operateur;
    private String pilote;
    private static ParamBDD paramBDD=null;

    private ParamBDD()
    {
        SAXBuilder sxb = new SAXBuilder();
        try
        {
            mes_documents mes=new mes_documents();
            Fichier fichier = new Fichier();
            File file=new File(mes.get_CheminParametre()+"parametreconnexion_crypte.xml");
            //File file=new File(this.getClass().getResource("/resources/Fichiers_parametres/parametreconnexion_crypte.xml").getFile());

            if (file.isFile())
            {
                fichier.crypterFichier("bi4tbi4t", mes.get_CheminParametre() + "parametreconnexion_Old.xml", mes.get_CheminParametre() + "parametreconnexion_crypte.xml");
                fichier.decrypterFichier("bi4tbi4t",file.getAbsolutePath(), mes.get_CheminParametre()+"parametreconnexion.xml");
                //String str_fichier = "./resources/Fichiers_parametres/parametreconnexion.xml";
                String str_fichier =mes.get_CheminParametre()+"parametreconnexion.xml";
                File fileTemp=new File(mes.get_CheminParametre()+"parametreconnexion.xml");

                if (fileTemp.isFile())
                {
                    org.jdom.Document document = sxb.build(new File(str_fichier));

                    Element racine = document.getRootElement();
                    List listParam = racine.getChildren("param");

                    Iterator i = listParam.iterator();
                    while (i.hasNext())
                    {
                        Element courant = (Element) i.next();

                        pilote = courant.getChild("pilote").getText().trim();
                        utilisateur = courant.getChild("login").getText().trim();
                        password = courant.getChild("password").getText().trim();
                        adresseIP = courant.getChild("adresseIP").getText().trim();
                        port = courant.getChild("port").getText().trim();
                        BDDGenerale = courant.getChild("bdd").getText().trim();
                        System.out.println("BDD Generale:"+BDDGenerale);
                        //JOptionPane.showMessageDialog(null, "Fin du fichier","",JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (fileTemp.isFile()) {
                        fileTemp.delete();
                    }
                }
                else JOptionPane.showMessageDialog(null, "le fichier contenant les parametres n'existe pas","",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (JDOMException ex)
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

            Logger.getLogger(ParamBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
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

            Logger.getLogger(ParamBDD.class.getName()).log(Level.SEVERE, null, ex);
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
            
            Logger.getLogger(ParamBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ParamBDD getInstance()
    {
        try
        {
            if (paramBDD == null)
            {
                paramBDD = new ParamBDD();
                return  paramBDD;
            }
        } catch (Exception ex)
        {
        }
        return paramBDD;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public String getPilote() {
        return pilote;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP(String adresseIP) {
        this.adresseIP = adresseIP;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setPilote(String pilote) {
        this.pilote = pilote;
    }

    public String getBDDGenerale() {
        return BDDGenerale;
    }

    public void setBDDGenerale(String BDDGenerale) {
        this.BDDGenerale = BDDGenerale;
    }

}
