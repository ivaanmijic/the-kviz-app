export class InputNameScreen {
    constructor(manager, ws) {
        this.manager = manager;
        this.ws = ws;
    }

    init() {

        document.getElementById("startQuiz").addEventListener("click", () => {
            const name = document.getElementById("playerName").value.trim();
            if (name.length <= 0) {
                alert("Must enter the name");
                return;
            }

            this.ws.send({"type":"setUserName", "name":name});
            this.manager.loadScreen("waitingForGameStart", `/waitingLobby?gameId=${window.gameId}`)
        });
    }
}
