
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

public class CalculHuawei3G
{

    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double tchbr = 0, tchbrbh = 0;
    private double tchm = 0;
    private double tchhm = 0;
    private double tchmbh = 0;
    private double[] TabTchmGlobal = new double[24];
    private double cssr = 0,cssrbh = 0;
    private double bhtr = 0;
    private double tchcr = 0;
    private double tchcrbh = 0;
    private double tchdr = 0;
    private double tchdrbh = 0;
    private double sdcchbrbh = 0,sdcchbr = 0;
    private double sdcchcrbh = 0,sdcchcr = 0;
    private double sdcchdrbh = 0,sdcchdr = 0;
    private double csr = 0,csrbh = 0;
    private double cdr = 0;
    private double cdrbh = 0;
    private double hosucces = 0, hosuccesbh = 0;
    private double HoFailureBh = 0;
    private double TchmBhGlobal = 0, TchmBhRegion = 0;
    private int BhG = -1, BhRegion = -1, BhCellule = -1;
    private Map<String, Double> mapTraficBhRegion;
    private Map<String, Integer> mapBhRegion;
    private Map<String, Double> mapTrHoraireMoyenRegion;
    private Map<String, Double> mapTraficBhCellule = null;
    private Map<String, Integer> mapBhCellule = null;
    private Map<String, Double> mapTrHoraireMoyenCellule = null;
    private int[] TabBhGlobal = new int[24];
    private int idinfo = 0;
    private double Rsmsr=0;

    public CalculHuawei3G(Operateur operateur, String datedebut, String datefin) throws SQLException
    {
        dateDebut = datedebut;
        dateFin = datefin;

        cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
    }

