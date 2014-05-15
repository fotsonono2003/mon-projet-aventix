package dao;

import java.util.List;

import modele.Carte;
import modele.Commande;
import modele.Employeur;
import modele.Facture;
import modele.Transactions;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import util.HibernateUtil;

public class CommandeDAO 
{
    public void AddCommande(Commande commande)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(commande);        
    }
    
    public boolean  SupprimerCommande(int numcommande)
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Commande commande=(Commande)session.get(Commande.class,numcommande); 
            if(commande!=null)
            {
                session.delete(commande);
            }
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la suppression de la commande:"+ex);
        }
        return false;
    }
    
    public boolean  MiseAJourCommande(Commande commande)
    {
        try
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(commande);
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise à jour de la commande: "+ex);
        }
        return false;
    }
    
    public boolean Exist(Commande commande)
    {
        if(commande==null)
        {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commande com=(Commande)session.get(Commande.class,commande.getNumcommande());
        session.getTransaction().commit();
        return com!=null;
    }
    
    public Commande getCommandeByNumero(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commande commande = (Commande) session.get(Commande.class, numero);
        return commande;
    }
    
    public void AddCommandeToEmployeur(int numeroEmployeur, Commande commande)
    {
        EmployeurDAO employeurDAO=new EmployeurDAO();
        Employeur employeur = employeurDAO.getEmployeurByNumero(numeroEmployeur);
        FactureDAO factureDAO=new FactureDAO();
        if (employeur != null && commande != null) 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            
            Facture facture=new Facture();
            facture.setCommande(commande);
            factureDAO.AddFacture(facture);
            
            this.AddCommande(commande);
            commande.setEmployeur(employeur);
            employeur.getCommandes().add(commande);
            
            session.update(employeur);
        }
    }
    
    public List<Commande> getCommandeByEmploye(int  numemployeur)
    {
    	EmployeurDAO employeurDAO =new EmployeurDAO();
    	Employeur employeur=employeurDAO.getEmployeurByNumero(numemployeur);
    			
        if(employeur!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Commande.class);
            SimpleExpression eqExp = Restrictions.eq("employeur", employeur);
            crit.add(eqExp);
            List<Commande> ListCommandes=crit.list();
            return ListCommandes;
        }
    	
    	return null;
    }
    
    public List<Commande> getCommandeByPeriode(int  numemployeur,String datedebut,String datefin)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query=session.createSQLQuery("select * from commande  where date(datecommande)>='"+datedebut+"'  and date(datecommande)<='"+datefin+"' and numemployeur="+numemployeur+"    ").addEntity(Commande.class);
        List<Commande> list=(List<Commande>)query.list();
        return list;
    }
    
    public List<Commande> getCommandeByEmployeur(Employeur employeur)
    {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Criteria crit = session.createCriteria(Commande.class);
		SimpleExpression eqExp = Restrictions.eq("employeur", employeur);
		crit.add(eqExp);
		List<Commande> ListCommandes = crit.list();
		return ListCommandes;
    }
    
    
    public List<Commande> findAllCommandes()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select com from Commande as com");
        List<Commande>liste=query.list();
        return liste;
    }
    
}
