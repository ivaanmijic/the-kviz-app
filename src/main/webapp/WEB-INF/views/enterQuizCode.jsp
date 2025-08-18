<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 11:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.tailwindcss.com"></script>
    <%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <script>window.ctx='${pageContext.request.contextPath}'</script>
</head>
<body>
<div class="w-full max-w-md text-center">
    <h1 class="text-5xl font-extrabold text-gray-800 mb-4">The Kviz App</h1>
    <p class="text-xl text-gray-600 mb-8">Unesite PIN da se pridružite kvizu</p>
    <div class="sl-card p-8">
        <input type="text" class="sl-input text-center text-4xl tracking-[1rem] font-bold" placeholder="123456" id="quizId">
        <button class="sl-button sl-button-primary w-full mt-6 !text-lg" id="joinBtn">Pridruži se</button>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/playerWebSocket.js"></script>
</html>
