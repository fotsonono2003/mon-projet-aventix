package Services;

import dao.CarteDAO;
import dao.CommercantDAO;
import dao.EmployeDAO;
import dao.LecteurCarteDAO;
import modele.Carte;
import modele.Commercant;
import modele.Employe;
import modele.Lecteurcarte;
import org.hibernate.Session;
import util.HibernateUtil;

public class ServicesCarte 
{
    
    public void AddCarteToEmploye(int numEmploye, Carte carte) 
    {
        if (carte != null) 
        {
            EmployeDAO employeDAO = new EmployeDAO();
            CarteDAO carteDAO=new CarteDAO();
            if (employeDAO.Exist(numEmploye))
            {
                Employe employe=employeDAO.getEmployeByNumero(numEmploye);
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                
                carte.setEmploye(employe);
                carteDAO.AddCarteToEmploye(numEmploye, carte);
                
                session.getTransaction().commit();
            }
        }
    }

    public void SupprimerCarteToCarte(int numEmploye, int numcarte) 
    {
        EmployeDAO employeDAO=new EmployeDAO();
        CarteDAO carteDAO=new CarteDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employe employe=employeDAO.getEmployeByNumero(numEmploye);
        Carte carte=carteDAO.getCarteByNumero(numcarte);
        
        if (employe!=null && carte!=null)
        {
            carte.setEmploye(null);
            carteDAO.SupprimerCarte(numcarte);
            employeDAO.MiseAJour(employe);
            session.getTransaction().commit();
        }
    }
    
    public void RechargerCarte(int numcarte,float montant)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
    	CarteDAO carteDAO=new CarteDAO();
    	carteDAO.RechargerCarte(numcarte, montant);
    	session.getTransaction().commit();
    }
    
    public boolean bloquerCarte(int numero)
    {
    	try 
    	{
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
        	CarteDAO carteDAO=new CarteDAO();
        	carteDAO.bloquerCarte(numero);
        	session.getTransaction().commit();
        	return true;
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
    }
    
    public boolean ActiverCarte(int numero)
    {
    	try 
    	{
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
        	CarteDAO carteDAO=new CarteDAO();
        	carteDAO.ActiverCarte(numero);
        	session.getTransaction().commit();
        	return true;
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
    }
    
}
