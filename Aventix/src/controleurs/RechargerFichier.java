package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.Carte;
import modele.Employe;
import modele.Employeur;

//import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Services.ServicesCarte;
import Services.ServicesEmploie;

/**
 * Servlet implementation class RechargerFichier
 */
@WebServlet("/RechargerFichier")
public class RechargerFichier extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
      private ServicesCarte servicesCarte=new ServicesCarte();
      private ServicesEmploie servicesEmploie=new ServicesEmploie();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RechargerFichier() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{ 
		//TraiterRequete(request, response);
		this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargerfichier.jsp").forward(request, response);		
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
    	
		String erreur=null;
    	try 
		{
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			Map<Integer , Float> mapEmpMon=new HashMap<Integer,Float>();
			for (FileItem item : items)
			{
	         
	           if (item.isFormField()) 
	            {
	                //String nomChamp = item.getFieldName();
	                //String valeurChamp = item.getString();
	            } else
	            {
	                String nomChamp = item.getFieldName();
	                String nomFichier = item.getName();
	                InputStream stream = item.getInputStream();
	                boolean b=true;
                	System.out.println("*******************-Nom du fichier:"+item.getName()); 
                	System.out.println("*******************-Nom du fichier:"+nomChamp); 
                	if(nomFichier.endsWith(".csv"))
                	{
    	                InputStreamReader reader=new InputStreamReader(stream);
    	                BufferedReader br=new BufferedReader(reader);
    	                String ligne=null;
    	                int nbre=0;
    	                while ((ligne=br.readLine())!=null && b==true)
    	                { 
    	                	nbre++;
    	                	ligne=ligne.trim();
    	                	if(!ligne.isEmpty() && nbre>1)
    	                	{
    	                		try
    	                		{
									int num=Integer.parseInt(ligne.split(";")[0]);
									float montant=Float.parseFloat(ligne.split(";")[4]);
									//if(!listNum.contains(Integer.parseInt(ligne.split(";")[0])))
									if(mapEmpMon.containsKey(Integer.parseInt(ligne.split(";")[0])))	
									{
										erreur="Ligne:"+nbre+"verifiez que le fichier ne contient pas des doublons  ";
										b=false;
									}
									if(b==true && montant>=5)
									{
										mapEmpMon.put(num,montant);
									}
									else
									{
										b=false;
										erreur="Ligne "+nbre+" :le montant doit être superieur à 5€ ";
									}
										
								} catch (Exception ex) 
								{
									b=false;
									erreur="Ligne "+nbre+": une erreur est survenue lors la lecture du fichier";
									//ex.printStackTrace();
								}
    	                	}
    	                	System.out.println("-------------------------ligne:"+ligne);
    	                }
                	}
                	else
                	{
                		erreur="selectionnez un fichier .csv ";
                		request.setAttribute("erreur", erreur);
                		break;
                	}
                	/////debut de la recharge des cartes
    		    	if(b==true)
    		    	{
    		    		boolean verif=false;
    		    		List<Carte>listCarte=new ArrayList<Carte>();
    		    		for(Entry<Integer, Float> entry : mapEmpMon.entrySet()) 
    		    		{
    		    		    int  numero = entry.getKey();
    		    		    //float montant = entry.getValue();
    		    			Employe employe=servicesEmploie.getEmployeByNumero(numero);
    		    			if(employe!=null)
    		    			{
    		    				System.out.println("Employeur:"+employeur.getNumemployeur());
    		    				System.out.println("Employe:"+employe.getEmployeur().getNumemployeur());
    		    				int numemplyeur=employeur.getNumemployeur();
    		    				int numemploye=employeur.getNumemployeur();
    		    				//if(employeur.getNumemployeur()==employe.getEmployeur().getNumemployeur())
    		    				if(numemploye==numemplyeur)
    		    				{
        		    				Carte carte=employe.getCarte();
        		    				if(carte!=null)
        		    				{
        		    					listCarte.add(carte);
        		    				}
        		    				else
        		    				{
        		    					verif=true;
        		    					erreur="L'employe de numero:"+employe.getNumemploye()+" ne possede pas de carte";
        		    					break;
        		    				}
    		    				}
    		    				else
    		    				{
    		    					erreur="le matricule numero "+employe.getNumemploye()+" ne correspond à aucun employé";
    		    					verif=true;
    		    					break;
    		    				}
    		    			}
    		    			else
    		    			{
    		    				verif=true;
		    					erreur="Le numero "+numero+" ne correspond à aucun employé";
    		    				break;
    		    			}
    		    		}
    		    		
    		    		if(verif==false)
    		    		{
    		    			int n=0;
        		    		for(Entry<Integer, Float> entry : mapEmpMon.entrySet()) 
        		    		{
        		    		    int  numero = entry.getKey();
        		    		    float montant = entry.getValue();
        		    		    //Employe employe=servicesEmploie.getEmployeByNumero(numero);
    		    		    	Carte carte=listCarte.get(n);
        		    		    if(carte!=null)
        		    		    {
        		    		    	servicesCarte.RechargerCarte(carte.getNumerocarte(), montant);
        		    		    }
        		    		    else
        		    		    {
        		    		    	erreur="L'employe de numero:"+numero+" n'existe pas";
        		    		    	break;
        		    		    }
        		    		    n++;
        		    		} 
        		    		if(n==listCarte.size())
        		    		{
        		    			erreur="recharge effectué avec succès";
        		    		}
    		    		}
    		    	}
    		    	/////////

	            }
			}
	    }
		catch (FileUploadException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("erreur", erreur);
		this.getServletContext().getRequestDispatcher("/WEB-INF/employeur/rechargerfichier.jsp").forward(request, response);				
	}
	
	
}
