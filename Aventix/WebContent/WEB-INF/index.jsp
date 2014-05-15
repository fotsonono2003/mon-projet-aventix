<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	<%@ page import="java.util.ArrayList" %>
	<%@ page import="java.lang.String" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title>

<!-- Bootstrap -->
<link href="./assets/css/bootstrap.min.css" rel="stylesheet">
<link href="./assets/css/signin.css" rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Projet Aventix</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#">Accueil</a></li>
					<li><a href="#about">Services</a></li>
					<li><a href="#contact">Entreprises</a></li>
					<li><a href="#contact">Partenaires</a></li>
					<li><a href="#contact">Connexion</a></li>
					<li><a href="#contact">Inscrption</a></li>
					<li><a href="#contact">Aide</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>

	<div class="container">
		<div class="errorLogin">
					<span>	
							  
					<%
					
					 ArrayList<String> listErrors = (ArrayList<String>) request.getSession().getAttribute("errors");
					if(listErrors !=null ){
						
						for (int i = 0; i < listErrors.size(); i++) {
							String error=listErrors.get(i);
							
							out.println("<H1>"+error+ "<H1>");
							
							
						}
						
					}
					
					%>	
					</span>		  
		
		</div>
		
		<form class="form-signin" role="form" method="post">
			<h2 class="form-signin-heading">Connectez-vous</h2>
			<input type="email" class="form-control" placeholder="Email address" name="email"  autofocus> 
			<input type="password" class="form-control" placeholder="Password" name="password" required> 
			<label class="checkbox"> 
				Votre statut
				<select name="statut"> 
				   <option selected="selected">Employe</option> 
				   <option>Employeur</option> 
				   <option>Commerçant</option> 
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
</body>
</html>