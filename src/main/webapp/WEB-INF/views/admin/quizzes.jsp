<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/26/25
  Time: 1:34 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="card-grid" id="card-grid">
  <c:forEach items="${myQuizzes}" var="quiz" varStatus="loop">
    <sl-card class="quiz-card">
      <img class="quiz-card-img" slot="image" src="${pageContext.request.contextPath}/css/resources/${quiz.thumbnail}">
      <h2>${quiz.title}</h2>
      <p class="quiz-card-description">${quiz.description}</p>
      <sl-button variant="text" size="medium" class="desc-button">More...</sl-button>
      <div slot="footer" class="footer">
        <sl-icon-button name="play"></sl-icon-button>
        <sl-icon-button name="pen"></sl-icon-button>
      </div>
    </sl-card>
  </c:forEach>

    <sl-dialog class="quiz-card-more-info" style="opacity:1; background-color:#ffffff">
      <div class="dialog-body">
        <img>
        <div class="dialog-text">
          <h2></h2>
          <p></p>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <sl-icon-button name="pen"></sl-icon-button>
      </div>
    </sl-dialog>
</div>

