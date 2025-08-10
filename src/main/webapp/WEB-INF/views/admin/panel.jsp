<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 3:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.kviz.model.supporting.AdminRole" %>
<%@ page import="com.example.kviz.model.Admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.tailwindcss.com"></script>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;900&display=swap"
      rel="stylesheet"
    /> 

    <title>The Kviz App</title>
    <%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">

    <script>
        window.ctx=`${pageContext.request.contextPath}`

        <%
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin != null) { %>
        window.admin = { id: <%= admin.getId() %>, username: "<%=admin.getUsername()%>" };
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

<script src="${pageContext.request.contextPath}/webjars/jquery/3.7.1/jquery.min.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/adminPanelRouter.js"></script>
<script src="${pageContext.request.contextPath}/js/create-quiz.js"></script>
</body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/fragments/footer.jsp"></jsp:include>
</html>
