package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.ServicesEmploie;
import modele.Employe;
import modele.Employeur;

/**
 * Servlet implementation class CreerEmploye
 */
@WebServlet("/CreerEmploye")
public class CreerEmploye extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreerEmploye() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//TraiterRequete(request, response);
		this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
		//this.getServletContext().getContextPath()
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		TraiterRequete(request, response);
		//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
	}
	
	private void TraiterRequete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String nom=request.getParameter("nom");
		String prenom=request.getParameter("prenom");
		String email=request.getParameter("email");
		String erreur=null;
		String succes=null;
		Employeur employeur=(Employeur)request.getSession().getAttribute("employeur");
		
		if(employeur!=null)
		{
			System.out.println("****************Employeur**************::::"+employeur);
			if(nom!=null && prenom!=null && email!=null)
			{
				Employe e=servicesEmploie.getEmployeByEmail(email);
				if(e==null)
				{
					Employe employe=new Employe();
					employe.setPrenom(prenom);
					employe.setNom(nom);
					employe.setEmail(email);
					servicesEmploie.AddEmployeToEmployeur(employeur.getNumemployeur(), employe);

					erreur="Enregistrement effectué avec succès";
					request.setAttribute("erreur",erreur);
					System.out.println("****************enregistrement employe**************::::"+e);
					
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
				}
				else
				{
					System.out.println("****************email existant**************::::"+email);
					erreur=null;
					succes="Enregistrement effectué avec succès";
					request.setAttribute("erreur",erreur);
					request.setAttribute("succes",succes);
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
				}
				//request.setAttribute("erreur",erreur);
				//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
				//response.sendRedirect("creeremploye");
			}
			else
			{
				erreur="remplissez tous les champs";
				request.setAttribute("erreur", erreur);
				//response.sendRedirect("creeremploye");
				this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
			}
		}
		else
		{
			response.sendRedirect("login");
			//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
		}
	}
}
