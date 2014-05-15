package dao;

import java.util.List;

import modele.Commande;
import modele.Commercant;
import modele.Employe;
import modele.Employeur;
import modele.Lecteurcarte;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import util.HibernateUtil;

public class CommercantDAO 
{
    public void AddCommercant(Commercant commercant)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(commercant);        
    }
    
    public boolean  SupprimerCommercant(int numcommercant)
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Commercant commercant=(Commercant)session.get(Commercant.class,numcommercant); 
            if(commercant!=null)
            {
                session.delete(commercant);
            }
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la suppression du commerçant:"+ex);
        }
        return false;
    }

    public boolean  MiseAJourCommercant(Commercant commercant)
    {
        try
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(commercant);
            return true;
        } catch (HibernateException ex)
        {
            System.out.println("Erreur lors de la mise � jour du commerçant: "+ex);
        }
        return false;
    }

    public boolean Exist(Commercant commercant)
    {
        if(commercant==null)
        {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commercant com=(Commercant)session.get(Commande.class,commercant.getNumcommercant());
        session.getTransaction().commit();
        return com!=null;
    }
    
    public boolean Exist(int numcommercant)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commercant commercant=(Commercant)session.get(Commande.class,numcommercant);
        return commercant!=null;
    }

    public Commande getCommandeByNumero(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commande commande = (Commande) session.get(Commande.class, numero);
        return commande;
    }

    public List<Commercant> findAllCommercants()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select com from Commercant as com");
        List<Commercant>liste=query.list();
        return liste;
    }
    
    public Commercant getCommercantByNumero(int numero)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commercant commercant = (Commercant) session.get(Commercant.class, numero);
        return commercant;
    }
    
    public Commercant VerifierLoginAndPassword(String login,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Commercant.class);
        Criterion crtLogin = Restrictions.eq("login", login);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtLogin, crtPass);
        crit.add(AndExp);
        Commercant commercant=(Commercant)crit.uniqueResult();
        return commercant;
    }
    
    public Commercant VerifierEmailAndPassword(String email,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Commercant.class);
        Criterion crtEmail = Restrictions.eq("email", email);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtEmail, crtPass);
        crit.add(AndExp);
        System.out.println("Unique result:"+(Commercant)crit.uniqueResult());
        Commercant commercant=(Commercant)crit.uniqueResult();
        return commercant;
    }
    
    public void AddLecteurCarteToCommercant(int numcommercant,Lecteurcarte lecteurcarte)
    {
    }
    
    public Commercant getCommercantByLogin(String login)
    {
        if(login!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Commercant.class);
            //Criterion crtLogin = Restrictions.eq("login", login);
            SimpleExpression EqExp = Restrictions.eq("login", login);
            crit.add(EqExp);
            return (Commercant)crit.uniqueResult();
        }
        return null;
    }
    
    public Commercant getCommercantByEmail(String email)
    {
        if(email!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Commercant.class);
            Criterion crtEmail = Restrictions.eq("email", email);
            //SimpleExpression EqExp = Restrictions.eq("email", email);
            crit.add(crtEmail);
            System.out.println("Unique result Employeur by email:" + (Employeur) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Commercant)crit.uniqueResult();
        }
        return null;
    }
}
