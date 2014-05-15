package Services;

import dao.AdministrateurDAO;
import dao.EmployeDAO;
import modele.Administrateur;
import modele.Employe;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

public class ServicesAdministrateur 
{
    public void AddAministrateur(Administrateur administrateur) 
    {
        if (administrateur!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            AdministrateurDAO administrateurDAO = new AdministrateurDAO();
            administrateurDAO.AddAdministrateur(administrateur);
            session.getTransaction().commit();
        }
    }

    public boolean SupprimerAdministrateur(int numeroAdmin) 
    {
        try
        {
            AdministrateurDAO administrateurDAO = new AdministrateurDAO();
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            if (administrateurDAO.SupprimerAdministrateur(numeroAdmin)) {
                session.getTransaction().commit();
                return true;
            }
            return false;
        }
        catch (HibernateException ex) 
        {
            System.out.println("Erreur lors de la suppression de l'administrateur:"+ex);
        }
        return false;
    }

    public boolean MiseAjourAdministrateur(Administrateur administrateur)
    {
        boolean b=false;
        try
        {
            if (administrateur != null) 
            {
                AdministrateurDAO administrateurDAO = new AdministrateurDAO();
                if (administrateurDAO.Exist(administrateur.getNumAdmin())) 
                {
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    session.beginTransaction();
                    b = administrateurDAO.MiseAjourAdministrateur(administrateur);
                    session.getTransaction().commit();
                }
            }
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise à jour de l'administrateur:"+ex);
        }
        return b;
    }
    

}
