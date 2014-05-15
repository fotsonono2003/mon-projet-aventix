
package dao;

import java.util.List;
import modele.Lecteurcarte;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

public class LecteurCarteDAO 
{
    public void AddLecteurCarte(Lecteurcarte lecteurcarte)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(lecteurcarte);
    }
    
    public Lecteurcarte getLecteurCarteByNumero(int numero) 
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Lecteurcarte lecteurcarte = (Lecteurcarte) session.get(Lecteurcarte.class, numero);//(Employe)session.load(Employe.class, numero);

        return lecteurcarte;
    }

    public boolean MiseAjourLecteurCarte(Lecteurcarte lecteurcarte) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(lecteurcarte);
            return true;
        } 
        catch (HibernateException ex) 
        {
            ex.printStackTrace();
            System.out.println("Erreur lors de la mise à jour du lecteur de carte :"+ex);
        }
        return false;
    }

    public boolean Exist(int numLecteurCarte)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Lecteurcarte lecteurcarte=(Lecteurcarte)session.get(Lecteurcarte.class,numLecteurCarte);
        return lecteurcarte!=null;
    }
    
    public boolean SupprimerLecteurCarte(int numero) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Lecteurcarte lecteurcarte = getLecteurCarteByNumero(numero);
            if(lecteurcarte!=null)
                session.delete(lecteurcarte);
            return true;

        } catch (HibernateException ex) 
        {
            System.out.println("Suppression du lecteur de carte ehoué:" + ex);
        }
        return false;
    }

    public List<Lecteurcarte> findAlllecLecteurcartes()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select lec from Lecteurcarte as lec");
        List<Lecteurcarte>liste=query.list();
        return liste;
    }

}
