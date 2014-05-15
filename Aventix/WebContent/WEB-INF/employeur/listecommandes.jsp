<%@page import="modele.Commande"%>
<%@page import="modele.Transactions"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="modele.Employe"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.lang.String" %>
<%@ page import="modele.Employeur" %>
<%
	Employeur employeur=(Employeur)request.getSession().getAttribute("employeur");
	if(employeur==null)
	{
		System.out.println("no user session");
		response.sendRedirect("login");
	}else
	{
		System.out.println("Session retreived Employeur:"+employeur.getLogin());
	}

%>

<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
	<meta charset="UTF-8" />
	<meta name=viewport content="width=device-width, maximum-scale=1.0, user-scalable=no" />

	<title>Aventix</title>
	<link href="./assets/css/employeur.css" rel="stylesheet" type="text/css" />
	<link href="" rel="stylesheet" type="text/css" />
	<script type="text/javascript"   src="js/jquery.js"></script>

	
	<!--[if lte IE 8]>
	<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	
	<!--[if lt IE 9]>
	
	<![endif]-->
</head>
<body >

<div id="conteneur" align="center">
	<header >
        <h1 style="text-align: center;" align="center" >La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="880" height="200" />
        <nav id="menu1" >
            <ul id="menu">
                    <li>
                    	<a href="#">Accueil</a>
                    </li>
                    <li>
                        <!--  <a href="#">Mon compte</a> -->
				         <%
				         	out.write("<a href=\"employeur\">Mon compte</a>");
				         %>
                        <ul>
                                <li>
				                    <%
				                    	out.write("<a href=\"employeur?action=listeemploye\">Gerer employe</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"employeur?action=modifier\">Modifer Mon compte</a>");
				                    %>
                                </li>
                                <li><a href="#">Commander des Cartes</a></li>
                                <li>
                                	<a href="#">Lister mes commandes</a>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"employeur?action=modifier\">Créer  employe</a>");
				                    %>
                                </li>
                                <li><a href="#">Recharger des cartes par fichier</a></li>
                        </ul>
                    </li>
                    <li><a href="#">A Propos</a></li>
                    <li><a href="#">Contact</a></li>
                    <li>
	                    <%
	                    	out.write("<a href=\"employeur?action=deconnexion\">Deconnexion</a>");
	                    %>
                    </li>
            </ul>
        </nav>
	</header>
    <h2 style="color:blue;text-align: center;">Bonjour :<% if(employeur!=null){out.write(employeur.getRaisonsocial());} %></h2>
  
	<span style="color: red;text-align: center;">
		<%
			/*String erreur=(String)request.getAttribute("erreur");
			if(erreur!=null)
			{
				out.write(erreur);
			}
			*/
			
		%>
	</span>
	<%
		List<Employe> ListEmploye=(List)request.getAttribute("listEmploye");
		if(ListEmploye==null)
		{
			ListEmploye=new ArrayList<Employe>();
		}
	%>

  	<div id="formul">
            <form id="formulaire" action="" method="post" enctype="" name="" style="height:200px ">
                <fieldset>
                	<legend>Selectionner une periode</legend>
					<span style="color: red;text-align: center;">
						<%
							String erreur=(String)request.getAttribute("erreur");
							if(erreur!=null)
							{
								out.write(erreur);
							}
						%>
					</span>
                    <ol>
                        <li>
                        	<label for="name">De : </label>
                            <input type="date"  name="debut"  id="date"  placeholder="date de fin" required  />                           

                        	<label for="name">à: </label>
                            <input type="date"  name="fin" id="fin" placeholder="date de début" required  />                           
                            
                        </li>
                        <li>
	                        <label for="name">Générer PDF : 
	                        	<input type="checkbox" name="pdf" id="pdf"   />                           
	                        </label>
							<input  type="submit" value="Envoyer" name="envoyer" />
                        </li>
	            	</ol>	                        
            	</fieldset>                                  
            </form>
            
		<div align="center">
			<h1 style="text-align:center;color: blue; ">Mes Commandes</h1>
			<table border="1"  style="text-align: center;" >
				<tr>
						<th>Numero</th>
						<th>Date </th>
						<th>Nombre de carte</th>
						<th>Montant</th>
				</tr>
				<%
				  	SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
					List<Commande> listCommandes=(List<Commande>)request.getAttribute("listCommandes");
					if(listCommandes!=null)
					{
						for(Commande c:listCommandes)
						{
							out.write("<tr>");
							out.println("<td>" + c.getNumcommande() + "</td>");
							out.println("<td>" + dateFormat.format(c.getDatecommande())+ "</td>");
							out.println("<td>" +c.getNbcarte()+ "</td>");
							out.println("<td>" +c.getFacture().getMontant()+ "</td>");
							out.write("</tr>");	
						}
					}
				%>
			</table> 
		</div>
  	</div>
	
	<p style="text-align: center;">
		<a href="employeur?action=creeremploye">Commander des cartes</a>
		<a href="generercommandes">Exporter la liste vers un fichier PDF</a>
	</p>
 </div>
</body>
</html>