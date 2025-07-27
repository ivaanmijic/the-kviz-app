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
                <sl-input id="usernamePlaceholder" class="input-field" type="text" placeholder="Email or Username" required filled pill>
                </sl-input>
                <sl-input class="input-field" type="password" placeholder="Password" required filled pill password-toggle></sl-input>
                <sl-checkbox>Remember Me</sl-checkbox>
                <sl-button class="input-field" type="submit" variant="primary" style="margin-top: 1rem;" pill>Submit</sl-button>
                <p class="error-label">Error placeholder</p>

                <p class="social-text">Or Sign in with social media account</p>
                <div class="social-media">
                    <sl-icon-button name="facebook" label="Facebook" href="#" target="_blank"></sl-icon-button>
                    <sl-icon-button name="twitter-x" label="Twitter/X" href="#" target="_blank"></sl-icon-button>
                    <sl-icon-button name="google" label="Google" href="#" target="_blank"></sl-icon-button>
                    <sl-icon-button name="linkedin" label="Linked Id" href="#" target="_blank"></sl-icon-button>
                </div>
            </form>

            <form action="" class="sign-up-form">
                <h2 class="title">Sign Up</h2>
                <sl-input class="input-field" type="email" placeholder="Email" required filled pill></sl-input>
                <sl-input class="input-field" type="text" placeholder="Username" required filled pill></sl-input>
                <sl-input class="input-field" type="password" placeholder="Password" required filled pill password-toggle></sl-input>
                <sl-button class="input-field" type="submit" variant="primary" style="margin-top: 1rem;" pill>
                    Submit
                </sl-button>
                <p class="error-label">Error placeholder</p>

                <p class="social-text">Or Sign up with social medial account</p>
                <div class="social-media">
                    <sl-icon-button name="facebook" label="Facebook" href="#" target="_blank"></sl-icon-button>
                    <sl-icon-button name="twitter-x" label="Twitter/X" href="#" target="_blank"></sl-icon-button>
                    <sl-icon-button name="google" label="Google" href="#" target="_blank"></sl-icon-button>
                    <sl-icon-button name="linkedin" label="Linked Id" href="#" target="_blank"></sl-icon-button>
                </div>
            </form>
        </div>
    </div>

    <div class="panels-container">

        <div class="panel left-panel">
            <div class="content">
                <h3>New here?</h3>
                <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Optio minus natus est.</p>
                <sl-button id="sign-up-button" pill>Sign Up</sl-button>
            </div>
            <img src="${pageContext.request.contextPath}/assets/images/log.svg" class="image" alt="">
        </div>

        <div class="panel right-panel">
            <div class="content">
                <h3>One of us?</h3>
                <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Optio minus natus est.</p>
                <sl-button id="sign-in-button" pill>Sign In</sl-button>
            </div>
            <img src="${pageContext.request.contextPath}/assets/images/register.svg" class="image" alt="">
        </div>

    </div>

</div>

<script src="${pageContext.request.contextPath}/js/login-layout.js"></script>
<script src="${pageContext.request.contextPath}/js/sign-in.js"></script>
</body>
</html>
