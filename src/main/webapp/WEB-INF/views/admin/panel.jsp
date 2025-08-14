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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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

<sl-dialog id="deleteAdminDialog" label="Confirm Deletion">
    <p>Are you sure you want to delete the admin: <strong><span id="deleteAdminUsername"></span></strong>? This action cannot be undone.</p>

    <sl-button slot="footer" id="cancelDeleteBtn">Cancel</sl-button>
    <sl-button slot="footer" variant="danger" id="confirmDeleteBtn">Confirm Delete</sl-button>
</sl-dialog>

<sl-dialog id="descriptionModal" label="Quiz Details">
    <p id="modalDescriptionContent" style="white-space: pre-wrap;"></p>
    <sl-button slot="footer" variant="primary">Close</sl-button>
</sl-dialog>

<sl-dialog id="deleteQuizDialog" label="Delete Quiz">
    <p>
        Are you sure you want to delete the quiz titled
        <strong id="deleteQuizTitle"></strong>?
    </p>
    <p class="text-sm text-neutral-500 mt-4">
        This action cannot be undone. All questions and results associated
        with this quiz will be permanently removed.
    </p>

    <sl-button slot="footer" id="cancelDeleteBtn">Cancel</sl-button>
    <sl-button slot="footer" id="confirmDeleteBtn" variant="danger">Delete</sl-button>
</sl-dialog>

<div class="alert-container">
    <sl-alert id="globalAlert" closeable>
        <sl-icon slot="icon" id="globalAlertIcon"></sl-icon>
        <span id="globalAlertMessage"></span>
    </sl-alert>
</div>

<script src="${pageContext.request.contextPath}/webjars/jquery/3.7.1/jquery.min.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/adminPanelRouter.js"></script>
<script src="${pageContext.request.contextPath}/js/create-quiz.js"></script>
</body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/fragments/footer.jsp"></jsp:include>
</html>
