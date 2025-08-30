import {Quiz} from "../../model/quiz.js";

export class QuizEditor {
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
        document.getElementById('submitQuiz').innerText = 'Save changes';
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