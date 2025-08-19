<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/26/25
  Time: 1:34 PM
  To change this template use File | Settings | File Templates.
--%>
<c:if test="${not empty quizzes}">
    <section>
        <h2 class="section-title">${title}</h2>
        <div class="card-grid" id="card-grid">
            <c:forEach items="${quizzes}" var="quiz" varStatus="loop">
                <sl-card class="quiz-card" data-quiz-id="${quiz.id}">
                    <img class="quiz-card-img" slot="image"
                         src="${pageContext.request.contextPath}/uploads/quizImages/${quiz.thumbnail}"
                         alt="Quiz Image"
                         onerror="this.onerror=null;this.src='https://placehold.co/600x400/334155/ffffff?text=Quiz';"
                    >

                    <h3 class="text-lg font-bold">${quiz.title}</h3>

                    <div class="description-container mt-2">
                        <div class="quiz-description-clipped">
                                ${quiz.description}
                        </div>
                        <sl-button class="read-more-btn" size="small" pill data-quiz-id="${quiz.id}" data-quiz-description="${quiz.description}">
                            Read more...
                        </sl-button>
                    </div>

                    <p class="text-sm mt-2">
                        <c:choose>
                            <c:when test="${quiz.visible}">
                                Public
                            </c:when>
                            <c:otherwise>
                                Private
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <div slot="footer" class="card-footer">
                        <div class="w-full flex space-x-2">
                            <sl-button variant="primary" class="flex-1" data-action="start" data-quiz-id="${quiz.id}">
                                Start
                            </sl-button>
                            <c:if test="${quiz.visible or sessionScope.admin.role eq 'SUPERADMIN'}">
                                <sl-button class="edit-quiz-btn" variant="neutral" data-action="edit"
                                           data-quiz-id="${quiz.id}">Edit
                                </sl-button>
                                <sl-button class="delete-quiz-btn danger-text-btn" variant="text" data-action="delete"
                                           data-quiz-id="${quiz.id}" data-quiz-title="${quiz.title}">
                                    Delete
                                </sl-button>
                            </c:if>
                        </div>
                    </div>
                </sl-card>
            </c:forEach>

            <sl-dialog id="descriptionModal" label="Description">
                <p id="modalDescriptionContent" style="white-space: pre-wrap;"></p>
            </sl-dialog>

            <sl-dialog class="editQuizDialog" id="editQuizDialog"></sl-dialog>
        </div>
    </section>
</c:if>
