package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Employe;
import modele.Employeur;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Services.EnvoiMail;
import Services.ServicesEmploie;

/**
 * Servlet implementation class CreerListeEmploye
 */
@WebServlet("/CreerListeEmploye")
public class CreerListeEmploye extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private final String vue="/WEB-INF/employeur/creerlisteemploye.jsp";
    private ServicesEmploie servicesEmploie=new ServicesEmploie();
      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreerListeEmploye() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		this.getServletContext().getRequestDispatcher(vue).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		TraiterRequete(request, response);
	} 
	
	private void TraiterRequete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Employeur employeur=(Employeur)request.getSession().getAttribute("employeur");
		if(employeur!=null)
		{
			String erreur=null;
			try 
			{ 
		        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		        for (FileItem item : items)
		        {
		            if (item.isFormField()) 
		            {
		                //String nomChamp = item.getFieldName();
		                //String valeurChamp = item.getString();
		            } else
		            {
		            	List<Employe>listEmploye=servicesEmploie.findAllEmployes();
		            	List<Employe>listToInsert=new ArrayList<Employe>();
		            	boolean correct=true;
		                //String nomChamp = item.getFieldName();
		                String nomFichier =item.getName();
		                InputStream stream = item.getInputStream();
		                if(nomFichier.endsWith(".csv"))
		                {
	    	                InputStreamReader reader=new InputStreamReader(stream);
	    	                BufferedReader br=new BufferedReader(reader);
	    	                int nbre=0;
	    	                String ligne=null;
	    	                while ((ligne=br.readLine())!=null && correct==true)
	    	                {
	    	                	nbre++;
	    	                	ligne=ligne.trim();
	    	                	if(!ligne.isEmpty() && nbre>1)
	    	                	{
	    	                		try 
	    	                		{
		    	                		String nom=ligne.split(";")[0].trim();
		    	                		String prenom=ligne.split(";")[1].trim();
		    	                		String email=ligne.split(";")[2].trim();
			    	                	System.out.println("Ligne:"+ligne+",nom:"+nom+",prenom:"+prenom+", email:"+email);
		    	                		if(nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || !email.contains("@") || !(email.lastIndexOf(".")>2))
		    	                		{
	    	                				correct=false;
	    	                				erreur="Ligne:"+nbre+", adresse email incorrecte et/ou champ incorrect ";
	    	                				break;
		    	                		}
		    	                		else
		    	                		{
			    	                		for(Employe e:listEmploye)
			    	                		{
			    	                			if(e.getEmail().equalsIgnoreCase(email))
			    	                			{
			    	                				correct=false;
			    	                				erreur="Ligne:"+nbre+", cette adresse email existe dejà ou est incorrecte";
			    	                				break;
			    	                			}
			    	                		}
			    	                		if(correct==true)
			    	                		{
			    	                			Employe employe=new Employe();
			    	                			employe.setNom(nom);
			    	                			employe.setPrenom(prenom);
			    	                			employe.setEmail(email);
			    	                			listToInsert.add(employe);
			    	                		}
		    	                		}
									} catch (Exception ex) 
									{
										erreur="Ligne:"+nbre+", verifiez que vous avez rempli tous les champs";
										//ex.printStackTrace();
										break;
									}
	    	                		
	    	                	}
	    	                }
	    	                ////insertion dans la BDD
	    	                if(correct==true)
	    	                {
	    	                	System.out.println("Liste a recuperer :"+listEmploye.size());
	    	                	System.out.println("Liste a inserer :"+listToInsert.size());
		    	                for(Employe e:listToInsert)
		    	                {
		    	                	System.out.println("enregistrment de :"+e);
		    	                	servicesEmploie.AddEmployeToEmployeur(employeur.getNumemployeur(), e);
		    	    				String contenu="Votre compte à été crée chez aventix: cliquez sur ce lient pour vous connecter :";
		    	    				contenu=contenu+" http://localhost:8080/Aventix/login  ou copiez et coller dans votre navigateur \n";
		    	    				contenu=contenu+" Conrdialement";
		    	    				
		    	    				EnvoiMail.envoyerMailSMTP(true,"Votre compte chez aventix", contenu,e.getEmail(), null);
		    	    				erreur="enregistrement effectué avec succès";				
		    	                } 
	    	                }
		                }
		                else
		                {
		                	erreur="selectionnez un fichier .csv";
		                }
		            }
		        }
		        request.setAttribute("erreur", erreur);
				this.getServletContext().getRequestDispatcher(vue).forward(request, response);
		    } catch (FileUploadException e) {
		        throw new ServletException("Échec de l'analyse de la requête multipart.", e);
		    }    	
		}
		else
		{
			request.getSession().invalidate();
			response.sendRedirect("login");
		}
	}
	
}
