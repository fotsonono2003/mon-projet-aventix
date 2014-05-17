package projet_consultation.demo;

import projet_consultation.GenererRapport.*;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.Calcul.Calcul2G.CalCulEricsson2G;
import projet_consultation.Calcul.Calcul2G.CalculAlcatel2G;
import projet_consultation.Calcul.Calcul2G.CalculHuawei2G;
import projet_consultation.Calcul.Calcul2G.CalculNSN2G;
import projet_consultation.Calcul.Calcul2G.CalculZTE2G;
import projet_consultation.Calcul.Calcul3G.CalCulEricsson3G;
import projet_consultation.Calcul.Calcul3G.CalculAlcatel3G;
import projet_consultation.Calcul.Calcul3G.CalculHuawei3G;
import projet_consultation.Calcul.Calcul3G.CalculNSN3G;
import projet_consultation.Calcul.Calcul3G.CalculZTE3G;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.Principale.Interface_Principale;
import projet_consultation.Principale.JFrame_connexion;
import projet_consultation.creation_dossiers.mes_documents;
import projet_consultation.genererCartes.Accessibilite;
import projet_consultation.genererCartes.Maintenabilite;
import projet_consultation.genererCartes.Mobilite;
import projet_consultation.genererCartes.SMS;
import projet_consultation.genererCartes.Thumbnail;
import projet_consultation.genererCartes.Trafic;
 
public class Rapport extends SwingWorker<Boolean, Void>
{
    private boolean b=false;
    private String datedebut;
    private String datefin;
    private Operateur operateur;
    private mes_documents mes=new mes_documents();
    

    public Rapport(Operateur operateur,String datedebut, String datefin,boolean b)
    {
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.operateur=operateur;
        this.b=b;
    }
    
