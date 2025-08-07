<%@ page import="com.example.kviz.model.supporting.AdminRole" %>
<%@ page import="com.example.kviz.model.Admin" %><%--
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
    <title>The Kviz App</title>
    <%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">

    <script>
        window.ctx=`${pageContext.request.contextPath}`

        <%
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin != null) { %>
        window.admin = { id: <%= admin.getId() %> };
        <% } %>

    </script>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/fragments/sidebar.jsp"></jsp:include>
<div class="main">
    <jsp:include page="/WEB-INF/views/fragments/header.jsp"></jsp:include>

    <div id="changeablePart" class="uk-padding-large">
    </div>
</div>

<sl-alert class="alert" id="succesAlert" variant="success" closable></sl-alert>
<sl-alert class="alert" id="errorAlert" variant="danger" closable></sl-alert>

<script src="${pageContext.request.contextPath}/webjars/jquery/3.7.1/jquery.min.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/admin-home.js"></script>
<script src="${pageContext.request.contextPath}/js/create-quiz.js"></script>
</body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/fragments/footer.jsp"></jsp:include>
</html>
