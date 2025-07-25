<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 3:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gretty Demo</title>
    <%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
</head>
<body>
<jsp:include page="WEB-INF/views/sideBar.jsp"></jsp:include>
<div class="main">
    <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
    <ul>
        <li>
            <sl-card class="quiz-card">
                <img slot="image" src="${pageContext.request.contextPath}/css/resources/test-quiz1.jpg">
                This is name of the quiz numero 1
            </sl-card>
        </li>
        <li>
            <sl-card class="quiz-card">
                <img slot="image" src="${pageContext.request.contextPath}/css/resources/test-quiz2.jpg">
                This is name of the quiz numero 2
            </sl-card>
        </li>
    </ul>
</div>
</body>
</html>