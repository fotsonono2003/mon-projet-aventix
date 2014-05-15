package Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion 
{
    private Connection c=null;

	public Connexion()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/aventix", "root", "");
		}
		catch (ClassNotFoundException ex) 
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		catch (Exception ex) 
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	public Connection getC() {
		return c;
	}

	public void setC(Connection c) {
		this.c = c;
	}
	
}
