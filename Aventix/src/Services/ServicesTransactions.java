package Services;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import util.HibernateUtil;
import dao.TransactionsDAO;
import modele.Transactions;

public class ServicesTransactions 
{
	
	public void AddTrasaction(Transactions transactions)
	{
		
	}
    
	public List<Transactions> ConsulterTransactionsCommercant(String strLecteur,String datedebut,String datefin)
    {
		TransactionsDAO transactionsDAO=new TransactionsDAO();
		return transactionsDAO.ConsulterTransactionsCommercant(strLecteur, datedebut, datefin);
    }

    public List<Transactions> getTransactionsByCarte(int numcarte)
	{
		TransactionsDAO transactionsDAO=new TransactionsDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        return transactionsDAO.getTransactionsByCarte(numcarte);
	}

    public List<Transactions> ConsulterTransactionsPeriode(String datedebut,String datefin)
    {
    	TransactionsDAO transactionsDAO=new TransactionsDAO();
    	return transactionsDAO.ConsulterTransactionsPeriode(datedebut, datefin);
    }
    
    public List<Transactions> ConsulterTransactionsCarte(int numerocarte,String datedebut,String datefin)
    {
    	TransactionsDAO transactionsDAO=new TransactionsDAO();
    	return transactionsDAO.ConsulterTransactionsCarte(numerocarte, datedebut, datefin);
    }
    
    public List<Transactions> getTransactionsByCommercant(int numcommercant)
    {
    	TransactionsDAO transactionsDAO=new TransactionsDAO();
    	return transactionsDAO.getTransactionsByCommercant(numcommercant);
    }
}
