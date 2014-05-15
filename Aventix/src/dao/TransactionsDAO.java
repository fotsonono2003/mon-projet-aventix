
package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import modele.Carte;
import modele.Commercant;
import modele.Employe;
import modele.Lecteurcarte;
import modele.Transactions;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import util.HibernateUtil;

public class TransactionsDAO 
{
    public void AddTransaction(Transactions transactions)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        session.save(transactions);
    }
    
    public Transactions getTransactionsByNumero(int numero) 
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Transactions transactions = (Transactions) session.get(Transactions.class, numero);//(Employe)session.load(Employe.class, numero);

        return transactions;
    }

    public boolean SupprimerTranctions(int numero) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Transactions transactions = getTransactionsByNumero(numero);
            if(transactions!=null)
                session.delete(transactions);
            return true;
        } catch (HibernateException ex) 
        {
            System.out.println("Suppressiontransaction echouée:" + ex);
        }
        return false;
    }

    public List<Transactions> findAllTransactionses()
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        org.hibernate.Query query=session.createQuery("select tr from Transactions as tr");
        List<Transactions>liste=query.list();
        return liste;
    }
    
    public boolean Exist(int numtr)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Transactions transactions=(Transactions)session.get(Transactions.class,numtr);
        return transactions!=null;
    }
    
    public boolean MiseAjourTranctions(Transactions transactions) 
    {
        try 
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            session.update(transactions);
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
    
    public List<Transactions> ConsulterTransactionsPeriode(Date dateDebut,Date dateFin)
    {
        if(dateDebut!=null && dateFin!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            
            Criteria crit = session.createCriteria(Transactions.class);
            SimpleExpression geExp = Restrictions.ge("dateheuretransaction", dateDebut);
            SimpleExpression leExp = Restrictions.le("dateheuretransaction", dateFin);

            SimpleExpression eqExpDebut = Restrictions.eq("dateheuretransaction", dateDebut);
            SimpleExpression eqExpFin = Restrictions.eq("dateheuretransaction", dateFin);
            
            LogicalExpression andLg =Restrictions.and(geExp, leExp);
            
            crit.add(geExp);
            crit.add(leExp);
            crit.add(andLg);
            crit.add(eqExpDebut);
            crit.add(eqExpFin);
            List<Transactions> ListTransactions=(List<Transactions>)crit.list();
            //List<Transactions> ListTransactions=query.list();
            return ListTransactions;
        }
    	
    	return null;
    }
    
    public List<Transactions> ConsulterTransactionsPeriode(String datedebut,String datefin)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query=session.createSQLQuery("select * from transactions  where date(dateheuretransaction)>='"+datedebut+"'  and date(dateheuretransaction)<='"+datefin+"'  order by date(dateheuretransaction) asc   ").addEntity(Transactions.class);
        List<Transactions> list=(List<Transactions>)query.list();
        return list;
    }

    public List<Transactions> ConsulterAllTransactionsCartePeriode(int numerocarte)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Query query=session.createSQLQuery("select * from transactions  where numerocarte="+numerocarte+" order by date(dateheuretransaction) asc ").addEntity(Transactions.class);
        List<Transactions> list=(List<Transactions>)query.list();
        return list;
    }
    
    public List<Transactions> ConsulterTransactionsCarte(int numerocarte,String datedebut,String datefin)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query=session.createSQLQuery("select * from transactions  where date(dateheuretransaction)>='"+datedebut+"'  and date(dateheuretransaction)<='"+datefin+"' and numerocarte="+numerocarte+"   order by date(dateheuretransaction) asc   ").addEntity(Transactions.class);
        List<Transactions> list=(List<Transactions>)query.list();
        return list;
    }
    
    public List<Transactions> ConsulterTransactionsCommercant(String strLecteur,String datedebut,String datefin)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query query=session.createSQLQuery("select * from transactions  where date(dateheuretransaction)>='"+datedebut+"'  and date(dateheuretransaction)<='"+datefin+"'  and traite=false and numLecteur in ("+strLecteur+") order by date(dateheuretransaction) asc   ").addEntity(Transactions.class);
        List<Transactions> list=(List<Transactions>)query.list();
        return list;
    }
    
    public List<Transactions> getTransactionsByCarte(int  numcarte)
    {
    	CarteDAO carteDAO=new CarteDAO();
    	Carte carte=carteDAO.getCarteByNumero(numcarte);
    			
        if(carte!=null)
        {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();

            Criteria crit = session.createCriteria(Transactions.class);
            SimpleExpression eqExp = Restrictions.eq("carte", carte);
            crit.add(eqExp);
            List<Transactions> ListTransactions=crit.list();
            return ListTransactions;
        }
    	
    	return null;
    }
    
    public List<Transactions> getTransactionsByCommercant(int numcommercant)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
    	CommercantDAO commercantDAO=new CommercantDAO();
    	Commercant commercant=commercantDAO.getCommercantByNumero(numcommercant);
    	if(commercant!=null)
    	{
        	List<Transactions>listTransactions=new ArrayList<Transactions>();
        	Set<Lecteurcarte> setLecteurcartes = commercant.getLecteurcartes();
        	
        	Iterator<Lecteurcarte>itrLecteurs=setLecteurcartes.iterator();
        	while (itrLecteurs.hasNext()) 
        	{
        		Lecteurcarte lecteurcarte=itrLecteurs.next();
        		Set<Transactions>setTransactions=lecteurcarte.getTransactionses();
        		
        		Iterator<Transactions>itrTransactions=setTransactions.iterator();
        		while (itrTransactions.hasNext()) 
        		{
        			Transactions t=itrTransactions.next();
        			if(!t.isTraite())
        			{
            			listTransactions.add(t);
        			}
    			}
    		}
        	return listTransactions;
    	}
        //if(commercant!=null)
        {

            /*Criteria crit = session.createCriteria(Transactions.class);
            SimpleExpression eqExp = Restrictions.eq("carte", carte);
            crit.add(eqExp);
            List<Transactions> ListTransactions=crit.list();
            return ListTransactions;
            */
        }
    	
    	return null;
    }
}
