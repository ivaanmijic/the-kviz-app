import { Quiz } from "../model/quiz.js";
import { QuizApiClient } from "../client/quizApiClient.js";

export class QuizController {
    constructor(baseUrl = '', adminId = null) {
        this.apiClient = new QuizApiClient(baseUrl);
        this.init();
    }

    init() {}

    getQuizzes(adminId = null) {
        return this.apiClient.getList(adminId)
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
        this.clearAlerts()

        const grid = document.createElement('div');
        grid.classList.add('card-grid');
        grid.id = 'card-grid';

        quizzes.forEach(q => {
            const card = document.createElement('sl-card');
            card.classList.add('quiz-card');

            const img = document.createElement('img');
            img.slot = 'image';
            img.classList.add('quiz-card-img');
            img.src = `${window.ctx}/${q.thumbnail}`;
            card.appendChild(img);

            const h3 = document.createElement('h3');
            h3.className = 'text-lg font-bold';
            h3.textContent = q.title;
            card.appendChild(h3);

            const p = document.createElement('p');
            p.className = 'text-sm mt-1';
            p.textContent = 'Broj pitanja';
            card.appendChild(p);

            const buttonGroup = document.createElement('div');
            buttonGroup.className = 'mt-4 flex space-x-2';

            const btnStart = document.createElement("sl-button");
            btnStart.variant = "primary";
            btnStart.className = "flex-1";
            btnStart.textContent = "Start";

            const btnEdit = document.createElement("sl-button");
            btnEdit.variant = "neutral";
            btnEdit.textContent = "Edit";

            const btnDelete = document.createElement("sl-button");
            btnDelete.variant = "danger";
            btnDelete.setAttribute("outline", "");
            btnDelete.textContent = "Delete";

            buttonGroup.append(btnStart, btnEdit, btnDelete);
            card.appendChild(buttonGroup);

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