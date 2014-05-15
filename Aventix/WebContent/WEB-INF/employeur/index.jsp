<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.lang.String" %>
<%@ page import="modele.Employeur" %>
<%
	Employeur employeur=(Employeur)request.getSession().getAttribute("employeur");
	if(employeur==null)
	{
		System.out.println("no user session");
		//response.sendRedirect("login");
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
<body role="document">
<div id="conteneur">
	<header >
        <h1 style="text-align: center;" >La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="880" height="200" />
        <nav id="menu1" role="navigation">
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
                                <li>
                                	<!--<a href="#">Lister mes commandes</a>  -->
				                    <%
				                    	out.write("<a href=\"listercommandes\">Lister mes commandes</a>");
				                    %>
                                </li>
                                <li>
                                	<!-- <a href="#">Créer une liste d'employes</a> -->
				                    <%
				                    	out.write("<a href=\"creerlisteemploye\">Créer une liste d'employes</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"rechargerfichier\">Recharger des cartes par fichier</a>");
				                    %>
                                </li>
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
  
   	<section id="content">
	
		<article>
            <form id="formulaire" action="" method="post" enctype="" name="formulaire">
                <fieldset><legend>Mon compte</legend>
                    <ol>
                        <li>
                        	<label for="name">Raison Sociale : </label>
                            <input type="text" name="raisonsociale" id="raisonsociale" placeholder="Raison Sociale" required value="<% if(employeur!=null){out.write(employeur.getRaisonsocial());} %>" />                           
                        	
                        	<label for="name">Adresse : </label>
                            <input type="text" name="adresse" id="adresse" placeholder="Adresse de l'entreprise" required value="<% if(employeur!=null){out.write(employeur.getAdresse());} %>" />                           
                        	
                        	<label for="name">Email : </label>
                            <input type="email" name="email" id="email" placeholder="adresse@mondomaine.domaine" required  value="<%if(employeur!=null){out.write(employeur.getEmail());} %>"  />                           

                        	<label for="name">Login : </label>
                            <input type="text" name="login" id="raison" placeholder="login" value="<% if(employeur!=null){ out.write(employeur.getLogin()); } %>"  required />                           

                        	<label for="name">Mot de passe : </label>
                            <input type="password" name="password" id="password" placeholder="password" required value="<%if(employeur!=null){out.write(employeur.getPassword());} %>" />                           
                            
                        	<label for="name">Mot de passe : </label>
                            <input type="password" name="password2" id="password2" placeholder="password" required value="<%if(employeur!=null){out.write(employeur.getPassword());} %>" />                           
                        </li>
                      
                    </ol>
                
            	</fieldset>                
            </form> 
			
		</article>
	</section>
  
 </Div>
</body>
</html>