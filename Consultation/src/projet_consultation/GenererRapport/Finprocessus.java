package projet_consultation.GenererRapport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;

public class Finprocessus
{
 
    public void supprime (String ch)
    {
        try
        {
            File MyFile = new File(ch);
            MyFile.delete();
        }
        catch (Exception e)
        {
        }
    }
 
    public void vidertable(String cheminfichierlog,Operateur operateur)
    {  
        try
        {
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="delete  from tablevaleurskpi";
            cn.ExecuterRequete(requete);

            requete="delete  from table_valeurs_regions";
            cn.ExecuterRequete(requete);

            requete="delete  from table_valeurs_bts";
            cn.ExecuterRequete(requete);

            requete="delete  from table_bts_bhtr";
            cn.ExecuterRequete(requete);

            requete="delete  from table_bts_cdrbh";
            cn.ExecuterRequete(requete);

            requete="delete  from table_bts_hosucces";
            cn.ExecuterRequete(requete);

            requete="delete  from table_bts_tchcrbh";
            cn.ExecuterRequete(requete);

            requete="delete  from table_bts_smslr";
            cn.ExecuterRequete(requete);

            requete="delete  from tauxinfo";
            cn.ExecuterRequete(requete);
            cn.closeConnection();
            System.out.println("/*/*/*/*/*/*table de calcul vidées");
        }
        catch (Exception e)
        {
            Logger.getLogger(Envoirapport.class.getName()).log(Level.SEVERE, null, e);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Finprocessus:"+DateduJour+" Heure:"+heuredejour+" echec pilote : "+e.getMessage(), cheminfichierlog);
        }
    }    
}
