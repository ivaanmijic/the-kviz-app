<%--
  Created by IntelliJ IDEA.
  User: ivan
  Date: 8/1/25
  Time: 1:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.kviz.model.Admin" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="com.example.kviz.util.DateDisplayUtil" %>

<script src="${pageContext.request.contextPath}/webjars/jquery/3.7.1/jquery.min.js"></script>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Admin admin = (Admin) session.getAttribute("admin");
    String id = admin.getId().toString();
    String editDialogId = id + "EditDialog";
%>

<div class="user-box">
    <div class="user-left">
        <sl-avatar label="username"></sl-avatar>
        <div class="user-labels">
            <span class="welcome">Welcome</span>
            <span class="title"><%=admin.getUsername()%></span>
        </div>
    </div>
    <sl-button class="yellow" size="large" onclick="document.getElementById('<%=editDialogId%>').show()">Profile</sl-button>
</div>

<sl-dialog label="Edit Profile" id="<%=editDialogId%>" style="--width: 70vw; max-width: 800px;">
    <div class="dialog-body">

        <div class="admin-info">
            <sl-avatar label="<%=admin.getUsername()%>"
                       initials="<%=admin.getUsername().charAt(0)%>"
                       style="--size: 80px; margin-bottom: 1rem;"></sl-avatar>
            <p><strong>Username:</strong> <%= admin.getUsername() %></p>
            <p><strong>Email:</strong> <%= admin.getEmail() %></p>
            <p><strong>Role:</strong> <%= admin.getRole() %></p>
            <p><strong>Created: </strong> <%=DateDisplayUtil.formatDate(admin.getCreatedAt())%></p>
            <%
                LocalDateTime updatedAt = admin.getUpdatedAt();
                if (updatedAt != null) {
            %>
            <p><strong>Last update:</strong> <%=DateDisplayUtil.formatDate(admin.getUpdatedAt())%></p>
            <% } %>
        </div>

        <div class="drawer-body" style="justify-content: space-between;">
            <div>
                <sl-input label="Email" name="email" value="<%=admin.getEmail()%>"></sl-input>
                <sl-input label="Username" name="username" value="<%=admin.getUsername()%>"></sl-input>

                <sl-divider></sl-divider>

                <sl-details summary="Change Password">
                    <sl-input type="password" name="new-password" placeholder="New Password" password-toggle></sl-input>
                    <sl-input type="password" name="rep-password" placeholder="Confirm Password" password-toggle></sl-input>
                    <span id="pwMatchIndicator" style="margin-left: .5rem; font-size: 1.2em"></span>
                </sl-details>

                <sl-divider></sl-divider>

                <p style="margin-bottom: 0.5rem;"><strong>Enter current password to save changes:</strong></p>
                <sl-input type="password" name="old-password" placeholder="Current Password" password-toggle required></sl-input>
            </div>

            <sl-button variant="danger" outline pill onclick="document.getElementById('<%=id%>Dialog').show()">Delete Profile</sl-button>
        </div>
    </div>

    <sl-button slot="footer" onclick="document.getElementById('<%=editDialogId%>').hide()">Cancel</sl-button>
    <sl-button type="button" slot="footer" id="updateBtn" variant="primary" disabled>Update</sl-button>
</sl-dialog>

<sl-dialog label="Confirm Deletion" id="<%=id%>Dialog">
    <p>Are you sure you want to delete <strong><%=admin.getUsername()%></strong>?</p>
    <sl-button slot="footer" variant="primary" outline pill onclick="document.getElementById('<%=id%>Dialog').hide()">Cancel</sl-button>
    <sl-button slot="footer"
               variant="danger"
               class="delete-admin-btn"
               data-admin-id="<%= id %>"
               data-self="<%= true %>"
               pill>
               Delete
    </sl-button>
</sl-dialog>

<script>
    const contextPath = "${pageContext.request.contextPath}";
    const adminId = "<%= id %>";
    const role = "<%=admin.getRole()%>";
</script>