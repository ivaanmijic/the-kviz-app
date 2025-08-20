<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/18/25
  Time: 8:24 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="text-center centered-box" >
    <h2 class="text-4xl font-bold text-gray-800 " id="correctness">You are ready!</h2>
    <p class="text-xl text-gray-600 mt-4" id="waitingReason">Wait for the admin to start the quiz...</p>
    <div class="mt-12">
        <div class="animate-spin rounded-full h-16 w-16 border-b-4 border-blue-600 mx-auto"></div>
    </div>
    <div class="mt-12 flex items-center justify-center space-x-2 text-gray-500">
        <sl-icon
                name="people-fill"
                class="text-4xl text-indigo-300"
        ></sl-icon>
        <span class="font-semibold text-lg"><span id="numberOfPlayers">${playerNumber}</span> connected players</span>
    </div>
</div>