    public boolean rapport ()
    {
        File file = new File(mes.get_CheminImage() + operateur.getCodeOperateur());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        boolean bCalcul=false;
        String cheminfichierlog=mes.get_CheminLog();
        String cheminfichierparam=mes.get_CheminParametre();
        String cheminrapport=mes.get_CheminRapport();
        String chemingraphiques=mes.get_CheminImage()+operateur.getCodeOperateur()+File.separator;

        Finprocessus supprimsuperflu = new Finprocessus();
        supprimsuperflu.vidertable(cheminfichierlog, operateur);//permet de vider les tables de valeurs des KPi de l'operateur.

        try
        {
            System.out.println("/*/*/*/*/*/*debut  du calcul /*/*/*/*/*/*/*/*/*-------------");

            String equipement = operateur.getEquipement();
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                if (equipement.toUpperCase().startsWith("ALC")) {
                    CalculAlcatel2G calculAlcatel = new CalculAlcatel2G(operateur, datedebut, datefin);
                    //calculAlcatel.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ERI")) {
                    CalCulEricsson2G calCulEricsson = new CalCulEricsson2G(operateur, datedebut, datefin);
                    calCulEricsson.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ZTE")) {
                    CalculZTE2G calculZTE = new CalculZTE2G(operateur, datedebut, datefin);
                    //calculZTE.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("HW")) {
                    CalculHuawei2G calcul = new CalculHuawei2G(operateur, datedebut, datefin);
                    //calcul.calculTotal();
                } else if (equipement.toUpperCase().startsWith("NSN")) {
                    CalculNSN2G calCulNSN = new CalculNSN2G(operateur, datedebut, datefin);
                    //calCulNSN.CalculTotal();
                }
            } else if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                if (equipement.toUpperCase().startsWith("ALC")) {
                    CalculAlcatel3G calculAlcatel = new CalculAlcatel3G(operateur, datedebut, datefin);
                    //calculAlcatel.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ERI")) {
                    CalCulEricsson3G calCulEricsson = new CalCulEricsson3G(operateur, datedebut, datefin);
                    calCulEricsson.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ZTE")) {
                    CalculZTE3G calculZTE = new CalculZTE3G(operateur, datedebut, datefin);
                    //calculZTE.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("HW")) {
                    CalculHuawei3G calcul = new CalculHuawei3G(operateur, datedebut, datefin);
                    //calcul.calculTotal();
                } else if (equipement.toUpperCase().startsWith("NSN")) {
                    CalculNSN3G calCulNSN = new CalculNSN3G(operateur, datedebut, datefin);
                    //calCulNSN.CalculTotal();
                }
            }
            //Float.parseFloat(datefin)
            bCalcul=true;
            System.out.println("/*/*/*/*/*/*Fin  du calcul /*/*/*/*/*/*/*/*/*-------------");
        }
        catch (Exception ex)
        {
            bCalcul=false;
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Rapport "+DateduJour+" Heure:"+heuredejour+" Erreur:"+ex.getMessage(),cheminfichierlog);
            Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            System.out.println("/*/*/*/*/*/*Debut carte /*/*/*/*/*/*/*/*/*-------------");
            Trafic etatTrafic=new Trafic();
            etatTrafic.trafic(cheminfichierparam,chemingraphiques, cheminrapport,cheminfichierlog, operateur);

            Accessibilite etatAccessibilite=new Accessibilite();
            etatAccessibilite.accessibilite(cheminfichierparam,chemingraphiques, cheminrapport,cheminfichierlog, operateur);

            Maintenabilite etatMaintenabilite=new Maintenabilite();
            etatMaintenabilite.maintenabilite(cheminfichierparam,chemingraphiques, cheminrapport, cheminfichierlog,  operateur);

            Mobilite etatMobilite=new Mobilite();
            etatMobilite.mobilite(cheminfichierparam,chemingraphiques, cheminrapport,cheminfichierlog,operateur);

            SMS etatsms=new SMS();
            etatsms.sms(cheminfichierparam,chemingraphiques,cheminrapport,cheminfichierlog,operateur);

            String perioderapport=this.datedebut+"_"+this.datefin;

            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String[] tabKpi={"BHTR","TCHCRBH","CDRBH","Hosucces","SMSLR"};
            for (int i = 0; i < tabKpi.length; i++)
            {
                String kpi=tabKpi[i];
                ParametreKPI parametreKPI = cnbdd.getParametresKPIFromKPI(kpi);
                //Filter filter = null;
                double seuil1 = parametreKPI.getSeuil1() / 100, seuil2 = parametreKPI.getSeuil2() / 100;
                String requete = "delete from table_bts_"+kpi;
                try
                {
                    cn.ExecuterRequete(requete);
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (parametreKPI.getType().equalsIgnoreCase("rov"))
                {
                    //filter=CQL.toFilter(kpi.toLowerCase()+" < "+seuil1);
                    try
                    {
                        requete = "select * from table_valeurs_bts where " + kpi.toLowerCase() + "<" + seuil1;
                        ResultSet result = cn.getResultset(requete);
                        while (result.next())
                        {
                            requete = "insert into table_bts_"+kpi + "(region,cell_name,valkpi,the_geom) values('" + result.getString("region") + "','" + result.getString("cell_name") + "'," + result.getDouble(kpi.toLowerCase()) + ",'" + result.getObject("the_geom").toString() + "') ";
                            cn.ExecuterRequete(requete);
                        }
                    }
                    catch (Exception ex)
                    {
                        Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (parametreKPI.getType().equalsIgnoreCase("vor"))
                {
                    requete = "select * from table_valeurs_bts where " + kpi.toLowerCase() + ">" + seuil2;
                    ResultSet result = cn.getResultset(requete);
                    try
                    {
                        while (result.next())
                        {
                            try
                            {
                                requete = "insert into table_bts_"+kpi + "(region,cell_name,valkpi,the_geom) values('" + result.getString("region") + "','" + result.getString("cell_name") + "'," + result.getDouble(kpi.toLowerCase()) + ",'" + result.getObject("the_geom").toString() + "') ";
                                cn.ExecuterRequete(requete);
                            }
                            catch (Exception ex)
                            {
                                Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            if(b==true)
                fin(operateur, cheminfichierparam, cheminfichierlog, chemingraphiques, cheminrapport, perioderapport);

            System.out.println("**************************************************fin de rapport*******************");
            bCalcul=true;
            Interface_Principale.CalculFini=true;
           System.out.println("/*/*/*/*/*/*Fin  carte /*/*/*/*/*/*/*/*/*-------------");
        }
        catch (Exception e)
        {
            bCalcul=false;
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Rapport "+DateduJour+" Heure:"+heuredejour+" Erreur:"+e.getMessage(),cheminfichierlog);
            Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, e);
        }
        return bCalcul;
    }
	
    private void fin(Operateur operateur, String cheminfichierparam, String cheminfichierlog,String chemingraphiques, String cheminrapport, String perioderapport)
    {
        Thumbnail cadransynthese = new Thumbnail(chemingraphiques+"synthese1.jpg", cheminfichierlog, chemingraphiques+"traficBHTR.jpg", chemingraphiques+"Acces15.jpg", chemingraphiques+"Acces3.jpg", chemingraphiques+"Acces4.jpg", chemingraphiques+"Acces5.jpg", chemingraphiques+"Acces7.jpg");
        Thumbnail cadransynthese2 = new Thumbnail(chemingraphiques+"synthese2.jpg", cheminfichierlog, chemingraphiques+"Acces8.jpg", chemingraphiques+"Maintien3.jpg", chemingraphiques+"Maintien4.jpg", chemingraphiques+"HO1.jpg", chemingraphiques+"HO7.jpg", chemingraphiques+"HO3.jpg");
        cadransynthese.dispose();
        cadransynthese2.dispose();
        String str=chemingraphiques+"logo.jpg";
        boolean bLogo=false;
        try
        {
            bLogo=getLogoOperateur(chemingraphiques+"logo.jpg", operateur.getCodeOperateur());
        } catch (Exception ex)
        {
            Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
        }
        File file=new File(str);
        if (file.exists() && bLogo)
        {
            GeneratePDF rapportpdf = new GeneratePDF();
            boolean b1 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "Page_de_garde.jrxml", cheminrapport + "Page_de_garde.pdf", datedebut, datefin, operateur,false);
            boolean b2 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "Rapport_de_synthese.jrxml", cheminrapport + "Rapport_de_synthese.pdf", datedebut, datefin, operateur,false);
            boolean b3=rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "affichage.jrxml", cheminrapport + "affichage.pdf", datedebut, datefin, operateur,false);
            boolean b4 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "listeregion.jrxml", cheminrapport + "listeregion.pdf", datedebut, datefin, operateur,true);
            boolean b5 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page1.jrxml", cheminrapport + "page1.pdf", datedebut, datefin, operateur,false);
            boolean b6 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page2.jrxml", cheminrapport + "page2.pdf", datedebut, datefin, operateur,false);
            boolean b7 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page3.jrxml", cheminrapport + "page3.pdf", datedebut, datefin, operateur,false);
            boolean b8 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page4.jrxml", cheminrapport + "page4.pdf", datedebut, datefin, operateur,false);
            boolean b9 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page5.jrxml", cheminrapport + "page5.pdf", datedebut, datefin, operateur,false);
            boolean b10 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page6.jrxml", cheminrapport + "page6.pdf", datedebut, datefin, operateur,false);
            boolean b11 = rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "page7.jrxml", cheminrapport + "page7.pdf", datedebut, datefin, operateur,false);

            String chemin1 = cheminrapport + "Rapport_" + operateur.getNomOperateur().toUpperCase() + "_" + perioderapport + ".pdf";

            String[] tabKpi={"BHTR","TCHCRBH","CDRBH","Hosucces","SMSLR"};
            ConnexionBDDOperateur cn=null;
            try
            {
                cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
            } catch (SQLException ex)
            {
                Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int i = 0; i < tabKpi.length; i++)
            {
                //System.out.println("Fichier param:"+cheminfichierparam +"listebtsdown_"+tabKpi[i].toUpperCase()+".jrxml");
                String requete="select count(*) as nbre from table_bts_"+tabKpi[i];
                int nbre=0;
                if(cn!=null)
                {
                    try
                    {
                        ResultSet resultSet = cn.getResultset(requete);
                        if (resultSet.next())
                        {
                            nbre=resultSet.getInt("nbre");
                        }
                    }
                    catch (Exception ex)
                    {
                        Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                        nbre=0;
                    }
                }
                if(nbre>0)
                {
                    rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "listebtsdown_" + tabKpi[i].toUpperCase() + ".jrxml", cheminrapport + "listebtsdown_" + tabKpi[i].toUpperCase() + ".pdf", datedebut, datefin, operateur,true);
                }else if(nbre==0)
                {
                    rapportpdf.genererPDF(cheminfichierlog, cheminfichierparam + "listebtsdownEntete_" + tabKpi[i].toUpperCase() + ".jrxml", cheminrapport + "listebtsdown_" + tabKpi[i].toUpperCase() + ".pdf", datedebut, datefin, operateur,false);
                }
            }

            String[] ordrerapport = new String[]{cheminrapport + "Page_de_garde.pdf", cheminrapport + "Rapport_de_synthese.pdf",  cheminrapport + "affichage.pdf",
                    cheminrapport + "page1.pdf",cheminrapport + "page2.pdf", cheminrapport + "page3.pdf", cheminrapport + "page4.pdf", cheminrapport + "page5.pdf",
                    cheminrapport + "page6.pdf",cheminrapport + "page7.pdf", cheminrapport + "listeregion.pdf" ,cheminrapport+"listebtsdown_"+tabKpi[0].toUpperCase()+".pdf",
                    cheminrapport+"listebtsdown_"+tabKpi[1].toUpperCase()+".pdf",cheminrapport+"listebtsdown_"+tabKpi[2].toUpperCase()+".pdf",cheminrapport+"listebtsdown_"+tabKpi[3].toUpperCase()+".pdf",
                    cheminrapport+"listebtsdown_"+tabKpi[4].toUpperCase()+".pdf", chemin1};

            Concat rapportfinal = new Concat();
            rapportfinal.concatener(cheminfichierlog, ordrerapport);

            if (b1 != true || b2 != true || b3 != true || b4 != true || b5 != true || b6 != true || b7 != true || b8 != true || b9 != true || b10 != true || b11!=true)
            {
                JOptionPane.showMessageDialog(null, "Erreur lors de la génération du Fichier PDF", "Erreur", JOptionPane.INFORMATION_MESSAGE);
            }
 
            Finprocessus supprimsuperflu = new Finprocessus();
            for (int i = 1; i <= 7; i++)
            {
                supprimsuperflu.supprime(cheminrapport + "page" + i + ".pdf");
            }
            for (int i = 0; i < tabKpi.length; i++)
            {
                supprimsuperflu.supprime(cheminrapport +"listebtsdown_"+tabKpi[i]+".pdf");
            }
            supprimsuperflu.supprime(cheminrapport + "affichage.pdf");
            supprimsuperflu.supprime(cheminrapport + "Page_de_garde.pdf");
            supprimsuperflu.supprime(cheminrapport + "listeregion.pdf");
            supprimsuperflu.supprime(cheminrapport + "Rapport_de_synthese.pdf");
            supprimsuperflu.supprime(chemingraphiques + "logo.jpg");

            String mailFrom = "fotsonono2003@yahoo.fr";
            String mailto = JFrame_connexion.email;
            String password = "password";
            String serveur = "smtp.mail.yahoo.fr";
            int port=25;
            Envoirapport adresse = new Envoirapport();
            try
            {
                adresse.envoyermail(cheminfichierlog, serveur,port, mailFrom, password, mailto, chemin1);
            }
            catch (SQLException ex)
            {
                Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
                JOptionPane.showMessageDialog(null, "Erreur lors de la récupération du logo de l'opérateur", "Erreur", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        System.out.println("********************Destruction de la classe rapport");
        super.finalize();
        System.out.println("********************Fin Destruction de la classe rapport");
    }

    private boolean getLogoOperateur(String location,String codeOperateur) throws Exception
    {
        File monImage = new File(location);
        FileOutputStream ostreamImage = new FileOutputStream(monImage);
        boolean test=false;
        try
        {
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            Connection conn=cnbdd.getConnection();
            PreparedStatement ps = conn.prepareStatement("select logo from operateur where code_operateur=?");
            try
            {
                ps.setString(1, codeOperateur);
                ResultSet rs = ps.executeQuery();
                try
                {
                    if (rs.next())
                    {
                        InputStream istreamImage = rs.getBinaryStream("logo");
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        while ((length = istreamImage.read(buffer)) != -1)
                        {
                            ostreamImage.write(buffer, 0, length);
                        }
                    }
                    test=true;
                }
                catch(Exception ex)
                {
                    test=false;
                }
                finally
                {
                    rs.close();
                }
            }
            catch(Exception ex)
            {
                test=false;
            }
            finally
            {
                try
                {
                    conn.close();
                    ps.close();
                } catch (Exception exception) {
                }
            }
        }
        catch(Exception ex)
        {
            test=false;
        }
        finally
        {
            ostreamImage.close();
        }
        return test;
    }


    @Override
    protected Boolean  doInBackground() throws Exception
    {
        boolean bCalcul=false;
        File file = new File(mes.get_CheminImage() + operateur.getCodeOperateur());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        String cheminfichierlog=mes.get_CheminLog();
        String cheminfichierparam=mes.get_CheminParametre();
        String cheminrapport=mes.get_CheminRapport();
        String chemingraphiques=mes.get_CheminImage()+operateur.getCodeOperateur()+File.separator;

        Finprocessus supprimsuperflu = new Finprocessus();
        supprimsuperflu.vidertable(cheminfichierlog, operateur);//permet de vider les tables de valeurs des KPi de l'operateur.
        
        try
        {
            System.out.println("/*/*/*/*/*/*debut  du calcul /*/*/*/*/*/*/*/*/*-------------");

            String equipement = operateur.getEquipement();
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                if (equipement.toUpperCase().startsWith("ALC")) {
                    CalculAlcatel2G calculAlcatel = new CalculAlcatel2G(operateur, datedebut, datefin);
                    //calculAlcatel.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ERI")) {
                    CalCulEricsson2G calCulEricsson = new CalCulEricsson2G(operateur, datedebut, datefin);
                    calCulEricsson.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ZTE")) {
                    CalculZTE2G calculZTE = new CalculZTE2G(operateur, datedebut, datefin);
                    //calculZTE.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("HW")) {
                    CalculHuawei2G calcul = new CalculHuawei2G(operateur, datedebut, datefin);
                    //calcul.calculTotal();
                } else if (equipement.toUpperCase().startsWith("NSN")) {
                    CalculNSN2G calCulNSN = new CalculNSN2G(operateur, datedebut, datefin);
                    //calCulNSN.CalculTotal();
                }
            } else if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                if (equipement.toUpperCase().startsWith("ALC")) {
                    CalculAlcatel3G calculAlcatel = new CalculAlcatel3G(operateur, datedebut, datefin);
                    //calculAlcatel.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ERI")) {
                    CalCulEricsson3G calCulEricsson = new CalCulEricsson3G(operateur, datedebut, datefin);
                    calCulEricsson.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("ZTE")) {
                    CalculZTE3G calculZTE = new CalculZTE3G(operateur, datedebut, datefin);
                    //calculZTE.CalculTotal();
                } else if (equipement.toUpperCase().startsWith("HW")) {
                    CalculHuawei3G calcul = new CalculHuawei3G(operateur, datedebut, datefin);
                    //calcul.calculTotal();
                } else if (equipement.toUpperCase().startsWith("NSN")) {
                    CalculNSN3G calCulNSN = new CalculNSN3G(operateur, datedebut, datefin);
                    //calCulNSN.CalculTotal();
                }
            }
            //Float.parseFloat(datefin)
            bCalcul=true;
            System.out.println("/*/*/*/*/*/*Fin  du calcul /*/*/*/*/*/*/*/*/*-------------");
        }
        catch (Exception ex)
        {
            bCalcul=false;
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Rapport "+DateduJour+" Heure:"+heuredejour+" Erreur:"+ex.getMessage(),cheminfichierlog);
            Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            System.out.println("/*/*/*/*/*/*Debut carte /*/*/*/*/*/*/*/*/*-------------");
            Trafic etatTrafic=new Trafic();
            etatTrafic.trafic(cheminfichierparam,chemingraphiques, cheminrapport,cheminfichierlog, operateur);

            Accessibilite etatAccessibilite=new Accessibilite();
            etatAccessibilite.accessibilite(cheminfichierparam,chemingraphiques, cheminrapport,cheminfichierlog, operateur);

            Maintenabilite etatMaintenabilite=new Maintenabilite();
            etatMaintenabilite.maintenabilite(cheminfichierparam,chemingraphiques, cheminrapport, cheminfichierlog,  operateur);

            Mobilite etatMobilite=new Mobilite();
            etatMobilite.mobilite(cheminfichierparam,chemingraphiques, cheminrapport,cheminfichierlog,operateur);

            SMS etatsms=new SMS();
            etatsms.sms(cheminfichierparam,chemingraphiques,cheminrapport,cheminfichierlog,operateur);

            String perioderapport=this.datedebut+"_"+this.datefin;

            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String[] tabKpi={"BHTR","TCHCRBH","CDRBH","Hosucces","SMSLR"};
            for (int i = 0; i < tabKpi.length; i++)
            {
                String kpi=tabKpi[i];
                ParametreKPI parametreKPI = cnbdd.getParametresKPIFromKPI(kpi);
                //Filter filter = null;
                double seuil1 = parametreKPI.getSeuil1() / 100, seuil2 = parametreKPI.getSeuil2() / 100;
                String requete = "delete from table_bts_"+kpi;
                try
                {
                    cn.ExecuterRequete(requete);
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (parametreKPI.getType().equalsIgnoreCase("rov"))
                {
                    //filter=CQL.toFilter(kpi.toLowerCase()+" < "+seuil1);
                    try
                    {
                        requete = "select * from table_valeurs_bts where " + kpi.toLowerCase() + "<" + seuil1;
                        ResultSet result = cn.getResultset(requete);
                        while (result.next())
                        {
                            requete = "insert into table_bts_"+kpi + "(region,cell_name,valkpi,the_geom) values('" + result.getString("region") + "','" + result.getString("cell_name") + "'," + result.getDouble(kpi.toLowerCase()) + ",'" + result.getObject("the_geom").toString() + "') ";
                            cn.ExecuterRequete(requete);
                        }
                    }
                    catch (Exception ex)
                    {
                        Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (parametreKPI.getType().equalsIgnoreCase("vor"))
                {
                    requete = "select * from table_valeurs_bts where " + kpi.toLowerCase() + ">" + seuil2;
                    ResultSet result = cn.getResultset(requete);
                    try
                    {
                        while (result.next())
                        {
                            try
                            {
                                requete = "insert into table_bts_"+kpi + "(region,cell_name,valkpi,the_geom) values('" + result.getString("region") + "','" + result.getString("cell_name") + "'," + result.getDouble(kpi.toLowerCase()) + ",'" + result.getObject("the_geom").toString() + "') ";
                                cn.ExecuterRequete(requete);
                            }
                            catch (Exception ex)
                            {
                                Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    catch (SQLException ex)
                    {
                        Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            if(b==true)
                fin(operateur, cheminfichierparam, cheminfichierlog, chemingraphiques, cheminrapport, perioderapport);

            System.out.println("**************************************************fin de rapport*******************");
            bCalcul=true;
            Interface_Principale.CalculFini=true;
           System.out.println("/*/*/*/*/*/*Fin  carte /*/*/*/*/*/*/*/*/*-------------");
        }
        catch (Exception e)
        {
            bCalcul=false;
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Rapport "+DateduJour+" Heure:"+heuredejour+" Erreur:"+e.getMessage(),cheminfichierlog);
            Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, e);
        }
        return bCalcul;
    }

    @Override
    public void done()
    {
        try
        {
            //boolean status=get();
            Toolkit.getDefaultToolkit().beep();
            System.out.println("-------Rapport Done------------\n");
        }
        catch (Exception ex)
        {
            Finprocessus finprocessus = new Finprocessus();
            finprocessus.vidertable(mes.get_CheminLog(), operateur);
            //Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erreur lors du calcul:"+ex.toString(),"Erreur",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Throwable ex)
        {
            //Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Suspension du calcul:"+ex.toString(),"Erreur",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String [] arg)
    {
        try
        {
            Operateur operateur = new Operateur();
            operateur.setCodeOperateur("AZ");
            operateur.setBddOperateur("azur_ericsson");
            operateur.setNomOperateur("AZUR");
            operateur.setGeneration("2G");
            operateur.setEquipement("ERI");
            Rapport rpt = new Rapport(operateur, "2012-08-22", "2013-08-25", true);
            rpt.rapport();
            System.exit(0);
        }
        catch (Exception ex)
        {
            Logger.getLogger(Rapport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}