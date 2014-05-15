package controleurs;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Services.GenererPDF;
import Services.ServicesTransactions;
import modele.Carte;
import modele.Employe;
import modele.Transactions;

/**
 * Servlet implementation class TransactionsEmploye
 */
@WebServlet("/TransactionsEmploye")
public class TransactionsEmploye extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServicesTransactions servicesTransactions=new ServicesTransactions();
    private static final String vue="/WEB-INF/employe/transactions.jsp"; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransactionsEmploye() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Employe employe=(Employe)request.getSession().getAttribute("employe");
		if(employe!=null)
		{
			String action =request.getParameter("action");
			if(action!=null)
			{
				if(action.equalsIgnoreCase("afficherfichier"))
				{
					String fichierPDF="/WEB-INF/employe/"+employe.getNumemploye()+"/transactions.pdf";
					this.getServletContext().getRequestDispatcher(fichierPDF).forward(request, response);
				}
			}
			else
			{
				if(employe.getCarte()!=null)
				{
					List<Transactions>listTransactions=servicesTransactions.getTransactionsByCarte(employe.getCarte().getNumerocarte());
					request.setAttribute("listTransactions", listTransactions);
				}
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
		Employe employe=(Employe)request.getSession().getAttribute("employe");
		
		if(employe!=null)
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
		System.out.println("****************transactions doPost**************::::");
		String debut=request.getParameter("debut");
		String fin=request.getParameter("fin");
		String  pdf=request.getParameter("pdf");
		System.out.println("pdf:"+pdf+", debut:"+debut+",fin:"+fin);
		
		//SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		try
		{
			System.out.println("pdf:"+pdf+", debut:"+debut+",fin:"+fin);
			String erreur=null;
			Employe employe=(Employe)request.getSession().getAttribute("employe");
			Carte carte=employe.getCarte();
			if(carte!=null)
			{
				List<Transactions>listTransactions=servicesTransactions.ConsulterTransactionsCarte(carte.getNumerocarte(), debut, fin);
				request.setAttribute("listTransactions", listTransactions);
				System.out.println("pdf:"+pdf+", Taille:"+listTransactions.size());
				if(pdf!=null) 
				{
					if(pdf.equalsIgnoreCase("on"))
					{
						////generer le pdf
						if(employe.getCarte()!=null)
						{
							GenererPDF genererPDF=new GenererPDF();
							genererPDF.GenererPDFTransactionsEmploye(employe, debut, fin);
						}
					}
				}
				erreur="";
				//this.getServletContext().getRequestDispatcher(vue).forward(request, response);
			}
			request.setAttribute("erreur", erreur);
			this.getServletContext().getRequestDispatcher(vue).forward(request, response);
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		
	}
}
