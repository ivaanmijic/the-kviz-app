export class QuestionMultiViewScreen {
    constructor(manager, ws) {
        this.manager = manager;
        this.ws = ws;
        this.selectedAnswers = new Set();
    }

    init() {

        document.querySelectorAll('.answers-toggle').forEach(btn => {
            btn.addEventListener('click', () => {
                const answer = btn.querySelector("span").innerText;
                console.log("clicke on " + answer);
                if (this.selectedAnswers.has(answer)) {
                    this.selectedAnswers.delete(answer);
                    btn.outline = false;       // optional visual feedback
                    btn.style.opacity = '1';
                } else {
                    this.selectedAnswers.add(answer);
                    btn.outline = true;        // optional visual feedback
                    btn.style.opacity = '0.2';
                }
            });
        });

// submit answers
        document.getElementById('submitMultipleBtn').addEventListener('click', () => {
            console.log("submit fired")
            if (this.selectedAnswers.size === 0) {
                console.log("none selected")
                return;
            } // prevent empty submission

            const answersArray = Array.from(this.selectedAnswers);
            console.log("Selected answers:", answersArray);

            // send via websocket
            this.ws.send({
                type: "answers",
                answer: answersArray
            });

            // disable buttons after submission
            document.querySelectorAll('.answer-toggle, #submitMultipleBtn').forEach(el => el.disabled = true);
        });
    }
}