package test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.TransactionsDAO;
import Services.GenererPDF;
import Services.ServicesCommandes;
import Services.ServicesEmploie;
import Services.ServicesFacture;
import Services.ServicesTransactions;
import util.HibernateUtil;
import modele.Commande;
import modele.Employe;
import modele.Employeur;
import modele.Transactions;

public class TestMain
{

    public static void main(String[] args)
    {
        Employe emp = new Employe();
        emp.setNom("Titi");
        emp.setPrenom("Narcisse");
        emp.setEmail("toto@yahoo.fr");
        emp.setLogin("login");
        emp.setPassword("password");
        emp.setDateEnregistrement(new Date());

        Employe emp1 = new Employe();
        emp1.setNom("toto");
        emp1.setPrenom("Thierry");
        emp1.setEmail("titi@yahoo.fr");
        emp1.setLogin("login");
        emp1.setPassword("password");
        emp1.setDateEnregistrement(new Date());

        Employe emp2 = new Employe();
        emp2.setNom("tutu");
        emp2.setPrenom("Thierry");
        emp2.setEmail("titi@yahoo.fr");
        emp2.setLogin("login");
        emp2.setPassword("password");
        emp2.setDateEnregistrement(new Date());

        Employe emp3 = new Employe();
        emp3.setNom("tutu");
        emp3.setPrenom("Thierry");
        emp3.setEmail("titi@yahoo.fr");
        emp3.setLogin("login");
        emp3.setPassword("password111");
        emp3.setDateEnregistrement(new Date());
        
        ServicesEmploie services=new ServicesEmploie();
        //services.AddEmploye(emp3);
        Employe e1=services.getEmployeByNumero(20);//new Employe();
        System.out.println("Employe:"+e1);
        //System.out.println(e1.getEmployeur().getListEmployes());
        //e1.setLogin("thierry");
        //e1.setDateEnregistrement(new Date());
        services.MiseAjourEmploye(e1);
        Employeur employeur=services.getEmployeurByNumero(14);
        //System.out.println("Employeur:"+employeur.getListEmployes());
        //System.out.println("/*/*/*/*/*/*/Liste Employes:"+services.findAllEmployes());
       // System.out.println("/*/*/*/*/*/*/Liste Employeurs:"+services.findAllEmployeurs());
        e1=new Employe();
        e1.setLogin("thierry");
        Date d=new Date();
        e1.setDateEnregistrement(d);        
        //services.AddEmployeToEmployeur(14, e1);
        //System.out.println("Employeur verif:"+services.VerifierLoginAndPasswordEmployeur("thierry", "thierry"));
        //System.out.println("Employe verif:"+services.VerifierLoginAndPasswordEmploye("thierry", "thierry"));
        //services.MiseAjourEmploye(e1);
        //System.out.println(employeur);
        //services.SupprimerEmployeur(employeur);        
        //services.findAllEmployes();
        //employeur=services.getEmployeurByLogin("thierry");
        Employe e2=new Employe();
        e2.setLogin("login2");
        //services.AddEmploye(e2);
        employeur.setLogin("employeur2");
        ServicesCommandes servicesCommandes=new ServicesCommandes();
        Commande commande=new Commande();
        //servicesCommandes.AddCommandeToEmployeur(14, commande);
        //ServicesFacture servicesFacture=new ServicesFacture();
        employeur.setAdresse("adresse nouvelle");
        
        System.out.println(services.getListEmployeDunEmployeur(14));
        
        //servicesFacture.
        
        //ServicesTransactions servicesTransactions=new ServicesTransactions();
        //List<Transactions>lists=servicesTransactions.ConsulterTransactionsPeriode("2014-03-01","2014-03-15");
        //System.out.println("/*/*/*/*/*/*/Liste Transactions:"+lists.get(0).toString());
        TransactionsDAO transactionsDAO=new TransactionsDAO();
        List<Transactions>lists=null;//transactionsDAO.ConsulterTransactionsPeriode("2014-03-1","2014-03-12");
        //for(Transactions t:lists)
        {
            //System.out.println("Transaction Liste:"+t);
        }
        GenererPDF genererPDF=new GenererPDF();
        //genererPDF.GenererPDFTransactionsCommercant(145, "2014-03-01","2014-03-30");
        
        HibernateUtil.sessionFactory.close();
        System.exit(0);
    }
}
