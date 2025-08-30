<%--
  Created by IntelliJ IDEA.
  User: haris
  Date: 7/29/25
  Time: 4:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<sl-alert id="successAlert" variant="success" duration="3000" closable>
    <sl-icon slot="icon" name="exclamation-octagon"></sl-icon>
    <strong>Your quiz has been created</strong><br/>
    Your quiz is successfully added to your quizzes!
</sl-alert>
<sl-alert id=dangerAlert" variant="danger" duration="3000" closable>
    <sl-icon slot="icon" name="exclamation-octagon"></sl-icon>
    <strong>Your quiz submission has been canceled</strong><br/>
</sl-alert>
<section>
    <h2 class="section-title" id="quizEditorTitle">Create New Quiz</h2>
    <form class="create-quiz" id="createQuiz" method="post" enctype="multipart/form-data">
        <div class="main-quiz-info">
            <label class="upload-box" id="uploadBox">
                <img id="quizPreview" style="display:none;">
                <sl-icon-button type="button" class="remove-btn" size="medium" name="x" id="removeBtn"
                                style="display:none"></sl-icon-button>
                <sl-icon id="plusIcon" name="plus-circle"></sl-icon>
            </label>
            <input type="file" name="image" id="imageInput" accept="image/*"/>
            <sl-input id="quizTitle" placeholder="Quiz Title"></sl-input>
            <sl-select id="quizCategory" placeholder="Category">
                <sl-option value="sport">Sport</sl-option>
                <sl-option value="history">History</sl-option>
                <sl-option value="geography">Geography</sl-option>
                <sl-option value="science">Science</sl-option>
                <sl-option value="pop-culture">Pop-culture</sl-option>
                <sl-option value="technology">Technology</sl-option>
                <sl-option value="other">Other</sl-option>
            </sl-select>
            <sl-select id="quizVisibility" placeholder="Visibility">
                <sl-option value="private">Private</sl-option>
                <sl-option value="public">Public</sl-option>
            </sl-select>
            <sl-textarea id="quizDescription" size="large" placeholder="Quiz Description"></sl-textarea>
        </div>
        <div id="questions-container">
        </div>
        <sl-tooltip content="Add new question">
            <sl-button variant="text" size="large" id="add-question-button">
                &#xFF0B Add Question
            </sl-button>
        </sl-tooltip>
        <sl-button variant="success" size="large" class="submit-quiz" id="submitQuiz" submit>Submit</sl-button>
    </form>
</section>
<script>
    const quiz = JSON.parse('<%= request.getAttribute("quizJson") %>');
</script>
<script src="${pageContext.request.contextPath}/js/create-quiz.js"></script>
