package controleurs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Employe;

/**
 * Servlet implementation class GenererFichier
 */
@WebServlet("/GenererFichier")
public class GenererFichier extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	//private static final String cheminEmployeur=System.getProperty("user.home" )+File.separator+"employeur"+File.separator;
	//private static final  String cheminEmploye=System.getProperty("user.home" )+File.separator+"employe"+File.separator;
       
	private static final String cheminEmployeur="employeur";
	private static final  String cheminEmploye="employe";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenererFichier() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}

	public void GenererFichierEmployeCSV(int numemployeur,List<Employe>listEmploye)
	{
		File file=new File(cheminEmployeur+numemployeur+File.separator);
		if(!file.exists())
		{
			file.mkdirs();
		}
		
		file=new File(cheminEmployeur+numemployeur+File.separator+"employes.csv");
		try 
		{			
			if(file.exists())
			{
				file.delete();
			}
			System.out.println("-----------chemin fichier employe.csv:"+file.getAbsolutePath());
			PrintStream printStream = new PrintStream(new FileOutputStream(file,true)); 
			//FileWriter fileWriter = new FileWriter(fichier,true);
			printStream.println("Numero;Nom;Prenom;Email");
			for(Employe e:listEmploye)
			{ 
				String ligne=e.getNumemploye()+";"+e.getNom()+";"+e.getPrenom()+";"+e.getEmail()+"";
				printStream.println(ligne);
				//fileWriter.append(ligne); 
			}
			//fileWriter.close();
			printStream.flush();
			printStream.close();
			printStream=null;
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void GenererFichierEmployeCSV(String chemin,List<Employe>listEmploye)
	{
		File file=new File(chemin);
		System.out.println("-----------chemin fichier employe.csv:"+file.getAbsolutePath());
		try
		{
			PrintStream printStream = new PrintStream(new FileOutputStream(file,true));
			printStream.println("Numero;Nom;Prenom;Email");
			for(Employe e:listEmploye)
			{
				String ligne=e.getNumemploye()+";"+e.getNom()+";"+e.getPrenom()+";"+e.getEmail()+"";
				printStream.println(ligne);
			}
			printStream.flush();
			printStream.close();
			printStream=null;
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	
}
