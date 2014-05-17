package projet_consultation.Calcul.Calcul2G;

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

public class CalculAlcatel2G
{
    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double tchhm = 0;
    private double tchm = 0;
    private double tchmbh = 0;
    private double[] TabTchmGlobal = new double[24];
    private double cssr = 0, cssrbh = 0;
    private double bhtr = 0;
    private double tchcr = 0;
    private double tchcrbh = 0;
    private double tchdr = 0;
    private double tchdrbh = 0;
    private double sdcchbrbh,sdcchdrbh ;
    private double sdcchcrbh , sdcchcr , sdcchdr ,sdcchbr;
    private double csrbh ;
    private double csr,cdr;
    private double cdrbh;
    private double hosucces ,hosuccesbh ;
    private int BhG = -1, BhRegion = -1, BhCellule = -1;
    private int[] TabBhGlobal = new int[24];
    private double tchbr = 0;
    private double tchbrbh = 0 ;
    private double Rsmsr = 0, smslrbh ;
    private double TchmBhRegion = 0;
    private double hofailurebh = 0;

    private int idinfo = 0;
    private Map<String, Double> mapTraficBhRegion = null;
    private Map<String, Integer> mapBhRegion = null;
    private Map<String, Double> mapTrHoraireMoyenRegion = null;

    private Map<String,Double> mapTraficBhCellule=null ;
    private Map<String,Integer> mapBhCellule=null;
    private Map<String,Double> mapTrHoraireMoyenCellule=null;

    public CalculAlcatel2G(Operateur operateur, String datedebut, String datefin) throws SQLException
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
                    + "from table_bts where region='" + region + "' group by region";
            System.out.println("Requete nombre dee jour:" + requete);
            ResultSet resultSet = cn.getResultset(requete);
            if (resultSet.next()) {
                NbreErgReel = resultSet.getInt("nbre");
            }

            ResultSet result = null;
            if (mapBhRegion.get(region) != null) {
                BhRegion = mapBhRegion.get(region);
            } else {
                BhRegion = -1;
            }
            requete = "select count(*)as nbre from tableregistre where trim(region)='" + region + "' and date>='" + dateDebut + "' and date<='" + dateFin + "' ";
            result = cn.getResultset(requete);
            try {
                if (result.next()) {
                    NbreErgRecus = result.getInt("nbre");
                }
            } catch (Exception ex) {
                Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
                NbreErgRecus = 0;
            }

            ///////
            tchm = 0;tchmbh = 0;tchhm=0;bhtr=0;tchcr = 0;tchcrbh = 0;tchdr = 0;tchdrbh = 0;
            tchbr = 0;tchbrbh = 0;cssr = 0;cssrbh=0;cdr = 0;cdrbh=0;csr = 0;csrbh = 0;hosucces = 0;hosuccesbh=0;
            sdcchcr=0;sdcchcrbh = 0;sdcchbr=0;sdcchbrbh = 0;sdcchdr=0;sdcchdrbh = 0;hofailurebh=0;

            double mc718 = 0, mc717a = 0, mc717b = 0, mc01 = 0, mc02 = 0, mc140a = 0, mc138 = 0, mc137 = 0, mc07 = 0, mc812 = 0, mc14c = 0, mc739 = 0, mc736 = 0, mc621 = 0, mc921c = 0, mc04 = 0,
                    c01 = 0, mc712 = 0, mc645a = 0, mc655a = 0, mc820 = 0, mc830 = 0, mc646 = 0, mc656 = 0, mc642 = 0, mc652 = 0, c194a = 0, c194c = 0, c195 = 0, mc142f = 0, mc142e = 0, mc789 = 0, mc10 = 0, c194b = 0;
            double mc718Bh = 0, mc717aBh = 0, mc717bBh = 0, mc01Bh = 0, mc02Bh = 0, mc140aBh = 0, mc138Bh = 0, mc137Bh = 0, mc07Bh = 0, mc812Bh = 0, mc14cBh = 0, mc739Bh = 0, mc736Bh = 0, mc621Bh = 0, mc921cBh = 0,
                    mc712Bh = 0, mc10Bh = 0, c01Bh = 0, mc04Bh = 0, c194bBh = 0,mc645aBh=0,mc655aBh=0,mc820Bh=0,mc830Bh=0,mc646Bh=0,mc656Bh=0,mc642Bh=0,mc652Bh=0;

