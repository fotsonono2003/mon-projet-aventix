package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil 
{

	public static final SessionFactory sessionFactory;
	
	static 
	{
		try 
		{
			
			sessionFactory=new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) 
		{
			System.err.println("Creation du session Factory echoué: "+ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
	
}
