package projet_consultation.genererCartes;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.generationChart.Cadran;
import projet_consultation.generationChart.CadranDouble_Aiguille;
import projet_consultation.generationChart.Dualaxe;

public class SMS
{ 
    private CarteKPI carte = new CarteKPI();
    private StyleCarte styledecarte= new StyleCarte();
    private Operateur operateur;
    
    public  void sms(String CheminFichierParametres,String chemingraphiques,String CheminFichierRapport,String cheminfichierlog,Operateur operateur)
    {
        try
   	{
            this.operateur=operateur;
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                CadranDouble_Aiguille cadranDouble_AiguilleSMS1 = new CadranDouble_Aiguille(chemingraphiques + "SMS1.jpg", cheminfichierlog, "SMSASR", "RSMSR", operateur);
                Cadran cadranSMS2 = new Cadran(chemingraphiques + "SMS2.jpg", cheminfichierlog, "SMSLR", operateur);
                Dualaxe dualaxeSMS1 = new Dualaxe(chemingraphiques + "SMS3.jpg", cheminfichierlog, "SMSASR", "RSMSR", "SMSLR", "Valeurs en %", "Valeurs en %", operateur);
            }
            else if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                CadranDouble_Aiguille cadranDouble_AiguilleSMS1 = new CadranDouble_Aiguille(chemingraphiques + "SMS1.jpg", cheminfichierlog, "SMSASR", "RSMSR", operateur);
                Cadran cadranSMS2 = new Cadran(chemingraphiques + "SMS2.jpg", cheminfichierlog, "SMSLR", operateur);
                Dualaxe dualaxeSMS1 = new Dualaxe(chemingraphiques + "SMS3.jpg", cheminfichierlog, "SMSASR", "RSMSR", "SMSLR", "Valeurs en %", "Valeurs en %", operateur);
            }
            tracer_carte(CheminFichierParametres, chemingraphiques, cheminfichierlog);
        }
   	catch (Exception ex)
   	{
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe SMS,méthode:SMS:"+DateduJour+" Heure:"+heuredejour+" problem occured while creating chart,Erreur:" + ex.toString(), cheminfichierlog);
            Logger.getLogger(SMS.class.getName()).log(Level.SEVERE, null, ex);
   	}
    }

    private void tracer_carte(String CheminFichierParametres,String chemingraphiques,String cheminfichierlog)
    {
        String[] tabKpi= {"SMSLR"};
        if(operateur.getGeneration().equalsIgnoreCase("2G"))
        {
            tabKpi = new String[1];
            tabKpi[0] = "SMSLR";
        }
        else if(operateur.getGeneration().equalsIgnoreCase("3G"))
        {
            tabKpi = new String[1];
            tabKpi[0] = "CSSR";
        }

        String kpi=null;
        String type=null;
        String typekpi=null;
        double seuil1=0;
        double seuil2=0;
        for(int i=0;i<tabKpi.length;i++)
        {
            kpi=tabKpi[i];
            try
            {
                Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
                ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(tabKpi[i]);
                type=parametreKPI.getType();
                typekpi= parametreKPI.getTypekpi();
                seuil1=parametreKPI.getSeuil1();
                seuil2=parametreKPI.getSeuil2();
                if(typekpi.trim().equalsIgnoreCase("pourcentage"))
                {
                    seuil1=seuil1/100;
                    seuil2=seuil2/100;
                }
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2,type, CheminFichierParametres+"stylecarteRegions.xml");
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2, type, CheminFichierParametres+"styleAXE.xml");
            }
            catch (Exception ex)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Mobilite,méthode:tracer_carte "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + ex.toString(), cheminfichierlog);
                Logger.getLogger(SMS.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                carte.carteKPI(chemingraphiques + "Carte" + kpi.toUpperCase() + ".jpg", kpi, cheminfichierlog, CheminFichierParametres + "stylecarteRegions.xml", operateur);
            } 
            catch (SQLException ex)
            {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour = formatter.format(date);

                formatter = new SimpleDateFormat("kk:mm");
                String heuredejour = formatter.format(date);
                Fichier fichier = new Fichier();
                fichier.ecrire("Classe Mobilite,méthode:tracer_carte " + DateduJour + " Heure:" + heuredejour + " problem occured while crerating map,Erreur:" + ex.toString(), cheminfichierlog);
                Logger.getLogger(SMS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}