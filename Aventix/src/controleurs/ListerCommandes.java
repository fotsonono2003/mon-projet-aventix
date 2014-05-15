package controleurs;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.ServicesCommandes;
import modele.Carte;
import modele.Commande;
import modele.Employe;
import modele.Employeur;
import modele.Transactions;


/**
 * Servlet implementation class ListerCommande
 */
@WebServlet("/ListerCommande")
public class ListerCommandes extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private final String vue="/WEB-INF/employeur/listecommandes.jsp";
	private ServicesCommandes servicesCommandes=new ServicesCommandes();
	
	private Employeur employeur;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListerCommandes() { 
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		employeur=(Employeur)request.getSession().getAttribute("employeur");
		if(employeur!=null)
		{
			List<Commande>listCommandes=servicesCommandes.getCommandeByEmploye(employeur);
			request.setAttribute("listCommandes", listCommandes);
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
		employeur=(Employeur)request.getSession().getAttribute("employeur");
		if(employeur!=null)
		{
			TraiterRequete(request, response);
		}
		else
		{
			request.getSession().invalidate();
			response.sendRedirect("login");
		}
	}

	private void  TraiterRequete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String debut=request.getParameter("debut");
		String fin=request.getParameter("fin");
		String  pdf=request.getParameter("pdf");
		System.out.println("pdf:"+pdf+", debut:"+debut+",fin:"+fin);
		
		try
		{
			String erreur=null;
			List<Commande> listCommandes = servicesCommandes.getCommandeByPeriode(employeur.getNumemployeur(), debut, fin);
			request.setAttribute("listTransactions", listCommandes);
			System.out.println("pdf:" + pdf + ", Taille:"+listCommandes.size());
			request.setAttribute("listCommandes", listCommandes);
			request.setAttribute("erreur", erreur);
			if (pdf != null) 
			{
				if (pdf.equalsIgnoreCase("on")) 
				{
					// //generer le pdf
				} 
			}
			this.getServletContext().getRequestDispatcher(vue).forward(request, response);
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}

		
		
		
	}
}
