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

public class CalculZTE2G
{

    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double tchhm = 0;
    private double tchm = 0;
    private double tchmhm = 0;
    private double tchmbh = 0;
    private double[] TabTchmGlobal = new double[24];
    private double cssr = 0, cssrbh = 0;
    private double bhtr = 0;
    private double tchcr = 0;
    private double tchcrbh = 0;
    private double tchdr = 0;
    private double tchdrbh = 0;
    private double sdcchbrbh = 0, sdcchbr = 0;
    private double sdcchcrbh = 0, sdcchcr = 0;
    private double sdcchdrbh = 0, sdcchdr = 0;
    private double csr = 0;
    private double csrbh = 0;
    private double cdr = 0;
    private double cdrbh = 0;
    private double hosucces = 0, hosuccesbh = 0;
    private double HoFailureBh = 0;
    private double TchmBhGlobal = 0, TchmBhRegion = 0;
    private int BhG = -1, BhRegion = -1, BhCellule = -1;
    private int[] TabBhGlobal = new int[24];
    private double tchbr = 0;
    private double tchbrbh = 0;
    private double Rsmsr = 0;
    private double smslr = 0;
    private Map<String, Double> mapTraficBhRegion;
    private Map<String, Integer> mapBhRegion;
    private Map<String, Double> mapTrHoraireMoyenRegion;
    private Map<String, Double> mapTraficBhCellule = null;
    private Map<String, Integer> mapBhCellule = null;
    private Map<String, Double> mapTrHoraireMoyenCellule = null;
    private int idinfo = 0;

    public CalculZTE2G(Operateur operateur, String dateDebut, String dateFin) throws SQLException
    {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
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
            }
            catch (Exception ex)
            {
                Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
                NbreErgRecus = 0;
            }

            /////////////////
            tchm = 0;tchmbh = 0;tchcr = 0;tchdr = 0;
            tchbr = 0;cssr = 0;cssrbh = 0;cdr = 0;
            csr = 0;tchcrbh = 0;tchdrbh = 0;tchbrbh = 0;
            hosucces = 0;hosuccesbh = 0;HoFailureBh = 0;sdcchcrbh = 0;
            sdcchbrbh = 0;sdcchdrbh = 0;sdcchcr = 0;sdcchbr = 0;
            sdcchdr = 0;cdrbh = 0;csrbh = 0;Rsmsr = 0;
            double FullRate = 0, HalfRate = 0;

            double c900060003 = 0, c900060005 = 0, c900060010 = 0, c900060011 = 0, c900060017 = 0, c900060018 = 0, c900060019 = 0, c900060020 = 0, c900060021 = 0, c900060022 = 0,
                    c900060029 = 0, c900060028 = 0, c900060030 = 0, c900060031 = 0, c900060032 = 0, c900060033 = 0, c900060036 = 0, c900060037 = 0, c900060038 = 0, c900060039 = 0,
                    c900060042 = 0, c900060043 = 0, c900060044 = 0, c900060045 = 0, c900060046 = 0, c900060047 = 0, c900060048 = 0, c900060049 = 0,
                    c900060053 = 0, c900060054 = 0, c900060055 = 0, c900060093 = 0, c900060094 = 0, c900060095 = 0, c900060096 = 0, c900060097 = 0,
                    c900060098 = 0, c900060099 = 0, c900060100 = 0, c900060101 = 0, c900060102 = 0, c900060119 = 0, c900060120 = 0, c900060127 = 0,
                    c900060129 = 0, c900060135 = 0, c900060199 = 0, c900060200 = 0, c900060210 = 0, c900060211 = 0, c900060213 = 0, c900060214 = 0, c900060215 = 0, c900060216 = 0, c900060235 = 0, c901070014 = 0,
                    c901070021 = 0, c901070028 = 0, c901070035 = 0, c901070042 = 0, c901070049 = 0;

            double c900060003Bh = 0, c900060005Bh = 0, c900060010Bh = 0, c900060011Bh = 0, c900060017Bh = 0, c900060018Bh = 0, c900060019Bh = 0, c900060020Bh = 0, c900060021Bh = 0, c900060022Bh = 0,
                    c900060029Bh = 0, c900060028Bh = 0, c900060030Bh = 0, c900060031Bh = 0, c900060032Bh = 0, c900060033Bh = 0, c900060036Bh = 0, c900060037Bh = 0, c900060038Bh = 0, c900060039Bh = 0,
                    c900060042Bh = 0, c900060043Bh = 0, c900060044Bh = 0, c900060045Bh = 0, c900060046Bh = 0, c900060047Bh = 0, c900060048Bh = 0, c900060049Bh = 0,
                    c900060053Bh = 0, c900060054Bh = 0, c900060055Bh = 0, c900060093Bh = 0, c900060094Bh = 0, c900060095Bh = 0, c900060096Bh = 0, c900060097Bh = 0,
                    c900060098Bh = 0, c900060099Bh = 0, c900060100Bh = 0, c900060101Bh = 0, c900060102Bh = 0, c900060119Bh = 0, c900060120Bh = 0, c900060127Bh = 0,
                    c900060129Bh = 0, c900060135Bh = 0, c900060199Bh = 0, c900060200Bh = 0, c900060210Bh = 0, c900060211Bh = 0, c900060213Bh = 0, c900060214Bh = 0, c900060215Bh = 0, c900060216Bh = 0, c900060235Bh = 0, c901070014Bh = 0,
                    c901070021Bh = 0, c901070028Bh = 0, c901070035Bh = 0, c901070042Bh = 0, c901070049Bh = 0;

            System.out.println("----------------------**********//////////////////////    Region en cour de calcul:" + region);

            requete = "select region,heure,sum(c900060003) as c900060003,sum(c900060005) as c900060005,sum(c900060010) as c900060010,sum(c900060011) as c900060011,sum(c900060019) as c900060019,sum(c900060020) as c900060020,sum(c900060021) as c900060021,sum(c900060022) as c900060022,"
                    + " sum(c900060028) as c900060028,sum(c900060030) as c900060030, sum(c900060031) as c900060031,sum(c900060032) as c900060032,sum(c900060033) as c900060033,sum(c900060036) as c900060036,sum(c900060038) as c900060038,sum(c900060039) as c900060039,"
                    + " sum(c900060042) as c900060042, sum(c900060043) as c900060043,sum(c900060044) as c900060044,sum(c900060045) as c900060045,sum(c900060046) as c900060046,sum(c900060047) as c900060047,sum(c900060047) as c900060048,sum(c900060047) as c900060049,"
                    + " sum(c900060053) as c900060053, sum(c900060054) as c900060054, sum(c900060055) as c900060055,sum(c900060093) as c900060093,sum(c90006094) as c900060094,sum(c900060095) as c900060095,sum(c900060096) as c900060096,sum(c900060097) as c900060097, "
                    + " sum(c900060098) as c900060098,sum(c900060099) as c900060099,sum(c900060100) as c900060100, sum(c900060101) as c900060101, sum(c900060102) as c900060102, sum(c900060119) as c900060119, sum(c900060120) as c900060120,sum(c900060127) as c900060127,"
                    + " sum(c900060129) as c900060129,sum(c900060199) as c900060199,sum(c900060210) as c900060210, sum(c900060213) as c900060213, sum(c900060214) as c900060214, sum(c900060215) as c900060215, sum(c900060216) as c900060216, sum(c901070014) as c901070014"
                    + " sum(c901070021) as c901070021,sum(c901070028) as c901070028,sum(c901070035) as c901070035, sum(c901070042) as c901070042, sum(c901070049) as c901070049,"
                    + " sum(c900060018) as c900060018,sum(c900060017) as c900060017, sum(c900060029) as c900060029,sum(c900060037) as c900060037,sum(c900060211) as c900060211, sum(c900060235) as c900060235, sum(c900060135) as c900060135, sum(c900060200) as c900060200,"
                    + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "' "
                    + "group by region,heure order by region,heure ; ";

