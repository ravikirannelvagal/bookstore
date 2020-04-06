<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
        <%
        Object user = session.getAttribute("user");
        if (user == null || "".equals(user.toString())) { %>
    <div style="color: red">
        <c:out value="Please login"/>
        <a href="/"> here</a>
    </div>
        <% }else{ %>
<h3>Book details</h3>
<table>
    <tr>
        <td>
            <img src="${bookDetail.book.image}" height="150" width="150"/>
        </td>
        <td>
            <table>
                <tr>
                    <td><strong>${bookDetail.book.name}</strong></td>
                </tr>
                <tr>
                    <td><fmt:formatNumber type="number" maxFractionDigits="2" value="${bookDetail.book.price}"/> &euro;</td>
                </tr>
                <tr>
                    <td>${bookDetail.book.details}</td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div style="padding-bottom: 20px"/>
<h4>Customers were also interested in:</h4>
<hr>
<table border="0" width="70%">
    <c:forEach var="book" items="${bookDetail.similarObjects}">
        <tr>
            <td><img src="${book.image}" height="100" width="100"/></td>
            <td>
                <table border="0" width="100%">
                    <tr>
                        <td>${book.name}</td>
                    </tr>
                    <tr>
                        <td><fmt:formatNumber type="number" maxFractionDigits="2" value="${book.price}"/> &euro;</td>
                    </tr>
                </table>
            </td>
            <td>
                <form:form method="get" action="details">
                    <input type="hidden" value="${book.id}" name="id">
                    <input style="background-color: #2396ff; color: white" type="submit" value="More >>" />
                </form:form>
            </td>
        </tr>
    </c:forEach>
</table>
<div style="padding-bottom: 30px"/>
<a href="/task/books">Home</a>
</div>
<%}%>
<br/>