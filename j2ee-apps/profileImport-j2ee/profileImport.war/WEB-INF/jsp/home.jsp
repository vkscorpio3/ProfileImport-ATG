<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<title>Card Service</title>
</head>
<body>
   <h2>${message}</h2>
   <a href="<c:url value="/create" />">Show profile list</a>
   <a href="<c:url value="/create" />">Import profile list</a>   
</body>
</html>