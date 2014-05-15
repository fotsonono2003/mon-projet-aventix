<%@page import="modele.Commercant"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
<meta charset="UTF-8" />
<meta name=viewport content="width=device-width, maximum-scale=1.0, user-scalable=no" />

<title>Aventix</title>
<link href="./assets/css/commercant.css" rel="stylesheet" type="text/css" />
<link href="" rel="stylesheet" type="text/css" />
<script type="text/javascript"   src="js/jquery.js"></script>


<!--[if lte IE 8]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<!--[if lt IE 9]>

<![endif]-->

</head>
<body >
<div id="conteneur">
	<header >
        <h1 style="text-align: center;" align="center" >La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="875" height="200" />
        <nav id="menu1" >
            <ul id="menu">
                    <li><a href="#">Accueil</a></li>
                    <li><a href="<%out.write("commercant"); %>">Mon compte</a></li>
                    <li><a href="<%out.write("transactionscommercant");%>">Mes Transactions</a></li>
                    <li><a href="transactionscommercant?action=deconnexion">Deconnexion</a></li>
                    <li><a href="#">A Propos</a></li>
                    <li><a href="#">Contact</a></li>
            </ul>
        </nav>
	</header>
  
  	<section id="content">
	
		<article>
        <h1>Profil commerçant</h1>
            <form id="formulaire" action="" method="post"  name="">
            
                <fieldset><legend>Renseignements</legend>
					<span style="color: red;text-align: center;">
						<%
							String erreur=(String)request.getAttribute("erreur");
							if(erreur!=null)
							{
								out.write(erreur);
							}
						%>
					</span>
					<span style="color:blue;text-align:center;font-size:20px">
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
                        	<label for="name">Raison Sociale : </label>
                            <input type="text" name="raison" id="raison" value="<%if(commercant!=null){ out.write(commercant.getRaisonSociale()); } %>" placeholder="Raison Sociale" required />                           
                        </li>
                        <p>
                      
                        <li><label for="email">E-mail</label>
                            <input type="email" name="email" id="email"  value="<%if(commercant!=null){ out.write(commercant.getEmail()); } %>"  placeholder="exemple@donaime.com" required />
                        </li>
                        
                       	<li><label for="tel">Téléphone : </label>
                            <input type="tel" name="tel" id="tel" value="<%if(commercant!=null){ out.write(commercant.getTelephone()); } %>" placeholder="Numero de téléphone" required />
                        </li>
                        
                        <li><label for="adresse">Adresse : </label>
                            <input type="text" name="adresse" id="adresse" value="<%if(commercant!=null){ out.write(commercant.getAdresse()); } %>" placeholder="Adresse + code postal" required />
                        </li>
                        
                        <li><label for="rib">RIB : </label>
                            <input type="number" name="rib" value="<%if(commercant!=null){ out.write(""+commercant.getRib()); } %>" id="rib" placeholder="RIB" required  />
                        </li>
                    </ol>
                </fieldset>
                
                <fieldset><legend>Login et mot de passe</legend>
                    <ol>
                        <li>
                            <Label for="identifiant">Identifiant</Label>
                            <input type="text" required id="login" name="login" value="<%if(commercant!=null){ out.write(commercant.getLogin()); } %>" placeholder="Votre nom d'identifiant" />       
                        </li>
                        
                       <li>
                            <Label for="motdepasse">Mot de passe:</Label>
                            <input type="password" required id="password" name="password" value="<%if(commercant!=null){ out.write(commercant.getPassword()); } %>" placeholder="mot de passe" />       
                        </li>
                       <li>
                            <Label for="password2">Re-tapez le mot de passe:</Label>
                            <input type="password" name="password2" required id="password2" value="<%if(commercant!=null){ out.write(commercant.getPassword()); } %>"  placeholder="mot de passe"/>       
                        </li>
                   </ol>
                
                </fieldset>
                
                <input type="submit" value="Enregistrer" />
        
            </form> 
			
		</article>
	</section>
  
</div>
</body>
</html>