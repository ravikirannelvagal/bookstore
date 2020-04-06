<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <title>Bookstore</title>
</head>
<body>
<h1>Welcome to the bookstore!</h1>
<form:form method="post" action="login">
    <table>
        <tr>
            <td>Username:</td>
            <td><form:input path="username"/></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><form:password path="password"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Login"/></td>
        </tr>
    </table>
</form:form>
<div>
    <%
        Object status = session.getAttribute("loginStatus");
        if (status != null && "FAILED".equalsIgnoreCase(status.toString())) { %>
    <div style="color: red">
        <c:out value="${sessionScope.loginMessage}"/>
    </div>
    <% }%>
</div>
</body>
</html>