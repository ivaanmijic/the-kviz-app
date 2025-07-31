<%@ page import="com.example.kviz.service.AdminService" %>
<%@ page import="com.example.kviz.model.Admin" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.kviz.util.DateDisplayUtil" %><%--
  Created by IntelliJ IDEA.
  User: ivan
  Date: 7/31/25
  Time: 9:41 AM
  To change this template use File | Settings | File Templates.
--%>
<section>
    <h2 class="section-title">Users</h2>

    <%
        AdminService adminService = new AdminService();
        List<Admin> editors = adminService.getAllEditors();
        if (editors.isEmpty()) {
    %>
    <p class="warning">No other users found.</p>
    <%
        } else {
    %>
    <div class="user-grid">
        <%
            for (Admin editor : editors) {
                String id = editor.getId().toString();

        %>
        <sl-card class="user-card">
            <div slot="header" class="admin-avatar">
                <sl-avatar
                    label="<%= editor.getUsername() %>"
                    initials="<%= editor.getUsername().charAt(0) %>">
                </sl-avatar>
            </div>

            <div class="admin-details">
                <div class="title"><%= editor.getUsername() %></div>
                <div class="subtitle"><%= editor.getEmail() %></div>
            </div>

            <div slot="footer" class="admin-actions">
                <sl-icon-button
                        name="info-circle"
                        label="Info"
                        onclick="document.getElementById('<%=id%>').show()";
                >
                </sl-icon-button>
                <div class="icn-btn-danger">
                <sl-icon-button
                        name="trash"
                        label="Delete"
                        size="small"
                        onclick="document.getElementById('<%=id%>Dialog').show()"
                >
                </sl-icon-button>
                </div>
            </div>

        </sl-card>

        <sl-drawer label="Admin Details" id="<%=id%>" placement="end">
            <div class="drawer-body" style="justify-content: space-between">
                <div class="admin-info">
                    <sl-avatar
                            label="<%= editor.getUsername() %>"
                            initials="<%= editor.getUsername().charAt(0) %>"
                            style="--size: 64px;">
                    </sl-avatar>

                    <p><strong>Username:</strong> <%= editor.getUsername() %></p>
                    <p><strong>Email:</strong> <%= editor.getEmail() %></p>
                    <p><strong>Role:</strong> <%= editor.getRole() %></p>
                    <p><strong>Joined:</strong> <%= DateDisplayUtil.formatDate(editor.getCreatedAt()) %></p>
<%--                    <p><strong>Quizzes:</strong> <%= editor.getQuizzes().size() %></p>--%>

                    <ul class="quiz-list">
<%--                        <% for (Quiz quiz : editor.getQuizzes()) { %>--%>
<%--                        <li><%= quiz.getTitle() %></li>--%>
<%--                        <% } %>--%>
                        <% for (int i = 0; i < 3; ++i) { %>
                            <li>Quiz Name shoud be here.</li>
                        <% } %>
                    </ul>
                </div>

                <sl-button
                        variant="danger"
                        class="drawer-button"
                        pill
                        onclick="document.getElementById('<%=id%>Dialog').show()">
                    Delete
                </sl-button>
            </div>
        </sl-drawer>

        <sl-dialog label="Confirm Deletion" id="<%=id%>Dialog">
            <p>Are you sure you want to delete <strong><%=editor.getUsername()%></strong>?</p>
            <sl-button slot="footer" variant="primary" outline pill onclick="document.getElementById('<%=id%>Dialog').hide()">Cancel</sl-button>
            <sl-button slot="footer" variant="danger" pill>Delete</sl-button>
        </sl-dialog>
        <% } %>
    </div>
    <% } %>
</section>
<script src="/js/delete-admin.js"></script>














































