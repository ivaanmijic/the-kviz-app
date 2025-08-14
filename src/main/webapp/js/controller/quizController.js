import { Quiz } from "../model/quiz.js";
import { QuizApiClient } from "../client/quizApiClient.js";
import { AlertManager } from "../manager/alertManager.js";

export class QuizController {
    constructor(baseUrl = '', adminId = null) {
        this.apiClient = new QuizApiClient(baseUrl);
        this.init();
        this._initializeDeleteDialogListeners();
        this._initializeDescriptionModalListeners();
    }

    init() {}

    _initializeDeleteDialogListeners() {
        const deleteDialog = document.querySelector('#deleteQuizDialog');
        if (!deleteDialog) {
            console.warn('Delete dialog for quizzes (#deleteQuizDialog) not found.');
            return;
        }

        const confirmButton = deleteDialog.querySelector('#confirmDeleteBtn');
        const cancelButton = deleteDialog.querySelector('#cancelDeleteBtn');

        if (!confirmButton || !cancelButton) {
            console.error('Confirm or Cancel button not found in #deleteQuizDialog.');
            return;
        }

        confirmButton.addEventListener('click', async () => {
            const quizId = deleteDialog.dataset.quizId;
            if (!quizId) return;

            confirmButton.loading = true;
            cancelButton.disabled = true;

            try {
                await this.apiClient.delete(quizId);
                deleteDialog.dataset.deleteStatus = 'success';
            } catch (jqXHR) {
                deleteDialog.dataset.deleteStatus = 'fail';
                const errorMsg = jqXHR.responseJSON?.error || 'Failed to delete quiz';
                deleteDialog.dataset.errorMessage = errorMsg;
            } finally {
                deleteDialog.hide();
            }
        });

        cancelButton.addEventListener('click', () => deleteDialog.hide());

        deleteDialog.addEventListener('sl-after-hide', () => {
            const quizId = deleteDialog.dataset.quizId;
            const quizTitle = deleteDialog.dataset.quizTitle;

            if (deleteDialog.dataset.deleteStatus === 'success') {
                document.querySelector(`.quiz-card-wrapper[data-quiz-id='${quizId}']`)?.remove();
                AlertManager.showSuccess(`Successfully deleted quiz: ${quizTitle}`);
            } else if (deleteDialog.dataset.deleteStatus === 'fail') {
                AlertManager.showError(deleteDialog.dataset.errorMessage);
            }

            confirmButton.loading = false;
            cancelButton.disabled = false;
            delete deleteDialog.dataset.quizId;
            delete deleteDialog.dataset.quizTitle;
            delete deleteDialog.dataset.deleteStatus;
            delete deleteDialog.dataset.errorMessage;
        });
    }

    _initializeDescriptionModalListeners() {
        const descriptionModal = document.querySelector('#descriptionModal');
        if (!descriptionModal) {
            console.warn('Description modal (#descriptionModal) not found.');
            return;
        }
        const closeButton = descriptionModal.querySelector('sl-button[slot="footer"]');
        if (closeButton) {
            closeButton.addEventListener('click', () => descriptionModal.hide());
        }
    }

    getQuizzes(adminId = null) {
        return this.apiClient.getList(adminId)
            .then(data => {
                const quizzes = data.map(Quiz.fromJson);
                const sectionTitle = adminId ? "My Quizzes" : "All Quizzes";
                return this.renderQuizzes(quizzes, sectionTitle, true);
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

    getPublicQuizzes() {
        return this.apiClient.getPublicList()
            .then(data => {
                const quizzes = data.map(Quiz.fromJson);
                const sectionTitle = "Public Quizzes";
                return this.renderQuizzes(quizzes, sectionTitle, false);
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

    async renderQuizzes(quizzes, sectionTitleText, showAdminActions) {
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
                let adminActionsHtml = '';
                if (showAdminActions) {
                    adminActionsHtml = `
                       <sl-button variant="neutral" data-action="edit" data-quiz-id="${quiz.id}">Edit</sl-button>
                       <sl-button class="danger-text-btn" variant="text" data-action="delete" data-quiz-id="${quiz.id}" data-quiz-title="${quiz.title}">
                            <sl-icon slot="suffix" name="trash"></sl-icon>
                            Delete
                       </sl-button>
                   `;
                }

                const description = quiz.description || 'No description available.';
                const cardHTML = cardTemplate
                    .replace('{{Thumbnail}}', `${window.ctx}/${quiz.thumbnail}`)
                    .replace('{{Title}}', quiz.title)
                    .replace(/{{Description}}/g, description)
                    .replace('{{QuestionCount}}', quiz.questions.length)
                    .replace(/{{QuizID}}/g, quiz.id)
                    .replace('{{AdminActions}}', adminActionsHtml);

                const cardWrapper = document.createElement('div');
                cardWrapper.className = 'quiz-card-wrapper';
                cardWrapper.dataset.quizId = quiz.id;
                cardWrapper.innerHTML = cardHTML;

                grid.appendChild(cardWrapper);
            });

            grid.addEventListener('click', (e) => {
                const button = e.target.closest('sl-button[data-action]');
                if (!button) return;

                const quizId = button.dataset.quizId;
                const action = button.dataset.action;

                if (action === 'details') {
                    const quiz = quizzes.find(q => String(q.id) === quizId);
                    if (quiz) {
                        this.showDetailsModal(quiz.title, quiz.description);
                    }
                } else if (action === 'delete') {
                    const quizTitle = button.dataset.quizTitle;
                    this.showDeleteDialog(quizId, quizTitle);
                } else if (action === 'edit') {
                    console.log(`Edit quiz ${quizId}`);
                } else if (action === 'start') {
                    console.log(`Start quiz ${quizId}`);
                }
            });

        } catch (error) {
            console.error('Failed to render quizzes', error);
            AlertManager.showError('Could not display the quizzes');
        }

        section.appendChild(grid);
        return section;
    }

    showDeleteDialog(quizId, quizTitle) {
        const deleteDialog = document.querySelector('#deleteQuizDialog');
        if (!deleteDialog) {
            console.error('Delete dialog element #deleteQuizDialog not found');
            AlertManager.showError('Error: Could not open the delete confirmation dialog');
            return;
        }

        const titleSpan = deleteDialog.querySelector('#deleteQuizTitle');
        if (titleSpan) {
            titleSpan.textContent = quizTitle;
        }

        deleteDialog.dataset.quizId = quizId;
        deleteDialog.dataset.quizTitle = quizTitle;
        deleteDialog.show();
    }

    showDetailsModal(title, description) {
        const modal = document.querySelector('#descriptionModal');
        if (!modal) {
            console.error('Description modal element #descriptionModal not found');
            AlertManager.showError('Error: Could not open the details dialog');
            return;
        }

        modal.label = title;
        const contentEl = modal.querySelector('#modalDescriptionContent');
        if (contentEl) {
            contentEl.textContent = description || 'No description available.';
        }

        modal.show();
    }
}
