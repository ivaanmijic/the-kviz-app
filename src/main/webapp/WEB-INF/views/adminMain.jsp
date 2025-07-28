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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <script>window.ctx='${pageContext.request.contextPath}'</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/sidebar.jsp"></jsp:include>
<div class="main">
    <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/views/myQuizzes.jsp"></jsp:include>
</div>
<script src="${pageContext.request.contextPath}/js/adminMainPage.js"></script>
</body>
</html>
