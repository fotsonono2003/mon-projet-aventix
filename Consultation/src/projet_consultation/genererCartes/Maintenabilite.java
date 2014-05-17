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
import projet_consultation.creation_dossiers.mes_documents;
import projet_consultation.generationChart.BarChart3D;
import projet_consultation.generationChart.CadranDouble_Aiguille;
import projet_consultation.generationChart.Dualaxe;
 
public class Maintenabilite
{ 
    private CarteKPI carte = new CarteKPI();
    private StyleCarte styledecarte= new StyleCarte();
    private Operateur operateur;

    public void maintenabilite(String CheminFichierParametres,String chemingraphiques,String CheminFichierRapport,String cheminfichierlog,Operateur operateur)
    {
        this.operateur=operateur;
        try
   	{
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                BarChart3D barChart3DMaintien1 = new BarChart3D(chemingraphiques + "Maintien1.jpg", cheminfichierlog, "CSR", operateur);
                BarChart3D barChart3DMaintien2 = new BarChart3D(chemingraphiques + "Maintien2.jpg", cheminfichierlog, "CDR", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleMaintien3 = new CadranDouble_Aiguille(chemingraphiques + "Maintien3.jpg", cheminfichierlog, "CSR", "CSRBH", operateur);
                CadranDouble_Aiguille cadranDouble_AiguilleMaintien4 = new CadranDouble_Aiguille(chemingraphiques + "Maintien4.jpg", cheminfichierlog, "CDR", "CDRBH", operateur);

                Dualaxe dualaxeMobil6 = new Dualaxe(chemingraphiques + "Maintien5.jpg", cheminfichierlog, "CDR", "CDRBH", "BH", "Valeurs en %", "Busy Hour", operateur);
            }
            else if(operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                BarChart3D barChart3DMaintien1 = new BarChart3D(chemingraphiques + "Maintien1.jpg", cheminfichierlog, "CSR", operateur);
                BarChart3D barChart3DMaintien2 = new BarChart3D(chemingraphiques + "Maintien2.jpg", cheminfichierlog, "CDR", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleMaintien3 = new CadranDouble_Aiguille(chemingraphiques + "Maintien3.jpg", cheminfichierlog, "CSR", "CSRBH", operateur);
                CadranDouble_Aiguille cadranDouble_AiguilleMaintien4 = new CadranDouble_Aiguille(chemingraphiques + "Maintien4.jpg", cheminfichierlog, "CDR", "CDRBH", operateur);

                Dualaxe dualaxeMobil6 = new Dualaxe(chemingraphiques + "Maintien5.jpg", cheminfichierlog, "CDR", "CDRBH", "BH", "Valeurs en %", "Busy Hour", operateur);
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
            fichier.ecrire("Classe Maintenabilité,méthode: maintenabilite "+DateduJour+" Heure:"+heuredejour+" problem occured while creating chart,Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(Maintenabilite.class.getName()).log(Level.SEVERE, null, ex);
   	}
    }

    private void tracer_carte(String CheminFichierParametres, String chemingraphiques, String cheminfichierlog) 
    {
        String[] tabKpi=null;
        if(operateur.getGeneration().equalsIgnoreCase("2G"))
        {
            tabKpi = new String[4];
            tabKpi[0] = "CSR";
            tabKpi[1] = "CSRBH";
            tabKpi[2] = "CDR";
            tabKpi[3] = "CDRBH";
        }
        else if(operateur.getGeneration().equalsIgnoreCase("3G"))
        {
            tabKpi = new String[3];
            tabKpi[0] = "CSSR";
            tabKpi[1] = "TCHCRBH";
            tabKpi[2] = "TCHDRBH";
        }

        String kpi=null;
        for(int i=0;i<tabKpi.length;i++)
        {
            kpi=tabKpi[i];
            try
            {
                Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
                ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(kpi);
                String type=parametreKPI.getType();
                String typekpi= parametreKPI.getTypekpi();
                double seuil1=parametreKPI.getSeuil1();
                double seuil2=parametreKPI.getSeuil2();
                if(typekpi.toLowerCase().trim().equalsIgnoreCase("pourcentage"))
                {
                    seuil1=seuil1/100;
                    seuil2=seuil2/100;
                }
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2, type, CheminFichierParametres+"stylecarteRegions.xml");
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2, type, CheminFichierParametres+"styleAXE.xml");
            }


            catch (ClassNotFoundException e)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Maintenabilite,méthode:tracer_carte: "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + e.getMessage(), cheminfichierlog);
                Logger.getLogger(Maintenabilite.class.getName()).log(Level.SEVERE, null, e);
                break;
            }            catch (SQLException e)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Maintenabilite,méthode:tracer_carte "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + e.getMessage(), cheminfichierlog);
                Logger.getLogger(Maintenabilite.class.getName()).log(Level.SEVERE, null, e);
            }            catch (Exception e)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);
                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Maintenabilite,méthode:tracer_carte "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + e.getMessage(), cheminfichierlog);
                Logger.getLogger(Maintenabilite.class.getName()).log(Level.SEVERE, null, e);
            }

            try
            {
                carte.carteKPI(chemingraphiques + "Carte" + kpi + ".jpg", kpi, cheminfichierlog, CheminFichierParametres + "stylecarteRegions.xml", operateur);
            } catch (SQLException ex)
            {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour = formatter.format(date);

                formatter = new SimpleDateFormat("kk:mm");
                String heuredejour = formatter.format(date);
                Fichier fichier = new Fichier();
                mes_documents mes = new mes_documents();
                cheminfichierlog = mes.get_CheminLog();
                fichier.ecrire("Classe Maintenabilité,méthode:tracer_carte:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + ex.getMessage(), cheminfichierlog);
                Logger.getLogger(Maintenabilite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
    
}