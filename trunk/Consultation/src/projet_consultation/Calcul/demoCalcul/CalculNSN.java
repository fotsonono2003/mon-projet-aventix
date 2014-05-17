package projet_consultation.Calcul.demoCalcul;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Operateur;

public class CalculNSN
{
    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double  tchhm=0;
    private double  tchm=0,Gtchm=0,NGtchm=0,DGtchm=0;
    private double  tchmhm=0,Gtchmhm=0,NGtchmhm=0,DGtchmhm=0;
    private double  tchmbh=0,Gtchmbh=0,NGtchmbh=0,DGtchmbh=0;
    private double [] TabTchmGlobal=new double[24];
    private double cssr=0,Gcssr=0,NGcssr=0,DGcssr=0;
    private double bhtr=0,Gbhtr=0,NGbhtr=0,DGbhtr=0;
    private double tchcr=0,Gtchcr=0,NGtchcr=0,DGtchcr=0;
    private double tchcrbh=0,Gtchcrbh=0,NGtchcrbh=0,DGtchcrbh=0;
    private double tchdr=0,Gtchdr=0,NGtchdr=0,DGtchdr=0;
    private double tchdrbh=0,Gtchdrbh=0,NGtchdrbh=0,DGtchdrbh=0;
    private double sdcchbrbh=0,Gsdcchbrbh=0,NGsdcchbrbh=0,DGsdcchbrbh=0;
    private double sdcchcrbh=0,Gsdcchcrbh=0,NGsdcchcrbh=0,DGsdcchcrbh=0;
    private double sdcchdrbh=0,Gsdcchdrbh=0,NGsdcchdrbh=0,DGsdcchdrbh=0;
    private double csr=0,Gcsr=0,NGcsr=0,DGcsr=0;
    private double csrbh=0,Gcsrbh=0,NGcsrbh=0,DGcsrbh=0;
    private double cdr=0,Gcdr=0,NGcdr=0,DGcdr=0;
    private double cdrbh=0,Gcdrbh=0,NGcdrbh=0,DGcdrbh=0;
    private double HoSucces=0,GHosucces=0,NGHosucces=0,DGHosucces=0;
    private double TchmBhGlobal=0;
    private int BhG=-1,BhRegion=-1,BhCellule=-1;
    private int[] TabBhGlobal=new int[24];
    private double tchbr=0,Gtchbr=0,NGtchbr=0,DGtchbr=0;
    private double tchbrbh=0,Gtchbrbh=0,NGtchbrbh=0,DGtchbrbh=0;
    private double Rsmsr=0,GRsmsr=0,NGRsmsr=0,DGRsmsr=0;

    private Map<String,Double> mapTraficBhRegion=null;
    private Map<String,Integer> mapBhRegion=null;
    private Map<String,Double> mapTrHoraireMoyenRegion=null;

    private Map<String,Double> mapTraficBhCellule=null ;
    private Map<String,Integer> mapBhCellule=null;
    private Map<String,Double> mapTrHoraireMoyenCellule=null;
    
    private double smslr=0,Gsmslr=0,NGsmslr=0,DGsmslr=0;
    private String bddoperateur;

    public CalculNSN()
    {
        cn = new ConnexionBDDOperateur();
    }

    public CalculNSN(Operateur operateur,String datedebut,String datefin) throws SQLException
    {
        dateDebut=datedebut;
        dateFin=datefin;
        cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
    }

