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
 
public class CalCulEricsson
{
    private final ConnexionBDDOperateur cn;
    private final String dateDebut;
    private final String dateFin;
    private double  tchhm=0,Gtchhm=0,NGtchhm=0,DGtchhm=0;
    private double  tchm=0,Gtchm=0,NGtchm=0,DGtchm=0;
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
    private double TchmBhGlobal=0,TchmBhRegion=0;
    private int BhG=-1,BhRegion=-1,BhCellule=-1;
    private int[] TabBhGlobal=new int[24];
    private double tchbr=0,Gtchbr=0,NGtchbr=0,DGtchbr=0;
    private double tchbrbh=0,Gtchbrbh=0,NGtchbrbh=0,DGtchbrbh=0;
    private double Rsmsr=0,GRsmsr=0,NGRsmsr=0,DGRsmsr=0;

    private Map<String,Double> mapTraficBhRegion=null;
    private Map<String,Integer> mapBhRegion =null;
    private Map<String,Double> mapTrHoraireMoyenRegion=null ;

    private Map<String,Double> mapTraficBhCellule=null ;
    private Map<String,Integer> mapBhCellule=null;
    private Map<String,Double> mapTrHoraireMoyenCellule=null;
    private int idinfo=0;

    //private Map<String,Float>MapTauxInfoManqCellule=null;
    //private Map<String,Float>MapTauxInfoManqRegion=null;
    
    
    public CalCulEricsson(Operateur operateur,String datedebut,String datefin) throws SQLException
    {
        dateDebut=datedebut;
        dateFin=datefin;
        cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
    }

