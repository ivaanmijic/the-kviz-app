import { QuizEditor } from "./editor/quizEditor.js";
import { QuestionEditor } from "./editor/questionEditor.js";
import { AlertManager } from "../manager/alertManager.js";
import { QuizApiClient } from "../client/quizApiClient.js";

export class QuizController {
    constructor() {
        this.deleteDialog = null;
        this.deleteQuizTitle = null;
        this.confirmDeleteBtn = null;
        this.cancelDeleteBtn = null;
        this.descriptionDialog = null;
        this.modalDescriptionContent = null;
        this.api = new QuizApiClient(`${window.ctx}`);
    }

    setupListeners() {
        this.deleteDialog = document.getElementById('deleteQuizDialog');
        this.deleteQuizTitle = document.getElementById('deleteQuizTitle');
        this.confirmDeleteBtn = document.getElementById('confirmQuizDeleteBtn');
        this.cancelDeleteBtn = document.getElementById('cancelQuizDeleteBtn');
        this.descriptionDialog = document.getElementById('descriptionModal');
        this.modalDescriptionContent = document.getElementById('modalDescriptionContent');

        document.querySelectorAll(".read-more-btn").forEach(btn => {
            btn.addEventListener('click', () => {
                this.modalDescriptionContent.innerText = btn.dataset.quizDescription;
                this.descriptionDialog.show();
            });
        });

        document.querySelectorAll(".play-quiz-btn").forEach(btn => {
            btn.addEventListener('click', () => {
               const quizId = btn.dataset.quizId;
               this.api.startGameForQuiz(quizId)
                   .catch(e => AlertManager.showError("Failed to start quiz:", e));
            });
        });

        document.querySelectorAll('.delete-quiz-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                const quizId = btn.dataset.quizId;
                const quizTitle = btn.dataset.quizTitle;

                this.deleteDialog.dataset.quizId = quizId;
                this.deleteDialog.dataset.quizTitle = quizTitle || "this quiz";
                this.deleteQuizTitle.textContent = quizTitle || "this quiz";

                this.deleteDialog.show();
            });
        });

        this.confirmDeleteBtn.addEventListener('click', async () => {
            const quizId = this.deleteDialog.dataset.quizId;
            if (!quizId) return;

            this.confirmDeleteBtn.loading = true;
            this.cancelDeleteBtn.disabled = true;

            try {
                await this.api.delete(quizId);
                this.deleteDialog.dataset.deleteStatus = 'success';
            } catch (err) {
                this.deleteDialog.dataset.deleteStatus = 'fail';
                this.deleteDialog.dataset.errorMessage = "Could not delete quiz.";
            } finally {
                this.deleteDialog.hide();
            }
        });

        this.cancelDeleteBtn.addEventListener('click', () => {
            this.deleteDialog.hide();
        });

        this.deleteDialog.addEventListener('sl-after-hide', () => {
            const quizId = this.deleteDialog.dataset.quizId;
            const quizTitle = this.deleteDialog.dataset.quizTitle;

            if (this.deleteDialog.dataset.deleteStatus === 'success') {
                document.querySelector(`.quiz-card[data-quiz-id='${quizId}']`)?.remove();
                AlertManager.showSuccess(`Successfully deleted quiz: ${quizTitle}`);
            } else if (this.deleteDialog.dataset.deleteStatus === 'fail') {
                AlertManager.showError(this.deleteDialog.dataset.errorMessage);
            }

            this.confirmDeleteBtn.loading = false;
            this.cancelDeleteBtn.disabled = false;

            this.deleteDialog.dataset.quizId = null;
            this.deleteDialog.dataset.quizTitle = null;
            this.deleteDialog.dataset.deleteStatus = null;
            this.deleteDialog.dataset.errorMessage = null;
        });

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

    }

}