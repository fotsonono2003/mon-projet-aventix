package Services;

import dao.CommercantDAO;
import dao.LecteurCarteDAO;
import modele.Commercant;
import modele.Lecteurcarte;

import org.hibernate.Session;

import util.HibernateUtil;

public class ServicesCommercant 
{
	public void AddCommercant(Commercant commercant)
	{
		if(commercant!=null)
		{
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            CommercantDAO commercantDAO = new CommercantDAO();
            commercantDAO.AddCommercant(commercant);
            session.getTransaction().commit();
		}
	}
	
	public Commercant VerifierLoginAndPassword(String login,String password)
	{
		CommercantDAO commercantDAO=new CommercantDAO();
		return commercantDAO.VerifierLoginAndPassword(login, password);
	}

	public Commercant VerifierEmailAndPassword(String email,String password)
	{
		CommercantDAO commercantDAO=new CommercantDAO();
		return commercantDAO.VerifierEmailAndPassword(email, password);
	}
	
	void AddLecteurCarte(int numcommercant, Lecteurcarte lecteurcarte)
    {
        if (lecteurcarte!= null) 
        {
            CommercantDAO commercantDAO = new CommercantDAO();
            Commercant commercant = commercantDAO.getCommercantByNumero(numcommercant);
            if (commercant!=null)
            {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();

                lecteurcarte.setCommercant(commercant);
                commercantDAO.AddLecteurCarteToCommercant(numcommercant, lecteurcarte);
                session.getTransaction().commit();
            }
        }        
    }
        
    public void SupprimerLecteurCarteToCommercant(int numcommercant, int numlecteur) 
    {
        CommercantDAO commercantDAO=new CommercantDAO();
        LecteurCarteDAO lecteurCarteDAO=new LecteurCarteDAO();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Commercant commercant=commercantDAO.getCommercantByNumero(numcommercant);
        Lecteurcarte lecteurcarte=lecteurCarteDAO.getLecteurCarteByNumero(numlecteur);
        
        if (commercant!=null && lecteurcarte!=null)
        {
            if (commercant.getLecteurcartes().contains(lecteurcarte)) {
                lecteurcarte.setCommercant(null);
                commercant.getLecteurcartes().remove(lecteurcarte);
                lecteurCarteDAO.SupprimerLecteurCarte(numlecteur);
                commercantDAO.MiseAJourCommercant(commercant);
                session.getTransaction().commit();
            }
        }
    }

    public Commercant VerifierLoginAndPasswordCommercant(String login, String password) 
    {
        CommercantDAO commercantDAO=new CommercantDAO();
        return commercantDAO.VerifierLoginAndPassword(login, password);
    }
    
    public Commercant VerifierEmailAndPasswordCommercant(String email, String password) 
    {
        CommercantDAO commercantDAO=new CommercantDAO();
        return commercantDAO.VerifierEmailAndPassword(email, password);
    }
    
    public Commercant getCommercantByEmail(String email)
    {
    	CommercantDAO  commercantDAO=new CommercantDAO();
    	return commercantDAO.getCommercantByLogin(email);
    }
    
    public Commercant getCommercantByLogin(String login)
    {
    	CommercantDAO  commercantDAO=new CommercantDAO();
    	return commercantDAO.getCommercantByLogin(login);
    }
    public boolean  MiseAJourCommercant(Commercant commercant)
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
    	CommercantDAO commercantDAO=new CommercantDAO();
    	boolean b= commercantDAO.MiseAJourCommercant(commercant);
    	session.getTransaction().commit();
    	return b;
    }
}
