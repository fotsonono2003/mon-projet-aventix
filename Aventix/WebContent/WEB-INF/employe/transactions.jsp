<%@page import="java.text.SimpleDateFormat"%>
<%@page import="modele.Transactions"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="java.lang.String" %>
<%@ page import="modele.Employe" %>

<%
	Employe employe=(Employe)request.getSession().getAttribute("employe");
	if(employe==null)
	{
		System.out.println("no user session");
		response.sendRedirect("login");
	}
	else
	{
		System.out.println("Session retreived:"+employe.getLogin());
	}

%>

<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
	<link href="./assets/css/employe.css" rel="stylesheet" type="text/css" />
	<link href="" rel="stylesheet" type="text/css" />
	<script type="text/javascript"   src="./assets/js/jquery.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Liste des Transactions</title>
</head>
<body>
<div id="conteneur">
	<header role="banner">
        <h1 style="text-align: center;">La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="880" height="250" />
        <nav id="menu1" role="navigation">
            <ul id="menu">
                    <li><a href="#">Accueil</a></li>
                    <li>
	                    <%
			             	out.write("<a href=\"employe\">Mon Compte</a>");
	                    %>
                        <!--  <a href="#">Mon compte</a> -->
                        <ul>
                                <li>
				                    <%
				                    	//out.write("<a href=\"employe?action=transactions\">Consulter mes Transactions</a>");
			                    		out.write("<a href=\"employe?action=transactions\">Consulter mes Transactions</a>");
				                    %>
                                </li>
                        </ul>
                    </li>
                    <li><a href="#">A Propos</a></li>
                    <li><a href="#">Contact</a></li>
                    <li>
	                    <%
	                    	out.write("<a href=\"employe?action=deconnexion\">Deconnexion</a>");
	                    %>
                    	<!-- <a href="#">Deconnexion</a> -->
                    </li>
            </ul>
        </nav>
	</header>

  	<span>
  		<%
  			if(employe!=null)
  			{
  				out.write("Connecté en tant que: <a href=\"employe?action=deconnexion\">"+employe.getLogin()+"</a>");
  			}
  			else
  			{
  				out.write("Bonjour: <a href=\"employe?action=connexion\"></a>");  				
  			}
  		%>
  	</span>
  	<h2 style="color:blue;text-align: center;">Bonjour :<% if(employe!=null){out.write(employe.getPrenom()+" "+employe.getNom());} %></h2>
  	<div id="formul">
            <form id="formulaire" action="" method="post" enctype="" name="">
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
			<h1 style="text-align:center;color: blue; ">Mes transactions</h1>
			<table border="1"  style="text-align: center;" >
				<tr>
						<th>Numero</th>
						<th>Commerçant</th>
						<th>Adresse</th>
						<th>Montant</th>
						<th>Date </th>
				</tr>
				<%
				  SimpleDateFormat dateFormat =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
					List<Transactions> listTransactions=(List<Transactions>)request.getAttribute("listTransactions");
					if(listTransactions!=null)
					{
						for(Transactions t:listTransactions)
						{
							out.write("<tr>");
							out.println("<td>" + t.getNumtransaction() + "</td>");
							out.println("<td>" + t.getLecteurcarte().getCommercant().getRaisonSociale() + "</td>");
							out.println("<td>" + t.getLecteurcarte().getCommercant().getAdresse() + "</td>");
							out.println("<td>" + t.getMontant() + "</td>");
							out.println("<td>" +dateFormat.format(t.getDateheuretransaction())+ "</td>");
							out.write("</tr>");				
						}
					}
					else
					{
						//System.out.println();
					}
				%>
			</table> 
		
			<p  align="center">
				<% 
					out.write("<a href=\"employe?action=retour\">Retour</a>");
				%>
			<p  align="center">
				<%
					out.write("<a href=\"transactionsemploye?action=afficherfichier\">Telecharger le fichier pdf </a>");
				%>
			</p>
			</p>
		</div>
	
  	</div>
</div>
	
</body>
</html>