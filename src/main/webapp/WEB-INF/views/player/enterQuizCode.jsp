<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 11:41 AM
  To change this template use File | Settings | File Templates.
--%>
<div class="player-insert-code-screen min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md text-center p-4">
        <h1 class="text-5xl font-extrabold mb-4">The Kviz App</h1>
        <p class="text-xl mb-8">Enter PIN to join the quiz.</p>

        <sl-card class="w-full p-8 rounded-2xl shadow-lg">
            <sl-input
                    size="large"
                    id="quizId"
                    class="text-center text-4xl tracking-[0.5rem] font-bold"
                    placeholder="123 456">
            </sl-input>

            <sl-button
                    variant="primary"
                    size="large"
                    class="w-full mt-6 join-in-btn"
                    id="joinBtn">
                Join
            </sl-button>
        </sl-card>
    </div>
</div>
