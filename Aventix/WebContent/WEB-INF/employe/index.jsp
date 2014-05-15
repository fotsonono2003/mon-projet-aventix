<%@page import="modele.Carte"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	
<%@ page import="java.lang.String" %>
<%@ page import="modele.Employe" %>
<%
	Employe employe=(Employe)request.getSession().getAttribute("employe");
	Carte carte=null;
	if(employe==null)
	{
		System.out.println("no user session");
		response.sendRedirect("login");
	}else
	{
		carte=employe.getCarte();
		System.out.println("Session retreived:"+employe.getLogin());
	}

%>
<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
<meta charset="UTF-8" />
<meta name=viewport content="width=device-width, maximum-scale=1.0, user-scalable=no" />

<title>Employe</title>
<link href="./assets/css/employe.css" rel="stylesheet" type="text/css" />
<link href="" rel="stylesheet" type="text/css" />
<script type="text/javascript"   src="./assets/js/jquery.js"></script>


<!--[if lte IE 8]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<!--[if lt IE 9]>

<![endif]-->

</head>
<body >
<div id="conteneur">
	<header role="banner">
        <h1 >La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="880" height="250" />
        <nav id="menu1" role="navigation">
            <ul id="menu">
                    <li><a href="#">Accueil</a></li>
                    <li>
						<%
							//out.write("<a href=\"employe?action=transactions\">Consulter mes Transactions</a>");
							out.write("<a href=\"employe\">Mon compte</a>");
						%>
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
  				out.write("Connecté en tant que: <a href=\"employe?action=deconnexion\">"+employe.getEmail()+"</a>");
  			}
  			else
  			{
  				out.write("Bonjour: <a href=\"InterfaceEmploye?action=connexion\"></a>");  				
  			}
  		%>
  	</span>
  	<h2 style="color:blue;text-align: center;">Bonjour :<% if(employe!=null){out.write(employe.getPrenom()+" "+employe.getNom());} %></h2>
  	<div id="formul">
            <form id="formulaire" action="employe" method="post"  name="">
                <fieldset>
                	<legend>Profil</legend>
					<span style="color: red;text-align: center;">
						<%
							String erreur=(String)request.getAttribute("erreur");
							if(erreur!=null)
							{
								out.write(erreur);
							}
						%>
					</span>
					<span style="text-align:center;color:green;font-size:18px">
						<%
							String succes=(String)request.getAttribute("succes");
							if(succes!=null)
							{
								out.write(succes);
							}
						%>
					</span>
                    <ol>
                        <li>
                        	<label for="name">Nom : </label>
                            <input type="text"  name="nom" id="nom" placeholder="Nom" required value="<% if(employe!=null){out.write(employe.getNom());} %>" />                           

                        	<label for="name">Prenom: </label>
                            <input type="text"  name="prenom" id="name" placeholder="prenom" required  value="<% if(employe!=null){out.write(employe.getPrenom());} %>"/>                           
	                        <label for="name">Solde : </label>
	                        <input type="text" name="solde" id="solde" disabled="disabled"  value="<% if(employe!=null ){if(employe.getCarte()!=null){out.write(""+employe.getCarte().getSolde());}} %>" />                           

                        	<label for="name">Email: </label>
                            <input type="email"  name="email" id="email" placeholder="email" required value="<% if(employe!=null){out.write(employe.getEmail());} %>" />                           
                            
                        	<label for="name">Login: </label>
                            <input type="text"  name="login" id="login" placeholder="Login" required value="<% if(employe!=null){out.write(employe.getLogin());} %>" />                           

	                        <label for="name">Mot de passe : </label>
	                        <input type="password" name="password" id="password" required  value="<% if(employe!=null ){out.write(""+employe.getPassword());} %>" />                           
	                        
	                        <label for="name">Retapez le Mot de passe : </label>
	                        <input type="password" name="password2" id="password2" required  value="<% if(employe!=null ){out.write(""+employe.getPassword());} %>" />                           

                        </li>
                        <li>
	                       
	                       <label for="name">Numero de carte: </label>
	                        <input type="text" name="numerocarte" disabled="disabled"  id="code" required  value="<% if(carte!=null ){out.write(""+carte.getNumerocarte());} %>" />                           
	                         
	                       
	                        <label for="name">Code de la carte: </label>
	                        <input type="password" name="code" <% if(carte==null){out.write("disabled=\"disabled\" ");}else{out.write("  required  ");} %>  id="code" required  value="<% if(carte!=null ){out.write(""+carte.getPassword());} %>" />                           
	                        
	                        <label for="name">Retapez le code de la carte : </label>
	                        <input type="password" name="code2" id="code2" <% if(carte==null){out.write("disabled=\"disabled\" ");}else{out.write("  required  ");} %>   value="<% if(carte!=null ){out.write(""+carte.getPassword());} %>" />                           
                        </li>
	                    <!--
		                    <li> 
								<a href="">Modifier mon compte</a>
		                    </li>
	                      -->
                        <li>
							<input  type="submit" value="Enregistrer"/>
                        </li>
	            	</ol>	                        
            	</fieldset>                                  
            </form> 
  	</div>
</div>
</body>
</html>