<%--
  Created by IntelliJ IDEA.
  User: ivanmijic
  Date: 24. 7. 2025.
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign In</title>
    <%@include file="shoelace-imoprt.jsp"%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.ctx='${pageContext.request.contextPath}'</script>
</head>
<body>

<div class="container">
    <div class="forms-container">
        <div class="signin-signup">
            <form action="" class="sign-in-form">
                <h2 class="title">Sign In</h2>
                <sl-input id="usernamePlaceholder" class="input-field" type="text" placeholder="Email or Username" filled pill required>
                </sl-input>
                <sl-input class="input-field" type="password" placeholder="Password" filled pill required password-toggle></sl-input>
                <sl-checkbox>Remember Me</sl-checkbox>
                <sl-button class="input-field" type="submit" variant="primary" style="margin-top: 1rem;">Submit</sl-button>
                <p class="error-label">Error placeholder</p>
            </form>

            <form action="" class="sign-up-form">
                <h2 class="title">Sign Up</h2>
                <sl-input class="input-field" type="email" placeholder="Email" filled pill required></sl-input>
                <sl-input class="input-field" type="text" placeholder="Username" filled pill required></sl-input>
                <sl-input class="input-field" type="password" placeholder="Password" filled pill requiredpassword-toggle></sl-input>
                <sl-button class="input-field" type="submit" variant="primary" style="margin-top: 1rem;">
                    Submit
                </sl-button>
                <p class="error-label">Error placeholder</p>
            </form>
        </div>
    </div>

    <div class="panels-container">

        <div class="panel left-panel">
            <div class="content">
                <h3>New here?</h3>
                <p>Create your free account and start building, sharing and hosting quizzes with ease.
                    Your dashboard is just a sign-up away..</p>
                <sl-button id="sign-up-button" >Sign Up</sl-button>
            </div>
            <img src="${pageContext.request.contextPath}/assets/images/log.svg" class="image" alt="">
        </div>

        <div class="panel right-panel">
            <div class="content">
                <h3>One of us?</h3>
                <p>Welcome back! Sign in to continue creating quizzes
                    and challenging your friends.</p>
                <sl-button id="sign-in-button" >Sign In</sl-button>
            </div>
            <img src="${pageContext.request.contextPath}/assets/images/register.svg" class="image" alt="">
        </div>

    </div>

</div>

<script src="${pageContext.request.contextPath}/js/login-layout.js"></script>
<script src="${pageContext.request.contextPath}/js/auth.js"></script>
</body>
</html>
