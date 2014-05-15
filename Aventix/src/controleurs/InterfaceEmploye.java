package controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.ServicesEmploie;
import modele.Employe;

/**
 * Servlet implementation class InterfaceEmployeur
 */
@WebServlet("/InterfaceEmployeur")
public class InterfaceEmploye extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
	//private ServicesTransactions servicesTransactions=new ServicesTransactions();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InterfaceEmploye() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Employe employe=(Employe)request.getSession().getAttribute("employe");
		if(employe==null)
		{
			request.getSession().invalidate();
			response.sendRedirect("login");
		}
		else
		{
			String action=request.getParameter("action");
			if(action!=null)
			{
				if(action.equalsIgnoreCase("deconnexion"))
				{ 
					request.getSession().invalidate();
					response.sendRedirect("login");
				} 
				else if(action.equalsIgnoreCase("retour"))
				{
					System.out.println("****************action de deconnexion nulle**************::::"+action);				
					
					response.sendRedirect("employe");
				}
				else if(action.equalsIgnoreCase("transactions"))
				{
					System.out.println("****************Numero Carte Employe**************::::"+employe.getCarte());
					//List<Transactions>listTransactions=servicesTransactions.getTransactionsByCarte(employe.getCarte().getNumerocarte());
					//request.setAttribute("listTransactions", listTransactions);
					response.sendRedirect("transactionsemploye");
				}
			}
			else
			{
				this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
				//TraiterRequete(request, response);
			}
			//System.out.println("****************action de deconnexion doGet**************::::"+employe);				
			//this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
		}
		//this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);

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
		Employe employe=(Employe)request.getSession().getAttribute("employe");
		if(employe!=null)
		{
			String action=request.getParameter("action");
			if(action!=null)
			{
				if(action.equalsIgnoreCase("deconnexion"))
				{
					request.getSession().invalidate();
					response.sendRedirect("login");
				} 
				else if(action.equalsIgnoreCase("retour"))
				{
					System.out.println("****************action de deconnexion nulle**************::::"+action);				
					
					response.sendRedirect("employe");
				}
				else if(action.equalsIgnoreCase("transactions"))
				{
					System.out.println("****************Numero Carte Employe**************::::"+employe.getCarte());
					//List<Transactions>listTransactions=servicesTransactions.getTransactionsByCarte(employe.getCarte().getNumerocarte());
					//request.setAttribute("listTransactions", listTransactions);
					response.sendRedirect("transactionsemploye");
				}
			}
			else
			{
				System.out.println("****************action nuuuuuuuuuuuuuuuuuulle**************::::"+action);				
				String nom=request.getParameter("nom");
				String prenom=request.getParameter("prenom");
				String email=request.getParameter("email");
				String login=request.getParameter("login");
				String password=request.getParameter("password");
				String password2=request.getParameter("password2");
				String code=request.getParameter("code");
				String code2=request.getParameter("code2");
				String erreur=null;
				
				System.out.println("***************login:"+login+",password:"+password+",passwrd2:"+password2);										
				System.out.println("***************nom:"+nom+",prenom:"+prenom+",email:"+email+",code:"+code+",code:"+code2);
				
					if(password.equals(password2))
					{
						System.out.println("******/*/*/*//*/passworddd*****login:"+login+",password:"+password+",passwrd2:"+password2);										
						System.out.println("***************nom:"+nom+",prenom:"+prenom+",email:"+email);
						Employe e=servicesEmploie.getEmployeByEmail(email);
						if(!email.equals(employe.getEmail()))
						{
							if(e!=null)
							{
								erreur="cette adresse email existe dejà";
							}
							//else erreur=null;
						} 
						/*else if (!login.equals(employe.getLogin())) 
						{
							System.out.println("******login diffzzzzzzzzzzzzzzzzzzzzzzrent******");										
							e=servicesEmploie.getEmployeByLogin(login);
							if(e!=null)
							{
								erreur="ce login existe deja";
							}
							//else erreur=null;
							
						}
						*/
						else
						{
							if (employe.getCarte() != null)
							{
								if (code.equals(code2) && code.length()>4)
								{
									try 
									{
										employe.setNom(nom);
										employe.setPrenom(prenom);
										employe.setLogin(login);
										employe.setEmail(email);
										employe.setPassword(password);
										employe.getCarte().setPassword(Integer.parseInt(code));
										servicesEmploie.MiseAjourEmploye(employe);
										erreur="Modification effectuée avec succès";
										request.setAttribute("erreur", erreur);
										//response.sendRedirect("employe");
									} catch (Exception e2)
									{
										erreur="Le code  de la carte doite être un nombre";
									}
								} else  
								{
									erreur = "les codes de carte doivent être identiques et ne doivent pas dépasser quatre chiffres";
								}

							} else
							{
								employe.setNom(nom);
								employe.setPrenom(prenom);
								employe.setLogin(login);
								employe.setEmail(email);
								employe.setPassword(password);
								servicesEmploie.MiseAjourEmploye(employe);
								erreur="Modification effectuée avec succès";
								request.setAttribute("erreur", erreur);
								//response.sendRedirect("employe");
							}
						}
					}
					else
					{
						erreur="les mots de passe doivent être identiques";
						request.setAttribute("erreur", erreur);
					} 
				//System.out.println("****************Parametres non null*************::::"+action);				
				//this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
				//System.out.println("****************Parametres non null*************::::"+action);				
				//this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
				request.setAttribute("erreur", erreur);
				this.getServletContext().getRequestDispatcher("/WEB-INF/employe/index.jsp").forward(request, response);
			}
		}
		else
		{
			request.setAttribute("erreur", "Login et/ou mot de passe  employe incorrect");
			response.sendRedirect("login");
		}
	}
}
