
package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import modele.Commande;
import modele.Employe;
import modele.Employeur;
import modele.Facture;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import util.HibernateUtil;

public class EmployeurDAO 
{
    public void AddEmployeur(Employeur employeur)
    {
        if (employeur.getListEmployes() != null) 
        {
            EmployeDAO eDao = new EmployeDAO();
            Iterator<Employe> itr = employeur.getListEmployes().iterator();
            while (itr.hasNext()) 
            {
                Employe e = (Employe) itr.next();
                e.setEmployeur(employeur);
                eDao.AddEmploye(e);
            }
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(employeur);
        //session.getTransaction().commit();
    }
    
    public boolean MiseAjourEmployeur(Employeur employeur) 
    {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(employeur);
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise à jour: "+ex);
        }
        return false;
    }

    public boolean Exist(Employeur employeur)
    {
        if(employeur==null)
        {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employeur e=(Employeur)session.get(Employeur.class,employeur.getNumemployeur());
        session.getTransaction().commit();
        return e!=null;
    }
    
    public boolean Exist(int numEmployeur)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employeur e=(Employeur)session.get(Employeur.class,numEmployeur);
        //session.getTransaction().commit();
        return e!=null;
    }
    
    public void AddEmploye(int numero, Employe employe) 
    {
        EmployeDAO employeDAO=new EmployeDAO();
        Employeur employeur = getEmployeurByNumero(numero);
        if (employeur != null && employe != null) 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            employeDAO.AddEmploye(employe);
            employe.setEmployeur(employeur);
            employeur.getListEmployes().add(employe);
            session.update(employeur);
        }
    }
    
    public Employeur getEmployeurByNumero(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employeur employeur = (Employeur) session.get(Employeur.class, numero);
        return employeur;
    }
    
    public boolean SuprimerEmployeur(int numeroEmployeur) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Employeur employeur = (Employeur) session.get(Employeur.class, numeroEmployeur);
            if (employeur != null) 
            {
		if(employeur.getListEmployes()!=null)
		{
			EmployeDAO eDao=new EmployeDAO();
			Iterator<Employe> itr=employeur.getListEmployes().iterator(); 
			while (itr.hasNext()) 
			{
				Employe e = (Employe) itr.next();
				e.setEmployeur(employeur);
				eDao.AddEmploye(e);
			}
		}
                session.delete(employeur);
                return true;
            }
        } catch (HibernateException ex)
        {
            System.out.println("Erreur survenue lors de la suppression:"+ex);
        }
        return false;
    }
    
    public void AddCommande(int numeroEmployeur, Commande commande)
    {
        CommandeDAO commandeDAO=new CommandeDAO();
        Employeur employeur = getEmployeurByNumero(numeroEmployeur);
        if (employeur != null && commande != null) 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            commandeDAO.AddCommande(commande);
            commande.setEmployeur(employeur);
            employeur.getCommandes().add(commande);
            session.update(employeur);
        }
    }
    
    public List<Employeur> findAllEmployeurs()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select em from Employeur as em");
        List<Employeur>liste=query.list();
        return liste;
    }
    
    public boolean VerifierLoginAndPasswordBool(String login,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Employe.class);
        Criterion crtLogin = Restrictions.eq("login", login);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtLogin, crtPass);
        crit.add(AndExp);
        List<Employeur> results = crit.list();
        return results.size()==1;
    }
    
    public Employeur VerifierLoginAndPassword(String login,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Employeur.class);
        Criterion crtLogin = Restrictions.eq("login", login);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtLogin, crtPass);
        crit.add(AndExp);
        Employeur employeur = (Employeur)crit.uniqueResult();
        return employeur;
    }
    
    public Employeur VerifierEmailAndPassword(String email,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Employeur.class);
        Criterion crtEmail = Restrictions.eq("email", email);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtEmail, crtPass);
        crit.add(AndExp);
        Employeur employeur = (Employeur)crit.uniqueResult();
        return employeur;
    }
    
    public void AddFacture(int numemployeur,Facture facture)
    {
        FactureDAO factureDAO=new FactureDAO();
        Employeur employeur = getEmployeurByNumero(numemployeur);
        if (employeur != null && facture != null) 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            factureDAO.AddFacture(facture);
            session.update(employeur);
        }
    }
    
    public Employeur getEmployeurByLogin(String login)
    {
        if(login!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employeur.class);
            //Criterion crtLogin = Restrictions.eq("login", login);
            SimpleExpression EqExp = Restrictions.eq("login", login);
            crit.add(EqExp);
            System.out.println("Unique result:" + (Employeur) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Employeur)crit.uniqueResult();
        }
        return null;
    }
    
    public Employeur getEmployeurByEmail(String email)
    {
        if(email!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employeur.class);
            Criterion crtEmail = Restrictions.eq("email", email);
            //SimpleExpression EqExp = Restrictions.eq("email", email);
            crit.add(crtEmail);
            System.out.println("Unique result Employeur by email:" + (Employeur) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Employeur)crit.uniqueResult();
        }
        return null;
    }
    
    public List<Employe> getListEmploye2(int numemployeur)
    {
    	EmployeurDAO employeurDAO=new EmployeurDAO();
    	Employeur employeur=employeurDAO.getEmployeurByNumero(numemployeur);
    	if(employeur!=null)
    	{
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employe.class);
            Criterion crtNum = Restrictions.eq("employeur", employeur);
            //SimpleExpression EqExp = Restrictions.eq("email", email);
            crit.add(crtNum);
            List<Employe>lisEmployes=crit.list();
            return lisEmployes;
    	}
    	return null;
    }
    
    public List<Employe> getListEmploye(int numemployeur)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employeur employeur=this.getEmployeurByNumero(numemployeur);
        
       /* Criteria crit = session.createCriteria(Employeur.class);
        Criterion crtNum = Restrictions.eq("numeployeur", numemployeur);
        crit.add(crtNum);
        List<Employe> ListEmploye = crit.list();
        */
        
        List<Employe>ListEmploye=new ArrayList<Employe>();
        
        Set<Employe>SetEmployeur=employeur.getListEmployes();
        Iterator<Employe > itr=SetEmployeur.iterator();
        while (itr.hasNext()) 
        {
        	Employe e=(Employe)itr.next();
        	ListEmploye.add(e);
		}
        return ListEmploye;
    }
}
