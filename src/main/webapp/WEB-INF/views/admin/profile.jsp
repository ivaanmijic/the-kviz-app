<%--
  Created by IntelliJ IDEA.
  User: ivan
  Date: 8/10/25
  Time: 1:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.example.kviz.model.Admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="admin" scope="request" type="com.example.kviz.model.Admin"/>
<c:if test="${not empty admin}">
    <section>
        <h2 class="section-title">Account</h2>

        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8" style="margin: 1.5rem auto">

            <div class="lg:col-span-1 bg-white p-6 rounded-xl shadow-md h-fit">
                <div class="flex flex-col items-center text-center">
                    <sl-avatar label="Admin Username" initials="A"
                               style="--size: 6rem; margin-bottom: 1rem;"></sl-avatar>
                    <h2 class="text-2xl font-bold text-gray-900">${admin.username}</h2>
                    <p class="text-gray-500">${admin.email}</p>
                </div>

                <sl-divider style="margin: 1.5rem 0;"></sl-divider>

                <div>
                    <h3 class="font-semibold text-gray-800 mb-4">Profile Details</h3>
                    <ul class="space-y-3 text-sm text-gray-600">
                        <li class="flex items-center">
                            <sl-icon name="shield-check" class="mr-3 text-gray-500"></sl-icon>
                            <span>Role: <span class="font-medium text-gray-800">${admin.role.toString()}</span></span>
                        </li>
                        <li class="flex items-center">
                            <sl-icon name="calendar-date" class="mr-3 text-gray-500"></sl-icon>
                            <span>Joined: <span class="font-medium text-gray-800">January 25, 2025</span></span>
                        </li>
                        <li class="flex items-center">
                            <sl-icon name="activity" class="mr-3 text-gray-500"></sl-icon>
                            <span>Last Update: <span class="font-medium text-gray-800">August 10, 2025</span></span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="lg:col-span-2 bg-white p-6 sm:p-8 rounded-xl shadow-md">
                <form id="editProfileForm">
                    <div class="space-y-6">
                        <div>
                            <h3 class="text-xl font-semibold text-gray-900 mb-4">Edit Information</h3>
                            <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                                <sl-input label="Email" name="email" value="${admin.email}"></sl-input>
                                <sl-input label="Username" name="username" value="${admin.username}"></sl-input>
                            </div>
                        </div>

                        <sl-divider style=""></sl-divider>

                        <sl-details summary="Change Password">
                            <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-6">
                                <sl-input type="password" name="new-password" placeholder="New Password"
                                          password-toggle></sl-input>
                                <sl-input type="password" name="rep-password" placeholder="Confirm Password"
                                          password-toggle></sl-input>
                                <div class="sm:col-span-2">
                                    <span id="pwMatchIndicator"></span>
                                </div>
                            </div>
                        </sl-details>

                        <sl-divider style=""></sl-divider>

                        <div>
                            <h3 class="text-xl font-semibold text-gray-900">Confirm Changes</h3>
                            <p class="text-sm text-gray-500 mt-1 mb-4">Enter your current password to save any
                                changes.</p>
                            <sl-input type="password" name="old-password" placeholder="Current Password" password-toggle
                                      required></sl-input>
                        </div>
                    </div>

                    <div class="mt-8 pt-5 border-t border-gray-200 flex flex-col sm:flex-row justify-between items-center gap-4">
                        <sl-button variant="danger" outline id="deleteProfileBtn">Delete Profile</sl-button>
                        <sl-button variant="primary" type="submit" id="updateBtn" disabled>Update Profile</sl-button>
                    </div>
                </form>
            </div>
        </div>
    </section>

    <sl-dialog label="Confirm Deletion" id="deleteDialog">
        <p>Are you sure you want to delete your profile? This action cannot be undone.</p>
        <sl-button slot="footer" id="cancelDeleteBtn">Cancel</sl-button>
        <sl-button slot="footer" variant="danger" id="confirmDeleteBtn">Confirm Delete</sl-button>
    </sl-dialog>
</c:if>

<c:if test="${empty admin}">
    <script type="module" src="${pageContext.request.contextPath}/js/manager/alertManager.js">
        AlertManager.showError("Could not display profile.");
    </script>
</c:if>