     private void CalculRegion(String region)
     {
        try
        {
            ////calcul du nombre d'enregistrement reels
            int NbreErgReel = 0, NbreErgRecus = 0;
            String requete = "select count(cell_name)*24*(SELECT DATE_PART('day', '" + dateFin + " 01:00:00'::timestamp - '" + dateDebut + " 00:00:00'::timestamp)+1) as nbre "
                    + "from table_bts "
                    + "where region='" + region + "' group by region";
            System.out.println("Requete nombre dee jour:" + requete);
            ResultSet resultSet = cn.getResultset(requete);
            if (resultSet.next())
            {
                NbreErgReel = resultSet.getInt("nbre");
            }

            ResultSet result = null;
            if (mapBhRegion.get(region) != null)
            {
                BhRegion = mapBhRegion.get(region);
            } else
            {
                BhRegion = -1;
            }
            requete = "select count(*)as nbre from tableregistre where trim(region)='" + region + "' and date>='" + dateDebut + "' and date<='" + dateFin + "' ";
            result = cn.getResultset(requete);
            try
            {
                if (result.next())
                {
                    NbreErgRecus = result.getInt("nbre");
                }
            } catch (Exception ex)
            {
                Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
                NbreErgRecus = 0;
            }

            /////////////////
            tchm = 0;tchmbh = 0;tchcr = 0;
            tchdr = 0;tchbr = 0;cssr = 0;
            cssrbh = 0;cdr = 0;csr = 0;tchcrbh = 0;
            tchdrbh = 0;tchbrbh = 0;hosucces = 0;
            sdcchcrbh = 0;sdcchbrbh = 0;sdcchdrbh = 0;
            cdrbh = 0;csrbh = 0;Rsmsr = 0;
            sdcchcr=0; sdcchdr=0; sdcchbr=0;hosucces=0;hosuccesbh=0; HoFailureBh=0;
            double FullRate = 0, HalfRate = 0;

            double ZCM30 = 0, ZK3000 = 0, ZK3001 = 0, ZK3003 = 0, ZTR103A = 0, ZTR104A = 0, CH310 = 0, CH313 = 0, CH323 = 0, CH330 = 0, CH333 = 0, CH343 = 0,
                    CM30C = 0, CM33 = 0, CR3553 = 0, CR3554 = 0, RH303 = 0, CR4110 = 0, CR4119 = 0, R3550N = 0, R3590 = 0, K3010A = 0, K3010B = 0, K3011A = 0,
                    K3011B = 0, K3013A = 0, K3020 = 0, K3021 = 0, K3045 = 0;
            double ZCM30Bh = 0, ZK3000Bh = 0, ZK3001Bh = 0, ZK3003Bh = 0, ZTR103ABh = 0, ZTR104ABh = 0, CH310Bh = 0, CH313Bh = 0, CH323Bh = 0, CH330Bh = 0, CH333Bh = 0, CH343Bh = 0,
                    CM30CBh = 0, CM33Bh = 0, CR3553Bh = 0, CR3554Bh = 0, RH303Bh = 0, CR4110Bh = 0, CR4119Bh = 0, R3550NBh = 0, R3590Bh = 0, K3010ABh = 0, K3010BBh = 0, K3011ABh = 0,
                    K3011BBh = 0, K3013ABh = 0, K3020Bh = 0, K3021Bh = 0, K3045Bh = 0;

            System.out.println("----------------------**********//////////////////////    Region en cour de calcul:" + region);

            requete = "select region,heure,sum(zcm30) as zcm30,sum(zk3000) as zk3000,sum(zk3001) as zk3001,sum(zk3003) as zk3003,sum(ztr103a) as ztr103a,"
                    + " sum(ztr104a) as ztr104a,sum(ch310) as ch310, sum(ch313) as ch313,sum(ch323) as ch323,sum(ch330) as ch330,sum(ch333) as ch333,"
                    + " sum(ch343) as ch343, sum(cm30c) as cm30c,sum(cm33) as cm33,sum(cr3553) as cr3553,sum(cr3554) as cr3554,sum(rh303) as rh303,"
                    + " sum(cr4110) as cr4110,sum(cr4119) as cr4119, "
                    + " sum(r3550n) as r3550n,sum(r3590) as r3590,sum(k3010a) as k3010a, sum(k3010b) as k3010b, sum(k3011a) as k3011a"
                    + " sum(k3011b) as k3011b,sum(k3013a) as k3013a,sum(k3020) as k3020, sum(k3021) as k3021, sum(k3045) as k3045"
                    + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "' "
                    + "group by region,heure order by region,heure ; ";

            System.out.println("---------------------------------------------------requte:" + requete);
            result = cn.getResultset(requete);
            while (result.next())
            {
                ZCM30 = ZCM30 + result.getFloat("zcm30");
                ZK3000 = ZK3000 + result.getFloat("zk3000");
                ZK3001 = ZK3001 + result.getFloat("zk3001");
                ZK3003 = ZK3003 + result.getFloat("zk3003");
                ZTR103A = ZTR103A + result.getFloat("ztr103a");
                ZTR104A = ZTR104A + result.getFloat("ztr104a");
                CH310 = CH310 + result.getFloat("ch310");
                CH313 = CH313 + result.getFloat("ch313");
                CH323 = CH323 + result.getFloat("ch323");
                CH330 = CH330 + result.getFloat("ch330");
                CH333 = CH333 + result.getFloat("ch333");

                CH343 = CH343 + result.getFloat("ch343");
                CM30C = CM30C + result.getFloat("cm30c");
                CM33 = CM33 + result.getFloat("cm33");
                CR3553 = CR3553 + result.getFloat("cr3553");
                CR3554 = CR3554 + result.getFloat("cr3554");
                RH303 = RH303 + result.getFloat("rh303");
                CR4110 = CR4110 + result.getFloat("cr4110");
                CR4119 = CR4119 + result.getFloat("cr4119");
                R3550N = R3550N + result.getFloat("r3550n");
                R3590 = R3590 + result.getFloat("r3590");
                K3010A = K3010A + result.getFloat("k3010a");

                K3010B = K3010B + result.getFloat("k3010b");
                K3011A = K3011A + result.getFloat("k3011a");
                K3011B = K3011B + result.getFloat("k3011b");
                K3013A = K3013A + result.getFloat("k3013a");
                K3020 = K3020 + result.getFloat("k3020");
                K3021 = K3021 + result.getFloat("k3021");
                K3045 = K3045 + result.getFloat("k3045");

                String str = result.getString("heure").trim();
                int heure = -1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex)
                {
                    heure = -1;
                }
                if (heure == BhRegion && heure >= 0)
                {
                    ZCM30Bh = result.getFloat("zcm30");
                    ZK3000Bh = result.getFloat("zk3000");
                    ZK3001Bh = result.getFloat("zk3001");
                    ZK3003Bh = result.getFloat("zk3003");
                    ZTR103ABh = result.getFloat("ztr103a");
                    ZTR104ABh = result.getFloat("ztr104a");
                    CH310Bh = result.getFloat("ch310");
                    CH313Bh = result.getFloat("ch313");
                    CH323Bh = result.getFloat("ch323");
                    CH330Bh = result.getFloat("ch330");
                    CH333Bh = result.getFloat("ch333");

                    CH343Bh = result.getFloat("ch343");
                    CM30CBh = result.getFloat("cm30c");
                    CM33Bh = result.getFloat("cm33");
                    CR3553Bh = result.getFloat("cr3553");
                    CR3554Bh = result.getFloat("cr3554");
                    RH303Bh = result.getFloat("rh303");
                    CR4110Bh = result.getFloat("cr4110");
                    CR4119Bh = result.getFloat("cr4119");
                    R3550NBh = result.getFloat("r3550n");
                    R3590Bh = result.getFloat("r3590");
                    K3010ABh = result.getFloat("k3010a");

                    K3010BBh = result.getFloat("k3010b");
                    K3011ABh = result.getFloat("k3011a");
                    K3011BBh = result.getFloat("k3011b");
                    K3013ABh = result.getFloat("k3013a");
                    K3020Bh = result.getFloat("k3020");
                    K3021Bh = result.getFloat("k3021");
                    K3045Bh = result.getFloat("k3045");

                }
            }
            float TauxInfoManq = 0;
            if (NbreErgReel > 0)
            {
                System.out.println("Region:" + region + " Bh:" + BhRegion);
                System.out.println("Nbre enrg reçu:" + NbreErgRecus);
                System.out.println("Nbre enrg reels:" + NbreErgReel);
                TauxInfoManq = 1 - (float) ((float) NbreErgRecus / (float) (NbreErgReel));
                TauxInfoManq = ((float) ((int) (TauxInfoManq * 10000)) / 10000) * 100;
            }
            Map<String, Float> MapTauxInfoManqRegion = new HashMap<String, Float>();

            //////calcul du TCHM
            if (R3590 > 0) {
                tchm = (CR3553 + CR3554) / R3590;
                tchm = (double) ((int) (tchm * 10000)) / 10000;
                MapTauxInfoManqRegion.put("tchm", TauxInfoManq);
            }
            else
            {
                tchm = 0;
                MapTauxInfoManqRegion.put("tchm", 100f);
            }

            /// calcul de TCHMBH
            if (R3590Bh > 0)
            {
                tchmbh = (CR3553Bh + CR3554Bh) / R3590Bh;
                tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;
                MapTauxInfoManqRegion.put("tchmbh", TauxInfoManq);
            }
            else
            {
                tchm = 0;
                MapTauxInfoManqRegion.put("tchmbh", 100F);
            }

            /////// calcul de CSSR
           /* if (ZK3003>0)
            {
            cssr= (1-(ZCM30*100/ZK3003))*ztr102a*100;
            cssr= (double) ((int) (cssr * 10000)) / 10000;
            MapTauxInfoManqRegion.put("cssr", TauxInfoManq);
            }
            else
            {
            cssr=0;
            MapTauxInfoManqRegion.put("cssr",100F);
            }
             */

            /////// calcul de CSSRBH
           /* if (ZK3003Bh>0)
            {
            cssrbh= (1-(ZCM30Bh*100/ZK3003Bh))*ztr102aBh*100;
            cssrbh= (double) ((int) (cssrbh * 10000)) / 10000;
            MapTauxInfoManqRegion.put("cssrbh", TauxInfoManq);
            }
            else
            {
            cssrbh=0;
            MapTauxInfoManqRegion.put("cssrbh",100F);
            }
             */
            //////// calcul de CDR
            if (K3013A + CH323 + CH343 - CH313 - CH333 > 0)
            {
                cdr = CM33 / (K3013A + CH323 + CH343 - CH313 - CH333);
                cdr = (double) ((int) (cdr * 10000)) / 10000;
                if (cdr >= 0 && cdr <= 1)
                {
                    MapTauxInfoManqRegion.put("cdr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("cdr", 100F);
                }
            }
            else
            {
                cdr = 1;
                MapTauxInfoManqRegion.put("cdr", 100F);
            }

            //////////// calcul CDRBH
            if (K3013ABh + CH323Bh + CH343Bh - CH313Bh - CH333Bh > 0)
            {
                cdrbh = CM33Bh / (K3013ABh + CH323Bh + CH343Bh - CH313Bh - CH333Bh);
                cdrbh = (double) ((int) (cdrbh * 10000)) / 10000;
                if (cdrbh >= 0 && cdrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("cdrbh", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("cdrbh", 100F);
                }
            }
            else
            {
                cdrbh = 1;
                MapTauxInfoManqRegion.put("cdrbh", 100F);
            }

            ////// calcul TCHCR
            if (K3020 + K3010A + K3010B > 0)
            {
                tchcr = (K3021 + K3011A + K3011B) / (K3020 + K3010A + K3010B);
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
                if (tchcr >= 0 && tchcr <= 1) {
                    MapTauxInfoManqRegion.put("tchcr", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("tchcr", 100F);
                }
            }
            else
            {
                tchcr = 1;
                MapTauxInfoManqRegion.put("tchcr", 100F);
            }

            ////// calcul TCHCRBH
            if (K3020Bh + K3010ABh + K3010BBh > 0)
            {
                tchcrbh = (K3021Bh + K3011ABh + K3011BBh) / (K3020Bh + K3010ABh + K3010BBh);
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
                if (tchcrbh >= 0 && tchcrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("tchcrbh", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("tchcrbh", 100F);
                }
            }
            else
            {
                tchcrbh = 1;
                MapTauxInfoManqRegion.put("tchcrbh", 100F);
            }

            ////////calcul de SDCCHCR
            if (ZK3000 > 0)
            {
                sdcchcr = ZK3001 / ZK3000;
                sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
                if (sdcchcr <= 1 && sdcchcr >= 0)
                {
                    MapTauxInfoManqRegion.put("sdcchcr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("sdcchcr", 100F);
                }
            }
            else
            {
                sdcchcr = 1;
                MapTauxInfoManqRegion.put("sdcchcr", 100F);
            }

            ////////calcul de SDCCHCRBH
            if (ZK3000Bh > 0)
            {
                sdcchcrbh = ZK3001Bh / ZK3000Bh;
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
                if (sdcchcrbh <= 1 && sdcchcrbh >= 0) {
                    MapTauxInfoManqRegion.put("sdcchcrbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
                }
            }
            else
            {
                sdcchcrbh = 1;
                MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
            }

            /////////// calcul de SDCCHDR
            if (ZK3003 > 0)
            {
                sdcchdr = ZCM30 / ZK3003;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr <= 1 && sdcchdr >= 0) {
                    MapTauxInfoManqRegion.put("sdcchdr", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchdr", 100F);
                }
            }
            else
            {
                sdcchdr = 1;
                MapTauxInfoManqRegion.put("sdcchdr", 100F);
            }

            /////////// calcul de SDCCHDRBH
            if (ZK3003Bh > 0)
            {
                sdcchdrbh = ZCM30Bh / ZK3003Bh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh <= 1 && sdcchdrbh >= 0) {
                    MapTauxInfoManqRegion.put("sdcchdrbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
                }
            }
            else
            {
                sdcchdrbh = 1;
                MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
            }

            ////////////// Calcul de HOSucces
            if (CH310 + CH330 > 0)
            {
                hosucces = (CH313 + CH333) / (CH310 + CH330);
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
                if (hosucces <= 1 && hosucces >= 0) {
                    MapTauxInfoManqRegion.put("hosucces", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("HoSucces", 100F);
                }
            }
            else
            {
                hosucces = 0;
                MapTauxInfoManqRegion.put("hosucces", 100F);
            }

            ////////////// Calcul de HOSuccesBh
            if (CH310Bh + CH330Bh > 0)
            {
                hosuccesbh = (CH313Bh + CH333Bh) / (CH310Bh + CH330Bh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
                if (hosuccesbh <= 1 && hosuccesbh >= 0) {
                    MapTauxInfoManqRegion.put("hosuccesbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sosuccesbh", 100F);
                }
            }
            else
            {
                hosuccesbh = 0;
                MapTauxInfoManqRegion.put("HoSuccesBh", 100F);
            }

            ////////////// Calcul de HOFailureBh
            HoFailureBh=1-hosuccesbh;
            HoFailureBh = (double) ((int) (HoFailureBh * 10000)) / 10000;

            Object obj = mapBhRegion.get(region);
            Object obj1 = mapTrHoraireMoyenRegion.get(region);
            Object obj2 = mapTraficBhRegion.get(region);

            if (obj != null && obj1 != null && obj2 != null)
            {
                tchhm = mapTrHoraireMoyenRegion.get(region);
                tchmbh = mapTraficBhRegion.get(region);
                if (tchm > 0)
                {
                    bhtr = tchmbh / tchm;
                }
                else
                {
                    bhtr = 1;
                }
                bhtr = (double) ((int) (bhtr * 10000)) / 10000;
                if (bhtr == 1)
                {
                    MapTauxInfoManqRegion.put("bhtr", 100F);
                }
                else
                {
                    MapTauxInfoManqRegion.put("bhtr", TauxInfoManq);
                }
                idinfo++;
                String requeteInsert = "insert into tauxinfo(idinfo,region,";
                String requeteValues = ") values(" + idinfo + ",'" + region + "',";
                for (Entry<String, Float> entry : MapTauxInfoManqRegion.entrySet())
                {
                    String cle = entry.getKey().toLowerCase();
                    float valeur = entry.getValue();

                    requeteInsert = requeteInsert + cle + ",";
                    requeteValues = requeteValues + "" + valeur + ",";
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
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getBhParRegion(String region)
    {
        int[] TabBhRegion = new int[24];
        double[] TabTchmRegion = new double[24];
        for (int i = 0; i < 24; i++) {
            TabBhRegion[i] = -1;
            TabTchmRegion[i] = 0;
        }
        ResultSet resultTotal = null;
        try
        {
            int nbreJrsHrs = 0;
            //String requete="select count(*) as nbre from (select distinct(date) from tableregistre  where date>='"+dateDebut+"' and date<='"+dateFin+"' and region='"+region+"') query";
            String requete = "select count(*)*(select count(*) as nbre from (select distinct(heure) from tableregistre  "
                    + "where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "' order by heure) query) as nbre from (select distinct(date) "
                    + "from tableregistre  where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "') query";
            resultTotal = cn.getResultset(requete);
            if (resultTotal.next())
            {
                try
                {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                }
                catch (Exception ex)
                {
                    Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs = 0;
                }
            }
            //String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select region,heure,sum(cr3553) as cr3553 ,sum(cr3554) as cr3554,sum(r3590) as r3590 from tableregistre "
                    + "where region='" + region + "' and date>='" + dateDebut + "' and date<='" + dateFin + "'"
                    + " group by region,heure  order by region,heure";
            resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                double val1 = 0;
                String str = resultTotal.getString("heure").trim();
                int heure = -1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                }
                catch (Exception ex)
                {
                    heure = -1;
                }
                try
                {
                    val1 = resultTotal.getFloat("r3590");
                } catch (Exception ex)
                {
                    val1 = 0;
                }

                if (val1 > 0 && heure >= 0)
                {
                    TabBhRegion[heure - 1] = heure;
                    TabTchmRegion[heure - 1] = TabTchmRegion[heure - 1] + ((resultTotal.getFloat("cr3553") + resultTotal.getFloat("cr3554")) / val1);

                    TabBhGlobal[heure - 1] = heure;
                    TabTchmGlobal[heure - 1] = TabTchmGlobal[heure - 1] + ((resultTotal.getFloat("cr3553") + resultTotal.getFloat("cr3554")) / val1);
                }
            }
            /***Calcul de Tchm & Bh par region ***/
            int n = TabTchmRegion.length;
            TchmBhRegion = 0;
            BhRegion = -1;
            double som = 0;
            for (int i = 0; i < n; i++)
            {
                som = som + TabTchmRegion[i];
                if (TabTchmRegion[i] > TchmBhRegion)
                {
                    TchmBhRegion = TabTchmRegion[i];
                    //BhRegion=TabBhRegion[i+1];
                    BhRegion = i + 1;
                }
            }

            if (nbreJrsHrs > 0)
            {
                som = som / (double) (nbreJrsHrs);
            }
            else
            {
                som = 0;
            }
            som = (double) ((int) (som * 100)) / 100;
            TchmBhRegion = (double) ((int) (TchmBhRegion * 100)) / 100;
            if (BhRegion >= 0)
            {
                mapBhRegion.put(region, BhRegion);
                mapTraficBhRegion.put(region, TchmBhRegion);
                mapTrHoraireMoyenRegion.put(region, som);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                resultTotal.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void CalCulBHParRegion(List<String> lstRegion)
    {
        for (int i = 0; i < 24; i++)
        {
            TabBhGlobal[i] = -1;
            TabTchmGlobal[i] = 0;
        }
        try
        {
            mapTraficBhRegion = new HashMap<String, Double>();
            mapBhRegion = new HashMap<String, Integer>();
            mapTrHoraireMoyenRegion = new HashMap<String, Double>();
            int nbreRegion = lstRegion.size();
            for (int i = 0; i < nbreRegion; i++)
            {
                String region = lstRegion.get(i);
                ////calcul de la Busy Hour par région
                getBhParRegion(region);
            }
            /***Calcul de Tchm & bh global de tout le réseau sur la période ****/
            int n = TabTchmGlobal.length;
            TchmBhGlobal = 0;
            BhG = -1;
            double som = 0;
            for (int i = 0; i < n; i++)
            {
                som = som + TabTchmGlobal[i];
                if (TabTchmGlobal[i] > TchmBhGlobal)
                {
                    TchmBhGlobal = TabTchmGlobal[i];
                    BhG = i + 1;
                }
            }
            String requete = "select count(*) as nbre from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "'";
            int nbreErg = cn.getNbreLigneResultset(requete);
            if (nbreErg > 0)
            {
                som = som / (double) (nbreErg);
            }
            else
            {
                som = 0;
            }
            som = (double) ((int) (som * 100)) / 100;
            TchmBhGlobal = (double) ((int) (TchmBhGlobal * 100)) / 100;
            if (BhG >= 0)
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

    private void getBHParCellule(String cellule)
    {
        int TabBhCellule[] = new int[24];
        double TabTchmCellule[] = new double[24];
        for (int i = 0; i < 24; i++) {
            TabBhCellule[i] = -1;
            TabTchmCellule[i] = 0;
        }
        try
        {
            ResultSet resultTotal = null;
            int nbreJrsHrs = 0;
            //String requete="select count(*) as nbre  from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"' and cell_name='"+cellule+"'";
            String requete = "select count(*)*(select count(*) as nbre from (select distinct(heure) from tableregistre  "
                    + "where date>='" + dateDebut + "' and date<='" + dateFin + "' and cell_name='" + cellule + "' order by heure) query) as nbre from (select distinct(date) "
                    + "from tableregistre  where date>='" + dateDebut + "' and date<='" + dateFin + "' and cell_name='" + cellule + "') query";
            resultTotal = cn.getResultset(requete);
            if (resultTotal.next())
            {
                try
                {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                }
                catch (Exception ex)
                {
                    Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs = 0;
                }
            }

            //requete = "select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select cell_name,heure,sum(cr3553) as cr3553 ,sum(cr3554) as cr3554,sum(r3590) as r3590 from tableregistre "
                    + "where cell_name='" + cellule + "' and date>='" + dateDebut + "' and date<='" + dateFin + "'"
                    + " group by cell_name,heure  order by cell_name,heure";

            resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                double val1 = 0, val2 = 0;
                int heure = -1;
                String str = resultTotal.getString("heure").trim();
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                }
                catch (Exception ex)
                {
                    heure = -1;
                }

                try
                {
                    val1 = resultTotal.getFloat("r3590");
                }
                catch (Exception ex)
                {
                    val1 = 0;
                }
                if (val1 > 0 && heure >= 0)
                {
                    TabBhCellule[heure - 1] = heure;
                    TabTchmCellule[heure - 1] = TabTchmCellule[heure - 1] + ((resultTotal.getFloat("cr3553") + resultTotal.getFloat("cr3554")) / val1);
                }
            }

            /***Calcul de Tchm & Bh par region ***/
            //requete = "select distinct(date) from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            int n = TabTchmCellule.length;
            double TchmBhCellule = 0;
            BhCellule = -1;
            double som = 0;
            for (int i = 0; i < n; i++)
            {
                som = som + TabTchmCellule[i];
                if (TabTchmCellule[i] > TchmBhCellule)
                {
                    TchmBhCellule = TabTchmCellule[i];
                    BhCellule = i + 1;
                }
            }
            if (nbreJrsHrs > 0)
            {
                som = som / (float) (nbreJrsHrs);
            } else
            {
                som = 0;
            }
            som = (double) ((int) (som * 100)) / 100;
            TchmBhCellule = (double) ((int) (TchmBhCellule * 100)) / 100;
            if (BhCellule >= 0)
            {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhCellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            }
            else
            {
                System.out.println("************************************************************:Cellule:" + cellule + " ,BHfausse:" + BhCellule);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculBHParCellule(List<String> lstCellules)
    {
        try
        {
            mapTraficBhCellule = new HashMap<String, Double>();
            mapBhCellule = new HashMap<String, Integer>();
            mapTrHoraireMoyenCellule = new HashMap<String, Double>();

            int nbreRegion = lstCellules.size();
            for (int i = 0; i < nbreRegion; i++)
            {
                String cellule = lstCellules.get(i);
                ////calcul de la Busy Hour par région
                getBHParCellule(cellule);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculValeurGlobales()
    {
        int bh = -1;
        Object objBh = mapBhRegion.get("Global");
        if (objBh != null)
        {
            try
            {
                bh = Integer.valueOf(objBh.toString());
            }
            catch (Exception ex)
            {
                bh = -1;
            }
        }
        else
        {
            bh = -1;
        }
        String StrBh = "";
        if (bh < 10)
        {
            StrBh = "0" + bh + ":00";
        }
        else
        {
            StrBh = bh + ":00";
        }
        System.out.println("******************************BhGlobal:" + StrBh);

        double ZCM30 = 0, ZK3000 = 0, ZK3001 = 0, ZK3003 = 0, ZTR103A = 0, ZTR104A = 0, CH310 = 0, CH313 = 0, CH323 = 0, CH330 = 0, CH333 = 0, CH343 = 0,
                CM30C = 0, CM33 = 0, CR3553 = 0, CR3554 = 0, RH303 = 0, CR4110 = 0, CR4119 = 0, R3550N = 0, R3590 = 0, K3010A = 0, K3010B = 0, K3011A = 0,
                K3011B = 0, K3013A = 0, K3020 = 0, K3021 = 0, K3045 = 0;
        double ZCM30Bh = 0, ZK3000Bh = 0, ZK3001Bh = 0, ZK3003Bh = 0, ZTR103ABh = 0, ZTR104ABh = 0, CH310Bh = 0, CH313Bh = 0, CH323Bh = 0, CH330Bh = 0, CH333Bh = 0, CH343Bh = 0,
                CM30CBh = 0, CM33Bh = 0, CR3553Bh = 0, CR3554Bh = 0, RH303Bh = 0, CR4110Bh = 0, CR4119Bh = 0, R3550NBh = 0, R3590Bh = 0, K3010ABh = 0, K3010BBh = 0, K3011ABh = 0,
                K3011BBh = 0, K3013ABh = 0, K3020Bh = 0, K3021Bh = 0, K3045Bh = 0;

        String requete = "select region,heure,sum(zcm30) as zcm30,sum(zk3000) as zk3000,sum(zk3001) as zk3001,sum(zk3003) as zk3003,sum(ztr103a) as ztr103a,"
                    + " sum(ztr104a) as ztr104a,sum(ch310) as ch310, sum(ch313) as ch313,sum(ch323) as ch323,sum(ch330) as ch330,sum(ch333) as ch333,"
                    + " sum(ch343) as ch343, sum(cm30c) as cm30c,sum(cm33) as cm33,sum(cr3553) as cr3553,sum(cr3554) as cr3554,sum(rh303) as rh303,"
                    + " sum(cr4110) as cr4110,sum(cr4119) as cr4119, "
                    + " sum(r3550n) as r3550n,sum(r3590) as r3590,sum(k3010a) as k3010a, sum(k3010b) as k3010b, sum(k3011a) as k3011a"
                    + " sum(k3011b) as k3011b,sum(k3013a) as k3013a,sum(k3020) as k3020, sum(k3021) as k3021, sum(k3045) as k3045"
                + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' group by heure";
        System.out.println("---------------------------------------------------requete Globale:" + requete);
        ResultSet result = cn.getResultset(requete);
        try
        {
            while (result.next())
            {

               ZCM30 = ZCM30 + result.getFloat("zcm30");
                ZK3000 = ZK3000 + result.getFloat("zk3000");
                ZK3001 = ZK3001 + result.getFloat("zk3001");
                ZK3003 = ZK3003 + result.getFloat("zk3003");
                ZTR103A = ZTR103A + result.getFloat("ztr103a");
                ZTR104A = ZTR104A + result.getFloat("ztr104a");
                CH310 = CH310 + result.getFloat("ch310");
                CH313 = CH313 + result.getFloat("ch313");
                CH323 = CH323 + result.getFloat("ch323");
                CH330 = CH330 + result.getFloat("ch330");
                CH333 = CH333 + result.getFloat("ch333");

                CH343 = CH343 + result.getFloat("ch343");
                CM30C = CM30C + result.getFloat("cm30c");
                CM33 = CM33 + result.getFloat("cm33");
                CR3553 = CR3553 + result.getFloat("cr3553");
                CR3554 = CR3554 + result.getFloat("cr3554");
                RH303 = RH303 + result.getFloat("rh303");
                CR4110 = CR4110 + result.getFloat("cr4110");
                CR4119 = CR4119 + result.getFloat("cr4119");
                R3550N = R3550N + result.getFloat("r3550n");
                R3590 = R3590 + result.getFloat("r3590");
                K3010A = K3010A + result.getFloat("k3010a");

                K3010B = K3010B + result.getFloat("k3010b");
                K3011A = K3011A + result.getFloat("k3011a");
                K3011B = K3011B + result.getFloat("k3011b");
                K3013A = K3013A + result.getFloat("k3013a");
                K3020 = K3020 + result.getFloat("k3020");
                K3021 = K3021 + result.getFloat("k3021");
                K3045 = K3045 + result.getFloat("k3045");

                int heure = -1;
                String str = result.getString("heure").split(":")[0];
                try
                {
                    heure = Integer.parseInt(str);
                }
                catch (Exception ex)
                {
                    heure = -1;
                }
                if (heure >= 0 && heure == BhG)
                {
                    ZCM30Bh = result.getFloat("zcm30");
                    ZK3000Bh = result.getFloat("zk3000");
                    ZK3001Bh = result.getFloat("zk3001");
                    ZK3003Bh = result.getFloat("zk3003");
                    ZTR103ABh = result.getFloat("ztr103a");
                    ZTR104ABh = result.getFloat("ztr104a");
                    CH310Bh = result.getFloat("ch310");
                    CH313Bh = result.getFloat("ch313");
                    CH323Bh = result.getFloat("ch323");
                    CH330Bh = result.getFloat("ch330");
                    CH333Bh = result.getFloat("ch333");

                    CH343Bh = result.getFloat("ch343");
                    CM30CBh = result.getFloat("cm30c");
                    CM33Bh = result.getFloat("cm33");
                    CR3553Bh = result.getFloat("cr3553");
                    CR3554Bh = result.getFloat("cr3554");
                    RH303Bh = result.getFloat("rh303");
                    CR4110Bh = result.getFloat("cr4110");
                    CR4119Bh = result.getFloat("cr4119");
                    R3550NBh = result.getFloat("r3550n");
                    R3590Bh = result.getFloat("r3590");
                    K3010ABh = result.getFloat("k3010a");

                    K3010BBh = result.getFloat("k3010b");
                    K3011ABh = result.getFloat("k3011a");
                    K3011BBh = result.getFloat("k3011b");
                    K3013ABh = result.getFloat("k3013a");
                    K3020Bh = result.getFloat("k3020");
                    K3021Bh = result.getFloat("k3021");
                    K3045Bh = result.getFloat("k3045");
                }
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (R3590 > 0) {
                tchm = (CR3553 + CR3554) / R3590;
                tchm = (double) ((int) (tchm * 10000)) / 10000;
            }
            else
            {
                tchm = 0;
            }

            /// calcul de TCHMBH

            if (R3590Bh > 0)
            {
                tchmbh = (CR3553Bh + CR3554Bh) / R3590Bh;
                tchm = (double) ((int) (tchm * 10000)) / 10000;
            }
            else
            {

                tchm = 0;
            }

            /////// calcul de CSSR
          /*  if (ZK3003>0)
            {
            cssr= (1-(ZCM30*100/ZK3003))*ztr102a*100;
            cssr= (double) ((int) (tchm * 10000)) / 10000;
            }
            else
            {
            cssr=0;
            }
             */
            //////// calcul de CDR
            if (K3013A + CH323 + CH343 - CH313 - CH333 > 0)
            {
                cdr = CM33 / (K3013A + CH323 + CH343 - CH313 - CH333);
                cdr = (double) ((int) (cdr * 10000)) / 10000;
            }
            else
            {
                cdr = 1;
            }

            //////////// calcul CDRBH
            if (K3013ABh + CH323Bh + CH343Bh - CH313Bh - CH333Bh > 0)
            {
                cdrbh = CM33Bh / (K3013ABh + CH323Bh + CH343Bh - CH313Bh - CH333Bh);
                cdrbh = (double) ((int) (cdrbh * 10000)) / 10000;
           }
            else
            {
                cdrbh = 1;
            }

            ////// calcul TCHCR
            if (K3020 + K3010A + K3010B > 0)
            {
                tchcr = (K3021 + K3011A + K3011B) / (K3020 + K3010A + K3010B);
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
            }
            else
            {
                tchcr = 1;
            }

            ////// calcul TCHCRBH
            if (K3020Bh + K3010ABh + K3010BBh > 0)
            {
                tchcrbh = (K3021Bh + K3011ABh + K3011BBh) / (K3020Bh + K3010ABh + K3010BBh);
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
            }
            else
            {
                tchcrbh = 1;
            }

         ////////calcul de SDCCHCR
            if (ZK3000 > 0)
            {
                sdcchcr = ZK3001 / ZK3000;
                sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
            }
            else
            {
                sdcchcr = 1;
            }


            ////////calcul de SDCCHCRBH
            if (ZK3000Bh > 0)
            {
                sdcchcrbh = ZK3001Bh / ZK3000Bh;
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
            }
            else
            {
                sdcchcrbh = 1;
            }

          /////////// calcul de SDCCHDRBH

            if (ZK3003 > 0)
            {
                sdcchdr = ZCM30 / ZK3003;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
            }
            else
            {
                sdcchdr = 1;
            }

            /////////// calcul de SDCCHDRBH

            if (ZK3003Bh > 0)
            {
                sdcchdrbh = ZCM30Bh / ZK3003Bh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
            }
            else
            {
                sdcchdrbh = 1;
            }

            ////////////// Calcul de HOSucces

            if (CH310 + CH330 > 0)
            {
                hosucces = (CH313 + CH333) / (CH310 + CH330);
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
            }
            else
            {
                hosucces = 0;
            }

            ////////////// Calcul de HOSuccesBh

            if (CH310Bh + CH330Bh > 0)
            {
                hosuccesbh = (CH313Bh + CH333Bh) / (CH310Bh + CH330Bh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
            }
            else
            {
                hosuccesbh = 0;
            }

         ////////////// Calcul de HoFailureBh

        HoFailureBh=1-hosuccesbh;
        HoFailureBh = (double) ((int) (HoFailureBh * 10000)) / 10000;


        Object obj = mapTraficBhRegion.get("Global");
        Object obj1 = mapTrHoraireMoyenRegion.get("Global");
        if (obj != null && obj1 != null) {
            double val = Double.parseDouble(obj.toString());
            if (val > 0) {
                bhtr = tchmbh / tchm;
            } else {
                bhtr = 1;
            }
            bhtr = (double) ((int) (bhtr * 100)) / 100;
        }
    }

    private void CalculCellule(String cellule)
    {
        try
        {
            tchm=0;tchmbh=0;tchcr=0;tchcrbh=0;
            tchdr=0;tchdrbh=0;tchbr=0;tchbrbh=0;
            cssr=0;sdcchcrbh=0; sdcchdrbh=0;sdcchbrbh=0;
            cdr=0;cdrbh=0;csr=0; csrbh=0;Rsmsr=0;
            sdcchcr=0; sdcchdr=0; sdcchbr=0;hosucces=0;hosuccesbh=0; HoFailureBh=0;


            double ZCM30 = 0, ZK3000 = 0, ZK3001 = 0, ZK3003 = 0, ZTR103A = 0, ZTR104A = 0, CH310 = 0, CH313 = 0, CH323 = 0, CH330 = 0, CH333 = 0, CH343 = 0,
                CM30C = 0, CM33 = 0, CR3553 = 0, CR3554 = 0, RH303 = 0, CR4110 = 0, CR4119 = 0, R3550N = 0, R3590 = 0, K3010A = 0, K3010B = 0, K3011A = 0,
                K3011B = 0, K3013A = 0, K3020 = 0, K3021 = 0, K3045 = 0;
            double ZCM30Bh = 0, ZK3000Bh = 0, ZK3001Bh = 0, ZK3003Bh = 0, ZTR103ABh = 0, ZTR104ABh = 0, CH310Bh = 0, CH313Bh = 0, CH323Bh = 0, CH330Bh = 0, CH333Bh = 0, CH343Bh = 0,
                CM30CBh = 0, CM33Bh = 0, CR3553Bh = 0, CR3554Bh = 0, RH303Bh = 0, CR4110Bh = 0, CR4119Bh = 0, R3550NBh = 0, R3590Bh = 0, K3010ABh = 0, K3010BBh = 0, K3011ABh = 0,
                K3011BBh = 0, K3013ABh = 0, K3020Bh = 0, K3021Bh = 0, K3045Bh = 0;

           String requete ="select region,heure,sum(zcm30) as zcm30,sum(zk3000) as zk3000,sum(zk3001) as zk3001,sum(zk3003) as zk3003,sum(ztr103a) as ztr103a,"
                    + " sum(ztr104a) as ztr104a,sum(ch310) as ch310, sum(ch313) as ch313,sum(ch323) as ch323,sum(ch330) as ch330,sum(ch333) as ch333,"
                    + " sum(ch343) as ch343, sum(cm30c) as cm30c,sum(cm33) as cm33,sum(cr3553) as cr3553,sum(cr3554) as cr3554,sum(rh303) as rh303,"
                    + " sum(cr4110) as cr4110,sum(cr4119) as cr4119, "
                    + " sum(r3550n) as r3550n,sum(r3590) as r3590,sum(k3010a) as k3010a, sum(k3010b) as k3010b, sum(k3011a) as k3011a"
                    + " sum(k3011b) as k3011b,sum(k3013a) as k3013a,sum(k3020) as k3020, sum(k3021) as k3021, sum(k3045) as k3045"
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
                ZCM30 = ZCM30 + result.getFloat("zcm30");
                ZK3000 = ZK3000 + result.getFloat("zk3000");
                ZK3001 = ZK3001 + result.getFloat("zk3001");
                ZK3003 = ZK3003 + result.getFloat("zk3003");
                ZTR103A = ZTR103A + result.getFloat("ztr103a");
                ZTR104A = ZTR104A + result.getFloat("ztr104a");
                CH310 = CH310 + result.getFloat("ch310");
                CH313 = CH313 + result.getFloat("ch313");
                CH323 = CH323 + result.getFloat("ch323");
                CH330 = CH330 + result.getFloat("ch330");
                CH333 = CH333 + result.getFloat("ch333");

                CH343 = CH343 + result.getFloat("ch343");
                CM30C = CM30C + result.getFloat("cm30c");
                CM33 = CM33 + result.getFloat("cm33");
                CR3553 = CR3553 + result.getFloat("cr3553");
                CR3554 = CR3554 + result.getFloat("cr3554");
                RH303 = RH303 + result.getFloat("rh303");
                CR4110 = CR4110 + result.getFloat("cr4110");
                CR4119 = CR4119 + result.getFloat("cr4119");
                R3550N = R3550N + result.getFloat("r3550n");
                R3590 = R3590 + result.getFloat("r3590");
                K3010A = K3010A + result.getFloat("k3010a");

                K3010B = K3010B + result.getFloat("k3010b");
                K3011A = K3011A + result.getFloat("k3011a");
                K3011B = K3011B + result.getFloat("k3011b");
                K3013A = K3013A + result.getFloat("k3013a");
                K3020 = K3020 + result.getFloat("k3020");
                K3021 = K3021 + result.getFloat("k3021");
                K3045 = K3045 + result.getFloat("k3045");


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
                    ZCM30Bh = result.getFloat("zcm30");
                    ZK3000Bh = result.getFloat("zk3000");
                    ZK3001Bh = result.getFloat("zk3001");
                    ZK3003Bh = result.getFloat("zk3003");
                    ZTR103ABh = result.getFloat("ztr103a");
                    ZTR104ABh = result.getFloat("ztr104a");
                    CH310Bh = result.getFloat("ch310");
                    CH313Bh = result.getFloat("ch313");
                    CH323Bh = result.getFloat("ch323");
                    CH330Bh = result.getFloat("ch330");
                    CH333Bh = result.getFloat("ch333");

                    CH343Bh = result.getFloat("ch343");
                    CM30CBh = result.getFloat("cm30c");
                    CM33Bh = result.getFloat("cm33");
                    CR3553Bh = result.getFloat("cr3553");
                    CR3554Bh = result.getFloat("cr3554");
                    RH303Bh = result.getFloat("rh303");
                    CR4110Bh = result.getFloat("cr4110");
                    CR4119Bh = result.getFloat("cr4119");
                    R3550NBh = result.getFloat("r3550n");
                    R3590Bh = result.getFloat("r3590");
                    K3010ABh = result.getFloat("k3010a");

                    K3010BBh = result.getFloat("k3010b");
                    K3011ABh = result.getFloat("k3011a");
                    K3011BBh = result.getFloat("k3011b");
                    K3013ABh = result.getFloat("k3013a");
                    K3020Bh = result.getFloat("k3020");
                    K3021Bh = result.getFloat("k3021");
                    K3045Bh = result.getFloat("k3045");
                }
            }

            if (R3590 > 0) {
                tchm = (CR3553 + CR3554) / R3590;
                tchm = (double) ((int) (tchm * 10000)) / 10000;
            } else {
                tchm = 0;
            }

            /// calcul de TCHMBH

            if (R3590Bh > 0)
            {
                tchmbh = (CR3553Bh + CR3554Bh) / R3590Bh;
                tchm = (double) ((int) (tchm * 10000)) / 10000;
            }
            else
            {

                tchm = 0;
            }

            /////// calcul de CSSR
          /*  if (ZK3003>0)
            {
            cssr= (1-(ZCM30*100/ZK3003))*ztr102a*100;
            cssr= (double) ((int) (tchm * 10000)) / 10000;
            }
            else
            {
            cssr=0;
            }
             */
            //////// calcul de CDR
            if (K3013A + CH323 + CH343 - CH313 - CH333 > 0)
            {
                cdr = CM33 / (K3013A + CH323 + CH343 - CH313 - CH333);
                cdr = (double) ((int) (cdr * 10000)) / 10000;
            }
            else
            {
                cdr = 1;
            }

            //////////// calcul CDRBH
            if (K3013ABh + CH323Bh + CH343Bh - CH313Bh - CH333Bh > 0)
            {
                cdrbh = CM33Bh / (K3013ABh + CH323Bh + CH343Bh - CH313Bh - CH333Bh);
                cdrbh = (double) ((int) (cdrbh * 10000)) / 10000;
           }
            else
            {
                cdrbh = 1;
            }

            ////// calcul TCHCR
            if (K3020 + K3010A + K3010B > 0)
            {
                tchcr = (K3021 + K3011A + K3011B) / (K3020 + K3010A + K3010B);
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
            }
            else
            {
                tchcr = 1;
            }

            ////// calcul TCHCRBH
            if (K3020Bh + K3010ABh + K3010BBh > 0)
            {
                tchcrbh = (K3021Bh + K3011ABh + K3011BBh) / (K3020Bh + K3010ABh + K3010BBh);
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
            }
            else
            {
                tchcrbh = 1;
            }
            ////////calcul de SDCCHCR
            if (ZK3000 > 0)
            {
                sdcchcr = ZK3001 / ZK3000;
                sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
            }
            else
            {
                sdcchcr = 1;
            }

            ////////calcul de SDCCHCRBH
            if (ZK3000Bh > 0)
            {
                sdcchcrbh = ZK3001Bh / ZK3000Bh;
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
            }
            else
            {
                sdcchcrbh = 1;
            }

              /////////// calcul de SDCCHDRBH

            if (ZK3003 > 0)
            {
                sdcchdr = ZCM30 / ZK3003;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
            }
            else
            {
                sdcchdr = 1;
            }
            /////////// calcul de SDCCHDRBH

            if (ZK3003Bh > 0)
            {
                sdcchdrbh = ZCM30Bh / ZK3003Bh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
            }
            else
            {
                sdcchdrbh = 1;
            }

            ////////////// Calcul de HOSucces

            if (CH310 + CH330 > 0)
            {
                hosucces = (CH313 + CH333) / (CH310 + CH330);
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
            }
            else
            {
                hosucces = 0;
            }

            ////////////// Calcul de HOSuccesBh

            if (CH310Bh + CH330Bh > 0)
            {
                hosuccesbh = (CH313Bh + CH333Bh) / (CH310Bh + CH330Bh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
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
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (Exception ex)
        {
            Logger.getLogger(CalculHuawei3G.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                result.close();
            } catch (SQLException ex){}
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
