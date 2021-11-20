<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table>
<tr>
<th>Nombre</th>
<th>Edad</th>
<th>Titulación</th>
</tr>
<c:forEach items="${nombres}" var="nombre" varStatus="status">
	<tr>
	<td>${nombre}</td>
	<td>${edades[status.index]}</td>
	<td>${titulaciones[status.index]}</td>
	</tr>
</c:forEach>
</table>
<a href="index.jsp">Index</a>
</body>
</html>