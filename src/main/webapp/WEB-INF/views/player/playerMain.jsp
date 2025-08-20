<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 4:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.tailwindcss.com"></script>
    <%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <script>window.ctx='${pageContext.request.contextPath}'</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/playerHeader.jsp"></jsp:include>
<div id="body">

</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/xlsx@0.18.5/dist/xlsx.full.min.js"></script>
<script type="module" src="${pageContext.request.contextPath}/js/playerScreenInitialization.js"></script>
</html>
