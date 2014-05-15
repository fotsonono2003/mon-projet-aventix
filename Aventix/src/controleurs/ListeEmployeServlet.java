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
 * Servlet implementation class ListeEmployeServlet
 */
@WebServlet("/ListeEmployeServlet")
public class ListeEmployeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
	//private ServicesCarte servicesCarte=new ServicesCarte();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListeEmployeServlet() {
        //super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		TraiterRequete(request, response);
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
			String action=request.getParameter("action");
			if(action!=null)
			{
				if(action.equalsIgnoreCase("deconnexion"))
				{
					request.getSession().invalidate();
					response.sendRedirect("login");
				} 
				else if(action.equalsIgnoreCase("listeemploye"))
				{
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					System.out.println("----------------action liste employeur-------------::::"+action);				
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("modifier"))
				{
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					System.out.println("-----------------action liste employeur------------::::"+action);				
					//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/listeEmploye.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("supprimer"))
				{
					String idemploye=(String)request.getParameter("idemploye");
					int id=0;
					try 
					{
						id=Integer.parseInt(idemploye);
					} catch (Exception e) 
					{
						id=0;
					}
					servicesEmploie.SupprimerEmployeToEmployeur(employeur.getNumemployeur(), id);
					//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/listeEmploye.jsp").forward(request, response);
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("recharger"))
				{
					////recharger carte employe
					//String id=request.getParameter("numcarte");
					int numcarte=0;
					float montant=0;
					try 
					{
						montant=Float.parseFloat(request.getParameter("montant"));
						numcarte=Integer.parseInt(request.getParameter("numcarte"));
					} catch (Exception e) 
					{
						montant=0;
						numcarte=0;
					}
					if(montant==0 || numcarte==0)
					{
						String erreur="donnez des valeurs superieurs à 0";
						request.setAttribute("erreur",erreur);
						this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
					}
					else
					{
						String erreur="recharge effectuée avec succès";
						request.setAttribute("erreur",erreur);
						this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
					}
					//servicesCarte.RechargerCarte(numcarte, montant);
				}
				else if(action.equalsIgnoreCase("creer"))
				{
					//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
					response.sendRedirect("creeremploye");
				}
			}
			else 
			{
				//servicesEmploie=new ServicesEmploie();
				request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
				this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/listeEmploye.jsp").forward(request, response);
			}
			
		}
		else
		{
			request.setAttribute("erreur", "Login et/ou mot de passe  employeur incorrect");
			response.sendRedirect("login");
		}
	}
}
