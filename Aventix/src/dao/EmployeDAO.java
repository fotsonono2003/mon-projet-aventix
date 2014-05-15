package dao;

import java.util.List;

import modele.Employe;
import modele.Employeur;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import util.HibernateUtil;

public class EmployeDAO 
{
    
    public void AddEmploye(Employe employe)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(employe);
    }
    
    public Employe getEmployeByNumero(int numero) 
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Employe employe = (Employe) session.get(Employe.class, numero);
        return employe;
    }

    public boolean MiseAJour(Employe employe) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(employe);
            //session.getTransaction().commit();
            return true;
        } 
        catch (HibernateException ex) 
        {
            ex.printStackTrace();
            System.out.println("Erreur lors de la mise à jour:"+ex);
        }
        return false;
    }
    
    public boolean Exist(Employe employe)
    {
        if(employe==null)
        {
            return false;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employe e=(Employe)session.get(Employe.class,employe.getNumemploye());
        session.getTransaction().commit();
        return e!=null;
    }
    
    public boolean Exist(int numemploye)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Employe e=(Employe)session.get(Employe.class,numemploye);
        return e!=null;
    }
    
    public boolean supprimerEmploye(int numero) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

        	CarteDAO carteDAO=new CarteDAO();
        	
            Employe emp = getEmployeByNumero(numero);//(Employe)session.load(Employe.class, numero);
            if(emp!=null)
            {
                session.delete(emp);
                //session.getTransaction().commit();
            }
            return true;

        } catch (HibernateException ex) 
        {
            System.out.println("Suppression ehouée:" + ex);
        }
        return false;
    }

    public List<Employe> findAllEmployes()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select em from Employe as em");
        List<Employe>liste=query.list();
        return liste;
    }
    
    public boolean VerifierLoginAndPasswordBool(String login,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Employe.class);
        Criterion crtLogin = Restrictions.eq("login", login);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtLogin, crtPass);
        crit.add(AndExp);
        System.out.println("Unique result:"+(Employe)crit.uniqueResult());
        Employe employe=(Employe)crit.uniqueResult();
        List<Employe> results = crit.list();
        return results.size()==1;
    }

    public Employe VerifierLoginAndPassword(String login,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Employe.class);
        Criterion crtLogin = Restrictions.eq("login", login);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtLogin, crtPass);
        crit.add(AndExp);
        System.out.println("Unique result:"+(Employe)crit.uniqueResult());
        Employe employe=(Employe)crit.uniqueResult();
        return employe;
    }
    
    public Employe VerifierEmailAndPassword(String email,String password)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Criteria crit = session.createCriteria(Employe.class);
        Criterion crtEmail = Restrictions.eq("email", email);
        Criterion crtPass = Restrictions.eq("password", password);
        LogicalExpression AndExp = Restrictions.and(crtEmail, crtPass);
        crit.add(AndExp);
        System.out.println("Unique result Employe Email:"+(Employe)crit.uniqueResult());
        Employe employe=(Employe)crit.uniqueResult();
        return employe;
    }
    
    public Employe getEmployeByLogin2(String login)
    {
        if(login!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employe.class);
            SimpleExpression EqExp = Restrictions.eq("login", login);
            crit.add(EqExp);
            System.out.println("Unique result:" + (Employe) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Employe)crit.uniqueResult();
        }
        return null;
    }

    public Employe getEmployeByEmail(String email)
    {
        if(email!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employe.class);
            Criterion crtEmail = Restrictions.eq("email", email);
            //SimpleExpression EqExp = Restrictions.eq("email", email);
            crit.add(crtEmail);
            System.out.println("Unique result Employe by email:" + (Employe) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Employe)crit.uniqueResult();
        }
        return null;
    }
    
    public Employe getEmployeByLogin(String login)
    {
        if(login!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employe.class);
            //Criterion crtLogin = Restrictions.eq("login", login);
            SimpleExpression EqExp = Restrictions.eq("login", login);
            crit.add(EqExp);
            System.out.println("Unique result:" + (Employe) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Employe)crit.uniqueResult();
        }
        return null;
    }

    public Employe getEmployeByEmail11(String email)
    {
        if(email!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Employe.class);
            Criterion crtEmail = Restrictions.eq("email", email);
            //SimpleExpression EqExp = Restrictions.eq("email", email);
            crit.add(crtEmail);
            System.out.println("Unique result Employe by email:" + (Employe) crit.uniqueResult());
            //session.getTransaction().commit();
            return (Employe)crit.uniqueResult();
        }
        return null;
    }
    
}
