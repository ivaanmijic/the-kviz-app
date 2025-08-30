<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 11:56 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    sl-input.w-full { width: 100%; display: block; }
    sl-input::part(input) { box-sizing: border-box; padding: 0.75rem 1rem; font-size: 1.25rem; }

    .input-name-box { width: 100%; max-width: 420px; }
</style>

<div class="enter-player-name min-h-screen flex items-center justify-center p-6">
    <div class="input-name-box glass p-8 w-full mx-auto text-center shadow-lg">
        <h2 id="quizTitle" class="text-3xl font-bold mb-2">Quiz title</h2>
        <p class="text-lg mb-6">Enter your nickname</p>

        <sl-input
                id="playerName"
                type="text"
                size="large"
                class="w-full text-center text-2xl"
                placeholder="Nickname">
        </sl-input>

        <div class="mt-6 flex justify-center">
            <sl-button
                    id="startQuiz"
                    variant="primary"
                    size="large"
                    class="start-game-btn w-full sm:w-auto !text-lg px-6">
                Start!
            </sl-button>
        </div>
    </div>
</div>
