<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/25/25
  Time: 8:45 AM
  To change this template use File | Settings | File Templates.
--%>
<script src="${pageContext.request.contextPath}/js/create-quiz.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <li onclick="createQuizWindow(); document.title = 'Quiz Maker';">
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
                    <sl-icon name="list-check" class="sidebar-icon"></sl-icon>
                    All Quizzes
                </li>
            </ul>
        </div>
    </c:if>

    <div class="sidebar-footer">
        <sl-button variant="text" class="danger-text-btn" href="${pageContext.request.contextPath}/auth/logout">
            <sl-icon slot="prefix" name="box-arrow-left"></sl-icon>
            Sign Out
        </sl-button>
    </div>
</aside>

<sl-drawer label="Menu" class="nav-drawer">
    <div class="sidebar-section">
        <p class="sidebar-heading">General</p>
        <ul class="sidebar-list">
            <li data-view="dashboard" class="sidebar-item selected">
                <sl-icon name="house" class="sidebar-icon"></sl-icon>
                Dashboard
            </li>
            <li onclick="createQuizWindow(); document.title = 'Quiz Maker';">
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
                    <sl-icon name="list-check" class="sidebar-icon"></sl-icon>
                    All Quizzes
                </li>
            </ul>
        </div>
    </c:if>

    <div class="sidebar-footer">
        <sl-button variant="text" class="danger-text-btn" href="${pageContext.request.contextPath}/auth/logout">
            <sl-icon slot="prefix" name="box-arrow-left"></sl-icon>
            Sign Out
        </sl-button>
    </div>
</sl-drawer>
