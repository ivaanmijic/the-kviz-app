class Quiz{
    constructor(id, img, title, category, visibility, description) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.category = category;
        this.visibility = visibility;
        this.description = description;
    }

}

class Question {
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

class QuizApi {
    constructor(basePath = ''){
        this.base = basePath;
    }

    delete(id){
        return fetch(`${this.base}/admin/deleteQuiz?id=${id}`, {
            method: 'DELETE'
        }).then(resp => {
            if(resp.ok) {
                window.location.href = '/admin/home';
            }
                console.log("SEnding get")
                fetch(`${this.base}/admin/home`, {
                    method: 'GET'
                })
                    .then(resp => resp.text())
                    .then(html => document.getElementById("changeablePart").innerHTML = html
            )}
        )
    }
}

class EditQuizController {
    constructor({quizId, api, contextPath}) {
        this.api = api;
        this.base = contextPath;
        this.quizId = quizId;
    }

    async fetchAndFillEditWindow(){
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
        console.log(this.quiz.visibility ? "public": "private")
        document.getElementById('deleteQuiz').style.display = 'block';
        document.getElementById('deleteQuiz').addEventListener('click', ()=>this.api.delete(this.quizId))
        document.getElementById('removeBtn').style.display = 'block';
        document.getElementById('plusIcon').style.display = 'none';
        document.getElementById('uploadBox').style.border = 'none';
        document.getElementById('quizTitle').value = this.quiz.title;
        document.getElementById('quizCategory').value = this.quiz.category.toLowerCase();
        document.getElementById('quizVisibility').value = this.quiz.visibility ? "public": "private";
        document.getElementById('quizDescription').value = this.quiz.description;
        document.getElementById('submitQuiz').innerText = 'Edit Quiz';
    }

    async _getQuizInfo(base, quizId){
        try {
            let result = await fetch(`${base}/admin/quiz-information?id=${quizId}`, {
                method: 'GET',
                headers: {'Content-Type': 'application/json'},
            })
            console.log(result);
            const {id, title, thumbnail, description, category, visible} = await result.json();
            return new Quiz(id, thumbnail, title, category, visible, description);

        }catch(err){
            console.log(err);
        }
    }

    async _getQuizCreateQuiz(base){
        await fetch(`${base}/admin/create-quiz-window`, {
            method: 'GET',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        })
            .then(response => response.text())
            .then(html =>{
                console.log(html);
                const editDialog = document.getElementById('editQuizDialog');
                editDialog.innerHTML = html;
                editDialog.show();
                this.quizForm = new QuizForm(this.quizId);
                console.log(this.quizForm)
            })
            .catch(error => console.log(error));
    }

    addQuestion(question){
        this.quizForm.addQuestion(question);
    }
}

class QuestionController {
    constructor({quizId, api, contextPath}){
        this.api = api;
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

    async _getQuestionsInfo(base, quizId){
         try{
             let result = await fetch(`${base}/admin/questions-info?id=${quizId}`, {
                 method: 'GET',
                 headers: {'Content-Type': 'application/x-www-form-urlencoded'},
             })
             console.log(result);
             let questionArray = await result.json();
             console.log(questionArray);
             return questionArray.map((qJsonData) => new Question(qJsonData));
         }catch(err){
             console.log(err);
         }
    }
}


document.querySelectorAll('.edit-quiz-btn').forEach(btn => {
    btn.addEventListener('click', async () => {
        const api = new QuizApi(contextPath);
        console.log(btn.dataset.quizId);
        let quizCnt = new EditQuizController({
            quizId: btn.dataset.quizId,
            api: api,
            contextPath: contextPath});
        let questionCnt = new QuestionController({
            quizId: btn.dataset.quizId,
            api: api,
            contextPath: contextPath});
        quizCnt.fetchAndFillEditWindow().then(()=>{
            questionCnt.fetchAndFillQuestions(quizCnt);
        });
    })
})
