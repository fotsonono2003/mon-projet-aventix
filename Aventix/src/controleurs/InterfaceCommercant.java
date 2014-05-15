package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.ServicesCommercant;
import modele.Commercant;

/**
 * Servlet implementation class InterfaceCommercant
 */
@WebServlet("/InterfaceCommercant")
public class InterfaceCommercant extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private final String vue ="/WEB-INF/commercant/index.jsp";
	private Commercant commercant=null;
	private ServicesCommercant servicesCommercant=new ServicesCommercant();
        
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InterfaceCommercant() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		commercant=(Commercant)request.getSession().getAttribute("commercant");
		if(commercant!=null)
		{
			this.getServletContext().getRequestDispatcher(vue).forward(request, response);
		}
		else
		{
			request.getSession().invalidate();
			response.sendRedirect("login");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		commercant=(Commercant)request.getSession().getAttribute("commercant");
		if(commercant!=null)
		{
			TraiterRequete(request, response);
		}
		else
		{
			request.getSession().invalidate();
			response.sendRedirect("login");
		}
	}
	
	private void TraiterRequete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String erreur=null;
		String succes=null;
		String raison = request.getParameter("raison");
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		String adresse = request.getParameter("adresse");
		String rib = request.getParameter("rib");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		
		System.out.println("password:"+password+",password2:"+password2);
		System.out.println("login:"+login+",password2:"+commercant.getLogin());
		System.out.println("email:"+email+",email:"+commercant.getEmail());
		if(password.equals(password2))
		{
			if(!email.equalsIgnoreCase(commercant.getEmail()))
			{
				Commercant c=servicesCommercant.getCommercantByEmail(email);
				if(c!=null)
				{
					erreur="cette adresse email existe dejà";
				}
			}
			if(!login.equalsIgnoreCase(commercant.getLogin()))
			{
				Commercant c=servicesCommercant.getCommercantByLogin(login);
				if(c!=null)
				{
					erreur="ce login existe dejà";
				}
			}
			if(erreur==null)
			{
				commercant.setRaisonSociale(raison);
				commercant.setEmail(email);
				commercant.setTelephone(tel);
				commercant.setAdresse(adresse);	
				commercant.setRib(Integer.parseInt(rib));
				commercant.setLogin(login);
				commercant.setPassword(password2);
				boolean b=servicesCommercant.MiseAJourCommercant(commercant);
				System.out.println("Boooolean:"+b);
				succes="modification effectuée";
			}
		}
		else
		{
			erreur="Les mots de passe doivent être identiques";
		}
		
		request.setAttribute("succes", succes);
		request.setAttribute("erreur", erreur);
		this.getServletContext().getRequestDispatcher(vue).forward(request, response);
	}

}
