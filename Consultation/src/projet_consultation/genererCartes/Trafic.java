package projet_consultation.genererCartes;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.generationChart.CadranBH;
import projet_consultation.generationChart.Dualaxe;
import projet_consultation.generationChart.OverlaidBarchart;
import projet_consultation.generationChart.Thermometer;

public class Trafic
{
    private Operateur operateur;

    public void trafic(String CheminFichierParametres,String chemingraphiques,String CheminFichierRapport,String cheminfichierlog,Operateur operateur)
    {
        try
        {
            this.operateur=operateur;
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                //OverlaidBarchart Traficmoyen = new OverlaidBarchart(chemingraphiques+"trafic11.jpg", cheminfichierlog, "TCHM","TCHMBH","BHTR",operateur);

                Dualaxe dualaxeMobil6 = new Dualaxe(chemingraphiques + "trafic1.jpg", cheminfichierlog, "TCHMBH", "TCHM", "BHTR", "Valeurs en Erlang", "Taux de Trafic @ Busy Hour", operateur);

                //Dualaxe dualaxeMobil6=new Dualaxe(chemingraphiques+"Maintien5.jpg", cheminfichierlog,"CDR","CDRBH","BH","Valeurs en %","Busy Hour",operateur);

                Dualaxe dualaxeBusyhour = new Dualaxe(chemingraphiques + "trafic2.jpg", cheminfichierlog, "TCHMBH", "BHTR", "Valeurs en Erlang", "Valeurs en %", operateur);
                //CadranDouble_Aiguille cadranDouble_AiguilleTrafic3 = new CadranDouble_Aiguille(chemingraphiques+"trafic3.jpg",cheminfichierlog,"BH","Busy Hour","BHTR","Taux de trafic @ Busy Hour");

                CadranBH cadranBHbh = new CadranBH(chemingraphiques + "traficBH.jpg", cheminfichierlog, "BH", operateur);
                Thermometer thermometerBHTR = new Thermometer(chemingraphiques + "traficBHTR.jpg", cheminfichierlog, "BHTR", operateur);
            }
            else if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                //OverlaidBarchart Traficmoyen = new OverlaidBarchart(chemingraphiques+"trafic11.jpg", cheminfichierlog, "TCHM","TCHMBH","BHTR",operateur);

                Dualaxe dualaxeMobil6 = new Dualaxe(chemingraphiques + "trafic1.jpg", cheminfichierlog, "TCHMBH", "TCHM", "BHTR", "Valeurs en Erlang", "Taux de Trafic @ Busy Hour", operateur);

                //Dualaxe dualaxeMobil6=new Dualaxe(chemingraphiques+"Maintien5.jpg", cheminfichierlog,"CDR","CDRBH","BH","Valeurs en %","Busy Hour",operateur);

                Dualaxe dualaxeBusyhour = new Dualaxe(chemingraphiques + "trafic2.jpg", cheminfichierlog, "TCHMBH", "BHTR", "Valeurs en Erlang", "Valeurs en %", operateur);
                //CadranDouble_Aiguille cadranDouble_AiguilleTrafic3 = new CadranDouble_Aiguille(chemingraphiques+"trafic3.jpg",cheminfichierlog,"BH","Busy Hour","BHTR","Taux de trafic @ Busy Hour");

                CadranBH cadranBHbh = new CadranBH(chemingraphiques + "traficBH.jpg", cheminfichierlog, "BH", operateur);
                Thermometer thermometerBHTR = new Thermometer(chemingraphiques + "traficBHTR.jpg", cheminfichierlog, "BHTR", operateur);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Trafic.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Trafic,méthode:trafic "+DateduJour+" Heure:"+heuredejour+" problem occured while creating chart,Erreur:" + ex.getMessage(), cheminfichierlog);
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}
