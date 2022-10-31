<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Orders containing the defined product</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="/jspf/header.jspf" %>

<div id="orderForm">
    <form method="post" action="/HilellJavaPro_ShopServlet_war_exploded/ordersHaveDefinedProduct">
        <p><input type="text" name="productName" placeholder="<fmt:message key="ordersHaveHaventDefinedProduct.form.input.placeholder"/>"></p>
        <p><input value="<fmt:message key="form.search"/>" type="submit"></p>
    </form>
</div>
<div id="table">
    <c:if test="${not empty orders}">
        <table>
            <c:forEach items="${orders}" var="order">
                <tr>
                    <td>${order.orderNumber}</td>
                    <td>
                        <c:if test="${not empty order.itemsOnOrder}">
                            <c:forEach items="${order.itemsOnOrder}" var="item">
                                ${item.product.name}
                                ${item.product.description}
                                ${item.product.cost}$
                                ${item.amount}
                                <br>
                            </c:forEach>
                        </c:if>
                    </td>
                    <td>${order.receiptDate}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</div>
</body>
</html>
