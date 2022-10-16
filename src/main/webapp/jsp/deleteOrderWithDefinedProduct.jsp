<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Delete order with defined product</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="/jspf/header.jspf" %>

<div id="orderForm" class="task2">
    <form method="post" action="/HilellJavaPro_ShopServlet_war_exploded/deleteOrderWithDefinedProduct">
        <p><input type="text" name="productName" placeholder="Enter product name"></p>
        <p><input type="text" name="amount" placeholder="Enter amount"></p>
        <p><input value="Delete" type="submit"></p>
    </form>
</div>
<c:if test="${not empty paramValues}">
<p id="message">
    <c:if test="${not empty result}">
        <c:choose>
            <c:when test="${result eq true}">
                Successfully deleted
            </c:when>
            <c:otherwise>
                Nothing to delete
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:if test="${not empty errorMessage}">
        ${errorMessage}
    </c:if>
</p>
</c:if>
</body>
</html>
