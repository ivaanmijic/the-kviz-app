class Quiz {
    constructor(id, img, title, category, visibility, description) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.category = category;
        this.visibility = visibility;
        this.description = description;
    }

}

class Question {
    constructor({id, question, thumbnail, points, time, category, answers, correctAnswer}) {
        this.id = id;
        this.img = thumbnail;
        this.question = question;
        this.points = points;
        this.time = time;
        this.category = category;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
}

class QuizEditor {
    constructor({quizId, contextPath}) {
        this.base = contextPath;
        this.quizId = quizId;
    }

    async fetchAndFillEditWindow() {
        const ret = await this._getQuizCreateQuiz(this.base);
        console.log(ret);
        this.quiz = await this._getQuizInfo(this.base, this.quizId);
        console.log(this.quiz);
        const image = document.getElementById('quizPreview')
        image.src = this.base + "/uploads/quizImages/" + this.quiz.img;
        image.style.display = 'block';
        window.quizImageFile = null;
        window.quizId = this.quiz.id;
        console.log(this.quiz.category);
        console.log(this.quiz.visibility ? "public" : "private")
        document.getElementById('removeBtn').style.display = 'block';
        document.getElementById('plusIcon').style.display = 'none';
        document.getElementById('uploadBox').style.border = 'none';
        document.getElementById('quizTitle').value = this.quiz.title;
        document.getElementById('quizCategory').value = this.quiz.category.toLowerCase();
        document.getElementById('quizVisibility').value = this.quiz.visibility ? "public" : "private";
        document.getElementById('quizDescription').value = this.quiz.description;
        document.getElementById('submitQuiz').innerText = 'Edit Quiz';
        document.getElementById('quizEditorTitle').innerText = 'Edit Quiz';
    }

    async _getQuizInfo(base, quizId) {
        try {
            let result = await fetch(`${base}/admin/quiz-information?id=${quizId}`, {
                method: 'GET',
                headers: {'Content-Type': 'application/json'},
            })
            console.log(result);
            const {id, title, thumbnail, description, category, visible} = await result.json();
            return new Quiz(id, thumbnail, title, category, visible, description);

        } catch (err) {
            console.log(err);
        }
    }

    async _getQuizCreateQuiz(base) {
        await fetch(`${base}/admin/create-quiz-window`, {
            method: 'GET',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        })
            .then(response => response.text())
            .then(html => {
                console.log(html);
                const editDialog = document.getElementById('editQuizDialog');
                editDialog.innerHTML = html;
                editDialog.show();
                this.quizForm = new QuizForm(this.quizId);
                console.log(this.quizForm)
            })
            .catch(error => console.log(error));
    }

    addQuestion(question) {
        this.quizForm.addQuestion(question);
    }
}

class QuestionEditor {
    constructor({quizId, api, contextPath}) {
        this.api = api;
        this.base = contextPath;
        this.quizId = quizId;
    }

    async fetchAndFillQuestions(quiz) {
        console.log(this.quizId);
        this.questionArray = await this._getQuestionsInfo(this.base, this.quizId);
        this.questionArray.forEach(question => {
            console.log(question);
            quiz.addQuestion(question);
        })
    }

    async _getQuestionsInfo(base, quizId) {
        try {
            let result = await fetch(`${base}/admin/questions-info?id=${quizId}`, {
                method: 'GET',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            })
            console.log(result);
            let questionArray = await result.json();
            console.log(questionArray);
            return questionArray.map((qJsonData) => new Question(qJsonData));
        } catch (err) {
            console.log(err);
        }
    }
}

const deleteDialog = document.getElementById('deleteQuizDialog');
const deleteQuizTitle = document.getElementById('deleteQuizTitle');
const confirmDeleteBtn = document.getElementById('confirmQuizDeleteBtn');
const cancelDeleteBtn = document.getElementById('cancelQuizDeleteBtn');

document.querySelectorAll('.delete-quiz-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const quizId = btn.dataset.quizId;
        const quizTitle = btn.dataset.quizTitle;

        deleteDialog.dataset.quizId = quizId;
        deleteDialog.dataset.quizTitle = quizTitle || "this quiz";
        deleteQuizTitle.textContent = quizTitle || "this quiz";

        deleteDialog.show();
    });
});

confirmDeleteBtn.addEventListener('click', async () => {
    const quizId = deleteDialog.dataset.quizId;
    if (!quizId) return;

    confirmDeleteBtn.loading = true;
    cancelDeleteBtn.disabled = true;

    try {
        await deleteQuiz(quizId);
        deleteDialog.dataset.deleteStatus = 'success';
    } catch (err) {
        deleteDialog.dataset.deleteStatus = 'fail';
        deleteDialog.dataset.errorMessage = "Could not delete quiz.";
    } finally {
        deleteDialog.hide();
    }
});

// Handle cancel button
cancelDeleteBtn.addEventListener('click', () => {
    deleteDialog.hide();
});

deleteDialog.addEventListener('sl-after-hide', () => {
    const quizId = deleteDialog.dataset.quizId;
    const quizTitle = deleteDialog.dataset.quizTitle;

    if (deleteDialog.dataset.deleteStatus === 'success') {
        document.querySelector(`.quiz-card[data-quiz-id='${quizId}']`)?.remove();
        showSuccess(`Successfully deleted quiz: ${quizTitle}`);
    } else if (deleteDialog.dataset.deleteStatus === 'fail') {
        showError(deleteDialog.dataset.errorMessage);
    }

    confirmDeleteBtn.loading = false;
    cancelDeleteBtn.disabled = false;

    delete deleteDialog.dataset.quizId;
    delete deleteDialog.dataset.quizTitle;
    delete deleteDialog.dataset.deleteStatus;
    delete deleteDialog.dataset.errorMessage;
});

function deleteQuiz(id) {
    if (!id) {
        return $.Deferred().reject(new Error("Quiz ID is required for deletion"));
    }

    return $.ajax({
        url: `${window.ctx}/admin/quiz/${id}`,
        type: 'DELETE'
    });
}



document.querySelectorAll('.edit-quiz-btn').forEach(btn => {
    btn.addEventListener('click', async () => {
        console.log(btn.dataset.quizId);
        let quizEditor = new QuizEditor({quizId: btn.dataset.quizId, contextPath: window.ctx});
        let questionEditor = new QuestionEditor({quizId: btn.dataset.quizId, contextPath: window.ctx});

        quizEditor.fetchAndFillEditWindow().then(() => {
            questionEditor.fetchAndFillQuestions(quizEditor);
        });
    })
})

function showAlert(message, variant = 'primary', icon = 'info-circle', duration = 4000) {
    const alertElement = document.querySelector('#globalAlert');
    const iconElement = alertElement?.querySelector('#globalAlertIcon');
    const messageElement = alertElement?.querySelector('#globalAlertMessage');

    if (!alertElement) {
        console.error('Could not find #globalAlert element. Make sure it exists in your HTML.');
        return;
    }

    alertElement.hide();

    alertElement.variant = variant;
    alertElement.duration = duration;
    if (iconElement) iconElement.name = icon;
    if (messageElement) messageElement.textContent = message;

    setTimeout(() => alertElement.show(), 50);
}

function showSuccess(message) {
    showAlert(message, 'success', 'check2-circle', 4000);
}

function showError(message) {
    showAlert(message, 'danger', 'exclamation-octagon', 5000);
}