package Services;

import java.util.List;

import dao.CommandeDAO;
import dao.EmployeurDAO;
import dao.FactureDAO;
import modele.Commande;
import modele.Employeur;
import modele.Facture;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import util.HibernateUtil;

public class ServicesCommandes 
{
    public void AddCommande(Commande commande)
    {
        if(commande!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            CommandeDAO commandeDAO = new CommandeDAO();
            commandeDAO.AddCommande(commande);
            session.getTransaction().commit();
        }
    }
    
    public  boolean  SupprimerCommande(int numcommande)
    {
        try
        {
            CommandeDAO commandeDAO = new CommandeDAO();
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            if (commandeDAO.SupprimerCommande(numcommande))
            {
                session.getTransaction().commit();
                return true;
            }
            return false;
        }
        catch (HibernateException ex) 
        {
            System.out.println("Erreur lors de la suppression de la commande:"+ex);
        }
        return false;
    }
    
    public boolean  MiseAJourOmmande(Commande commande)
    {
        boolean b=false;
        try
        {
            if (commande != null) 
            {
                CommandeDAO commandeDAO = new CommandeDAO();
                if (commandeDAO.Exist(commande)) 
                {
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    session.beginTransaction();
                    b = commandeDAO.MiseAJourCommande(commande);
                    session.getTransaction().commit();
                }
            }
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise à jour de la commande:"+ex);
        }
        return b;        
    }
    
    public void AddCommandeToEmployeur(int numEmployeur,Commande commande)
    {
        if (commande != null) 
        {
            EmployeurDAO employeurDAO = new EmployeurDAO();
            FactureDAO factureDAO=new FactureDAO();
            Employeur employeur = employeurDAO.getEmployeurByNumero(numEmployeur);
            
            if (employeur!=null)
            {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                
                commande.setEmployeur(employeur);
                employeurDAO.AddCommande(employeur.getNumemployeur(), commande);
                
                Facture facture = new Facture();
                facture.setMontant(commande.getNbcarte()*5);
                facture.setCommande(commande);
                factureDAO.AddFacture(facture);
                
                session.getTransaction().commit();
            }
        }
    }

    public List<Commande> getCommandeByPeriode(int  numemployeur,String datedebut,String datefin)
    {
    	CommandeDAO commandeDAO=new CommandeDAO();
    	return commandeDAO.getCommandeByPeriode(numemployeur, datedebut, datefin);
    }
    
    public List<Commande> getCommandeByEmploye(Employeur employeur)
    {
    	CommandeDAO commandeDAO=new CommandeDAO();
    	return commandeDAO.getCommandeByEmployeur(employeur);
    }
    
    public List<Commande> findALlCommandes()
    {
    	CommandeDAO commandeDAO=new CommandeDAO();    	
        return commandeDAO.findAllCommandes();
    }

}
