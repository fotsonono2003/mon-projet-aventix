package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Employeur;
import Services.EnvoiMail;
import Services.ServicesEmploie;

/**
 * Servlet implementation class CreerEmployeur
 */
@WebServlet("/CreerEmployeur")
public class CreerEmployeur extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreerEmployeur() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		this.getServletContext().getRequestDispatcher("/WEB-INF/employeur.jsp").forward(request, response);
		//this.getServletContext().getRequestDispatcher("/WEB-INF/test.jsp").forward(request, response);
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
		String raison=request.getParameter("raison");
		String email=request.getParameter("email");
		String login=request.getParameter("login");
		String password=request.getParameter("password");
		String password2=request.getParameter("password2");
		String erreur=null;
		if(password.equals(password2))
		{ 
			Employeur employeur=new Employeur();
			if(servicesEmploie.getEmployeByEmail(email)!=null)
			{
				erreur="cette adresse mail existe dejà";
			}
			else if(servicesEmploie.getEmployeByLogin(login)!=null)
			{
				erreur="ce login existe dejà";
			}
			else
			{
				employeur.setRaisonsocial(raison);
				employeur.setEmail(email);
				employeur.setPassword(password2);
				employeur.setLogin(login);
				servicesEmploie.AddEmployeur(employeur);
				
				String contenu="Votre compte à été crée chez aventix: cliquez sur ce lien pour vous connecter :";
				contenu=contenu+" http://localhost:8080/Aventix/login   ou copiez et coller dans votre navigateur \n";
				contenu=contenu+" Conrdialement";
				erreur="enregistrement effectué";
				
				EnvoiMail.envoyerMailSMTP(true,"Votre compte chez aventix", contenu, email, null);
			}
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher("/WEB-INF/employeur.jsp").forward(request, response);			
		}
		else
		{
			erreur="les mots de passe doivent être identiques";
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher("/WEB-INF/employeur.jsp").forward(request, response);			
		}
		
	}
}
