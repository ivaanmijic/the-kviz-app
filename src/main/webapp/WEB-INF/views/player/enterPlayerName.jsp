<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="enter-player-name">
    <div class="input-name-box w-full text-center glass p-8" style="width:30%">
        <h2 id="quizTitle" class="text-3xl font-bold mb-2"></h2>
        <p class="text-lg mb-6">Enter your name</p>
        <input type="text" class="sl-input text-center text-2xl" placeholder="Nickname" id="playerName">
        <sl-button variant="success" size="large" id="startQuiz" class="max-w-md mt-6 !text-lg start-game-btn" >Start!</sl-button>
    </div>
</div>
