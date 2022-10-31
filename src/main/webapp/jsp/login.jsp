<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<%@ include file="/jspf/header.jspf" %>
<div id="orderForm">
<form method="post" action="/HilellJavaPro_ShopServlet_war_exploded/login">
    <p><input type="text" name="login" placeholder="<fmt:message key="login.form.placeholder.login"/>"></p>
    <p><input type="text" name="password" placeholder="<fmt:message key="login.form.placeholder.password"/>"></p>
    <p><input value="<fmt:message key="header.login"/>" type="submit"></p>
</form>
</div>
<c:if test="${errorMessage eq true}">
<p id="message">
    <fmt:message key="errorLoginMessage"/>
</p>
</c:if>

</body>
</html>
