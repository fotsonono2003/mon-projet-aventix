package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Commercant;
import modele.Employe;
import modele.Employeur;
import Services.ServicesCommercant;
import Services.ServicesEmploie;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
	private ServicesCommercant servicesCommercant=new ServicesCommercant();
       
    public LoginServlet() 
    { 
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		//Traitement(request, response);
		this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		//response.sendRedirect("login");
		//request.getRequestDispatcher("/accueil.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Traitement(request, response);
	}

	private void Traitement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String login=request.getParameter("login");
		String password=request.getParameter("password");
		String statut=request.getParameter("statut");
		System.out.println("******************Debut Login:"+login+", password:"+password+", Statut:"+statut);	
		if(statut==null)
		{
			//request.setAttribute("erreur", "Entrer un login et un mot de passe");
			this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
			
		if(statut.equals("employe"))
		{
			Employe employe=servicesEmploie.VerifierLoginAndPasswordEmploye(login, password);
			Employe e1=servicesEmploie.VerifierEmailAndPasswordEmploye(login, password);
			if(employe!=null || e1!=null)
			{
				if(employe==null)
				{
					employe=e1;
				}
				System.out.println("******************Login Employe reussi :"+login+", password:"+password+", Statut:"+statut);	
				request.getSession().setAttribute("employe",employe);
				response.sendRedirect("employe");
				//request.getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
				//this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
			} 			
			else
			{
				request.setAttribute("erreur", "Login et/ou mot de passe  employe incorrect");
				//request.getSession().setAttribute("erreur", "/*/*/*/*/*Login ou mot de passe incorrect");
				this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}
		else if(statut.equals("employeur"))
		{
			Employeur employeur=servicesEmploie.VerifierLoginAndPasswordEmployeur(login, password);
			Employeur e1=servicesEmploie.VerifierEmailAndPasswordEmployeur(login, password);
			if(employeur!=null || e1!=null)
			{
				if(employeur==null)
				{
					employeur=e1;
				}
				request.getSession().setAttribute("employeur",employeur);
				response.sendRedirect("employeur");
				//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/index.jsp").forward(request, response);
			}
			else
			{
				request.setAttribute("erreur", "Login et/ou mot de de l'employeur passe incorrect");
				this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}
		else if(statut.equals("commercant"))
		{
			Commercant commercant=servicesCommercant.VerifierLoginAndPasswordCommercant(login, password);
			Commercant com=servicesCommercant.VerifierEmailAndPasswordCommercant(login, password);
			if(commercant!=null || com!=null)
			{
				if(commercant==null)
				{
					commercant=com;
				}
				request.getSession().setAttribute("commercant",commercant);
				response.sendRedirect("commercant");
				System.out.println("******************Login Commercant:"+login+", password:"+password+", Statut:"+statut);	
			}
			else   
			{
				request.setAttribute("erreur", "Login et/ou mot de passe  de commerçant incorrect");
				this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}
		else   
		{
			request.setAttribute("erreur", "Login et/ou mot de passe  de commerçant incorrect");
			this.getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