            System.out.println("---------------------------------------------------requte:" + requete);
            result = cn.getResultset(requete);
            while (result.next())
            {
                c900060003 = c900060003 + result.getFloat("c900060003");
                c900060005 = c900060005 + result.getFloat("c900060005");
                c900060010 = c900060010 + result.getFloat("c900060010");
                c900060011 = c900060011 + result.getFloat("c900060011");
                c900060017 = c900060017 + result.getFloat("c900060017");
                c900060018 = c900060018 + result.getFloat("c900060018");
                c900060019 = c900060019 + result.getFloat("c900060019");
                c900060020 = c900060020 + result.getFloat("c900060020");
                c900060021 = c900060021 + result.getFloat("c900060021");
                c900060022 = c900060022 + result.getFloat("c900060022");
                c900060029 = c900060029 + result.getFloat("c900060029");
                c900060028 = c900060028 + result.getFloat("c900060028");
                c900060030 = c900060030 + result.getFloat("c900060030");
                c900060031 = c900060031 + result.getFloat("c900060031");
                c900060032 = c900060032 + result.getFloat("c900060032");
                c900060033 = c900060033 + result.getFloat("c900060033");
                c900060036 = c900060036 + result.getFloat("c900060036");
                c900060037 = c900060037 + result.getFloat("c900060037");
                c900060038 = c900060038 + result.getFloat("c900060038");
                c900060039 = c900060039 + result.getFloat("c900060039");
                c900060042 = c900060042 + result.getFloat("c900060042");
                c900060043 = c900060043 + result.getFloat("c900060043");
                c900060044 = c900060044 + result.getFloat("c900060044");
                c900060045 = c900060045 + result.getFloat("c900060045");
                c900060046 = c900060046 + result.getFloat("c900060046");
                c900060047 = c900060047 + result.getFloat("c900060047");
                c900060048 = c900060048 + result.getFloat("c900060048");
                c900060049 = c900060049 + result.getFloat("c900060049");
                c900060053 = c900060053 + result.getFloat("c900060053");
                c900060054 = c900060054 + result.getFloat("c900060054");
                c900060055 = c900060055 + result.getFloat("c900060055");
                c900060093 = c900060093 + result.getFloat("c900060093");
                c900060094 = c900060094 + result.getFloat("c900060094");

                c900060095 = c900060095 + result.getFloat("c900060095");
                c900060096 = c900060096 + result.getFloat("c900060096");
                c900060097 = c900060097 + result.getFloat("c900060097");
                c900060098 = c900060098 + result.getFloat("c900060098");
                c900060099 = c900060099 + result.getFloat("c900060099");
                c900060100 = c900060100 + result.getFloat("c900060100");
                c900060101 = c900060101 + result.getFloat("c900060101");
                c900060102 = c900060102 + result.getFloat("c900060102");
                c900060119 = c900060119 + result.getFloat("c900060119");
                c900060120 = c900060120 + result.getFloat("c900060120");
                c900060127 = c900060127 + result.getFloat("c900060127");
                c900060129 = c900060129 + result.getFloat("c900060129");
                c900060135 = c900060135 + result.getFloat("c900060135");
                c900060199 = c900060199 + result.getFloat("c900060199");
                c900060200 = c900060200 + result.getFloat("c900060200");
                c900060210 = c900060210 + result.getFloat("c900060210");
                c900060211 = c900060211 + result.getFloat("c900060211");
                c900060213 = c900060213 + result.getFloat("c900060213");
                c900060214 = c900060214 + result.getFloat("c900060214");

                c900060215 = c900060215 + result.getFloat("c900060215");
                c900060216 = c900060216 + result.getFloat("c900060216");
                c900060235 = c900060235 + result.getFloat("c900060235");
                c901070014 = c901070014 + result.getFloat("c901070014");
                c901070021 = c901070021 + result.getFloat("c901070021");
                c901070028 = c901070028 + result.getFloat("c901070028");
                c901070035 = c901070035 + result.getFloat("c901070035");
                c901070042 = c901070042 + result.getFloat("c901070042");
                c901070049 = c901070049 + result.getFloat("c901070049");

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
                    c900060003Bh = result.getFloat("c900060003");
                    c900060005Bh = result.getFloat("c900060005");
                    c900060010Bh = result.getFloat("c900060010");
                    c900060011Bh = result.getFloat("c900060011");
                    c900060017Bh = result.getFloat("c900060017");
                    c900060018Bh = result.getFloat("c900060018");
                    c900060019Bh = result.getFloat("c900060019");
                    c900060020Bh = result.getFloat("c900060020");
                    c900060021Bh = result.getFloat("c900060021");
                    c900060022Bh = result.getFloat("c900060022");
                    c900060029Bh = result.getFloat("c900060029");
                    c900060028Bh = result.getFloat("c900060028");
                    c900060030Bh = result.getFloat("c900060030");
                    c900060031Bh = result.getFloat("c900060031");
                    c900060032Bh = result.getFloat("c900060032");
                    c900060033Bh = result.getFloat("c900060033");
                    c900060036Bh = result.getFloat("c900060036");
                    c900060037Bh = result.getFloat("c900060037");
                    c900060038Bh = result.getFloat("c900060038");
                    c900060039Bh = result.getFloat("c900060039");
                    c900060042Bh = result.getFloat("c900060042");
                    c900060043Bh = result.getFloat("c900060043");
                    c900060044Bh = result.getFloat("c900060044");
                    c900060045Bh = result.getFloat("c900060045");
                    c900060046Bh = result.getFloat("c900060046");
                    c900060047Bh = result.getFloat("c900060047");
                    c900060048Bh = result.getFloat("c900060048");
                    c900060049Bh = result.getFloat("c900060049");
                    c900060053Bh = result.getFloat("c900060053");
                    c900060054Bh = result.getFloat("c900060054");
                    c900060055Bh = result.getFloat("c900060055");
                    c900060093Bh = result.getFloat("c900060093");
                    c900060094Bh = result.getFloat("c900060094");

                    c900060095Bh = result.getFloat("c900060095");
                    c900060096Bh = result.getFloat("c900060096");
                    c900060097Bh = result.getFloat("c900060097");
                    c900060098Bh = result.getFloat("c900060098");
                    c900060099Bh = result.getFloat("c900060099");
                    c900060100Bh = result.getFloat("c900060100");
                    c900060101Bh = result.getFloat("c900060101");
                    c900060102Bh = result.getFloat("c900060102");
                    c900060119Bh = result.getFloat("c900060119");
                    c900060120Bh = result.getFloat("c900060120");
                    c900060127Bh = result.getFloat("c900060127");
                    c900060129Bh = result.getFloat("c900060129");
                    c900060135Bh = result.getFloat("c900060135");
                    c900060199Bh = result.getFloat("c900060199");
                    c900060200Bh = result.getFloat("c900060200");
                    c900060210Bh = result.getFloat("c900060210");
                    c900060211Bh = result.getFloat("c900060211");
                    c900060213Bh = result.getFloat("c900060213");
                    c900060214Bh = result.getFloat("c900060214");

                    c900060215Bh = result.getFloat("c900060215");
                    c900060216Bh = result.getFloat("c900060216");
                    c900060235Bh = result.getFloat("c900060235");
                    c901070014Bh = result.getFloat("c901070014");
                    c901070021Bh = result.getFloat("c901070021");
                    c901070028Bh = result.getFloat("c901070028");
                    c901070035Bh = result.getFloat("c901070035");
                    c901070042Bh = result.getFloat("c901070042");
                    c901070049Bh = result.getFloat("c901070049");

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
            tchm = (c900060129 + c900060127) / 3600;
            tchm = (double) ((int) (tchm * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchm", TauxInfoManq);

            /// calcul de TCHMBH
            tchmbh = (c900060129Bh + c900060129Bh) / 3600;
            tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;
            MapTauxInfoManqRegion.put("tchmbh", TauxInfoManq);

            /////// calcul de CSSR
            if ((c900060003 + c900060010 + c900060038) > 0 && (c900060019 + c900060030 + c900060042 + c900060046) > 0 && (c900060017 + c900060028 + c900060036 + c900060018 + c900060029 + c900060037 + c900060235 + c900060199 + c900060210 + c900060135 + c900060200 + c900060211) > 0)
            {
                cssr = (1 - c900060053 / (c900060003 + c900060010 + c900060038)) * (1 - ((c900060020 + c900060031 + c900060043 + c900060047) / (c900060019 + c900060030 + c900060042 + c900060046))) * (1 - ((c900060018 + c900060029 + c900060037 + c900060211) / (c900060017 + c900060028 + c900060036 + c900060018 + c900060029 + c900060037 + c900060235 + c900060199 + c900060210 + c900060135 + c900060200 + c900060211)));
                cssr = (double) ((int) (tchm * 10000)) / 10000;
                if(cssr>=0 && cssr<=1)
                {
                    MapTauxInfoManqRegion.put("cssr", TauxInfoManq);
                }
                else
                {
                    cssr = 0;
                    MapTauxInfoManqRegion.put("cssr", 100F);
                }
            } else {
                cssr = 0;
                MapTauxInfoManqRegion.put("cssr", 100F);
            }

            /////// calcul de CSSRBH
            if ((c900060003Bh + c900060010Bh + c900060038Bh) > 0 && (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh) > 0 && (c900060017Bh + c900060028Bh + c900060036Bh + c900060018Bh + c900060029Bh + c900060037Bh + c900060235Bh + c900060199Bh + c900060210Bh + c900060135Bh + c900060200Bh + c900060211Bh) > 0)
            {
                cssrbh = (1 - c900060053Bh / (c900060003Bh + c900060010Bh + c900060038Bh)) * (1 - ((c900060020Bh + c900060031Bh + c900060043Bh + c900060047Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh))) * (1 - ((c900060018Bh + c900060029Bh + c900060037Bh + c900060211Bh) / (c900060017Bh + c900060028Bh + c900060036Bh + c900060018Bh + c900060029Bh + c900060037Bh + c900060235Bh + c900060199Bh + c900060210Bh + c900060135Bh + c900060200Bh + c900060211Bh)));
                cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
                
                if(cssrbh>=0 && cssrbh<=1)
                {
                    MapTauxInfoManqRegion.put("cssrbh", TauxInfoManq);
                }
                else
                {
                    cssrbh = 0;
                    MapTauxInfoManqRegion.put("cssrbh", 100F);
                }
            } else
            {
                cssrbh = 0;
                MapTauxInfoManqRegion.put("cssrbh", 100F);
            }

            //////// calcul de CDR

            //////////// calcul CDRBH

            ////// calcul TCHCR
            if ((c900060019 + c900060030 + c900060042 + c900060046 + c900060021 + c900060032 + c900060044 + c900060048) > 0)
            {
                tchcr = (c900060020 + c900060031 + c900060043 + c900060047 + c900060022 + c900060033 + c900060045 + c900060049) / (c900060019 + c900060030 + c900060042 + c900060046 + c900060021 + c900060032 + c900060044 + c900060048);
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
                if(tchcr>=0 && tchcr<=1)
                {
                    MapTauxInfoManqRegion.put("tchcr", TauxInfoManq);
                }else
                {
                    tchcr = 1;
                    MapTauxInfoManqRegion.put("tchcr", 100f);
                }
            } else
            {
                tchcr = 1;
                MapTauxInfoManqRegion.put("tchcr", 100f);
            }

            ////// calcul TCHCRBH
            if ((c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh + c900060021Bh + c900060032Bh + c900060044Bh + c900060048Bh) > 0)
            {
                tchcrbh = (c900060020Bh + c900060031Bh + c900060043Bh + c900060047Bh + c900060022Bh + c900060033Bh + c900060045Bh + c900060049Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh + c900060021Bh + c900060032Bh + c900060044Bh + c900060048Bh);
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
                if (tchcrbh >= 0 && tchcrbh <= 1)
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
            ////////////// calcul de TCHDR
            if ((c900060028 + c900060036 + c900060199 + c900060210 + c900060098 + c900060102 + c900060120) > 0)
            {
                tchdr = (c900060054 + c900060055) / (c900060028 + c900060036 + c900060199 + c900060210 + c900060098 + c900060102 + c900060120);
                tchdr = (double) ((int) (tchdr * 10000)) / 10000;
                if (tchdr >= 0 && tchdr <= 1)
                {
                    MapTauxInfoManqRegion.put("tchdr", TauxInfoManq);
                } else
                {
                    tchdr = 1;
                    MapTauxInfoManqRegion.put("tchdr", 100F);
                }
            } else {
                tchdr = 1;
                MapTauxInfoManqRegion.put("tchdr", 100F);
            }
            //////////// calcul de TCHDRBH
            if ((c900060028Bh + c900060036Bh + c900060199Bh + c900060210Bh + c900060098Bh + c900060102Bh + c900060120Bh) > 0) {
                tchdrbh = (c900060054Bh + c900060055Bh) / (c900060028Bh + c900060036Bh + c900060199Bh + c900060210Bh + c900060098Bh + c900060102Bh + c900060120Bh);
                tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
                if (tchdrbh >= 0 && tchdrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("tchdrbh", TauxInfoManq);
                } else
                {
                    tchdrbh = 1;
                    MapTauxInfoManqRegion.put("tchdrbh", 100F);
                }
            } else {
                tchdrbh = 1;
                MapTauxInfoManqRegion.put("tchdrbh", 100F);
            }

            ////////calcul de SDCCHCR
            if ((c900060003 + c900060010 + c900060038) > 0)
            {
                sdcchcr = (c900060005 + c900060011 + c900060039) / (c900060003 + c900060010 + c900060038);
                sdcchcr = (double) ((int) (sdcchcr * 10000)) / 10000;
                if (sdcchcr >= 0 && sdcchcr <= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchcr", TauxInfoManq);
                } else
                {
                    sdcchcr = 1;
                    MapTauxInfoManqRegion.put("sdcchcr", 100F);
                }
            } else {
                sdcchcr = 1;
                MapTauxInfoManqRegion.put("sdcchcr", 100F);
            }

            ////////calcul de SDCCHCRBH
            if ((c900060003Bh + c900060010Bh + c900060038Bh) > 0)
            {
                sdcchcrbh = (c900060005Bh + c900060011Bh + c900060039Bh) / (c900060003Bh + c900060010Bh + c900060038Bh);
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
                if (sdcchcrbh >= 0 && sdcchcrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("sdcchcrbh", TauxInfoManq);
                } else {
                    sdcchcrbh = 1;
                    MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
                }
            } else {
                sdcchcrbh = 1;
                MapTauxInfoManqRegion.put("sdcchcrbh", 100F);
            }

            /////////// calcul de SDCCHDR
            if ((c900060019 + c900060030 + c900060042 + c900060046) > 0)
            {
                sdcchdr = (c901070014 + c901070021 + c901070028 + c901070035 + c901070042 + c901070049) / (c900060019 + c900060030 + c900060042 + c900060046);
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr >= 0 && sdcchdr <= 1) {
                    MapTauxInfoManqRegion.put("sdcchdr", TauxInfoManq);
                } else {
                    sdcchdr = 1;
                    MapTauxInfoManqRegion.put("sdcchdr", 100F);
                }
            } else
            {
                sdcchdr = 1;
                MapTauxInfoManqRegion.put("sdcchdr", 100F);
            }

            /////////// calcul de SDCCHDRBH
            if ((c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh) > 0) {
                sdcchdrbh = (c901070014Bh + c901070021Bh + c901070028Bh + c901070035Bh + c901070042Bh + c901070049Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh);
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh >=0 && sdcchdrbh <= 1) {
                    MapTauxInfoManqRegion.put("sdcchdrbh", TauxInfoManq);
                } else 
                {
                    sdcchdrbh = 1;
                    MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
                }
            } else {
                sdcchdrbh = 1;
                MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
            }

            ////////////// Calcul de HOSucces
            if ((c900060097 + c900060213 + c900060214 + c900060215 + c900060099 + c900060010 + c900060119 + c900060093 + c900060095) > 0) {
                hosucces= (c900060098 + c900060102 + c900060120 + c900060094 + c900060096) / (c900060097 + c900060213 + c900060214 + c900060215 + c900060099 + c900060010 + c900060119 + c900060093 + c900060095);
                hosucces= (double) ((int) (hosucces * 10000)) / 10000;
                if (hosucces >= 0 && hosucces <= 1) {
                    MapTauxInfoManqRegion.put("HoSucces", TauxInfoManq);
                } else {
                    hosucces = 0;
                    MapTauxInfoManqRegion.put("HoSucces", 100F);
                }
            } else {
                hosucces = 0;
                MapTauxInfoManqRegion.put("HoSucces", 100F);
            }

            ////////////// Calcul de HOSuccesBh
            if ((c900060097Bh + c900060213Bh + c900060214Bh + c900060215Bh + c900060099Bh + c900060010Bh + c900060119Bh + c900060093Bh + c900060095Bh) > 0) {
                hosuccesbh = (c900060098Bh + c900060102Bh + c900060120Bh + c900060094Bh + c900060096Bh) / (c900060097Bh + c900060213Bh + c900060214Bh + c900060215Bh + c900060099Bh + c900060010Bh + c900060119Bh + c900060093Bh + c900060095Bh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
                if (hosuccesbh >= 0 && hosuccesbh <= 1) {
                    MapTauxInfoManqRegion.put("HoSuccesBh", TauxInfoManq);
                } else {
                    hosuccesbh = 0;
                    MapTauxInfoManqRegion.put("HoSuccesBh", 100F);
                }
            } else {
                hosuccesbh = 0;
                MapTauxInfoManqRegion.put("HoSuccesBh", 100F);
            }

            ////////////// Calcul de HOFailureBh
            HoFailureBh = 1 - hosuccesbh;
            hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;

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
                } else {
                    bhtr = 1;
                }
                bhtr = (double) ((int) (bhtr * 10000)) / 10000;
                if (bhtr == 1)
                {
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

                    requeteInsert = requeteInsert + cle.toLowerCase() + ",";
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
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
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

        /////////////////
        tchm = 0;tchmbh = 0;tchcr = 0;tchdr = 0;
        tchbr = 0;cssr = 0;cssrbh = 0;cdr = 0;
        csr = 0;tchcrbh = 0;tchdrbh = 0;tchbrbh = 0;
        hosucces = 0;hosuccesbh = 0;HoFailureBh = 0;sdcchcrbh = 0;
        sdcchbrbh = 0;sdcchdrbh = 0; sdcchcr = 0;sdcchbr = 0;
        sdcchdr = 0;cdrbh = 0;csrbh = 0;Rsmsr = 0;

        double c900060003 = 0, c900060005 = 0, c900060010 = 0, c900060011 = 0, c900060017 = 0, c900060018 = 0, c900060019 = 0, c900060020 = 0, c900060021 = 0, c900060022 = 0,
                c900060029 = 0, c900060028 = 0, c900060030 = 0, c900060031 = 0, c900060032 = 0, c900060033 = 0, c900060036 = 0, c900060037 = 0, c900060038 = 0, c900060039 = 0,
                c900060042 = 0, c900060043 = 0, c900060044 = 0, c900060045 = 0, c900060046 = 0, c900060047 = 0, c900060048 = 0, c900060049 = 0,
                c900060053 = 0, c900060054 = 0, c900060055 = 0, c900060093 = 0, c900060094 = 0, c900060095 = 0, c900060096 = 0, c900060097 = 0,
                c900060098 = 0, c900060099 = 0, c900060100 = 0, c900060101 = 0, c900060102 = 0, c900060119 = 0, c900060120 = 0, c900060127 = 0,
                c900060129 = 0, c900060135 = 0, c900060199 = 0, c900060200 = 0, c900060210 = 0, c900060211 = 0, c900060213 = 0, c900060214 = 0, c900060215 = 0, c900060216 = 0, c900060235 = 0, c901070014 = 0,
                c901070021 = 0, c901070028 = 0, c901070035 = 0, c901070042 = 0, c901070049 = 0;

        double c900060003Bh = 0, c900060005Bh = 0, c900060010Bh = 0, c900060011Bh = 0, c900060017Bh = 0, c900060018Bh = 0, c900060019Bh = 0, c900060020Bh = 0, c900060021Bh = 0, c900060022Bh = 0,
                c900060029Bh = 0, c900060028Bh = 0, c900060030Bh = 0, c900060031Bh = 0, c900060032Bh = 0, c900060033Bh = 0, c900060036Bh = 0, c900060037Bh = 0, c900060038Bh = 0, c900060039Bh = 0,
                c900060042Bh = 0, c900060043Bh = 0, c900060044Bh = 0, c900060045Bh = 0, c900060046Bh = 0, c900060047Bh = 0, c900060048Bh = 0, c900060049Bh = 0,
                c900060053Bh = 0, c900060054Bh = 0, c900060055Bh = 0, c900060093Bh = 0, c900060094Bh = 0, c900060095Bh = 0, c900060096Bh = 0, c900060097Bh = 0,
                c900060098Bh = 0, c900060099Bh = 0, c900060100Bh = 0, c900060101Bh = 0, c900060102Bh = 0, c900060119Bh = 0, c900060120Bh = 0, c900060127Bh = 0,
                c900060129Bh = 0, c900060135Bh = 0, c900060199Bh = 0, c900060200Bh = 0, c900060210Bh = 0, c900060211Bh = 0, c900060213Bh = 0, c900060214Bh = 0, c900060215Bh = 0, c900060216Bh = 0, c900060235Bh = 0, c901070014Bh = 0,
                c901070021Bh = 0, c901070028Bh = 0, c901070035Bh = 0, c901070042Bh = 0, c901070049Bh = 0;

        String requete = "select region,heure,sum(c900060003) as c900060003,sum(c900060005) as c900060005,sum(c900060010) as c900060010,sum(c900060011) as c900060011,sum(c900060019) as c900060019,sum(c900060020) as c900060020,sum(c900060021) as c900060021,sum(c900060022) as c900060022,"
                + " sum(c900060028) as c900060028,sum(c900060030) as c900060030, sum(c900060031) as c900060031,sum(c900060032) as c900060032,sum(c900060033) as c900060033,sum(c900060036) as c900060036,sum(c900060038) as c900060038,sum(c900060039) as c900060039,"
                + " sum(c900060042) as c900060042, sum(c900060043) as c900060043,sum(c900060044) as c900060044,sum(c900060045) as c900060045,sum(c900060046) as c900060046,sum(c900060047) as c900060047,sum(c900060047) as c900060048,sum(c900060047) as c900060049,"
                + " sum(c900060053) as c900060053, sum(c900060054) as c900060054, sum(c900060055) as c900060055,sum(c900060093) as c900060093,sum(c90006094) as c900060094,sum(c900060095) as c900060095,sum(c900060096) as c900060096,sum(c900060097) as c900060097, "
                + " sum(c900060098) as c900060098,sum(c900060099) as c900060099,sum(c900060100) as c900060100, sum(c900060101) as c900060101, sum(c900060102) as c900060102, sum(c900060119) as c900060119, sum(c900060120) as c900060120,sum(c900060127) as c900060127,"
                + " sum(c900060129) as c900060129,sum(c900060199) as c900060199,sum(c900060210) as c900060210, sum(c900060213) as c900060213, sum(c900060214) as c900060214, sum(c900060215) as c900060215, sum(c900060216) as c900060216, sum(c901070014) as c901070014"
                + " sum(c901070021) as c901070021,sum(c901070028) as c901070028,sum(c901070035) as c901070035, sum(c901070042) as c901070042, sum(c901070049) as c901070049,"
                + " sum(c900060018) as c900060018,sum(c900060017) as c900060017, sum(c900060029) as c900060029,sum(c900060037) as c900060037,sum(c900060211) as c900060211, sum(c900060235) as c900060235, sum(c900060135) as c900060135, sum(c900060200) as c900060200,"
                + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' group by heure";
        System.out.println("---------------------------------------------------requete Globale:" + requete);
        ResultSet result = cn.getResultset(requete);
        try {
            while (result.next()) {

                c900060003 = c900060003 + result.getFloat("c900060003");
                c900060005 = c900060005 + result.getFloat("c900060005");
                c900060010 = c900060010 + result.getFloat("c900060010");
                c900060011 = c900060011 + result.getFloat("c900060011");
                c900060017 = c900060017 + result.getFloat("c900060017");
                c900060018 = c900060018 + result.getFloat("c900060018");
                c900060019 = c900060019 + result.getFloat("c900060019");
                c900060020 = c900060020 + result.getFloat("c900060020");
                c900060021 = c900060021 + result.getFloat("c900060021");
                c900060022 = c900060022 + result.getFloat("c900060022");
                c900060029 = c900060029 + result.getFloat("c900060029");
                c900060028 = c900060028 + result.getFloat("c900060028");
                c900060030 = c900060030 + result.getFloat("c900060030");
                c900060031 = c900060031 + result.getFloat("c900060031");
                c900060032 = c900060032 + result.getFloat("c900060032");
                c900060033 = c900060033 + result.getFloat("c900060033");
                c900060036 = c900060036 + result.getFloat("c900060036");
                c900060037 = c900060037 + result.getFloat("c900060037");
                c900060038 = c900060038 + result.getFloat("c900060038");
                c900060039 = c900060039 + result.getFloat("c900060039");
                c900060042 = c900060042 + result.getFloat("c900060042");
                c900060043 = c900060043 + result.getFloat("c900060043");
                c900060044 = c900060044 + result.getFloat("c900060044");
                c900060045 = c900060045 + result.getFloat("c900060045");
                c900060046 = c900060046 + result.getFloat("c900060046");
                c900060047 = c900060047 + result.getFloat("c900060047");
                c900060048 = c900060048 + result.getFloat("c900060048");
                c900060049 = c900060049 + result.getFloat("c900060049");
                c900060053 = c900060053 + result.getFloat("c900060053");
                c900060054 = c900060054 + result.getFloat("c900060054");
                c900060055 = c900060055 + result.getFloat("c900060055");
                c900060093 = c900060093 + result.getFloat("c900060093");
                c900060094 = c900060094 + result.getFloat("c900060094");

                c900060095 = c900060095 + result.getFloat("c900060095");
                c900060096 = c900060096 + result.getFloat("c900060096");
                c900060097 = c900060097 + result.getFloat("c900060097");
                c900060098 = c900060098 + result.getFloat("c900060098");
                c900060099 = c900060099 + result.getFloat("c900060099");
                c900060100 = c900060100 + result.getFloat("c900060100");
                c900060101 = c900060101 + result.getFloat("c900060101");
                c900060102 = c900060102 + result.getFloat("c900060102");
                c900060119 = c900060119 + result.getFloat("c900060119");
                c900060120 = c900060120 + result.getFloat("c900060120");
                c900060127 = c900060127 + result.getFloat("c900060127");
                c900060129 = c900060129 + result.getFloat("c900060129");
                c900060135 = c900060135 + result.getFloat("c900060135");
                c900060199 = c900060199 + result.getFloat("c900060199");
                c900060200 = c900060200 + result.getFloat("c900060200");
                c900060210 = c900060210 + result.getFloat("c900060210");
                c900060211 = c900060211 + result.getFloat("c900060211");
                c900060213 = c900060213 + result.getFloat("c900060213");
                c900060214 = c900060214 + result.getFloat("c900060214");

                c900060215 = c900060215 + result.getFloat("c900060215");
                c900060216 = c900060216 + result.getFloat("c900060216");
                c900060235 = c900060235 + result.getFloat("c900060235");
                c901070014 = c901070014 + result.getFloat("c901070014");
                c901070021 = c901070021 + result.getFloat("c901070021");
                c901070028 = c901070028 + result.getFloat("c901070028");
                c901070035 = c901070035 + result.getFloat("c901070035");
                c901070042 = c901070042 + result.getFloat("c901070042");
                c901070049 = c901070049 + result.getFloat("c901070049");

                int heure = -1;
                String str = result.getString("heure").split(":")[0];
                try {
                    heure = Integer.parseInt(str);
                } catch (Exception ex) {
                    heure = -1;
                }
                if (heure >= 0 && heure == BhG) {
                    c900060003Bh = result.getFloat("c900060003");
                    c900060005Bh = result.getFloat("c900060005");
                    c900060010Bh = result.getFloat("c900060010");
                    c900060011Bh = result.getFloat("c900060011");
                    c900060017Bh = result.getFloat("c900060017");
                    c900060018Bh = result.getFloat("c900060018");
                    c900060019Bh = result.getFloat("c900060019");
                    c900060020Bh = result.getFloat("c900060020");
                    c900060021Bh = result.getFloat("c900060021");
                    c900060022Bh = result.getFloat("c900060022");
                    c900060029Bh = result.getFloat("c900060029");
                    c900060028Bh = result.getFloat("c900060028");
                    c900060030Bh = result.getFloat("c900060030");
                    c900060031Bh = result.getFloat("c900060031");
                    c900060032Bh = result.getFloat("c900060032");
                    c900060033Bh = result.getFloat("c900060033");
                    c900060036Bh = result.getFloat("c900060036");
                    c900060037Bh = result.getFloat("c900060037");
                    c900060038Bh = result.getFloat("c900060038");
                    c900060039Bh = result.getFloat("c900060039");
                    c900060042Bh = result.getFloat("c900060042");
                    c900060043Bh = result.getFloat("c900060043");
                    c900060044Bh = result.getFloat("c900060044");
                    c900060045Bh = result.getFloat("c900060045");
                    c900060046Bh = result.getFloat("c900060046");
                    c900060047Bh = result.getFloat("c900060047");
                    c900060048Bh = result.getFloat("c900060048");
                    c900060049Bh = result.getFloat("c900060049");
                    c900060053Bh = result.getFloat("c900060053");
                    c900060054Bh = result.getFloat("c900060054");
                    c900060055Bh = result.getFloat("c900060055");
                    c900060093Bh = result.getFloat("c900060093");
                    c900060094Bh = result.getFloat("c900060094");

                    c900060095Bh = result.getFloat("c900060095");
                    c900060096Bh = result.getFloat("c900060096");
                    c900060097Bh = result.getFloat("c900060097");
                    c900060098Bh = result.getFloat("c900060098");
                    c900060099Bh = result.getFloat("c900060099");
                    c900060100Bh = result.getFloat("c900060100");
                    c900060101Bh = result.getFloat("c900060101");
                    c900060102Bh = result.getFloat("c900060102");
                    c900060119Bh = result.getFloat("c900060119");
                    c900060120Bh = result.getFloat("c900060120");
                    c900060127Bh = result.getFloat("c900060127");
                    c900060129Bh = result.getFloat("c900060129");
                    c900060135Bh = result.getFloat("c900060135");
                    c900060199Bh = result.getFloat("c900060199");
                    c900060200Bh = result.getFloat("c900060200");
                    c900060210Bh = result.getFloat("c900060210");
                    c900060211Bh = result.getFloat("c900060211");
                    c900060213Bh = result.getFloat("c900060213");
                    c900060214Bh = result.getFloat("c900060214");

                    c900060215Bh = result.getFloat("c900060215");
                    c900060216Bh = result.getFloat("c900060216");
                    c900060235Bh = result.getFloat("c900060235");
                    c901070014Bh = result.getFloat("c901070014");
                    c901070021Bh = result.getFloat("c901070021");
                    c901070028Bh = result.getFloat("c901070028");
                    c901070035Bh = result.getFloat("c901070035");
                    c901070042Bh = result.getFloat("c901070042");
                    c901070049Bh = result.getFloat("c901070049");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        }

        //////calcul du TCHM

        tchm = (c900060129 + c900060127) / 3600;
        tchm = (double) ((int) (tchm * 10000)) / 10000;

        /// calcul de TCHMBH

        tchmbh = (c900060129Bh + c900060129Bh) / 3600;
        tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;

        /////// calcul de CSSR
        if ((c900060003 + c900060010 + c900060038) > 0 && (c900060019 + c900060030 + c900060042 + c900060046) > 0 && (c900060017 + c900060028 + c900060036 + c900060018 + c900060029 + c900060037 + c900060235 + c900060199 + c900060210 + c900060135 + c900060200 + c900060211) > 0) {
            cssr = (1 - c900060053 / (c900060003 + c900060010 + c900060038)) * (1 - ((c900060020 + c900060031 + c900060043 + c900060047) / (c900060019 + c900060030 + c900060042 + c900060046))) * (1 - ((c900060018 + c900060029 + c900060037 + c900060211) / (c900060017 + c900060028 + c900060036 + c900060018 + c900060029 + c900060037 + c900060235 + c900060199 + c900060210 + c900060135 + c900060200 + c900060211)));
            cssr = (double) ((int) (tchm * 10000)) / 10000;
        } else {
            cssr = 0;
        }

        /////// calcul de CSSRBH
        if ((c900060003Bh + c900060010Bh + c900060038Bh) > 0 && (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh) > 0 && (c900060017Bh + c900060028Bh + c900060036Bh + c900060018Bh + c900060029Bh + c900060037Bh + c900060235Bh + c900060199Bh + c900060210Bh + c900060135Bh + c900060200Bh + c900060211Bh) > 0) {
            cssrbh = (1 - c900060053Bh / (c900060003Bh + c900060010Bh + c900060038Bh)) * (1 - ((c900060020Bh + c900060031Bh + c900060043Bh + c900060047Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh))) * (1 - ((c900060018Bh + c900060029Bh + c900060037Bh + c900060211Bh) / (c900060017Bh + c900060028Bh + c900060036Bh + c900060018Bh + c900060029Bh + c900060037Bh + c900060235Bh + c900060199Bh + c900060210Bh + c900060135Bh + c900060200Bh + c900060211Bh)));
            cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
        } else {
            cssrbh = 0;
        }

        //////// calcul de CDR

        //////////// calcul CDRBH

        ////// calcul TCHCR
        if ((c900060019 + c900060030 + c900060042 + c900060046 + c900060021 + c900060032 + c900060044 + c900060048) > 0) {
            tchcr = (c900060020 + c900060031 + c900060043 + c900060047 + c900060022 + c900060033 + c900060045 + c900060049) / (c900060019 + c900060030 + c900060042 + c900060046 + c900060021 + c900060032 + c900060044 + c900060048);
            tchcr = (double) ((int) (tchcr * 10000)) / 10000;
        } else {
            tchcr = 1;
        }

        ////// calcul TCHCRBH
        if ((c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh + c900060021Bh + c900060032Bh + c900060044Bh + c900060048Bh) > 0) {
            tchcrbh = (c900060020Bh + c900060031Bh + c900060043Bh + c900060047Bh + c900060022Bh + c900060033Bh + c900060045Bh + c900060049Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh + c900060021Bh + c900060032Bh + c900060044Bh + c900060048Bh);
            tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
        } else {
            tchcrbh = 1;
        }

        ////////////// calcul de TCHDR
        if ((c900060028 + c900060036 + c900060199 + c900060210 + c900060098 + c900060102 + c900060120) > 0) {
            sdcchcrbh = (c900060054 + c900060055) / (c900060028 + c900060036 + c900060199 + c900060210 + c900060098 + c900060102 + c900060120);
            sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
        } else {
            sdcchcrbh = 1;
        }

        //////////// calcul de TCHDRBH
        if ((c900060028Bh + c900060036Bh + c900060199Bh + c900060210Bh + c900060098Bh + c900060102Bh + c900060120Bh) > 0) {
            sdcchcrbh = (c900060054Bh + c900060055Bh) / (c900060028Bh + c900060036Bh + c900060199Bh + c900060210Bh + c900060098Bh + c900060102Bh + c900060120Bh);
            sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
        } else {
            sdcchcrbh = 1;
        }

        ////////calcul de SDCCHCRBH
        if ((c900060003Bh + c900060010Bh + c900060038Bh) > 0) {
            sdcchcrbh = (c900060005Bh + c900060011Bh + c900060039Bh) / (c900060003Bh + c900060010Bh + c900060038Bh);
            sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
        } else {
            sdcchcrbh = 1;
        }

        /////////// calcul de SDCCHDRBH
        if ((c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh) > 0) {
            sdcchdrbh = (c901070014Bh + c901070021Bh + c901070028Bh + c901070035Bh + c901070042Bh + c901070049Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh);
            sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
        } else {
            sdcchdrbh = 1;
        }

        ////////////// Calcul de HOSucces
        if ((c900060097 + c900060213 + c900060214 + c900060215 + c900060099 + c900060010 + c900060119 + c900060093 + c900060095) > 0) {
            hosucces = (c900060098 + c900060102 + c900060120 + c900060094 + c900060096) / (c900060097 + c900060213 + c900060214 + c900060215 + c900060099 + c900060010 + c900060119 + c900060093 + c900060095);
            hosucces= (double) ((int) (hosucces * 10000)) / 10000;
        } else {
            hosucces = 0;
        }

        ////////////// Calcul de HOSuccesBh
        if ((c900060097Bh + c900060213Bh + c900060214Bh + c900060215Bh + c900060099Bh + c900060010Bh + c900060119Bh + c900060093Bh + c900060095Bh) > 0) {
            hosuccesbh = (c900060098Bh + c900060102Bh + c900060120Bh + c900060094Bh + c900060096Bh) / (c900060097Bh + c900060213Bh + c900060214Bh + c900060215Bh + c900060099Bh + c900060010Bh + c900060119Bh + c900060093Bh + c900060095Bh);
            hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
        } else {
            hosuccesbh = 0;
        }

        ////////////// Calcul de HOFailureBh
        HoFailureBh = 1 - hosuccesbh;
        hosuccesbh= (double) ((int) (hosuccesbh * 10000)) / 10000;

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

    public void getBhParRegion(String region) {
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
                    Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs = 0;
                }
            }
            //String requete = "select * from tableregistre where trim(region)='"+region+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select region,heure,sum(cr3553) as cr3553 ,sum(cr3554) as cr3554,sum(r3590) as r3590 from tableregistre "
                    + "where region='" + region + "' and date>='" + dateDebut + "' and date<='" + dateFin + "'"
                    + " group by region,heure  order by region,heure";
            resultTotal = cn.getResultset(requete);
            while (resultTotal.next()) {
                double val1 = 0;
                String str = resultTotal.getString("heure").trim();
                int heure = -1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {
                    heure = -1;
                }
                try {
                    val1 = resultTotal.getFloat("r3590");
                } catch (Exception ex) {
                    val1 = 0;
                }

                if (val1 > 0 && heure >= 0) {
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
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                resultTotal.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void CalCulBHParRegion(List<String> lstRegion) {
        for (int i = 0; i < 24; i++) {
            TabBhGlobal[i] = -1;
            TabTchmGlobal[i] = 0;
        }
        try {
            mapTraficBhRegion = new HashMap<String, Double>();
            mapBhRegion = new HashMap<String, Integer>();
            mapTrHoraireMoyenRegion = new HashMap<String, Double>();
            int nbreRegion = lstRegion.size();
            for (int i = 0; i < nbreRegion; i++) {
                String region = lstRegion.get(i);
                ////calcul de la Busy Hour par région
                getBhParRegion(region);
            }
            /***Calcul de Tchm & bh global de tout le réseau sur la période ****/
            int n = TabTchmGlobal.length;
            TchmBhGlobal = 0;
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
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getBHParCellule(String cellule) {
        int TabBhCellule[] = new int[24];
        double TabTchmCellule[] = new double[24];
        for (int i = 0; i < 24; i++) {
            TabBhCellule[i] = -1;
            TabTchmCellule[i] = 0;
        }
        try {
            ResultSet resultTotal = null;
            int nbreJrsHrs = 0;
            //String requete="select count(*) as nbre  from tableregistre where date>='"+dateDebut+"' and date<='"+dateFin+"' and cell_name='"+cellule+"'";
            String requete = "select count(*)*(select count(*) as nbre from (select distinct(heure) from tableregistre  "
                    + "where date>='" + dateDebut + "' and date<='" + dateFin + "' and cell_name='" + cellule + "' order by heure) query) as nbre from (select distinct(date) "
                    + "from tableregistre  where date>='" + dateDebut + "' and date<='" + dateFin + "' and cell_name='" + cellule + "') query";
            resultTotal = cn.getResultset(requete);
            if (resultTotal.next()) {
                try {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                } catch (Exception ex) {
                    Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
                    nbreJrsHrs = 0;
                }
            }

            //requete = "select * from tableregistre where trim(cell_name)='"+cellule+"' and date>='" + dateDebut + "' and date<='" + dateFin + "'";
            requete = "select cell_name,heure,sum(cr3553) as cr3553 ,sum(cr3554) as cr3554,sum(r3590) as r3590 from tableregistre "
                    + "where cell_name='" + cellule + "' and date>='" + dateDebut + "' and date<='" + dateFin + "'"
                    + " group by cell_name,heure  order by cell_name,heure";

            resultTotal = cn.getResultset(requete);
            while (resultTotal.next()) {
                double val1 = 0, val2 = 0;
                int heure = -1;
                String str = resultTotal.getString("heure").trim();
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {
                    heure = -1;
                }

                try {
                    val1 = resultTotal.getFloat("r3590");
                } catch (Exception ex) {
                    val1 = 0;
                }
                if (val1 > 0 && heure >= 0) {
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
            for (int i = 0; i < n; i++) {
                som = som + TabTchmCellule[i];
                if (TabTchmCellule[i] > TchmBhCellule) {
                    TchmBhCellule = TabTchmCellule[i];
                    BhCellule = i + 1;
                }
            }
            if (nbreJrsHrs > 0) {
                som = som / (float) (nbreJrsHrs);
            } else {
                som = 0;
            }
            som = (double) ((int) (som * 100)) / 100;
            TchmBhCellule = (double) ((int) (TchmBhCellule * 100)) / 100;
            if (BhCellule >= 0) {
                mapBhCellule.put(cellule, BhCellule);
                mapTraficBhCellule.put(cellule, TchmBhCellule);
                mapTrHoraireMoyenCellule.put(cellule, som);
            } else {
                System.out.println("************************************************************:Cellule:" + cellule + " ,BHfausse:" + BhCellule);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculBHParCellule(List<String> lstCellules) {
        try {
            mapTraficBhCellule = new HashMap<String, Double>();
            mapBhCellule = new HashMap<String, Integer>();
            mapTrHoraireMoyenCellule = new HashMap<String, Double>();

            int nbreRegion = lstCellules.size();
            for (int i = 0; i < nbreRegion; i++) {
                String cellule = lstCellules.get(i);
                ////calcul de la Busy Hour par région
                getBHParCellule(cellule);
            }
        } catch (Exception ex) {
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculCellule(String cellule)
    {
        try
        {
            tchm = 0;tchmbh = 0;tchcr = 0;tchcrbh = 0;
            tchdr = 0;tchdrbh = 0;tchbr = 0;tchbrbh = 0;
            cssr = 0;sdcchcrbh = 0;sdcchdrbh = 0;sdcchbrbh = 0;
            cdr = 0;cdrbh = 0;csr = 0;csrbh = 0;Rsmsr = 0;
            sdcchcr = 0;sdcchdr = 0;sdcchbr = 0;cssrbh = 0;
            HoFailureBh = 0;hosucces = 0;hosuccesbh = 0;

            double c900060003 = 0, c900060005 = 0, c900060010 = 0, c900060011 = 0, c900060017 = 0, c900060018 = 0, c900060019 = 0, c900060020 = 0, c900060021 = 0, c900060022 = 0,
                    c900060029 = 0, c900060028 = 0, c900060030 = 0, c900060031 = 0, c900060032 = 0, c900060033 = 0, c900060036 = 0, c900060037 = 0, c900060038 = 0, c900060039 = 0,
                    c900060042 = 0, c900060043 = 0, c900060044 = 0, c900060045 = 0, c900060046 = 0, c900060047 = 0, c900060048 = 0, c900060049 = 0,
                    c900060053 = 0, c900060054 = 0, c900060055 = 0, c900060093 = 0, c900060094 = 0, c900060095 = 0, c900060096 = 0, c900060097 = 0,
                    c900060098 = 0, c900060099 = 0, c900060100 = 0, c900060101 = 0, c900060102 = 0, c900060119 = 0, c900060120 = 0, c900060127 = 0,
                    c900060129 = 0, c900060135 = 0, c900060199 = 0, c900060200 = 0, c900060210 = 0, c900060211 = 0, c900060213 = 0, c900060214 = 0, c900060215 = 0, c900060216 = 0, c900060235 = 0, c901070014 = 0,
                    c901070021 = 0, c901070028 = 0, c901070035 = 0, c901070042 = 0, c901070049 = 0;

            double c900060003Bh = 0, c900060005Bh = 0, c900060010Bh = 0, c900060011Bh = 0, c900060017Bh = 0, c900060018Bh = 0, c900060019Bh = 0, c900060020Bh = 0, c900060021Bh = 0, c900060022Bh = 0,
                    c900060029Bh = 0, c900060028Bh = 0, c900060030Bh = 0, c900060031Bh = 0, c900060032Bh = 0, c900060033Bh = 0, c900060036Bh = 0, c900060037Bh = 0, c900060038Bh = 0, c900060039Bh = 0,
                    c900060042Bh = 0, c900060043Bh = 0, c900060044Bh = 0, c900060045Bh = 0, c900060046Bh = 0, c900060047Bh = 0, c900060048Bh = 0, c900060049Bh = 0,
                    c900060053Bh = 0, c900060054Bh = 0, c900060055Bh = 0, c900060093Bh = 0, c900060094Bh = 0, c900060095Bh = 0, c900060096Bh = 0, c900060097Bh = 0,
                    c900060098Bh = 0, c900060099Bh = 0, c900060100Bh = 0, c900060101Bh = 0, c900060102Bh = 0, c900060119Bh = 0, c900060120Bh = 0, c900060127Bh = 0,
                    c900060129Bh = 0, c900060135Bh = 0, c900060199Bh = 0, c900060200Bh = 0, c900060210Bh = 0, c900060211Bh = 0, c900060213Bh = 0, c900060214Bh = 0, c900060215Bh = 0, c900060216Bh = 0, c900060235Bh = 0, c901070014Bh = 0,
                    c901070021Bh = 0, c901070028Bh = 0, c901070035Bh = 0, c901070042Bh = 0, c901070049Bh = 0;

            String requete = "select region,heure,sum(c900060003) as c900060003,sum(c900060005) as c900060005,sum(c900060010) as c900060010,sum(c900060011) as c900060011,sum(c900060019) as c900060019,sum(c900060020) as c900060020,sum(c900060021) as c900060021,sum(c900060022) as c900060022,"
                    + " sum(c900060028) as c900060028,sum(c900060030) as c900060030, sum(c900060031) as c900060031,sum(c900060032) as c900060032,sum(c900060033) as c900060033,sum(c900060036) as c900060036,sum(c900060038) as c900060038,sum(c900060039) as c900060039,"
                    + " sum(c900060042) as c900060042, sum(c900060043) as c900060043,sum(c900060044) as c900060044,sum(c900060045) as c900060045,sum(c900060046) as c900060046,sum(c900060047) as c900060047,sum(c900060047) as c900060048,sum(c900060047) as c900060049,"
                    + " sum(c900060053) as c900060053, sum(c900060054) as c900060054, sum(c900060055) as c900060055,sum(c900060093) as c900060093,sum(c90006094) as c900060094,sum(c900060095) as c900060095,sum(c900060096) as c900060096,sum(c900060097) as c900060097, "
                    + " sum(c900060098) as c900060098,sum(c900060099) as c900060099,sum(c900060100) as c900060100, sum(c900060101) as c900060101, sum(c900060102) as c900060102, sum(c900060119) as c900060119, sum(c900060120) as c900060120,sum(c900060127) as c900060127,"
                    + " sum(c900060129) as c900060129,sum(c900060199) as c900060199,sum(c900060210) as c900060210, sum(c900060213) as c900060213, sum(c900060214) as c900060214, sum(c900060215) as c900060215, sum(c900060216) as c900060216, sum(c901070014) as c901070014"
                    + " sum(c901070021) as c901070021,sum(c901070028) as c901070028,sum(c901070035) as c901070035, sum(c901070042) as c901070042, sum(c901070049) as c901070049,"
                    + " sum(c900060018) as c900060018,sum(c900060017) as c900060017, sum(c900060029) as c900060029,sum(c900060037) as c900060037,sum(c900060211) as c900060211, sum(c900060235) as c900060235, sum(c900060135) as c900060135, sum(c900060200) as c900060200,"
                    + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' and cell_name='" + cellule + "' "
                    + "group by cell_name,heure order by cell_name,heure ; ";

            ResultSet result = null;
            if (mapBhCellule.get(cellule) != null) {
                BhCellule = mapBhCellule.get(cellule);
            } else {
                BhCellule = -1;
            }
            result = cn.getResultset(requete);
            while (result.next()) {
                c900060003 = c900060003 + result.getFloat("c900060003");
                c900060005 = c900060005 + result.getFloat("c900060005");
                c900060010 = c900060010 + result.getFloat("c900060010");
                c900060011 = c900060011 + result.getFloat("c900060011");
                c900060017 = c900060017 + result.getFloat("c900060017");
                c900060018 = c900060018 + result.getFloat("c900060018");
                c900060019 = c900060019 + result.getFloat("c900060019");
                c900060020 = c900060020 + result.getFloat("c900060020");
                c900060021 = c900060021 + result.getFloat("c900060021");
                c900060022 = c900060022 + result.getFloat("c900060022");
                c900060029 = c900060029 + result.getFloat("c900060029");
                c900060028 = c900060028 + result.getFloat("c900060028");
                c900060030 = c900060030 + result.getFloat("c900060030");
                c900060031 = c900060031 + result.getFloat("c900060031");
                c900060032 = c900060032 + result.getFloat("c900060032");
                c900060033 = c900060033 + result.getFloat("c900060033");
                c900060036 = c900060036 + result.getFloat("c900060036");
                c900060037 = c900060037 + result.getFloat("c900060037");
                c900060038 = c900060038 + result.getFloat("c900060038");
                c900060039 = c900060039 + result.getFloat("c900060039");
                c900060042 = c900060042 + result.getFloat("c900060042");
                c900060043 = c900060043 + result.getFloat("c900060043");
                c900060044 = c900060044 + result.getFloat("c900060044");
                c900060045 = c900060045 + result.getFloat("c900060045");
                c900060046 = c900060046 + result.getFloat("c900060046");
                c900060047 = c900060047 + result.getFloat("c900060047");
                c900060048 = c900060048 + result.getFloat("c900060048");
                c900060049 = c900060049 + result.getFloat("c900060049");
                c900060053 = c900060053 + result.getFloat("c900060053");
                c900060054 = c900060054 + result.getFloat("c900060054");
                c900060055 = c900060055 + result.getFloat("c900060055");
                c900060093 = c900060093 + result.getFloat("c900060093");
                c900060094 = c900060094 + result.getFloat("c900060094");

                c900060095 = c900060095 + result.getFloat("c900060095");
                c900060096 = c900060096 + result.getFloat("c900060096");
                c900060097 = c900060097 + result.getFloat("c900060097");
                c900060098 = c900060098 + result.getFloat("c900060098");
                c900060099 = c900060099 + result.getFloat("c900060099");
                c900060100 = c900060100 + result.getFloat("c900060100");
                c900060101 = c900060101 + result.getFloat("c900060101");
                c900060102 = c900060102 + result.getFloat("c900060102");
                c900060119 = c900060119 + result.getFloat("c900060119");
                c900060120 = c900060120 + result.getFloat("c900060120");
                c900060127 = c900060127 + result.getFloat("c900060127");
                c900060129 = c900060129 + result.getFloat("c900060129");
                c900060135 = c900060135 + result.getFloat("c900060135");
                c900060199 = c900060199 + result.getFloat("c900060199");
                c900060200 = c900060200 + result.getFloat("c900060200");
                c900060210 = c900060210 + result.getFloat("c900060210");
                c900060211 = c900060211 + result.getFloat("c900060211");
                c900060213 = c900060213 + result.getFloat("c900060213");
                c900060214 = c900060214 + result.getFloat("c900060214");

                c900060215 = c900060215 + result.getFloat("c900060215");
                c900060216 = c900060216 + result.getFloat("c900060216");
                c900060235 = c900060235 + result.getFloat("c900060235");
                c901070014 = c901070014 + result.getFloat("c901070014");
                c901070021 = c901070021 + result.getFloat("c901070021");
                c901070028 = c901070028 + result.getFloat("c901070028");
                c901070035 = c901070035 + result.getFloat("c901070035");
                c901070042 = c901070042 + result.getFloat("c901070042");
                c901070049 = c901070049 + result.getFloat("c901070049");


                String str = result.getString("heure").trim();
                int heure = -1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {
                    heure = -1;
                }
                if (heure == BhCellule && heure >= 0) {
                    c900060003Bh = result.getFloat("c900060003");
                    c900060005Bh = result.getFloat("c900060005");
                    c900060010Bh = result.getFloat("c900060010");
                    c900060011Bh = result.getFloat("c900060011");
                    c900060017Bh = result.getFloat("c900060017");
                    c900060018Bh = result.getFloat("c900060018");
                    c900060019Bh = result.getFloat("c900060019");
                    c900060020Bh = result.getFloat("c900060020");
                    c900060021Bh = result.getFloat("c900060021");
                    c900060022Bh = result.getFloat("c900060022");
                    c900060029Bh = result.getFloat("c900060029");
                    c900060028Bh = result.getFloat("c900060028");
                    c900060030Bh = result.getFloat("c900060030");
                    c900060031Bh = result.getFloat("c900060031");
                    c900060032Bh = result.getFloat("c900060032");
                    c900060033Bh = result.getFloat("c900060033");
                    c900060036Bh = result.getFloat("c900060036");
                    c900060037Bh = result.getFloat("c900060037");
                    c900060038Bh = result.getFloat("c900060038");
                    c900060039Bh = result.getFloat("c900060039");
                    c900060042Bh = result.getFloat("c900060042");
                    c900060043Bh = result.getFloat("c900060043");
                    c900060044Bh = result.getFloat("c900060044");
                    c900060045Bh = result.getFloat("c900060045");
                    c900060046Bh = result.getFloat("c900060046");
                    c900060047Bh = result.getFloat("c900060047");
                    c900060048Bh = result.getFloat("c900060048");
                    c900060049Bh = result.getFloat("c900060049");
                    c900060053Bh = result.getFloat("c900060053");
                    c900060054Bh = result.getFloat("c900060054");
                    c900060055Bh = result.getFloat("c900060055");
                    c900060093Bh = result.getFloat("c900060093");
                    c900060094Bh = result.getFloat("c900060094");

                    c900060095Bh = result.getFloat("c900060095");
                    c900060096Bh = result.getFloat("c900060096");
                    c900060097Bh = result.getFloat("c900060097");
                    c900060098Bh = result.getFloat("c900060098");
                    c900060099Bh = result.getFloat("c900060099");
                    c900060100Bh = result.getFloat("c900060100");
                    c900060101Bh = result.getFloat("c900060101");
                    c900060102Bh = result.getFloat("c900060102");
                    c900060119Bh = result.getFloat("c900060119");
                    c900060120Bh = result.getFloat("c900060120");
                    c900060127Bh = result.getFloat("c900060127");
                    c900060129Bh = result.getFloat("c900060129");
                    c900060135Bh = result.getFloat("c900060135");
                    c900060199Bh = result.getFloat("c900060199");
                    c900060200Bh = result.getFloat("c900060200");
                    c900060210Bh = result.getFloat("c900060210");
                    c900060211Bh = result.getFloat("c900060211");
                    c900060213Bh = result.getFloat("c900060213");
                    c900060214Bh = result.getFloat("c900060214");

                    c900060215Bh = result.getFloat("c900060215");
                    c900060216Bh = result.getFloat("c900060216");
                    c900060235Bh = result.getFloat("c900060235");
                    c901070014Bh = result.getFloat("c901070014");
                    c901070021Bh = result.getFloat("c901070021");
                    c901070028Bh = result.getFloat("c901070028");
                    c901070035Bh = result.getFloat("c901070035");
                    c901070042Bh = result.getFloat("c901070042");
                    c901070049Bh = result.getFloat("c901070049");

                }
            }

            //////calcul du TCHM

            tchm = (c900060129 + c900060127) / 3600;
            tchm = (double) ((int) (tchm * 10000)) / 10000;

            /// calcul de TCHMBH

            tchmbh = (c900060129Bh + c900060129Bh) / 3600;
            tchmbh = (double) ((int) (tchmbh * 10000)) / 10000;

            /////// calcul de CSSR
            if ((c900060003 + c900060010 + c900060038) > 0 && (c900060019 + c900060030 + c900060042 + c900060046) > 0 && (c900060017 + c900060028 + c900060036 + c900060018 + c900060029 + c900060037 + c900060235 + c900060199 + c900060210 + c900060135 + c900060200 + c900060211) > 0) {
                cssr = (1 - c900060053 / (c900060003 + c900060010 + c900060038)) * (1 - ((c900060020 + c900060031 + c900060043 + c900060047) / (c900060019 + c900060030 + c900060042 + c900060046))) * (1 - ((c900060018 + c900060029 + c900060037 + c900060211) / (c900060017 + c900060028 + c900060036 + c900060018 + c900060029 + c900060037 + c900060235 + c900060199 + c900060210 + c900060135 + c900060200 + c900060211)));
                cssr = (double) ((int) (tchm * 10000)) / 10000;
            } else {
                cssr = 0;
            }

            /////// calcul de CSSRBH
            if ((c900060003Bh + c900060010Bh + c900060038Bh) > 0 && (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh) > 0 && (c900060017Bh + c900060028Bh + c900060036Bh + c900060018Bh + c900060029Bh + c900060037Bh + c900060235Bh + c900060199Bh + c900060210Bh + c900060135Bh + c900060200Bh + c900060211Bh) > 0) {
                cssrbh = (1 - c900060053Bh / (c900060003Bh + c900060010Bh + c900060038Bh)) * (1 - ((c900060020Bh + c900060031Bh + c900060043Bh + c900060047Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh))) * (1 - ((c900060018Bh + c900060029Bh + c900060037Bh + c900060211Bh) / (c900060017Bh + c900060028Bh + c900060036Bh + c900060018Bh + c900060029Bh + c900060037Bh + c900060235Bh + c900060199Bh + c900060210Bh + c900060135Bh + c900060200Bh + c900060211Bh)));
                cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
            } else {
                cssrbh = 0;
            }

            //////// calcul de CDR

            //////////// calcul CDRBH

            ////// calcul TCHCR
            if ((c900060019 + c900060030 + c900060042 + c900060046 + c900060021 + c900060032 + c900060044 + c900060048) > 0) {
                tchcr = (c900060020 + c900060031 + c900060043 + c900060047 + c900060022 + c900060033 + c900060045 + c900060049) / (c900060019 + c900060030 + c900060042 + c900060046 + c900060021 + c900060032 + c900060044 + c900060048);
                tchcr = (double) ((int) (tchcr * 10000)) / 10000;
            } else {
                tchcr = 1;
            }

            ////// calcul TCHCRBH
            if ((c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh + c900060021Bh + c900060032Bh + c900060044Bh + c900060048Bh) > 0) {
                tchcrbh = (c900060020Bh + c900060031Bh + c900060043Bh + c900060047Bh + c900060022Bh + c900060033Bh + c900060045Bh + c900060049Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh + c900060021Bh + c900060032Bh + c900060044Bh + c900060048Bh);
                tchcrbh = (double) ((int) (tchcrbh * 10000)) / 10000;
            } else {
                tchcrbh = 1;
            }

            ////////////// calcul de TCHDR

            if ((c900060028 + c900060036 + c900060199 + c900060210 + c900060098 + c900060102 + c900060120) > 0) {
                sdcchcrbh = (c900060054 + c900060055) / (c900060028 + c900060036 + c900060199 + c900060210 + c900060098 + c900060102 + c900060120);
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
            } else {
                sdcchcrbh = 1;
            }

            //////////// calcul de TCHDRBH

            if ((c900060028Bh + c900060036Bh + c900060199Bh + c900060210Bh + c900060098Bh + c900060102Bh + c900060120Bh) > 0) {
                sdcchcrbh = (c900060054Bh + c900060055Bh) / (c900060028Bh + c900060036Bh + c900060199Bh + c900060210Bh + c900060098Bh + c900060102Bh + c900060120Bh);
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
            } else {
                sdcchcrbh = 1;
            }

            ////////calcul de SDCCHCRBH
            if ((c900060003Bh + c900060010Bh + c900060038Bh) > 0) {
                sdcchcrbh = (c900060005Bh + c900060011Bh + c900060039Bh) / (c900060003Bh + c900060010Bh + c900060038Bh);
                sdcchcrbh = (double) ((int) (sdcchcrbh * 10000)) / 10000;
            } else {
                sdcchcrbh = 1;
            }

            /////////// calcul de SDCCHDRBH

            if ((c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh) > 0) {
                sdcchdrbh = (c901070014Bh + c901070021Bh + c901070028Bh + c901070035Bh + c901070042Bh + c901070049Bh) / (c900060019Bh + c900060030Bh + c900060042Bh + c900060046Bh);
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
            } else {
                sdcchdrbh = 1;
            }

            ////////////// Calcul de HOSucces

            if ((c900060097 + c900060213 + c900060214 + c900060215 + c900060099 + c900060010 + c900060119 + c900060093 + c900060095) > 0) {
                hosucces = (c900060098 + c900060102 + c900060120 + c900060094 + c900060096) / (c900060097 + c900060213 + c900060214 + c900060215 + c900060099 + c900060010 + c900060119 + c900060093 + c900060095);
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
            } else {
                hosucces= 0;
            }

            ////////////// Calcul de HOSuccesBh
            if ((c900060097Bh + c900060213Bh + c900060214Bh + c900060215Bh + c900060099Bh + c900060010Bh + c900060119Bh + c900060093Bh + c900060095Bh) > 0) {
                hosuccesbh = (c900060098Bh + c900060102Bh + c900060120Bh + c900060094Bh + c900060096Bh) / (c900060097Bh + c900060213Bh + c900060214Bh + c900060215Bh + c900060099Bh + c900060010Bh + c900060119Bh + c900060093Bh + c900060095Bh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;

            } else {
                hosuccesbh = 0;
            }

            ////////////// Calcul de HOFailureBh
            HoFailureBh = 1 - hosuccesbh;
            HoFailureBh = (double) ((int) (HoFailureBh * 10000)) / 10000;


            Object obj = mapBhCellule.get(cellule);
            Object obj1 = mapTrHoraireMoyenCellule.get(cellule);
            Object obj2 = mapTraficBhCellule.get(cellule);

            if (obj != null && obj1 != null && obj2 != null) {
                tchhm = mapTrHoraireMoyenCellule.get(cellule);
                tchmbh = mapTraficBhCellule.get(cellule);
                if (tchm > 0) {
                    bhtr = tchmbh / tchm;
                } else {
                    bhtr = 1;
                }
                bhtr = (double) ((int) (bhtr * 100)) / 100;
                InsertTableCellule(cellule);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void CalculTotal() {
        System.out.println("Debut calcul total Ericsson");
        ResultSet result = null;
        try {
            List<String> lstRegion = new ArrayList<String>();
            String requete = "select distinct(region) from table_regions";
            result = cn.getResultset(requete);
            while (result.next()) {
                lstRegion.add(result.getString("region").trim());
                //System.out.println("Liste:" + lstRegion);
            }
            CalCulBHParRegion(lstRegion);
            int n = lstRegion.size();
            for (int i = 0; i < n; i++) {
                CalculRegion(lstRegion.get(i));
            }
            CalculValeurGlobales();
            if (BhG >= 0) {
                BhRegion = BhG;
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
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculZTE2G.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                result.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        cn.closeConnection();
    }
    
}
