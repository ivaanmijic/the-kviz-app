<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 8:45 AM
  To change this template use File | Settings | File Templates.
--%>
<script src="${pageContext.request.contextPath}/js/admin-home.js"></script>
<aside>
    <header>
        <h1>TheKvizApp</h1>
    </header>
    <ul>
        <li class="selected" onclick="loadMyQuizzes()">
            <sl-animation name="rubberBand" duration="2000" iterations="1">
                <sl-icon name="bookmarks-fill"></sl-icon>
            </sl-animation>
            <span>My Quizzes</span>
        </li>
        <li onclick="loadMyQuizzes()">
            <sl-animation name="rubberBand" duration="2000" iterations="1">
                <sl-icon name="plus-circle"></sl-icon>
            </sl-animation>
            <span>New Quiz</span>
        </li>
        <li onclick="loadMyQuizzes()">
            <sl-animation name="rubberBand" duration="2000" iterations="1">
                <sl-icon name="globe-europe-africa"></sl-icon>
            </sl-animation>
            <span>All Quizzes</span>
        </li>
        <li onclick="loadMyQuizzes()">
            <sl-animation name="rubberBand" duration="2000" iterations="1">
                <sl-icon name="controller"></sl-icon>
            </sl-animation>
            <span>Play Quiz</span>
        </li>
        <li onclick="loadMyQuizzes()">
            <sl-animation name="rubberBand" duration="2000" iterations="1">
                <sl-icon name="person"></sl-icon>
            </sl-animation>
            <span>Users</span>
        </li>
    </ul>
</aside>

<sl-drawer label="Menu" class="nav-drawer">
    <li class="selected" onclick="loadMyQuizzes()">
        <sl-animation name="rubberBand" duration="2000" iterations="1">
            <sl-icon name="bookmarks-fill"></sl-icon>
        </sl-animation>
        <span>My Quizzes</span>
    </li>
    <li onclick="loadMyQuizzes()">
        <sl-animation name="rubberBand" duration="2000" iterations="1">
            <sl-icon name="plus-circle"></sl-icon>
        </sl-animation>
        <span>New Quiz</span>
    </li>
    <li onclick="loadMyQuizzes()">
        <sl-animation name="rubberBand" duration="2000" iterations="1">
            <sl-icon name="globe-europe-africa"></sl-icon>
        </sl-animation>
        <span>All Quizzes</span>
    </li>
    <li onclick="loadMyQuizzes()">
        <sl-animation name="rubberBand" duration="2000" iterations="1">
            <sl-icon name="controller"></sl-icon>
        </sl-animation>
        <span>Play Quiz</span>
    </li>
    <li onclick="loadMyQuizzes()">
        <sl-animation name="rubberBand" duration="2000" iterations="1">
            <sl-icon name="person"></sl-icon>
        </sl-animation>
        <span>Users</span>
    </li>
</sl-drawer>
