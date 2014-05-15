<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ page import="java.util.ArrayList" %>
	<%@ page import="java.lang.String" %>

<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
<meta charset="UTF-8" />
<meta name=viewport content="width=device-width, maximum-scale=1.0, user-scalable=no" />

<title>Aventix</title>
<link href="./assets/css/bootstrap.min.css" rel="stylesheet">
<link href="./assets/css/signin.css" rel="stylesheet">
<link href="./assets/css/connexion.css" rel="stylesheet" type="text/css" />
<link href="" rel="stylesheet" type="text/css" />
<script type="text/javascript"   src="js/jquery.js"></script>


<!--[if lte IE 8]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<!--[if lt IE 9]>

<![endif]-->

</head>
<body >
<div id="conteneur" style="text-align: center;"  align="center">
	<header role="banner">
        <h1 >La Restauration 2.0</h1>
         <img align="top" src="./assets/images/banniere.jpg" alt="paysage" style="text-align:center;border:10px " width="860" height="250" />
        <nav id="menu1" role="navigation"> 
            <ul id="menu">
                    <li><a href="accueil.jsp">Accueil</a></li>
                    <li><a href="#">Services</a></li>
                    <li><a href="#">Entreprises</a></li>
                    <li><a href="#">Partenaires</a></li>
                    <li>
                        <a href="#">Inscription</a>
                        <ul>
                                <li>
				                    <%
				                    	out.write("<a href=\"creeremployeur?\">Employeur</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"/Aventix/creercommercant\">Commerçant</a>");
				                    %>
                                </li>
                        </ul>
                    </li>
                    <li><a href="#">Connexion</a></li>
                    <li><a href="#">Contact</a></li>
            </ul>
        </nav>
	</header>
	
	<div  style="text-align: center;" align="center">
		<form class="form-signin" method="post"   >
			<h2 class="form-signin-heading" style="color:silver;"   >Login & mot de passe</h2>
	    	<span>
	    	<%
	    		String erreur= (String)request.getAttribute("erreur");
	    		if(erreur!=null)
	    		{
	    			System.out.println("*/*/*/*/*/*/*/*/*/*/*/*/*/*/");
	    			out.println("<h4 style=\"color:red; \">"+erreur+ "</h4>");
	    			erreur=null;
	    		}	    		
	    	%>
	    	</span>
			<input type="text"  class="form-control" placeholder="login" name="login" required  autofocus> 
			<input type="password" class="form-control" placeholder="mot de passe" name="password" required> 
			<label class="checkbox"> 
				Votre statut
				<select name="statut"> 
				   <option selected="selected" value="employe">Employe</option> 
				   <option value="employeur">Employeur</option> 
				   <option value="commercant">Commerçant</option> 
				</select> 
			</label>
			<button class="btn btn-lg btn-primary btn-block" type="submit">
				Connexion
			</button>
		</form>

	</div>
	<!-- /.container -->
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://code.jquery.com/jquery.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="./assets/js/bootstrap.min.js"></script>
	
</div>
  
</body>
</html>