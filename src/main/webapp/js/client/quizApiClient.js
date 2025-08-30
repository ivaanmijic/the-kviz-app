export class QuizApiClient {
    constructor(contextPath) {
        this.base = contextPath;
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

    startGameForQuiz(quizId) {
        if (!quizId)
            return $.Deferred()
                .reject(new Error("Quiz ID is required for playing quiz"))
                .promise();

        window.location.href = `${this.base}/admin/startGame?quizId=${quizId}`;
    }
}