export class QuestionViewScreen{
    constructor(manager, ws) {
        this.manager = manager;
        this.ws = ws;
    }

    init() {
        document.querySelectorAll(".answers").forEach((element)=>{
            element.addEventListener('click', () => {
                let answer = element.querySelector("span").innerText;
                this.ws.send({"type":"answer","answer": answer});
            })
        })
    }
}