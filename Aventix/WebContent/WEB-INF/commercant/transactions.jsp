<%@page import="java.io.File"%>
<%@page import="modele.Commercant"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="modele.Transactions"%>
<%@page import="java.util.List"%>

<%@ page import="java.lang.String" %>
<%@ page import="modele.Employe" %>

<%
	Commercant commercant=(Commercant)request.getSession().getAttribute("commercant");
	if(commercant==null)
	{
		System.out.println("no user session");
		response.sendRedirect("login");
	}else
	{
		System.out.println("Session retreived Commerçant:"+commercant.getLogin());
	}

%>

<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
	<link href="./assets/css/transactionscommercant.css" rel="stylesheet" type="text/css" />
	<link href="" rel="stylesheet" type="text/css" />
	<script type="text/javascript"   src="./assets/js/jquery.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Liste des Transactions</title>
</head>
<body>
<div id="conteneur">
	<header >
        <h1 style="text-align: center;" align="center" >La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="875" height="200" />
        <nav id="menu1" >
            <ul id="menu">
                    <li><a href="#">Accueil</a></li>
                    <li><a href="<%out.write("commercant"); %>">Mon compte</a></li>
                    <li><a href="<%out.write("transactionscommercant"); %>">Mes Transactions</a></li>
                    <li><a href="transactionscommercant?action=deconnexion">Deconnexion</a></li>
                    <li><a href="#">A Propos</a></li>
                    <li><a href="#">Contact</a></li>
            </ul>
        </nav>
	</header>
 
  	<h2 style="color:blue;text-align: center;">Bonjour :<% if(commercant!=null){out.write(commercant.getLogin());} %></h2>
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
						<th>Numero du Lecteur</th>
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
							out.println("<td>" + t.getLecteurcarte().getNumlecteur() + "</td>");
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
					out.write("<a href=\"transactionscommercant?action=genererfichier\">Telecharger le fichier pdf </a>");
				%>
			</p>
		</div>
	
  	</div>
</div>
	
</body>
</html>