    private void CalculRegion(String region)
    {
        try
        {
            ////calcul du nombre d'enregistrement reels
            int NbreErgReel = 0,NbreErgRecus=0;
            String requete="select count(cell_name)*(SELECT DATE_PART('day', '"+dateFin+" 01:00:00'::timestamp - '"+dateDebut+" 00:00:00'::timestamp)+1) as nbre from table_bts "
                    + "where region='"+region+"' group by region";
            ResultSet resultSet=cn.getResultset(requete);
            if(resultSet.next())
            {
                NbreErgReel=resultSet.getInt("nbre");
            }
            
            /////////////////
            requete ="select * from tableregistre where trim(region)='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"' ";
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
            double Hoversuc=0,Hovercnt=0;HoSucces=0;
            float NRsmsr=0,DRsmsr=0;Rsmsr=0;

            ResultSet result=null;
            if (mapBhRegion.get(region)!=null)
            {
                BhRegion = mapBhRegion.get(region);
            }else BhRegion=-1;
            result = cn.getResultset(requete);
            while (result.next())
            {
                NbreErgRecus++;
                double val1=0,val2=0,cssrCour=0;
                val1=result.getFloat("tfnscan");
                val2=result.getFloat("thnscan");
                if(val1>0 && val2>0)
                {
                    Ntchm=Ntchm+(result.getFloat("tftralacc")/val1)+(result.getFloat("thtralacc")/val2);
                    Dtchm=1;
                }

                val1=result.getFloat("cmsestab");val2=result.getFloat("tassall");
                if(val1>0 && val2>0)
                {
                    cssrCour=100*(1-(result.getFloat("cndrop")-result.getFloat("cnrelcong"))/val1)*(1-(result.getFloat("tassall")-result.getFloat("tcassall"))/val2);
                    Ncssr=Ncssr+cssrCour;
                    Dcssr=1;
                }

                Ntchcr=Ntchcr+100*result.getFloat("cnrelcong");
                Dtchcr=Dtchcr+result.getFloat("tassall");

                Ntchbr=Ntchcr;
                Dtchbr=1;

                //**//
                Ntchdr=Ntchdr+100*result.getFloat("tfndrop");
                Dtchdr=Dtchdr+result.getFloat("tcassall");

                Nsdcchcrbh=Nsdcchcrbh+100*result.getFloat("ccongs");
                Dsdcchcrbh=Dsdcchcrbh+result.getFloat("ccalls");

                Nsdcchdrbh=Nsdcchdrbh+100*result.getFloat("cndrop");
                Dsdcchdrbh=Dsdcchdrbh+result.getFloat("cmsestab");

                Ncsr=Ncsr+Ncssr*(1-Ncdr);
                Dcsr=1;

                Hoversuc=Hoversuc+result.getFloat("hoversuc");
                Hovercnt=Hovercnt+result.getFloat("hovercnt");

                //NRsmsr=NRsmsr+result.getFloat("csmsdwn")+result.getFloat("tsmsdwn");
                DRsmsr=1;
                String str=result.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                }
                catch (Exception ex)
                {
                    heure=-1;
                }
                if(heure==BhRegion && heure>=0)
                {
                    val1=result.getFloat("tfnscan");
                    val2=result.getFloat("thnscan");
                    if(val1>0 && val2>0)
                    {
                        Ntchmbh=Ntchmbh+(result.getFloat("tftralacc")/val1)+(result.getFloat("thtralacc")/val2);
                    }
                    Dtchmbh=1;
                    Ntchcrbh=Ntchcrbh+100*result.getFloat("cnrelcong");
                    Dtchcrbh=Dtchcrbh+result.getFloat("tassall");

                    Ntchdrbh=Ntchdrbh+100*result.getFloat("tfndrop");
                    Dtchdrbh=Dtchdrbh+result.getFloat("tcassall");

                    Ntchbrbh=Ntchcrbh;
                    Dtchbrbh=1;

                    val1=result.getFloat("cmsestab");
                    val2=result.getFloat("tassall");
                    if(val1>0 && val2>0)
                        Ncsrbh=Ncsrbh+(100*(1-(result.getFloat("cndrop")-result.getFloat("cnrelcong"))/val1)*(1-(result.getFloat("tassall")-result.getFloat("tcassall"))/val2))*(1-Ncdrbh);
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
                MapTauxInfoManqRegion.put("tchm",100F);
            }

            if(Dcssr!=0)
            {
                cssr=Ncssr/Dcssr;
                cssr=(double)((int)(cssr*100))/100;
                if(cssr>=0 && cssr<=1)
                {
                    NGcssr = NGcssr + Ncssr;
                    DGcssr = 1;
                }else cssr = -1;
            }
            else cssr = -1;
            if(cssr==-1)
                MapTauxInfoManqRegion.put("cssr",100F);
            else
                MapTauxInfoManqRegion.put("cssr",TauxInfoManq);

            if(Dtchcr!=0)
            {
                tchcr=Ntchcr/Dtchcr;
                tchcr=(double)((int)(tchcr*100))/100;
                if(tchcr>=0 && tchcr<=1)
                {
                    NGtchcr = NGtchcr + Ntchcr;
                    DGtchcr = DGtchcr + Dtchcr;

                    tchbr = tchcr;
                    NGtchbr = NGtchcr;
                    DGtchbr = 1;
                }else 
                {
                    tchcr = 2;
                    tchbr=2;
                }
            }
            else
            {
                tchbr=2;
                tchcr = 2;
            }
            if(tchbr==2)
            {
                MapTauxInfoManqRegion.put("tchbr",100F);
                MapTauxInfoManqRegion.put("tchcr",100F);
            }else
            {
                MapTauxInfoManqRegion.put("tchbr",TauxInfoManq);
                MapTauxInfoManqRegion.put("tchcr" ,TauxInfoManq);
            }

            if(Dtchdr!=0)
            {
                tchdr=Ntchdr/Dtchdr;
                tchdr=(double)((int)(tchdr*100))/100;
                if(tchdr>=0 && tchdr<=1)
                {
                    NGtchdr = NGtchdr + Ntchdr;
                    DGtchdr = DGtchdr + Dtchdr;
                }else tchdr=2;
            }
            else tchdr=2;
            if(tchdr==2)
            {
                MapTauxInfoManqRegion.put("tchdr",100F);
            }else   MapTauxInfoManqRegion.put("tchdr",TauxInfoManq);

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
            else MapTauxInfoManqRegion.put("sdcchcrbh",TauxInfoManq);

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
            else    MapTauxInfoManqRegion.put("sdcchdrbh",TauxInfoManq);

            if(Dcsr!=0)
            {
                csr=Ncsr/Dcsr;
                csr=(double)((int)(csr*100))/100;
                if(csr>=0 && csr<=1)
                {
                    NGcsr = NGcsr + Ncsr;
                    DGcsr = 1;
                }else csr=-1;
            }
            else csr=-1;
            if(csr==-1)
                MapTauxInfoManqRegion.put("csr",100F);
            else    MapTauxInfoManqRegion.put("csr",TauxInfoManq);

            if(DRsmsr!=0)
            {
                Rsmsr=NRsmsr/DRsmsr;
                Rsmsr=(double)((int)(Rsmsr*100))/100;
                if(Rsmsr>=0 && Rsmsr<=1)
                {
                    NGRsmsr = NGsdcchcrbh + NRsmsr;
                    DGRsmsr = DGRsmsr + DRsmsr;
                }else Rsmsr=-1;
            }
            else Rsmsr=-1;
            if(Rsmsr==2)
                MapTauxInfoManqRegion.put("rsmsr",100F);
            else    MapTauxInfoManqRegion.put("rsmsr",TauxInfoManq);

            if(Hovercnt!=0)
            {
                HoSucces=Hoversuc/Hovercnt;
                HoSucces=(double)((int)(HoSucces*100))/100;
                if(HoSucces>=0 && HoSucces<=1)
                {
                    NGHosucces = NGHosucces + Hoversuc;
                    DGHosucces = DGHosucces+Hovercnt;
                }else if(HoSucces>1)
                {
                    HoSucces=1;
                    NGHosucces = NGHosucces + Hoversuc;
                    DGHosucces = DGHosucces+Hovercnt;
                }
                else HoSucces=-1;
            }
            else HoSucces=-1;
            if(HoSucces==-1)
                MapTauxInfoManqRegion.put("hosucces",100F);
            else    MapTauxInfoManqRegion.put("hosucces",TauxInfoManq);

            if(Dtchmbh!=0)
            {
                tchmbh=Ntchmbh/Dtchmbh;
                tchmbh=(double)((int)(tchmbh*100))/100;

                NGtchmbh = NGtchmbh + Ntchmbh;
                DGtchmbh = 1;
                MapTauxInfoManqRegion.put("tchmbh",TauxInfoManq);
            }
            else 
            {
                tchmbh = 0;
                MapTauxInfoManqRegion.put("tchmbh",100F);
            }

            if(Dtchcrbh!=0)
            {
                tchcrbh=Ntchcrbh/Dtchcrbh;
                tchcrbh=(double)((int)(tchcrbh*100))/100;
                if(tchcrbh>=0 && tchcrbh<=1)
                {
                    NGtchcrbh = NGtchcrbh + Ntchcrbh;
                    DGtchcrbh = DGtchbrbh + Dtchcrbh;
                }else tchcrbh=2;
            }
            else tchcrbh=2;
            if(tchcrbh==2)
                MapTauxInfoManqRegion.put("tchcrbh",100F);
            else    MapTauxInfoManqRegion.put("tchcrbh",TauxInfoManq);

            if(Dtchdrbh!=0)
            {
                tchdrbh=Ntchdrbh/Dtchdrbh;
                tchdrbh=(double)((int)(tchdrbh*100))/100;
                if(tchdrbh>=0 && tchdrbh<=1)
                {
                    NGtchdrbh = NGtchdrbh + Ntchdrbh;
                    DGHosucces = DGtchdrbh + Dtchdrbh;
                }else tchdrbh=2;
            }
            else tchdrbh=2;
            if(tchdrbh==2)
                MapTauxInfoManqRegion.put("tchdrbh",100F);
            else    MapTauxInfoManqRegion.put("tchdrbh",TauxInfoManq);

            if(Dtchbrbh!=0)
            {
                tchbrbh=Ntchbrbh/Dtchbrbh;
                tchbrbh=(double)((int)(tchbrbh*100))/100;
                if(tchbrbh>=0 && tchbrbh<=1)
                {
                    NGtchbrbh = NGtchbrbh + Ntchbrbh;
                    DGtchbrbh = 1;
                }else tchbrbh=2;
            }
            else tchbrbh=2;
            if(tchbrbh==2)
                MapTauxInfoManqRegion.put("tchbrbh",100F);
            else    MapTauxInfoManqRegion.put("tchbrbh",TauxInfoManq);

            if(Dcsrbh!=0)
            {
                csrbh=Ncsrbh/Dcsrbh;
                csrbh=(double)((int)(csrbh*100))/100;
                if(csrbh>=0 && csrbh>=1)
                {
                    NGcsrbh = NGcsrbh + Ncsrbh;
                    DGcsrbh = 1;
                }else csrbh=-1;
            }
            else csrbh=-1;
            if(csrbh==-1)
                MapTauxInfoManqRegion.put("csrbh",100F);
            else    MapTauxInfoManqRegion.put("csrbh",TauxInfoManq);
            
            Object obj=mapBhRegion.get(region);
            Object obj1=mapTrHoraireMoyenRegion.get(region);
            Object obj2=mapTraficBhRegion.get(region);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                tchhm=mapTrHoraireMoyenRegion.get(region);
                tchmbh=mapTraficBhRegion.get(region);
                if(tchhm>0)
                {
                    int nbreHeure=cn.getNbreHeurePeriode(dateDebut, dateFin);
                    bhtr=tchmbh/(nbreHeure*tchhm);
                } else bhtr=2;
                bhtr=(double)((int)(bhtr*100))/100;
                if (bhtr ==2) {
                    MapTauxInfoManqRegion.put("bhtr", 100F);
                } else {
                    MapTauxInfoManqRegion.put("bhtr", TauxInfoManq);
                }
                idinfo++;
                String requeteInsert="insert into tauxinfo(idinfo,region,";
                String requeteValues=") values("+idinfo+",'"+region+"',";
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
                cn.ExecuterRequete(requete); //permet d'inserer des ligne dans la table tauxinfo
                InsertTableRegion(region);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CalculTotal()
    {
        System.out.println("Debut calcul total Ericsson");
        ResultSet result=null;
        try
        {            
            List<String> lstRegion = new ArrayList<String>();
            String requete = "select distinct(region) from table_regions where region!='Nkayi District'";
            result = cn.getResultset(requete);
            while (result.next())
            {
                lstRegion.add(result.getString("region").trim());
                //System.out.println("Liste:" + lstRegion);
            }
            CalCulBHParRegion(lstRegion);
            int n=lstRegion.size();
            for(int i=0;i<n;i++)
            {
                CalculRegion(lstRegion.get(i));
            }
            CalculValeurGlobales();
            if(BhG>=0)
                InsertTableRegion("Global");
            
            /////////Calcul par Cellule
            List<String> lstCellule = new ArrayList<String>();
            requete = "select distinct(cell_name) from tableregistre";
            result = cn.getResultset(requete);
            while (result.next())
            {
                lstCellule.add(result.getString("cell_name").trim());
                //System.out.println("Liste:" + lstCellule);
            }
            CalculBHParCellule(lstCellule);
            int nbreCellule=lstCellule.size();
            for(int i=0;i<nbreCellule;i++)
            {
                CalculCellule(lstCellule.get(i));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                result.close();
            } catch (SQLException ex){}
        }
    }

    public void getBhParRegion(String region)
    {
        int [] TabBhRegion=new int[24];
        double [] TabTchmRegion=new double[24];
        for (int i = 0; i < 24; i++)
        {
            TabBhRegion[i]=-1;
            TabTchmRegion[i]=0;
        }
        ResultSet resultTotal=null;
        try
        {            
            int nbreErg=0;
            String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                nbreErg++;
                double val1=0,val2=0;
                String str=resultTotal.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex)
                {
                    heure=-1;
                }
                try
                {
                    val1 = resultTotal.getFloat("tfnscan");
                    val2 = resultTotal.getFloat("thnscan");
                } catch (Exception ex) {val1=0;val2=0; }

                if(val1>0 && val2>0 && heure>=0)
                {
                    TabBhRegion[heure-1]=heure;
                    TabTchmRegion[heure-1]=TabTchmRegion[heure-1]+(resultTotal.getFloat("tftralacc")/val1)+(resultTotal.getFloat("thtralacc")/val2);
                    
                    TabBhGlobal[heure-1]=heure;
                    TabTchmGlobal[heure-1]=TabTchmGlobal[heure-1]+(resultTotal.getFloat("tftralacc")/val1)+(resultTotal.getFloat("thtralacc")/val2);
                }
            }
            /***Calcul de Tchm & Bh par region ***/            
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
        finally
        {
            try
            {
                resultTotal.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void CalCulBHParRegion(List<String> lstRegion)
    {
        for (int i = 0; i < 24; i++)
        {
            TabBhGlobal[i]=-1;
            TabTchmGlobal[i]=0;
        }
        try
        {
            mapTraficBhRegion = new HashMap<String,Double>();
            mapBhRegion= new HashMap<String, Integer>();
            mapTrHoraireMoyenRegion= new HashMap<String, Double>();
            int nbreRegion=lstRegion.size();
            for (int i = 0; i<nbreRegion;i++)
            {
                String region=lstRegion.get(i);
                ////calcul de la Busy Hour par région
                getBhParRegion(region);
            }
            /***Calcul de Tchm & bh global de tout le réseau sur la période ****/
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
            String requete="select count(*) as nbre from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"'";
            int nbreErg=cn.getNbreLigneResultset(requete);
            if(nbreErg>0)
                som=som/(double)(nbreErg);
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

        if(DGtchbr!=0)
        {
            Gtchbr=NGtchbr/DGtchbr;
            Gtchbr=(double)((int)(Gtchbr*100))/100;
        }
        else Gtchbr=0;tchbr=Gtchbr;

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
            if(GHosucces>1)
            {
                GHosucces=1;
            }
        }
        else GHosucces=0;HoSucces=GHosucces;

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
            bhtr=(double)((int)(bhtr*100))/100;
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
    
    private void CalculCellule(String cellule)
    {
        try
        {
            String requete ="select * from tableregistre where trim(cell_name)='"+cellule+"' and date>'"+dateDebut+"' and date<='"+dateFin+"' ";
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
            double Hoversuc=0,Hovercnt=0;HoSucces=0;
            double NRsmsr=0,DRsmsr=0;Rsmsr=0;
            

            ResultSet result=null;
            if(mapBhCellule.get(cellule)!=null)
            {
                BhCellule=mapBhCellule.get(cellule);
            }else BhCellule=-1;
            result = cn.getResultset(requete);
            while (result.next())
            {
                double val1=0,val2=0,cssrCour=0;
                val1=result.getFloat("tfnscan");
                val2=result.getFloat("thnscan");
                if(val1>0 && val2>0)
                {
                    Ntchm=Ntchm+(result.getFloat("tftralacc")/val1)+(result.getFloat("thtralacc")/val2);
                    Dtchm=1;
                }

                val1=result.getFloat("cmsestab");val2=result.getFloat("tassall");
                if(val1>0 && val2>0)
                {
                    cssrCour=100*(1-(result.getFloat("cndrop")-result.getFloat("cnrelcong"))/val1)*(1-(result.getFloat("tassall")-result.getFloat("tcassall"))/val2);
                    Ncssr=Ncssr+cssrCour;
                    Dcssr=1;
                }

                Ntchcr=Ntchcr+100*result.getFloat("cnrelcong");
                Dtchcr=Dtchcr+result.getFloat("tassall");

                Ntchbr=Ntchcr;
                Dtchbr=1;

                Ntchdr=Ntchdr+100*result.getFloat("tfndrop");
                Dtchdr=Dtchdr+result.getFloat("tcassall");

                Nsdcchcrbh=Nsdcchcrbh+100*result.getFloat("ccongs");
                Dsdcchcrbh=Dsdcchcrbh+result.getFloat("ccalls");

                Nsdcchdrbh=Nsdcchdrbh+100*result.getFloat("cndrop");
                Dsdcchdrbh=Dsdcchdrbh+result.getFloat("cmsestab");

                Ncsr=Ncsr+Ncssr*(1-Ncdr);
                Dcsr=1;

                Hoversuc = Hoversuc+result.getFloat("hoversuc");
                Hovercnt = Hovercnt+result.getFloat("hovercnt");

                //NRsmsr=NRsmsr+result.getFloat("csmsdwn")+result.getFloat("tsmsdwn");
                //DRsmsr=1;
                String str=result.getString("heure").trim();
                int heure=-1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                }
                catch (Exception ex)
                {
                    heure=-1;
                }
                if(heure==BhCellule && heure>=0)
                {
                    val1=result.getFloat("tfnscan");
                    val2=result.getFloat("thnscan");
                    if(val1>0 && val2>0)
                    {
                        Ntchmbh=Ntchmbh+(result.getFloat("tftralacc")/val1)+(result.getFloat("thtralacc")/val2);
                    }
                    Dtchmbh=1;
                    Ntchcrbh=Ntchcrbh+100*result.getFloat("cnrelcong");
                    Dtchcrbh=Dtchcrbh+result.getFloat("tassall");

                    Ntchdrbh=Ntchdrbh+100*result.getFloat("tfndrop");
                    Dtchdrbh=Dtchdrbh+result.getFloat("tcassall");

                    Ntchbrbh=Ntchcrbh;
                    Dtchbrbh=1;

                    val1=result.getFloat("cmsestab");
                    val2=result.getFloat("tassall");
                    if(val1>0 && val2>0)
                        Ncsrbh=Ncsrbh+(100*(1-(result.getFloat("cndrop")-result.getFloat("cnrelcong"))/val1)*(1-(result.getFloat("tassall")-result.getFloat("tcassall"))/val2))*(1-Ncdrbh);
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
            else tchm=0;

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

            if(Dtchcr!=0)
            {
                tchcr=Ntchcr/Dtchcr;
                tchcr=(double)((int)(tchcr*100))/100;
                if(tchcr>=0 &&  tchcr<=1)
                {
                    NGtchcr = NGtchcr + Ntchcr;
                    DGtchcr = DGtchcr + Dtchcr;

                    tchbr = tchcr;
                    NGtchbr = NGtchcr;
                    DGtchbr = 1;
                }else 
                {
                    tchcr = 2;
                    tchbr=2;
                }
            }
            else tchcr=2;

            if(Dtchdr!=0)
            {
                tchdr=Ntchdr/Dtchdr;
                tchdr = (double) ((int) (tchdr * 100)) / 100;
                if(tchdr>=0 && tchdr<=1)
                {
                    NGtchdr = NGtchdr + Ntchdr;
                    DGtchdr = DGtchdr + Dtchdr;
                }else tchdr=2;
            }
            else tchdr=2;

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

            if(Dcsr!=0)
            {
                csr=Ncsr/Dcsr;
                csr=(double)((int)(csr*100))/100;
                if(csr>=0 && csr<=1)
                {
                    NGcsr = NGcsr + Ncsr;
                    DGcsr = 1;
                }else csr=-1;
            }
            else csr=-1;

            if(DRsmsr!=0)
            {
                Rsmsr=NRsmsr/DRsmsr;
                Rsmsr=(double)((int)(Rsmsr*100))/100;
                if(Rsmsr>=0 && Rsmsr>=1)
                {
                    NGRsmsr = NGsdcchcrbh + NRsmsr;
                    DGRsmsr = DGRsmsr + DRsmsr;
                }else Rsmsr=-1;
            }
            else Rsmsr=-1;

            if(Hovercnt!=0)
            {
                HoSucces=Hoversuc/Hovercnt;
                HoSucces=(double)((int)(HoSucces*100))/100;
                if(HoSucces>=0 && HoSucces<=1 )
                {
                    NGHosucces = NGHosucces + Hoversuc;
                    DGHosucces = DGHosucces+Hovercnt;
                }
                else if(HoSucces > 1)
                {
                    HoSucces=1;
                    NGHosucces = 1;
                    DGHosucces = 1;
                }
                else HoSucces=-1;
            }
            else HoSucces=-1;

            if(Dtchmbh!=0)
            {
                tchmbh=Ntchmbh/Dtchmbh;
                tchmbh=(double)((int)(tchmbh*100))/100;
                
                NGtchmbh = NGtchmbh + Ntchmbh;
                DGtchmbh = 1;
            }
            else tchmbh=0;

            if(Dtchcrbh!=0)
            {
                tchcrbh=Ntchcrbh/Dtchcrbh;
                tchcrbh=(double)((int)(tchcrbh*100))/100;
                if(tchcrbh<=1 && tchcrbh>=0)
                {
                    NGtchcrbh = NGtchcrbh + Ntchcrbh;
                    DGtchcrbh = DGtchbrbh + Dtchcrbh;
                }else tchcrbh=2;
            }
            else tchcrbh=2;

            if(Dtchdrbh!=0)
            {
                tchdrbh=Ntchdrbh/Dtchdrbh;
                tchdrbh=(double)((int)(tchdrbh*100))/100;
                if(tchdrbh<=1 && tchdrbh>=0)
                {
                    NGtchdrbh = NGtchdrbh + Ntchdrbh;
                    DGHosucces = DGtchdrbh + Dtchdrbh;
                }else tchdrbh=2;
            }
            else tchdrbh=2;

            if(Dtchbrbh!=0)
            {
                tchbrbh=Ntchbrbh/Dtchbrbh;
                tchbrbh=(double)((int)(tchbrbh*100))/100;
                if(tchbrbh>=0 && tchbrbh<=1)
                {
                    NGtchbrbh = NGtchbrbh + Ntchbrbh;
                    DGtchbrbh = 1;
                }else tchbrbh=2;
            }
            else tchbrbh=2;

            if(Dcsrbh!=0)
            {
                csrbh=Ncsrbh/Dcsrbh;
                csrbh=(double)((int)(csrbh*100))/100;
                if(csrbh>=0 && csrbh<=1)
                {
                    NGcsrbh = NGcsrbh + Ncsrbh;
                    DGcsrbh = 1;
                }else csrbh=2;
            }
            else csrbh=2;
            
            Object obj=mapBhCellule.get(cellule);
            Object obj1=mapTrHoraireMoyenCellule.get(cellule);
            Object obj2=mapTraficBhCellule.get(cellule);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                tchhm=mapTrHoraireMoyenCellule.get(cellule);
                tchmbh=mapTraficBhCellule.get(cellule);
                if(tchhm>0)
                {
                    int nbreHeure=cn.getNbreHeurePeriode(dateDebut, dateFin);
                    bhtr=tchmbh/(nbreHeure*tchhm);
                } else bhtr=2;
                bhtr=(double)((int)(bhtr*100))/100;
                InsertTableCellule(cellule);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
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
            int nbreEnrg=0;
            String requete = "select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            ResultSet resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                nbreEnrg++;
                double val1=0,val2=0;
                int heure=-1;
                String str=resultTotal.getString("heure").trim();
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {heure=-1;}

                try
                {
                    val1 = resultTotal.getFloat("tfnscan");
                } catch (Exception ex) {val1=0; }
                try
                {
                    val2 = resultTotal.getFloat("thnscan");
                } catch (Exception ex){val2=0;}

                if(val1>0 && val2>0 && heure>=0)
                {
                    TabBhCellule[heure-1]=heure;
                    TabTchmCellule[heure-1]=TabTchmCellule[heure-1]+(resultTotal.getFloat("tftralacc")/val1)+(resultTotal.getFloat("thtralacc")/val2);
                }
            }

            /***Calcul de Tchm & Bh par region ***/
            requete = "select distinct(date) from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
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
            if(nbreEnrg>0)
                som=som/(float)(nbreEnrg);
            else som=0;
            som=(double)((int)(som*100))/100;
            TchmBhCellule=(double)((int)(TchmBhCellule*100))/100;
            if(BhCellule>=0)
            {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhCellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            }else System.out.println("************************************************************:Cellule:"+cellule+" ,BHfausse:"+BhCellule);
        } catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void InsertTableCellule(String cellule)
    {
        String requete="select * from table_bts where trim(cell_name)='"+cellule.trim()+"'";
        try
        {
            ResultSet resultSet = cn.getResultset(requete);
            if(resultSet.next())
            {
                requete = "insert into table_valeurs_bts(cell_name,tchm,tchmbh,tchhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,HoSucces,the_geom) values(";
                requete = requete +"'"+cellule + "',";
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
                requete = requete +"'"+resultSet.getObject("the_geom").toString()+"'";
                requete = requete + ")";
                cn.ExecuterRequete(requete);
                System.out.println("valeur requete:" + requete);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void InsertTableRegion(String region)
    {
        String requete="select * from table_regions where trim(region)='"+region.trim()+"'";
        try
        {
            ResultSet resultSet = cn.getResultset(requete);
            if(resultSet.next())
            {
                requete = "insert into table_valeurs_regions(region,tchm,tchmbh,tchhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,hosucces,the_geom) values(";
                requete = requete +"'"+ region + "',";
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
                requete = requete + "'"+resultSet.getObject("the_geom").toString()+"'";
                requete = requete + ")";
                System.out.println("valeur requete:" + requete);
                cn.ExecuterRequete(requete);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }

        requete="insert into tablevaleurskpi(region,tchm,tchmbh,tchhm,cssr,tchcr,tchcrbh,tchdr,tchdrbh,sdcchcrbh,sdcchdrbh,cdr,cdrbh,csr,bh,hosucces) values(";
        requete=requete+"'"+region+"',";
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
        requete=requete+HoSucces;
        requete=requete+")";
        System.out.println("valeur requete:"+requete);
        cn.ExecuterRequete(requete);
    }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("*************Desctruction de la classe Ericsson***************************");
        super.finalize();
        cn.closeConnection();
        System.out.println("*************Fin Desctruction de la classe Ericsson***************************");
    }

    
    public static void main(String arg[]) throws ClassNotFoundException
    {
        try
        {
            Operateur operateur = new Operateur();
            operateur.setBddOperateur("azur_ericsson");
            operateur.setNomOperateur("MTN");
            operateur.setEquipement("ERC");
            CalCulEricsson ch = new CalCulEricsson(operateur, "2013-08-02", "2013-08-31");
            //ch.getBhParRegion("Kinkala");
            ch.CalculTotal();
        } catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
