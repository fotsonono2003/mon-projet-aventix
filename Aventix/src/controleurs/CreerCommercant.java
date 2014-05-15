package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.EnvoiMail;
import Services.ServicesCommercant;
import modele.Commercant;

/**
 * Servlet implementation class CreerCommercant
 */
@WebServlet("/CreerCommercant")
public class CreerCommercant extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private ServicesCommercant servicesCommercant=new ServicesCommercant();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreerCommercant()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.getServletContext().getRequestDispatcher("/WEB-INF/commercant.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		TraiterRequete(request, response);
	}
	
	private void TraiterRequete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String raison=request.getParameter("raison");
		String email=request.getParameter("email");
		String adresse=request.getParameter("adresse");
		String login=request.getParameter("login");
		String password=request.getParameter("password");
		String password2=request.getParameter("password2");
		String rib=request.getParameter("rib");
		String erreur=null;
		if(password.equals(password2))
		{
			Commercant commercant=servicesCommercant.getCommercantByEmail(email);
			if(commercant!=null)
			{
				erreur="cette adresse email existe dejà";
			}
			else if(servicesCommercant.getCommercantByLogin(login)!=null)
			{
				erreur="cette adresse email existe dejà";
				request.setAttribute("erreur", erreur);
			}
			else
			{
				Commercant com=new Commercant();
				com.setEmail(email);
				com.setLogin(login);
				com.setPassword(password);
				com.setRaisonSociale(raison);
				com.setAdresse(adresse);
				com.setRib(Integer.parseInt(rib.trim()));
				servicesCommercant.AddCommercant(com);
				String contenu="Votre compte à été crée chez aventix: cliquez sur ce lien pour vous connecter :";
				contenu=contenu+" http://localhost:8080/Aventix/login  ou copiez et coller dans votre navigateur \n";
				contenu=contenu+" Conrdialement";
				
				EnvoiMail.envoyerMailSMTP(true,"Votre compte chez aventix", contenu, email, null);
				erreur="enregistrement effectué avec succès";				
			}
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher("/WEB-INF/commercant.jsp").forward(request, response);
		}
		else
		{
			erreur="les mots de passe doivent être identiques";
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher("/WEB-INF/commercant.jsp").forward(request, response);
		}
	}

}
