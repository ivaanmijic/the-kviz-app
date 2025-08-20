<%--
  Created by IntelliJ IDEA.
  User: ivan
  Date: 8/19/25
  Time: 10:21 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>The Kviz App</title>

    <script src="https://cdn.tailwindcss.com"></script>
    <%@include file="./WEB-INF/views/shoelace-imoprt.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <style>
        body {
            background: url('${pageContext.request.contextPath}/assets/images/background.svg') no-repeat center center fixed;
            background-size: cover;
        }
    </style>
</head>
<body class="min-h-screen flex items-center justify-center">
<div class="glass p-10 max-w-lg w-full text-center">
    <img src="${pageContext.request.contextPath}/assets/logo.png" alt="The Kviz App Logo" class="mx-auto h-24 mb-6" />
    <h1 class="text-4xl font-extrabold mb-2">Welcome to</h1>
    <h1 class="text-4xl font-extrabold mb-2">The Kviz App</h1>
    <p class="mb-8">Have fun and test your knowledge!</p>
    <div class="space-y-4">
        <sl-button variant="primary" size="large" class="w-full" href="${pageContext.request.contextPath}/playerMainPage">
            🎮 Play the quiz
        </sl-button>
        <sl-button variant="success" size="large" class="w-full" href="${pageContext.request.contextPath}/admin/panel">
            ⚙️ Managing and hosting quizzes (Admin) 📊
        </sl-button>
    </div>
</div>
</body>
</html>
