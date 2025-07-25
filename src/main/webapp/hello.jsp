<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Gretty Demo</title>
    <%@ include file="/WEB-INF/views/shoelace-imoprt.jsp"%>
  </head>
  <body>
      <h1>${message}</h1>
      <p>Served at: ${pageContext.request.contextPath}</p>
  </body>
</html>
