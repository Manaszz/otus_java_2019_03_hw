<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation='"http://www.w3.org/1999/xhtml\ '>
<head>
<meta charset=\"UTF-8\"/>
<title>Adminka</title>
</head>
<body>
<h2>adminka</h2>

<c:forEach items="${users}" var="usr">
    <p>${usr}</p>
</c:forEach>
<p> Security info page:  </p>
<p>Create user:</p>
<form action='/AdmData/create' method="POST">
    Login: <input type="text" name="login"/> <BR>
    Password: <input type="password" name="pass"/><BR>
    Role: <input type="text" name="role"/><BR>
    <input type="submit" value="Create"><BR>
</form>
<Div><a href = /infoPage.html> Список пользователей </a></div>
</body>
</html>
