package dao;

import java.util.List;
import modele.Administrateur;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

public class AdministrateurDAO 
{
    public void AddAdministrateur(Administrateur administrateur)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(administrateur);
    }
    
    public Administrateur getAdministrateurByNumero(int numero) 
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Administrateur administrateur = (Administrateur) session.get(Administrateur.class, numero);

        return administrateur;
    }

    public boolean SupprimerAdministrateur(int numero) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Administrateur administrateur = getAdministrateurByNumero(numero);
            if(administrateur!=null)
                session.delete(administrateur);
            return true;
        } catch (HibernateException ex) 
        {
            System.out.println("Suppression Administrateur echouée:" + ex);
        }
        return false;
    }

    public List<Administrateur> findAllAdministrateurs()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select ad from Administrateur as ad");
        List<Administrateur>liste=query.list();
        return liste;
    }
    
    public boolean Exist(int numadministrateur)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Administrateur administrateur=(Administrateur)session.get(Administrateur.class,numadministrateur);
        return administrateur!=null;
    }
    
    public boolean MiseAjourAdministrateur(Administrateur administrateur) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(administrateur);
            //session.getTransaction().commit();
            return true;
        } 
        catch (HibernateException ex) 
        {
            ex.printStackTrace();
            System.out.println("Erreur lors de la mise à jour de l'administrateur:"+ex);
        }
        return false;
    }

}
