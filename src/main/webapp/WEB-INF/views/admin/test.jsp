<%--
  Created by IntelliJ IDEA.
  User: ivanmijic
  Date: 27. 7. 2025.
  Time: 11:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Test</title>
</head>
<body>

<h3><%=session.getAttribute("admin")%></h3>
<script>
    // ... some JavaScript code ...
    <%
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
    %>
    <h2><%=cookie.getValue()%></h2><% // This will cause an error in <script> block
                }
            }
        }
    %>
    // ... more JavaScript code ...
</script>


</body>
</html>
