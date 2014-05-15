package controleurs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.GenererPDF;
import Services.ServicesCarte;
import Services.ServicesCommandes;
import Services.ServicesEmploie;
import modele.Carte;
import modele.Commande;
import modele.Employe;
import modele.Employeur;

/**
 * Servlet implementation class InterfaceEmployeur
 */
@WebServlet("/InterfaceEmployeur")
public class EmployeurServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private ServicesEmploie servicesEmploie=new ServicesEmploie();
	private final String recharger="/WEB-INF/employeur/rechargerfichier.jsp";
	private final String vueListe="/WEB-INF/employeur/liste.jsp";
    private ServicesCarte servicesCarte=new ServicesCarte();
    private ServicesCommandes servicesCommandes=new ServicesCommandes();
    //private final String cheminEmployeur="employeur";
    //private final String cheminEmploye="employe";
	File fileTest=new File(this.getClass().getResource("/").getFile());
	private final String cheminEmployeur=fileTest.getParentFile().getPath()+File.separator+"employeur"+File.separator;
	private final String cheminEmploye=fileTest.getParentFile().getPath()+File.separator+"employe"+File.separator;
      
    /**  
     * @see HttpServlet#HttpServlet()
     */
    public EmployeurServlet() 
    { 
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Employeur employeur=(Employeur)request.getSession().getAttribute("employeur");
		if(employeur!=null)
		{
			String action=request.getParameter("action");
			if(action!=null)
			{
				System.out.println("looooooooooooooooooooooooooooooggggggggggiiiiiiiiiinnnn");
				 if(action.equalsIgnoreCase("genererfichier"))
				{ 
					System.out.println("-----------gennnnerer fichier csv:");
					List<Employe>listEmployeFichierCSV=servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur());
					if(listEmployeFichierCSV!=null)
					{
						GenererFichierEmployeCSV(employeur.getNumemployeur(), listEmployeFichierCSV);
						//generFichier.GenererFichierEmployeCSV(employeur.getNumemployeur(), listEmployeFichierCSV);
						request.setAttribute("listEmploye",listEmployeFichierCSV);
						String fichiercsv="/WEB-INF/employeur/"+employeur.getNumemployeur()+"/employes.csv";
						this.getServletContext().getRequestDispatcher(fichiercsv).forward(request, response);					
					} 
					//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				}
				else
				{
					TraiterRequete(request, response);
				}
				
			}
			else
			{
				TraiterRequete(request, response);
			}
		}
		else
		{
			request.getSession().invalidate();
			response.sendRedirect("login");
		}
		//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/index.jsp").forward(request, response);
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
				else if(action.equalsIgnoreCase("rechargerfichier"))
				{
					request.getRequestDispatcher(recharger).forward(request, response);
				} 
				else if(action.equalsIgnoreCase("genererfichier"))
				{ 
					System.out.println("-----------gennnnnnnnnnnnnnnnnn:");
					List<Employe>listEmployeFichierCSV=servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur());
					if(listEmployeFichierCSV!=null)
					{
						GenererFichierEmployeCSV(employeur.getNumemployeur(), listEmployeFichierCSV);
						//generFichier.GenererFichierEmployeCSV(employeur.getNumemployeur(), listEmployeFichierCSV);
						request.setAttribute("listEmploye",listEmployeFichierCSV);
					} 
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("bloquer"))
				{
					String id=request.getParameter("idcarte");
					System.out.println("Id EMploye:"+id);
					
					int num=Integer.parseInt(id);
					boolean b=servicesCarte.bloquerCarte(num);
					System.out.println("Etat blocage:"+b);
					servicesEmploie.MiseAjourEmployeur(employeur);
					
					//employeur=servicesEmploie.getEmployeurByNumero(employeur.getNumemployeur());
					
					request.getSession().setAttribute("employeur", employeur);
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				} 
				else if(action.equalsIgnoreCase("activer"))
				{
					String id=request.getParameter("idcarte");
					int num=Integer.parseInt(id);
					boolean b=servicesCarte.ActiverCarte(num);
					System.out.println("Etat blocage:"+b);
					servicesEmploie.MiseAjourEmployeur(employeur);
					
					employeur=servicesEmploie.getEmployeurByNumero(employeur.getNumemployeur());
					
					request.getSession().setAttribute("employeur", employeur);					
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					//response.sendRedirect("employeur?action=listeemploye");
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				} 
				else if(action.equalsIgnoreCase("validercommande"))
				{
					List<Employe>listEmployeCommande=(List<Employe>)request.getSession().getAttribute("listEmployeCommande");
					Commande commande=new Commande();
					if(listEmployeCommande!=null)
					{
						commande.setNbcarte(listEmployeCommande.size());
						servicesCommandes.AddCommandeToEmployeur(employeur.getNumemployeur(), commande);
						for(Employe employe:listEmployeCommande)
						{ 
							Carte carte=new Carte();
							servicesCarte.AddCarteToEmploye(employe.getNumemploye(), carte);
						} 
						//GenererPDF genererPDF=new GenererPDF();
						//genererPDF.GenererPDFFacture(employeur, commande);
						request.getSession().setAttribute("listEmployeCommande",null);
						request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
						//request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					}
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("annulercommande"))
				{ 
					String id=request.getParameter("idemploye");
					int num=Integer.parseInt(id);
					List<Employe>listEmployeCommande=(List<Employe>)request.getSession().getAttribute("listEmployeCommande");
					for(Employe employe: listEmployeCommande)
					{
						if(employe.getNumemploye()==num)
						{
							listEmployeCommande.remove(employe);
							break;
						}
					}
					request.getSession().setAttribute("listEmployeCommande",listEmployeCommande);
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("commander"))
				{
					String id=request.getParameter("idemploye");
					int num=Integer.parseInt(id);
					List<Employe>listEmployeCommande=(List<Employe>)request.getSession().getAttribute("listEmployeCommande");
					Employe employe=servicesEmploie.getEmployeByNumero(num);
					if(listEmployeCommande==null)
					{
						listEmployeCommande=new ArrayList<Employe>();
						listEmployeCommande.add(employe);
						request.getSession().setAttribute("listEmployeCommande",listEmployeCommande);
					}
					else
					{
						if(!listEmployeCommande.contains(employe))
						{
							listEmployeCommande.add(employe);
							request.getSession().setAttribute("listEmployeCommande",listEmployeCommande);
						}
					}
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
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
					String val=request.getParameter("idemploye");
					try
					{ 
						int id=Integer.parseInt(val);
						Employe employe=servicesEmploie.getEmployeByNumero(id);
						System.out.println("--------Employe selectionné Employeur:-------::::"+employe);
						System.out.println("--------id Employe selectionné employeur :-------::::"+val);
						Carte carte=employe.getCarte();
						request.getSession().setAttribute("employe", employe);
						request.getSession().setAttribute("carte", carte);
						 if(carte!=null)
						 {
								response.sendRedirect("recharger");
						 }
						 else
						 {
								request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
								this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
						 }
						//request.getRequestDispatcher("/WEB-INF/employeur/rechargercarte.jsp").forward(request, response);;
						//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargercarte.jsp").forward(request, response);
					}  
					catch (Exception ex) 
					{
						ex.printStackTrace();
					}
				} 
				else if(action.equalsIgnoreCase("creeremploye"))
				{
					response.sendRedirect("creeremploye");
					//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/creeremploye.jsp").forward(request, response);
				}
				else if(action.equalsIgnoreCase("connexion"))
				{
					System.out.println("looooooooooooooooooooooooooooooggggggggggiiiiiiiiiinnnn");
					request.getSession().invalidate();
					response.sendRedirect("login");
				}
				else if(action.equalsIgnoreCase("modifier"))
				{
					response.sendRedirect("modifier");
					System.out.println("--------action modifier-------::::");
					 
					//this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/modifierEmployeur.jsp").forward(request, response);
				} 
				else if(action.equalsIgnoreCase("listeemploye"))
				{
					request.setAttribute("listEmploye", servicesEmploie.getListEmployeDunEmployeur(employeur.getNumemployeur()));
					System.out.println("----------------------employeur:"+employeur);				
					System.out.println("-------------action liste employeur----------::::"+action);
					//response.sendRedirect("employeur");
					this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/liste.jsp").forward(request, response);
				} 
			}
			else
			{
				this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/index.jsp").forward(request, response);
			}
		}
		else
		{
			System.out.println("looooooooooooooooooooooooooooooggggggggggiiiiiiiiiinnnn");
			String action=request.getParameter("action");
			if(action==null)
			{
				request.getSession().invalidate();
				response.sendRedirect("login");
			}
			else if(action.equals("connexion"))
			{
				request.getSession().invalidate();
				response.sendRedirect("login");
			}
		}
	}

	private void GenererFichierEmployeCSV(int numemployeur,List<Employe>listEmploye)
	{
		
		File file=new File(cheminEmployeur+numemployeur+File.separator);
		if(!file.exists())
		{
			file.mkdirs();
		}
		
		file=new File(cheminEmployeur+numemployeur+File.separator+"employes.csv");
		try 
		{
			if(file.exists())
			{
				file.delete();
			}
			
			System.out.println("-----------chemin fichier employe.csv:"+file.getAbsolutePath());
			PrintStream printStream = new PrintStream(new FileOutputStream(file,true)); 
			printStream.println("Numero;Nom;Prenom;Email");
			for(Employe e:listEmploye)
			{ 
				String ligne=e.getNumemploye()+";"+e.getNom()+";"+e.getPrenom()+";"+e.getEmail()+"";
				printStream.println(ligne);
			}
			//fileWriter.close();
			printStream.flush();
			printStream.close();
			printStream=null;
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	
	
}
