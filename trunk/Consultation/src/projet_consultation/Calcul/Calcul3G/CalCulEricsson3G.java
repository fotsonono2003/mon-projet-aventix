package projet_consultation.Calcul.Calcul3G;

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
 
public class CalCulEricsson3G
{
    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double  tchhm=0;
    private double  tchm=0;
    private double  tchmbh=0;
    private double cssr=0,cssrbh=0;
    private double bhtr=0;
    private double tchcr=0;
    private double tchcrbh=0;
    private double tchdr=0;
    private double tchdrbh=0;
    private double sdcchbrbh=0,sdcchbr=0;
    private double sdcchcrbh=0,sdcchcr=0;
    private double sdcchdrbh=0,sdcchdr=0;
    private double csr=0;
    private double csrbh=0;
    private double cdr=0;
    private double cdrbh=0;
    private double hosucces=0,hosuccesbh=0;
    
    private int BhG=-1,BhRegion=-1,BhCellule=-1;
    private double tchbr=0;
    private double tchbrbh=0;
    private double Rsmsr=0;

    private Map<String,Double> mapTraficBhRegion=null;
    private Map<String,Integer> mapBhRegion =null;
    private Map<String,Double> mapTrHoraireMoyenRegion=null ;

    private Map<String,Double> mapTraficBhCellule=null ;
    private Map<String,Integer> mapBhCellule=null;
    private Map<String,Double> mapTrHoraireMoyenCellule=null;
    private int idinfo=0;
   
    
    public CalCulEricsson3G(Operateur operateur,String datedebut,String datefin) throws SQLException
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
            String requete="select count(cell_name)*24*(SELECT DATE_PART('day', '"+dateFin+" 01:00:00'::timestamp - '"+dateDebut+" 00:00:00'::timestamp)+1) as nbre "
                    + "from table_bts "
                    + "where region='"+region+"' group by region";
            System.out.println("Requete nombre dee jour:"+requete);
            ResultSet resultSet=cn.getResultset(requete);
            if(resultSet.next())
            {
                NbreErgReel=resultSet.getInt("nbre");
            }
            

            ResultSet result=null;
            if (mapBhRegion.get(region)!=null)
            {
                BhRegion = mapBhRegion.get(region);
            }else BhRegion=-1;
            requete="select count(*)as nbre from tableregistre where trim(region)='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"' ";
            result=cn.getResultset(requete);
            try
            {
                if (result.next())
                {
                    NbreErgRecus = result.getInt("nbre");
                }
            }
            catch (Exception ex)
            {
                Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
                NbreErgRecus=0;
            }
            
            /////////////////
            tchm=0;tchmbh=0;tchcr=0;tchdr=0;
            tchbr=0;cssr=0;cdr=0;csr=0;
            cssrbh=0;cdrbh=0;csrbh=0;
            tchcrbh=0;tchdrbh=0;tchbrbh=0;
            double Hoversuc=0,Hovercnt=0;hosucces=0;
            sdcchcrbh=0;sdcchbrbh=0;sdcchdrbh=0;cdrbh=0;csrbh=0;Rsmsr=0;
            sdcchcr=0;sdcchbr=0;sdcchdr=0;
            double FullRate=0,HalfRate=0;
            
            double thnrelcong=0,tfnrelcong=0,thnrelcongsub=0,tfnrelcongsub=0,thndropsub=0,tfcassallsub=0,thcassall=0,thcassallsub=0,thndrop=0,tfcassall=0,tftralacc=0,thtralacc=0,tfndropsub=0,tfnscan=0,thnscan=0,
                    cnrelcong=0,tassall=0,ccongs=0,ccalls=0,cmsestab=0,hovercnt=0,hoversuc=0,tfndrop=0,cndrop=0;
            double  thnrelcongBh=0,tfnrelcongBh=0,thnrelcongsubBh=0,tfnrelcongsubBh=0,thndropsubBh=0,tfcassallsubBh=0,thcassallBh=0,thcassallsubBh=0,thndropBh=0,tfcassallBh=0,tftralaccBh=0,thtralaccBh=0,tfndropsubBh=0,
                    tfnscanBh=0,thnscanBh=0,cnrelcongBh=0,tassallBh=0,ccongsBh=0,ccallsBh=0,cmsestabBh=0,hovercntBh=0,hoversucBh=0,tfndropBh=0,
                    cndropBh=0;
            
