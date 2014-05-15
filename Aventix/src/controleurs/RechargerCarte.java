package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Carte;
import Services.ServicesCarte;

/**
 * Servlet implementation class RechargerCarte
 */
@WebServlet("/RechargerCarte")
public class RechargerCarte extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private ServicesCarte servicesCarte=new ServicesCarte();
        
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RechargerCarte()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("--------reque doGet:-------::::");
		//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargercarte.jsp").forward(request, response);
		//response.sendRedirect("recharger");
		TraiterRequete(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("--------reque doPost:-------::::");
		TraiterRequete(request, response);
	}
	
	private void TraiterRequete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String montant=request.getParameter("montant");
		Carte carte=(Carte)request.getSession().getAttribute("carte"); 
		String erreur=null;
		if(montant!=null)
		{
			try
			{
				float val=Float.parseFloat(montant);
				if(val>0)
				{
					servicesCarte.RechargerCarte(carte.getNumerocarte(), val);
					erreur="Recharge effectuée";
				}
				else
				{
					erreur="Le montant doit être positif";
				}
				request.setAttribute("erreur", erreur);
				this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargercarte.jsp").forward(request, response);
			}
			catch (Exception ex) 
			{
				//ex.printStackTrace();
				erreur="verifiez le montant ";
				request.setAttribute("erreur", erreur);
				this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargercarte.jsp").forward(request, response);
			}
		}
		else
		{
			erreur="Saisissez un montant positif";
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargercarte.jsp").forward(request, response);
		}
		
		
	}
}
