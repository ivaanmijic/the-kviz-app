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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <script>window.ctx='${pageContext.request.contextPath}'</script>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/fragments/playerHeader.jsp"></jsp:include>
<div class="lobby-main-screen bg-gray-900 text-white">
    <div class="text-center" id="replaceable">
        <p class="text-2xl text-gray-300">Join the quiz with</p>
        <p class="text-3xl text-gray-300">PIN</p>
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
            <p class="text-5xl font-bold" id="numberOfPlayers">${joinedPlayers}</p>
            <p class="text-2xl text-gray-300">Players Joined</p>
        </div>
        <sl-button
                variant="success"
                size="large"
                class="mt-12 !text-2xl !px-12 !py-4"
                id="startQuiz"
        >
            Start!
        </sl-button>
    </div>
</div>
</body>
<script src="https://cdn.jsdelivr.net/npm/xlsx@0.18.5/dist/xlsx.full.min.js"></script>
<script src="${pageContext.request.contextPath}/js/adminWebSocket.js"></script>
</html>