            System.out.println("----------------------**********//////////////////////    Region en cour de calcul:" + region);
            //requete ="select * from tableregistre where trim(region)='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"' ";
            requete ="select region,heure,sum(tfnscan) as tfnscan,sum(thnscan) as thnscan,sum(tftralacc) as tftralacc,sum(thtralacc) as thtralacc,sum(cmsestab) as cmsestab,"
                    + " sum(tassall) as tassall,sum(cnrelcong) as cnrelcong, sum(cndrop) as cndrop,sum(tfcassall) as tfcassall,sum(tfndrop) as tfndrop,sum(tfndropsub) as tfndropsub,"
                    + " sum(ccongs) as ccongs, sum(ccalls) as ccalls,sum(hoversuc) as hoversuc,sum(hovercnt) as hovercnt,sum(tfnrelcong) as tfnrelcong,sum(thnrelcong) as thnrelcong,"
                    + " sum(tfnrelcongsub) as tfnrelcongsub,sum(thnrelcongsub) as thnrelcongsub, "
                    + " sum(thndrop) as thndrop,sum(thcassall) as thcassall,sum(thcassallsub) as thcassallsub, sum(tfcassallsub) as tfcassallsub, sum(thndropsub) as thndropsub"
                    + " from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"' and region='"+region+"' "
                    + "group by region,heure order by region,heure ; ";
            System.out.println("---------------------------------------------------requte:"+requete);
            result = cn.getResultset(requete);
            while (result.next())
            {
                tfcassall=tfcassall+result.getFloat("tfcassall");
                tfnscan=tfnscan+result.getFloat("tfnscan");
                thnscan=thnscan+result.getFloat("thnscan");
                tassall=tassall+result.getFloat("tassall");
                ccalls=ccalls+result.getFloat("ccalls");
                ccongs=ccongs+result.getFloat("ccongs");
                cnrelcong=cnrelcong+result.getFloat("cnrelcong");
                cndrop=cndrop+result.getFloat("cndrop");
                cmsestab=cmsestab+result.getFloat("cmsestab");
                tftralacc=tftralacc+result.getFloat("tftralacc");
                thtralacc=thtralacc+result.getFloat("thtralacc");

                tfndrop=tfndrop+result.getFloat("tfndrop");
                tfndropsub=tfndropsub+result.getFloat("tfndropsub");
                thndrop=thndrop+result.getFloat("thndrop");
                thcassall=thcassall+result.getFloat("thcassall");
                thcassallsub=thcassallsub+result.getFloat("thcassallsub");
                tfcassallsub=tfcassallsub+result.getFloat("tfcassallsub");
                thndropsub=thndropsub+result.getFloat("thndropsub");
                thnrelcongsub=thnrelcongsub+result.getFloat("thnrelcongsub");
                tfnrelcongsub=tfnrelcongsub+result.getFloat("tfnrelcongsub");
                thnrelcong=thnrelcong+result.getFloat("thnrelcong");
                tfnrelcong=tfnrelcong+result.getFloat("tfnrelcong");

                //NbreErgRecus++;

                Hoversuc=Hoversuc+result.getFloat("hoversuc");
                Hovercnt=Hovercnt+result.getFloat("hovercnt");

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
                    tfcassallBh = result.getFloat("tfcassall");
                    tfnscanBh = result.getFloat("tfnscan");
                    thnscanBh = result.getFloat("thnscan");
                    tassallBh = result.getFloat("tassall");
                    ccallsBh = result.getFloat("ccalls");
                    ccongsBh = result.getFloat("ccongs");
                    cnrelcongBh =result.getFloat("cnrelcong");
                    cndropBh = result.getFloat("cndrop");
                    cmsestabBh = result.getFloat("cmsestab");
                    tftralaccBh=result.getFloat("tftralacc");
                    thtralaccBh=result.getFloat("thtralacc");                    
                    tfndropBh=result.getFloat("tfndrop");
                    tfndropsubBh =result.getFloat("tfndropsub");
                    thndropBh = result.getFloat("thndrop");
                    thcassallBh =result.getFloat("thcassall");
                    thcassallsubBh = result.getFloat("thcassallsub");
                    tfcassallsubBh =result.getFloat("tfcassallsub");
                    thndropsubBh=result.getFloat("thndropsub");
                    thnrelcongsubBh = result.getFloat("thnrelcongsub");
                    tfnrelcongsubBh = result.getFloat("tfnrelcongsub");
                    thnrelcongBh =result.getFloat("thnrelcong");
                    tfnrelcongBh =result.getFloat("tfnrelcong");
                }
            }
            /*taux d'information manquante pour cette region*/            
            float TauxInfoManq=0;
            if(NbreErgReel>0)
            {
                System.out.println("Region:"+region+" Bh:"+BhRegion);
                System.out.println("Nbre enrg reçu:"+NbreErgRecus);
                System.out.println("Nbre enrg reels:"+NbreErgReel);
                TauxInfoManq=1-(float)((float)NbreErgRecus/(float)(NbreErgReel));
                TauxInfoManq=((float)((int)(TauxInfoManq*10000))/10000)*100;
            }
            Map<String,Float>MapTauxInfoManqRegion=new HashMap<String, Float>();
            //////calcul du Taux d'information manquantes
            tchm = (tftralacc / 360) + (thtralacc / 360);
            tchm = (double) ((int) (tchm * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchm", TauxInfoManq);
            
            tchmbh = (tftralaccBh / 360) + (thtralaccBh / 360);
            tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchmbh", TauxInfoManq);

            if((thcassall+thcassallsub+tfcassall+tfcassallsub)>0)
            {
                cdr=(tfndrop+tfndropsub+thndrop+thndropsub)/(thcassall+thcassallsub+tfcassall+tfcassallsub);
                cdr=(double)((int)(cdr*10000))/10000;
                if (cdr >= 0 && cdr <= 1) {
                    MapTauxInfoManqRegion.put("cdr", TauxInfoManq);
                } else {
                    cdr = 1;
                    MapTauxInfoManqRegion.put("cdr", 100F);
                }
            }else
            {
                cdr=1;
                MapTauxInfoManqRegion.put("cdr", 100F);
            }

            if((thcassallBh+thcassallsubBh+tfcassallBh+tfcassallsubBh)>0)
            {
                cdrbh=(tfndropBh+tfndropsubBh+thndropBh+thndropsubBh)/(thcassallBh+thcassallsubBh+tfcassallBh+tfcassallsubBh);
                cdrbh=(double)((int)(cdr*10000))/10000;
                if (cdrbh >= 0 && cdrbh <= 1) {
                    MapTauxInfoManqRegion.put("cdrbh", TauxInfoManq);
                } else {
                    cdrbh = 1;
                    MapTauxInfoManqRegion.put("cdrbh", 100F);
                }
            }else
            {
                cdrbh=1;
                MapTauxInfoManqRegion.put("cdrbh", 100F);
            }

            if(tassall>0 && ccalls>0 && cmsestab>0)
            {
                cssr=(tfcassall/tassall)*(1-(ccongs/ccalls))*(1-((cndrop-cnrelcong)/cmsestab));
                cssr=(double)((int)(cssr*10000))/10000;
                if(cssr>=0 && cssr<=1)
                {
                    MapTauxInfoManqRegion.put("cssr",TauxInfoManq);
                }else
                {
                    cssr = 0;
                    MapTauxInfoManqRegion.put("cssr",100F);
                }
            }
            else
            {
                cssr = 0;
                MapTauxInfoManqRegion.put("cssr",100F);
            }                

            cssrbh=0;
            if(tassallBh>0 && ccallsBh>0 && cmsestabBh>0)
            {
                cssrbh=(tfcassallBh/tassallBh)*(1-(ccongsBh/ccallsBh))*(1-((cndropBh-cnrelcongBh)/cmsestabBh));
                cssrbh=(double)((int)(cssrbh*10000))/10000;
                if (cssrbh >= 0 && cssrbh <= 1) {
                    MapTauxInfoManqRegion.put("cssrbh", TauxInfoManq);
                } else {
                    cssrbh = 0;
                    MapTauxInfoManqRegion.put("cssrbh", 100F);
                }
            }
            else
            {
                cssrbh = 0;
                MapTauxInfoManqRegion.put("cssrbh", 100F);
            }

            csr = cssr*(1-cdr);
            csr = (double) ((int) (csr * 10000)) / 10000;
            if (csr >= 0 && csr <= 1)
            {
                MapTauxInfoManqRegion.put("csr",TauxInfoManq);
            } else
            {
                csr = 0;
                MapTauxInfoManqRegion.put("csr",100F);
            }

            csrbh = cssrbh*(1-cdrbh);
            csrbh = (double) ((int) (csrbh * 10000)) / 10000;
            if (csrbh >= 0 && csrbh <= 1)
            {
                MapTauxInfoManqRegion.put("csrbh", TauxInfoManq);
            } else
            {
                csrbh=0;
                MapTauxInfoManqRegion.put("csrbh", 100F);
            }
            
            if(tassall!=0)
            {
                tchcr=(cnrelcong+tfnrelcong+thnrelcong+tfnrelcongsub+thnrelcongsub)/tassall;
                tchcr=(double)((int)(tchcr*10000))/10000;
                if(tchcr>=0 && tchcr<=1)
                {
                    tchbr = tchcr;
                    MapTauxInfoManqRegion.put("tchbr", TauxInfoManq);
                    MapTauxInfoManqRegion.put("tchcr", TauxInfoManq);
                }else 
                {
                    tchcr = 1;
                    tchbr=1;
                    MapTauxInfoManqRegion.put("tchbr", 100F);
                    MapTauxInfoManqRegion.put("tchcr", 100F);
                }
            }
            else
            {
                tchbr=1;
                tchcr = 1;
                MapTauxInfoManqRegion.put("tchbr",100F);
                MapTauxInfoManqRegion.put("tchcr",100F);
            }
            
            if(tassallBh!=0)
            {
                tchcrbh=(cnrelcongBh+tfnrelcongBh+thnrelcongBh+tfnrelcongsubBh+thnrelcongsubBh)/tassallBh;
                tchcrbh=(double)((int)(tchcrbh*10000))/10000;
                if(tchcrbh>=0 && tchcrbh<=1)
                {
                    tchbrbh = tchcrbh;
                    MapTauxInfoManqRegion.put("tchbrbh", TauxInfoManq);
                    MapTauxInfoManqRegion.put("tchcrbh", TauxInfoManq);
                }else
                {
                    tchcrbh = 1;
                    tchbrbh=1;
                    MapTauxInfoManqRegion.put("tchbrbh", 100F);
                    MapTauxInfoManqRegion.put("tchcrbh", 100F);
                }
            }
            else
            {
                tchbrbh=1;
                tchcrbh = 1;
                MapTauxInfoManqRegion.put("tchbrbh",100F);
                MapTauxInfoManqRegion.put("tchcrbh",100F);
            }

            tchdr = cdr;
            if (tchdr >= 0 && tchdr <= 1) {
                MapTauxInfoManqRegion.put("tchdr", TauxInfoManq);
            } else {
                tchdr = 1;
                MapTauxInfoManqRegion.put("tchdr", 100F);
            }

            tchdrbh = cdrbh;
            tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
            if (tchdrbh >= 0 && tchdrbh <= 1) {
                MapTauxInfoManqRegion.put("tchdrbh", TauxInfoManq);
            } else {
                tchdrbh = 1;
                MapTauxInfoManqRegion.put("tchdrbh", 100F);
            }


            if (cmsestab != 0)
            {
                sdcchdr = (cndrop - cnrelcong) / cmsestab;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr >= 0 && sdcchdr<= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchdr", TauxInfoManq);
                }else
                {
                    sdcchdr = 1;
                    MapTauxInfoManqRegion.put("sdcchdr", 100F);
                }
            } else
            {
                sdcchdr= 1;
                MapTauxInfoManqRegion.put("sdcchdr", 100F);
            }

            if (cmsestabBh != 0)
            {
                sdcchdrbh = (cndropBh - cnrelcongBh) / cmsestabBh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh >= 0 && sdcchdrbh<= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchdrbh", TauxInfoManq);
                }else
                {
                    sdcchdrbh = 1;
                    MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
                }
            } else
            {
                sdcchdrbh = 1;
                MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
            }

            if (ccalls != 0)
            {
                sdcchbr = ccongs / ccalls;
                sdcchbr = (double) ((int) (sdcchbr * 10000)) / 10000;
                sdcchcr = sdcchbr;
                if (sdcchbr >=0 && sdcchbr <=1)
                {
                    MapTauxInfoManqRegion.put("sdcchbr", TauxInfoManq);
                    MapTauxInfoManqRegion.put("sdcchcr", TauxInfoManq);
                }else
                {
                    MapTauxInfoManqRegion.put("sdcchbr", 100F);
                    MapTauxInfoManqRegion.put("sdcchcr", 100F);
                    sdcchbr = 1;
                    sdcchcr = 1;
                }
            } else
            {
                sdcchbr = 1;
                sdcchcr = 1;
                MapTauxInfoManqRegion.put("sdcchbr",100F);
                MapTauxInfoManqRegion.put("sdcchcr",100F);
            }

            if (ccallsBh != 0)
            {
                sdcchbrbh = ccongsBh / ccallsBh;
                sdcchbrbh = (double) ((int) (sdcchbrbh * 10000)) / 10000;
                sdcchcrbh = sdcchbrbh;
                if (sdcchbrbh >=0 && sdcchbrbh <=1)
                {
                    MapTauxInfoManqRegion.put("sdcchbrbh", TauxInfoManq);
                    MapTauxInfoManqRegion.put("sdcchcrbh", TauxInfoManq);
                }else
                {
                    MapTauxInfoManqRegion.put("sdcchbrbh", 100F);
                    MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
                    sdcchbrbh = 1;
                    sdcchcrbh = 1;
                }
            } else
            {
                sdcchbrbh = 1;
                sdcchcrbh = 1;
                MapTauxInfoManqRegion.put("sdcchbrbh",100F);
                MapTauxInfoManqRegion.put("sdcchcrbh",100F);
            }
                                    
            Rsmsr = 0;
            Rsmsr = (double) ((int) (Rsmsr * 10000)) / 10000;
            if (Rsmsr >= 0 && Rsmsr <= 1)
            {
                MapTauxInfoManqRegion.put("rsmsr", TauxInfoManq);
            } else {
                Rsmsr = 0;
                MapTauxInfoManqRegion.put("rsmsr", 100F);
            }

            if(Hovercnt!=0)
            {
                hosucces=Hoversuc/Hovercnt;
                hosucces=(double)((int)(hosucces*10000))/10000;
                if(hosucces>=0 &&  hosucces<=1)
                {
                    MapTauxInfoManqRegion.put("hosucces", TauxInfoManq);
                }
                else
                {
                    hosucces = 0;
                    MapTauxInfoManqRegion.put("hosucces", 100f);
                }
            }
            else if(Hoversuc==0 && Hovercnt==0)
            {
                hosucces=1;
                MapTauxInfoManqRegion.put("hosucces", TauxInfoManq);
            }
            else 
            {
                hosucces = 0;
                MapTauxInfoManqRegion.put("hosucces", 100f);
            }

            if(hovercntBh!=0)
            {
                hosuccesbh=hoversucBh/hovercntBh;
                hosuccesbh=(double)((int)(hosuccesbh*10000))/10000;
                if(hosuccesbh>=0 &&  hosuccesbh<=1)
                {
                    MapTauxInfoManqRegion.put("hosuccesbh", TauxInfoManq);
                }
                else
                {
                    hosuccesbh = 0;
                    MapTauxInfoManqRegion.put("hosuccesbh", 100f);
                }
            }
            else if(hoversucBh==0 && hovercntBh==0)
            {
                hosuccesbh=1;
                MapTauxInfoManqRegion.put("hosuccesbh", TauxInfoManq);
            }
            else
            {
                hosuccesbh = 0;
                MapTauxInfoManqRegion.put("hosuccesbh", 100f);
            }

            Object obj=mapBhRegion.get(region);
            Object obj1=mapTrHoraireMoyenRegion.get(region);
            Object obj2=mapTraficBhRegion.get(region);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                tchhm=mapTrHoraireMoyenRegion.get(region);
                tchmbh=mapTraficBhRegion.get(region);
                if(tchm>0)
                {
                    bhtr=tchmbh/tchm;
                } else bhtr=1;
                bhtr=(double)((int)(bhtr*10000))/10000;
                if (bhtr ==1) {
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
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CalculTotal()
    {
        System.out.println("Debut calcul total Ericsson");
        ResultSet result=null;
        try
        {            
            List<String> lstRegion = new ArrayList<String>();
            String requete = "select distinct(region) from table_regions";
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
            {
                BhRegion=BhG;
                InsertTableRegion("Global");
            }
            
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
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
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
        double [] TabTchmRegion=new double[24];
        for (int i = 0; i < 24; i++)
        {
            TabTchmRegion[i]=0;
        }
        ResultSet resultTotal=null;
        try
        {
            int nbreJrsHrs=0;
            //String requete="select count(*) as nbre from (select distinct(date) from tableregistre  where date>='"+dateDebut+"' and date<='"+dateFin+"' and region='"+region+"') query";
            String requete="select count(*)*(select count(*) as nbre from (select distinct(heure) from tableregistre  "
                    + "where date>='"+dateDebut+"' and date<='"+dateFin+"' and region='"+region+"' order by heure) query) as nbre from (select distinct(date) "
                    + "from tableregistre  where date>='"+dateDebut+"' and date<='"+dateFin+"' and region='"+region+"') query";
            resultTotal=cn.getResultset(requete);
            if(resultTotal.next())
            {
                try
                {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                }
                catch (Exception ex)
                {
                    Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs=0;
                }
            }
            //String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select region,heure,sum(tfnscan) as tfnscan ,sum(thnscan) as thnscan,sum(tftralacc) as tftralacc,sum(thtralacc) as thtralacc from tableregistre "
                    + "where region='"+region+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'"
                    + " group by region,heure  order by region,heure";
            resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
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

                //if(val1>0 && val2>0 && heure>=0)
                if(heure>=0)
                {
                    //TabTchmRegion[heure-1]=TabTchmRegion[heure-1]+(resultTotal.getFloat("tftralacc")/val1)+(resultTotal.getFloat("thtralacc")/val2);
                    TabTchmRegion[heure-1]=TabTchmRegion[heure-1]+(resultTotal.getFloat("tftralacc")/360)+(resultTotal.getFloat("thtralacc")/360);
                }
            }
            /***Calcul de Tchm & Bh par region ***/            
            int n=TabTchmRegion.length;
            double TchmBhRegion=0;BhRegion=-1;
            double som=0;
            for(int i=0;i<n; i++)
            {
                som=som+TabTchmRegion[i];
                if(TabTchmRegion[i]>TchmBhRegion)
                {
                    TchmBhRegion=TabTchmRegion[i];
                    BhRegion=i+1;
                }
            }

            if(nbreJrsHrs>0)
                som=som/(double)(nbreJrsHrs);
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
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                resultTotal.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void CalCulBHParRegion(List<String> lstRegion)
    {
        double [] TabTchmGlobal=new double[24];
        for (int i = 0; i < 24; i++)
        {
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
            String requete = "select heure,sum(tfnscan) as tfnscan,sum(thnscan) as thnscan,sum(tftralacc) as tftralacc,sum(thtralacc) as thtralacc"
                + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' group by heure order by heure";
            ResultSet result=cn.getResultset(requete);
            try
            {
                while (result.next())
                {
                    int heure=-1;
                    try
                    {
                        heure = Integer.parseInt(result.getString("heure").split(":")[0]);
                    }
                    catch (Exception ex)
                    {
                        heure=-1;
                    }
                    if (heure>=0)
                    {
                        TabTchmGlobal[heure - 1] = TabTchmGlobal[heure - 1] + (result.getFloat("tftralacc") / 360) + (result.getFloat("thtralacc") / 360);
                    }
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
            }

            int n=TabTchmGlobal.length;
            double TchmBhGlobal=0; BhG=-1;
            double som=0;
            for(int i=0;i<n; i++)
            {
                som=som+TabTchmGlobal[i];
                if(TabTchmGlobal[i]>TchmBhGlobal)
                {
                    TchmBhGlobal=TabTchmGlobal[i];
                    BhG=i+1;
                }
            }
            requete="select count(*) as nbre from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"'";
            int nbreErg=cn.getNbreLigneResultset(requete);
            if(nbreErg>0)
                som=som/(double)(nbreErg);
            else som=0;
            som=(double)((int)(som*10000))/10000;
            TchmBhGlobal=(double)((int)(TchmBhGlobal*10000))/10000;
            if(BhG>=0)
            {
                mapBhRegion.put("Global", BhG);
                mapTraficBhRegion.put("Global", TchmBhGlobal);
                mapTrHoraireMoyenRegion.put("Global", som);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculValeurGlobales()
    {
        int bh=-1;
        Object objBh=mapBhRegion.get("Global");
        if(objBh!=null)
        {
            try
            {
                bh = Integer.valueOf(objBh.toString());
            }
            catch (Exception ex)
            {
                bh=-1;
            }
        }else bh=-1;
        String StrBh="";
        if(bh<10)
        {
            StrBh="0"+bh+":00";
        }else StrBh=bh+":00";
        System.out.println("******************************BhGlobal:"+StrBh);
        
        double thnrelcong = 0, tfnrelcong = 0, thnrelcongsub = 0, tfnrelcongsub = 0, thndropsub = 0, tfcassallsub = 0, thcassall = 0, thcassallsub = 0, thndrop = 0,
                tfcassall = 0,tftralacc = 0, thtralacc = 0, tfndropsub = 0,tfnscan = 0, thnscan = 0,
                cnrelcong = 0, tassall = 0, ccongs = 0, ccalls = 0, cmsestab = 0, hovercnt = 0, hoversuc = 0, tfndrop = 0, cndrop = 0;
        double thnrelcongBh = 0, tfnrelcongBh = 0, thnrelcongsubBh = 0, tfnrelcongsubBh = 0, thndropsubBh = 0, tfcassallsubBh = 0, thcassallBh = 0, thcassallsubBh = 0,
                thndropBh = 0,tfcassallBh = 0, tftralaccBh = 0, thtralaccBh = 0, tfndropsubBh = 0,tfnscanBh = 0, thnscanBh = 0, cnrelcongBh = 0, tassallBh = 0, ccongsBh = 0,
                ccallsBh = 0, cmsestabBh = 0, hovercntBh = 0, hoversucBh = 0, tfndropBh = 0,cndropBh = 0;
        double Hoversuc=0,Hovercnt=0;
        String requete = "select heure,sum(tfnscan) as tfnscan,sum(thnscan) as thnscan,sum(tftralacc) as tftralacc,sum(thtralacc) as thtralacc,sum(cmsestab) as cmsestab,"
                + " sum(tassall) as tassall,sum(cnrelcong) as cnrelcong, sum(cndrop) as cndrop,sum(tfcassall) as tfcassall,sum(tfndrop) as tfndrop,sum(tfndropsub) as tfndropsub,"
                + " sum(ccongs) as ccongs, sum(ccalls) as ccalls,sum(hoversuc) as hoversuc,sum(hovercnt) as hovercnt,sum(tfnrelcong) as tfnrelcong,sum(thnrelcong) as thnrelcong,"
                + " sum(tfnrelcongsub) as tfnrelcongsub,sum(thnrelcongsub) as thnrelcongsub, "
                + " sum(thndrop) as thndrop,sum(thcassall) as thcassall,sum(thcassallsub) as thcassallsub, sum(tfcassallsub) as tfcassallsub, sum(thndropsub) as thndropsub"
                + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' group by heure";
        System.out.println("---------------------------------------------------requete Globale:" + requete);
        ResultSet result=cn.getResultset(requete);
        try
        {
            while (result.next())
            {

                Hoversuc=Hoversuc+result.getFloat("hoversuc");
                Hovercnt=Hovercnt+result.getFloat("hovercnt");

                tfcassall=tfcassall+result.getFloat("tfcassall");
                tfnscan=tfnscan+result.getFloat("tfnscan");
                thnscan=thnscan+result.getFloat("thnscan");
                tassall=tassall+result.getFloat("tassall");
                ccalls=ccalls+result.getFloat("ccalls");
                ccongs=ccongs+result.getFloat("ccongs");
                cnrelcong=cnrelcong+result.getFloat("cnrelcong");
                cndrop=cndrop+result.getFloat("cndrop");
                cmsestab=cmsestab+result.getFloat("cmsestab");
                tftralacc=tftralacc+result.getFloat("tftralacc");
                thtralacc=thtralacc+result.getFloat("thtralacc");
                tfndrop=tfndrop+result.getFloat("tfndrop");
                tfndropsub=tfndropsub+result.getFloat("tfndropsub");
                thndrop=thndrop+result.getFloat("thndrop");
                thcassall=thcassall+result.getFloat("thcassall");
                thcassallsub=thcassallsub+result.getFloat("thcassallsub");
                tfcassallsub=tfcassallsub+result.getFloat("tfcassallsub");
                thndropsub=thndropsub+result.getFloat("thndropsub");
                thnrelcongsub=thnrelcongsub+result.getFloat("thnrelcongsub");
                tfnrelcongsub=tfnrelcongsub+result.getFloat("tfnrelcongsub");
                thnrelcong=thnrelcong+result.getFloat("thnrelcong");
                tfnrelcong=tfnrelcong+result.getFloat("tfnrelcong");
                int heure=-1;
                String str=result.getString("heure").split(":")[0];
                try
                {
                    heure = Integer.parseInt(str);
                }
                catch (Exception ex)
                {
                    heure=-1;
                }
                if (heure>=0 && heure==BhG)
                {
                    tfcassallBh = result.getFloat("tfcassall");
                    tfnscanBh = result.getFloat("tfnscan");
                    thnscanBh = result.getFloat("thnscan");
                    tassallBh = result.getFloat("tassall");
                    ccallsBh = result.getFloat("ccalls");
                    ccongsBh = result.getFloat("ccongs");
                    cnrelcongBh = result.getFloat("cnrelcong");
                    cndropBh = result.getFloat("cndrop");
                    cmsestabBh = result.getFloat("cmsestab");
                    tftralaccBh = result.getFloat("tftralacc");
                    thtralaccBh = result.getFloat("thtralacc");
                    tfndropBh = result.getFloat("tfndrop");
                    tfndropsubBh = result.getFloat("tfndropsub");
                    thndropBh = result.getFloat("thndrop");
                    thcassallBh = result.getFloat("thcassall");
                    thcassallsubBh = result.getFloat("thcassallsub");
                    tfcassallsubBh = result.getFloat("tfcassallsub");
                    thndropsubBh = result.getFloat("thndropsub");
                    thnrelcongsubBh = result.getFloat("thnrelcongsub");
                    tfnrelcongsubBh = result.getFloat("tfnrelcongsub");
                    thnrelcongBh = result.getFloat("thnrelcong");
                    tfnrelcongBh = result.getFloat("tfnrelcong");
                }
            }
        } catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (tfnscan > 0 && thnscan > 0)
        {
            tchm = (tftralacc / 360) + (thtralacc / 360);
            tchm = (double) ((int) (tchm * 10000)) / 10000;
        } else
        {
            tchm = 0;
        }

        if (tfnscanBh > 0 && thnscanBh > 0)
        {
            tchmbh = (tftralaccBh / 360) + (thtralaccBh / 360);
            tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;
        } else {
            tchmbh = 0;
        }

        if ((thcassall + thcassallsub + tfcassall + tfcassallsub) > 0)
        {
            cdr = (tfndrop + tfndropsub + thndrop + thndropsub) / (thcassall + thcassallsub + tfcassall + tfcassallsub);
            cdr = (double) ((int) (cdr * 10000)) / 10000;
            if(cdr<0 || cdr>1)
            {
                cdr=1;
            }
        } else
        {
            cdr = 1;
        }

        if ((thcassallBh + thcassallsubBh + tfcassallBh + tfcassallsubBh) > 0) {
            cdrbh = (tfndropBh + tfndropsubBh + thndropBh + thndropsubBh) / (thcassallBh + thcassallsubBh + tfcassallBh + tfcassallsubBh);
            cdrbh = (double) ((int) (cdr * 10000)) / 10000;
        } else
        {
            cdrbh = 1;
        }

        if (tassall > 0 && ccalls > 0 && cmsestab > 0)
        {
            cssr = (tfcassall / tassall) * (1 - (ccongs / ccalls)) * (1 - ((cndrop - cnrelcong) / cmsestab));
            cssr = (double) ((int) (cssr * 10000)) / 10000;
            if (cssr <0 || cssr > 1)
            {
                cssr = 0;
            }
        } else
        {
            cssr = 0;
        }

        cssrbh = 0;
        if (tassallBh > 0 && ccallsBh > 0 && cmsestabBh > 0)
        {
            cssrbh = (tfcassallBh / tassallBh) * (1 - (ccongsBh / ccallsBh)) * (1 - ((cndropBh - cnrelcongBh) / cmsestabBh));
            cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
            if (cssrbh <0 || cssrbh > 1)
            {
                cssrbh = 0;
            }
        } else
        {
            cssrbh = 0;
        }

        csr = cssr * (1 - cdr);
        csr = (double) ((int) (csr * 10000)) / 10000;
        if (csr <0 || csr > 1)
        {
            csr = 0;
        }

        csrbh = cssrbh * (1 - cdrbh);
        csrbh = (double) ((int) (csrbh * 10000)) / 10000;
        if (csrbh< 0 || csrbh> 1)
        {
            csrbh = 0;
        }

        if (tassall != 0)
        {
            tchcr = (cnrelcong + tfnrelcong + thnrelcong + tfnrelcongsub + thnrelcongsub) / tassall;
            tchcr = (double) ((int) (tchcr * 10000)) / 10000;
            if (tchcr >= 0 && tchcr <= 1)
            {
                tchbr = tchcr;
            } else
            {
                tchcr = 1;
                tchbr = 1;
            }
        } else
        {
            tchbr = 1;
            tchcr = 1;
        }

        if (tassallBh != 0)
        {
            tchcrbh = (cnrelcongBh + tfnrelcongBh + thnrelcongBh + tfnrelcongsubBh + thnrelcongsubBh) / tassallBh;
            tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
            if (tchcrbh >= 0 && tchcrbh <= 1)
            {
                tchbrbh = tchcrbh;
            } else
            {
                tchcrbh = 1;
                tchbrbh = 1;
            }
        } else
        {
            tchbrbh = 1;
            tchcrbh = 1;
        }

        tchdr = cdr;
        if (tchdr >0 || tchdr > 1)
        {
            tchdr = 1;
        }

        tchdrbh = cdrbh;
        tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
        if (tchdrbh< 0 || tchdrbh>1)
        {
            tchdrbh = 1;
        }

        if (cmsestab != 0)
        {
            sdcchdr = (cndrop - cnrelcong) / cmsestab;
            sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
            if (sdcchdr < 0 || sdcchdr > 1)
            {
                sdcchdr = 1;
            }
        } else
        {
            sdcchdr= 1;
        }

        if (cmsestabBh != 0)
        {
            sdcchdrbh = (cndropBh - cnrelcongBh) / cmsestabBh;
            sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
            if (sdcchdrbh < 0 || sdcchdrbh > 1)
            {
                sdcchdrbh = 1;
            }
        } else
        {
            sdcchdrbh = 1;
        }

        if (ccalls != 0)
        {
            sdcchbr = ccongsBh / ccallsBh;
            sdcchbr = (double) ((int) (sdcchbr * 10000)) / 10000;
            sdcchcr = sdcchbr;
            if (sdcchbr < 0 || sdcchbr > 1)
            {
                sdcchbr = 1;
                sdcchcr = 1;
            }
        } else
        {
            sdcchbr = 1;
            sdcchcr = 1;
        }

        if (ccallsBh != 0)
        {
            sdcchbrbh = ccongsBh / ccallsBh;
            sdcchbrbh = (double) ((int) (sdcchbrbh * 10000)) / 10000;
            sdcchcrbh = sdcchbrbh;
            if (sdcchbrbh < 0 || sdcchbrbh > 1)
            {
                sdcchbrbh = 1;
                sdcchcrbh = 1;
            }
        } else
        {
            sdcchbrbh = 1;
            sdcchcrbh = 1;
        }

        Rsmsr = 0;
        Rsmsr = (double) ((int) (Rsmsr * 10000)) / 10000;
        if (Rsmsr < 0 || Rsmsr > 1)
        {
            Rsmsr = 0;
        }

        if (Hovercnt != 0)
        {
            hosucces = Hoversuc / Hovercnt;
            hosucces = (double) ((int) (hosucces * 10000)) / 10000;
            if (hosucces < 0 || hosucces > 1)
            {
                hosucces= 0;
            }
        } else if (Hoversuc == 0 && Hovercnt == 0)
        {
            hosucces = 1;
        } else
        {
            hosucces = 0;
        }

        if (hovercntBh != 0)
        {
            hosuccesbh = hoversucBh / hovercntBh;
            hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
            if (hosuccesbh < 0 || hosuccesbh > 1)
            {
                hosuccesbh= 0;
            }
        } else if (hoversucBh== 0 && hovercntBh== 0)
        {
            hosuccesbh = 1;
        } else
        {
            hosuccesbh = 0;
        }
        
        Object obj=mapTraficBhRegion.get("Global");
        Object obj1=mapTrHoraireMoyenRegion.get("Global");

        if(obj!=null && obj1!=null)
        {
            tchhm = Double.parseDouble(obj1.toString());
            double val=Double.parseDouble(obj.toString());
            if (val > 0)
            {
                bhtr = tchmbh / tchm;
            } else
            {
                bhtr = 1;
            }
            bhtr=(double)((int)(bhtr*10000))/10000;
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
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void CalculCellule(String cellule)
    {
        try
        {
            tchm=0;tchmbh=0;tchcr=0;tchcrbh=0;
            tchdr=0;tchdrbh=0;tchbr=0;tchbrbh=0;
            cssr=0;sdcchcrbh=0; sdcchdrbh=0;sdcchbrbh=0;
            sdcchcr=0; sdcchdr=0;sdcchbr=0;
            cdr=0;cdrbh=0;csr=0; csrbh=0;Rsmsr=0;
            double Hoversuc=0,Hovercnt=0;hosucces=0;
            
            double FullRate=0,HalfRate=0;
            
            //String requete ="select * from tableregistre where trim(cell_name)='"+cellule+"' and date>'"+dateDebut+"' and date<='"+dateFin+"' ";
            double thnrelcong=0,tfnrelcong=0,thnrelcongsub=0,tfnrelcongsub=0,thndropsub=0,tfcassallsub=0,thcassall=0,thcassallsub=0,thndrop=0,tfcassall=0,tftralacc=0,thtralacc=0,tfndropsub=0,tfnscan=0,thnscan=0,
                    cnrelcong=0,tassall=0,ccongs=0,ccalls=0,cmsestab=0,hovercnt=0,hoversuc=0,tfndrop=0,cndrop=0;
            double  thnrelcongBh=0,tfnrelcongBh=0,thnrelcongsubBh=0,tfnrelcongsubBh=0,thndropsubBh=0,tfcassallsubBh=0,thcassallBh=0,thcassallsubBh=0,thndropBh=0,tfcassallBh=0,tftralaccBh=0,thtralaccBh=0,tfndropsubBh=0,
                    tfnscanBh=0,thnscanBh=0,cnrelcongBh=0,tassallBh=0,ccongsBh=0,ccallsBh=0,cmsestabBh=0,hovercntBh=0,hoversucBh=0,tfndropBh=0,
                    cndropBh=0;
           String requete ="select cell_name,heure,sum(tfnscan) as tfnscan,sum(thnscan) as thnscan,sum(tftralacc) as tftralacc,sum(thtralacc) as thtralacc,sum(cmsestab) as cmsestab,"
                    + " sum(tassall) as tassall,sum(cnrelcong) as cnrelcong, sum(cndrop) as cndrop,sum(tfcassall) as tfcassall,sum(tfndrop) as tfndrop,sum(tfndropsub) as tfndropsub,"
                    + " sum(ccongs) as ccongs, sum(ccalls) as ccalls,sum(hoversuc) as hoversuc,sum(hovercnt) as hovercnt,sum(tfnrelcong) as tfnrelcong,sum(thnrelcong) as thnrelcong,"
                    + " sum(tfnrelcongsub) as tfnrelcongsub,sum(thnrelcongsub) as thnrelcongsub, "
                    + " sum(thndrop) as thndrop,sum(thcassall) as thcassall,sum(thcassallsub) as thcassallsub, sum(tfcassallsub) as tfcassallsub, sum(thndropsub) as thndropsub"
                    + " from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"' and cell_name='"+cellule+"' "
                    + "group by cell_name,heure order by cell_name,heure ; ";
            ResultSet result=null;
            if(mapBhCellule.get(cellule)!=null)
            {
                BhCellule=mapBhCellule.get(cellule);
            }else BhCellule=-1;
            result = cn.getResultset(requete);
            while (result.next())
            {
                tfcassall=tfcassall+result.getFloat("tfcassall");
                tfnscan=tfnscan+result.getFloat("tfnscan");
                thnscan=thnscan+result.getFloat("thnscan");
                tassall=tassall+result.getFloat("tassall");
                ccalls=ccalls+result.getFloat("ccalls");
                ccongs=ccongs+result.getFloat("ccongs");
                cnrelcong=cnrelcong+result.getFloat("cnrelcong");
                cndrop=cndrop+result.getFloat("cndrop");
                cmsestab=cmsestab+result.getFloat("cmsestab");
                tftralacc=tftralacc+result.getFloat("tftralacc");
                thtralacc=thtralacc+result.getFloat("thtralacc");

                tfndrop=tfndrop+result.getFloat("tfndrop");
                tfndropsub=tfndropsub+result.getFloat("tfndropsub");
                thndrop=thndrop+result.getFloat("thndrop");
                thcassall=thcassall+result.getFloat("thcassall");
                thcassallsub=thcassallsub+result.getFloat("thcassallsub");
                tfcassallsub=tfcassallsub+result.getFloat("tfcassallsub");
                thndropsub=thndropsub+result.getFloat("thndropsub");
                thnrelcongsub=thnrelcongsub+result.getFloat("thnrelcongsub");
                tfnrelcongsub=tfnrelcongsub+result.getFloat("tfnrelcongsub");
                thnrelcong=thnrelcong+result.getFloat("thnrelcong");
                tfnrelcong=tfnrelcong+result.getFloat("tfnrelcong");
                
                FullRate=FullRate+result.getFloat("tfnscan");
                HalfRate=HalfRate+result.getFloat("thnscan");

                Hoversuc = Hoversuc+result.getFloat("hoversuc");
                Hovercnt = Hovercnt+result.getFloat("hovercnt");

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
                    tfcassallBh = result.getFloat("tfcassall");
                    tfnscanBh = result.getFloat("tfnscan");
                    thnscanBh = result.getFloat("thnscan");
                    tassallBh = result.getFloat("tassall");
                    ccallsBh = result.getFloat("ccalls");
                    ccongsBh = result.getFloat("ccongs");
                    cnrelcongBh =result.getFloat("cnrelcong");
                    cndropBh = result.getFloat("cndrop");
                    cmsestabBh = result.getFloat("cmsestab");
                    tftralaccBh=result.getFloat("tftralacc");
                    thtralaccBh=result.getFloat("thtralacc");
                    tfndropBh=result.getFloat("tfndrop");
                    tfndropsubBh =result.getFloat("tfndropsub");
                    thndropBh = result.getFloat("thndrop");
                    thcassallBh =result.getFloat("thcassall");
                    thcassallsubBh = result.getFloat("thcassallsub");
                    tfcassallsubBh =result.getFloat("tfcassallsub");
                    thndropsubBh=result.getFloat("thndropsub");
                    thnrelcongsubBh = result.getFloat("thnrelcongsub");
                    tfnrelcongsubBh = result.getFloat("tfnrelcongsub");
                    thnrelcongBh =result.getFloat("thnrelcong");
                    tfnrelcongBh =result.getFloat("tfnrelcong");
                }
            }
            
            if(tfnscan>0 && thnscan>0)
            {
                tchm=(tftralacc/360)+ (thtralacc/360);
                tchm=(double)((int)(tchm*10000))/10000;
            }
            else
            {
                tchm = 0;
            }

            if(tfnscanBh>0 && thnscanBh>0)
            {
                tchmbh=(tftralaccBh/360)+ (thtralaccBh/360);
                tchmbh=(double)((int)(tchmbh*10000))/10000;
            }
            else
            {
                tchmbh = 0;
            }

            if((thcassall+thcassallsub+tfcassall+tfcassallsub)>0)
            {
                cdr=(tfndrop+tfndropsub+thndrop+thndropsub)/(thcassall+thcassallsub+tfcassall+tfcassallsub);
                cdr=(double)((int)(cdr*10000))/10000;
                if(cdr<0 || cdr>1)
                {
                    cdr=1;
                }
            }else
            {
                cdr=1;
            }

            if((thcassallBh+thcassallsubBh+tfcassallBh+tfcassallsubBh)>0)
            {
                cdrbh=(tfndropBh+tfndropsubBh+thndropBh+thndropsubBh)/(thcassallBh+thcassallsubBh+tfcassallBh+tfcassallsubBh);
                cdrbh=(double)((int)(cdr*10000))/10000;
                if(cdrbh<0 || cdrbh>1)
                {
                    cdrbh=1;
                }
            }else
            {
                cdrbh=1;
            }

            if(tassall>0 && ccalls>0 && cmsestab>0)
            {
                cssr=(tfcassall/tassall)*(1-(ccongs/ccalls))*(1-((cndrop-cnrelcong)/cmsestab));
                cssr=(double)((int)(cssr*10000))/10000;
                if(cssr<0 || cssr>1)
                {
                    cssr = 0;
                }
            }
            else
            {
                cssr = 0;
            }

            cssrbh=0;
            if(tassallBh>0 && ccallsBh>0 && cmsestabBh>0)
            {
                cssrbh=(tfcassallBh/tassallBh)*(1-(ccongsBh/ccallsBh))*(1-((cndropBh-cnrelcongBh)/cmsestabBh));
                cssrbh=(double)((int)(cssrbh*10000))/10000;
                if(cssrbh<0 || cssrbh>1)
                {
                    cssrbh = 0;
                } 
            }
            else cssrbh = 0;

            csr = cssr*(1-cdr);
            csr = (double) ((int) (csr * 10000)) / 10000;
            if (csr <0 || csr >1)
            {
                csr = 0;
            }

            csrbh = cssrbh*(1-cdrbh);
            csrbh = (double) ((int) (csrbh * 10000)) / 10000;
            if (csrbh< 0 || csrbh >1)
            {
                csrbh=0;
            }

            if(tassall!=0)
            {
                tchcr=(cnrelcong+tfnrelcong+thnrelcong+tfnrelcongsub+thnrelcongsub)/tassall;
                tchcr=(double)((int)(tchcr*10000))/10000;
                if(tchcr>=0 && tchcr<=1)
                {
                    tchbr = tchcr;
                }else
                {
                    tchcr = 1;
                    tchbr=1;
                }
            }
            else
            {
                tchbr=1;
                tchcr = 1;
            }

            if(tassallBh!=0)
            {
                tchcrbh=(cnrelcongBh+tfnrelcongBh+thnrelcongBh+tfnrelcongsubBh+thnrelcongsubBh)/tassallBh;
                tchcrbh=(double)((int)(tchcrbh*10000))/10000;
                if(tchcrbh>=0 && tchcrbh<=1)
                {
                    tchbrbh = tchcrbh;
                }else
                {
                    tchcrbh = 1;
                    tchbrbh=1;
                }
            }
            else
            {
                tchbrbh=1;
                tchcrbh = 1;
            }

            tchdr = cdr;
            if (tchdr >= 0 && tchdr <= 1) {
            } else {
                tchdr = 1;
            }

            tchdrbh = cdrbh;
            tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
            if (tchdrbh >= 0 && tchdrbh <= 1) {
            } else {
                tchdrbh = 1;
            }

            if (cmsestab != 0)
            {
                sdcchdr = (cndrop - cnrelcong) / cmsestab;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr < 0 || sdcchdr > 1)
                {
                    sdcchdr = 1;
                }
            } else
            {
                sdcchdr = 1;
            }

            if (cmsestabBh != 0)
            {
                sdcchdrbh = (cndropBh - cnrelcongBh) / cmsestabBh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh < 0 || sdcchdrbh > 1)
                {
                    sdcchdrbh = 1;
                }
            } else
            {
                sdcchdrbh = 1;
            }

            if (ccalls != 0)
            {
                sdcchbr = ccongs / ccalls;
                sdcchbr = (double) ((int) (sdcchbr * 10000)) / 10000;
                sdcchcr = sdcchbr;
                if (sdcchbr <0 || sdcchbr >1)
                {
                    sdcchbr = 1;
                    sdcchcr = 1;
                }
            } else
            {
                sdcchbr = 1;
                sdcchcr = 1;
            }

            if (ccallsBh != 0)
            {
                sdcchbrbh = ccongsBh / ccallsBh;
                sdcchbrbh = (double) ((int) (sdcchbrbh * 10000)) / 10000;
                sdcchcrbh = sdcchbrbh;
                if (sdcchbrbh <0 || sdcchbrbh >1)
                {
                    sdcchbrbh = 1;
                    sdcchcrbh = 1;
                }
            } else
            {
                sdcchbrbh = 1;
                sdcchcrbh = 1;
            }

            Rsmsr =0;
            Rsmsr = (double) ((int) (Rsmsr * 10000)) / 10000;
            if (Rsmsr < 0 || Rsmsr > 1) {
                Rsmsr = 0;
            }

            if(Hovercnt!=0)
            {
                hosucces=Hoversuc/Hovercnt;
                hosucces=(double)((int)(hosucces*10000))/10000;
                if(hosucces<0 ||  hosucces>1)
                {
                    hosucces = 0;
                }
            }
            else if(Hoversuc==0 && Hovercnt==0)
            {
                hosucces=1;
            }
            else
            {
                hosucces = 0;
            }
            
            if(hovercntBh!=0)
            {
                hosuccesbh=hoversucBh/hovercntBh;
                hosuccesbh=(double)((int)(hosuccesbh*10000))/10000;
                if(hosuccesbh<0 ||  hosuccesbh>1)
                {
                    hosuccesbh = 0;
                }
            }
            else if(hoversucBh==0 && hovercntBh==0)
            {
                hosuccesbh=1;
            }
            else
            {
                hosuccesbh = 0;
            }

            Object obj=mapBhCellule.get(cellule);
            Object obj1=mapTrHoraireMoyenCellule.get(cellule);
            Object obj2=mapTraficBhCellule.get(cellule);

            if(obj!=null && obj1!=null && obj2!=null)
            {
                tchhm=mapTrHoraireMoyenCellule.get(cellule);
                tchmbh=mapTraficBhCellule.get(cellule);
                if(tchm>0)
                {
                    bhtr=tchmbh/tchm;
                } else bhtr=1;
                bhtr=(double)((int)(bhtr*100))/100;
                InsertTableCellule(cellule);
            }
        }

        catch (SQLException ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getBHParCellule(String cellule)
    {
        double TabTchmCellule[]=new double[24];
        for (int i = 0; i < 24; i++)
        {
            TabTchmCellule[i]=0;
        }
        try
        {
            ResultSet resultTotal=null;
            int nbreJrsHrs=0;
            //String requete="select count(*) as nbre  from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"' and cell_name='"+cellule+"'";
            String requete="select count(*)*(select count(*) as nbre from (select distinct(heure) from tableregistre  "
                    + "where date>='"+dateDebut+"' and date<='"+dateFin+"' and cell_name='"+cellule+"' order by heure) query) as nbre from (select distinct(date) "
                    + "from tableregistre  where date>='"+dateDebut+"' and date<='"+dateFin+"' and cell_name='"+cellule+"') query";
            resultTotal=cn.getResultset(requete);
            if(resultTotal.next())
            {
                try
                {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                }
                catch (Exception ex)
                {
                    Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs=0;
                }
            }

            //requete = "select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select cell_name,heure,sum(tfnscan) as tfnscan ,sum(thnscan) as thnscan,sum(tftralacc) as tftralacc,sum(thtralacc) as thtralacc from tableregistre "
                    + "where cell_name='"+cellule+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'"
                    + " group by cell_name,heure  order by cell_name,heure";

            resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                double val1=0,val2=0;
                int heure=-1;
                String str=resultTotal.getString("heure").trim();
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {heure=-1;}


                if(heure>=0)
                {
                    TabTchmCellule[heure-1]=TabTchmCellule[heure-1]+(resultTotal.getFloat("tftralacc")/360)+(resultTotal.getFloat("thtralacc")/360);
                }
            }

            /***Calcul de Tchm & Bh par region ***/
            //requete = "select distinct(date) from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            int n=TabTchmCellule.length;
            double TchmBhCellule=0;BhCellule=-1;
            double som=0;
            for(int i=0;i<n; i++)
            {
                som=som+TabTchmCellule[i];
                if(TabTchmCellule[i]>TchmBhCellule)
                {
                    TchmBhCellule=TabTchmCellule[i];
                    BhCellule=i+1;
                }
            }
            if(nbreJrsHrs>0)
                som=som/(double)(nbreJrsHrs);
            else som=0;
            som=(double)((int)(som*10000))/10000;
            TchmBhCellule=(double)((int)(TchmBhCellule*10000))/10000;
            if(BhCellule>=0)
            {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhCellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            }else System.out.println("************************************************************:Cellule:"+cellule+" ,BHfausse:"+BhCellule);
        } catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void InsertTableCellule(String cellule)
    {
        String requete="select * from table_bts where trim(cell_name)='"+cellule.trim()+"'";
        try
        {
            idinfo++;
            ResultSet resultSet = cn.getResultset(requete);
            if(resultSet.next())
            {
                requete = "insert into table_valeurs_bts(region,cell_name,id_pkm,bhtr,tchm,tchmbh,tchhm,cssr,cssrbh,tchcr,tchbr,tchcrbh,tchbrbh,tchdr,tchdrbh,sdcchcr,sdcchcrbh,sdcchbr,sdcchbrbh,sdcchdr,sdcchdrbh,cdr,cdrbh,csr,csrbh,bh,hosucces,hosuccesbh,the_geom) values(";
                requete = requete +"'"+resultSet.getString("region") + "',";
                requete = requete +"'"+cellule + "',";
                requete = requete + idinfo+",";
                requete = requete + bhtr + ",";
                requete = requete + tchm + ",";
                requete = requete + tchmbh + ",";
                requete = requete + tchhm + ",";
                requete = requete + cssr + ",";
                requete = requete + cssrbh + ",";
                requete = requete + tchcr + ",";
                requete = requete + tchbr + ",";
                requete = requete + tchcrbh + ",";
                requete = requete + tchbrbh + ",";
                requete = requete + tchdr + ",";
                requete = requete + tchdrbh + ",";
                requete = requete + sdcchcr + ",";
                requete = requete + sdcchcrbh + ",";
                requete = requete + sdcchbr + ",";
                requete = requete + sdcchbrbh + ",";
                requete = requete + sdcchdr + ",";
                requete = requete + sdcchdrbh + ",";
                requete = requete + cdr + ",";
                requete = requete + cdrbh + ",";
                requete = requete + csr + ",";
                requete = requete + csrbh + ",";
                requete = requete + BhCellule + ",";
                requete = requete +hosucces+",";
                requete = requete +hosuccesbh+",";
                requete = requete +"'"+resultSet.getObject("the_geom").toString()+"'";
                requete = requete + ")";
                cn.ExecuterRequete(requete);
                System.out.println("valeur requete:" + requete);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void InsertTableRegion(String region)
    {
        String requete="select * from table_regions where trim(region)='"+region.trim()+"'";
        try
        {
            idinfo++;
            ResultSet resultSet = cn.getResultset(requete);
            if(resultSet.next())
            {
                requete = "insert into table_valeurs_regions(region,gid,bhtr,tchm,tchmbh,tchhm,cssr,cssrbh,tchcr,tchbr,tchcrbh,tchbrbh,tchdr,tchdrbh,sdcchcr,sdcchcrbh,sdcchdr,sdcchdrbh,sdcchbr,sdcchbrbh,cdr,cdrbh,csr,csrbh,bh,hosucces,hosuccesbh,the_geom) values(";
                requete = requete +"'"+ region + "',";
                requete = requete + idinfo + ",";
                requete = requete + bhtr + ",";
                requete = requete + tchm + ",";
                requete = requete + tchmbh + ",";
                requete = requete + tchhm + ",";
                requete = requete + cssr + ",";
                requete = requete + cssrbh + ",";
                requete = requete + tchcr + ",";
                requete = requete + tchbr + ",";
                requete = requete + tchcrbh + ",";
                requete = requete + tchbrbh + ",";
                requete = requete + tchdr + ",";
                requete = requete + tchdrbh + ",";
                requete = requete + sdcchcr + ",";
                requete = requete + sdcchcrbh + ",";
                requete = requete + sdcchdr + ",";
                requete = requete + sdcchdrbh + ",";
                requete = requete + sdcchbr + ",";
                requete = requete + sdcchbrbh + ",";
                requete = requete + cdr + ",";
                requete = requete + cdrbh + ",";
                requete = requete + csr + ",";
                requete = requete + csrbh + ",";
                requete = requete + BhRegion + ",";
                requete = requete + hosucces+",";
                requete = requete + "'"+resultSet.getObject("the_geom").toString()+"'";
                requete = requete + ")";
                System.out.println("valeur requete:" + requete);
                cn.ExecuterRequete(requete);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            idinfo++;
            requete = "insert into tablevaleurskpi(region,coderegion,bhtr,tchm,tchmbh,tchhm,cssr,cssrbh,tchcr,tchbr,tchcrbh,tchbrbh,tchdr,tchdrbh,sdcchcr,sdcchcrbh,sdcchdr,sdcchdrbh,sdcchbr,sdcchbrbh,cdr,cdrbh,csr,csrbh,bh,hosucces,hosuccesbh) values(";
            requete = requete + "'" + region + "',";
            requete = requete + idinfo + ",";
            requete = requete + bhtr + ",";
            requete = requete + tchm + ",";
            requete = requete + tchmbh + ",";
            requete = requete + tchhm + ",";
            requete = requete + cssr + ",";
            requete = requete + cssrbh + ",";
            requete = requete + tchcr + ",";
            requete = requete + tchbr + ",";
            requete = requete + tchcrbh + ",";
            requete = requete + tchbrbh + ",";
            requete = requete + tchdr + ",";
            requete = requete + tchdrbh + ",";
            requete = requete + sdcchcr + ",";
            requete = requete + sdcchcrbh + ",";
            requete = requete + sdcchdr + ",";
            requete = requete + sdcchdrbh + ",";
            requete = requete + sdcchbr + ",";
            requete = requete + sdcchbrbh + ",";
            requete = requete + cdr + ",";
            requete = requete + cdrbh + ",";
            requete = requete + csr + ",";
            requete = requete + csrbh + ",";
            requete = requete + BhRegion + ",";
            requete = requete + hosucces+",";
            requete = requete + hosuccesbh;
            requete = requete + ")";
            System.out.println("valeur requete:" + requete);
            cn.ExecuterRequete(requete);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalCulEricsson3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        cn.closeConnection();
    }
    
}
