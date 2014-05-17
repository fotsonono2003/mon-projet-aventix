
package projet_consultation.Calcul.demoCalcul;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ClassesGenerales.Operateur;

public class CalculHuawei
{
    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double tchbr=0,tchbrbh=0;
    private double  tchm=0,Gtchm=0,NGtchm=0,DGtchm=0;
    private double  tchhm=0,Gtchhm=0,NGtchhm=0,DGtchhm=0;
    private double  tchmbh=0,Gtchmbh=0,NGtchmbh=0,DGtchmbh=0;
    private double [] TabTchmGlobal=new double[24];
    private double cssr=0,Gcssr=0,NGcssr=0,DGcssr=0;
    private double bhtr=0,Gbhtr=0,NGbhtr=0,DGbhtr=0;
    private double tchcr=0,Gtchcr=0,NGtchcr=0,DGtchcr=0;
    private double tchcrbh=0,Gtchcrbh=0,NGtchcrbh=0,DGtchcrbh=0;
    private double tchdr=0,Gtchdr=0,NGtchdr=0,DGtchdr=0;
    private double tchdrbh=0,GtchdrBh=0,NGtchdrBh=0,DGtchdrBh=0;
    private double sdcchbrbh=0,Gsdcchbrbh=0,NGsdcchbrbh=0,DGsdcchbrbh=0;
    private double sdcchcrbh=0,Gsdcchcrbh=0,NGsdcchcrbh=0,DGsdcchcrbh=0;
    private double sdcchdrbh=0,Gsdcchdrbh=0,NGsdcchdrbh=0,DGsdcchdrbh=0;
    private double csr=0,Gcsr=0,NGcsr=0,DGcsr=0;
    private double csrbh=0,Gcsrbh=0,NGcsrbh=0,DGcsrbh=0;
    private double cdr=0,Gcdr=0,NGcdr=0,DGcdr=0;
    private double cdrbh=0,Gcdrbh=0,NGcdrbh=0,DGcdrbh=0;
    private double HoSucces=0,GHosucces=0,NGHosucces=0,DGHosucces=0;
    private double TchmBhGlobal=0,TchmBhRegion=0;
    private int BhG=-1,BhRegion=-1,BhCellule=-1;

    private Map<String,Double> mapTraficBhRegion;
    private Map<String,Integer> mapBhRegion ;
    private Map<String,Double> mapTrHoraireMoyenRegion ;

    private Map<String,Double> mapTraficBhCellule=null ;
    private Map<String,Integer> mapBhCellule=null;
    private Map<String,Double> mapTrHoraireMoyenCellule=null;

    private int[] TabBhGlobal=new int[24];
    private int idinfo=0;
    
    public CalculHuawei(Operateur operateur,String datedebut,String datefin) throws SQLException
    {
        dateDebut=datedebut;
        dateFin=datefin;
        for (int i=0; i<24 ;i++)
        {
            TabBhGlobal[i]=-1;
            TabTchmGlobal[i]=0;
        }
        cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
    }

