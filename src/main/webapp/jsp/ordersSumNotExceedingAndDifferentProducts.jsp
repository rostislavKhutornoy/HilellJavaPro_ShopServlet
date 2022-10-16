<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Orders which sum not exceeding the set sum
        and number different products is equal the set product amount</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="/jspf/header.jspf" %>

<div id="orderForm" class="task2">
    <form method="post" action="/HilellJavaPro_ShopServlet_war_exploded/ordersSumNotExceedingAndDifferentProducts">
        <p><input type="text" name="sum" placeholder="Enter max total cost"></p>
        <p><input type="text" name="differentProducts" placeholder="Quantity different products"></p>
        <p><input value="Search" type="submit"></p>
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
