package Services;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import modele.Carte;
import modele.Commande;
import modele.Commercant;
import modele.Employe;
import modele.Employeur;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


public class GenererPDF 
{
	File fileTest=new File(this.getClass().getResource("/").getFile());
	//private final String cheminCommercant=fileTest.getParentFile().getPath()+File.separator+"employeur"+File.separator;
	//private final String cheminEmploye=fileTest.getParentFile().getPath()+File.separator+"employe"+File.separator;
	private final String logo=fileTest.getParentFile().getPath()+File.separator+"logo.jpg";
	
	private final String fichierParametre=fileTest.getParentFile().getPath()+File.separator+"ressources"+File.separator;
	
	private final String fichierPDFCommercant=fileTest.getParentFile().getPath()+File.separator+"commercant"+File.separator;
	private final String fichierPDFEmploye=fileTest.getParentFile().getPath()+File.separator+"employe"+File.separator;
	
	
	public void GenererPDFTransactionsCommercant(Commercant commercant,String dateDebut,String dateFin)
	{
        
		Connexion cn=new Connexion();
        Connection connexion=cn.getC();//DriverManager.getConnection("jdbc:postgresql://"+cn.getAdresseIP()+":"+cn.getPort()+"/"+cn.getBDDOperateur(),cn.getUtilisateur(),cn.getPassword());
        Map<String,String> parameters = new HashMap<String,String>();
        int numcommercant=commercant.getNumcommercant();
		File file=new File(fichierPDFCommercant+numcommercant+File.separator);
		
		if(!file.isDirectory())
		{
			file.mkdirs();
		}

		file=new File(fichierPDFCommercant+numcommercant+File.separator+"transactions.pdf");
		if(file.exists())
		{
			file.delete();
		}
		
        parameters.put("logo", logo);
        parameters.put("dateDebut",dateDebut);
        parameters.put("dateFin",dateFin);
        parameters.put("raisonsociale",commercant.getRaisonSociale());
        parameters.put("nbcarte","");
        parameters.put("unite","");
        parameters.put("montant","");
        try 
        {
        	String requete="SELECT transactions.numtransaction, transactions.dateheuretransaction, transactions.numLecteur, commercant.raisonSociale, transactions.montant, transactions.dateheuretransaction";
        	requete=requete+" FROM transactions, commercant, lecteurcarte ";
        	requete=requete+" WHERE commercant.numcommercant ="+numcommercant+"  and commercant.numcommercant = lecteurcarte.numcommercant and transactions.traite=false ";
        	requete=requete+" AND DATE(transactions.dateheuretransaction )>= '"+dateDebut+"'  AND DATE(transactions.dateheuretransaction )<='"+dateFin+"' ";
        	
        	/* requete="SELECT transactions.numtransaction, transactions.dateheuretransaction, transactions.numLecteur, commercant.raisonSociale, transactions.montant, transactions.dateheuretransaction";
        	requete=requete+" FROM transactions, commercant, lecteurcarte ";
        	requete=requete+" WHERE commercant.numcommercant ="+numcommercant+"  and commercant.numcommercant = lecteurcarte.numcommercant ";
        	requete=requete+" AND transactions.dateheuretransaction >= '"+dateDebut+"'  AND transactions.dateheuretransaction<='"+dateFin+"' ";
        	*/
        	
        	JasperDesign jasperDesign = JRXmlLoader.load(fichierParametre+"transactionscommercant.jrxml");
            
    		System.out.println("requuuuuueeettte:"+requete);
        	JRDesignQuery jrdquery = new JRDesignQuery();
            jrdquery.setText(requete);
            jasperDesign.setQuery(jrdquery);

        	
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            
		} catch (JRException ex) 
		{
			ex.printStackTrace();
		}

        
	}


