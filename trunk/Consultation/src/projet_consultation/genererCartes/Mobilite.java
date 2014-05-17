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
import projet_consultation.generationChart.Cadran;
import projet_consultation.generationChart.CadranDouble_Aiguille;
import projet_consultation.generationChart.OverlaidBarchart;
import projet_consultation.generationChart.StackedBarChart;
 
public class Mobilite
{ 
    private CarteKPI carte = new CarteKPI();
    private StyleCarte styledecarte= new StyleCarte();
    private Operateur operateur;

    public void mobilite(String CheminFichierParametres,String chemingraphiques,String CheminFichierRapport,String cheminfichierlog,Operateur operateur)
    {
        try
   	{
            this.operateur=operateur;
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                Cadran cadranHo1 = new Cadran(chemingraphiques + "HO1.jpg", cheminfichierlog, "HOSucces", operateur);

                BarChart3D barChartHo2 = new BarChart3D(chemingraphiques + "HO2.jpg", cheminfichierlog, "HOSucces", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleHo3 = new CadranDouble_Aiguille(chemingraphiques + "HO3.jpg", cheminfichierlog, "HOULQR", "HODLQR", operateur);

                StackedBarChart stackedBarChartHo4 = new StackedBarChart(chemingraphiques + "HO4.jpg", cheminfichierlog, "HOULQR", "HOULLR", operateur);
                StackedBarChart stackedBarChartHo5 = new StackedBarChart(chemingraphiques + "HO5.jpg", cheminfichierlog, "HODLQR", "HODLLR", operateur);

                OverlaidBarchart overlaidBarchartHo6 = new OverlaidBarchart(chemingraphiques + "HO6.jpg", cheminfichierlog, "HOPB", "RXL", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleHo7 = new CadranDouble_Aiguille(chemingraphiques + "HO7.jpg", cheminfichierlog, "IntraBSSHODR", "InterBSSHODR", operateur);

                OverlaidBarchart overlaidBarchartHo8 = new OverlaidBarchart(chemingraphiques + "HO8.jpg", cheminfichierlog, "IntraBSSHODR", "InterBSSHODR", operateur);
            }
            else if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                Cadran cadranHo1 = new Cadran(chemingraphiques + "HO1.jpg", cheminfichierlog, "HOSucces", operateur);

                BarChart3D barChartHo2 = new BarChart3D(chemingraphiques + "HO2.jpg", cheminfichierlog, "HOSucces", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleHo3 = new CadranDouble_Aiguille(chemingraphiques + "HO3.jpg", cheminfichierlog, "HOULQR", "HODLQR", operateur);

                StackedBarChart stackedBarChartHo4 = new StackedBarChart(chemingraphiques + "HO4.jpg", cheminfichierlog, "HOULQR", "HOULLR", operateur);
                StackedBarChart stackedBarChartHo5 = new StackedBarChart(chemingraphiques + "HO5.jpg", cheminfichierlog, "HODLQR", "HODLLR", operateur);

                OverlaidBarchart overlaidBarchartHo6 = new OverlaidBarchart(chemingraphiques + "HO6.jpg", cheminfichierlog, "HOPB", "RXL", operateur);

                CadranDouble_Aiguille cadranDouble_AiguilleHo7 = new CadranDouble_Aiguille(chemingraphiques + "HO7.jpg", cheminfichierlog, "IntraBSSHODR", "InterBSSHODR", operateur);

                OverlaidBarchart overlaidBarchartHo8 = new OverlaidBarchart(chemingraphiques + "HO8.jpg", cheminfichierlog, "IntraBSSHODR", "InterBSSHODR", operateur);
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
            fichier.ecrire("Classe Mobilité,méthode: mobilite "+DateduJour+" Heure:"+heuredejour+" problem occured while creating chart,Erreur:" + ex.getMessage(), cheminfichierlog);
            Logger.getLogger(Mobilite.class.getName()).log(Level.SEVERE, null, ex);
   	}
        
     }

    private void tracer_carte(String CheminFichierParametres,String chemingraphiques,String cheminfichierlog)
    {
        String[] tabKpi=null;
        if(operateur.getGeneration().equalsIgnoreCase("2G"))
        {
            tabKpi = new String[3];
            tabKpi[0] = "HOSucces";
            tabKpi[1] = "HOULQR";
            tabKpi[2] = "HODLQR";
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
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2,type, CheminFichierParametres+"stylecarteRegions.xml");
                styledecarte.modifierStyle(kpi.toLowerCase(),seuil1,seuil2, type, CheminFichierParametres+"styleAXE.xml");
            }
            catch (Exception e)
            {
                Date date=new Date();
                SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour=formatter.format(date);

                formatter= new SimpleDateFormat("kk:mm");
                String heuredejour=formatter.format(date);
                Fichier fichier =new Fichier();
                fichier.ecrire("Classe Mobilite,méthode:tracer_carte: "+DateduJour+" Heure:"+heuredejour+" problem occured while creating map,Erreur:" + e.getMessage(), cheminfichierlog);
                Logger.getLogger(Mobilite.class.getName()).log(Level.SEVERE, null, e);
            }

            try
            {
                carte.carteKPI(chemingraphiques + "Carte" + kpi.toUpperCase() + ".jpg", kpi, cheminfichierlog, CheminFichierParametres + "stylecarteRegions.xml", operateur);
            } catch (SQLException ex) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
                String DateduJour = formatter.format(date);

                formatter = new SimpleDateFormat("kk:mm");
                String heuredejour = formatter.format(date);
                Fichier fichier = new Fichier();
                fichier.ecrire("Classe Mobilite,méthode:tracer_carte: " + DateduJour + " Heure:" + heuredejour + " problem occured while creating map,Erreur:" + ex.getMessage(), cheminfichierlog);
                Logger.getLogger(Mobilite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}