package Services;

import dao.EmployeDAO;
import dao.EmployeurDAO;

import java.util.List;

import modele.Employe;
import modele.Employeur;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import util.HibernateUtil;

public class ServicesEmploie  implements ServicesEmploye,ServicesEmployeur
{

    @Override
    public void AddEmploye(Employe employe) 
    {
        if (employe!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            EmployeDAO employeDAO = new EmployeDAO();
            employeDAO.AddEmploye(employe);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean SupprimerEmploye(int numeroEmploye) 
    {
        try
        {
            EmployeDAO employeDAO = new EmployeDAO();
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            if (employeDAO.supprimerEmploye(numeroEmploye)) {
                session.getTransaction().commit();
                return true;
            }
            return false;
        }
        catch (HibernateException ex) 
        {
            System.out.println("Erreur lors de la suppression de l'employe:"+ex);
        }
        return false;
    }

    @Override
    public boolean MiseAjourEmploye(Employe employe)
    {
        boolean b=false;
        try
        {
            if (employe != null) 
            {
                EmployeDAO employeDAO = new EmployeDAO();
                if (employeDAO.Exist(employe)) 
                {
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    session.beginTransaction();
                    b = employeDAO.MiseAJour(employe);
                    session.getTransaction().commit();
                }
            }
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise Ã  jour de l'employe:"+ex);
        }
        return b;
    }
    
    @Override
    public Employe getEmployeByNumero(int numero) 
    {
        EmployeDAO employeDAO=new EmployeDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employe employe=employeDAO.getEmployeByNumero(numero);
        //session.getTransaction().commit();
        return employe;
    }

    @Override
    public void AddEmployeur(Employeur employeur) 
    {
        EmployeurDAO employeurDAO = new EmployeurDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        employeurDAO.AddEmployeur(employeur);
        session.getTransaction().commit();
    }

    @Override
    public void AddEmployeToEmployeur(Employeur employeur, Employe employe) 
    {
        if (employeur != null && employe != null) 
        {
            EmployeurDAO employeurDAO = new EmployeurDAO();
            
            if (employeurDAO.Exist(employeur))
            {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                //Employeur employeur=(Employeur)session.get(Employeur.class,employeur.getNumEmployeur());
                employe.setEmployeur(employeur);
                employeurDAO.AddEmploye(employeur.getNumemployeur(), employe);
                
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public void AddEmployeToEmployeur(int numEmployeur, Employe employe) 
    {
        if (employe != null) 
        {
            EmployeurDAO employeurDAO = new EmployeurDAO();
            
            if (employeurDAO.Exist(numEmployeur))
            {
                Employeur employeur=employeurDAO.getEmployeurByNumero(numEmployeur);
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                
                employe.setEmployeur(employeur);
                employeurDAO.AddEmploye(employeur.getNumemployeur(), employe);
                
                session.getTransaction().commit();
            }
        }
    }

    @Override
    public boolean SupprimerEmployeur(int numeroEmployeur) 
    {
        try 
        {
            EmployeurDAO employeurDAO=new EmployeurDAO();
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Employeur employeur = employeurDAO.getEmployeurByNumero(numeroEmployeur);
            if (employeur != null) 
            {
                employeurDAO.SuprimerEmployeur(numeroEmployeur);
                session.getTransaction().commit();
                return true;
            }
        }
        catch (Exception ex) 
        {
            System.out.println("Erreur survenue lors de la suppression:"+ex);
        }
        return false;
    }

    @Override
    public boolean MiseAjourEmployeur(Employeur employeur) 
    {
        if (employeur != null) 
        {
            EmployeurDAO employeurDAO = new EmployeurDAO();
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            boolean b = employeurDAO.MiseAjourEmployeur(employeur);
            session.getTransaction().commit();
            return b;
        }
        return false;
    }

    @Override
    public Employeur getEmployeurByNumero(int numero) 
    {
        EmployeurDAO employeurDAO = new EmployeurDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try
        {
            Employeur employeur = employeurDAO.getEmployeurByNumero(numero);
            //session.getTransaction().commit();
            return employeur;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la recuperation de l'employeur:"+ex);
        }
        return null;
    }

    @Override
    public void SupprimerEmployeToEmployeur(int numeroEmployeur, int numeroEmploye) 
    {
        EmployeurDAO employeurDAO=new EmployeurDAO();
        EmployeDAO employeDAO=new EmployeDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employeur employeur=employeurDAO.getEmployeurByNumero(numeroEmployeur);
        Employe employe=employeDAO.getEmployeByNumero(numeroEmploye);
        
        if (employeur.getListEmployes().contains(employe))
        {
            employe.setEmployeur(null);
            employeur.getListEmployes().remove(employe);
            employeDAO.supprimerEmploye(employe.getNumemploye());
            employeurDAO.MiseAjourEmployeur(employeur);
            //session.delete(employe);
            //session.update(employeur);
            session.getTransaction().commit();
			//System.out.println("****************suppression effectuée**************::::");				
            
        }
    }

    @Override
    public boolean SupprimerEmployeur(Employeur employeur) 
    {
        try 
        {
            if (employeur!=null) 
            {
                EmployeurDAO employeurDAO = new EmployeurDAO();
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                Employeur emp = employeurDAO.getEmployeurByNumero(employeur.getNumemployeur());
                if (emp != null) {
                    employeurDAO.SuprimerEmployeur(employeur.getNumemployeur());
                    session.getTransaction().commit();
                    return true;
                }
            }
        }
        catch (Exception ex) 
        {
            System.out.println("Erreur survenue lors de la suppression:"+ex);
        }
        return false;
    }

    public  List<Employeur> findAllEmployeurs()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        EmployeurDAO employeurDAO=new EmployeurDAO();
        List<Employeur>liste=employeurDAO.findAllEmployeurs();
        //session.getTransaction().commit();
        return liste;
    }
    
    public  List<Employe> findAllEmployes()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        EmployeDAO employeDAO=new EmployeDAO();
        List<Employe>liste=employeDAO.findAllEmployes();
        System.out.println("/*/*/*/*/*/*/Liste:"+liste);
        //session.getTransaction().commit();
        return liste;
    }
    
    public Employeur  VerifierLoginAndPasswordEmployeur(String login,String password)
    {
        EmployeurDAO employeurDAO=new EmployeurDAO();        
        return employeurDAO.VerifierLoginAndPassword(login, password);
    }
    
    public Employeur  VerifierEmailAndPasswordEmployeur(String email,String password)
    {
        EmployeurDAO employeurDAO=new EmployeurDAO();        
        return employeurDAO.VerifierEmailAndPassword(email, password);
    }
    
    public Employe VerifierLoginAndPasswordEmploye(String login, String password) 
    {
        EmployeDAO employeDAO=new EmployeDAO();
        return employeDAO.VerifierLoginAndPassword(login, password);
    }
    
    public Employe VerifierEmailAndPasswordEmploye(String email, String password) 
    {
        EmployeDAO employeDAO=new EmployeDAO();
        return employeDAO.VerifierEmailAndPassword(email, password);
    }
    
    public Employeur getEmployeurByLogin(String login)
    {
        EmployeurDAO employeurDAO=new EmployeurDAO();
        return employeurDAO.getEmployeurByLogin(login);
    }
    
    public Employeur getEmployeurByEmail(String email)
    {
        EmployeurDAO employeurDAO=new EmployeurDAO();
        return employeurDAO.getEmployeurByEmail(email);
    }
    
    public Employe getEmployeByEmail(String email)
    {
        EmployeDAO employeDAO=new EmployeDAO();
        return employeDAO.getEmployeByEmail(email);
    }
    
    public Employe getEmployeByLogin(String login)
    {
        EmployeDAO employeDAO=new EmployeDAO();
        return employeDAO.getEmployeByLogin(login);
    }
    
    public List<Employe> getListEmployeDunEmployeur(int numemployeur)
    {
    	EmployeurDAO employeurDAO=new EmployeurDAO();
    	return employeurDAO.getListEmploye(numemployeur);
    }
}
