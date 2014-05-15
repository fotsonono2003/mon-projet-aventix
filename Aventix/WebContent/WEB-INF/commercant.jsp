<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
<meta charset="UTF-8" />
<meta name=viewport content="width=device-width, maximum-scale=1.0, user-scalable=no" />

<title>Aventix</title>
<link href="./assets/css/creer_commercant.css" rel="stylesheet" type="text/css" />
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
        <h1 >La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="880" height="250" />
        <nav id="menu1" >
            <ul id="menu">
                    <li><a href="#">Accueil</a></li>
                    <li><a href="#">Services</a></li>
                    <li><a href="#">Entreprises</a></li>
                    <li><a href="#">Partenaires</a></li>
                    <li>
                        <a href="#">Inscrption</a>
                        <ul>
                                <li>
				                    <%
				                    	out.write("<a href=\"creercommercant\">Commerçant</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"creeremployeur\">Employeur</a>");
				                    %>
                                </li>
                        </ul>
                    </li>
                    <li>
						<%
							out.write("<a href=\"employeur?action=connexion\">Connexion</a>");
						%>
                    </li>
                    <li><a href="#">Contact</a></li>
            </ul>
        </nav>
	</header>
  
  	<section id="content">
	
		<article>
        <h1>Inscription Commerçant </h1>
            <form id="formulaire"  method="post"  name="">
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
                    <ol>
                        <li>
                        	<label for="name">Raison Sociale : </label>
                            <input type="text" name="raison" id="raison" placeholder="Raison Sociale" required />                           
                        </li>
                        
                        <li><label for="email">E-mail</label>
                            <input type="email" name="email" id="email" placeholder="exemple@donaime.com" required />
                        </li>
                        
                        <li><label for="adresse">Adresse : </label>
                            <input type="text" name="adresse" id="adresse" placeholder="Adresse + code postal" required />
                        </li>
                        
                        <li><label for="tel">Tel : </label>
                            <input type="number" name="tel" id="tel" placeholder="telephone" required />
                        </li>
                        <li><label for="tel">RIB : </label>
                            <input type="number" name="rib" id="rib" placeholder="RIB" required />
                        </li>
                    </ol>
                </fieldset>
                
                <fieldset><legend>Login et mot de passe</legend>
                    <ol>
                        <li>
                            <Label for="identifiant">Identifiant</Label>
                            <input type="text" required id="login" name="login" placeholder="Votre nom d'identifiant" />       
                        </li>
                        
                       <li>
                            <Label for="motdepasse">Mot de passe:</Label>
                            <input type="password" required id="password" name="password" placeholder="mot de passe" />       
                        </li>
                       <li>
                            <Label for="password2">Re-tapez le mot de passe:</Label>
                            <input type="password"  required id="password2" name="password2" placeholder="mot de passe"/>       
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
