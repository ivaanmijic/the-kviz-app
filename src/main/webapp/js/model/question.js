export class Question {
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