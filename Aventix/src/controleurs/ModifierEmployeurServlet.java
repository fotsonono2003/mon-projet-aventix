package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.ServicesEmploie;
import modele.Employeur;

/**
 * Servlet implementation class InterfaceEmployeur
 */
@WebServlet("/InterfaceEmployeur")
public class ModifierEmployeurServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifierEmployeurServlet() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//TraiterRequete(request, response);
		this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/modifierEmployeur.jsp").forward(request, response);
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
			String raison = request.getParameter("raisonsociale");
			String adresse = request.getParameter("adresse");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String password = request.getParameter("password");
			String password2 = request.getParameter("password2");
			System.out.println("login:"+login+", employeur:"+employeur.getLogin());
			System.out.println("password:"+password+", employeur:"+employeur.getPassword());
			System.out.println("email:"+email+", employeur:"+employeur.getEmail());
			String erreur = null;
			if (password == null || password2 == null)
			{ 
				erreur = "remplissez le login et/ou le mot de passe";
				System.out.println("****************action de deconnexion nulle**************::::"+ erreur);
			} 
			else if (!password.equals(password2)) 
			{ 
				erreur = "tapez le meme mot de passe dans les deux cases";
				System.out.println("****************mot de passe different**************::::"+ erreur);
			} 
			else if (!email.equalsIgnoreCase(employeur.getEmail())) 
			{
				if(servicesEmploie.getEmployeurByEmail(email) != null)
				{
					erreur = "cette adresse email existe dejà";
				}
				System.out.println("****************email different **************::::"+ erreur);
			}
			else if (!login.equalsIgnoreCase(employeur.getLogin())) 
			{
				if(servicesEmploie.getEmployeurByLogin(login) != null)
				{
					erreur = "ce login existe dejà";
				}
				System.out.println("****************login different**************::::"+ erreur);
			}
			else 
			{
				employeur.setRaisonsocial(raison);
				employeur.setAdresse(adresse);
				employeur.setEmail(email);
				employeur.setLogin(login);
				employeur.setPassword(password);
				servicesEmploie.MiseAjourEmployeur(employeur);
				erreur = "eneregistrement effectué";
			}
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/modifierEmployeur.jsp").forward(request, response);
			 
		}else
		{
			request.setAttribute("erreur", "Login et/ou mot de passe  employeur incorrect");
			response.sendRedirect("login");
		}
	}

}
