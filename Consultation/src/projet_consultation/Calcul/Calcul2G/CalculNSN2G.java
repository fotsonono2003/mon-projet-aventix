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

public class CalculNSN2G {

    private ConnexionBDDOperateur cn;
    private String dateDebut;
    private String dateFin;
    private double tchhm = 0;
    private double tchm = 0;
    private double tchmbh = 0;
    private double[] TabTchmGlobal = new double[24];
    private double cssr = 0;
    private double cssrbh = 0;
    private double bhtr = 0;
    private double tchcr = 0;
    private double tchcrbh = 0;
    private double tchdr = 0;
    private double tchdrbh = 0;
    private double sdcchbr = 0;
    private double sdcchcr = 0;
    private double sdcchdr = 0;
    private double sdcchbrbh = 0;
    private double sdcchcrbh = 0;
    private double sdcchdrbh = 0;
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

    public CalculNSN2G(Operateur operateur, String dateDebut, String dateFin) throws SQLException
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
                Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
                NbreErgRecus = 0;
            }

            /////////////////
            tchm = 0;tchmbh = 0;tchcr = 0;
            tchdr = 0;tchbr = 0;cssr = 0;
            cdr = 0;csr = 0;tchcrbh = 0;
            tchdrbh = 0;tchbrbh = 0;hosucces = 0;
            hosuccesbh = 0;HoFailureBh = 0;sdcchcrbh = 0;
            sdcchbrbh = 0;sdcchdrbh = 0;sdcchcr = 0;
            sdcchbr = 0;sdcchdr = 0;cdrbh = 0;
            csrbh = 0;Rsmsr = 0;
            double FullRate = 0, HalfRate = 0;

            double ho_bsc_i_sdcch_tch = 0, ho_bsc_I_sdcch_tch_at = 0, ho_bsc_i_unsucc_a_int_circ_type = 0, ho_bsc_o_fail_lack = 0, ho_bsc_o_sdcch_at = 0, ho_bsc_o_sdcch_tch = 0, ho_bsc_o_sdcch_tch_at = 0, ho_bsc_o_succ_ho = 0,
                    ho_bsc_o_tch_tch_at = 0, ho_cell_fail_lack = 0, ho_cell_sdcch_at = 0, ho_cell_sdcch_tch = 0, ho_cell_sdcch_tch_at = 0, ho_cell_succ_ho = 0, ho_cell_tch_tch_at = 0, ho_ho_unsucc_a_int_circ_type = 0, ho_msc_controlled_in_ho = 0,
                    ho_msc_i_sdcch_tch = 0, ho_msc_I_sdcch_tch_at = 0, ho_msc_o_fail_lack = 0, ho_msc_o_sdcch_at = 0, ho_msc_o_sdcch_tch = 0, ho_msc_o_sdcch_tch_at = 0, ho_msc_o_succ_ho = 0, ho_msc_o_tch_tch_at = 0, pcu_packet_ch_req = 0, res_access_ch_req_msg_rec = 0,
                    res_access_ghost_ccch_res = 0, res_access_rej_seiz_att_due_dist = 0, rxqual = 0, service_tch_ho_assign = 0, service_tch_new_call_assign = 0, traffic_ms_tch_succ_seiz_assign_cmplt = 0, traffic_sdcch_a_if_fail_call = 0, traffic_sdcch_a_if_fail_old = 0,
                    traffic_sdcch_abis_fail_call = 0, traffic_sdcch_abis_fail_old = 0, traffic_sdcch_assign = 0, traffic_sdcch_bcsu_reset = 0, traffic_sdcch_bts_fail = 0, traffic_sdcch_busy_att = 0, traffic_sdcch_ho_seiz = 0, traffic_sdcch_lapd_fail = 0, traffic_sdcch_netw_act = 0,
                    traffic_sdcch_radio_fail = 0, traffic_sdcch_rf_old_ho = 0, traffic_sdcch_seiz_att = 0, traffic_sdcch_user_act = 0, traffic_tch_abis_fail_old = 0, traffic_tch_bcsu_reset = 0, traffic_tch_bts_fail = 0, traffic_tch_call_req = 0, traffic_tch_lapd_fail = 0,
                    traffic_tch_netw_act = 0, traffic_tch_norm_seiz = 0, traffic_tch_radio_fail = 0, traffic_tch_rej_due_req_ch_a_if_crc = 0, traffic_tch_rel_due_rad_fail_ph_2_3 = 0, traffic_tch_seiz_due_sdcch_con = 0, traffic_tch_succ_seiz_for_dir_acc = 0, traffic_tch_tr_fail_old = 0;

            double ho_bsc_i_sdcch_tchBh = 0, ho_bsc_I_sdcch_tch_atBh = 0, ho_bsc_i_unsucc_a_int_circ_typeBh = 0, ho_bsc_o_fail_lackBh = 0, ho_bsc_o_sdcch_atBh = 0, ho_bsc_o_sdcch_tchBh = 0, ho_bsc_o_sdcch_tch_atBh = 0, ho_bsc_o_succ_hoBh = 0,
                    ho_bsc_o_tch_tch_atBh = 0, ho_cell_fail_lackBh = 0, ho_cell_sdcch_atBh = 0, ho_cell_sdcch_tchBh = 0, ho_cell_sdcch_tch_atBh = 0, ho_cell_succ_hoBh = 0, ho_cell_tch_tch_atBh = 0, ho_ho_unsucc_a_int_circ_typeBh = 0, ho_msc_controlled_in_hoBh = 0,
                    ho_msc_i_sdcch_tchBh = 0, ho_msc_I_sdcch_tch_atBh = 0, ho_msc_o_fail_lackBh = 0, ho_msc_o_sdcch_atBh = 0, ho_msc_o_sdcch_tchBh = 0, ho_msc_o_sdcch_tch_atBh = 0, ho_msc_o_succ_hoBh = 0, ho_msc_o_tch_tch_atBh = 0, pcu_packet_ch_reqBh = 0, res_access_ch_req_msg_recBh = 0,
                    res_access_ghost_ccch_resBh = 0, res_access_rej_seiz_att_due_distBh = 0, rxqualBh = 0, service_tch_ho_assignBh = 0, service_tch_new_call_assignBh = 0, traffic_ms_tch_succ_seiz_assign_cmpltBh = 0, traffic_sdcch_a_if_fail_callBh = 0, traffic_sdcch_a_if_fail_oldBh = 0,
                    traffic_sdcch_abis_fail_callBh = 0, traffic_sdcch_abis_fail_oldBh = 0, traffic_sdcch_assignBh = 0, traffic_sdcch_bcsu_resetBh = 0, traffic_sdcch_bts_failBh = 0, traffic_sdcch_busy_attBh = 0, traffic_sdcch_ho_seizBh = 0, traffic_sdcch_lapd_failBh = 0, traffic_sdcch_netw_actBh = 0,
                    traffic_sdcch_radio_failBh = 0, traffic_sdcch_rf_old_hoBh = 0, traffic_sdcch_seiz_attBh = 0, traffic_sdcch_user_actBh = 0, traffic_tch_abis_fail_oldBh = 0, traffic_tch_bcsu_resetBh = 0, traffic_tch_bts_failBh = 0, traffic_tch_call_reqBh = 0, traffic_tch_lapd_failBh = 0,
                    traffic_tch_netw_actBh = 0, traffic_tch_norm_seizBh = 0, traffic_tch_radio_failBh = 0, traffic_tch_rej_due_req_ch_a_if_crcBh = 0, traffic_tch_rel_due_rad_fail_ph_2_3Bh = 0, traffic_tch_seiz_due_sdcch_conBh = 0, traffic_tch_succ_seiz_for_dir_accBh = 0, traffic_tch_tr_fail_oldBh = 0;

            System.out.println("----------------------**********//////////////////////    Region en cour de calcul:" + region);

            requete = "select region,heure,sum(ho.bsc_i_sdcch_tch) as ho.bsc_i_sdcch_tch,sum(ho.bsc_I_sdcch_tch_at) as ho.bsc_I_sdcch_tch_at,sum(ho.bsc_i_unsucc_a_int_circ_type) as ho.bsc_i_unsucc_a_int_circ_type,sum(ho.bsc_o_fail_lack) as ho.bsc_o_fail_lack,sum(ho.bsc_o_sdcch_at) as ho.bsc_o_sdcch_at,sum(ho.bsc_o_sdcch_tch) as ho.bsc_o_sdcch_tch,sum(ho.bsc_o_sdcch_tch_at) as ho.bsc_o_sdcch_tch_at,sum(ho.bsc_o_succ_ho) as ho.bsc_o_succ_ho,"
                    + " sum(ho.bsc_o_tch_tch_at) as ho.bsc_o_tch_tch_at,sum(ho.cell_fail_lack) as ho.cell_fail_lack, sum(ho.cell_sdcch_at) as ho.cell_sdcch_at,sum(ho.cell_sdcch_tch) as ho.cell_sdcch_tch,sum(ho.cell_sdcch_tch_at) as ho.cell_sdcch_tch_at,sum(ho.cell_succ_ho) as ho.cell_succ_ho,sum(ho.cell_tch_tch_at) as ho.cell_tch_tch_at,sum(ho.ho_unsucc_a_int_circ_type) as ho.ho_unsucc_a_int_circ_type,"
                    + " sum(ho.msc_controlled_in_ho) as ho.msc_controlled_in_ho, sum(ho.msc_i_sdcch_tch) as ho.msc_i_sdcch_tch,sum(ho.msc_I_sdcch_tch_at) as ho.msc_I_sdcch_tch_at,sum(ho.msc_o_fail_lack) as ho.msc_o_fail_lack,sum(ho.msc_o_sdcch_at) as ho.msc_o_sdcch_at,sum(ho.msc_o_sdcch_tch) as ho.msc_o_sdcch_tch,sum(ho.msc_o_sdcch_tch_at) as ho.msc_o_sdcch_tch_at,sum(ho.msc_o_succ_ho) as ho.msc_o_succ_ho,"
                    + " sum(ho.msc_o_tch_tch_at) as ho.msc_o_tch_tch_at, sum(pcu.packet_ch_req) as pcu.packet_ch_req, sum(res_access.ch_req_msg_rec) as res_access.ch_req_msg_rec,sum(res_access.ghost_ccch_res) as res_access.ghost_ccch_res,sum(res_access.rej_seiz_att_due_dist) as res_access.rej_seiz_att_due_dist,sum(rxqual) as rxqual,sum(service.tch_ho_assign) as service.tch_ho_assign,sum(service.tch_new_call_assign) as service.tch_new_call_assign, "
                    + " sum(traffic.ms_tch_succ_seiz_assign_cmplt) as traffic.ms_tch_succ_seiz_assign_cmplt,sum(traffic.sdcch_a_if_fail_call) as traffic.sdcch_a_if_fail_call,sum(traffic.sdcch_a_if_fail_old) as traffic.sdcch_a_if_fail_old, sum(traffic.sdcch_abis_fail_call) as traffic.sdcch_abis_fail_call, sum(traffic.sdcch_abis_fail_old) as traffic.sdcch_abis_fail_old, sum(traffic.sdcch_assign) as traffic.sdcch_assign, sum(traffic.sdcch_bcsu_reset) as traffic.sdcch_bcsu_reset,sum(traffic.sdcch_bts_fail) as traffic.sdcch_bts_fail,"
                    + " sum(traffic.sdcch_busy_att) as traffic.sdcch_busy_att,sum(traffic.sdcch_ho_seiz) as traffic.sdcch_ho_seiz,sum(traffic.sdcch_lapd_fail) as traffic.sdcch_lapd_fail, sum(traffic.sdcch_netw_act) as traffic.sdcch_netw_act, sum(traffic.sdcch_radio_fail) as traffic.sdcch_radio_fail, sum(traffic.sdcch_rf_old_ho) as traffic.sdcch_rf_old_ho, sum(traffic.sdcch_seiz_att) as traffic.sdcch_seiz_att, sum(traffic.sdcch_user_act) as traffic.sdcch_user_act"
                    + " sum(traffic.tch_abis_fail_old) as traffic.tch_abis_fail_old,sum(traffic.tch_bcsu_reset) as traffic.tch_bcsu_reset,sum(traffic.tch_bts_fail) as traffic.tch_bts_fail, sum(traffic.tch_call_req) as traffic.tch_call_req, sum(traffic.tch_lapd_fail) as traffic.tch_lapd_fail,sum(traffic.tch_netw_act) as traffic.tch_netw_act, sum(traffic.tch_norm_seiz) as traffic.tch_norm_seiz, sum(traffic.tch_radio_fail) as traffic.tch_radio_fail, sum(traffic.tch_radio_fail) as traffic.tch_radio_fail,"
                    + " sum(traffic.tch_rel_due_rad_fail_ph_2_3) as traffic.tch_rel_due_rad_fail_ph_2_3,sum(traffic.tch_seiz_due_sdcch_con) as traffic.tch_seiz_due_sdcch_con, sum(traffic.tch_succ_seiz_for_dir_acc) as traffic.tch_succ_seiz_for_dir_acc,sum(traffic.tch_tr_fail_old) as traffic.tch_tr_fail_old,"
                    + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' and region='" + region + "' "
                    + "group by region,heure order by region,heure ; ";

            System.out.println("---------------------------------------------------requte:" + requete);
            result = cn.getResultset(requete);

            while (result.next())
            {
                ho_bsc_i_sdcch_tch = ho_bsc_i_sdcch_tch + result.getFloat("ho.bsc_i_sdcch_tch");
                ho_bsc_I_sdcch_tch_at = ho_bsc_I_sdcch_tch_at + result.getFloat("ho.bsc_I_sdcch_tch_at");
                ho_bsc_i_unsucc_a_int_circ_type = ho_bsc_i_unsucc_a_int_circ_type + result.getFloat("ho.bsc_i_unsucc_a_int_circ_type");
                ho_bsc_o_fail_lack = ho_bsc_o_fail_lack + result.getFloat("ho.bsc_o_fail_lack");
                ho_bsc_o_sdcch_at = ho_bsc_o_sdcch_at + result.getFloat("ho.bsc_o_sdcch_at");
                ho_bsc_o_sdcch_tch = ho_bsc_o_sdcch_tch + result.getFloat("ho.bsc_o_sdcch_tch");
                ho_bsc_o_sdcch_tch_at = ho_bsc_o_sdcch_tch_at + result.getFloat("ho.bsc_o_sdcch_tch_at");
                ho_bsc_o_succ_ho = ho_bsc_o_succ_ho + result.getFloat("ho.bsc_o_succ_ho");
                ho_bsc_o_tch_tch_at = ho_bsc_o_tch_tch_at + result.getFloat("ho.bsc_o_tch_tch_at");
                ho_cell_fail_lack = ho_cell_fail_lack + result.getFloat("ho.cell_fail_lack");
                ho_cell_sdcch_at = ho_cell_sdcch_at + result.getFloat("ho.cell_sdcch_at");
                ho_cell_sdcch_tch = ho_cell_sdcch_tch + result.getFloat("ho.cell_sdcch_tch");
                ho_cell_sdcch_tch_at = ho_cell_sdcch_tch_at + result.getFloat("ho.cell_sdcch_tch_at");
                ho_cell_succ_ho = ho_cell_succ_ho + result.getFloat("ho.cell_succ_ho");
                ho_cell_tch_tch_at = ho_cell_tch_tch_at + result.getFloat("ho.cell_tch_tch_at");
                ho_ho_unsucc_a_int_circ_type = ho_ho_unsucc_a_int_circ_type + result.getFloat("ho.ho_unsucc_a_int_circ_type");
                ho_msc_controlled_in_ho = ho_msc_controlled_in_ho + result.getFloat("ho.msc_controlled_in_ho");
                ho_msc_i_sdcch_tch = ho_msc_i_sdcch_tch + result.getFloat("ho.msc_i_sdcch_tch");
                ho_msc_I_sdcch_tch_at = ho_msc_I_sdcch_tch_at + result.getFloat("ho.msc_I_sdcch_tch_at");
                ho_msc_o_fail_lack = ho_msc_o_fail_lack + result.getFloat("ho.msc_o_fail_lack");
                ho_msc_o_sdcch_at = ho_msc_o_sdcch_at + result.getFloat("ho.msc_o_sdcch_at");
                ho_msc_o_sdcch_tch = ho_msc_o_sdcch_tch + result.getFloat("ho.msc_o_sdcch_tch");
                ho_msc_o_sdcch_tch_at = ho_msc_o_sdcch_tch_at + result.getFloat("ho.msc_o_sdcch_tch_at");
                ho_msc_o_succ_ho = ho_msc_o_succ_ho + result.getFloat("ho.msc_o_succ_ho");
                ho_msc_o_tch_tch_at = ho_msc_o_tch_tch_at + result.getFloat("ho.msc_o_tch_tch_at");
                pcu_packet_ch_req = pcu_packet_ch_req + result.getFloat("pcu.packet_ch_req");
                res_access_ch_req_msg_rec = res_access_ch_req_msg_rec + result.getFloat("res_access.ch_req_msg_rec");
                res_access_ghost_ccch_res = res_access_ghost_ccch_res + result.getFloat("res_access.ghost_ccch_res");
                res_access_rej_seiz_att_due_dist = res_access_rej_seiz_att_due_dist + result.getFloat("res_access.rej_seiz_att_due_dist");
                rxqual = rxqual + result.getFloat("rxqual");
                service_tch_ho_assign = service_tch_ho_assign + result.getFloat("service.tch_ho_assign");
                service_tch_new_call_assign = service_tch_new_call_assign + result.getFloat("service.tch_new_call_assign");
                traffic_ms_tch_succ_seiz_assign_cmplt = traffic_ms_tch_succ_seiz_assign_cmplt + result.getFloat("traffic.ms_tch_succ_seiz_assign_cmplt");

                traffic_sdcch_a_if_fail_call = traffic_sdcch_a_if_fail_call + result.getFloat("traffic.sdcch_a_if_fail_call");
                traffic_sdcch_a_if_fail_old = traffic_sdcch_a_if_fail_old + result.getFloat("traffic.sdcch_a_if_fail_old");
                traffic_sdcch_abis_fail_call = traffic_sdcch_abis_fail_call + result.getFloat("traffic.sdcch_abis_fail_call");
                traffic_sdcch_abis_fail_old = traffic_sdcch_abis_fail_old + result.getFloat("traffic.sdcch_abis_fail_old");
                traffic_sdcch_assign = traffic_sdcch_assign + result.getFloat("traffic.sdcch_assign");
                traffic_sdcch_bcsu_reset = traffic_sdcch_bcsu_reset + result.getFloat("traffic.sdcch_bcsu_reset");
                traffic_sdcch_bts_fail = traffic_sdcch_bts_fail + result.getFloat("traffic.sdcch_bts_fail");
                traffic_sdcch_busy_att = traffic_sdcch_busy_att + result.getFloat("traffic.sdcch_busy_att");
                traffic_sdcch_ho_seiz = traffic_sdcch_ho_seiz + result.getFloat("traffic.sdcch_ho_seiz");
                traffic_sdcch_lapd_fail = traffic_sdcch_lapd_fail + result.getFloat("traffic.sdcch_lapd_fail");
                traffic_sdcch_netw_act = traffic_sdcch_netw_act + result.getFloat("traffic.sdcch_netw_act");
                traffic_sdcch_radio_fail = traffic_sdcch_radio_fail + result.getFloat("traffic.sdcch_radio_fail");
                traffic_sdcch_rf_old_ho = traffic_sdcch_rf_old_ho + result.getFloat("traffic.sdcch_rf_old_ho");
                traffic_sdcch_seiz_att = traffic_sdcch_seiz_att + result.getFloat("traffic.sdcch_seiz_att");
                traffic_sdcch_user_act = traffic_sdcch_user_act + result.getFloat("traffic.sdcch_user_act");
                traffic_tch_abis_fail_old = traffic_tch_abis_fail_old + result.getFloat("traffic.tch_abis_fail_old");
                traffic_tch_bcsu_reset = traffic_tch_bcsu_reset + result.getFloat("traffic.tch_bcsu_reset");
                traffic_tch_bts_fail = traffic_tch_bts_fail + result.getFloat("traffic.tch_bts_fail");
                traffic_tch_call_req = traffic_tch_call_req + result.getFloat("msc_o_sdcch_at");

                traffic_tch_lapd_fail = traffic_tch_lapd_fail + result.getFloat("traffic.tch_lapd_fail");
                traffic_tch_netw_act = traffic_tch_netw_act + result.getFloat("traffic.tch_netw_act");
                traffic_tch_norm_seiz = traffic_tch_norm_seiz + result.getFloat("traffic.tch_norm_seiz");
                traffic_tch_radio_fail = traffic_tch_radio_fail + result.getFloat("traffic.tch_radio_fail");
                traffic_tch_rej_due_req_ch_a_if_crc = traffic_tch_rej_due_req_ch_a_if_crc + result.getFloat("traffic.tch_rej_due_req_ch_a_if_crc");
                traffic_tch_rel_due_rad_fail_ph_2_3 = traffic_tch_rel_due_rad_fail_ph_2_3 + result.getFloat("traffic.tch_rel_due_rad_fail_ph_2_3");
                traffic_tch_seiz_due_sdcch_con = traffic_tch_seiz_due_sdcch_con + result.getFloat("traffic.tch_seiz_due_sdcch_con");
                traffic_tch_succ_seiz_for_dir_acc = traffic_tch_succ_seiz_for_dir_acc + result.getFloat("traffic.tch_succ_seiz_for_dir_acc");
                traffic_tch_tr_fail_old = traffic_tch_tr_fail_old + result.getFloat("traffic.tch_tr_fail_old");


                //NbreErgRecus++;

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
                    ho_bsc_i_sdcch_tchBh = result.getFloat("ho.bsc_i_sdcch_tch");
                    ho_bsc_I_sdcch_tch_atBh = result.getFloat("ho.bsc_I_sdcch_tch_at");
                    ho_bsc_i_unsucc_a_int_circ_typeBh = result.getFloat("sdcch_radio_fail");
                    ho_bsc_o_fail_lackBh = result.getFloat("ho.bsc_o_fail_lack");
                    ho_bsc_o_sdcch_at = result.getFloat("ho.bsc_o_sdcch_at");
                    ho_bsc_o_sdcch_tchBh = result.getFloat("ho.bsc_o_sdcch_tch");
                    ho_bsc_o_sdcch_tch_atBh = result.getFloat("ho.bsc_o_sdcch_tch_at");
                    ho_bsc_o_succ_hoBh = result.getFloat("ho.bsc_o_succ_ho");
                    ho_bsc_o_tch_tch_atBh = result.getFloat("ho.bsc_o_tch_tch_at");
                    ho_cell_fail_lackBh = result.getFloat("ho.cell_fail_lack");
                    ho_cell_sdcch_atBh = result.getFloat("ho.cell_sdcch_at");
                    ho_cell_sdcch_tchBh = result.getFloat("ho.cell_sdcch_tch");
                    ho_cell_sdcch_tch_atBh = result.getFloat("ho.cell_sdcch_tch_at");
                    ho_cell_succ_hoBh = result.getFloat("ho.cell_succ_ho");
                    ho_cell_tch_tch_atBh = result.getFloat("ho.cell_tch_tch_at");
                    ho_ho_unsucc_a_int_circ_typeBh = result.getFloat("ho.ho_unsucc_a_int_circ_type");
                    ho_msc_controlled_in_hoBh = result.getFloat("ho.msc_controlled_in_ho");
                    ho_msc_i_sdcch_tchBh = result.getFloat("ho.msc_i_sdcch_tch");
                    ho_msc_I_sdcch_tch_atBh = result.getFloat("ho.msc_I_sdcch_tch_at");
                    ho_msc_o_fail_lackBh = result.getFloat("ho.msc_o_fail_lack");
                    ho_msc_o_sdcch_atBh = result.getFloat("ho.msc_o_sdcch_at");
                    ho_msc_o_sdcch_tchBh = result.getFloat("ho.msc_o_sdcch_tch");
                    ho_msc_o_sdcch_tch_atBh = result.getFloat("ho.msc_o_sdcch_tch_at");
                    ho_msc_o_succ_ho = result.getFloat("ho.msc_o_succ_ho");
                    ho_msc_o_tch_tch_atBh = result.getFloat("ho.msc_o_tch_tch_at");
                    pcu_packet_ch_reqBh = result.getFloat("pcu.packet_ch_req");
                    res_access_ch_req_msg_recBh = result.getFloat("res_access.ch_req_msg_rec");
                    res_access_ghost_ccch_resBh = result.getFloat("res_access.ghost_ccch_res");
                    res_access_rej_seiz_att_due_distBh = result.getFloat("res_access.rej_seiz_att_due_dist");
                    rxqualBh = result.getFloat("rxqual");
                    service_tch_ho_assignBh = result.getFloat("service.tch_ho_assign");
                    service_tch_new_call_assignBh = result.getFloat("service.tch_new_call_assign");
                    traffic_ms_tch_succ_seiz_assign_cmpltBh = result.getFloat("traffic.ms_tch_succ_seiz_assign_cmplt");

                    traffic_sdcch_a_if_fail_callBh = result.getFloat("traffic.sdcch_a_if_fail_call");
                    traffic_sdcch_a_if_fail_oldBh = result.getFloat("traffic.sdcch_a_if_fail_old");
                    traffic_sdcch_abis_fail_callBh = result.getFloat("traffic.sdcch_abis_fail_call");
                    traffic_sdcch_abis_fail_oldBh = result.getFloat("traffic.sdcch_abis_fail_old");
                    traffic_sdcch_assignBh = result.getFloat("traffic.sdcch_assign");
                    traffic_sdcch_bcsu_resetBh = result.getFloat("traffic.sdcch_bcsu_reset");
                    traffic_sdcch_bts_fail = result.getFloat("traffic.sdcch_bts_fail");
                    traffic_sdcch_busy_attBh = result.getFloat("traffic.sdcch_busy_att");
                    traffic_sdcch_ho_seizBh = result.getFloat("traffic.sdcch_ho_seiz");
                    traffic_sdcch_lapd_failBh = result.getFloat("traffic.sdcch_lapd_fail");
                    traffic_sdcch_netw_actBh = result.getFloat("traffic.sdcch_netw_act");
                    traffic_sdcch_radio_fail = result.getFloat("traffic.sdcch_radio_fail");
                    traffic_sdcch_rf_old_hoBh = result.getFloat("traffic.sdcch_rf_old_ho");
                    traffic_sdcch_seiz_attBh = result.getFloat("traffic.sdcch_seiz_att");
                    traffic_sdcch_user_actBh = result.getFloat("traffic.sdcch_user_act");
                    traffic_tch_abis_fail_oldBh = result.getFloat("traffic.tch_abis_fail_old");
                    traffic_tch_bcsu_resetBh = result.getFloat("traffic.tch_bcsu_reset");
                    traffic_tch_bts_failBh = result.getFloat("traffic.tch_bts_fail");
                    traffic_tch_call_reqBh = result.getFloat("traffic.tch_call_req");

                    traffic_tch_lapd_failBh = result.getFloat("traffic.tch_lapd_fail");
                    traffic_tch_netw_actBh = result.getFloat("traffic.tch_netw_act");
                    traffic_tch_norm_seizBh = result.getFloat("traffic.tch_norm_seiz");
                    traffic_tch_radio_failBh = result.getFloat("traffic.tch_radio_fail");
                    traffic_tch_rej_due_req_ch_a_if_crcBh = result.getFloat("traffic.tch_rej_due_req_ch_a_if_crc");
                    traffic_tch_rel_due_rad_fail_ph_2_3Bh = result.getFloat("traffic.tch_rel_due_rad_fail_ph_2_3");
                    traffic_tch_seiz_due_sdcch_conBh = result.getFloat("traffic.tch_seiz_due_sdcch_con");
                    traffic_tch_succ_seiz_for_dir_accBh = result.getFloat("traffic.tch_succ_seiz_for_dir_acc");
                    traffic_tch_tr_fail_oldBh = result.getFloat("traffic.tch_tr_fail_old");


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

            ////// calcul TCHBR
            if ((traffic_tch_call_req - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type))) > 0)
            {
                tchbr = ((traffic_tch_call_req - traffic_tch_norm_seiz) - (ho_msc_o_sdcch_tch + ho_bsc_o_sdcch_tch + ho_cell_sdcch_tch) + (traffic_tch_succ_seiz_for_dir_acc) - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type))) / (traffic_tch_call_req - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type)));
                tchbr = (double) ((int) (tchbr * 10000)) / 10000;
                if (tchbr >= 0 && tchbr <= 1)
                {
                    MapTauxInfoManqRegion.put("tchbr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("tchbr", 100f);
                }
            } else
            {
                tchcr = 1;
                MapTauxInfoManqRegion.put("tchbr", 100f);
            }

            ////// calcul TCHBRBH
            if ((traffic_tch_call_reqBh - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh))) > 0)
            {
                tchbrbh = ((traffic_tch_call_reqBh - traffic_tch_norm_seizBh) - (ho_msc_o_sdcch_tchBh + ho_bsc_o_sdcch_tchBh + ho_cell_sdcch_tchBh) + (traffic_tch_succ_seiz_for_dir_accBh) - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh))) / (traffic_tch_call_reqBh - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh)));
                tchbrbh = (double) ((int) (tchbrbh * 10000)) / 10000;
                if (tchbrbh >= 0 && tchbrbh <= 1)
                {
                    MapTauxInfoManqRegion.put("tchbrbh", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("tchbrbh", 100F);
                }
            } else
            {
                tchbrbh = 1;
                MapTauxInfoManqRegion.put("tchbrbh", 100F);
            }

            ////// calcul TCHDR
            if ((service_tch_new_call_assign + service_tch_ho_assign) > 0) {
                tchdr = (traffic_tch_radio_fail - traffic_tch_rel_due_rad_fail_ph_2_3 + traffic_tch_abis_fail_old + traffic_tch_tr_fail_old + traffic_tch_lapd_fail + traffic_tch_bts_fail + traffic_tch_bcsu_reset + traffic_tch_netw_act) / (service_tch_new_call_assign + service_tch_ho_assign);
                tchdr = (double) ((int) (tchdr * 10000)) / 10000;
                if (tchdr >= 0 && tchdr <= 1)
                {
                    MapTauxInfoManqRegion.put("tchdr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("tchdr", 100F);
                }
            } else
            {
                tchdr = 1;
                MapTauxInfoManqRegion.put("tchdr", 100F);
            }

            ////// calcul TCHCRBH
            if ((service_tch_new_call_assignBh + service_tch_ho_assignBh) > 0)
            {
                tchdrbh = (traffic_tch_radio_failBh - traffic_tch_rel_due_rad_fail_ph_2_3Bh + traffic_tch_abis_fail_oldBh + traffic_tch_tr_fail_oldBh + traffic_tch_lapd_failBh + traffic_tch_bts_failBh + traffic_tch_bcsu_resetBh + traffic_tch_netw_actBh) / (service_tch_new_call_assignBh + service_tch_ho_assignBh);
                tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
                MapTauxInfoManqRegion.put("tchdrbh", TauxInfoManq);
            } else
            {
                tchdrbh = 1;
                MapTauxInfoManqRegion.put("tchdrbh", 100F);
            }
            if (tchdrbh >= 0 || tchdrbh <= 1)
            {
                MapTauxInfoManqRegion.put("tchdrbh", TauxInfoManq);
            } else
            {
                MapTauxInfoManqRegion.put("tchdrbh", 100F);
            }

            ////////calcul de SDCCHBR
            if ((traffic_sdcch_seiz_att) > 0)
            {
                sdcchbr = (traffic_sdcch_busy_att - traffic_tch_seiz_due_sdcch_con) / traffic_sdcch_seiz_att;
                sdcchbr = (double) ((int) (sdcchbr * 10000)) / 10000;
                if (sdcchbr <= 1 && sdcchbr >= 0) {
                    MapTauxInfoManqRegion.put("sdcchbr", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchbr", 100F);
                }
            } else
            {
                sdcchbr = 1;
                MapTauxInfoManqRegion.put("sdcchbr", 100F);
            }

            ////////calcul de SDCCHBRBH
            if ((traffic_sdcch_seiz_attBh) > 0)
            {
                sdcchbrbh = (traffic_sdcch_busy_attBh - traffic_tch_seiz_due_sdcch_conBh) / traffic_sdcch_seiz_attBh;
                sdcchbrbh = (double) ((int) (sdcchbrbh * 10000)) / 10000;
                if (sdcchbrbh <= 1 && sdcchbrbh >= 0) {
                    MapTauxInfoManqRegion.put("sdcchbrbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchbrbh", 100F);
                }
            } else
            {
                sdcchbrbh = 1;
                MapTauxInfoManqRegion.put("sdcchbrbh", 100F);
            }

            /////////// calcul de SDCCHDR
            if ((traffic_sdcch_assign + traffic_sdcch_ho_seiz - traffic_sdcch_abis_fail_call - traffic_sdcch_abis_fail_old - traffic_sdcch_a_if_fail_call - traffic_sdcch_a_if_fail_old) > 0)
            {
                sdcchdr = (traffic_sdcch_radio_fail + traffic_sdcch_rf_old_ho + traffic_sdcch_user_act + traffic_sdcch_bcsu_reset + traffic_sdcch_netw_act + traffic_sdcch_bts_fail + traffic_sdcch_lapd_fail) / (traffic_sdcch_assign + traffic_sdcch_ho_seiz - traffic_sdcch_abis_fail_call - traffic_sdcch_abis_fail_old - traffic_sdcch_a_if_fail_call - traffic_sdcch_a_if_fail_old);
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if (sdcchdr <= 1 || sdcchdr >= 0)
                {
                    MapTauxInfoManqRegion.put("sdcchdr", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("sdcchdr", 100F);
                }
            } else
            {
                sdcchdr = 1;
                MapTauxInfoManqRegion.put("sdcchdr", 100F);
            }

            /////////// calcul de SDCCHDRBH
            if ((traffic_sdcch_assignBh + traffic_sdcch_ho_seizBh - traffic_sdcch_abis_fail_callBh - traffic_sdcch_abis_fail_oldBh - traffic_sdcch_a_if_fail_callBh - traffic_sdcch_a_if_fail_oldBh) > 0)
            {
                sdcchdrbh = (traffic_sdcch_radio_failBh + traffic_sdcch_rf_old_hoBh + traffic_sdcch_user_actBh + traffic_sdcch_bcsu_resetBh + traffic_sdcch_netw_actBh + traffic_sdcch_bts_failBh + traffic_sdcch_lapd_failBh) / (traffic_sdcch_assignBh + traffic_sdcch_ho_seizBh - traffic_sdcch_abis_fail_callBh - traffic_sdcch_abis_fail_oldBh - traffic_sdcch_a_if_fail_callBh - traffic_sdcch_a_if_fail_oldBh);
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if (sdcchdrbh <= 1 || sdcchdrbh >= 0)
                {
                    MapTauxInfoManqRegion.put("sdcchdrbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
                }
            } else
            {
                sdcchdrbh = 1;
                MapTauxInfoManqRegion.put("sdcchdrbh", 100F);
            }

            /////// calcul de CSSR
            cssr = 1 - (sdcchbr + tchbr + sdcchdr);
            cssr = (double) ((int) (cssr * 10000)) / 10000;
            MapTauxInfoManqRegion.put("cssr", TauxInfoManq);

            /////// calcul de CSSRBH
            cssrbh = 1 - (sdcchbrbh + tchbrbh + sdcchdrbh);
            cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
            MapTauxInfoManqRegion.put("cssr", TauxInfoManq);
            ////////////// Calcul de HOSucces

            if ((ho_msc_o_tch_tch_at + ho_msc_o_sdcch_tch_at + ho_msc_o_sdcch_at + ho_bsc_o_tch_tch_at + ho_bsc_o_sdcch_tch_at + ho_bsc_o_sdcch_at + ho_cell_tch_tch_at + ho_cell_sdcch_tch_at + ho_cell_sdcch_at) > 0) {
                hosucces = ((ho_msc_o_succ_ho + ho_bsc_o_succ_ho + ho_cell_succ_ho) + (ho_msc_o_fail_lack + ho_bsc_o_fail_lack + ho_cell_fail_lack)) / (ho_msc_o_tch_tch_at + ho_msc_o_sdcch_tch_at + ho_msc_o_sdcch_at + ho_bsc_o_tch_tch_at + ho_bsc_o_sdcch_tch_at + ho_bsc_o_sdcch_at + ho_cell_tch_tch_at + ho_cell_sdcch_tch_at + ho_cell_sdcch_at);
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
                if (hosucces >= 0 && hosucces <= 1)
                {
                    MapTauxInfoManqRegion.put("hosucces", TauxInfoManq);
                } else
                {
                    MapTauxInfoManqRegion.put("hosucces", 100F);
                }
            } else
            {
                hosucces = 0;
                MapTauxInfoManqRegion.put("hosucces", 100F);
            }

            ////////////// Calcul de HOSuccesBh

            if ((ho_msc_o_tch_tch_atBh + ho_msc_o_sdcch_tch_atBh + ho_msc_o_sdcch_atBh + ho_bsc_o_tch_tch_atBh + ho_bsc_o_sdcch_tch_atBh + ho_bsc_o_sdcch_atBh + ho_cell_tch_tch_atBh + ho_cell_sdcch_tch_atBh + ho_cell_sdcch_atBh) > 0) {
                hosuccesbh = ((ho_msc_o_succ_hoBh + ho_bsc_o_succ_hoBh + ho_cell_succ_hoBh) + (ho_msc_o_fail_lackBh + ho_bsc_o_fail_lackBh + ho_cell_fail_lackBh)) / (ho_msc_o_tch_tch_atBh + ho_msc_o_sdcch_tch_atBh + ho_msc_o_sdcch_atBh + ho_bsc_o_tch_tch_atBh + ho_bsc_o_sdcch_tch_atBh + ho_bsc_o_sdcch_atBh + ho_cell_tch_tch_atBh + ho_cell_sdcch_tch_atBh + ho_cell_sdcch_atBh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
                if (hosuccesbh >= 0 && hosuccesbh <= 1) {
                    MapTauxInfoManqRegion.put("hosuccesbh", TauxInfoManq);
                } else {
                    MapTauxInfoManqRegion.put("hosuccesbh", 100F);
                }
            } else {
                hosuccesbh = 0;
                MapTauxInfoManqRegion.put("hosuccesbh", 100F);
            }

            ///////////// calcul HoFailureBh
            HoFailureBh = 1 - hosuccesbh;

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
                for (Entry<String, Float> entry : MapTauxInfoManqRegion.entrySet()) {
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void CalculValeurGlobales()
    {
        int bh = -1;
        Object objBh = mapBhRegion.get("Global");
        if (objBh != null)
        {
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

        tchm = 0;tchmbh = 0;tchcr = 0;tchdr = 0;tchbr = 0;cssr = 0;cssrbh = 0;
        cdr = 0;csr = 0;tchcrbh = 0;tchdrbh = 0;tchbrbh = 0;hosucces = 0;hosuccesbh = 0;
        HoFailureBh = 0;sdcchcrbh = 0;sdcchbrbh = 0;sdcchdrbh = 0;sdcchcr = 0;
        sdcchbr = 0;sdcchdr = 0;cdrbh = 0;csrbh = 0;Rsmsr = 0;

        double ho_bsc_i_sdcch_tch = 0, ho_bsc_I_sdcch_tch_at = 0, ho_bsc_i_unsucc_a_int_circ_type = 0, ho_bsc_o_fail_lack = 0, ho_bsc_o_sdcch_at = 0, ho_bsc_o_sdcch_tch = 0, ho_bsc_o_sdcch_tch_at = 0, ho_bsc_o_succ_ho = 0,
                ho_bsc_o_tch_tch_at = 0, ho_cell_fail_lack = 0, ho_cell_sdcch_at = 0, ho_cell_sdcch_tch = 0, ho_cell_sdcch_tch_at = 0, ho_cell_succ_ho = 0, ho_cell_tch_tch_at = 0, ho_ho_unsucc_a_int_circ_type = 0, ho_msc_controlled_in_ho = 0,
                ho_msc_i_sdcch_tch = 0, ho_msc_I_sdcch_tch_at = 0, ho_msc_o_fail_lack = 0, ho_msc_o_sdcch_at = 0, ho_msc_o_sdcch_tch = 0, ho_msc_o_sdcch_tch_at = 0, ho_msc_o_succ_ho = 0, ho_msc_o_tch_tch_at = 0, pcu_packet_ch_req = 0, res_access_ch_req_msg_rec = 0,
                res_access_ghost_ccch_res = 0, res_access_rej_seiz_att_due_dist = 0, rxqual = 0, service_tch_ho_assign = 0, service_tch_new_call_assign = 0, traffic_ms_tch_succ_seiz_assign_cmplt = 0, traffic_sdcch_a_if_fail_call = 0, traffic_sdcch_a_if_fail_old = 0,
                traffic_sdcch_abis_fail_call = 0, traffic_sdcch_abis_fail_old = 0, traffic_sdcch_assign = 0, traffic_sdcch_bcsu_reset = 0, traffic_sdcch_bts_fail = 0, traffic_sdcch_busy_att = 0, traffic_sdcch_ho_seiz = 0, traffic_sdcch_lapd_fail = 0, traffic_sdcch_netw_act = 0,
                traffic_sdcch_radio_fail = 0, traffic_sdcch_rf_old_ho = 0, traffic_sdcch_seiz_att = 0, traffic_sdcch_user_act = 0, traffic_tch_abis_fail_old = 0, traffic_tch_bcsu_reset = 0, traffic_tch_bts_fail = 0, traffic_tch_call_req = 0, traffic_tch_lapd_fail = 0,
                traffic_tch_netw_act = 0, traffic_tch_norm_seiz = 0, traffic_tch_radio_fail = 0, traffic_tch_rej_due_req_ch_a_if_crc = 0, traffic_tch_rel_due_rad_fail_ph_2_3 = 0, traffic_tch_seiz_due_sdcch_con = 0, traffic_tch_succ_seiz_for_dir_acc = 0, traffic_tch_tr_fail_old = 0;

        double ho_bsc_i_sdcch_tchBh = 0, ho_bsc_I_sdcch_tch_atBh = 0, ho_bsc_i_unsucc_a_int_circ_typeBh = 0, ho_bsc_o_fail_lackBh = 0, ho_bsc_o_sdcch_atBh = 0, ho_bsc_o_sdcch_tchBh = 0, ho_bsc_o_sdcch_tch_atBh = 0, ho_bsc_o_succ_hoBh = 0,
                ho_bsc_o_tch_tch_atBh = 0, ho_cell_fail_lackBh = 0, ho_cell_sdcch_atBh = 0, ho_cell_sdcch_tchBh = 0, ho_cell_sdcch_tch_atBh = 0, ho_cell_succ_hoBh = 0, ho_cell_tch_tch_atBh = 0, ho_ho_unsucc_a_int_circ_typeBh = 0, ho_msc_controlled_in_hoBh = 0,
                ho_msc_i_sdcch_tchBh = 0, ho_msc_I_sdcch_tch_atBh = 0, ho_msc_o_fail_lackBh = 0, ho_msc_o_sdcch_atBh = 0, ho_msc_o_sdcch_tchBh = 0, ho_msc_o_sdcch_tch_atBh = 0, ho_msc_o_succ_hoBh = 0, ho_msc_o_tch_tch_atBh = 0, pcu_packet_ch_reqBh = 0, res_access_ch_req_msg_recBh = 0,
                res_access_ghost_ccch_resBh = 0, res_access_rej_seiz_att_due_distBh = 0, rxqualBh = 0, service_tch_ho_assignBh = 0, service_tch_new_call_assignBh = 0, traffic_ms_tch_succ_seiz_assign_cmpltBh = 0, traffic_sdcch_a_if_fail_callBh = 0, traffic_sdcch_a_if_fail_oldBh = 0,
                traffic_sdcch_abis_fail_callBh = 0, traffic_sdcch_abis_fail_oldBh = 0, traffic_sdcch_assignBh = 0, traffic_sdcch_bcsu_resetBh = 0, traffic_sdcch_bts_failBh = 0, traffic_sdcch_busy_attBh = 0, traffic_sdcch_ho_seizBh = 0, traffic_sdcch_lapd_failBh = 0, traffic_sdcch_netw_actBh = 0,
                traffic_sdcch_radio_failBh = 0, traffic_sdcch_rf_old_hoBh = 0, traffic_sdcch_seiz_attBh = 0, traffic_sdcch_user_actBh = 0, traffic_tch_abis_fail_oldBh = 0, traffic_tch_bcsu_resetBh = 0, traffic_tch_bts_failBh = 0, traffic_tch_call_reqBh = 0, traffic_tch_lapd_failBh = 0,
                traffic_tch_netw_actBh = 0, traffic_tch_norm_seizBh = 0, traffic_tch_radio_failBh = 0, traffic_tch_rej_due_req_ch_a_if_crcBh = 0, traffic_tch_rel_due_rad_fail_ph_2_3Bh = 0, traffic_tch_seiz_due_sdcch_conBh = 0, traffic_tch_succ_seiz_for_dir_accBh = 0, traffic_tch_tr_fail_oldBh = 0;

        String requete = "select region,heure,sum(ho.bsc_i_sdcch_tch) as ho.bsc_i_sdcch_tch,sum(ho.bsc_I_sdcch_tch_at) as ho.bsc_I_sdcch_tch_at,sum(ho.bsc_i_unsucc_a_int_circ_type) as ho.bsc_i_unsucc_a_int_circ_type,sum(ho.bsc_o_fail_lack) as ho.bsc_o_fail_lack,sum(ho.bsc_o_sdcch_at) as ho.bsc_o_sdcch_at,sum(ho.bsc_o_sdcch_tch) as ho.bsc_o_sdcch_tch,sum(ho.bsc_o_sdcch_tch_at) as ho.bsc_o_sdcch_tch_at,sum(ho.bsc_o_succ_ho) as ho.bsc_o_succ_ho,"
                + " sum(ho.bsc_o_tch_tch_at) as ho.bsc_o_tch_tch_at,sum(ho.cell_fail_lack) as ho.cell_fail_lack, sum(ho.cell_sdcch_at) as ho.cell_sdcch_at,sum(ho.cell_sdcch_tch) as ho.cell_sdcch_tch,sum(ho.cell_sdcch_tch_at) as ho.cell_sdcch_tch_at,sum(ho.cell_succ_ho) as ho.cell_succ_ho,sum(ho.cell_tch_tch_at) as ho.cell_tch_tch_at,sum(ho.ho_unsucc_a_int_circ_type) as ho.ho_unsucc_a_int_circ_type,"
                + " sum(ho.msc_controlled_in_ho) as ho.msc_controlled_in_ho, sum(ho.msc_i_sdcch_tch) as ho.msc_i_sdcch_tch,sum(ho.msc_I_sdcch_tch_at) as ho.msc_I_sdcch_tch_at,sum(ho.msc_o_fail_lack) as ho.msc_o_fail_lack,sum(ho.msc_o_sdcch_at) as ho.msc_o_sdcch_at,sum(ho.msc_o_sdcch_tch) as ho.msc_o_sdcch_tch,sum(ho.msc_o_sdcch_tch_at) as ho.msc_o_sdcch_tch_at,sum(ho.msc_o_succ_ho) as ho.msc_o_succ_ho,"
                + " sum(ho.msc_o_tch_tch_at) as ho.msc_o_tch_tch_at, sum(pcu.packet_ch_req) as pcu.packet_ch_req, sum(res_access.ch_req_msg_rec) as res_access.ch_req_msg_rec,sum(res_access.ghost_ccch_res) as res_access.ghost_ccch_res,sum(res_access.rej_seiz_att_due_dist) as res_access.rej_seiz_att_due_dist,sum(rxqual) as rxqual,sum(service.tch_ho_assign) as service.tch_ho_assign,sum(service.tch_new_call_assign) as service.tch_new_call_assign, "
                + " sum(traffic.ms_tch_succ_seiz_assign_cmplt) as traffic.ms_tch_succ_seiz_assign_cmplt,sum(traffic.sdcch_a_if_fail_call) as traffic.sdcch_a_if_fail_call,sum(traffic.sdcch_a_if_fail_old) as traffic.sdcch_a_if_fail_old, sum(traffic.sdcch_abis_fail_call) as traffic.sdcch_abis_fail_call, sum(traffic.sdcch_abis_fail_old) as traffic.sdcch_abis_fail_old, sum(traffic.sdcch_assign) as traffic.sdcch_assign, sum(traffic.sdcch_bcsu_reset) as traffic.sdcch_bcsu_reset,sum(traffic.sdcch_bts_fail) as traffic.sdcch_bts_fail,"
                + " sum(traffic.sdcch_busy_att) as traffic.sdcch_busy_att,sum(traffic.sdcch_ho_seiz) as traffic.sdcch_ho_seiz,sum(traffic.sdcch_lapd_fail) as traffic.sdcch_lapd_fail, sum(traffic.sdcch_netw_act) as traffic.sdcch_netw_act, sum(traffic.sdcch_radio_fail) as traffic.sdcch_radio_fail, sum(traffic.sdcch_rf_old_ho) as traffic.sdcch_rf_old_ho, sum(traffic.sdcch_seiz_att) as traffic.sdcch_seiz_att, sum(traffic.sdcch_user_act) as traffic.sdcch_user_act"
                + " sum(traffic.tch_abis_fail_old) as traffic.tch_abis_fail_old,sum(traffic.tch_bcsu_reset) as traffic.tch_bcsu_reset,sum(traffic.tch_bts_fail) as traffic.tch_bts_fail, sum(traffic.tch_call_req) as traffic.tch_call_req, sum(traffic.tch_lapd_fail) as traffic.tch_lapd_fail,sum(traffic.tch_netw_act) as traffic.tch_netw_act, sum(traffic.tch_norm_seiz) as traffic.tch_norm_seiz, sum(traffic.tch_radio_fail) as traffic.tch_radio_fail, sum(traffic.tch_radio_fail) as traffic.tch_radio_fail,"
                + " sum(traffic.tch_rel_due_rad_fail_ph_2_3) as traffic.tch_rel_due_rad_fail_ph_2_3,sum(traffic.tch_seiz_due_sdcch_con) as traffic.tch_seiz_due_sdcch_con, sum(traffic.tch_succ_seiz_for_dir_acc) as traffic.tch_succ_seiz_for_dir_acc,sum(traffic.tch_tr_fail_old) as traffic.tch_tr_fail_old,"
                + " from tableregistre where date>='" + dateDebut + "' and date<='" + dateFin + "' group by heure";
        System.out.println("---------------------------------------------------requete Globale:" + requete);
        ResultSet result = cn.getResultset(requete);
        try
        {
            while (result.next())
            {

                ho_bsc_i_sdcch_tch = ho_bsc_i_sdcch_tch + result.getFloat("ho.bsc_i_sdcch_tch");
                ho_bsc_I_sdcch_tch_at = ho_bsc_I_sdcch_tch_at + result.getFloat("ho.bsc_I_sdcch_tch_at");
                ho_bsc_i_unsucc_a_int_circ_type = ho_bsc_i_unsucc_a_int_circ_type + result.getFloat("ho.bsc_i_unsucc_a_int_circ_type");
                ho_bsc_o_fail_lack = ho_bsc_o_fail_lack + result.getFloat("ho.bsc_o_fail_lack");
                ho_bsc_o_sdcch_at = ho_bsc_o_sdcch_at + result.getFloat("ho.bsc_o_sdcch_at");
                ho_bsc_o_sdcch_tch = ho_bsc_o_sdcch_tch + result.getFloat("ho.bsc_o_sdcch_tch");
                ho_bsc_o_sdcch_tch_at = ho_bsc_o_sdcch_tch_at + result.getFloat("ho.bsc_o_sdcch_tch_at");
                ho_bsc_o_succ_ho = ho_bsc_o_succ_ho + result.getFloat("ho.bsc_o_succ_ho");
                ho_bsc_o_tch_tch_at = ho_bsc_o_tch_tch_at + result.getFloat("ho.bsc_o_tch_tch_at");
                ho_cell_fail_lack = ho_cell_fail_lack + result.getFloat("ho.cell_fail_lack");
                ho_cell_sdcch_at = ho_cell_sdcch_at + result.getFloat("ho.cell_sdcch_at");
                ho_cell_sdcch_tch = ho_cell_sdcch_tch + result.getFloat("ho.cell_sdcch_tch");
                ho_cell_sdcch_tch_at = ho_cell_sdcch_tch_at + result.getFloat("ho.cell_sdcch_tch_at");
                ho_cell_succ_ho = ho_cell_succ_ho + result.getFloat("ho.cell_succ_ho");
                ho_cell_tch_tch_at = ho_cell_tch_tch_at + result.getFloat("ho.cell_tch_tch_at");
                ho_ho_unsucc_a_int_circ_type = ho_ho_unsucc_a_int_circ_type + result.getFloat("ho.ho_unsucc_a_int_circ_type");
                ho_msc_controlled_in_ho = ho_msc_controlled_in_ho + result.getFloat("ho.msc_controlled_in_ho");
                ho_msc_i_sdcch_tch = ho_msc_i_sdcch_tch + result.getFloat("ho.msc_i_sdcch_tch");
                ho_msc_I_sdcch_tch_at = ho_msc_I_sdcch_tch_at + result.getFloat("ho.msc_I_sdcch_tch_at");
                ho_msc_o_fail_lack = ho_msc_o_fail_lack + result.getFloat("ho.msc_o_fail_lack");
                ho_msc_o_sdcch_at = ho_msc_o_sdcch_at + result.getFloat("ho.msc_o_sdcch_at");
                ho_msc_o_sdcch_tch = ho_msc_o_sdcch_tch + result.getFloat("ho.msc_o_sdcch_tch");
                ho_msc_o_sdcch_tch_at = ho_msc_o_sdcch_tch_at + result.getFloat("ho.msc_o_sdcch_tch_at");
                ho_msc_o_succ_ho = ho_msc_o_succ_ho + result.getFloat("ho.msc_o_succ_ho");
                ho_msc_o_tch_tch_at = ho_msc_o_tch_tch_at + result.getFloat("ho.msc_o_tch_tch_at");
                pcu_packet_ch_req = pcu_packet_ch_req + result.getFloat("pcu.packet_ch_req");
                res_access_ch_req_msg_rec = res_access_ch_req_msg_rec + result.getFloat("res_access.ch_req_msg_rec");
                res_access_ghost_ccch_res = res_access_ghost_ccch_res + result.getFloat("res_access.ghost_ccch_res");
                res_access_rej_seiz_att_due_dist = res_access_rej_seiz_att_due_dist + result.getFloat("res_access.rej_seiz_att_due_dist");
                rxqual = rxqual + result.getFloat("rxqual");
                service_tch_ho_assign = service_tch_ho_assign + result.getFloat("service.tch_ho_assign");
                service_tch_new_call_assign = service_tch_new_call_assign + result.getFloat("service.tch_new_call_assign");
                traffic_ms_tch_succ_seiz_assign_cmplt = traffic_ms_tch_succ_seiz_assign_cmplt + result.getFloat("traffic.ms_tch_succ_seiz_assign_cmplt");

                traffic_sdcch_a_if_fail_call = traffic_sdcch_a_if_fail_call + result.getFloat("traffic.sdcch_a_if_fail_call");
                traffic_sdcch_a_if_fail_old = traffic_sdcch_a_if_fail_old + result.getFloat("traffic.sdcch_a_if_fail_old");
                traffic_sdcch_abis_fail_call = traffic_sdcch_abis_fail_call + result.getFloat("traffic.sdcch_abis_fail_call");
                traffic_sdcch_abis_fail_old = traffic_sdcch_abis_fail_old + result.getFloat("traffic.sdcch_abis_fail_old");
                traffic_sdcch_assign = traffic_sdcch_assign + result.getFloat("traffic.sdcch_assign");
                traffic_sdcch_bcsu_reset = traffic_sdcch_bcsu_reset + result.getFloat("traffic.sdcch_bcsu_reset");
                traffic_sdcch_bts_fail = traffic_sdcch_bts_fail + result.getFloat("traffic.sdcch_bts_fail");
                traffic_sdcch_busy_att = traffic_sdcch_busy_att + result.getFloat("traffic.sdcch_busy_att");
                traffic_sdcch_ho_seiz = traffic_sdcch_ho_seiz + result.getFloat("traffic.sdcch_ho_seiz");
                traffic_sdcch_lapd_fail = traffic_sdcch_lapd_fail + result.getFloat("traffic.sdcch_lapd_fail");
                traffic_sdcch_netw_act = traffic_sdcch_netw_act + result.getFloat("traffic.sdcch_netw_act");
                traffic_sdcch_radio_fail = traffic_sdcch_radio_fail + result.getFloat("traffic.sdcch_radio_fail");
                traffic_sdcch_rf_old_ho = traffic_sdcch_rf_old_ho + result.getFloat("traffic.sdcch_rf_old_ho");
                traffic_sdcch_seiz_att = traffic_sdcch_seiz_att + result.getFloat("traffic.sdcch_seiz_att");
                traffic_sdcch_user_act = traffic_sdcch_user_act + result.getFloat("traffic.sdcch_user_act");
                traffic_tch_abis_fail_old = traffic_tch_abis_fail_old + result.getFloat("traffic.tch_abis_fail_old");
                traffic_tch_bcsu_reset = traffic_tch_bcsu_reset + result.getFloat("traffic.tch_bcsu_reset");
                traffic_tch_bts_fail = traffic_tch_bts_fail + result.getFloat("traffic.tch_bts_fail");
                traffic_tch_call_req = traffic_tch_call_req + result.getFloat("msc_o_sdcch_at");

                traffic_tch_lapd_fail = traffic_tch_lapd_fail + result.getFloat("traffic.tch_lapd_fail");
                traffic_tch_netw_act = traffic_tch_netw_act + result.getFloat("traffic.tch_netw_act");
                traffic_tch_norm_seiz = traffic_tch_norm_seiz + result.getFloat("traffic.tch_norm_seiz");
                traffic_tch_radio_fail = traffic_tch_radio_fail + result.getFloat("traffic.tch_radio_fail");
                traffic_tch_rej_due_req_ch_a_if_crc = traffic_tch_rej_due_req_ch_a_if_crc + result.getFloat("traffic.tch_rej_due_req_ch_a_if_crc");
                traffic_tch_rel_due_rad_fail_ph_2_3 = traffic_tch_rel_due_rad_fail_ph_2_3 + result.getFloat("traffic.tch_rel_due_rad_fail_ph_2_3");
                traffic_tch_seiz_due_sdcch_con = traffic_tch_seiz_due_sdcch_con + result.getFloat("traffic.tch_seiz_due_sdcch_con");
                traffic_tch_succ_seiz_for_dir_acc = traffic_tch_succ_seiz_for_dir_acc + result.getFloat("traffic.tch_succ_seiz_for_dir_acc");
                traffic_tch_tr_fail_old = traffic_tch_tr_fail_old + result.getFloat("traffic.tch_tr_fail_old");


                int heure = -1;
                String str = result.getString("heure").split(":")[0];
                try
                {
                    heure = Integer.parseInt(str);
                } catch (Exception ex)
                {
                    heure = -1;
                }
                if (heure >= 0 && heure == BhG)
                {
                    ho_bsc_i_sdcch_tchBh = result.getFloat("ho.bsc_i_sdcch_tch");
                    ho_bsc_I_sdcch_tch_atBh = result.getFloat("ho.bsc_I_sdcch_tch_at");
                    ho_bsc_i_unsucc_a_int_circ_typeBh = result.getFloat("sdcch_radio_fail");
                    ho_bsc_o_fail_lackBh = result.getFloat("ho.bsc_o_fail_lack");
                    ho_bsc_o_sdcch_at = result.getFloat("ho.bsc_o_sdcch_at");
                    ho_bsc_o_sdcch_tchBh = result.getFloat("ho.bsc_o_sdcch_tch");
                    ho_bsc_o_sdcch_tch_atBh = result.getFloat("ho.bsc_o_sdcch_tch_at");
                    ho_bsc_o_succ_hoBh = result.getFloat("ho.bsc_o_succ_ho");
                    ho_bsc_o_tch_tch_atBh = result.getFloat("ho.bsc_o_tch_tch_at");
                    ho_cell_fail_lackBh = result.getFloat("ho.cell_fail_lack");
                    ho_cell_sdcch_atBh = result.getFloat("ho.cell_sdcch_at");
                    ho_cell_sdcch_tchBh = result.getFloat("ho.cell_sdcch_tch");
                    ho_cell_sdcch_tch_atBh = result.getFloat("ho.cell_sdcch_tch_at");
                    ho_cell_succ_hoBh = result.getFloat("ho.cell_succ_ho");
                    ho_cell_tch_tch_atBh = result.getFloat("ho.cell_tch_tch_at");
                    ho_ho_unsucc_a_int_circ_typeBh = result.getFloat("ho.ho_unsucc_a_int_circ_type");
                    ho_msc_controlled_in_hoBh = result.getFloat("ho.msc_controlled_in_ho");
                    ho_msc_i_sdcch_tchBh = result.getFloat("ho.msc_i_sdcch_tch");
                    ho_msc_I_sdcch_tch_atBh = result.getFloat("ho.msc_I_sdcch_tch_at");
                    ho_msc_o_fail_lackBh = result.getFloat("ho.msc_o_fail_lack");
                    ho_msc_o_sdcch_atBh = result.getFloat("ho.msc_o_sdcch_at");
                    ho_msc_o_sdcch_tchBh = result.getFloat("ho.msc_o_sdcch_tch");
                    ho_msc_o_sdcch_tch_atBh = result.getFloat("ho.msc_o_sdcch_tch_at");
                    ho_msc_o_succ_ho = result.getFloat("ho.msc_o_succ_ho");
                    ho_msc_o_tch_tch_atBh = result.getFloat("ho.msc_o_tch_tch_at");
                    pcu_packet_ch_reqBh = result.getFloat("pcu.packet_ch_req");
                    res_access_ch_req_msg_recBh = result.getFloat("res_access.ch_req_msg_rec");
                    res_access_ghost_ccch_resBh = result.getFloat("res_access.ghost_ccch_res");
                    res_access_rej_seiz_att_due_distBh = result.getFloat("res_access.rej_seiz_att_due_dist");
                    rxqualBh = result.getFloat("rxqual");
                    service_tch_ho_assignBh = result.getFloat("service.tch_ho_assign");
                    service_tch_new_call_assignBh = result.getFloat("service.tch_new_call_assign");
                    traffic_ms_tch_succ_seiz_assign_cmpltBh = result.getFloat("traffic.ms_tch_succ_seiz_assign_cmplt");

                    traffic_sdcch_a_if_fail_callBh = result.getFloat("traffic.sdcch_a_if_fail_call");
                    traffic_sdcch_a_if_fail_oldBh = result.getFloat("traffic.sdcch_a_if_fail_old");
                    traffic_sdcch_abis_fail_callBh = result.getFloat("traffic.sdcch_abis_fail_call");
                    traffic_sdcch_abis_fail_oldBh = result.getFloat("traffic.sdcch_abis_fail_old");
                    traffic_sdcch_assignBh = result.getFloat("traffic.sdcch_assign");
                    traffic_sdcch_bcsu_resetBh = result.getFloat("traffic.sdcch_bcsu_reset");
                    traffic_sdcch_bts_fail = result.getFloat("traffic.sdcch_bts_fail");
                    traffic_sdcch_busy_attBh = result.getFloat("traffic.sdcch_busy_att");
                    traffic_sdcch_ho_seizBh = result.getFloat("traffic.sdcch_ho_seiz");
                    traffic_sdcch_lapd_failBh = result.getFloat("traffic.sdcch_lapd_fail");
                    traffic_sdcch_netw_actBh = result.getFloat("traffic.sdcch_netw_act");
                    traffic_sdcch_radio_fail = result.getFloat("traffic.sdcch_radio_fail");
                    traffic_sdcch_rf_old_hoBh = result.getFloat("traffic.sdcch_rf_old_ho");
                    traffic_sdcch_seiz_attBh = result.getFloat("traffic.sdcch_seiz_att");
                    traffic_sdcch_user_actBh = result.getFloat("traffic.sdcch_user_act");
                    traffic_tch_abis_fail_oldBh = result.getFloat("traffic.tch_abis_fail_old");
                    traffic_tch_bcsu_resetBh = result.getFloat("traffic.tch_bcsu_reset");
                    traffic_tch_bts_failBh = result.getFloat("traffic.tch_bts_fail");
                    traffic_tch_call_reqBh = result.getFloat("traffic.tch_call_req");

                    traffic_tch_lapd_failBh = result.getFloat("traffic.tch_lapd_fail");
                    traffic_tch_netw_actBh = result.getFloat("traffic.tch_netw_act");
                    traffic_tch_norm_seizBh = result.getFloat("traffic.tch_norm_seiz");
                    traffic_tch_radio_failBh = result.getFloat("traffic.tch_radio_fail");
                    traffic_tch_rej_due_req_ch_a_if_crcBh = result.getFloat("traffic.tch_rej_due_req_ch_a_if_crc");
                    traffic_tch_rel_due_rad_fail_ph_2_3Bh = result.getFloat("traffic.tch_rel_due_rad_fail_ph_2_3");
                    traffic_tch_seiz_due_sdcch_conBh = result.getFloat("traffic.tch_seiz_due_sdcch_con");
                    traffic_tch_succ_seiz_for_dir_accBh = result.getFloat("traffic.tch_succ_seiz_for_dir_acc");
                    traffic_tch_tr_fail_oldBh = result.getFloat("traffic.tch_tr_fail_old");

                }
            }
        } catch (Exception ex)
        {
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        }

        ////// calcul TCHBR
        if ((traffic_tch_call_req - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type))) > 0) {
            tchbr = ((traffic_tch_call_req - traffic_tch_norm_seiz) - (ho_msc_o_sdcch_tch + ho_bsc_o_sdcch_tch + ho_cell_sdcch_tch) + (traffic_tch_succ_seiz_for_dir_acc) - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type))) / (traffic_tch_call_req - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type)));
            tchbr = (double) ((int) (tchbr * 10000)) / 10000;
            if(tchbr>1 || tchbr<0)
            {
                tchcr = 1;
            }
        } else
        {
            tchcr = 1;
        }

        ////// calcul TCHBRBH
        if ((traffic_tch_call_reqBh - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh))) > 0) {
            tchbrbh = ((traffic_tch_call_reqBh - traffic_tch_norm_seizBh) - (ho_msc_o_sdcch_tchBh + ho_bsc_o_sdcch_tchBh + ho_cell_sdcch_tchBh) + (traffic_tch_succ_seiz_for_dir_accBh) - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh))) / (traffic_tch_call_reqBh - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh)));
            tchbrbh = (double) ((int) (tchbrbh * 10000)) / 10000;
        } else
        {
            tchbrbh = 1;
        }

        ////// calcul TCHDR
        if ((service_tch_new_call_assign + service_tch_ho_assign) > 0)
        {
            tchdr = (traffic_tch_radio_fail - traffic_tch_rel_due_rad_fail_ph_2_3 + traffic_tch_abis_fail_old + traffic_tch_tr_fail_old + traffic_tch_lapd_fail + traffic_tch_bts_fail + traffic_tch_bcsu_reset + traffic_tch_netw_act) / (service_tch_new_call_assign + service_tch_ho_assign);
            tchdr = (double) ((int) (tchdr * 10000)) / 10000;
        } else {
            tchdr = 1;
        }

        ////// calcul TCHDRBH
        if ((service_tch_new_call_assignBh + service_tch_ho_assignBh) > 0)
        {
            tchdrbh = (traffic_tch_radio_failBh - traffic_tch_rel_due_rad_fail_ph_2_3Bh + traffic_tch_abis_fail_oldBh + traffic_tch_tr_fail_oldBh + traffic_tch_lapd_failBh + traffic_tch_bts_failBh + traffic_tch_bcsu_resetBh + traffic_tch_netw_actBh) / (service_tch_new_call_assignBh + service_tch_ho_assignBh);
            tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
        } else
        {
            tchdrbh = 1;
        }

        ////////calcul de SDCCHBR
        if ((traffic_sdcch_seiz_att) > 0)
        {
            sdcchbr = (traffic_sdcch_busy_att - traffic_tch_seiz_due_sdcch_con) / traffic_sdcch_seiz_att;
            sdcchbr = (double) ((int) (sdcchbr * 10000)) / 10000;
        } else
        {
            sdcchbr = 1;
        }

        ////////calcul de SDCCHBRBH
        if ((traffic_sdcch_seiz_attBh) > 0)
        {
            sdcchbrbh = (traffic_sdcch_busy_attBh - traffic_tch_seiz_due_sdcch_conBh) / traffic_sdcch_seiz_attBh;
            sdcchbrbh = (double) ((int) (sdcchbrbh * 10000)) / 10000;
        } else
        {
            sdcchbrbh = 1;
        }

        /////////// calcul de SDCCHDR
        if ((traffic_sdcch_assign + traffic_sdcch_ho_seiz - traffic_sdcch_abis_fail_call - traffic_sdcch_abis_fail_old - traffic_sdcch_a_if_fail_call - traffic_sdcch_a_if_fail_old) > 0) {
            sdcchdr = (traffic_sdcch_radio_fail + traffic_sdcch_rf_old_ho + traffic_sdcch_user_act + traffic_sdcch_bcsu_reset + traffic_sdcch_netw_act + traffic_sdcch_bts_fail + traffic_sdcch_lapd_fail) / (traffic_sdcch_assign + traffic_sdcch_ho_seiz - traffic_sdcch_abis_fail_call - traffic_sdcch_abis_fail_old - traffic_sdcch_a_if_fail_call - traffic_sdcch_a_if_fail_old);
            sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
        } else {
            sdcchdr = 1;
        }

        /////////// calcul de SDCCHDRBH
        if ((traffic_sdcch_assignBh + traffic_sdcch_ho_seizBh - traffic_sdcch_abis_fail_callBh - traffic_sdcch_abis_fail_oldBh - traffic_sdcch_a_if_fail_callBh - traffic_sdcch_a_if_fail_oldBh) > 0)
        {
            sdcchdrbh = (traffic_sdcch_radio_failBh + traffic_sdcch_rf_old_hoBh + traffic_sdcch_user_actBh + traffic_sdcch_bcsu_resetBh + traffic_sdcch_netw_actBh + traffic_sdcch_bts_failBh + traffic_sdcch_lapd_failBh) / (traffic_sdcch_assignBh + traffic_sdcch_ho_seizBh - traffic_sdcch_abis_fail_callBh - traffic_sdcch_abis_fail_oldBh - traffic_sdcch_a_if_fail_callBh - traffic_sdcch_a_if_fail_oldBh);
            sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
        } else {
            sdcchdrbh = 1;
        }

        /////// calcul de CSSR
        cssr = 1 - (sdcchbr + tchbr + sdcchdr);
        cssr = (double) ((int) (cssr * 10000)) / 10000;

        /////// calcul de CSSRBH
        cssrbh = 1 - (sdcchbrbh + tchbrbh + sdcchdrbh);
        cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;

        ////////////// Calcul de HOSucces
        if ((ho_msc_o_tch_tch_at + ho_msc_o_sdcch_tch_at + ho_msc_o_sdcch_at + ho_bsc_o_tch_tch_at + ho_bsc_o_sdcch_tch_at + ho_bsc_o_sdcch_at + ho_cell_tch_tch_at + ho_cell_sdcch_tch_at + ho_cell_sdcch_at) > 0)
        {
            hosucces = ((ho_msc_o_succ_ho + ho_bsc_o_succ_ho + ho_cell_succ_ho) + (ho_msc_o_fail_lack + ho_bsc_o_fail_lack + ho_cell_fail_lack)) / (ho_msc_o_tch_tch_at + ho_msc_o_sdcch_tch_at + ho_msc_o_sdcch_at + ho_bsc_o_tch_tch_at + ho_bsc_o_sdcch_tch_at + ho_bsc_o_sdcch_at + ho_cell_tch_tch_at + ho_cell_sdcch_tch_at + ho_cell_sdcch_at);
            hosucces = (double) ((int) (hosucces * 10000)) / 10000;
        } else {
            hosucces = 0;
        }

        ////////////// Calcul de HOSuccesBh
        if ((ho_msc_o_tch_tch_atBh + ho_msc_o_sdcch_tch_atBh + ho_msc_o_sdcch_atBh + ho_bsc_o_tch_tch_atBh + ho_bsc_o_sdcch_tch_atBh + ho_bsc_o_sdcch_atBh + ho_cell_tch_tch_atBh + ho_cell_sdcch_tch_atBh + ho_cell_sdcch_atBh) > 0) {
            hosuccesbh = ((ho_msc_o_succ_hoBh + ho_bsc_o_succ_hoBh + ho_cell_succ_hoBh) + (ho_msc_o_fail_lackBh + ho_bsc_o_fail_lackBh + ho_cell_fail_lackBh)) / (ho_msc_o_tch_tch_atBh + ho_msc_o_sdcch_tch_atBh + ho_msc_o_sdcch_atBh + ho_bsc_o_tch_tch_atBh + ho_bsc_o_sdcch_tch_atBh + ho_bsc_o_sdcch_atBh + ho_cell_tch_tch_atBh + ho_cell_sdcch_tch_atBh + ho_cell_sdcch_atBh);
            hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;

        } else
        {
            hosuccesbh = 0;
        }

        ///////////// calcul HoFailureBh
        HoFailureBh = 1 - hosuccesbh;
        HoFailureBh = (double) ((int) (HoFailureBh * 10000)) / 10000;

        Object obj = mapTraficBhRegion.get("Global");
        Object obj1 = mapTrHoraireMoyenRegion.get("Global");
        if (obj != null && obj1 != null)
        {
            double val = Double.parseDouble(obj.toString());
            if (val > 0)
            {
                bhtr = tchmbh / tchm;
            } else {
                bhtr = 1;
            }
            bhtr = (double) ((int) (bhtr * 10000)) / 10000;
        }
    }

    private void CalculCellule(String cellule)
    {
        try
        {
            tchm = 0; tchmbh = 0;tchcr = 0;tchcrbh = 0;
            tchdr = 0;tchdrbh = 0;tchbr = 0;
            tchbrbh = 0;cssr = 0;sdcchcrbh = 0;
            sdcchdrbh = 0;sdcchbrbh = 0;cdr = 0;
            cdrbh = 0;csr = 0;csrbh = 0;Rsmsr = 0;
            cssrbh = 0;sdcchcr = 0;sdcchdr = 0;
            sdcchbr = 0;hosucces = 0;hosuccesbh = 0;
            HoFailureBh = 0;

            double ho_bsc_i_sdcch_tch = 0, ho_bsc_I_sdcch_tch_at = 0, ho_bsc_i_unsucc_a_int_circ_type = 0, ho_bsc_o_fail_lack = 0, ho_bsc_o_sdcch_at = 0, ho_bsc_o_sdcch_tch = 0, ho_bsc_o_sdcch_tch_at = 0, ho_bsc_o_succ_ho = 0,
                    ho_bsc_o_tch_tch_at = 0, ho_cell_fail_lack = 0, ho_cell_sdcch_at = 0, ho_cell_sdcch_tch = 0, ho_cell_sdcch_tch_at = 0, ho_cell_succ_ho = 0, ho_cell_tch_tch_at = 0, ho_ho_unsucc_a_int_circ_type = 0, ho_msc_controlled_in_ho = 0,
                    ho_msc_i_sdcch_tch = 0, ho_msc_I_sdcch_tch_at = 0, ho_msc_o_fail_lack = 0, ho_msc_o_sdcch_at = 0, ho_msc_o_sdcch_tch = 0, ho_msc_o_sdcch_tch_at = 0, ho_msc_o_succ_ho = 0, ho_msc_o_tch_tch_at = 0, pcu_packet_ch_req = 0, res_access_ch_req_msg_rec = 0,
                    res_access_ghost_ccch_res = 0, res_access_rej_seiz_att_due_dist = 0, rxqual = 0, service_tch_ho_assign = 0, service_tch_new_call_assign = 0, traffic_ms_tch_succ_seiz_assign_cmplt = 0, traffic_sdcch_a_if_fail_call = 0, traffic_sdcch_a_if_fail_old = 0,
                    traffic_sdcch_abis_fail_call = 0, traffic_sdcch_abis_fail_old = 0, traffic_sdcch_assign = 0, traffic_sdcch_bcsu_reset = 0, traffic_sdcch_bts_fail = 0, traffic_sdcch_busy_att = 0, traffic_sdcch_ho_seiz = 0, traffic_sdcch_lapd_fail = 0, traffic_sdcch_netw_act = 0,
                    traffic_sdcch_radio_fail = 0, traffic_sdcch_rf_old_ho = 0, traffic_sdcch_seiz_att = 0, traffic_sdcch_user_act = 0, traffic_tch_abis_fail_old = 0, traffic_tch_bcsu_reset = 0, traffic_tch_bts_fail = 0, traffic_tch_call_req = 0, traffic_tch_lapd_fail = 0,
                    traffic_tch_netw_act = 0, traffic_tch_norm_seiz = 0, traffic_tch_radio_fail = 0, traffic_tch_rej_due_req_ch_a_if_crc = 0, traffic_tch_rel_due_rad_fail_ph_2_3 = 0, traffic_tch_seiz_due_sdcch_con = 0, traffic_tch_succ_seiz_for_dir_acc = 0, traffic_tch_tr_fail_old = 0;

            double ho_bsc_i_sdcch_tchBh = 0, ho_bsc_I_sdcch_tch_atBh = 0, ho_bsc_i_unsucc_a_int_circ_typeBh = 0, ho_bsc_o_fail_lackBh = 0, ho_bsc_o_sdcch_atBh = 0, ho_bsc_o_sdcch_tchBh = 0, ho_bsc_o_sdcch_tch_atBh = 0, ho_bsc_o_succ_hoBh = 0,
                    ho_bsc_o_tch_tch_atBh = 0, ho_cell_fail_lackBh = 0, ho_cell_sdcch_atBh = 0, ho_cell_sdcch_tchBh = 0, ho_cell_sdcch_tch_atBh = 0, ho_cell_succ_hoBh = 0, ho_cell_tch_tch_atBh = 0, ho_ho_unsucc_a_int_circ_typeBh = 0, ho_msc_controlled_in_hoBh = 0,
                    ho_msc_i_sdcch_tchBh = 0, ho_msc_I_sdcch_tch_atBh = 0, ho_msc_o_fail_lackBh = 0, ho_msc_o_sdcch_atBh = 0, ho_msc_o_sdcch_tchBh = 0, ho_msc_o_sdcch_tch_atBh = 0, ho_msc_o_succ_hoBh = 0, ho_msc_o_tch_tch_atBh = 0, pcu_packet_ch_reqBh = 0, res_access_ch_req_msg_recBh = 0,
                    res_access_ghost_ccch_resBh = 0, res_access_rej_seiz_att_due_distBh = 0, rxqualBh = 0, service_tch_ho_assignBh = 0, service_tch_new_call_assignBh = 0, traffic_ms_tch_succ_seiz_assign_cmpltBh = 0, traffic_sdcch_a_if_fail_callBh = 0, traffic_sdcch_a_if_fail_oldBh = 0,
                    traffic_sdcch_abis_fail_callBh = 0, traffic_sdcch_abis_fail_oldBh = 0, traffic_sdcch_assignBh = 0, traffic_sdcch_bcsu_resetBh = 0, traffic_sdcch_bts_failBh = 0, traffic_sdcch_busy_attBh = 0, traffic_sdcch_ho_seizBh = 0, traffic_sdcch_lapd_failBh = 0, traffic_sdcch_netw_actBh = 0,
                    traffic_sdcch_radio_failBh = 0, traffic_sdcch_rf_old_hoBh = 0, traffic_sdcch_seiz_attBh = 0, traffic_sdcch_user_actBh = 0, traffic_tch_abis_fail_oldBh = 0, traffic_tch_bcsu_resetBh = 0, traffic_tch_bts_failBh = 0, traffic_tch_call_reqBh = 0, traffic_tch_lapd_failBh = 0,
                    traffic_tch_netw_actBh = 0, traffic_tch_norm_seizBh = 0, traffic_tch_radio_failBh = 0, traffic_tch_rej_due_req_ch_a_if_crcBh = 0, traffic_tch_rel_due_rad_fail_ph_2_3Bh = 0, traffic_tch_seiz_due_sdcch_conBh = 0, traffic_tch_succ_seiz_for_dir_accBh = 0, traffic_tch_tr_fail_oldBh = 0;

            String requete = "select region,heure,sum(ho.bsc_i_sdcch_tch) as ho.bsc_i_sdcch_tch,sum(ho.bsc_I_sdcch_tch_at) as ho.bsc_I_sdcch_tch_at,sum(ho.bsc_i_unsucc_a_int_circ_type) as ho.bsc_i_unsucc_a_int_circ_type,sum(ho.bsc_o_fail_lack) as ho.bsc_o_fail_lack,sum(ho.bsc_o_sdcch_at) as ho.bsc_o_sdcch_at,sum(ho.bsc_o_sdcch_tch) as ho.bsc_o_sdcch_tch,sum(ho.bsc_o_sdcch_tch_at) as ho.bsc_o_sdcch_tch_at,sum(ho.bsc_o_succ_ho) as ho.bsc_o_succ_ho,"
                    + " sum(ho.bsc_o_tch_tch_at) as ho.bsc_o_tch_tch_at,sum(ho.cell_fail_lack) as ho.cell_fail_lack, sum(ho.cell_sdcch_at) as ho.cell_sdcch_at,sum(ho.cell_sdcch_tch) as ho.cell_sdcch_tch,sum(ho.cell_sdcch_tch_at) as ho.cell_sdcch_tch_at,sum(ho.cell_succ_ho) as ho.cell_succ_ho,sum(ho.cell_tch_tch_at) as ho.cell_tch_tch_at,sum(ho.ho_unsucc_a_int_circ_type) as ho.ho_unsucc_a_int_circ_type,"
                    + " sum(ho.msc_controlled_in_ho) as ho.msc_controlled_in_ho, sum(ho.msc_i_sdcch_tch) as ho.msc_i_sdcch_tch,sum(ho.msc_I_sdcch_tch_at) as ho.msc_I_sdcch_tch_at,sum(ho.msc_o_fail_lack) as ho.msc_o_fail_lack,sum(ho.msc_o_sdcch_at) as ho.msc_o_sdcch_at,sum(ho.msc_o_sdcch_tch) as ho.msc_o_sdcch_tch,sum(ho.msc_o_sdcch_tch_at) as ho.msc_o_sdcch_tch_at,sum(ho.msc_o_succ_ho) as ho.msc_o_succ_ho,"
                    + " sum(ho.msc_o_tch_tch_at) as ho.msc_o_tch_tch_at, sum(pcu.packet_ch_req) as pcu.packet_ch_req, sum(res_access.ch_req_msg_rec) as res_access.ch_req_msg_rec,sum(res_access.ghost_ccch_res) as res_access.ghost_ccch_res,sum(res_access.rej_seiz_att_due_dist) as res_access.rej_seiz_att_due_dist,sum(rxqual) as rxqual,sum(service.tch_ho_assign) as service.tch_ho_assign,sum(service.tch_new_call_assign) as service.tch_new_call_assign, "
                    + " sum(traffic.ms_tch_succ_seiz_assign_cmplt) as traffic.ms_tch_succ_seiz_assign_cmplt,sum(traffic.sdcch_a_if_fail_call) as traffic.sdcch_a_if_fail_call,sum(traffic.sdcch_a_if_fail_old) as traffic.sdcch_a_if_fail_old, sum(traffic.sdcch_abis_fail_call) as traffic.sdcch_abis_fail_call, sum(traffic.sdcch_abis_fail_old) as traffic.sdcch_abis_fail_old, sum(traffic.sdcch_assign) as traffic.sdcch_assign, sum(traffic.sdcch_bcsu_reset) as traffic.sdcch_bcsu_reset,sum(traffic.sdcch_bts_fail) as traffic.sdcch_bts_fail,"
                    + " sum(traffic.sdcch_busy_att) as traffic.sdcch_busy_att,sum(traffic.sdcch_ho_seiz) as traffic.sdcch_ho_seiz,sum(traffic.sdcch_lapd_fail) as traffic.sdcch_lapd_fail, sum(traffic.sdcch_netw_act) as traffic.sdcch_netw_act, sum(traffic.sdcch_radio_fail) as traffic.sdcch_radio_fail, sum(traffic.sdcch_rf_old_ho) as traffic.sdcch_rf_old_ho, sum(traffic.sdcch_seiz_att) as traffic.sdcch_seiz_att, sum(traffic.sdcch_user_act) as traffic.sdcch_user_act"
                    + " sum(traffic.tch_abis_fail_old) as traffic.tch_abis_fail_old,sum(traffic.tch_bcsu_reset) as traffic.tch_bcsu_reset,sum(traffic.tch_bts_fail) as traffic.tch_bts_fail, sum(traffic.tch_call_req) as traffic.tch_call_req, sum(traffic.tch_lapd_fail) as traffic.tch_lapd_fail,sum(traffic.tch_netw_act) as traffic.tch_netw_act, sum(traffic.tch_norm_seiz) as traffic.tch_norm_seiz, sum(traffic.tch_radio_fail) as traffic.tch_radio_fail, sum(traffic.tch_radio_fail) as traffic.tch_radio_fail,"
                    + " sum(traffic.tch_rel_due_rad_fail_ph_2_3) as traffic.tch_rel_due_rad_fail_ph_2_3,sum(traffic.tch_seiz_due_sdcch_con) as traffic.tch_seiz_due_sdcch_con, sum(traffic.tch_succ_seiz_for_dir_acc) as traffic.tch_succ_seiz_for_dir_acc,sum(traffic.tch_tr_fail_old) as traffic.tch_tr_fail_old,"
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
                ho_bsc_i_sdcch_tch = ho_bsc_i_sdcch_tch + result.getFloat("ho.bsc_i_sdcch_tch");
                ho_bsc_I_sdcch_tch_at = ho_bsc_I_sdcch_tch_at + result.getFloat("ho.bsc_I_sdcch_tch_at");
                ho_bsc_i_unsucc_a_int_circ_type = ho_bsc_i_unsucc_a_int_circ_type + result.getFloat("ho.bsc_i_unsucc_a_int_circ_type");
                ho_bsc_o_fail_lack = ho_bsc_o_fail_lack + result.getFloat("ho.bsc_o_fail_lack");
                ho_bsc_o_sdcch_at = ho_bsc_o_sdcch_at + result.getFloat("ho.bsc_o_sdcch_at");
                ho_bsc_o_sdcch_tch = ho_bsc_o_sdcch_tch + result.getFloat("ho.bsc_o_sdcch_tch");
                ho_bsc_o_sdcch_tch_at = ho_bsc_o_sdcch_tch_at + result.getFloat("ho.bsc_o_sdcch_tch_at");
                ho_bsc_o_succ_ho = ho_bsc_o_succ_ho + result.getFloat("ho.bsc_o_succ_ho");
                ho_bsc_o_tch_tch_at = ho_bsc_o_tch_tch_at + result.getFloat("ho.bsc_o_tch_tch_at");
                ho_cell_fail_lack = ho_cell_fail_lack + result.getFloat("ho.cell_fail_lack");
                ho_cell_sdcch_at = ho_cell_sdcch_at + result.getFloat("ho.cell_sdcch_at");
                ho_cell_sdcch_tch = ho_cell_sdcch_tch + result.getFloat("ho.cell_sdcch_tch");
                ho_cell_sdcch_tch_at = ho_cell_sdcch_tch_at + result.getFloat("ho.cell_sdcch_tch_at");
                ho_cell_succ_ho = ho_cell_succ_ho + result.getFloat("ho.cell_succ_ho");
                ho_cell_tch_tch_at = ho_cell_tch_tch_at + result.getFloat("ho.cell_tch_tch_at");
                ho_ho_unsucc_a_int_circ_type = ho_ho_unsucc_a_int_circ_type + result.getFloat("ho.ho_unsucc_a_int_circ_type");
                ho_msc_controlled_in_ho = ho_msc_controlled_in_ho + result.getFloat("ho.msc_controlled_in_ho");
                ho_msc_i_sdcch_tch = ho_msc_i_sdcch_tch + result.getFloat("ho.msc_i_sdcch_tch");
                ho_msc_I_sdcch_tch_at = ho_msc_I_sdcch_tch_at + result.getFloat("ho.msc_I_sdcch_tch_at");
                ho_msc_o_fail_lack = ho_msc_o_fail_lack + result.getFloat("ho.msc_o_fail_lack");
                ho_msc_o_sdcch_at = ho_msc_o_sdcch_at + result.getFloat("ho.msc_o_sdcch_at");
                ho_msc_o_sdcch_tch = ho_msc_o_sdcch_tch + result.getFloat("ho.msc_o_sdcch_tch");
                ho_msc_o_sdcch_tch_at = ho_msc_o_sdcch_tch_at + result.getFloat("ho.msc_o_sdcch_tch_at");
                ho_msc_o_succ_ho = ho_msc_o_succ_ho + result.getFloat("ho.msc_o_succ_ho");
                ho_msc_o_tch_tch_at = ho_msc_o_tch_tch_at + result.getFloat("ho.msc_o_tch_tch_at");
                pcu_packet_ch_req = pcu_packet_ch_req + result.getFloat("pcu.packet_ch_req");
                res_access_ch_req_msg_rec = res_access_ch_req_msg_rec + result.getFloat("res_access.ch_req_msg_rec");
                res_access_ghost_ccch_res = res_access_ghost_ccch_res + result.getFloat("res_access.ghost_ccch_res");
                res_access_rej_seiz_att_due_dist = res_access_rej_seiz_att_due_dist + result.getFloat("res_access.rej_seiz_att_due_dist");
                rxqual = rxqual + result.getFloat("rxqual");
                service_tch_ho_assign = service_tch_ho_assign + result.getFloat("service.tch_ho_assign");
                service_tch_new_call_assign = service_tch_new_call_assign + result.getFloat("service.tch_new_call_assign");
                traffic_ms_tch_succ_seiz_assign_cmplt = traffic_ms_tch_succ_seiz_assign_cmplt + result.getFloat("traffic.ms_tch_succ_seiz_assign_cmplt");

                traffic_sdcch_a_if_fail_call = traffic_sdcch_a_if_fail_call + result.getFloat("traffic.sdcch_a_if_fail_call");
                traffic_sdcch_a_if_fail_old = traffic_sdcch_a_if_fail_old + result.getFloat("traffic.sdcch_a_if_fail_old");
                traffic_sdcch_abis_fail_call = traffic_sdcch_abis_fail_call + result.getFloat("traffic.sdcch_abis_fail_call");
                traffic_sdcch_abis_fail_old = traffic_sdcch_abis_fail_old + result.getFloat("traffic.sdcch_abis_fail_old");
                traffic_sdcch_assign = traffic_sdcch_assign + result.getFloat("traffic.sdcch_assign");
                traffic_sdcch_bcsu_reset = traffic_sdcch_bcsu_reset + result.getFloat("traffic.sdcch_bcsu_reset");
                traffic_sdcch_bts_fail = traffic_sdcch_bts_fail + result.getFloat("traffic.sdcch_bts_fail");
                traffic_sdcch_busy_att = traffic_sdcch_busy_att + result.getFloat("traffic.sdcch_busy_att");
                traffic_sdcch_ho_seiz = traffic_sdcch_ho_seiz + result.getFloat("traffic.sdcch_ho_seiz");
                traffic_sdcch_lapd_fail = traffic_sdcch_lapd_fail + result.getFloat("traffic.sdcch_lapd_fail");
                traffic_sdcch_netw_act = traffic_sdcch_netw_act + result.getFloat("traffic.sdcch_netw_act");
                traffic_sdcch_radio_fail = traffic_sdcch_radio_fail + result.getFloat("traffic.sdcch_radio_fail");
                traffic_sdcch_rf_old_ho = traffic_sdcch_rf_old_ho + result.getFloat("traffic.sdcch_rf_old_ho");
                traffic_sdcch_seiz_att = traffic_sdcch_seiz_att + result.getFloat("traffic.sdcch_seiz_att");
                traffic_sdcch_user_act = traffic_sdcch_user_act + result.getFloat("traffic.sdcch_user_act");
                traffic_tch_abis_fail_old = traffic_tch_abis_fail_old + result.getFloat("traffic.tch_abis_fail_old");
                traffic_tch_bcsu_reset = traffic_tch_bcsu_reset + result.getFloat("traffic.tch_bcsu_reset");
                traffic_tch_bts_fail = traffic_tch_bts_fail + result.getFloat("traffic.tch_bts_fail");
                traffic_tch_call_req = traffic_tch_call_req + result.getFloat("msc_o_sdcch_at");

                traffic_tch_lapd_fail = traffic_tch_lapd_fail + result.getFloat("traffic.tch_lapd_fail");
                traffic_tch_netw_act = traffic_tch_netw_act + result.getFloat("traffic.tch_netw_act");
                traffic_tch_norm_seiz = traffic_tch_norm_seiz + result.getFloat("traffic.tch_norm_seiz");
                traffic_tch_radio_fail = traffic_tch_radio_fail + result.getFloat("traffic.tch_radio_fail");
                traffic_tch_rej_due_req_ch_a_if_crc = traffic_tch_rej_due_req_ch_a_if_crc + result.getFloat("traffic.tch_rej_due_req_ch_a_if_crc");
                traffic_tch_rel_due_rad_fail_ph_2_3 = traffic_tch_rel_due_rad_fail_ph_2_3 + result.getFloat("traffic.tch_rel_due_rad_fail_ph_2_3");
                traffic_tch_seiz_due_sdcch_con = traffic_tch_seiz_due_sdcch_con + result.getFloat("traffic.tch_seiz_due_sdcch_con");
                traffic_tch_succ_seiz_for_dir_acc = traffic_tch_succ_seiz_for_dir_acc + result.getFloat("traffic.tch_succ_seiz_for_dir_acc");
                traffic_tch_tr_fail_old = traffic_tch_tr_fail_old + result.getFloat("traffic.tch_tr_fail_old");



                String str = result.getString("heure").trim();
                int heure = -1;
                try {
                    heure = Integer.parseInt(str.split(":")[0]);
                } catch (Exception ex) {
                    heure = -1;
                }
                if (heure == BhCellule && heure >= 0) {
                    ho_bsc_i_sdcch_tchBh = result.getFloat("ho.bsc_i_sdcch_tch");
                    ho_bsc_I_sdcch_tch_atBh = result.getFloat("ho.bsc_I_sdcch_tch_at");
                    ho_bsc_i_unsucc_a_int_circ_typeBh = result.getFloat("sdcch_radio_fail");
                    ho_bsc_o_fail_lackBh = result.getFloat("ho.bsc_o_fail_lack");
                    ho_bsc_o_sdcch_at = result.getFloat("ho.bsc_o_sdcch_at");
                    ho_bsc_o_sdcch_tchBh = result.getFloat("ho.bsc_o_sdcch_tch");
                    ho_bsc_o_sdcch_tch_atBh = result.getFloat("ho.bsc_o_sdcch_tch_at");
                    ho_bsc_o_succ_hoBh = result.getFloat("ho.bsc_o_succ_ho");
                    ho_bsc_o_tch_tch_atBh = result.getFloat("ho.bsc_o_tch_tch_at");
                    ho_cell_fail_lackBh = result.getFloat("ho.cell_fail_lack");
                    ho_cell_sdcch_atBh = result.getFloat("ho.cell_sdcch_at");
                    ho_cell_sdcch_tchBh = result.getFloat("ho.cell_sdcch_tch");
                    ho_cell_sdcch_tch_atBh = result.getFloat("ho.cell_sdcch_tch_at");
                    ho_cell_succ_hoBh = result.getFloat("ho.cell_succ_ho");
                    ho_cell_tch_tch_atBh = result.getFloat("ho.cell_tch_tch_at");
                    ho_ho_unsucc_a_int_circ_typeBh = result.getFloat("ho.ho_unsucc_a_int_circ_type");
                    ho_msc_controlled_in_hoBh = result.getFloat("ho.msc_controlled_in_ho");
                    ho_msc_i_sdcch_tchBh = result.getFloat("ho.msc_i_sdcch_tch");
                    ho_msc_I_sdcch_tch_atBh = result.getFloat("ho.msc_I_sdcch_tch_at");
                    ho_msc_o_fail_lackBh = result.getFloat("ho.msc_o_fail_lack");
                    ho_msc_o_sdcch_atBh = result.getFloat("ho.msc_o_sdcch_at");
                    ho_msc_o_sdcch_tchBh = result.getFloat("ho.msc_o_sdcch_tch");
                    ho_msc_o_sdcch_tch_atBh = result.getFloat("ho.msc_o_sdcch_tch_at");
                    ho_msc_o_succ_ho = result.getFloat("ho.msc_o_succ_ho");
                    ho_msc_o_tch_tch_atBh = result.getFloat("ho.msc_o_tch_tch_at");
                    pcu_packet_ch_reqBh = result.getFloat("pcu.packet_ch_req");
                    res_access_ch_req_msg_recBh = result.getFloat("res_access.ch_req_msg_rec");
                    res_access_ghost_ccch_resBh = result.getFloat("res_access.ghost_ccch_res");
                    res_access_rej_seiz_att_due_distBh = result.getFloat("res_access.rej_seiz_att_due_dist");
                    rxqualBh = result.getFloat("rxqual");
                    service_tch_ho_assignBh = result.getFloat("service.tch_ho_assign");
                    service_tch_new_call_assignBh = result.getFloat("service.tch_new_call_assign");
                    traffic_ms_tch_succ_seiz_assign_cmpltBh = result.getFloat("traffic.ms_tch_succ_seiz_assign_cmplt");

                    traffic_sdcch_a_if_fail_callBh = result.getFloat("traffic.sdcch_a_if_fail_call");
                    traffic_sdcch_a_if_fail_oldBh = result.getFloat("traffic.sdcch_a_if_fail_old");
                    traffic_sdcch_abis_fail_callBh = result.getFloat("traffic.sdcch_abis_fail_call");
                    traffic_sdcch_abis_fail_oldBh = result.getFloat("traffic.sdcch_abis_fail_old");
                    traffic_sdcch_assignBh = result.getFloat("traffic.sdcch_assign");
                    traffic_sdcch_bcsu_resetBh = result.getFloat("traffic.sdcch_bcsu_reset");
                    traffic_sdcch_bts_fail = result.getFloat("traffic.sdcch_bts_fail");
                    traffic_sdcch_busy_attBh = result.getFloat("traffic.sdcch_busy_att");
                    traffic_sdcch_ho_seizBh = result.getFloat("traffic.sdcch_ho_seiz");
                    traffic_sdcch_lapd_failBh = result.getFloat("traffic.sdcch_lapd_fail");
                    traffic_sdcch_netw_actBh = result.getFloat("traffic.sdcch_netw_act");
                    traffic_sdcch_radio_fail = result.getFloat("traffic.sdcch_radio_fail");
                    traffic_sdcch_rf_old_hoBh = result.getFloat("traffic.sdcch_rf_old_ho");
                    traffic_sdcch_seiz_attBh = result.getFloat("traffic.sdcch_seiz_att");
                    traffic_sdcch_user_actBh = result.getFloat("traffic.sdcch_user_act");
                    traffic_tch_abis_fail_oldBh = result.getFloat("traffic.tch_abis_fail_old");
                    traffic_tch_bcsu_resetBh = result.getFloat("traffic.tch_bcsu_reset");
                    traffic_tch_bts_failBh = result.getFloat("traffic.tch_bts_fail");
                    traffic_tch_call_reqBh = result.getFloat("traffic.tch_call_req");

                    traffic_tch_lapd_failBh = result.getFloat("traffic.tch_lapd_fail");
                    traffic_tch_netw_actBh = result.getFloat("traffic.tch_netw_act");
                    traffic_tch_norm_seizBh = result.getFloat("traffic.tch_norm_seiz");
                    traffic_tch_radio_failBh = result.getFloat("traffic.tch_radio_fail");
                    traffic_tch_rej_due_req_ch_a_if_crcBh = result.getFloat("traffic.tch_rej_due_req_ch_a_if_crc");
                    traffic_tch_rel_due_rad_fail_ph_2_3Bh = result.getFloat("traffic.tch_rel_due_rad_fail_ph_2_3");
                    traffic_tch_seiz_due_sdcch_conBh = result.getFloat("traffic.tch_seiz_due_sdcch_con");
                    traffic_tch_succ_seiz_for_dir_accBh = result.getFloat("traffic.tch_succ_seiz_for_dir_acc");
                    traffic_tch_tr_fail_oldBh = result.getFloat("traffic.tch_tr_fail_old");
                }
            }

            ////// calcul TCHBR
            if ((traffic_tch_call_req - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type))) > 0) {
                tchbr = ((traffic_tch_call_req - traffic_tch_norm_seiz) - (ho_msc_o_sdcch_tch + ho_bsc_o_sdcch_tch + ho_cell_sdcch_tch) + (traffic_tch_succ_seiz_for_dir_acc) - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type))) / (traffic_tch_call_req - (traffic_tch_rej_due_req_ch_a_if_crc - (ho_bsc_i_unsucc_a_int_circ_type + ho_msc_controlled_in_ho + ho_ho_unsucc_a_int_circ_type)));
                tchbr = (double) ((int) (tchbr * 10000)) / 10000;
            } else {
                tchcr = 1;
            }

            ////// calcul TCHBRBH
            if ((traffic_tch_call_reqBh - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh))) > 0) {
                tchbrbh = ((traffic_tch_call_reqBh - traffic_tch_norm_seizBh) - (ho_msc_o_sdcch_tchBh + ho_bsc_o_sdcch_tchBh + ho_cell_sdcch_tchBh) + (traffic_tch_succ_seiz_for_dir_accBh) - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh))) / (traffic_tch_call_reqBh - (traffic_tch_rej_due_req_ch_a_if_crcBh - (ho_bsc_i_unsucc_a_int_circ_typeBh + ho_msc_controlled_in_hoBh + ho_ho_unsucc_a_int_circ_typeBh)));
                tchbrbh = (double) ((int) (tchbrbh * 10000)) / 10000;
            } else {
                tchbrbh = 1;
            }

            ////// calcul TCHDR
            if ((service_tch_new_call_assign + service_tch_ho_assign) > 0) {
                tchdr = (traffic_tch_radio_fail - traffic_tch_rel_due_rad_fail_ph_2_3 + traffic_tch_abis_fail_old + traffic_tch_tr_fail_old + traffic_tch_lapd_fail + traffic_tch_bts_fail + traffic_tch_bcsu_reset + traffic_tch_netw_act) / (service_tch_new_call_assign + service_tch_ho_assign);
                tchdr = (double) ((int) (tchdr * 10000)) / 10000;
            } else {
                tchdr = 1;
            }

            ////// calcul TCHCRBH
            if ((service_tch_new_call_assignBh + service_tch_ho_assignBh) > 0) {
                tchdrbh = (traffic_tch_radio_failBh - traffic_tch_rel_due_rad_fail_ph_2_3Bh + traffic_tch_abis_fail_oldBh + traffic_tch_tr_fail_oldBh + traffic_tch_lapd_failBh + traffic_tch_bts_failBh + traffic_tch_bcsu_resetBh + traffic_tch_netw_actBh) / (service_tch_new_call_assignBh + service_tch_ho_assignBh);
                tchdrbh = (double) ((int) (tchdrbh * 10000)) / 10000;
                if(tchdrbh<0 || tchdrbh>1)
                {
                    tchdrbh=1;
                }
            } else {
                tchdrbh = 1;
            }


            ////////calcul de SDCCHBR
            if ((traffic_sdcch_seiz_att) > 0) {
                sdcchbr = (traffic_sdcch_busy_att - traffic_tch_seiz_due_sdcch_con) / traffic_sdcch_seiz_att;
                sdcchbr = (double) ((int) (sdcchbr * 10000)) / 10000;
                if(sdcchbr<0 || sdcchbr>1)
                {
                    sdcchbr=1;
                }
            } else {
                sdcchbr = 1;
            }

            ////////calcul de SDCCHBRBH
            if ((traffic_sdcch_seiz_attBh) > 0)
            {
                sdcchbrbh = (traffic_sdcch_busy_attBh - traffic_tch_seiz_due_sdcch_conBh) / traffic_sdcch_seiz_attBh;
                sdcchbrbh = (double) ((int) (sdcchbrbh * 10000)) / 10000;
                if(sdcchbrbh<0  || sdcchbrbh>1)
                {
                    sdcchbrbh=1;
                }
            } else {
                sdcchbrbh = 1;
            }

            /////////// calcul de SDCCHDR
            if ((traffic_sdcch_assign + traffic_sdcch_ho_seiz - traffic_sdcch_abis_fail_call - traffic_sdcch_abis_fail_old - traffic_sdcch_a_if_fail_call - traffic_sdcch_a_if_fail_old) > 0) {
                sdcchdr = (traffic_sdcch_radio_fail + traffic_sdcch_rf_old_ho + traffic_sdcch_user_act + traffic_sdcch_bcsu_reset + traffic_sdcch_netw_act + traffic_sdcch_bts_fail + traffic_sdcch_lapd_fail) / (traffic_sdcch_assign + traffic_sdcch_ho_seiz - traffic_sdcch_abis_fail_call - traffic_sdcch_abis_fail_old - traffic_sdcch_a_if_fail_call - traffic_sdcch_a_if_fail_old);
                sdcchdr = (double) ((int) (sdcchdr * 10000)) / 10000;
                if(sdcchdr<0 || sdcchdr>1)
                {
                    sdcchdr=1;
                }
            } else {
                sdcchdr = 1;
            }


            /////////// calcul de SDCCHDRBH
            if ((traffic_sdcch_assignBh + traffic_sdcch_ho_seizBh - traffic_sdcch_abis_fail_callBh - traffic_sdcch_abis_fail_oldBh - traffic_sdcch_a_if_fail_callBh - traffic_sdcch_a_if_fail_oldBh) > 0) {
                sdcchdrbh = (traffic_sdcch_radio_failBh + traffic_sdcch_rf_old_hoBh + traffic_sdcch_user_actBh + traffic_sdcch_bcsu_resetBh + traffic_sdcch_netw_actBh + traffic_sdcch_bts_failBh + traffic_sdcch_lapd_failBh) / (traffic_sdcch_assignBh + traffic_sdcch_ho_seizBh - traffic_sdcch_abis_fail_callBh - traffic_sdcch_abis_fail_oldBh - traffic_sdcch_a_if_fail_callBh - traffic_sdcch_a_if_fail_oldBh);
                sdcchdrbh = (double) ((int) (sdcchdrbh * 10000)) / 10000;
                if(sdcchdrbh<0 || sdcchdrbh>1)
                {
                    sdcchdrbh=1;
                }
            } else {
                sdcchdrbh = 1;
            }

            /////// calcul de CSSR
            cssr = 1 - (sdcchbr + tchbr + sdcchdr);
            cssr = (double) ((int) (cssr * 10000)) / 10000;
            if(cssr<0 || cssr>1)
            {
                cssr=0;
            }
            /////// calcul de CSSR
            cssrbh = 1 - (sdcchbrbh + tchbrbh + sdcchdrbh);
            cssrbh = (double) ((int) (cssrbh * 10000)) / 10000;
            if(cssrbh<0 || cssrbh>1)
            {
                cssrbh=0;
            }

            ////////////// Calcul de HOSucces
            if ((ho_msc_o_tch_tch_at + ho_msc_o_sdcch_tch_at + ho_msc_o_sdcch_at + ho_bsc_o_tch_tch_at + ho_bsc_o_sdcch_tch_at + ho_bsc_o_sdcch_at + ho_cell_tch_tch_at + ho_cell_sdcch_tch_at + ho_cell_sdcch_at) > 0) {
                hosucces = ((ho_msc_o_succ_ho + ho_bsc_o_succ_ho + ho_cell_succ_ho) + (ho_msc_o_fail_lack + ho_bsc_o_fail_lack + ho_cell_fail_lack)) / (ho_msc_o_tch_tch_at + ho_msc_o_sdcch_tch_at + ho_msc_o_sdcch_at + ho_bsc_o_tch_tch_at + ho_bsc_o_sdcch_tch_at + ho_bsc_o_sdcch_at + ho_cell_tch_tch_at + ho_cell_sdcch_tch_at + ho_cell_sdcch_at);
                hosucces = (double) ((int) (hosucces * 10000)) / 10000;
                if(hosucces>1)
                {
                    hosucces=0;
                }
            } else {
                hosucces = 0;
            }

            ////////////// Calcul de HOSuccesBh
            if ((ho_msc_o_tch_tch_atBh + ho_msc_o_sdcch_tch_atBh + ho_msc_o_sdcch_atBh + ho_bsc_o_tch_tch_atBh + ho_bsc_o_sdcch_tch_atBh + ho_bsc_o_sdcch_atBh + ho_cell_tch_tch_atBh + ho_cell_sdcch_tch_atBh + ho_cell_sdcch_atBh) > 0) {
                hosuccesbh = ((ho_msc_o_succ_hoBh + ho_bsc_o_succ_hoBh + ho_cell_succ_hoBh) + (ho_msc_o_fail_lackBh + ho_bsc_o_fail_lackBh + ho_cell_fail_lackBh)) / (ho_msc_o_tch_tch_atBh + ho_msc_o_sdcch_tch_atBh + ho_msc_o_sdcch_atBh + ho_bsc_o_tch_tch_atBh + ho_bsc_o_sdcch_tch_atBh + ho_bsc_o_sdcch_atBh + ho_cell_tch_tch_atBh + ho_cell_sdcch_tch_atBh + ho_cell_sdcch_atBh);
                hosuccesbh = (double) ((int) (hosuccesbh * 10000)) / 10000;
                if(hosuccesbh>1)
                {
                    hosuccesbh=0;
                }
            } else {
                hosuccesbh = 0;
            }

            ///////////// calcul HoFailureBh
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getBhParRegion(String region)
    {
        int[] TabBhRegion = new int[24];
        double[] TabTchmRegion = new double[24];
        for (int i = 0; i < 24; i++)
        {
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
                try {
                    nbreJrsHrs = resultTotal.getInt("nbre");
                } catch (Exception ex)
                {
                    Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                resultTotal.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CalculBHParCellule(List<String> lstCellules)
    {
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculNSN2G.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                result.close();
            } catch (SQLException ex) {
            }
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        cn.closeConnection();
    }

}