            System.out.println("----------------------**********//////////////////////    Region en cour de calcul:" + region);
            requete = "select region,heure,sum(mc718) as mc718,sum(mc717a) as mc717a,sum(mc717b) as mc717b,sum(mc138) as mc138,sum(mc137) as mc137,"
                    + " sum(mc07) as mc07,sum(mc01) as mc01, sum(mc02) as mc02,sum(mc140a) as mc140a, sum(mc142e) as mc142e,sum(mc142f) as mc142f,"
                    + " sum(mc718) as mc718, sum(mc812) as mc812,sum(mc812) as mc812,sum(mc789) as mc789,sum(mc736) as mc736,sum(mc621) as mc621,"
                    + " sum(mc921c) as mc921c,sum(mc04) as mc04, "
                    + " sum(c01) as c01,sum(mc14c) as mc14c,sum(mc739) as mc739, sum(mc712) as mc712, sum(mc646) as mc646, sum(mc656) as mc656, sum(mc645a) as mc645a, sum(mc655a) as mc655a,"
                    + " sum(mc642) as mc642, sum(mc652) as mc652, sum(mc820) as mc820, sum(mc830) as mc820, sum(c194a) as c194a, sum(c194c) as c194c, sum(c195) as c195, sum(c194b) as c194b"
                    + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "' "
                    + "group by region,heure order by region,heure ; ";
            System.out.println("---------------------------------------------------requte:" + requete);
            result = cn.getResultset(requete);
            while (result.next())
            {
                mc718 = mc718 + result.getFloat("mc718");
                mc717a = mc717a + result.getFloat("mc717a");
                mc717b = mc717b + result.getFloat("mc717b");
                mc138 = mc138 + result.getFloat("mc138");
                mc137 = mc137 + result.getFloat("mc137");
                mc07 = mc07 + result.getFloat("mc07");
                mc01 = mc01 + result.getFloat("mc01");
                mc02 = mc02 + result.getFloat("mc02");
                mc140a = mc140a + result.getFloat("mc140a");
                mc142e = mc142e + result.getFloat("mc142e");
                mc142f = mc142f + result.getFloat("mc142f");
                mc718 = mc718 + result.getFloat("mc718");
                mc812 = mc812 + result.getFloat("mc812");
                mc789 = mc789 + result.getFloat("mc789");
                mc736 = mc736 + result.getFloat("mc736");
                mc621 = mc621 + result.getFloat("mc621");
                mc921c = mc921c + result.getFloat("mc921c");
                mc04 = mc04 + result.getFloat("mc04");
                c01 = c01 + result.getFloat("c01");
                mc14c = mc14c + result.getFloat("mc14c");
                mc739 = mc739 + result.getFloat("mc739");
                mc712 = mc712 + result.getFloat("mc712");
                mc646 = mc646 + result.getFloat("mc646");
                mc656 = mc656 + result.getFloat("mc656");
                mc645a = mc645a + result.getFloat("mc645a");
                mc655a = mc655a + result.getFloat("mc655a");
                mc642 = mc642 + result.getFloat("mc642");
                mc652 = mc652 + result.getFloat("mc652");
                mc820 = mc820 + result.getFloat("mc820");
                mc830 = mc830 + result.getFloat("mc830");
                c194a = c194a + result.getFloat("c194a");
                c194c = c194c + result.getFloat("c194c");
                c195 = c195 + result.getFloat("c195");
                c194b = c194b + result.getFloat("c194b");


                String str = result.getString("heure").trim();
                int heure = -1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex)
                {
                    heure = -1;
                }
                if (heure == BhRegion && heure >= 0)
                {
                    mc718Bh = mc718Bh + result.getFloat("mc718");
                    mc717aBh = mc717aBh + result.getFloat("mc717a");
                    mc717bBh = mc717bBh + result.getFloat("mc717b");
                    mc812Bh = mc812Bh + result.getFloat("mc812");
                    mc140aBh = mc140aBh + result.getFloat("mc140a");
                    mc14cBh = mc14cBh + result.getFloat("mc14c");
                    mc739Bh = mc739Bh + result.getFloat("mc739");
                    mc736Bh = mc736Bh + result.getFloat("mc736");
                    mc621Bh = mc621Bh + result.getFloat("mc621");
                    mc921cBh = mc921cBh + result.getFloat("mc921c");
                    mc621Bh = mc621Bh + result.getFloat("mc621");
                    mc14cBh = mc14cBh + result.getFloat("mc14c");
                    mc736Bh = mc736Bh + result.getFloat("mc736");
                    mc739Bh = mc739Bh + result.getFloat("mc739");
                    mc921cBh = mc921cBh + result.getFloat("mc921c");
                    mc718Bh = mc718Bh + result.getFloat("mc718");
                    mc717aBh = mc717aBh + result.getFloat("mc717a");
                    mc717bBh = mc717bBh + result.getFloat("mc717b");
                    mc712Bh = mc712Bh + result.getFloat("mc712");
                    mc138Bh = mc138Bh + result.getFloat("mc138");
                    mc137Bh = mc137Bh + result.getFloat("mc137");
                    mc07Bh = mc07Bh + result.getFloat("mc07");
                    mc01Bh = mc01Bh + result.getFloat("mc01");
                    mc02Bh = mc02Bh + result.getFloat("mc02");
                    mc10Bh = mc10Bh + result.getFloat("mc10");
                    c01Bh = c01Bh + result.getFloat("c01");
                    mc04Bh = mc04Bh + result.getFloat("mc04");
                    c194bBh = c194bBh + result.getFloat("c194b");
                    mc645aBh=mc645aBh+result.getFloat("mc645a");
                    mc655aBh=mc655aBh+result.getFloat("mc655a");
                    mc820Bh=mc820Bh+result.getFloat("mc820");
                    mc830Bh=mc830Bh+result.getFloat("mc830");
                    mc646Bh=mc646Bh+result.getFloat("mc646");
                    mc656Bh=mc656Bh+result.getFloat("mc656");
                    mc642Bh=mc642Bh+result.getFloat("mc642");
                    mc652Bh=mc652Bh+result.getFloat("mc652");
                }
            }
            /*taux d'information manquante pour cette region*/
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
            //////calcul du Taux d'information manquantes

            //tchm
            tchm = mc718 + mc717a + mc717b;
            tchm = (double) ((int) (tchm * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchm", TauxInfoManq);

            //tchmbh
            tchmbh = mc718Bh + mc717aBh + mc717bBh;
            tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchmbh", TauxInfoManq);

            ///tchhm
            tchhm=tchm/24;
            tchhm = (double) ((int) (tchhm * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchhm", TauxInfoManq);

            //tchcr
            if (mc140a > 0)
            {
                tchcr = (mc812 / mc140a) * 100;
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
                if(tchcr>=0 && tchcr<=1)
                {
                    MapTauxInfoManqRegion.put("tchcr", TauxInfoManq);
                } else
                {
                    tchcr = 1;
                    MapTauxInfoManqRegion.put("tchcr", 100F);
                }
            } else {
                tchcr = 1;
                MapTauxInfoManqRegion.put("tchcr", 100F);
            }

            //tchcrbh
            if (mc140aBh > 0)
            {
                tchcrbh = (mc812Bh / mc140aBh) * 100;
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
                if(tchcrbh>=0 && tchcrbh<=1)
                {
                    MapTauxInfoManqRegion.put("tchcrbh", TauxInfoManq);
                } else
                {
                    tchcrbh = 1;
                    MapTauxInfoManqRegion.put("tchcrbh", 100F);
                }
            } else
            {
                tchcrbh = 1;
                MapTauxInfoManqRegion.put("tchcrbh", 100F);
            }

            //tchdr
            tchdr = mc14c + mc739 + mc736 + mc621 + mc921c;
            tchdr = (double) ((int) (tchdr * 10000)) / 10000;
            if (tchdr >= 0 && tchdr <= 1) {
                MapTauxInfoManqRegion.put("tchdr", TauxInfoManq);
            } else {
                tchdr = 1;
                MapTauxInfoManqRegion.put("tchdr", 100F);
            }

            //tchdrbh
            tchdrbh = mc14cBh + mc739Bh + mc736Bh + mc621Bh + mc921cBh;
            tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
            if (tchdrbh >= 0 && tchdrbh <= 1)
            {
                MapTauxInfoManqRegion.put("tchdrbh", TauxInfoManq);
            } else
            {
                tchdrbh = 1;
                MapTauxInfoManqRegion.put("tchdrbh", 100F);
            }

            //sdcchcr
            if (c01!= 0)
            {
                sdcchcr = mc04 / c01;
                sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
                if (sdcchcr >= 0 && sdcchcr <= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchcr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("sdcchcr", 100F);
                    sdcchcr = 1;
                }
            } else
            {
                sdcchcr = 1;
                MapTauxInfoManqRegion.put("sdcchcr", 100F);
            }

            ////sdcchcrbh
            if (c01Bh != 0)
            {
                sdcchcrbh = mc04 / c01;
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
                if (sdcchcrbh >= 0 && sdcchcrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchcrbh", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
                    sdcchcrbh = 1;
                }
            } else
            {
                sdcchcrbh = 1;
                MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
            }
            //sdcchdr
            if (c01 != 0)
            {
                sdcchdr = mc04 / c01;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr >= 0 && sdcchdr <= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchdr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("sdcchdr", 100F);
                    sdcchdr = 1;
                }
            } else
            {
                sdcchdr = 1;
                MapTauxInfoManqRegion.put("sdcchdr", 100F);
            }

            //sdcchdrbh
            if (c01Bh != 0)
            {
                sdcchdrbh = mc04Bh / c01Bh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh >= 0 && sdcchdrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchdrbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
                    sdcchdrbh = 1;
                }
            } else {
                sdcchdrbh = 1;
                MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
            }

            //cssr
            //cssr=((1-(mc138+mc137+mc07))/(mc01+mc02))*(1-(mc140a-(mc142+mc142ef-mc718)/(mc140a-(mc142e+mc142ef))));
            //mc142ef???
            cssr=(double) ((int) (cssr * 10000)) / 10000;
            if (cssr >= 0 && cssr <= 1) {
                MapTauxInfoManqRegion.put("cssr", TauxInfoManq);
            } else {
                cssr = 0;
                MapTauxInfoManqRegion.put("cssr", 100F);
            }

            //cssrbh
            cssrbh=(double) ((int) (cssrbh * 10000)) / 10000;
            if (cssr >= 0 && cssr <= 1) {
                MapTauxInfoManqRegion.put("cssrbh", TauxInfoManq);
            } else {
                cssrbh = 0;
                MapTauxInfoManqRegion.put("cssrbh", 100F);
            }

            //csr
            csr = cssr * (1 - cdr);
            csr = (double) ((int) (csr * 10000)) / 10000;
            if (csr >= 0 && csr <= 1) {
                MapTauxInfoManqRegion.put("csr", TauxInfoManq);
            } else {
                csr = 0;
                MapTauxInfoManqRegion.put("csr", 100F);
            }
            //csrbh
            csrbh = cssrbh * (1 - cdrbh);
            csrbh = (double) ((int) (csrbh * 10000)) / 10000;
            if (csrbh >= 0 && csrbh <= 1)
            {
                MapTauxInfoManqRegion.put("csrbh", TauxInfoManq);
            } else
            {
                csrbh = 0;
                MapTauxInfoManqRegion.put("csrbh", 100F);
            }

            //cdr
            if ((mc718 + (mc717a + mc717b) - mc712) > 0)
            {
                cdr = ((mc621 + mc14c + mc736 + mc739 + mc921c) / (mc718 + (mc717a + mc717b) - mc712));
                cdr = (double) ((int) (cdr * 10000)) / 10000;
                if (cdr >= 0 && cdr <= 1)
                {
                    MapTauxInfoManqRegion.put("cdr", TauxInfoManq);
                }else
                {
                    cdr = 1;
                    MapTauxInfoManqRegion.put("cdr", 100F);
                }
            } else
            {
                cdr = 1;
                MapTauxInfoManqRegion.put("cdr", 100F);
            }

            //cdrbh
            if ((mc718Bh + (mc717aBh + mc717bBh) - mc712Bh) > 0)
            {
                cdrbh = ((mc621Bh + mc14cBh + mc736Bh + mc739Bh + mc921cBh) / (mc718Bh + (mc717aBh + mc717bBh) - mc712Bh));
                cdrbh = (double) ((int) (cdrbh * 10000)) / 10000;
                if (cdrbh >= 0 && cdrbh < 1)
                {
                    MapTauxInfoManqRegion.put("cdrbh", TauxInfoManq);
                } else
                {
                    cdrbh = 1;
                    MapTauxInfoManqRegion.put("cdrbh", 100F);
                }
            } else
            {
                cdrbh = 1;
                MapTauxInfoManqRegion.put("cdrbh", 100F);
            }

            //hosucces
            if (((mc645a + mc655a) != 0) && ((mc820 + mc830) != 0))
            {
                hosucces = (((mc646 + mc656) / (mc645a + mc655a)) + ((mc642 + mc652) / (mc820 + mc830)));
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
                if (hosucces >=0 && hosucces <= 1)
                {
                    MapTauxInfoManqRegion.put("hosucces", TauxInfoManq);
                } else {
                    hosucces = 0;
                    MapTauxInfoManqRegion.put("hosucces", 100f);
                }
            } else
            {
                MapTauxInfoManqRegion.put("hosucces", 100f);
                hosucces= 0;
            }

            //hosuccessbh
            if (((mc645aBh + mc655aBh) != 0) && ((mc820Bh + mc830Bh) != 0))
            {
                hosuccesbh = (((mc646Bh + mc656Bh) / (mc645aBh + mc655aBh)) + ((mc642Bh + mc652Bh) / (mc820Bh + mc830Bh)));
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
                if (hosuccesbh >=0 && hosuccesbh <= 1)
                {
                    MapTauxInfoManqRegion.put("hosuccesbh", TauxInfoManq);
                } else
                {
                    hosuccesbh = 0;
                    MapTauxInfoManqRegion.put("hosuccesbh", 100f);
                }
            } else
            {
                MapTauxInfoManqRegion.put("hosuccesbh", 100f);
                hosuccesbh = 0;
            }

            ///hofailurebh
            hofailurebh=1-hosuccesbh;
            hofailurebh = (double) ((int) (hofailurebh * 10000)) / 10000;
            if (hofailurebh >= 0 && hofailurebh <= 1)
            {
                MapTauxInfoManqRegion.put("hofailurebh", TauxInfoManq);
            } else
            {
                hofailurebh = 0;
                MapTauxInfoManqRegion.put("hofailurebh", 100f);
            }

            Object obj = mapBhRegion.get(region);
            Object obj1 = mapTrHoraireMoyenRegion.get(region);
            Object obj2 = mapTraficBhRegion.get(region);

            if (obj != null && obj1 != null && obj2 != null) {
                tchhm = mapTrHoraireMoyenRegion.get(region);
                tchmbh = mapTraficBhRegion.get(region);
                if (tchm > 0)
                {
                    bhtr = tchmbh / tchm;
                } else
                {
                    bhtr = 1;
                }
                bhtr = (double) ((int) (bhtr * 10000)) / 10000;
                if (bhtr == 1) {
                    MapTauxInfoManqRegion.put("bhtr", 100F);
                } else {
                    MapTauxInfoManqRegion.put("bhtr", TauxInfoManq);
                }
                idinfo++;
                String requeteInsert = "insert into tauxinfo(idinfo,region,";
                String requeteValues = ") values(" + idinfo + ",'" + region + "',";
                for (Entry<String, Float> entry : MapTauxInfoManqRegion.entrySet())
                {
                    String cle = entry.getKey();
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
        } catch (SQLException ex) {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CalculTotal()
    {
        System.out.println("Debut calcul total Alcatel");
        ResultSet result = null;
        try {
            List<String> lstRegion = new ArrayList<String>();
            String requete = "select distinct(region) from table_regions";
            result = cn.getResultset(requete);
            while (result.next()) {
                lstRegion.add(result.getString("region").trim());
            }
            CalCulBHParRegion(lstRegion);
            int n = lstRegion.size();
            for (int i = 0; i < n; i++) {
                CalculRegion(lstRegion.get(i));
            }

            CalculValeurGlobales();
            if (BhG >= 0)
            {
                BhRegion=BhG;
                InsertTableRegion("Global");
            }

            /////////Calcul par Cellule
            List<String> lstCellule = new ArrayList<String>();
            requete = "select distinct(cell_name) from tableregistre";
            result = cn.getResultset(requete);
            while (result.next()) {
                lstCellule.add(result.getString("cell_name").trim());
                //System.out.println("Liste:" + lstCellule);
            }
            CalculBHParCellule(lstCellule);
            int nbreCellule = lstCellule.size();
            for (int i = 0; i < nbreCellule; i++) {
                CalculCellule(lstCellule.get(i));
            }

        } catch (SQLException ex) {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try {
                result.close();
            } catch (SQLException ex) {
            }
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
        try {
            int nbreJrsHrs = 0;
            //String requete="select count(*) as nbre from (select distinct(date) from tableregistre  where date>='"+dateDebut+"' and date<='"+dateFin+"' and region='"+region+"') query";
            String requete = "select count(*)*(select count(*) as nbre from (select distinct(heure) from tableregistre  "
                    + "where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "' order by heure) query) as nbre from (select distinct(date) "
                    + "from tableregistre  where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "') query";
            resultTotal = cn.getResultset(requete);
            if (resultTotal.next()) {
                try {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                } catch (Exception ex) {
                    Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs = 0;
                }
            }
            //String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select region,heure,sum(mc718) as mc718 ,sum(mc717a) as mc717a,sum(mc717b) as mc717b from tableregistre "
                    + "where region='" + region + "' and date>='" + dateDebut + "' and date<='" + dateFin + "'"
                    + " group by region,heure  order by region,heure";
            resultTotal = cn.getResultset(requete);
            while (resultTotal.next()) {

                String str = resultTotal.getString("heure").trim();
                int heure = -1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {
                    heure = -1;
                }
                if (heure >= 0) {
                    TabBhRegion[heure - 1] = heure;
                    TabTchmRegion[heure - 1] = TabTchmRegion[heure - 1] + (resultTotal.getFloat("mc718") + resultTotal.getFloat("mc717a") + resultTotal.getFloat("mc717b"));

                    TabBhGlobal[heure - 1] = heure;
                    TabTchmGlobal[heure - 1] = TabTchmGlobal[heure - 1] + (resultTotal.getFloat("mc718") + resultTotal.getFloat("mc717a") + resultTotal.getFloat("mc717b"));
                }
            }
            /***Calcul de Tchm & Bh par region ***/
            int n = TabTchmRegion.length;
            TchmBhRegion = 0;
            BhRegion = -1;
            double som = 0;
            for (int i = 0; i < n; i++) {
                som = som + TabTchmRegion[i];
                if (TabTchmRegion[i] > TchmBhRegion) {
                    TchmBhRegion = TabTchmRegion[i];
                    //BhRegion=TabBhRegion[i+1];
                    BhRegion = i + 1;
                }
            }
            if (nbreJrsHrs > 0) {
                som = som / (double) (nbreJrsHrs);
            } else {
                som = 0;
            }
            som = (double) ((int) (som * 100)) / 100;
            TchmBhRegion = (double) ((int) (TchmBhRegion * 100)) / 100;
            if (BhRegion >= 0) {
                mapBhRegion.put(region, BhRegion);
                mapTraficBhRegion.put(region, TchmBhRegion);
                mapTrHoraireMoyenRegion.put(region, som);
            }


        } catch (SQLException ex) {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                resultTotal.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
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
            double TchmBhGlobal = 0;
            BhG = -1;
            double som = 0;
            for (int i = 0; i < n; i++) {
                som = som + TabTchmGlobal[i];
                if (TabTchmGlobal[i] > TchmBhGlobal) {
                    TchmBhGlobal = TabTchmGlobal[i];
                    BhG = i + 1;
                }
            }
            String requete = "select count(*) as nbre from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "'";
            int nbreErg = cn.getNbreLigneResultset(requete);
            if (nbreErg > 0) {
                som = som / (double) (nbreErg);
            } else {
                som = 0;
            }
            som = (double) ((int) (som * 100)) / 100;
            TchmBhGlobal = (double) ((int) (TchmBhGlobal * 100)) / 100;
            if (BhG >= 0) {
                mapBhRegion.put("Global", BhG);
                mapTraficBhRegion.put("Global", TchmBhGlobal);
                mapTrHoraireMoyenRegion.put("Global", som);
            }

        } catch (Exception ex) {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void CalculValeurGlobales()
    {
        int bh = -1;
        Object objBh = mapBhRegion.get("Global");
        if (objBh != null) {
            try {
                bh = Integer.valueOf(objBh.toString());
            } catch (Exception ex) {
                bh = -1;
            }
        } else {
            bh = -1;
        }
        String StrBh = "";
        if (bh < 10) {
            StrBh = "0" + bh + ":00";
        } else {
            StrBh = bh + ":00";
        }
        System.out.println("******************************BhGlobal:" + StrBh);
        tchm = 0;tchmbh = 0;tchhm=0;bhtr=0;tchcr = 0;tchcrbh = 0;tchdr = 0;tchdrbh = 0;
        tchbr = 0;tchbrbh = 0;cssr = 0;cssrbh=0;cdr = 0;cdrbh=0;csr = 0;csrbh = 0;hosucces = 0;hosuccesbh=0;
        sdcchcr=0;sdcchcrbh = 0;sdcchbr=0;sdcchbrbh = 0;sdcchdr=0;sdcchdrbh = 0;hofailurebh=0;

        double mc718 = 0, mc717a = 0, mc717b = 0, mc01 = 0, mc02 = 0, mc140a = 0, mc138 = 0, mc137 = 0, mc07 = 0, mc812 = 0, mc14c = 0, mc739 = 0, mc736 = 0, mc621 = 0, mc921c = 0, mc04 = 0,
                    c01 = 0, mc712 = 0, mc645a = 0, mc655a = 0, mc820 = 0, mc830 = 0, mc646 = 0, mc656 = 0, mc642 = 0, mc652 = 0, c194a = 0, c194c = 0, c195 = 0, mc142f = 0, mc142e = 0, mc789 = 0, mc10 = 0, c194b = 0;
            double mc718Bh = 0, mc717aBh = 0, mc717bBh = 0, mc01Bh = 0, mc02Bh = 0, mc140aBh = 0, mc138Bh = 0, mc137Bh = 0, mc07Bh = 0, mc812Bh = 0, mc14cBh = 0, mc739Bh = 0, mc736Bh = 0, mc621Bh = 0, mc921cBh = 0,
                    mc712Bh = 0, mc10Bh = 0, c01Bh = 0, mc04Bh = 0, c194bBh = 0,mc645aBh=0,mc655aBh=0,mc820Bh=0,mc830Bh=0,mc646Bh=0,mc656Bh=0,mc642Bh=0,mc652Bh=0;
        String requete = "select region,heure,sum(mc718) as mc718,sum(mc717a) as mc717a,sum(mc717b) as mc717b,sum(mc138) as mc138,sum(mc137) as mc137,"
                + " sum(mc07) as mc07,sum(mc01) as mc01, sum(mc02) as mc02,sum(mc140a) as mc140a, sum(mc142e) as mc142e,sum(mc142f) as mc142f,"
                + " sum(mc718) as mc718, sum(mc812) as mc812,sum(mc812) as mc812,sum(mc789) as mc789,sum(mc736) as mc736,sum(mc621) as mc621,"
                + " sum(mc921c) as mc921c,sum(mc04) as mc04, "
                + " sum(c01) as c01,sum(mc14c) as mc14c,sum(mc739) as mc739, sum(mc712) as mc712, sum(mc646) as mc646, sum(mc656) as mc656, sum(mc645a) as mc645a, sum(mc655a) as mc655a,"
                + " sum(mc642) as mc642, sum(mc652) as mc652, sum(mc820) as mc820, sum(mc830) as mc820, sum(c194a) as c194a, sum(c194c) as c194c, sum(c195) as c195, sum(c194b) as c194b"
                + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' group by heure";
        System.out.println("---------------------------------------------------requete Globale:" + requete);
        ResultSet result = cn.getResultset(requete);
        try
        {
            while (result.next())
            {
                mc718 = mc718 + result.getFloat("mc718");
                mc717a = mc717a + result.getFloat("mc717a");
                mc717b = mc717b + result.getFloat("mc717b");
                mc138 = mc138 + result.getFloat("mc138");
                mc137 = mc137 + result.getFloat("mc137");
                mc07 = mc07 + result.getFloat("mc07");
                mc01 = mc01 + result.getFloat("mc01");
                mc02 = mc02 + result.getFloat("mc02");
                mc140a = mc140a + result.getFloat("mc140a");
                mc142e = mc142e + result.getFloat("mc142e");
                mc142f = mc142f + result.getFloat("mc142f");
                mc718 = mc718 + result.getFloat("mc718");
                mc812 = mc812 + result.getFloat("mc812");
                mc789 = mc789 + result.getFloat("mc789");
                mc736 = mc736 + result.getFloat("mc736");
                mc621 = mc621 + result.getFloat("mc621");
                mc921c = mc921c + result.getFloat("mc921c");
                mc04 = mc04 + result.getFloat("mc04");
                c01 = c01 + result.getFloat("c01");
                mc14c = mc14c + result.getFloat("mc14c");
                mc739 = mc739 + result.getFloat("mc739");
                mc712 = mc712 + result.getFloat("mc712");
                mc646 = mc646 + result.getFloat("mc646");
                mc656 = mc656 + result.getFloat("mc656");
                mc645a = mc645a + result.getFloat("mc645a");
                mc655a = mc655a + result.getFloat("mc655a");
                mc642 = mc642 + result.getFloat("mc642");
                mc652 = mc652 + result.getFloat("mc652");
                mc820 = mc820 + result.getFloat("mc820");
                mc830 = mc830 + result.getFloat("mc830");
                c194a = c194a + result.getFloat("c194a");
                c194c = c194c + result.getFloat("c194c");
                c195 = c195 + result.getFloat("c195");
                c194b = c194b + result.getFloat("c194b");


                String str = result.getString("heure").trim();
                int heure = -1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {
                    heure = -1;
                }
                if (heure>=0 && heure==BhG) {
                    mc718Bh = mc718Bh + result.getFloat("mc718");
                    mc717aBh = mc717aBh + result.getFloat("mc717a");
                    mc717bBh = mc717bBh + result.getFloat("mc717b");
                    mc812Bh = mc812Bh + result.getFloat("mc812");
                    mc140aBh = mc140aBh + result.getFloat("mc140a");
                    mc14cBh = mc14cBh + result.getFloat("mc14c");
                    mc739Bh = mc739Bh + result.getFloat("mc739");
                    mc736Bh = mc736Bh + result.getFloat("mc736");
                    mc621Bh = mc621Bh + result.getFloat("mc621");
                    mc921cBh = mc921cBh + result.getFloat("mc921c");
                    mc621Bh = mc621Bh + result.getFloat("mc621");
                    mc14cBh = mc14cBh + result.getFloat("mc14c");
                    mc736Bh = mc736Bh + result.getFloat("mc736");
                    mc739Bh = mc739Bh + result.getFloat("mc739");
                    mc921cBh = mc921cBh + result.getFloat("mc921c");
                    mc718Bh = mc718Bh + result.getFloat("mc718");
                    mc717aBh = mc717aBh + result.getFloat("mc717a");
                    mc717bBh = mc717bBh + result.getFloat("mc717b");
                    mc712Bh = mc712Bh + result.getFloat("mc712");
                    mc138Bh = mc138Bh + result.getFloat("mc138");
                    mc137Bh = mc137Bh + result.getFloat("mc137");
                    mc07Bh = mc07Bh + result.getFloat("mc07");
                    mc01Bh = mc01Bh + result.getFloat("mc01");
                    mc02Bh = mc02Bh + result.getFloat("mc02");
                    mc10Bh = mc10Bh + result.getFloat("mc10");
                    c01Bh = c01Bh + result.getFloat("c01");
                    mc04Bh = mc04Bh + result.getFloat("mc04");
                    c194bBh = c194bBh + result.getFloat("c194b");
                    mc645aBh=mc645aBh+result.getFloat("mc645a");
                    mc655aBh=mc655aBh+result.getFloat("mc655a");
                    mc820Bh=mc820Bh+result.getFloat("mc820");
                    mc830Bh=mc830Bh+result.getFloat("mc830");
                    mc646Bh=mc646Bh+result.getFloat("mc646");
                    mc656Bh=mc656Bh+result.getFloat("mc656");
                    mc642Bh=mc642Bh+result.getFloat("mc642");
                    mc652Bh=mc652Bh+result.getFloat("mc652");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*taux d'information manquante pour cette region*/

        //tchm
        tchm = mc718 + mc717a + mc717b;
        tchm = (double) ((int) (tchm * 10000)) / 10000;

        //tchmbh
        tchmbh = mc718Bh + mc717aBh + mc717bBh;
        tchmbh = (double) ((int) (tchm * 10000)) / 10000;
        //tchhm
        tchhm=tchm/24;
        tchhm = (double) ((int) (tchhm * 10000)) / 10000;

        //tchcr
        if (mc140a != 0)
        {
            tchcr = (mc812 / mc140a) * 100;
            tchcr = (double) ((int) (tchcr * 10000)) / 10000;
            if(tchcr<0 || tchcr > 1)
            {
                tchcr = 1;
            }
        } else
            tchcr = 1;
        //tchcrbh
        if (mc140aBh != 0)
        {
            tchcrbh = (mc812 / mc140a) * 100;
            tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
            if(tchcrbh<0 || tchcrbh > 1)
            {
                tchcrbh = 1;
            }
        } else
            tchcrbh = 1;

        //tchdr
        tchdr = mc14c + mc739 + mc736 + mc621 + mc921c;
        tchdr = (double) ((int) (tchdr * 10000)) / 100;
        if (tchdr <0 || tchdr > 1)
        {
            tchdr = 1;
        }
        //tchdrbh
        tchdrbh = mc14cBh + mc739Bh + mc736Bh + mc621Bh + mc921cBh;
        tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
        if (tchdrbh< 0 || tchdrbh> 1)
        {
            tchdrbh = 1;
        }

        //sdcchcr
            if (c01 != 0)
            {
                sdcchcr = mc04 / c01;
                sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
                if (sdcchcr <0 || sdcchcr > 1) {
                    sdcchcr = 1;
                }
            } else
                sdcchcr = 1;


            ////sdcchcrbh
            if (c01Bh != 0)
            {
                sdcchcrbh = mc04 / c01;
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
                if (sdcchcrbh <0 || sdcchcrbh > 1) {
                    sdcchcrbh = 1;
                }
            } else
                sdcchcrbh = 1;

            //sdcchdr
            if (c01 != 0) {
                sdcchdr = mc04 / c01;
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr <0 || sdcchdr > 1) {
                    sdcchdr = 1;
                }
            } else
                sdcchdr = 1;


            //sdcchdrbh
            if (c01Bh != 0) {
                sdcchdrbh = mc04Bh / c01Bh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh <0 || sdcchdrbh > 1) {
                        sdcchdrbh = 1;
                }
            } else
                sdcchdrbh = 1;


        //sdcchdrbh
        if (c01Bh != 0) {
            sdcchdrbh = mc04Bh / c01Bh;
            sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
            if (sdcchdrbh < 0 || sdcchdrbh > 1) {
                sdcchdrbh = 1;
            }
        } else {
            sdcchdrbh = 1;
        }
        ///cssr
        ///formuel voir à verifier
        cssr = (double) ((int) (cssr * 10000)) / 10000;
            if (cssr <0 || cssr > 1)
            {
                cssr = 0;
            }
        //cssrbh
         cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
            if (cssrbh <0 || cssrbh > 1)
            {
                cssrbh = 0;
            }

        //csr
        csr = cssr * (1 - cdr);
        csr = (double) ((int) (csr * 10000)) / 10000;
        if (csr <0 || csr > 1)
        {
            csr = 0;
        }
        //csrbh
        csrbh = cssrbh * (1 - cdrbh);
        csrbh = (double) ((int) (csrbh * 10000)) / 10000;
        if (csrbh< 0 || csrbh> 1)
        {
            csrbh = 0;
        }

        //cdr
        if ((mc718 + (mc717a + mc717b) - mc712) > 0)
        {
            cdr = ((mc621 + mc14c + mc736 + mc739 + mc921c) / (mc718 + (mc717a + mc717b) - mc712));
            cdr = (double) ((int) (cdr * 10000)) / 10000;
            if(cdr < 0 ||cdr > 1)
            {
                cdr=1;
            }
        } else cdr = 1;


        //cdrbh
        if ((mc718Bh + (mc717aBh + mc717bBh) - mc712Bh) > 0)
        {
            cdrbh = ((mc621Bh + mc14cBh + mc736Bh + mc739Bh + mc921cBh) / (mc718Bh + (mc717aBh + mc717bBh) - mc712Bh));
            cdrbh = (double) ((int) (cdrbh * 10000)) / 10000;
            if(cdrbh < 0 ||cdrbh > 1)
            {
                cdrbh=1;
            }
         } else
            cdrbh = 1;

        //hosucces
        if (((mc645a + mc655a) != 0) && ((mc820 + mc830) != 0))
        {
            hosucces = (((mc646 + mc656) / (mc645a + mc655a)) + ((mc642 + mc652) / (mc820 + mc830)));
            hosucces = (double) ((int) (hosucces * 10000)) / 10000;
            if (hosucces < 0 || hosucces > 1)
            {
                hosucces = 0;
            }
        } else
            hosucces = 0;

        //hosuccessbh
        if (((mc645aBh + mc655aBh) != 0) && ((mc820Bh + mc830Bh) != 0))
        {
            hosuccesbh = (((mc646 + mc656) / (mc645a + mc655a)) + ((mc642 + mc652) / (mc820 + mc830)));
            hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
            if (hosuccesbh < 0 || hosuccesbh > 1)
            {
                hosuccesbh = 0;
            }
        } else
            hosuccesbh = 0;

        //hofailurebh
        hofailurebh=1-hosuccesbh;
            hofailurebh = (double) ((int) (hofailurebh * 10000)) / 10000;
        if (hofailurebh< 0 || hofailurebh > 1) {
            hofailurebh = 0;
        } else {
            hofailurebh = 0;
        }

        Object obj = mapTraficBhRegion.get("Global");
        Object obj1 = mapTrHoraireMoyenRegion.get("Global");
        if (obj != null && obj1 != null) {
            tchhm=Double.parseDouble(obj1.toString());
            double val = Double.parseDouble(obj.toString());
            if (val > 0) {
                bhtr = tchmbh / tchm;
            } else {
                bhtr = 1;
            }
            bhtr = (double) ((int) (bhtr * 10000)) / 10000;
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
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculCellule(String cellule)
    {
        try
        {
            tchm = 0;tchmbh = 0;tchhm=0;bhtr=0;tchcr = 0;tchcrbh = 0;tchdr = 0;tchdrbh = 0;
            tchbr = 0;tchbrbh = 0;cssr = 0;cssrbh=0;cdr = 0;cdrbh=0;csr = 0;csrbh = 0;hosucces = 0;hosuccesbh=0;
            sdcchcr=0;sdcchcrbh = 0;sdcchbr=0;sdcchbrbh = 0;sdcchdr=0;sdcchdrbh = 0;hofailurebh=0;
            double mc718 = 0, mc717a = 0, mc717b = 0, mc01 = 0, mc02 = 0, mc140a = 0, mc138 = 0, mc137 = 0, mc07 = 0, mc812 = 0, mc14c = 0, mc739 = 0, mc736 = 0, mc621 = 0, mc921c = 0, mc04 = 0,
                    c01 = 0, mc712 = 0, mc645a = 0, mc655a = 0, mc820 = 0, mc830 = 0, mc646 = 0, mc656 = 0, mc642 = 0, mc652 = 0, c194a = 0, c194c = 0, c195 = 0, mc142f = 0, mc142e = 0, mc789 = 0, mc10 = 0, c194b = 0;
            double mc718Bh = 0, mc717aBh = 0, mc717bBh = 0, mc01Bh = 0, mc02Bh = 0, mc140aBh = 0, mc138Bh = 0, mc137Bh = 0, mc07Bh = 0, mc812Bh = 0, mc14cBh = 0, mc739Bh = 0, mc736Bh = 0, mc621Bh = 0, mc921cBh = 0,
                    mc712Bh = 0, mc10Bh = 0, c01Bh = 0, mc04Bh = 0, c194bBh = 0,mc645aBh=0,mc655aBh=0,mc820Bh=0,mc830Bh=0,mc646Bh=0,mc656Bh=0,mc642Bh=0,mc652Bh=0;
            String requete = "select region,heure,sum(mc718) as mc718,sum(mc717a) as mc717a,sum(mc717b) as mc717b,sum(mc138) as mc138,sum(mc137) as mc137,"
                + " sum(mc07) as mc07,sum(mc01) as mc01, sum(mc02) as mc02,sum(mc140a) as mc140a, sum(mc142e) as mc142e,sum(mc142f) as mc142f,"
                + " sum(mc718) as mc718, sum(mc812) as mc812,sum(mc812) as mc812,sum(mc789) as mc789,sum(mc736) as mc736,sum(mc621) as mc621,"
                + " sum(mc921c) as mc921c,sum(mc04) as mc04, "
                + " sum(c01) as c01,sum(mc14c) as mc14c,sum(mc739) as mc739, sum(mc712) as mc712, sum(mc646) as mc646, sum(mc656) as mc656, sum(mc645a) as mc645a, sum(mc655a) as mc655a,"
                + " sum(mc642) as mc642, sum(mc652) as mc652, sum(mc820) as mc820, sum(mc830) as mc820, sum(c194a) as c194a, sum(c194c) as c194c, sum(c195) as c195, sum(c194b) as c194b"
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
                mc718 = mc718 + result.getFloat("mc718");
                mc717a = mc717a + result.getFloat("mc717a");
                mc717b = mc717b + result.getFloat("mc717b");
                mc138 = mc138 + result.getFloat("mc138");
                mc137 = mc137 + result.getFloat("mc137");
                mc07 = mc07 + result.getFloat("mc07");
                mc01 = mc01 + result.getFloat("mc01");
                mc02 = mc02 + result.getFloat("mc02");
                mc140a = mc140a + result.getFloat("mc140a");
                mc142e = mc142e + result.getFloat("mc142e");
                mc142f = mc142f + result.getFloat("mc142f");
                mc718 = mc718 + result.getFloat("mc718");
                mc812 = mc812 + result.getFloat("mc812");
                mc789 = mc789 + result.getFloat("mc789");
                mc736 = mc736 + result.getFloat("mc736");
                mc621 = mc621 + result.getFloat("mc621");
                mc921c = mc921c + result.getFloat("mc921c");
                mc04 = mc04 + result.getFloat("mc04");
                c01 = c01 + result.getFloat("c01");
                mc14c = mc14c + result.getFloat("mc14c");
                mc739 = mc739 + result.getFloat("mc739");
                mc712 = mc712 + result.getFloat("mc712");
                mc646 = mc646 + result.getFloat("mc646");
                mc656 = mc656 + result.getFloat("mc656");
                mc645a = mc645a + result.getFloat("mc645a");
                mc655a = mc655a + result.getFloat("mc655a");
                mc642 = mc642 + result.getFloat("mc642");
                mc652 = mc652 + result.getFloat("mc652");
                mc820 = mc820 + result.getFloat("mc820");
                mc830 = mc830 + result.getFloat("mc830");
                c194a = c194a + result.getFloat("c194a");
                c194c = c194c + result.getFloat("c194c");
                c195 = c195 + result.getFloat("c195");
                c194b = c194b + result.getFloat("c194b");


                String str = result.getString("heure").trim();
                int heure = -1;
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex)
                {
                    heure = -1;
                }
                if (heure == BhCellule && heure >= 0)
                {
                    mc718Bh = mc718Bh + result.getFloat("mc718");
                    mc717aBh = mc717aBh + result.getFloat("mc717a");
                    mc717bBh = mc717bBh + result.getFloat("mc717b");
                    mc812Bh = mc812Bh + result.getFloat("mc812");
                    mc140aBh = mc140aBh + result.getFloat("mc140a");
                    mc14cBh = mc14cBh + result.getFloat("mc14c");
                    mc739Bh = mc739Bh + result.getFloat("mc739");
                    mc736Bh = mc736Bh + result.getFloat("mc736");
                    mc621Bh = mc621Bh + result.getFloat("mc621");
                    mc921cBh = mc921cBh + result.getFloat("mc921c");
                    mc621Bh = mc621Bh + result.getFloat("mc621");
                    mc14cBh = mc14cBh + result.getFloat("mc14c");
                    mc736Bh = mc736Bh + result.getFloat("mc736");
                    mc739Bh = mc739Bh + result.getFloat("mc739");
                    mc921cBh = mc921cBh + result.getFloat("mc921c");
                    mc718Bh = mc718Bh + result.getFloat("mc718");
                    mc717aBh = mc717aBh + result.getFloat("mc717a");
                    mc717bBh = mc717bBh + result.getFloat("mc717b");
                    mc712Bh = mc712Bh + result.getFloat("mc712");
                    mc138Bh = mc138Bh + result.getFloat("mc138");
                    mc137Bh = mc137Bh + result.getFloat("mc137");
                    mc07Bh = mc07Bh + result.getFloat("mc07");
                    mc01Bh = mc01Bh + result.getFloat("mc01");
                    mc02Bh = mc02Bh + result.getFloat("mc02");
                    mc10Bh = mc10Bh + result.getFloat("mc10");
                    c01Bh = c01Bh + result.getFloat("c01");
                    mc04Bh = mc04Bh + result.getFloat("mc04");
                    c194bBh = c194bBh + result.getFloat("c194b");
                    mc645aBh=mc645aBh+result.getFloat("mc645a");
                    mc655aBh=mc655aBh+result.getFloat("mc655a");
                    mc820Bh=mc820Bh+result.getFloat("mc820");
                    mc830Bh=mc830Bh+result.getFloat("mc830");
                    mc646Bh=mc646Bh+result.getFloat("mc646");
                    mc656Bh=mc656Bh+result.getFloat("mc656");
                    mc642Bh=mc642Bh+result.getFloat("mc642");
                    mc652Bh=mc652Bh+result.getFloat("mc652");
                }
            }
                //tchm
            tchm = mc718 + mc717a + mc717b;
            tchm = (double) ((int) (tchm * 10000)) / 10000;

            //tchmbh
            tchmbh = mc718Bh + mc717aBh + mc717bBh;
            tchmbh = (double) ((int) (tchm * 10000)) / 10000;
            //tchhm
            tchhm=tchm/24;
            tchhm = (double) ((int) (tchhm * 10000)) / 10000;

            //tchcr
            if (mc140a != 0)
            {
                tchcr = (mc812 / mc140a) * 100;
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
                if(tchcr<0 || tchcr > 1)
                {
                    tchcr = 1;
                }
            } else
                tchcr = 1;
            //tchcrbh
            if (mc140aBh != 0)
            {
                tchcrbh = (mc812 / mc140a) * 100;
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
                if(tchcrbh<0 || tchcrbh > 1)
                {
                    tchcrbh = 1;
                }
            } else
                tchcrbh = 1;

            //tchdr
            tchdr = mc14c + mc739 + mc736 + mc621 + mc921c;
            tchdr = (double) ((int) (tchdr * 10000)) / 100;
            if (tchdr <0 || tchdr > 1)
            {
                tchdr = 1;
            }
            //tchdrbh
            tchdrbh = mc14cBh + mc739Bh + mc736Bh + mc621Bh + mc921cBh;
            tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
            if (tchdrbh< 0 || tchdrbh> 1)
            {
                tchdrbh = 1;
            }

            //sdcchcr
                if (c01 != 0)
                {
                    sdcchcr = mc04 / c01;
                    sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
                    if (sdcchcr <0 || sdcchcr > 1) {
                        sdcchcr = 1;
                    }
                } else
                    sdcchcr = 1;


                ////sdcchcrbh
                if (c01Bh != 0)
                {
                    sdcchcrbh = mc04 / c01;
                    sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
                    if (sdcchcrbh <0 || sdcchcrbh > 1) {
                        sdcchcrbh = 1;
                    }
                } else
                    sdcchcrbh = 1;

                //sdcchdr
                if (c01 != 0) {
                    sdcchdr = mc04 / c01;
                    sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                    if (sdcchdr <0 || sdcchdr > 1) {
                        sdcchdr = 1;
                    }
                } else
                    sdcchdr = 1;


                //sdcchdrbh
                if (c01Bh != 0) {
                    sdcchdrbh = mc04Bh / c01Bh;
                    sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                    if (sdcchdrbh <0 || sdcchdrbh > 1) {
                            sdcchdrbh = 1;
                    }
                } else
                    sdcchdrbh = 1;


            //sdcchdrbh
            if (c01Bh != 0) {
                sdcchdrbh = mc04Bh / c01Bh;
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh < 0 || sdcchdrbh > 1) {
                    sdcchdrbh = 1;
                }
            } else {
                sdcchdrbh = 1;
            }
            ///cssr
            ///formuel voir à verifier
            cssr = (double) ((int) (cssr * 10000)) / 10000;
                if (cssr <0 || cssr > 1)
                {
                    cssr = 0;
                }
            //cssrbh
             cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
                if (cssrbh <0 || cssrbh > 1)
                {
                    cssrbh = 0;
                }

            //csr
            csr = cssr * (1 - cdr);
            csr = (double) ((int) (csr * 10000)) / 10000;
            if (csr <0 || csr > 1)
            {
                csr = 0;
            }
            //csrbh
            csrbh = cssrbh * (1 - cdrbh);
            csrbh = (double) ((int) (csrbh * 10000)) / 10000;
            if (csrbh< 0 || csrbh> 1)
            {
                csrbh = 0;
            }

            //cdr
            if ((mc718 + (mc717a + mc717b) - mc712) > 0)
            {
                cdr = ((mc621 + mc14c + mc736 + mc739 + mc921c) / (mc718 + (mc717a + mc717b) - mc712));
                cdr = (double) ((int) (cdr * 10000)) / 10000;
                if(cdr < 0 ||cdr > 1)
                {
                    cdr=1;
                }
            } else cdr = 1;


            //cdrbh
            if ((mc718Bh + (mc717aBh + mc717bBh) - mc712Bh) > 0)
            {
                cdrbh = ((mc621Bh + mc14cBh + mc736Bh + mc739Bh + mc921cBh) / (mc718Bh + (mc717aBh + mc717bBh) - mc712Bh));
                cdrbh = (double) ((int) (cdrbh * 10000)) / 10000;
                if(cdrbh < 0 ||cdrbh > 1)
                {
                    cdrbh=1;
                }
             } else
                cdrbh = 1;

            //hosucces
            if (((mc645a + mc655a) != 0) && ((mc820 + mc830) != 0))
            {
                hosucces = (((mc646 + mc656) / (mc645a + mc655a)) + ((mc642 + mc652) / (mc820 + mc830)));
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
                if (hosucces < 0 || hosucces > 1)
                {
                    hosucces= 0;
                }
            } else
                hosucces = 0;

            //hosuccessbh
            if (((mc645aBh + mc655aBh) != 0) && ((mc820Bh + mc830Bh) != 0))
            {
                hosuccesbh = (((mc646 + mc656) / (mc645a + mc655a)) + ((mc642 + mc652) / (mc820 + mc830)));
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
                if (hosuccesbh < 0 || hosuccesbh > 1)
                {
                    hosuccesbh = 0;
                }
            } else
                hosuccesbh = 0;

            //hofailurebh
            hofailurebh=1-hosuccesbh;
                hofailurebh = (double) ((int) (hofailurebh * 10000)) / 10000;
            if (hofailurebh< 0 || hofailurebh > 1) {
                hofailurebh = 0;
            } else {
                hofailurebh = 0;
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
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs=0;
                }
            }
            requete = "select cell_name,heure,sum(mc718) as mc718 ,sum(mc717a) as mc717a,sum(mc717b) as mc717b from tableregistre "
                    + "where cell_name='"+cellule+"' and date>='"+dateDebut+"' and date<='"+dateFin+"'"
                    + " group by cell_name,heure  order by cell_name,heure";

            resultTotal = cn.getResultset(requete);
            while (resultTotal.next())
            {
                int heure=-1;
                String str=resultTotal.getString("heure").trim();
                try
                {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {heure=-1;}
                if(heure>=0)
                {
                    TabBhCellule[heure-1]=heure;
                    TabTchmCellule[heure-1]=TabTchmCellule[heure-1]+(resultTotal.getFloat("mc718"))+(resultTotal.getFloat("mc717a"))+(resultTotal.getFloat("mc717b"));
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
                som=som/(float)(nbreJrsHrs);
            else som=0;
            som=(double)((int)(som*100))/100;
            TchmBhCellule=(double)((int)(TchmBhCellule*100))/100;
            if(BhCellule>=0)
            {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhCellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            }else System.out.println("************************************************************:Cellule:"+cellule+" ,BHfausse:"+BhCellule);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CalculAlcatel2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalCulEricsson2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalCulEricsson2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalCulEricsson2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void finalize()
    {
        try
        {
            super.finalize();
            cn.closeConnection();
        } catch (Throwable ex) {
        }
    }

}