    public void CalculTotal()
    {
        try
        {
            List<String> lstRegion = new ArrayList<String>();
            String requete = "select distinct(region) from table_regions";
            ResultSet result = cn.getResultset(requete);
            while (result.next())
            {
                lstRegion.add(result.getString("region").trim());
                //System.out.println("Liste:" + lstRegion);
            }
            CalCulBH(lstRegion);
            int n=lstRegion.size();
            for (int i=0;i<n;i++)
            {
                String region=lstRegion.get(i);
                CalculRegion(region);
            }
            CalculValeurGlobales();
            if(BhG>=0)
                InsertTable("Global");
            ///////////////////Calcul par cellule
            List<String> lstCellule = new ArrayList<String>();
            requete = "select distinct(cell_name) from tableregistre";
            result = cn.getResultset(requete);
            while (result.next())
            {
                lstCellule.add(result.getString("cell_name").trim());
                //System.out.println("Liste:" + lstCellule);
            }
            int nbreCellule=0;
            CalculBhParCellule(lstCellule);
            nbreCellule=lstCellule.size();
            for(int i=0;i<nbreCellule;i++)
            {
                CalculCellule(lstCellule.get(i));
            }
        }

        catch (SQLException ex)
        {
            Logger.getLogger(CalculNSN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CalculRegion(String region)
    {
        try
        {
            String requete="select * from tableregistre where trim(region) ='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"' ";
            ResultSet result=cn.getResultset(requete);
            double Ntchm=0,Dtchm=0;tchm=0;
            double Ntchmbh=0,Dtchmbh=0;tchmbh=0;
            double Ntchcr=0,Dtchcr=0;tchcr=0;
            double Ntchcrbh=0,Dtchcrbh=0;tchcrbh=0;
            double Ntchdr=0,Dtchdr=0;tchdr=0;
            double Ntchdrbh=0,Dtchdrbh=0;tchdrbh=0;
            double Ntchbr=0,Dtchbr=0;tchbr=0;
            double Ntchbrbh=0,Dtchbrbh=0;tchbrbh=0;
            double Ncssr=0,Dcssr=0;cssr=0;
            double Nsdcchcrbh=0,Dsdcchcrbh=0;sdcchcrbh=0;
            double Nsdcchdrbh=0,Dsdcchdrbh=0;sdcchdrbh=0;
            double Ncdr=0,Dcdr=0;cdr=0;
            double Ncdrbh=0,Dcdrbh=0;cdrbh=0;
            double Ncsr=0,Dcsr=0;csr=0;
            double Ncsrbh=0,Dcsrbh=0;csrbh=0;
            double csrRegion=0,csrGlobal=0;
            double NHoSucces=0,DHoSucces=0;HoSucces=0;
            float NRsmsr=0,DRsmsr=0;Rsmsr=0;
            double Nsmslr=0,Dsmslr=0;smslr=0;
            System.out.println("---------------------------------------------------------------------------");
            while (result.next())
            {
                double val=0,val1=0,val2=0,cssrCour=0;
                Ntchm=Ntchm+result.getFloat("mc718")+result.getFloat("mc717a")+result.getFloat("mc717b");
                Dtchm=1;

                val=result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10");val1=result.getFloat("mc01")+result.getFloat("mc02");val2=result.getFloat("mc140a");
                if(val>0 && val1>0 && val2>0)
                {
                    Ncssr=Ncssr+1-(((result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"))/(val)*100)/(val1))*(1-((result.getFloat("mc140a")-result.getFloat("mc718"))/val2));
                    Dcssr=1;
                }

                Ntchcr=Ntchcr+100*result.getFloat("mc812");
                Dtchcr=Dtchcr+result.getFloat("mc140a");

                Ntchdr=Ntchdr+result.getFloat("mc14c")+result.getFloat("mc739")+result.getFloat("mc736")+ result.getFloat("mc621")+result.getFloat("mc921c");
                Dtchdr=1;

                Nsdcchcrbh=Nsdcchcrbh+result.getFloat("mc04");
                Dsdcchcrbh=Dsdcchcrbh+result.getFloat("c01");

                Nsdcchdrbh=Nsdcchdrbh+100*(result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"));
                Dsdcchdrbh=Dsdcchdrbh+result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10");

                Ncdr=Ncdr+result.getFloat("mc621")+result.getFloat("mc14c")+result.getFloat("mc736")+result.getFloat("mc739")+result.getFloat("mc921c");
                Dcdr=Dcdr+(result.getFloat("mc718")+(result.getFloat("mc717a")+result.getFloat("mc717b"))-result.getFloat("mc712"));

                double cdrCour=0,NcdrCour=0,DcdrCour=0;
                NcdrCour=result.getFloat("mc621")+result.getFloat("mc14c")+result.getFloat("mc736")+result.getFloat("mc739")+result.getFloat("mc921c");
                DcdrCour=result.getFloat("mc718")+(result.getFloat("mc717a")+result.getFloat("mc717b"))-result.getFloat("mc712");
                if(DcdrCour>0)
                    cdrCour=NcdrCour/DcdrCour;
                else cdrCour = 0;

                val=result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10");
                val2=result.getFloat("mc140a");
                if(val>0 && val2>0)
                {
                    cssrCour=1-(((result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"))/(val)*100)/(result.getFloat("mc01")+result.getFloat("mc02")))*(1-((result.getFloat("mc140a")-result.getFloat("mc718"))/val2));
                    Ncsr=Ncsr+cssrCour*(1-cdrCour);
                    Dcsr=1;
                }

                double Outgoing=0,Incoming=0;
                val=result.getFloat("mc645a")+result.getFloat("mc655a");
                val1=result.getFloat("mc820")+result.getFloat("mc830");
                if(val>0 && val1>0)
                {
                    Outgoing=(result.getFloat("mc646")+result.getFloat("mc656"))/(val);
                    Incoming=(result.getFloat("mc642")+result.getFloat("mc652"))/(val1);
                    NHoSucces=NHoSucces+Outgoing+Incoming;
                    DHoSucces=1;
                }

                Nsmslr =Nsmslr+result.getFloat("c194a")+result.getFloat("c194b")+result.getFloat("c194c")+result.getFloat("c195");
                Dsmslr=1;

                String str=result.getString("heure").trim();
                int heure=-1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex)
                {
                    heure=-1;
                    Logger.getLogger(CalculNSN.class.getName()).log(Level.SEVERE, null, ex);
                }

                if(heure==BhG && heure>=0)
                {
                    Ntchmbh=Ntchmbh+result.getFloat("mc718")+result.getFloat("mc717a")+result.getFloat("mc717b");
                    Dtchmbh=1;

                    Ntchcrbh=Ntchcrbh+100*result.getFloat("mc812");
                    Dtchcrbh=Dtchcrbh+result.getFloat("mc140a");

                    Ntchdrbh=Ntchdrbh+result.getFloat("mc14c")+result.getFloat("mc739")+result.getFloat("mc736")+ result.getFloat("mc621")+result.getFloat("mc921c");
                    Dtchdrbh=1;

                    Ncdrbh=Ncdrbh+result.getFloat("mc621")+result.getFloat("mc14c")+result.getFloat("mc736")+result.getFloat("mc739")+result.getFloat("mc921c");
                    Dcdrbh=Dcdrbh+(result.getFloat("mc718")+(result.getFloat("mc717a")+result.getFloat("mc717b"))-result.getFloat("mc712"));

                    cssrCour=0;
                    cssrCour=1-(((result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"))/(result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10"))*100)/(result.getFloat("mc01")+result.getFloat("mc02")))*(1-((result.getFloat("mc140a")-result.getFloat("mc718"))/result.getFloat("mc140a")));
                    Ncsrbh=Ncsrbh+cssrCour*(1-cdrCour);
                }
            }

            if(Dtchm!=0)
            {
                tchm=Ntchm/Dtchm;
                tchm=(double)((int)(tchm*100))/100;
            }
            else tchm=0;
            NGtchm=NGtchm+Ntchm;
            DGtchm=1;

            if(Dcssr!=0)
            {
                cssr=Ncssr/Dcssr;
                cssr=(double)((int)(cssr*100))/100;
            }
            else cssr=0;
            NGcssr=NGcssr+Ncssr;
            DGcssr=1;

            if(Dtchcr!=0)
            {
                tchcr=Ntchcr/Dtchcr;
                tchcr=(double)((int)(tchcr*100))/100;
            }
            else tchcr=0;
            NGtchcr=NGtchcr+Ntchcr;
            DGtchcr=DGtchcr+Dtchcr;

            if(Dtchdr!=0)
            {
                tchdr=Ntchdr/Dtchdr;
                tchdr=(double)((int)(tchdr*100))/100;
            }
            else tchdr=0;
            NGtchdr=NGtchdr+Ntchdr;
            DGtchdr=1;

            if(Dsdcchcrbh!=0)
            {
                sdcchcrbh=Nsdcchcrbh/Dsdcchcrbh;
                sdcchcrbh=(double)((int)(sdcchcrbh*100))/100;
            }
            else sdcchcrbh=0;
            NGsdcchcrbh=NGsdcchcrbh+Nsdcchcrbh;
            DGsdcchcrbh=DGsdcchcrbh+Dsdcchcrbh;

            if(Dsdcchdrbh!=0)
            {
                sdcchdrbh=Nsdcchdrbh/Dsdcchdrbh;
                sdcchdrbh=(double)((int)(sdcchdrbh*100))/100;
            }
            else sdcchdrbh=0;
            NGsdcchdrbh=NGsdcchdrbh+Nsdcchdrbh;
            DGsdcchdrbh=DGsdcchdrbh+Dsdcchdrbh;

            if(Dcsr!=0)
            {
                csr=Ncsr/Dcsr;
                csr=(double)((int)(csr*100))/100;
            }
            else sdcchcrbh=0;
            NGcsr=NGcsr+Ncsr;
            DGcsr=1;

            if(Dsmslr!=0)
            {
                smslr=Nsmslr/Dsmslr;
                smslr=(double)((int)(smslr*100))/100;
            }
            else smslr=0;
            NGsmslr=NGsmslr+Nsmslr;
            DGsmslr=1;

            if(DHoSucces!=0)
            {
                HoSucces=NHoSucces/DHoSucces;
                HoSucces=(double)((int)(HoSucces*100))/100;
            }
            else HoSucces=0;
            NGHosucces=NGHosucces+NHoSucces;
            DGHosucces=1;

            if(Dtchmbh!=0)
            {
                tchmbh=NHoSucces/DHoSucces;
                tchmbh=(double)((int)(tchmbh*100))/100;
            }
            else tchmbh=0;
            NGtchmbh=NGtchmbh+Ntchmbh;
            DGtchmbh=1;

            if(Dtchcrbh!=0)
            {
                tchcrbh=Ntchcrbh/Dtchcrbh;
                tchcrbh=(double)((int)(tchcrbh*100))/100;
            }
            else tchcrbh=0;
            NGtchcrbh=NGtchcrbh+Ntchcrbh;
            DGtchcrbh=DGtchcrbh+Dtchcrbh;

            if(Dtchdrbh!=0)
            {
                tchdrbh=Ntchdrbh/Dtchdrbh;
                tchdrbh=(double)((int)(tchdrbh*100))/100;
            }
            else tchdrbh=0;
            NGtchdrbh=NGtchdrbh+Ntchdrbh;
            DGtchdrbh=1;

            if(Dcdrbh!=0)
            {
                cdrbh=Ntchdrbh/Dtchdrbh;
                cdrbh=(double)((int)(cdrbh*100))/100;
            }
            else cdrbh=0;
            NGcdrbh=NGcdrbh+Ncdrbh;
            DGcdrbh=DGcdrbh+Dcdrbh;

            if(Dcsrbh!=0)
            {
                csrbh=Ncsrbh/Dcsrbh;
                csrbh=(double)((int)(csrbh*100))/100;
            }
            else csrbh=0;
            NGcsrbh=NGcsrbh+Ncsrbh;
            DGcsrbh=DGcsrbh+Dcsrbh;
            Object obj=mapBhRegion.get(region);
            Object obj1=mapTrHoraireMoyenRegion.get(region);
            Object obj2=mapTraficBhRegion.get(region);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                BhRegion=mapBhRegion.get(region);
                tchmhm=mapTrHoraireMoyenRegion.get(region);
                tchmbh=mapTraficBhRegion.get(region);
                if(tchmhm>0)
                {
                    bhtr=tchmbh/(24*tchmhm);
                } else bhtr=0;
                bhtr=(double)((int)(bhtr*100))/100;
                InsertTable(region);
            }
            System.out.println("*******************************************************************************");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculNSN.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void CalculCellule(String cellule)
    {
        try
        {
            String requete="select * from tableregistre where trim(cell_name) ='"+cellule+"' and date>='"+dateDebut+"' and date<='"+dateFin+"' ";
            ResultSet result=cn.getResultset(requete);
            double Ntchm=0,Dtchm=0;tchm=0;
            double Ntchmbh=0,Dtchmbh=0;tchmbh=0;
            double Ntchcr=0,Dtchcr=0;tchcr=0;
            double Ntchcrbh=0,Dtchcrbh=0;tchcrbh=0;
            double Ntchdr=0,Dtchdr=0;tchdr=0;
            double Ntchdrbh=0,Dtchdrbh=0;tchdrbh=0;
            double Ntchbr=0,Dtchbr=0;tchbr=0;
            double Ntchbrbh=0,Dtchbrbh=0;tchbrbh=0;
            double Ncssr=0,Dcssr=0;cssr=0;
            double Nsdcchcrbh=0,Dsdcchcrbh=0;sdcchcrbh=0;
            double Nsdcchdrbh=0,Dsdcchdrbh=0;sdcchdrbh=0;
            double Ncdr=0,Dcdr=0;cdr=0;
            double Ncdrbh=0,Dcdrbh=0;cdrbh=0;
            double Ncsr=0,Dcsr=0;csr=0;
            double Ncsrbh=0,Dcsrbh=0;csrbh=0;
            double csrRegion=0,csrGlobal=0;
            double NHoSucces=0,DHoSucces=0;HoSucces=0;
            float NRsmsr=0,DRsmsr=0;Rsmsr=0;
            double Nsmslr=0,Dsmslr=0;smslr=0;
            System.out.println("---------------------------------------------------------------------------");
            while (result.next())
            {
                double val=0,val1=0,val2=0,cssrCour=0;
                Ntchm=Ntchm+result.getFloat("mc718")+result.getFloat("mc717a")+result.getFloat("mc717b");
                Dtchm=1;

                val=result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10");val1=result.getFloat("mc01")+result.getFloat("mc02");val2=result.getFloat("mc140a");
                if(val>0 && val1>0 && val2>0)
                {
                    Ncssr=Ncssr+1-(((result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"))/(val)*100)/(val1))*(1-((result.getFloat("mc140a")-result.getFloat("mc718"))/val2));
                    Dcssr=1;
                }

                Ntchcr=Ntchcr+100*result.getFloat("mc812");
                Dtchcr=Dtchcr+result.getFloat("mc140a");

                Ntchdr=Ntchdr+result.getFloat("mc14c")+result.getFloat("mc739")+result.getFloat("mc736")+ result.getFloat("mc621")+result.getFloat("mc921c");
                Dtchdr=1;

                Nsdcchcrbh=Nsdcchcrbh+result.getFloat("mc04");
                Dsdcchcrbh=Dsdcchcrbh+result.getFloat("c01");

                Nsdcchdrbh=Nsdcchdrbh+100*(result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"));
                Dsdcchdrbh=Dsdcchdrbh+result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10");

                Ncdr=Ncdr+result.getFloat("mc621")+result.getFloat("mc14c")+result.getFloat("mc736")+result.getFloat("mc739")+result.getFloat("mc921c");
                Dcdr=Dcdr+(result.getFloat("mc718")+(result.getFloat("mc717a")+result.getFloat("mc717b"))-result.getFloat("mc712"));

                double cdrCour=0,NcdrCour=0,DcdrCour=0;
                NcdrCour=result.getFloat("mc621")+result.getFloat("mc14c")+result.getFloat("mc736")+result.getFloat("mc739")+result.getFloat("mc921c");
                DcdrCour=result.getFloat("mc718")+(result.getFloat("mc717a")+result.getFloat("mc717b"))-result.getFloat("mc712");
                if(DcdrCour>0)
                    cdrCour=NcdrCour/DcdrCour;
                else cdrCour = 0;

                val=result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10");
                val2=result.getFloat("mc140a");
                if(val>0 && val2>0)
                {
                    cssrCour=1-(((result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"))/(val)*100)/(result.getFloat("mc01")+result.getFloat("mc02")))*(1-((result.getFloat("mc140a")-result.getFloat("mc718"))/val2));
                    Ncsr=Ncsr+cssrCour*(1-cdrCour);
                    Dcsr=1;
                }

                double Outgoing=0,Incoming=0;
                val=result.getFloat("mc645a")+result.getFloat("mc655a");
                val1=result.getFloat("mc820")+result.getFloat("mc830");
                if(val>0 && val1>0)
                {
                    Outgoing=(result.getFloat("mc646")+result.getFloat("mc656"))/(val);
                    Incoming=(result.getFloat("mc642")+result.getFloat("mc652"))/(val1);
                    NHoSucces=NHoSucces+Outgoing+Incoming;
                    DHoSucces=1;
                }

                Nsmslr =Nsmslr+result.getFloat("c194a")+result.getFloat("c194b")+result.getFloat("c194c")+result.getFloat("c195");
                Dsmslr=1;

                String str=result.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) { heure=-1; }
                
                if(heure==BhCellule && heure>=0)
                {
                    Ntchmbh=Ntchmbh+result.getFloat("mc718")+result.getFloat("mc717a")+result.getFloat("mc717b");
                    Dtchmbh=1;

                    Ntchcrbh=Ntchcrbh+100*result.getFloat("mc812");
                    Dtchcrbh=Dtchcrbh+result.getFloat("mc140a");

                    Ntchdrbh=Ntchdrbh+result.getFloat("mc14c")+result.getFloat("mc739")+result.getFloat("mc736")+ result.getFloat("mc621")+result.getFloat("mc921c");
                    Dtchdrbh=1;

                    Ncdrbh=Ncdrbh+result.getFloat("mc621")+result.getFloat("mc14c")+result.getFloat("mc736")+result.getFloat("mc739")+result.getFloat("mc921c");
                    Dcdrbh=Dcdrbh+(result.getFloat("mc718")+(result.getFloat("mc717a")+result.getFloat("mc717b"))-result.getFloat("mc712"));

                    cssrCour=0;
                    cssrCour=1-(((result.getFloat("mc138")+result.getFloat("mc137")+result.getFloat("mc07"))/(result.getFloat("mc01")+result.getFloat("mc02")+result.getFloat("mc10"))*100)/(result.getFloat("mc01")+result.getFloat("mc02")))*(1-((result.getFloat("mc140a")-result.getFloat("mc718"))/result.getFloat("mc140a")));
                    Ncsrbh=Ncsrbh+cssrCour*(1-cdrCour);
                }
            }

            if(Dtchm!=0)
            {
                tchm=Ntchm/Dtchm;
                tchm=(double)((int)(tchm*100))/100;
            }
            else tchm=0;
            NGtchm=NGtchm+Ntchm;
            DGtchm=1;

            if(Dcssr!=0)
            {
                cssr=Ncssr/Dcssr;
                cssr=(double)((int)(cssr*100))/100;
            }
            else cssr=0;
            NGcssr=NGcssr+Ncssr;
            DGcssr=1;

            if(Dtchcr!=0)
            {
                tchcr=Ntchcr/Dtchcr;
                tchcr=(double)((int)(tchcr*100))/100;
            }
            else tchcr=0;
            NGtchcr=NGtchcr+Ntchcr;
            DGtchcr=DGtchcr+Dtchcr;

            if(Dtchdr!=0)
            {
                tchdr=Ntchdr/Dtchdr;
                tchdr=(double)((int)(tchdr*100))/100;
            }
            else tchdr=0;
            NGtchdr=NGtchdr+Ntchdr;
            DGtchdr=1;

            if(Dsdcchcrbh!=0)
            {
                sdcchcrbh=Nsdcchcrbh/Dsdcchcrbh;
                sdcchcrbh=(double)((int)(sdcchcrbh*100))/100;
            }
            else sdcchcrbh=0;
            NGsdcchcrbh=NGsdcchcrbh+Nsdcchcrbh;
            DGsdcchcrbh=DGsdcchcrbh+Dsdcchcrbh;

            if(Dsdcchdrbh!=0)
            {
                sdcchdrbh=Nsdcchdrbh/Dsdcchdrbh;
                sdcchdrbh=(double)((int)(sdcchdrbh*100))/100;
            }
            else sdcchdrbh=0;
            NGsdcchdrbh=NGsdcchdrbh+Nsdcchdrbh;
            DGsdcchdrbh=DGsdcchdrbh+Dsdcchdrbh;

            if(Dcsr!=0)
            {
                csr=Ncsr/Dcsr;
                csr=(double)((int)(csr*100))/100;
            }
            else sdcchcrbh=0;
            NGcsr=NGcsr+Ncsr;
            DGcsr=1;

            if(Dsmslr!=0)
            {
                smslr=Nsmslr/Dsmslr;
                smslr=(double)((int)(smslr*100))/100;
            }
            else smslr=0;
            NGsmslr=NGsmslr+Nsmslr;
            DGsmslr=1;

            if(DHoSucces!=0)
            {
                HoSucces=NHoSucces/DHoSucces;
                HoSucces=(double)((int)(HoSucces*100))/100;
            }
            else HoSucces=0;
            NGHosucces=NGHosucces+NHoSucces;
            DGHosucces=1;

            if(Dtchmbh!=0)
            {
                tchmbh=NHoSucces/DHoSucces;
                tchmbh=(double)((int)(tchmbh*100))/100;
            }
            else tchmbh=0;
            NGtchmbh=NGtchmbh+Ntchmbh;
            DGtchmbh=1;

            if(Dtchcrbh!=0)
            {
                tchcrbh=Ntchcrbh/Dtchcrbh;
                tchcrbh=(double)((int)(tchcrbh*100))/100;
            }
            else tchcrbh=0;
            NGtchcrbh=NGtchcrbh+Ntchcrbh;
            DGtchcrbh=DGtchcrbh+Dtchcrbh;

            if(Dtchdrbh!=0)
            {
                tchdrbh=Ntchdrbh/Dtchdrbh;
                tchdrbh=(double)((int)(tchdrbh*100))/100;
            }
            else tchdrbh=0;
            NGtchdrbh=NGtchdrbh+Ntchdrbh;
            DGtchdrbh=1;

            if(Dcdrbh!=0)
            {
                cdrbh=Ntchdrbh/Dtchdrbh;
                cdrbh=(double)((int)(cdrbh*100))/100;
            }
            else cdrbh=0;
            NGcdrbh=NGcdrbh+Ncdrbh;
            DGcdrbh=DGcdrbh+Dcdrbh;

            if(Dcsrbh!=0)
            {
                csrbh=Ncsrbh/Dcsrbh;
                csrbh=(double)((int)(csrbh*100))/100;
            }
            else csrbh=0;
            NGcsrbh=NGcsrbh+Ncsrbh;
            DGcsrbh=DGcsrbh+Dcsrbh;
            Object obj=mapBhCellule.get(cellule);
            Object obj1=mapTrHoraireMoyenCellule.get(cellule);
            Object obj2=mapTraficBhCellule.get(cellule);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                BhCellule=mapBhCellule.get(cellule);
                tchmhm=mapTrHoraireMoyenCellule.get(cellule);
                tchmbh=mapTraficBhCellule.get(cellule);
                if(tchmhm>0)
                {
                    bhtr=tchmbh/(24*tchmhm);
                } else bhtr=0;
                bhtr=(double)((int)(bhtr*100))/100;
                InsertTableCellule(cellule);
            }
            System.out.println("*******************************************************************************");
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculNSN.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void getBhParRegion(String region)
    {
        int [] TabBhRegion=new int[24];
        double[] TabTchmRegion=new double[24];
        
        for (int i = 0; i < 24; i++)
        {
            TabBhRegion[i]=-1;
            TabTchmRegion[i]=0;
        }
        try
        {
            String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            ResultSet resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                String str=resultTotal.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) { heure=-1; }
                
                if(heure>=0)
                {
                    TabBhRegion[heure]=heure;
                    TabTchmRegion[heure]=TabTchmRegion[heure]+resultTotal.getFloat("mc718")+resultTotal.getFloat("mc717a")+resultTotal.getFloat("mc717b");

                    TabBhGlobal[heure]=heure;
                    TabTchmGlobal[heure]=TabTchmGlobal[heure]+resultTotal.getFloat("mc718")+resultTotal.getFloat("mc717a")+resultTotal.getFloat("mc717b");
                }
            }

            /***Calcul de Tchm & Bh par region***/
            int nbreJr=0;
            requete="select distinct(date) from tableregistre where region='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'";
            nbreJr=cn.getNbreLigneResultset(requete);
            int n=TabTchmRegion.length;
            double TchmBhRegion=0;BhRegion=-1;
            double som=0;
            for(int l=0;l<n; l++)
            {
                som=som+TabTchmRegion[l];
                if(TabTchmRegion[l]>TchmBhRegion)
                {
                    TchmBhRegion=TabTchmRegion[l];
                    BhRegion=TabBhRegion[l];
                }
            }
            if(nbreJr>0)
                som=som/(24*nbreJr);
            else som=0;
            som=(double)((int)(som*100))/100;
            TchmBhRegion=(double)((int)(TchmBhRegion*100))/100;
            if(BhRegion>=0)
            {
                mapBhRegion.put(region, BhRegion);
                mapTraficBhRegion.put(region, TchmBhRegion);
                mapTrHoraireMoyenRegion.put(region, som);
            }            
        } catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getBhParCellule(String cellule)
    {
        int [] TabBhCellule=new int[24];
        double[] TabTchmCellule=new double[24];

        for (int i = 0; i < 24; i++)
        {
            TabBhCellule[i]=-1;
            TabTchmCellule[i]=0;
        }
        try
        {
            String requete = "select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            ResultSet resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                String str=resultTotal.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) { heure=-1; }
                if(heure>=0)
                {
                    TabBhCellule[heure]=heure;
                    TabTchmCellule[heure]=TabTchmCellule[heure]+resultTotal.getFloat("mc718")+resultTotal.getFloat("mc717a")+resultTotal.getFloat("mc717b");
                }
            }

            /***Calcul de Tchm & Bh par region***/
            int nbreJr=0;
            requete="select distinct(date) from tableregistre where trim(cell_name)='"+cellule+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'";
            nbreJr=cn.getNbreLigneResultset(requete);
            int n=TabTchmCellule.length;
            double TchmBhcellule=0;BhCellule=-1;
            double som=0;
            for(int l=0;l<n; l++)
            {
                som=som+TabTchmCellule[l];
                if(TabTchmCellule[l]>TchmBhcellule)
                {
                    TchmBhcellule=TabTchmCellule[l];
                    BhCellule=TabBhCellule[l];
                }
            }
            if(nbreJr>0)
                som=som/(24*nbreJr);
            else som=0;
            som=(double)((int)(som*100))/100;
            TchmBhcellule=(double)((int)(TchmBhcellule*100))/100;
            if(BhCellule>=0)
            {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhcellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void CalCulBH(List<String> lstRegion)
    {
        for (int i = 0; i < 24; i++)
        {
            TabBhGlobal[i]=-1;
            TabTchmGlobal[i]=0;
        }
        try
        {
            int nbreJr=0;
            mapTraficBhRegion = new HashMap<String, Double>();
            mapBhRegion= new HashMap<String, Integer>();
            mapTrHoraireMoyenRegion= new HashMap<String, Double>();
            int nbreRegion=lstRegion.size();
            for (int i = 0; i<nbreRegion;i++)
            {
                String region=lstRegion.get(i);
                getBhParRegion(region);
            }
            /***Calcul de Tchm & gh global de tout le réseau sur la période ****/
            String requete="select distinct(date) from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"'";
            nbreJr=cn.getNbreLigneResultset(requete);
            int n=TabTchmGlobal.length;
            TchmBhGlobal=0; BhG=-1;
            double som=0;
            for(int i=0;i<n; i++)
            {
                som=som+TabTchmGlobal[i];
                if(TabTchmGlobal[i]>TchmBhGlobal)
                {
                    TchmBhGlobal=TabTchmGlobal[i];
                    BhG=TabBhGlobal[i];
                    System.out.println("Bh global:"+BhG);
                }
            }
            if(nbreJr>0)
                som=som/(24*nbreJr);
            else som=0;
            som=(double)((int)(som*100))/100;
            TchmBhGlobal=(double)((int)(TchmBhGlobal*100))/100;

            if(BhG>=0)
            {
                mapBhRegion.put("Global", BhG);
                mapTraficBhRegion.put("Global", TchmBhGlobal);
                mapTrHoraireMoyenRegion.put("Global", som);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculBhParCellule(List<String> lstcellule)
    {
        try
        {
            mapTraficBhCellule = new HashMap<String, Double>();
            mapBhCellule= new HashMap<String, Integer>();
            mapTrHoraireMoyenCellule= new HashMap<String, Double>();
            int nbreCellule=lstcellule.size();
            for (int i = 0; i<nbreCellule;i++)
            {
                String cellule=lstcellule.get(i);
                getBhParCellule(cellule);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void InsertTable(String region)
    {
        String requete="select * from table_regions where trim(regions)='"+region.trim()+"'";
        try
        {
            ResultSet resultSet = cn.getResultset(requete);
            if(resultSet.next())
            {
                requete = "insert into tablevaleurskpi(region,tchm,tchmbh,tchmhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,HoSucces,the_geom) values(";
                requete = requete + region + ",";
                requete = requete + tchm + ",";
                requete = requete + tchmbh + ",";
                requete = requete + tchhm + ",";
                requete = requete + cssr + ",";
                requete = requete + tchcr + ",";
                requete = requete + tchcrbh + ",";
                requete = requete + tchdr + ",";
                requete = requete + tchdrbh + ",";
                requete = requete + sdcchcrbh + ",";
                requete = requete + sdcchdrbh + ",";
                requete = requete + cdr + ",";
                requete = requete + cdrbh + ",";
                requete = requete + csr + ",";
                requete = requete + BhRegion + ",";
                requete = requete + HoSucces+",";
                requete = requete + resultSet.getObject("the_geom").toString();
                requete = requete + ")";
                System.out.println("valeur requete:" + requete);
                //cn.ExecuterRequete(requete);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculNSN.class.getName()).log(Level.SEVERE, null, ex);
        }

        requete="insert into tablevaleurskpi(region,tchm,tchmbh,tchmhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,HoSucces) values(";
        requete=requete+region+",";
        requete=requete+tchm+",";
        requete=requete+tchmbh+",";
        requete=requete+tchmhm+",";
        requete=requete+cssr+",";
        requete=requete+tchcr+",";
        requete=requete+tchcrbh+",";
        requete=requete+tchdr+",";
        requete=requete+tchdrbh+",";
        requete=requete+sdcchcrbh+",";
        requete=requete+sdcchdrbh+",";
        requete=requete+cdr+",";
        requete=requete+cdrbh+",";
        requete=requete+csr+",";
        requete=requete+BhRegion+",";
        requete=requete+HoSucces;
        requete=requete+")";
        System.out.println("valeur requete:"+requete);
        //cn.ExecuterRequete(requete);
    }

    private void InsertTableCellule(String cellule)
    {
        String requete="select * from table_bts where trim(cell_name)='"+cellule.trim()+"'";
        try
        {
            ResultSet resultSet = cn.getResultset(requete);
            if(resultSet.next())
            {
                requete = "insert into table_valeurs_bts(cell_name,tchm,tchmbh,tchmhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,HoSucces,the_geom) values(";
                requete = requete + cellule + ",";
                requete = requete + tchm + ",";
                requete = requete + tchmbh + ",";
                requete = requete + tchhm + ",";
                requete = requete + cssr + ",";
                requete = requete + tchcr + ",";
                requete = requete + tchcrbh + ",";
                requete = requete + tchdr + ",";
                requete = requete + tchdrbh + ",";
                requete = requete + sdcchcrbh + ",";
                requete = requete + sdcchdrbh + ",";
                requete = requete + cdr + ",";
                requete = requete + cdrbh + ",";
                requete = requete + csr + ",";
                requete = requete + BhRegion + ",";
                requete = requete + HoSucces+",";
                requete = requete + resultSet.getObject("the_geom").toString();
                requete = requete + ")";
                //cn.ExecuterRequete(requete);
                System.out.println("valeur requete:" + requete);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void CalculValeurGlobales()
    {
        if(DGtchm!=0)
        {
            Gtchm=NGtchm/DGtchm;
            Gtchm=(double)((int)(Gtchm*100))/100;
        }
        else Gtchm=0;tchm=Gtchm;

        if(DGcdr!=0)
        {
            Gcdr=NGcdr/DGcdr;
            Gcdr=(double)((int)(Gcdr*100))/100;
        }
        else Gcdr=0;cdr=Gcdr;

        if(DGtchdr!=0)
        {
            Gtchdr=NGtchdr/DGtchdr;
            Gtchdr=(double)((int)(Gtchdr*100))/100;
        }
        else Gtchdr=0;tchdr=Gtchdr;

        if(DGtchcr!=0)
        {
            Gtchcr=NGtchcr/DGtchcr;
            Gtchcr=(double)((int)(Gtchcr*100))/100;
        }
        else Gtchcr=0;tchcr=Gtchcr;

        if(DGcssr!=0)
        {
            Gcssr=NGcssr/DGcssr;
            Gcssr=(double)((int)(Gcssr*100))/100;
        }
        else Gcssr=0;cssr=Gcssr;

        if(DGsdcchcrbh!=0)
        {
            Gsdcchcrbh=NGsdcchcrbh/DGsdcchcrbh;
            Gsdcchcrbh=(double)((int)(Gsdcchcrbh*100))/100;
        }
        else Gsdcchcrbh=0;sdcchcrbh=Gsdcchcrbh;

        if(DGsdcchdrbh!=0)
        {
            Gsdcchdrbh=NGsdcchdrbh/DGsdcchdrbh;
            Gsdcchdrbh=(double)((int)(Gsdcchdrbh*100))/100;
        }
        else Gsdcchdrbh=0;sdcchdrbh=Gsdcchdrbh;

        if(DGcsr!=0)
        {
            Gcsr=NGcsr/DGcsr;
            Gcsr=(double)((int)(Gcsr*100))/100;
        }
        else Gcsr=0;csr=Gcsr;

        if(DGHosucces!=0)
        {
            GHosucces=NGHosucces/DGHosucces;
            GHosucces=(double)((int)(GHosucces*100))/100;
        }
        else GHosucces=0;HoSucces=GHosucces;

        if(DGsmslr!=0)
        {
            Gsmslr=NGsmslr/DGsmslr;
            Gsmslr=(double)((int)(Gsmslr*100))/100;
        }
        else Gsmslr=0;smslr=Gsmslr;

        if(DGRsmsr!=0)
        {
            GRsmsr=NGRsmsr/DGRsmsr;
            GRsmsr=(double)((int)(GRsmsr*100))/100;
        }
        else GRsmsr=0;Rsmsr=GRsmsr;

        if(DGtchmbh!=0)
        {
            Gtchmbh=NGtchmbh/DGtchmbh;
            Gtchmbh=(double)((int)(Gtchmbh*100))/100;
        }
        else Gtchmbh=0;tchmbh=Gtchmbh;

        if(DGtchcrbh!=0)
        {
            Gtchcrbh=NGtchcrbh/DGtchcrbh;
            Gtchcrbh=(double)((int)(Gtchcrbh*100))/100;
        }
        else Gtchcrbh=0;tchcrbh=Gtchcrbh;

        if(DGtchdrbh!=0)
        {
            Gtchdrbh=NGtchdrbh/DGtchdrbh;
            Gtchdrbh=(double)((int)(Gtchdrbh*100))/100;
        }
        else Gtchdrbh=0;tchdrbh=Gtchdrbh;

        if(DGtchbrbh!=0)
        {
            Gtchbrbh=NGtchbrbh/DGtchbrbh;
            Gtchbrbh=(double)((int)(Gtchbrbh*100))/100;
        }
        else Gtchbrbh=0;tchbrbh=Gtchbrbh;

        if(DGcsrbh!=0)
        {
            Gcsrbh=NGcsrbh/DGcsrbh;
            Gcsrbh=(double)((int)(Gcsrbh*100))/100;
        }
        else Gcsrbh=0;csrbh=Gcsrbh;

        Object obj=mapTraficBhRegion.get("Global");
        Object obj1=mapTrHoraireMoyenRegion.get("Global");
        if(obj!=null && obj1!=null)
        {
            double val=Double.parseDouble(obj.toString());
            if(val>0)
                bhtr=(Double.parseDouble(obj.toString())/(24*val));
            else bhtr=0;
        }
    }
    
    @Override
    protected void finalize()
    {
        try
        {
            System.out.println("*************Desctruction de la classe Alcatel***************************");
            super.finalize();
            cn.closeConnection();
            System.out.println("*************Fin Desctruction de la classe Alcatel***************************");
        } catch (Throwable throwable)
        {
        }
    }

    public static void main(String arg[])
    {
        try
        {
            Operateur operateur = new Operateur();
            operateur.setBddOperateur("mtnbdd");
            operateur.setNomOperateur("MTN");
            CalculNSN ch = new CalculNSN(operateur, "2012-01-02", "2013-2-20");
            ch.CalculTotal();
        } catch (SQLException ex) {
            Logger.getLogger(CalculNSN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
