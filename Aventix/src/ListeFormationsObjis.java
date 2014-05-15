

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import dao.EmployeDAO;
import util.HibernateUtil;
import modele.Employe;
import Services.ServicesEmploie;

/**
 * Servlet implementation class ListeFormationsObjis
 */
@WebServlet("/ListeFormationsObjis")
public class ListeFormationsObjis extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListeFormationsObjis() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
//		ServicesEmploie services=new ServicesEmploie();
//		List<Employe> listEmploye=services.findAllEmployes();
//		System.out.println("+++++++++++++++++++Liste des employés ++++++++++++++++++++++++++++++++++");
//		for (Iterator iterator = listEmploye.iterator(); iterator.hasNext();) {
//			Employe employe = (Employe) iterator.next();
//			System.out.println("login  :"+employe.getLogin());
//			System.out.println("mot de passe   :"+employe.getPassword());
//		}
//		request.getSession().setAttribute("listEmploye", listEmploye);
		
		
		
		doGet(request, response);
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

	
		
		ServicesEmploie services=new ServicesEmploie();
		ArrayList<Employe> listEmploye=(ArrayList<Employe>) services.findAllEmployes();
		request.getSession().setAttribute("listEmploye", listEmploye);

//		ArrayList<Employe> listEmploye = new ArrayList<Employe>();
//		try
//		{
//			// Begin unit of work
//			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
//			
//			 EmployeDAO employeDAO=new EmployeDAO();
//			 listEmploye=(ArrayList<Employe>)employeDAO.findAllEmployes();
//		     
//		     for (Iterator iterator = listEmploye.iterator(); iterator.hasNext();) 
//		     {
//					Employe employe = (Employe) iterator.next();
//					System.out.println("login  :"+employe.getLogin());
//					System.out.println("mot de passe   :"+employe.getPassword());
//		     }
//		     System.out.println("Nb listEmploye "+ listEmploye.size());
//		     request.getSession().setAttribute("test", "Test");
//			 //request.getSession().setAttribute("listEmploye", listEmploye);
//
//			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
//		}
//		catch (Exception ex)
//		{
//			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
//			if ( ServletException.class.isInstance( ex ) ) 
//			{
//				throw ( ServletException ) ex;
//			}
//			else
//			{
//				throw new ServletException( ex );
//			}
//		}		
		
		
		request.getRequestDispatcher("/WEB-INF/employe.jsp").forward(request, response);
	}
			

}