    public void calculTotal()
    {
        try
        {
            List<String>lstRegion=new ArrayList<String>();
            lstRegion.clear();
            String requete = "select distinct(region) from tableregistre";
            ResultSet result= cn.getResultset(requete);
            while (result.next())
            {
                lstRegion.add(result.getString("region").trim());
                System.out.println("Liste:"+lstRegion);
            }
            CalCulBH(lstRegion);

            result =cn.getResultset(requete);
            int nbreRegion=lstRegion.size();
            for (int i = 0; i <nbreRegion;i++)
            {
                String region=lstRegion.get(i);
                CalculRegion(region);
            }
            CalculValeurGlobales();
            if(BhG>=0)
                InsertTable("Global");
            /////////////////////////
            /////////Calcul par Cellule
            List<String> lstCellule = new ArrayList<String>();
            requete = "select distinct(cell_name) from tableregistre";
            result = cn.getResultset(requete);
            while (result.next())
            {
                lstCellule.add(result.getString("cell_name").trim());
                //System.out.println("Liste:" + lstCellule);
            }
            int nbreCellule=0;
            CalculBHParCellule(lstCellule);
            nbreCellule=lstCellule.size();
            for(int i=0;i<nbreCellule;i++)
            {
                CalculCellule(lstCellule.get(i));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculRegion(String region)
    {
        try
        {
            int NbreErgReel = 0,NbreErgRecus=0;
            String requete="select * from tableregistre where trim(region)='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'";
            ResultSet result =cn.getResultset(requete);
            double Ntchm=0,Dtchm=0;tchm=0;
            double Ntchmbh=0,Dtchmbh=0;tchmbh=0;
            double Ntchcr=0,Dtchcr=0;tchcr=0;
            double Ntchdr=0,Dtchdr=0;tchdr=0;
            double Ncssr=0,Dcssr=0;cssr=0;
            double Nsdcchbrbh=0,Dsdcchbrbh=0;sdcchbrbh=0;
            double Nsdcchcrbh=0,Dsdcchcrbh=0;sdcchcrbh=0;
            double Nsdcchdrbh=0,Dsdcchdrbh=0;sdcchdrbh=0;
            double Ncdr=0,Dcdr=0;cdr=0;
            double Ncdrbh=0,Dcdrbh=0;cdrbh=0;
            double Ncsr=0,Dcsr=0;csr=0;
            double Ncsrbh=0,Dcsrbh=0;csrbh=0;
            double NHoSucces=0,DHoSucces=0;HoSucces=0;
            
            BhRegion=mapBhRegion.get(region);

            while (result.next())
            {
                NbreErgReel++;
                Ntchm=Ntchm+(result.getFloat("cr3553")+result.getFloat("cr3554"));
                Dtchm=Dtchm+result.getFloat("r3590");

                Ncssr=Ncssr+result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c"));
                Dcssr=1;

                //Ntchdr=Ntchdr+1-Ncssr;
                Ntchdr=Ntchdr+1-result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c"));
                Dtchdr=1;

                Ntchcr=Ntchcr+(result.getDouble("k3021")+result.getDouble("k3011A")+result.getDouble("k3011b"))*100;
                Dtchcr=Dtchcr+result.getDouble("k3020")+result.getDouble("k3010a")+result.getDouble("k3010b");

                Nsdcchcrbh=Nsdcchcrbh+(result.getDouble("zk3001")*100);
                Dsdcchcrbh=Dsdcchcrbh+result.getDouble("zk3000");

                Nsdcchdrbh=Nsdcchdrbh+result.getDouble("zcm30")*100;
                Dsdcchdrbh=Dsdcchdrbh+result.getDouble("zk3003");

                Ncdr=Ncdr+result.getDouble("cm33")*100;
                Dcdr=Dcdr+result.getDouble("k3013a")+result.getDouble("ch323")+result.getDouble("ch343")-result.getDouble("ch313")-result.getDouble("ch333");

                double cdrCour=0,NcdrCour=0,DcdrCour=0;
                NcdrCour=result.getDouble("cm33")*100;
                DcdrCour=result.getDouble("k3013a")+result.getDouble("ch323")+result.getDouble("ch343")-result.getDouble("ch313")-result.getDouble("ch333");
                if(DcdrCour>0)
                    cdrCour=NcdrCour/DcdrCour;
                else cdrCour = 0;

                //Ncsr=Ncsr+Ncssr*(1-cdrCour);
                Ncsr=Ncsr+(result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c")))*(1-cdrCour);
                Dcsr=1;

                NHoSucces=(result.getDouble("CH313")+result.getDouble("CH333"))*100;
                DHoSucces=result.getDouble("ch310")+result.getDouble("ch330");

                String str=result.getString("heure").trim();
                int heure = Integer.parseInt(str.split(":")[0]);
                if(heure==BhRegion && heure>-1)
                {
                    Ntchmbh=Ntchmbh+(result.getFloat("cr3553")+result.getFloat("cr3554"));
                    Dtchmbh=Dtchm+result.getFloat("r3590");

                    Ncdrbh=Ncdr+result.getDouble("cm33")*100;
                    Dcdrbh=Dcdrbh+result.getDouble("k3013a")+result.getDouble("ch323")+result.getDouble("ch343")-result.getDouble("ch313")-result.getDouble("ch333");

                    double cssrCour=0;
                    cssrCour=Ncssr+result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c"));
                    Ncsrbh=Ncsrbh+cssrCour*(1-cdrCour);
                    Dcsrbh=1;
                }
            }
            /*taux d'information manquante pour cette region*/
            float TauxInfoManq=0;
            if(NbreErgReel>0)
            {
                TauxInfoManq=1-(float)((float)NbreErgRecus/(float)(24*NbreErgReel));
                TauxInfoManq=(float)((int)(TauxInfoManq*100))/100;
            }
            Map<String,Float>MapTauxInfoManqRegion=new HashMap<String, Float>();
            //////calcul du Taux d'information manquantes
            
            if(Dtchm!=0)
            {
                tchm=Ntchm/Dtchm;
                tchm=(double)((int)(tchm*100))/100;
                NGtchm = NGtchm + Ntchm;
                DGtchm = DGtchm + Dtchm;
                MapTauxInfoManqRegion.put("tchm", TauxInfoManq);
            }
            else 
            {
                tchm = 0;
                MapTauxInfoManqRegion.put("tchm", 100F);
            }
            
            if(Dcssr!=0)
            {
                cssr=Ncssr/Dcssr;
                cssr=(double)((int)(cssr*100))/100;
                if(cssr>=0 && cssr<=1)
                {
                    NGcssr = NGcssr + Ncssr;
                    DGcssr = 1;
                }else cssr=-1;
            }
            else cssr=-1;
            if(cssr==-1)
                MapTauxInfoManqRegion.put("cssr",100F);
            else
                MapTauxInfoManqRegion.put("cssr",TauxInfoManq);

            tchdr=1-cssr;
            if(tchdr>=0 && tchdr<=1)
            {
                NGtchdr = NGtchdr + Ntchdr;
                DGtchdr = 1;
            }else tchdr=2;
            if(tchdr==2)
                MapTauxInfoManqRegion.put("tchdr",100F);
            else
                MapTauxInfoManqRegion.put("tchdr",TauxInfoManq);

            if(Dtchcr!=0)
            {
                tchcr=Ntchcr/Dtchcr;
                tchcr=(double)((int)(tchcr*100))/100;
                if(tchcr>=0 && tchcr<=1)
                {
                    NGtchcr = NGtchcr + Ntchcr;
                    DGtchcr = DGtchcr + Dtchcr;
                }else tchcr=2;
            }
            else tchcr=2;
            if(tchcr==2)
                MapTauxInfoManqRegion.put("tchcr",100F);
            else
                MapTauxInfoManqRegion.put("tchcr",TauxInfoManq);

            if(Dsdcchcrbh!=0)
            {
                sdcchcrbh=Nsdcchcrbh/Dsdcchcrbh;
                sdcchcrbh=(double)((int)(sdcchcrbh*100))/100;
                if(sdcchcrbh>=0 && sdcchcrbh<=1)
                {
                    NGsdcchcrbh = NGsdcchcrbh + Nsdcchcrbh;
                    DGsdcchcrbh = DGsdcchcrbh + Dsdcchcrbh;
                }else sdcchcrbh=2;
            }
            else sdcchcrbh=2;
            if(sdcchcrbh==2)
                MapTauxInfoManqRegion.put("sdcchcrbh",100F);
            else
                MapTauxInfoManqRegion.put("sdcchcrbh",TauxInfoManq);

            if(Dsdcchdrbh!=0)
            {
                sdcchdrbh=Nsdcchdrbh/Dsdcchdrbh;
                sdcchdrbh=(double)((int)(sdcchdrbh*100))/100;
                if(sdcchdrbh>=0 && sdcchdrbh<=1)
                {
                    NGsdcchdrbh = NGsdcchdrbh + Nsdcchdrbh;
                    DGsdcchdrbh = DGsdcchdrbh + Dsdcchdrbh;
                }else sdcchdrbh=2;
            }
            else sdcchdrbh=2;
            if(sdcchdrbh==2)
                MapTauxInfoManqRegion.put("sdcchdrbh",100F);
            else
                MapTauxInfoManqRegion.put("sdcchdrbh",TauxInfoManq);

            if(Dcdr!=0)
            {
                cdr=Ncdr/Dcdr;
                cdr=(double)((int)(cdr*100))/100;
                if(cdr>=0 && cdr<=1)
                {
                    NGcdr = NGcdr + Ncdr;
                    DGcdr = DGcdr + Dcdr;
                }else cdr=2;
            }
            else cdr=2;
            if(cdr==2)
                MapTauxInfoManqRegion.put("cdr",100F);
            else
                MapTauxInfoManqRegion.put("cdr",TauxInfoManq);

            csr=cssr*(1-cdr);
            csr=(double)((int)(csr*100))/100;
            if(csr>=0 && csr<=1)
            {
                NGcsr = NGcsr + Ncsr;
                DGcsr = 1;
            }else csr=-1;
            if(csr==-1)
                MapTauxInfoManqRegion.put("csr",100F);
            else
                MapTauxInfoManqRegion.put("csr",TauxInfoManq);

            if(DHoSucces!=0)
            {
                HoSucces=NHoSucces/DHoSucces;
                HoSucces=(double)((int)(HoSucces*100))/100;
                if(HoSucces>=0 && HoSucces<=1)
                {
                    NGHosucces = NGHosucces + NHoSucces;
                    DGHosucces = DGHosucces + DHoSucces;
                }else HoSucces=-1;
            }
            else HoSucces=-1;
            if(HoSucces==-1)
                MapTauxInfoManqRegion.put("hosucces",100F);
            else
                MapTauxInfoManqRegion.put("hosucces",TauxInfoManq);

            if(Dtchmbh!=0)
            {
                tchmbh=Ntchmbh/Dtchmbh;
                tchmbh=(double)((int)(tchmbh*100))/100;
                NGtchmbh = NGtchmbh + Ntchmbh;
                DGtchmbh = DGtchm + Dtchmbh;
                MapTauxInfoManqRegion.put("tchmbh",TauxInfoManq);
            }
            else 
            {
                tchmbh = 0;
                MapTauxInfoManqRegion.put("tchmbh", 100F);
            }

            if(Dcdrbh!=0)
            {
                cdrbh=Ncdrbh/Dcdrbh;
                cdrbh=(double)((int)(cdrbh*100))/100;
                if(cdrbh>=0 && cdrbh<=1)
                {
                    NGcdrbh = NGcdrbh + Ncdrbh;
                    DGcdrbh = DGcdrbh + Dcdrbh;
                }else cdrbh=2;
            }
            else cdrbh=2;
            if(cdrbh==2)
                MapTauxInfoManqRegion.put("cdrbh",100F);
            else
                MapTauxInfoManqRegion.put("cdrbh",TauxInfoManq);

            if(Dcsrbh!=0)
            {
                csrbh=Ncsrbh/Dcsrbh;
                csrbh=(double)((int)(csrbh*100))/100;
                if(csrbh>=0 && csrbh<=1)
                {
                    NGcsrbh = NGcsrbh + Ncdrbh;
                    DGcsrbh = 1;
                }else csrbh=-1;
            }
            else csrbh=-1;
            if(csrbh==-1)
                MapTauxInfoManqRegion.put("csrbh",100F);
            else
                MapTauxInfoManqRegion.put("csrbh",TauxInfoManq);

            Object obj=mapBhRegion.get(region);
            Object obj1=mapTrHoraireMoyenRegion.get(region);
            Object obj2=mapTraficBhRegion.get(region);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                tchhm=mapTrHoraireMoyenRegion.get(region);
                tchmbh=mapTraficBhRegion.get(region);
                if(tchhm>0)
                {
                    int nbreHeure = cn.getNbreHeurePeriode(dateDebut, dateFin);
                    bhtr = tchmbh / (nbreHeure * tchhm);
                    if(bhtr<0 || bhtr>1)
                    {
                        bhtr=2;
                    }
                } else bhtr=2;
                System.out.println("Region:"+region);
                System.out.println("TCHMBH:"+tchmbh);
                System.out.println("TCHMHM:"+tchhm);
                bhtr=(double)((int)(bhtr*100))/100;
                if (bhtr ==2) {
                    MapTauxInfoManqRegion.put("bhtr", 100F);
                } else {
                    MapTauxInfoManqRegion.put("bhtr", TauxInfoManq);
                }
                idinfo++;
                String requeteInsert="insert into tauxinfo(idinfo,";
                String requeteValues=") values("+idinfo+",";
                for (Entry<String,Float> entry : MapTauxInfoManqRegion.entrySet())
                {
                    String cle = entry.getKey();
                    float valeur = entry.getValue();

                    requeteInsert=requeteInsert+cle+",";
                    requeteValues=requeteValues+""+valeur+",";
                }
                requeteInsert = requeteInsert.substring(0, requeteInsert.length() - 1);
                requeteValues = requeteValues.substring(0, requeteValues.length() - 1) + ")";
                requete = requeteInsert + requeteValues;
                //cn.ExecuterRequete(requete); //permet d'inserer des ligne dans la table tauxinfo
                InsertTable(region);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculCellule(String cellule)
    {
        try
        {
            String requete="select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'";
            ResultSet result =cn.getResultset(requete);
            double Ntchm=0,Dtchm=0;tchm=0;
            double Ntchmbh=0,Dtchmbh=0;tchmbh=0;
            double Ntchcr=0,Dtchcr=0;tchcr=0;
            double Ntchdr=0,Dtchdr=0;tchdr=0;
            double Ncssr=0,Dcssr=0;cssr=0;
            double Nsdcchbrbh=0,Dsdcchbrbh=0;sdcchbrbh=0;
            double Nsdcchcrbh=0,Dsdcchcrbh=0;sdcchcrbh=0;
            double Nsdcchdrbh=0,Dsdcchdrbh=0;sdcchdrbh=0;
            double Ncdr=0,Dcdr=0;cdr=0;
            double Ncdrbh=0,Dcdrbh=0;cdrbh=0;
            double Ncsr=0,Dcsr=0;csr=0;
            double Ncsrbh=0,Dcsrbh=0;csrbh=0;
            double NHoSucces=0,DHoSucces=0;HoSucces=0;
            BhCellule=mapBhCellule.get(cellule);
            
            while (result.next())
            {
                Ntchm=Ntchm+(result.getFloat("cr3553")+result.getFloat("cr3554"));
                Dtchm=Dtchm+result.getFloat("r3590");

                Ncssr=Ncssr+result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c"));
                Dcssr=1;

                //Ntchdr=Ntchdr+1-Ncssr;
                Ntchdr=Ntchdr+1-result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c"));
                Dtchdr=1;

                Ntchcr=Ntchcr+(result.getDouble("k3021")+result.getDouble("k3011A")+result.getDouble("k3011b"))*100;
                Dtchcr=Dtchcr+result.getDouble("k3020")+result.getDouble("k3010a")+result.getDouble("k3010b");

                Nsdcchcrbh=Nsdcchcrbh+(result.getDouble("zk3001")*100);
                Dsdcchcrbh=Dsdcchcrbh+result.getDouble("zk3000");

                Nsdcchdrbh=Nsdcchdrbh+result.getDouble("zcm30")*100;
                Dsdcchdrbh=Dsdcchdrbh+result.getDouble("zk3003");

                Ncdr=Ncdr+result.getDouble("cm33")*100;
                Dcdr=Dcdr+result.getDouble("k3013a")+result.getDouble("ch323")+result.getDouble("ch343")-result.getDouble("ch313")-result.getDouble("ch333");

                double cdrCour=0,NcdrCour=0,DcdrCour=0;
                NcdrCour=result.getDouble("cm33")*100;
                DcdrCour=result.getDouble("k3013a")+result.getDouble("ch323")+result.getDouble("ch343")-result.getDouble("ch313")-result.getDouble("ch333");
                if(DcdrCour>0)
                    cdrCour=NcdrCour/DcdrCour;
                else cdrCour = 0;

                //Ncsr=Ncsr+Ncssr*(1-cdrCour);
                Ncsr=Ncsr+(result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c")))*(1-cdrCour);
                Dcsr=1;

                NHoSucces=(result.getDouble("CH313")+result.getDouble("CH333"))*100;
                DHoSucces=result.getDouble("ch310")+result.getDouble("ch330");

                String str=result.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex){ heure=-1; }
                if(heure==BhCellule && heure>=0)
                {
                    Ntchmbh=Ntchmbh+(result.getFloat("cr3553")+result.getFloat("cr3554"));
                    Dtchmbh=Dtchm+result.getFloat("r3590");

                    Ncdrbh=Ncdr+result.getDouble("cm33")*100;
                    Dcdrbh=Dcdrbh+result.getDouble("k3013a")+result.getDouble("ch323")+result.getDouble("ch343")-result.getDouble("ch313")-result.getDouble("ch333");

                    double cssrCour=0;
                    cssrCour=Ncssr+result.getFloat("cr4110")*result.getFloat("cr4119")*(1-result.getFloat("cm30c"));
                    Ncsrbh=Ncsrbh+cssrCour*(1-cdrCour);
                    Dcsrbh=1;
                }
            }

            if(Dtchm!=0)
            {
                tchm=Ntchm/Dtchm;
                tchm=(double)((int)(tchm*100))/100;
                NGtchm = NGtchm + Ntchm;
                DGtchm = DGtchm + Dtchm;
            }
            else tchm = 0;

            if(Dcssr!=0)
            {
                cssr=Ncssr/Dcssr;
                cssr=(double)((int)(cssr*100))/100;
                if(cssr>=0 && cssr<=1)
                {
                    NGcssr = NGcssr + Ncssr;
                    DGcssr = 1;
                }else cssr=-1;
            }
            else cssr=-1;

            tchdr=1-cssr;
            if(tchdr>=0 && tchdr<=1)
            {
                NGtchdr = NGtchdr + Ntchdr;
                DGtchdr = 1;
            }else tchdr=2;

            if(Dtchcr!=0)
            {
                tchcr=Ntchcr/Dtchcr;
                tchcr=(double)((int)(tchcr*100))/100;
                if(tchcr>=0 && tchcr<=1)
                {
                    NGtchcr = NGtchcr + Ntchcr;
                    DGtchcr = DGtchcr + Dtchcr;
                }else tchcr=2;
            }
            else tchcr=2;

            if(Dsdcchcrbh!=0)
            {
                sdcchcrbh=Nsdcchcrbh/Dsdcchcrbh;
                sdcchcrbh=(double)((int)(sdcchcrbh*100))/100;
                if(sdcchcrbh>=0 && sdcchcrbh<=1)
                {
                    NGsdcchcrbh = NGsdcchcrbh + Nsdcchcrbh;
                    DGsdcchcrbh = DGsdcchcrbh + Dsdcchcrbh;
                }else sdcchcrbh=2;
            }
            else sdcchcrbh=2;

            if(Dsdcchdrbh!=0)
            {
                sdcchdrbh=Nsdcchdrbh/Dsdcchdrbh;
                sdcchdrbh=(double)((int)(sdcchdrbh*100))/100;
                if(sdcchdrbh>=0 && sdcchdrbh<=1)
                {
                    NGsdcchdrbh = NGsdcchdrbh + Nsdcchdrbh;
                    DGsdcchdrbh = DGsdcchdrbh + Dsdcchdrbh;
                }else sdcchdrbh=2;
            }
            else sdcchdrbh=2;

            if(Dcdr!=0)
            {
                cdr=Ncdr/Dcdr;
                cdr=(double)((int)(cdr*100))/100;
                if(cdr>=0 && cdr<=1)
                {
                    NGcdr = NGcdr + Ncdr;
                    DGcdr = DGcdr + Dcdr;
                }else cdr=2;
            }
            else cdr=2;

            csr=cssr*(1-cdr);
            csr=(double)((int)(csr*100))/100;
            if(csr>=0 && csr<=1)
            {
                NGcsr = NGcsr + Ncsr;
                DGcsr = 1;
            }else csr=-1;

            if(DHoSucces!=0)
            {
                HoSucces=NHoSucces/DHoSucces;
                HoSucces=(double)((int)(HoSucces*100))/100;
                if(HoSucces>=0 && HoSucces<=1)
                {
                    NGHosucces = NGHosucces + NHoSucces;
                    DGHosucces = DGHosucces + DHoSucces;
                }else HoSucces=-1;
            }
            else HoSucces=-1;

            if(Dtchmbh!=0)
            {
                tchmbh=Ntchmbh/Dtchmbh;
                tchmbh=(double)((int)(tchmbh*100))/100;
                NGtchmbh = NGtchmbh + Ntchmbh;
                DGtchmbh = DGtchm + Dtchmbh;
            }
            else
            {
                tchmbh = 0;
            }

            if(Dcdrbh!=0)
            {
                cdrbh=Ncdrbh/Dcdrbh;
                cdrbh=(double)((int)(cdrbh*100))/100;
                if(cdrbh>=0 && cdrbh<=1)
                {
                    NGcdrbh = NGcdrbh + Ncdrbh;
                    DGcdrbh = DGcdrbh + Dcdrbh;
                }else cdrbh=2;
            }
            else cdrbh=2;

            if(Dcsrbh!=0)
            {
                csrbh=Ncsrbh/Dcsrbh;
                csrbh=(double)((int)(csrbh*100))/100;
                if(csrbh>=0 && csrbh<=1)
                {
                    NGcsrbh = NGcsrbh + Ncdrbh;
                    DGcsrbh = 1;
                }else csrbh=-1;
            }
            else csrbh=-1;

            Object obj=mapBhCellule.get(cellule);
            Object obj1=mapTrHoraireMoyenCellule.get(cellule);
            Object obj2=mapTraficBhCellule.get(cellule);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                tchhm=mapTrHoraireMoyenCellule.get(cellule);
                tchmbh=mapTraficBhCellule.get(cellule);
                if(tchhm>0)
                {
                    int nbreHeure = cn.getNbreHeurePeriode(dateDebut, dateFin);
                    bhtr = tchmbh / (nbreHeure * tchhm);
                } else bhtr=2;
                System.out.println("Cellule:"+cellule);
                System.out.println("TCHMBH:"+tchmbh);
                System.out.println("TCHMHM:"+tchhm);
                bhtr=(double)((int)(bhtr*100))/100;
                InsertTableCellule(cellule);
            }
        }
        catch (SQLException sQLException)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, sQLException);
        }
    }

    public void CalculValeurGlobales()
    {
        if(DGtchm!=0)
        {
            Gtchm=NGtchm/DGtchm;
            Gtchm=(double)((int)(Gtchm*100))/100;
        }
        else Gtchm=0;
        tchm=Gtchm;

        if(DGcdr!=0)
        {
            Gcdr=NGcdr/DGcdr;
            Gcdr=(double)((int)(Gcdr*100))/100;
        }
        else Gcdr=0;
        cdr=Gcdr;

        if(DGtchdr!=0)
        {
            Gtchdr=NGtchdr/DGtchdr;
            Gtchdr=(double)((int)(Gtchdr*100))/100;
        }
        else Gtchdr=0;
        tchdr=Gtchdr;

        if(DGtchcr!=0)
        {
            Gtchcr=NGtchcr/DGtchcr;
            Gtchcr=(double)((int)(Gtchcr*100))/100;
        }
        else Gtchcr=0;
        tchcr=Gtchcr;

        if(DGcssr!=0)
        {
            Gcssr=NGcssr/DGcssr;
            Gcssr=(double)((int)(Gcssr*100))/100;
        }
        else Gcssr=0;
        cssr=Gcssr;

        if(DGsdcchcrbh!=0)
        {
            Gsdcchcrbh=NGsdcchcrbh/DGsdcchcrbh;
            Gsdcchcrbh=(double)((int)(Gsdcchcrbh*100))/100;
        }
        else Gsdcchcrbh=0;
        sdcchcrbh=Gsdcchcrbh;

        if(DGsdcchdrbh!=0)
        {
            Gsdcchdrbh=NGsdcchdrbh/DGsdcchdrbh;
            Gsdcchdrbh=(double)((int)(Gsdcchdrbh*100))/100;
        }
        else Gsdcchdrbh=0;

        sdcchdrbh=Gsdcchdrbh;
        if(DGcsr!=0)
        {
            Gcsr=NGcsr/DGcsr;
            Gcsr=(double)((int)(Gcsr*100))/100;
        }
        else Gcsr=0;
        csr=Gcsr;

        if(DGHosucces!=0)
        {
            GHosucces=NGHosucces/DGHosucces;
            GHosucces=(double)((int)(GHosucces*100))/100;
        }
        else GHosucces=0;
        HoSucces=GHosucces;

        if(DGtchmbh!=0)
        {
            Gtchmbh=NGtchmbh/DGtchmbh;
            Gtchmbh=(double)((int)(Gtchmbh*100))/100;
        }
        else Gtchmbh=0;
        tchmbh=Gtchmbh;

        if(DGcdrbh!=0)
        {
            Gcdrbh=NGcdrbh/DGcdrbh;
            Gcdrbh=(double)((int)(Gcdrbh*100))/100;
        }
        else Gcdrbh=0;
        cdrbh=Gcdrbh;

        if(DGcsrbh!=0)
        {
            Gcsrbh=NGcsrbh/DGcsrbh;
            Gcsrbh=(double)((int)(Gcsrbh*100))/100;
        }
        else Gcsrbh=0;
        csrbh=Gcsrbh;

        Object obj=mapTraficBhRegion.get("Global");
        Object obj1=mapTrHoraireMoyenRegion.get("Global");
        if(obj!=null && obj1!=null)
        {
            double val=Double.parseDouble(obj.toString());
            if(val>0)
                bhtr=(Double.parseDouble(obj.toString())/(24*val));
            else bhtr=0;
            bhtr=(double)((int)(bhtr*100))/100;
        }
    }

    private void getBhParRegion(String region)
    {
        int [] TabBhRegion=new int[24];
        double [] TabTchmRegion=new double[24];
        for (int i = 0; i < 24; i++)
        {
            TabBhRegion[i]=-1;
            TabTchmRegion[i]=0;
        }
        try
        {
            int nbreErg=0;
            String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            ResultSet resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                nbreErg++;
                double val=0;
                String str=resultTotal.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) { heure=-1; }
                
                val=resultTotal.getFloat("r3590");
                if(val>0 && heure>=0)
                {
                    TabBhRegion[heure-1] = heure;
                    TabTchmRegion[heure-1] = TabTchmRegion[heure-1] + ((resultTotal.getFloat("cr3553") + resultTotal.getFloat("cr3554"))/val);

                    TabBhGlobal[heure-1] = heure;
                    TabTchmGlobal[heure-1] = TabTchmGlobal[heure-1] + ((resultTotal.getFloat("cr3553") + resultTotal.getFloat("cr3554")) /val);
                }
            }
            
            /***Calcul de Tchm & Bh par region***/
            int n=TabTchmRegion.length;
            TchmBhRegion=0;BhRegion=-1;
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
            if(nbreErg>0)
                som=som/(double)(nbreErg);
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
            mapTraficBhRegion = new HashMap<String,Double>();
            mapBhRegion= new HashMap<String, Integer>();
            mapTrHoraireMoyenRegion= new HashMap<String, Double>();
            int nbreRegion=lstRegion.size();
            for (int i = 0; i<nbreRegion;i++)
            {
                String region=lstRegion.get(i);
                getBhParRegion(region);
            }
            /***Calcul de Tchm & Bh global de tout le réseau sur la période ****/
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
                }
            }
            if(nbreJr>0)
                som=som/(24*nbreJr);
            else som=0;
            som=(double)((int)(som*100))/100;

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

    private void CalculBHParCellule(List<String> lstCellules)
    {
        try
        {
            mapTraficBhCellule = new HashMap<String,Double>();
            mapBhCellule= new HashMap<String, Integer>();
            mapTrHoraireMoyenCellule= new HashMap<String, Double>();

            int nbreRegion=lstCellules.size();
            for (int i = 0; i<nbreRegion;i++)
            {
                String cellule=lstCellules.get(i);
                ////calcul de la Busy Hour par région
                getBHParCellule(cellule);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void InsertTable(String region)
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
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }

        requete="insert into tablevaleurskpi(region,tchm,tchmbh,tchhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,HoSucces,bhtr) values(";
        requete=requete+region+",";
        requete=requete+tchm+",";
        requete=requete+tchmbh+",";
        requete=requete+tchhm+",";
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
        requete=requete+HoSucces+",";
        requete=requete+bhtr;
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

    private void getBHParCellule(String cellule)
    {
        int TabBhCellule[]=new int[24];
        double TabTchmCellule[]=new double[24];
        for (int i = 0; i < 24; i++)
        {
            TabBhCellule[i]=-1;
            TabTchmCellule[i]=0;
        }
        try
        {
            int nbreErg=0;
            String requete = "select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            ResultSet resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                nbreErg++;
                double val1=0,val2=0;
                int heure=-1;
                String str=resultTotal.getString("heure").trim();
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {heure=-1;}
                try
                {
                    val1= resultTotal.getFloat("r3590");
                } catch (Exception ex){val2=0;}

                if(val1>0 && heure>=0)
                {
                    System.out.println("heure cellule:"+heure);
                    System.out.println("TabBhCellule[heure]:"+TabBhCellule[heure]);
                    TabBhCellule[heure-1]=heure;
                    TabTchmCellule[heure-1] = TabTchmCellule[heure-1] + ((resultTotal.getFloat("cr3553") + resultTotal.getFloat("cr3554"))/val1);
                }
            }

            /***Calcul de Tchm & Bh par cellule ***/
            int n=TabTchmCellule.length;
            double TchmBhCellule=0;BhCellule=-1;
            double som=0;
            for(int i=0;i<n; i++)
            {
                som=som+TabTchmCellule[i];
                if(TabTchmCellule[i]>TchmBhCellule)
                {
                    TchmBhCellule=TabTchmCellule[i];
                    BhCellule=TabBhCellule[i];
                }
            }
            if(nbreErg>0)
                som=som/(double)(nbreErg);
            else som=0;
            som=(double)((int)(som*100))/100;
            TchmBhCellule=(double)((int)(TchmBhCellule*100))/100;
            if(BhCellule>=0)
            {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhCellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("******Destruction de la classe CalculHuawei***********");
        super.finalize();
        cn.closeConnection();
        System.out.println("******Fin de la destruction de la classe CalculHuawei***********");
    }

    public static void main(String arg[])
    {
        try {
            Operateur operateur = new Operateur();
            operateur.setBddOperateur("mtnbdd");
            operateur.setNomOperateur("MTN");
            CalculHuawei ch = new CalculHuawei(operateur, "2012-01-02", "2013-2-20");
            ch.calculTotal();
        } catch (SQLException ex) {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
