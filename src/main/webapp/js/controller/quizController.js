import { Quiz } from "../model/quiz.js";
import { QuizApiClient } from "../client/quizApiClient.js";
import { AlertManager } from "../manager/alertManager.js";

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
                return this.renderQuizzes(quizzes, adminId ? "My Quizzes" : "All Quizzes");
            })
            .catch(err => {
                let msg = 'Failed to load quizzes';
                try {
                    const json = JSON.parse(err.responseText);
                    if (json.error) msg = json.error;
                } catch (e) {}
                AlertManager.showError(msg);
                console.error('Quiz API error', err);
                return null;
            });
    }

    async renderQuizzes(quizzes, sectionTitleText) {
        const section = document.createElement('section');
        const sectionTitle = document.createElement('h2');
        sectionTitle.classList.add('section-title');
        sectionTitle.textContent = sectionTitleText;
        section.appendChild(sectionTitle);

        const grid = document.createElement('div');
        grid.classList.add('card-grid');
        grid.id = 'card-grid';

        try {
            const response = await fetch(`${window.ctx}/templates/quiz-card.html`);
            if (!response.ok) throw new Error('Failed to load quiz card template');
            const cardTemplate = await response.text();

            quizzes.forEach((quiz) => {
               const cardHTML = cardTemplate
                   .replace('{{Thumbnail}}', `${window.ctx}/${quiz.thumbnail}`)
                   .replace('{{Title}}', quiz.title);

               const tempDiv = document.createElement('div');
               tempDiv.innerHTML = cardHTML;
               grid.appendChild(tempDiv.firstElementChild);
            });
        } catch (error) {
            console.error('Failed to render quizzes', error);
            AlertManager.showError('Could not display the quizzes');
        }

        section.appendChild(grid);
        return section;
    }
}