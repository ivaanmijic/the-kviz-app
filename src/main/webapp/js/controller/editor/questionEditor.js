import { Question } from "../../model/question.js";

export class QuestionEditor {
    constructor({quizId, contextPath}) {
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