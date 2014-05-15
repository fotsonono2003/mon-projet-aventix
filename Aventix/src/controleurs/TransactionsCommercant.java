package controleurs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Carte;
import modele.Commercant;
import modele.Employe;
import modele.Lecteurcarte;
import modele.Transactions;
import Services.GenererPDF;
import Services.ServicesCommercant;
import Services.ServicesTransactions;

/**
 * Servlet implementation class TransactionsCommercant
 */
@WebServlet("/TransactionsCommercant")
public class TransactionsCommercant extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private final String vue="/WEB-INF/commercant/transactions.jsp";
	private ServicesCommercant servicesCommercant=new ServicesCommercant();
	private ServicesTransactions servicesTransactions=new ServicesTransactions();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransactionsCommercant() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Commercant commercant=(Commercant)request.getSession().getAttribute("commercant");
		if(commercant!=null)
		{
			//this.getServletContext().getRequestDispatcher(vue).forward(request, response);
			String action=request.getParameter("action");
			if(action!=null)
			{
				if(action.equalsIgnoreCase("deconnexion"))
				{
					request.getSession().invalidate();
					response.sendRedirect("login");
				}
				else  if(action.equalsIgnoreCase("genererfichier"))
				{
					String fichierPDF="/WEB-INF/commercant/"+commercant.getNumcommercant()+"/transactions.pdf";
					this.getServletContext().getRequestDispatcher(fichierPDF).forward(request, response);
				}
			}
			else
			{
				List<Transactions> listTransactions=servicesTransactions.getTransactionsByCommercant(commercant.getNumcommercant());
		        System.out.println("Transaction Liste:"+listTransactions);
				request.setAttribute("listTransactions", listTransactions);
				this.getServletContext().getRequestDispatcher(vue).forward(request, response);
			}
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
		Commercant commercant=(Commercant)request.getSession().getAttribute("commercant");
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
		Commercant commercant=(Commercant)request.getSession().getAttribute("commercant");
		
		String debut=request.getParameter("debut");
		String fin=request.getParameter("fin");
		String  pdf=request.getParameter("pdf");
		System.out.println("pdf:"+pdf+", debut:"+debut+",fin:"+fin);
		
		try
		{
			System.out.println("pdf:"+pdf+", debut:"+debut+",fin:"+fin);
			String erreur=null;
			Set<Lecteurcarte>setLecteurcartes=commercant.getLecteurcartes();
			Iterator<Lecteurcarte> itrLecteur=setLecteurcartes.iterator();
			String str="";
			while(itrLecteur.hasNext())
			{
				str=str+itrLecteur.next().getNumlecteur()+",";
			}
			str=str.substring(0, str.length()-1);
			System.out.println("Liste Lecteur de carte:"+str);
			List<Transactions> listTransactions=servicesTransactions.ConsulterTransactionsCommercant(str, debut, fin);
			
			request.setAttribute("erreur", erreur);
			request.setAttribute("listTransactions", listTransactions);
	        System.out.println("Transaction Liste:"+listTransactions);
	        if(pdf!=null)
	        {
		        if(pdf.equalsIgnoreCase("on"))
		        {
		        	GenererPDF genererPDF=new GenererPDF();
		        	genererPDF.GenererPDFTransactionsCommercant(commercant, debut, fin);
		        }
	        }
			this.getServletContext().getRequestDispatcher(vue).forward(request, response);
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		
		/*List<Transactions> listTransactions=servicesTransactions.getTransactionsByCommercant(commercant.getNumcommercant());
		request.setAttribute("listTransactions", listTransactions);
        System.out.println("Transaction Liste:"+listTransactions);
		this.getServletContext().getRequestDispatcher(vue).forward(request, response);
		*/
	}
}
