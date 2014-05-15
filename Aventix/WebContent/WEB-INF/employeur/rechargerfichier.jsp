<%@page import="java.nio.file.Path"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
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
                                <a href="#">Commander des Cartes</a>
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
            <form id="formulaire" action="rechargerfichier" method="post" enctype="multipart/form-data"   >
                <fieldset><legend>Recharger les cartes des employés</legend>
                    <ol>
                        <li>
                        	<label for="email">Uploader le fichier contenant la liste des employes</label>
                            <input type="file"  name="fichier" id="fichier" required />
                        </li>
                    </ol>
                </fieldset>
                <input type="submit" value="Enregistrer" />
        
            </form>   
 </div>
</body>
</html>