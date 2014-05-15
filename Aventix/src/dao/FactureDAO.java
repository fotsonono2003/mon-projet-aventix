package dao;

import java.util.List;
import modele.Facture;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import util.HibernateUtil;

public class FactureDAO 
{
    public void AddFacture(Facture facture)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(facture);
    }
    
    public Facture getFactureByNumero(int numero) 
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Facture facture = (Facture) session.get(Facture.class, numero);

        return facture;
    }

    public boolean SupprimerFacture(int numero) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Facture facture = getFactureByNumero(numero);
            if(facture!=null)
                session.delete(facture);
            return true;
        } catch (HibernateException ex) 
        {
            System.out.println("Suppression facture echouée:" + ex);
        }
        return false;
    }

    public List<Facture> findAllFactures()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select fa from Facture as fa");
        List<Facture>liste=query.list();
        return liste;
    }
    
    public boolean Exist(int numfacture)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Facture facture=(Facture)session.get(Facture.class,numfacture);
        return facture!=null;
    }
    
    public boolean MiseAjourFacture(Facture facture) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(facture);
            //session.getTransaction().commit();
            return true;
        } 
        catch (HibernateException ex) 
        {
            ex.printStackTrace();
            System.out.println("Erreur lors de la mise à jour de la transaction:"+ex);
        }
        return false;
    }
    
}
