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
    <div class="sidebar-section">
        <p class="sidebar-heading">General</p>
        <ul class="sidebar-list">
            <li data-view="dashboard" class="sidebar-item selected">
                <sl-icon name="house" class="sidebar-icon"></sl-icon>
                Dashboard
            </li>
            <li data-view="quizzes" class="sidebar-item">
                <sl-icon name="list-check" class="sidebar-icon"></sl-icon>
                My Quizzes
            </li>
            <li data-view="create-quiz" class="sidebar-item">
                <sl-icon name="plus-square" class="sidebar-icon"></sl-icon>
                Create Quiz
            </li>
            <li data-view="profile" class="sidebar-item">
                <sl-icon name="person" class="sidebar-icon"></sl-icon>
                My Profile
            </li>
        </ul>
    </div>

    <c:if test="${sessionScope.admin.role == 'SUPERADMIN'}">
        <div class="sidebar-section">
            <p class="sidebar-heading">Management</p>
            <ul class="sidebar-list">
                <li data-view="admin-list" class="sidebar-item">
                    <sl-icon name="people" class="sidebar-icon"></sl-icon>
                    Admin List
                </li>
                <li data-view="all-quizzes" class="sidebar-item">
                    <sl-icon name="globe-europe-africa" class="sidebar-icon"></sl-icon>
                    All Quizzes
                </li>
                <li data-view="logs" class="sidebar-item">
                    <sl-icon name="terminal" class="sidebar-icon"></sl-icon>
                    Activity Log
                </li>
            </ul>
        </div>
    </c:if>
</aside>


<sl-drawer label="Menu" class="nav-drawer">
    <div class="sidebar-section">
        <p class="sidebar-heading">General</p>
        <ul class="sidebar-list">
            <li data-view="dashboard" class="sidebar-item selected">
                <sl-icon name="house" class="sidebar-icon"></sl-icon>
                Dashboard
            </li>
            <li data-view="quizzes" class="sidebar-item">
                <sl-icon name="list-check" class="sidebar-icon"></sl-icon>
                My Quizzes
            </li>
            <li data-view="create-quiz" class="sidebar-item">
                <sl-icon name="plus-square" class="sidebar-icon"></sl-icon>
                Create Quiz
            </li>
            <li data-view="profile" class="sidebar-item">
                <sl-icon name="user" class="sidebar-icon"></sl-icon>
                My Profile
            </li>
        </ul>
    </div>

    <c:if test="${sessionScope.admin.role == 'SUPERADMIN'}">
        <div class="sidebar-section">
            <p class="sidebar-heading">Management</p>
            <ul class="sidebar-list">
                <li data-view="admin-list" class="sidebar-item">
                    <sl-icon name="people" class="sidebar-icon"></sl-icon>
                    Admin List
                </li>
                <li data-view="all-quizzes" class="sidebar-item">
                    <sl-icon name="globe-europe-africa" class="sidebar-icon"></sl-icon>
                    All Quizzes
                </li>
                <li data-view="logs" class="sidebar-item">
                    <sl-icon name="terminal" class="sidebar-icon"></sl-icon>
                    Activity Log
                </li>
            </ul>
        </div>
    </c:if>
</sl-drawer>
