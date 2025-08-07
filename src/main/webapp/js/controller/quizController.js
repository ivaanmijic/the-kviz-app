import { Quiz } from "../model/quiz.js";
import { QuizApiClient } from "../client/quizApiClient.js";

export class QuizController {
    constructor(baseUrl = '', adminId = null) {
        this.apiClient = new QuizApiClient(baseUrl);
        this.adminId = adminId;
        this.init();
    }

    init() {}

    getQuizzes() {
        return this.apiClient.getList(this.adminId)
            .then(data => {
                const quizzes = data.map(Quiz.fromJson);
                return this.renderQuizzes(quizzes);
            })
            .catch(err => {
                let msg = 'Failed to load quizzes';
                try {
                    const json = JSON.parse(err.responseText);
                    if (json.error) msg = json.error;
                } catch (e) {}
                this.showError(msg);
                console.error('Quiz API error', err);
                return null;
            });
    }

    renderQuizzes(quizzes) {
        const grid = document.createElement('div');
        grid.classList.add('card-grid');
        grid.id = 'card-grid';

        quizzes.forEach(q => {
            const card = document.createElement('sl-card');
            card.classList.add('quiz-card');

            const img = document.createElement('img');
            img.slot = 'image';
            img.classList.add('quiz-card-img');
            img.src = `${window.ctx}/uploads/quizImages/${q.thumbnail}.jpg`;
            card.appendChild(img);

            const title = document.createElement('div');
            title.classList.add('title');
            title.textContent = q.title;
            card.appendChild(title);

            const subtitle = document.createElement('div');
            subtitle.classList.add('subtitle');
            subtitle.textContent = q.description;
            card.appendChild(subtitle);

            const editBtn = document.createElement('sl-icon-button');
            editBtn.name = 'pen';
            editBtn.classList.add('edit-btn');
            editBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                this.editQuiz(q);
            });
            card.appendChild(editBtn);

            const btn = document.createElement('sl-button');
            btn.setAttribute('variant', 'text');
            btn.setAttribute('size', 'medium');
            btn.classList.add('desc-button');
            btn.textContent = 'More...';
            btn.addEventListener('click', () => this.showDetails(q));
            card.appendChild(btn);

            grid.appendChild(card);
        });

        return grid;
    }

    showError(message) {
        let alert = document.getElementById('errorAlert');
        if (alert) {
            alert.innerHTML = `<sl-icon slot=\"icon\" name=\"exclamation-octagon\"></sl-icon> ${message}'`;
        }
        alert.show();
    }

    clearAlerts() {
        const alert = document.getElementById('errorAlert');
        if (alert) {
            alert.hide();
        }
    }
}