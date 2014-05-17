package projet_consultation.genererCartes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.generationChart.BarChart3D;
import projet_consultation.generationChart.Cadran;
import projet_consultation.generationChart.CadranDouble_Aiguille;
import projet_consultation.generationChart.Dualaxe;
import projet_consultation.generationChart.LineChart;
 
public class Accessibilite
{ 
    private CarteKPI carte = new CarteKPI();
    private StyleCarte styledecarte = new StyleCarte();
    private String cheminfichierlog;
    private Operateur operateur;

    public void accessibilite(String CheminFichierParametres,String chemingraphiques,String CheminFichierRapport,String cheminfichierlog,Operateur operateur)
    {
        try
   	{
            this.cheminfichierlog=cheminfichierlog;
            this.operateur=operateur;
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                LineChart lineChartAcces1 = new LineChart(chemingraphiques + "Acces1.jpg", cheminfichierlog, "CRD", "CST", "Valeurs en Secondes", operateur);

                BarChart3D barChart3DAcces2 = new BarChart3D(chemingraphiques + "Acces2.jpg", cheminfichierlog, "TCHCR", operateur);

                Cadran cadranAcces3 = new Cadran(chemingraphiques + "Acces3.jpg", cheminfichierlog, "TCHBRBH", operateur);
                Cadran cadranAcces4 = new Cadran(chemingraphiques + "Acces4.jpg", cheminfichierlog, "TCHCRBH", operateur);
                Cadran cadranAcces5 = new Cadran(chemingraphiques + "Acces5.jpg", cheminfichierlog, "TCHDRBH", operateur);
                Cadran cadranAcces6 = new Cadran(chemingraphiques + "Acces6.jpg", cheminfichierlog, "SDCCHBRBH", operateur);
                Cadran cadranAcces7 = new Cadran(chemingraphiques + "Acces7.jpg", cheminfichierlog, "SDCCHCRBH", operateur);
                Cadran cadranAcces8 = new Cadran(chemingraphiques + "Acces8.jpg", cheminfichierlog, "SDCCHDRBH", operateur);

                Dualaxe dualaxeAcces10 = new Dualaxe(chemingraphiques + "Acces9.jpg", cheminfichierlog, "TCHCR", "TCHCRBH", "BH", "Valeurs en %", "Busy Hour", operateur);
                Dualaxe dualaxeAcces12 = new Dualaxe(chemingraphiques + "Acces10.jpg", cheminfichierlog, "TCHDR", "TCHDRBH", "BH", "Valeur en %", "Busy Hour", operateur);

                LineChart lineChartAcces13 = new LineChart(chemingraphiques + "Acces11.jpg", cheminfichierlog, "TCHBR", "TCHCR", "TCHDR", "Valeur %", operateur);
                LineChart lineChartAcces14 = new LineChart(chemingraphiques + "Acces12.jpg", cheminfichierlog, "TCHBRBH", "TCHCRBH", "TCHDRBH", "Valeurs en %", operateur);
                LineChart lineChartAcces15 = new LineChart(chemingraphiques + "Acces13.jpg", cheminfichierlog, "SDCCHBRBH", "SDCCHCRBH", "SDCCHDRBH", "Valeurs en %", operateur);

                BarChart3D barChart3DAcces14 = new BarChart3D(chemingraphiques + "Acces14.jpg", cheminfichierlog, "CSSR", operateur);

                Cadran cadranAcces15 = new Cadran(chemingraphiques + "Acces15.jpg", cheminfichierlog, "CSSR", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleAcces16 = new CadranDouble_Aiguille(chemingraphiques + "Acces16.jpg", cheminfichierlog, "TCHCRBH", "TCHDRBH", operateur);
            }
            else if(operateur.getGeneration().equalsIgnoreCase("3G"))
            {
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
            fichier.ecrire("Classe Accessibilite "+DateduJour+" Heure:"+heuredejour+" problem occured while connecting to databases:" + ex.toString(), cheminfichierlog);
            Logger.getLogger(Accessibilite.class.getName()).log(Level.SEVERE, null, ex);
   	}
    }

    private void tracer_carte(String CheminFichierParametres, String chemingraphiques, String cheminfichierlog)
    {
        String[] tabKpi=null;
        if(operateur.getGeneration().equalsIgnoreCase("2G"))
        {
            tabKpi = new String[3];
            tabKpi[0] = "CSSR";
            tabKpi[1] = "TCHCRBH";
            tabKpi[2] = "TCHDRBH";
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
                ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(tabKpi[i]);
                String type=parametreKPI.getType();
                String typekpi= parametreKPI.getTypekpi();
                double seuil1=parametreKPI.getSeuil1();
                double seuil2=parametreKPI.getSeuil2();
                if(typekpi.trim().equalsIgnoreCase("pourcentage"))
                {
                    seuil1=seuil1/100;
                    seuil2=seuil2/100;
                }
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2, type, CheminFichierParametres+"stylecarteRegions.xml");
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2, type, CheminFichierParametres+"styleAXE.xml");
            }


            catch (IOException ex)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Accessibilité,méthode:tracer_carte: "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + ex.getMessage(), cheminfichierlog);
                Logger.getLogger(Accessibilite.class.getName()).log(Level.SEVERE, null, ex);
            }            catch (SQLException ex)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier(); 
                fichier.ecrire("Classe Accessibilité,méthode:tracer_carte: "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + ex.getMessage(), cheminfichierlog);
                Logger.getLogger(Accessibilite.class.getName()).log(Level.SEVERE, null, ex);
            }            catch (Exception ex)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Accessibilité,méthode:tracer_carte "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur::" + ex.getMessage(), cheminfichierlog);
                Logger.getLogger(Accessibilite.class.getName()).log(Level.SEVERE, null, ex);
            }

            try
            {
                carte.carteKPI(chemingraphiques + "Carte" + kpi + ".jpg", kpi.toLowerCase().trim(), cheminfichierlog, CheminFichierParametres + "stylecarteRegions.xml", operateur);
            }
            catch (SQLException ex)
            {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour = formatter.format(date);

                formatter = new SimpleDateFormat("kk:mm");
                String heuredejour = formatter.format(date);
                Fichier fichier = new Fichier();
                fichier.ecrire("Classe Connexion " + DateduJour + " Heure:" + heuredejour + " problem occured while creating map,Erreur:" + ex.getMessage(), cheminfichierlog);
                Logger.getLogger(Accessibilite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}