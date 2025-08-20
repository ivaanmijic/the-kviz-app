<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 8/19/25
  Time: 7:50 PM
  To change this template use File | Settings | File Templates.
--%>
<header class="flex items-center justify-between bg-gray-800 text-white px-4 py-2 shadow-md">
    <!-- Exit button (always visible) -->
    <sl-button variant="danger" size="medium" id="leaveQuiz">
        <sl-icon slot="prefix" name="x-circle"></sl-icon>
        Exit Quiz
    </sl-button>

    <!-- Continue button (only for admin, toggled via JS) -->
    <sl-button id="nextQuestion" variant="primary" size="medium" >
        <sl-icon slot="prefix" name="arrow-right-circle"></sl-icon>
        Continue
    </sl-button>
</header>
