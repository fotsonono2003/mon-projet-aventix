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
        <h1 >La Restauration 2.0</h1>
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
                                <li>
				                    <%
				                    	out.write("<a href=\"listercommandes\">Lister mes commandes</a>");
				                    %>
                                </li>
                                <li>
                                		<a href="employeur?action=creeremploye">Créer employé</a>
				                    <%
				                    	//out.write("<a href=\"employeur?action=modifier\">Créer  employe</a>");
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
	                    	out.write("<a href=\"gestionemploye?action=deconnexion\">Deconnexion</a>");
	                    %>
                    </li>
            </ul>
        </nav>
	</header>
    <h2 style="color:blue;text-align: center;">Bonjour :<% if(employeur!=null){out.write(employeur.getRaisonsocial());} %></h2>
    <span style="color: red;text-align: center;">
	    <%
	    	String erreur=(String)request.getAttribute("erreur");
	    	if(erreur!=null)
	    	{
	    		out.write(erreur);
	    		erreur=null;
	    	}
	    %>
    </span>
            <form id="formulaire"   method="post"  name=""  >
                <fieldset><legend>Créer Nouveau Employé</legend>
                    <ol>
                        <li>
                        	<label for="name">Nom </label>
                            <input type="text" name="nom" id="name" placeholder="Nom" required />   
                            
                            <label for="name">Prenom </label>
                            <input type="text" name="prenom" id="name" placeholder="Prenom" required /> 							
                        	                      	                                                     
                        </li>
                                             
                        <li><label for="email">E-mail</label>
                            <input type="email" name="email" id="emai" placeholder="exemple@donaime.com" required />
                        </li>
                    </ol>
                </fieldset>
                <input type="submit" value="Enregistrer" />
        
            </form> 
  
 </div>
</body>
</html>