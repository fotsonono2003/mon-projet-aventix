<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
 
<!DOCTYPE html>
<html lang="fr-FR" dir="ltr">
<head>
<meta charset="UTF-8" />
<meta name=viewport content="width=device-width, maximum-scale=1.0, user-scalable=no" />

<title>Aventix</title>
<link href="./assets/css/accueil.css" rel="stylesheet" type="text/css" />
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
	<header role="banner">
        <h1 style="text-align: center;" align="center">La Restauration 2.0</h1>
         <img src="./assets/images/banniere.jpg" alt="paysage" width="880" height="250" />
        <nav id="menu1" role="navigation">
            <ul id="menu">
                    <li><a href="#">Accueil</a></li>
                    <li><a href="#">Services</a></li>
                    <li><a href="#">Entreprises</a></li>
                    <li><a href="#">Partenaires</a></li>
                    <li>
                        <a href="#">Inscription</a>
                        <ul>
                                <li>
				                    <%
				                    	out.write("<a href=\"/Aventix/creercommercant\">Commer�ant</a>");
				                    %>
                                </li>
                                <li>
				                    <%
				                    	out.write("<a href=\"/Aventix/creeremployeur\">Employeur</a>");
				                    %>
                                </li>
                        </ul>
                    </li>
                    <li>
						<%
							out.write("<a href=\"employeur?action=connexion\">Connexion</a>");
						%>
						<!--  <a href="login.jsp">Connexion</a>  -->
                    </li>
                    <li><a href="#">Contact</a></li>
            </ul>
        </nav>
	</header>
  
  	<section>
  		<!--<hgroup>
  			<h1>Sélecteurs CSS2 et CSS3 implémentés</h1>
  		</hgroup>
        -->
  	</section>	

  	<section id="content">
  		<h1>AVENTIX</h1>
  			<div id="entete">
  				<img src="./assets/images/img1.jpg" class="premier" alt="flore paysage" width="80" height="80" />
  				<img src="./assets/images/img2.jpg" alt="paysage" width="80" height="80" />
  				<img src="./assets/images/img3.jpg" alt="paysage" width="80" height="80" />
  				<img src="./assets/images/img4.jpg" alt="flore" width="80" height="80" />
  				<img src="./assets/images/img5.jpg" alt="panorama" width="80" height="80" />
  				<img src="./assets/images/img6.jpg" alt="croix" width="80" height="80" />
  				<img src="./assets/images/img7.jpg" alt="papillon" width="80" height="80" />
  				<img src="./assets/images/img8.jpg" class="dernier" alt="flore" width="80" height="80" />
  			</div>
    		<section id="contenu">
  				
  					<article>
  					<h1>Dolor sit amet</h1>
    					<p title="paragraphe 1" lang="fr" xml:lang="fr">
Consectetur adipiscing elit. <span>Phasellus</span> lacus ante; blandit non pretium ac, semper nec tortor. Vivamus urna massa, rutrum eget luctus vitae, iaculis ac quam! Cras diam augue, vehicula vel commodo sed, interdum dictum nibh. Aliquam et placerat purus.
    					</p>
    					<p title ="paragraphe 2" lang="en-fr" xml:lang="en-fr">
Aenean sit amet purus sit amet lectus pharetra consectetur. Curabitur vel sem quis ipsum gravida porta nec eget mi? Morbi laoreet magna non magna malesuada varius? Maecenas venenatis vestibulum felis, vel fermentum lectus sagittis sed.
						</p>
    					<p title="paragraphe 3">
In ornare feugiat sem, sollicitudin imperdiet leo tristique vitae? Nulla facilisi. Donec elementum commodo leo nec dignissim. Praesent dictum elementum lacinia.
						</p>
    				</article>
    				<article class="conseils"><!--CONSEIL 1-->
      					<h2>Conseils</h2>
      					<p class="conseil">
Suspendisse potenti. Nullam purus metus, suscipit ac sodales non, convallis a enim. Morbi et sapien neque. Maecenas mattis erat vel ante tincidunt vitae varius odio vestibulum. Morbi sed risus ut enim feugiat varius. Mauris pellentesque tempor lobortis.
						</p>
					</article><!-- FIN CONSEIL 1-->
    		</section><!--FIN SECTION 1-->  
 

			<section>
    			
    				<article><h1>Curabitur ullamcorper</h1>
    					<p>
Mattis sem in volutpat. Maecenas ac urna nisl; ut tempor neque. Sed dignissim justo ac nisl facilisis malesuada. Aliquam dui tellus, mattis sed volutpat non, sagittis a justo. Fusce magna erat, rhoncus cursus tincidunt eget, porta et urna.
						</p>
    					<p class="autre">
Nullam cursus dui vel arcu suscipit in luctus quam luctus. Nulla facilisi. Etiam turpis massa, iaculis vitae euismod pharetra, rhoncus quis mauris! Quisque sit amet cursus arcu. Ut at nisl ligula. Morbi scelerisque tortor eget nunc lacinia lacinia.
						</p>
					</article>
					<article class="conseils"><!--CONSEIL 2-->
      					<h2>Conseils</h2>
      					<p class="conseil">
Suspendisse potenti. Nullam purus metus, suscipit ac sodales non, convallis a enim. Morbi et sapien neque. Maecenas mattis erat vel ante tincidunt vitae varius odio vestibulum. Morbi sed risus ut enim feugiat varius. Mauris pellentesque tempor lobortis.
						</p>
    				</article><!-- FIN CONSEIL 2-->
    		</section><!--FIN SECTION 2-->
    	</section>
    		<aside>
  	<div id="renseignements">
    	<form id="form1" method="post" action="#">
	    	<ul>
	    	<span>
	    	<%
	    		/*code d'affichage des errreurs*/
	    	%>
	    	</span>
	    		<li>
	      		<label for="log">Login :</label>
	      		<input name="login" type="text"  id="login"  placeholder="login" required />
	      		</li>
	      		<li>
	      		<label for="password">Pwd : </label>
	      		<input name="password" type="text" placeholder="mot de passe" id="password"  required />
	      		</li>
	      		<li>
					Votre statut
					<select name="statut"> 
					   <option selected="selected">Employe</option> 
					   <option>Employeur</option> 
					   <option>Commer�ant</option> 
					</select> 
	      		</li>
	      		<li>
	      		<input name="envoyer" type="submit" />
	      		</li>
	      	</ul>
    	</form>
  	</div>
    			<h1> Maecenas</h1>
    			<p>
Aliquam auctor dapibus. Fusce non neque non sem sagittis mattis. Aenean rhoncus placerat nisi nec sodales. Mauris nec nunc eu leo ultrices aliquet eu a mauris. Sed eleifend egestas tristique! Nam tempor bibendum enim, eget gravida orci pretium eget.
				</p>
    			<p>
<span>Suspendisse po</span>tenti. Nullam purus metus, suscipit ac sodales non, convallis a enim. Morbi et sapien neque. Maecenas mattis erat vel ante tincidunt vitae varius odio vestibulum. Morbi sed risus ut enim feugiat varius. Mauris pellentesque tempor lobortis.</span>
				</p>
  
  </aside>
  
  <br class="annule" />
  
  	<footer>	
  	<p id="footer">© AVENTIX - 2014</p>
  	</footer>
</div>
</body>
</html>