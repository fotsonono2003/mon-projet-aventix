<%@page import="modele.Carte"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="modele.Employe"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ page import="java.lang.String" %>
<%@ page import="modele.Employeur" %>

<%
	Employeur employeur=(Employeur)request.getSession().getAttribute("employeur");
	Employe employe=(Employe)request.getSession().getAttribute("employe");
	System.out.println("--------Employe selectionné:-------::::"+employe);

	Carte carte=new Carte();
	carte=(Carte)request.getSession().getAttribute("carte");
	System.out.println("--------carte selectionné:-------::::"+carte);
	if(employeur==null)
	{
		System.out.println("no user session");
		response.sendRedirect("login");
	}else
	{
		if(employe!=null)
		{
			carte=employe.getCarte();
			//System.out.println("--------carte selectionné iterf recharger carte:-------::::"+carte);
		}
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
                <fieldset><legend>Recharger la carte de: <% if(employe!=null) out.write(employe.getLogin());%></legend>
                    <ol>
                        <li>
                        	<label for="name">Nom </label>
                            <input  type="text" name="nom" value="<%if(employe!=null) out.write(employe.getNom()); %>" id="name" placeholder="Nom"  disabled="disabled"  />   
                            
                            <label for="name">Prenom </label>
                            <input type="text" value="<% if(employe!=null) out.write(employe.getPrenom()); %>"  name="prenom" id="name" placeholder="Prenom" disabled="disabled"  /> 							
                        </li>
                                             
                        <li>
                        	<label for="email">E-mail</label>
                            <input type="email" value="<% if(employe!=null) out.write(employe.getEmail()); %>"  name="email" id="emai" disabled="disabled" />
                        </li>
                        <li>
                        	<label for="email">Login</label>
                            <input type="text" value="<% if(employe!=null) out.write(employe.getLogin()); %>" name="login" id="login" disabled="disabled"  />
                        </li>
                        <li>
                        	<label for="num">Numero de la carte</label>
                            <input type="text" value="<% if(carte!=null) out.write(""+carte.getNumerocarte()); %>" disabled="disabled" name="numcarte" id="numm" autofocus="autofocus" required />
                        </li>
                        <li>
                        	<label for="email">Montant à recharger</label>
                            <input type="text" name="montant" id="montant" autofocus="autofocus" required />
                        </li>
                    </ol>
                </fieldset>
                <input type="submit" value="Enregistrer" />
        
            </form>   
 </div>
</body>
</html>