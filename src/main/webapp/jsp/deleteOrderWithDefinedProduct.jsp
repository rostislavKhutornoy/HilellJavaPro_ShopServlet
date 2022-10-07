<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Delete order with defined product</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<c:import url="/jspf/header.jspf" />
<div id="orderForm" class="task2">
    <form method="post" action="/HilellJavaPro_ShopServlet_war_exploded/deleteOrderWithDefinedProduct">
        <p><input type="text" name="productName" placeholder="Enter product name"></p>
        <p><input type="text" name="amount" placeholder="Enter amount"></p>
        <p><input value="Delete" type="submit"></p>
    </form>
</div>
<c:if test="${not empty paramValues}">
<p id="message">
    ${message}
</p>
</c:if>
</body>
</html>
