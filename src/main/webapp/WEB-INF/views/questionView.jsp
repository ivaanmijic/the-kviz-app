<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/19/25
  Time: 10:05 AM
  To change this template use File | Settings | File Templates.
--%>
<div class="w-full max-w-3xl mx-auto p-4">
    <div class="flex items-center justify-between mb-4">
        <span class="font-semibold text-lg"><span id="numberOfPlayers">${playerNumber}</span> connected players</span>

        <div class="w-36">
            <sl-progress-bar id="progress" value="100"></sl-progress-bar>
        </div>
    </div>


    <h2 id="questionText" class="text-center text-3xl font-bold mb-6">
        Question will appear here
    </h2>

    <div id="questionImagePlaceholder" class="flex justify-center items-center mb-6">
        <img id="questionImage" src="${pageContext.request.contextPath}/assets/images/background.svg" alt="question image"
             class="max-h-64 w-auto object-contain rounded shadow-sm" />

    </div>

    <div class="w-full grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
        <button id="btnAns0" class="answers-toggle sl-button !h-24 !text-xl bg-red-500 text-white hover:bg-red-600 answers w-full flex items-center justify-start px-4">
            <sl-icon name="triangle-fill" slot="prefix"></sl-icon>
            <span id="answ0" class="ml-3">Answer A</span>
        </button>

        <button id="btnAns1" class="answers-toggle sl-button !h-24 !text-xl bg-blue-500 text-white hover:bg-blue-600 answers w-full flex items-center justify-start px-4">
            <sl-icon name="circle-fill" slot="prefix"></sl-icon>
            <span id="answ1" class="ml-3">Answer B</span>
        </button>

        <button id="btnAns2" class="answers-toggle sl-button !h-24 !text-xl bg-yellow-500 text-white hover:bg-yellow-600 answers w-full flex items-center justify-start px-4">
            <sl-icon name="square-fill" slot="prefix"></sl-icon>
            <span id="answ2" class="ml-3">Answer C</span>
        </button>

        <button id="btnAns3" class="answers-toggle sl-button !h-24 !text-xl bg-green-500 text-white hover:bg-green-600 answers w-full flex items-center justify-start px-4">
            <sl-icon name="diamond-fill" slot="prefix"></sl-icon>
            <span id="answ3" class="ml-3">Answer D</span>
        </button>
    </div>

    <div class="flex justify-center">
        <sl-button id="submitMultipleBtn" variant="primary">Submit</sl-button>
    </div>
</div>