	public void GenererPDFTransactionsEmploye(Employe employe,String dateDebut,String dateFin)
	{
        Carte carte=employe.getCarte();
		Connexion cn=new Connexion();
        Connection connexion=cn.getC();//DriverManager.getConnection("jdbc:postgresql://"+cn.getAdresseIP()+":"+cn.getPort()+"/"+cn.getBDDOperateur(),cn.getUtilisateur(),cn.getPassword());
        Map<String,String> parameters = new HashMap<String,String>();
        int numemploye=employe.getNumemploye();
		File file=new File(fichierPDFEmploye+numemploye+File.separator);
		
		if(!file.isDirectory())
		{
			file.mkdirs();
		}

		file=new File(fichierPDFEmploye+numemploye+File.separator+"transactions.pdf");
		if(file.exists())
		{
			file.delete();
		}
		
        parameters.put("logo", logo);
        parameters.put("dateDebut",dateDebut);
        parameters.put("dateFin",dateFin);
        parameters.put("raisonsociale","");
        parameters.put("nbcarte","");
        parameters.put("unite","");
        parameters.put("montant","");
        parameters.put("nom",employe.getPrenom()+" "+employe.getNom());
        try 
        {
        	String requete="select numtransaction,numerocarte,transactions.numLecteur,montant,dateheuretransaction, traite,commercant.raisonsociale,commercant.adresse  ";
        	requete=requete+" from transactions,lecteurcarte,commercant   ";
        	requete=requete+" where traite =false and transactions.numLecteur=lecteurcarte.numLecteur  ";        	
        	requete=requete+" and lecteurcarte.numcommercant=commercant.numcommercant and numerocarte="+carte.getNumerocarte()+"  ";        	
        	requete=requete+" AND DATE(transactions.dateheuretransaction )>= '"+dateDebut+"'  AND DATE(transactions.dateheuretransaction )<='"+dateFin+"' ";
        	        	
        	JasperDesign jasperDesign = JRXmlLoader.load(fichierParametre+"transactionsemploye.jrxml");
            
    		System.out.println("requuuuuueeettte:"+requete);
        	JRDesignQuery jrdquery = new JRDesignQuery();
            jrdquery.setText(requete);
            jasperDesign.setQuery(jrdquery);
        	
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            
		} catch (JRException ex) 
		{
			ex.printStackTrace();
		}

        
	}


	public void GenererPDFFacture(Employeur employeur,Commande commande)
	{
		Connexion cn=new Connexion();
        Connection connexion=cn.getC();//DriverManager.getConnection("jdbc:postgresql://"+cn.getAdresseIP()+":"+cn.getPort()+"/"+cn.getBDDOperateur(),cn.getUtilisateur(),cn.getPassword());
        Map<String,String> parameters = new HashMap<String,String>();
        int numemployeur=employeur.getNumemployeur();
		File file=new File(fichierPDFEmploye+numemployeur+File.separator);
		
		if(!file.isDirectory())
		{
			file.mkdirs();
		}

		file=new File(fichierPDFEmploye+numemployeur+File.separator+"facture .pdf");
		if(file.exists())
		{
			file.delete();
		}
		
        parameters.put("logo", logo);
        parameters.put("dateDebut","");
        parameters.put("dateFin","");
        parameters.put("raisonsociale",employeur.getRaisonsocial());
        parameters.put("nbcarte",""+commande.getNbcarte());
        parameters.put("unite",""+5);
        parameters.put("montant",""+commande.getNbcarte()*10);
        try 
        {
        	String requete="SELECT numtransactions FROM  transactions LIMIT 0 , 1 ";
        	        	
        	JasperDesign jasperDesign = JRXmlLoader.load(fichierParametre+"FactureNouveau.jrxml");
            
    		System.out.println("requuuuuueeettte:"+requete);
        	JRDesignQuery jrdquery = new JRDesignQuery();
            jrdquery.setText(requete);
            jasperDesign.setQuery(jrdquery);
        	
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connexion);
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            
		} catch (JRException ex) 
		{
			ex.printStackTrace();
		}

        
	}


}
