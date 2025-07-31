<%@ page import="com.example.kviz.model.Admin" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="com.example.kviz.util.DateDisplayUtil" %><%--
  Created by IntelliJ IDEA.
  User: ivan
  Date: 7/31/25
  Time: 1:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Admin admin = (Admin) session.getAttribute("admin");
  String id = admin.getId().toString();
%>

<div class="user-box">
  <div class="user-left">
    <sl-avatar label="username"></sl-avatar>
    <div class="user-labels">
      <span class="welcome">Welcome</span>
      <span class="title"><%=admin.getUsername()%></span>
    </div>
  </div>
  <sl-button class="yellow" size="large" onclick="document.getElementById('<%=id%>Drawer').show()">Profile</sl-button>
</div>

<sl-drawer label="Edit Profile" id="<%=id%>Drawer" placement="end">
  <div class="drawer-body" style="justify-content: space-between">
    <div>
    <div class="admin-info">
      <sl-avatar label="<%=admin.getUsername()%>"
                 initials="<%=admin.getUsername().charAt(0)%>"
                 style="--size: 64px"></sl-avatar>
      <p><strong>Username:</strong> <%= admin.getUsername() %></p>
      <p><strong>Email:</strong> <%= admin.getEmail() %></p>
      <p><strong>Role:</strong> <%= admin.getRole() %></p>
      <p><strong>Created: </strong> <%=DateDisplayUtil.formatDate(admin.getCreatedAt())%></p>
      <%
        LocalDateTime updatedAt = admin.getUpdatedAt();
        if (updatedAt != null) {
      %>
      <p><strong>Last update</strong> <%=DateDisplayUtil.formatDate(admin.getUpdatedAt())%></p>
      <% } %>
    </div>

    <sl-details summary="Edit profile">
      <sl-details summary="Change email">
        <sl-input name="email" value="<%=admin.getEmail()%>"></sl-input>
      </sl-details>
      <sl-details summary="Change username">
        <sl-input name="username" value="<%=admin.getUsername()%>"></sl-input>
      </sl-details>
      <sl-details summary="Change password">
        <sl-input type="password" name="old-password" placeholder="Current password" password-toggle></sl-input>
        <sl-divider style="--spacing= 0.5rem"></sl-divider>
        <sl-input type="password" name="new-password" placeholder="New password" password-toggle></sl-input>
        <sl-input type="password" name="rep-password" placeholder="Confirm password" password-toggle></sl-input>
      </sl-details>
      <sl-button variant="primary" class="drawer-button" pill outline>Update</sl-button>
    </sl-details>
    </div>

    <sl-button variant="danger" class="drawer-button" pill onclick="document.getElementById('<%=id%>Dialog').show()">Delete</sl-button>
  </div>
</sl-drawer>

<sl-dialog label="Confirm Deletion" id="<%=id%>Dialog">
  <p>Are you sure you want to delete <strong><%=admin.getUsername()%></strong>?</p>
  <sl-button slot="footer" variant="primary" outline pill onclick="document.getElementById('<%=id%>Dialog').hide()">Cancel</sl-button>
  <sl-button slot="footer" variant="danger" pill onclick="deleteAdmin(<%=id%>, true)">Delete</sl-button>
</sl-dialog>

<script src="/js/admin-crud.js"></script>