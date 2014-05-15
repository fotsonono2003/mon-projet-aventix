<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.File"%>
<%@page import="modele.Carte"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="modele.Employe"%>
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
	<script type="text/javascript">
		/*
		function getMontant()
		{
			return document.write(document.getElementById('montant').value);			
		}
		*/
		
	</script>
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
				                    	//out.write("<a href=\"employeur?action=modifier\">Modifer Mon compte</a>");
			                    		out.write("<a href=\"modifier\">Modifer Mon compte</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"listercommandes\">Lister mes commandes</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"creeremploye\">Créer employé</a>");
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
  
	<h1>Liste des employés</h1>
	<span style="color: red;text-align: center;">
		<%
			String erreur=(String)request.getAttribute("erreur");
			if(erreur!=null)
			{
				out.write(erreur);
			}
			
		%>
	</span>
	<%
		List<Employe> ListEmploye=(List)request.getAttribute("listEmploye");
		if(ListEmploye==null)
		{
			ListEmploye=new ArrayList<Employe>();
		}
		List<Employe>listEmployeCommande=(List<Employe>)request.getSession().getAttribute("listEmployeCommande");
		if(listEmployeCommande==null)
		{
			listEmployeCommande=new ArrayList<Employe>();
		}
	%>
	<div align="right" style="color: blue;" >
		<%
		if(listEmployeCommande!=null)
		{
			if(listEmployeCommande.size()>0)
			{
				out.write("<h4 >Nombre de carte en cours de commande:"+listEmployeCommande.size()+"</h4>");
			}
		}
		%>
	</div>
	<table border="1" style="text-align:center;" >
		<tr>
				<th>Matricule</th>
				<th>Nom</th>
				<th>Prenom</th>
				<th>Date Enregistrement</th>
				<th>Email</th>
				<th>Carte</th>
				<th colspan=4 align="center">Action</th>
			
		</tr>
		<% 	
			for (Employe employe : ListEmploye) 
			{
		%>
				<tr>
					<td><%out.print(employe.getNumemploye()); %></td>
					<td><%out.print(employe.getNom()); %></td>
					<td><%out.print(employe.getPrenom()); %></td>
					<td><%out.print(employe.getDateEnregistrement()); %></td>
					<td><%out.print(employe.getEmail()); %></td>
					<td><%if(employe.getCarte()!=null){out.print(employe.getCarte().getNumerocarte());} %></td>
					<td><a href="employeur?action=supprimer&idemploye=<%out.print(employe.getNumemploye());%>">supprimer</a></td>
					<td>
						<%
							if(employe.getCarte()!=null)
							{
								out.write("<a  href=\"employeur?action=recharger&idemploye="+employe.getNumemploye()+"\" >recharger</a>");
							}
							else
							{
								out.write("");
							}
						%>
					</td>
					<td>
						<%
							if(employe.getCarte()==null)
							{
								if(listEmployeCommande.contains(employe))
								{
									out.write("<a  href=\"employeur?action=annulercommande&idemploye="+employe.getNumemploye()+"\" >retirer la carte de la commande</a>");
								}
								else
								{
									out.write("<a  href=\"employeur?action=commander&idemploye="+employe.getNumemploye()+"\" >Commander une carte</a>");
								}
							}
						%>
					</td>	
					<td>
						<%
							if(employe.getCarte()!=null)
							{
								if(employe.getCarte().isActif()==true)
								{
									out.write("<a  href=\"employeur?action=bloquer&idcarte="+employe.getCarte().getNumerocarte()+"\">bloquer</a>");
								}
								else
								{
									//out.write("<a  href=\"employeur?action=recharger&idemploye="+employe.getNumemploye()+"\" >recharger</a>");
									out.write("<a  href=\"employeur?action=activer&idcarte="+employe.getCarte().getNumerocarte()+" \" >activer</a>");
								}
							}
							else
							{
								out.write("");
							}
						%>
					</td>
				</tr>
		<%
			}
		%>
	</table>  
	<p style="text-align: center;">
		<a href="employeur?action=creeremploye">Créer employé</a>
		<a href="employeur?action=genererfichier">Generer fichier csv</a>
		<a href="employeur?action=validercommande">Valider ma commande</a>
	</p>
 </div>
</body>
</html>