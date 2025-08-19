<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="enter-player-name">
    <div class="input-name-box w-full text-center bg-white/80 backdrop-blur-sm p-8 rounded-lg" style="width:30%">
        <h2 id="quizTitle" class="text-3xl font-bold text-gray-800 mb-2"></h2>
        <p class="text-lg text-gray-600 mb-6">Unesite vaše ime ili nadimak</p>
        <input type="text" class="sl-input text-center text-2xl" placeholder="Vaše Ime" id="playerName">
        <button id="startQuiz" class="sl-button sl-button-primary max-w-md mt-6 !text-lg start-game-btn" >Početak!</button>
    </div>
</div>
