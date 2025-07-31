<%@ page import="com.example.kviz.service.AdminService" %>
<%@ page import="com.example.kviz.model.Admin" %>
<%@ page import="java.util.List" %><%--
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
        <% for (Admin editor : editors) { %>
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
                    name="pencil"
                    label="Edit"
                    size="small">
                    </sl-icon-button>
                <div class="icn-btn-danger">
                <sl-icon-button
                        name="trash"
                        label="Delete"
                        size="small">
                </sl-icon-button>
                </div>
            </div>

        </sl-card>
        <% } %>
    </div>
    <% } %>
</section>














































