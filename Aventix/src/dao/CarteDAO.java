package dao;

import modele.Carte;
import modele.Employe;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

public class CarteDAO 
{
    
    public void AddCarte(Carte carte)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(carte);        
    }
    
    public boolean  SupprimerCarte(int numcarte)
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Carte carte=(Carte)session.get(Carte.class,numcarte); 
            if(carte!=null)
            {
                session.delete(carte);
            }
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la suppression de la carte:"+ex);
        }
        return false;
    }
    
    public boolean  MiseAJourCarte(Carte carte)
    {
        try
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(carte);
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise à jour de la carte: "+ex);
        }
        return false;
    }
    
    public boolean Exist(Carte carte)
    {
        if(carte==null)
        {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Carte car=(Carte)session.get(Carte.class,carte.getNumerocarte());
        session.getTransaction().commit();
        return car!=null;
    }
    
    public boolean Exist(int numcarte)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Carte carte=(Carte)session.get(Carte.class,numcarte);
        session.getTransaction().commit();
        return carte!=null;
    }
    
    public Carte getCarteByNumero(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Carte carte = (Carte) session.get(Carte.class, numero);
        return carte;
    }
    
    public void AddCarteToEmploye(int numeroEmploye, Carte carte)
    {
        EmployeDAO employeDAO=new EmployeDAO();
        Employe employe = employeDAO.getEmployeByNumero(numeroEmploye);
        if (employe != null && carte != null) 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            carte.setEmploye(employe);
            employe.setCarte(carte);
            this.AddCarte(carte);
            session.update(employe);
        }
    }
    
    public void RechargerCarte(int numcarte,float montant)
    {
    	Carte carte=this.getCarteByNumero(numcarte);
    	if(carte!=null)
    	{
    		float solde=carte.getSolde();
    		solde=solde+montant;
    		carte.setSolde(solde);
    		this.MiseAJourCarte(carte);
    	}
    	
    }
    public void ActiverCarte(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Carte carte=(Carte)session.load(Carte.class, numero);
        carte.setActif(true);
    }
    
    public void bloquerCarte(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Carte carte=(Carte)session.load(Carte.class, numero);
        carte.setActif(false);
    }
    
}
