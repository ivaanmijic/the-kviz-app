export class WaitingScreen{
    constructor(manager, ws) {
        this.manager = manager;
        this.ws = ws;
    }

    init() {
        document.getElementById("numberOfPlayers").innerText = this.ws.getPlayerCount();
    }
}