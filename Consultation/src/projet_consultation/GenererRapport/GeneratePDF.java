package projet_consultation.GenererRapport;
 
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.creation_dossiers.mes_documents;

public class GeneratePDF 
{
    public boolean genererPDF (String cheminfichierlog, String fichierParametre, String fichierPdf, String datedebut, String datefin,Operateur operateur,boolean  needConnexion )
    {
        Date date=new Date();
	SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
	Fichier fichier =new Fichier();
	try
	{
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            Connection connexion=cn.getConnection();//DriverManager.getConnection("jdbc:postgresql://"+cn.getAdresseIP()+":"+cn.getPort()+"/"+cn.getBDDOperateur(),cn.getUtilisateur(),cn.getPassword());
            mes_documents mes=new mes_documents();
            Map<String,String> parameters = new HashMap<String,String>();
            String cheminImage=mes.get_CheminImage()+operateur.getCodeOperateur()+File.separator;
            String cheminRapport=mes.get_CheminRapport();
            String requete="";
            if(fichierPdf.equalsIgnoreCase(cheminRapport+"Rapport_de_synthese.pdf"))
            {/////////
                String [] tab={"CSSR","TCHBRBH","TCHDRBH","SDCCHBRBH","SDCCHDRBH","SDCCHCRBH","CDRBH","HOSucces","HOULQR","HODLQR","CSRBH"};
                Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
                for (int i=0;i<tab.length;i++)
                {
                    double valG=0;int nbreTotal=0,nbre=0;double seuil=0;
                    ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(tab[i]);
                    String str="select region,"+tab[i].toLowerCase()+" from tablevaleurskpi ";
                    ResultSet resultSet=cn.getResultset(str);
                    while(resultSet.next())
                    {
                        String region =""; 
                        double valeur = 0;
                        try
                        {
                            region = resultSet.getString("region").trim();
                            valeur = (resultSet.getDouble(tab[i].toLowerCase())*100);
                            valeur = (double)((int)(valeur*10000))/10000;
                        } catch (SQLException ex)
                        {
                            region ="";
                            valeur = 0;
                            Logger.getLogger(GeneratePDF.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if(!region.trim().equalsIgnoreCase("Global"))
                            nbreTotal++;
                        if(region.equalsIgnoreCase("Global"))
                        {
                            valeur = (double)((int)(valeur*100))/100;
                            valG=valeur;
                        }
                        if(parametreKPI.getType().trim().equalsIgnoreCase("rov"))
                        {
                            seuil=parametreKPI.getSeuil1();
                            if(valeur<seuil && !region.trim().equalsIgnoreCase("Global"))
                            {
                                nbre++;
                            }
                        }
                        else if(parametreKPI.getType().trim().equalsIgnoreCase("vor"))
                        {
                            seuil=parametreKPI.getSeuil2();
                            if(valeur>seuil && !region.trim().equalsIgnoreCase("Global"))
                            {
                                nbre++;
                            }
                        }
                    }
                    double val=0;
                    if((nbreTotal-1)>0)
                    {
                        val=((double)nbre/nbreTotal);
                        val=val*100;
                        val=(double)((int)(val*10000))/10000;
                    }

                    parameters.put("S"+tab[i].toLowerCase(), ""+seuil);
                    parameters.put("G"+tab[i].toLowerCase(), ""+valG);
                    parameters.put("R"+tab[i].toLowerCase(), ""+val);
                }
            }///////
            else if(fichierPdf.equalsIgnoreCase(cheminRapport+"synthese_bts.pdf"))
            {
                String [] tab={"TCHCRBH","TCHDRBH","TCHAV","SDCCHDRBH","SDCCHCRBH","CDRBH","HOFAILUREBH","CSRBH"};
                Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();

                requete="select tchav, tchcrbh, tchdrbh, sdcchcrbh, sdcchdrbh, ccsrbh, cdrbh, hofailurebh"
                        + "from tablevaleurskpi "
                        + "where  ";
                for (int i=0;i<tab.length;i++)
                {
                    double seuil=0;
                    ParametreKPI parametreKPI = cnbdd.getParametresKPIFromKPI(tab[i]);
                    if(parametreKPI.getType().trim().equalsIgnoreCase("vor"))
                    {
                        seuil=parametreKPI.getSeuil2()/100;
                        parameters.put("s" + tab[i].toLowerCase(), "" + seuil);
                        requete=requete+" "+tab[i].toLowerCase()+">"+seuil+" or";
                    }
                    else if(parametreKPI.getType().trim().equalsIgnoreCase("rov"))
                    {
                        seuil=parametreKPI.getSeuil1()/100;
                        parameters.put("s" + tab[i].toLowerCase(), "" + seuil);
                        requete=requete+" "+tab[i].toLowerCase()+"<"+seuil+" or";
                    }
                }
                requete=requete.substring(0, requete.length()-2);
                requete=requete+" order by region";
                parameters.put("requete", requete);
            }
            parameters.put("cheminImage", cheminImage);
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(projet_consultation.Projet_ConsultationApp.class).getContext().getResourceMap(projet_consultation.Principale.Interface_Principale.class);
            parameters.put("logo_regulateur",resourceMap.getImageIcon("logo_regulateur").toString());
            parameters.put("logo_operateur",cheminImage+"logo.jpg");
            parameters.put("dateDebut",datedebut);
            parameters.put("dateFin",datefin);
            parameters.put("operateur",operateur.getNomOperateur());
            parameters.put("drapeau",resourceMap.getImageIcon("drapeau").toString());

            List<String>lstStr=new ArrayList<String>();
            lstStr.add(cheminImage);
            lstStr.add(datedebut);
            lstStr.add(datefin);
            lstStr.add(resourceMap.getImageIcon("drapeau").toString());
            lstStr.add(operateur.getNomOperateur());
            lstStr.add(cheminImage+"logo.jpg");
            lstStr.add(resourceMap.getImageIcon("logo_regulateur").toString());
            DatasourcePersonalise datasource=new DatasourcePersonalise(lstStr);

            JasperDesign jasperDesign = JRXmlLoader.load(fichierParametre);
            if(fichierPdf.equalsIgnoreCase(cheminRapport+"synthese_bts.pdf"))
            {
                JRDesignQuery jrdquery = new JRDesignQuery();
                jrdquery.setText(requete);
                jasperDesign.setQuery(jrdquery);
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            if (needConnexion)
            {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connexion);
                JasperExportManager.exportReportToPdfFile(jasperPrint, fichierPdf);
                System.out.println("----------------------Rapport avec connexion:"+fichierPdf);
            } else {
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,datasource);
                JasperExportManager.exportReportToPdfFile(jasperPrint, fichierPdf);
                System.out.println("****************************Rapport sans connexion:"+fichierPdf);
            }
            
            cn.closeConnection();
            return true;
        }
	catch (JRException ex)
	{
            formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            fichier.ecrire("Classe GeneratePDF,méthode:genererPDF:"+DateduJour+" Heure:"+heuredejour+ " Erreur:"+ex, cheminfichierlog);
            Logger.getLogger(GeneratePDF.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
	catch (SQLException ex)
        {            
            formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            fichier.ecrire("Classe GeneratePDF,méthode:genererPDF:"+DateduJour+" Heure:"+heuredejour+ " Erreur:"+ex.getMessage(), cheminfichierlog);
            Logger.getLogger(GeneratePDF.class.getName()).log(Level.SEVERE, null, ex);
            return false;
	}
	catch (Exception ex)
        {
            formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            fichier.ecrire("Classe GeneratePDF,méthode:genererPDF:"+DateduJour+" Heure:"+heuredejour+ " Erreur:"+ex.getMessage(), cheminfichierlog);
            Logger.getLogger(GeneratePDF.class.getName()).log(Level.SEVERE, null, ex);
            return false;
	}
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    public class  DatasourcePersonalise implements JRDataSource
    {
        private int index = -1;
        private Object[][] data;

        public DatasourcePersonalise(List<String> lstchemin)
        {
            if(lstchemin!=null && lstchemin.size()>0)
            {
                data = new Object[1][lstchemin.size()];
                int cpt = 0;
                for (String str : lstchemin)
                {
                    data[0][cpt] = str;
                    cpt++;
                }
            }
        }

        public boolean next() throws JRException
        {
            index++;
            return (index < data.length);
        }

        public Object getFieldValue(JRField jrf) throws JRException
        {
            Object value = null;
            if (jrf.getName().equals("cheminImage"))
            {
                value = data[0][index];
            }
            else if(jrf.getName().equals("logo_operateur"))
            {
                value =  data[0][index];
            }
            else if(jrf.getName().equals("logo_regulateur"))
            {
                value = data[0][index];
            }
            else if(jrf.getName().equals("drapeau"))
            {
                value = data[0][index];
            }
            else if(jrf.getName().equals("dateDebut"))
            {
                value = data[0][index];
            }
            else if(jrf.getName().equals("dateFin"))
            {
                value =data[0][index];
            }
            else if(jrf.getName().equals("operateur"))
            {
                value = data[0][index];
            }
            return value;
        }

    }
}