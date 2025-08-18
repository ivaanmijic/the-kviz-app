<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 10:36 AM
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
<div class="lobby-main-screen bg-gray-900 text-white">
    <div class="text-center">
        <p class="text-2xl text-gray-300">Pridružite se na</p>
        <p class="text-3xl text-gray-300">PIN Kviz</p>
        <div class="my-6 bg-white/10 text-white rounded-lg p-8 inline-block backdrop-blur-sm border border-white/20">
            <p class="text-8xl font-extrabold tracking-widest pin-screen-glow" id="gameId">
            ${gameId}
            </p>
        </div>
        <div class="mt-8 flex items-center justify-center space-x-4">
            <sl-icon
                    name="people-fill"
                    class="text-4xl text-indigo-300"
            ></sl-icon>
            <p class="text-5xl font-bold" id="playerCount">${joinedPlayers}</p>
            <p class="text-2xl text-gray-300">Igrača Prijavljeno</p>
        </div>
        <sl-button
                variant="success"
                size="large"
                class="mt-12 !text-2xl !px-12 !py-4"
        >
            Započni Kviz!
        </sl-button>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/adminWebSocket.js"></script>
</html>
