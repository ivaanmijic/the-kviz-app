export class InputCodeScreen {
    constructor(manager, ws) {
        this.manager = manager;
        this.ws = ws;
    }

    init() {
        document.getElementById("joinBtn").addEventListener("click", () => {

            const code = document.getElementById("quizId").value.trim();
            if (code.length !== 6) {
                alert("Code must be 6 characters");
                return;
            }

            this.ws.connect("ws://localhost:8080/quiz?gameId=" + code + "&role=player");
        });
    }
}
