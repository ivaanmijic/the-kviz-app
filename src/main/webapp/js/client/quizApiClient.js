export class QuizApiClient {
    constructor(baseUrl = '') {
        this.base = baseUrl;
    }

    getList(adminId = null) {
        const params = {}
        if (adminId && adminId !== '') params.admin_id = adminId;
        return $.get(`${this.base}/admin/quiz/list`, params)
    }

    getPublicList() {
        return $.get(`${this.base}/admin/quiz/list/public`, {});
    }

    delete(quizId) {
        if (!quizId) {
            return $.Deferred().reject(new Error("Quiz ID is required for deletion.")).promise();
        }

        return $.ajax({
            url: `${this.base}/admin/quiz/${quizId}`,
            type: 'DELETE'
        });
    }
}