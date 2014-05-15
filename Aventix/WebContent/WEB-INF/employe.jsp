<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="modele.Employe" %> 
<%@ page import="java.util.ArrayList" %>
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

<H1>!!  Liste des employes !!! </H1>

<%

 ArrayList<Employe> listEmploye = (ArrayList<Employe>) request.getSession().getAttribute("listEmploye");
 out.println("jsp listEmploye nb "+listEmploye.size());
%>
 
<table>
<%
	for (int i = 0; i < listEmploye.size(); i++) 
	{
		Employe employe=listEmploye.get(i);
		out.println("<tr>");
		out.println("<td>"+employe.getLogin()+ "<td>");
		out.println("<td>"+employe.getPassword()+ "<td>");
		out.println("</tr>");
	}

%>
</table>
</body>
</html>