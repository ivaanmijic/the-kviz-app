export class PlayerQuizSocket {
    constructor(manager) {
        this.ws = null;
        this.manager = manager;
    }

    connect(url) {
        console.log(url);
        this.ws = new WebSocket(url);

        this.ws.onopen = () => {
            console.log("Connected to server");
        };

        this.ws.onmessage = (msg) => {
            const data = JSON.parse(msg.data);
            this.handleMessage(data);
        };

        this.ws.onclose = () => console.log("Connection closed");
    }

    handleMessage(data) {
        switch (data.type) {
            case "error":
                alert(data); // "Invalid code"
                this.ws.close(); // close manually
                break;
            case "codeOk":
                this.manager.loadScreen("inputName", "/enterName")
                    .then(()=>{
                        window.gameId = data.gameId;
                        document.getElementById("quizTitle").innerText = data.quizTitle;
                    })
                break;
            case "playerCount":
                document.getElementById("numberOfPlayers").innerText = data.count;
                break;
            case "question":
                this.manager.loadScreen("question", `/getQuestionView`)
                    .then(() => {
                        const deadline = data.deadline; // millis from server
                        const duration = data.time * 1000; // ms
                        const progressBar = document.querySelector("#progress");
                        document.getElementById("questionText").innerText = data.question;
                        document.getElementById("answ0").innerText = data.answers[0];
                        document.getElementById("answ1").innerText = data.answers[1];
                        document.getElementById("answ2").innerText = data.answers[2];
                        document.getElementById("answ3").innerText = data.answers[3];
                        const interval = setInterval(() => {
                            const now = Date.now();
                            const remaining = Math.max(0, deadline - now);
                            progressBar.value = (remaining / duration) * 100;

                            if (remaining <= 0) {
                                clearInterval(interval);
                            }
                        }, 100);
                    })
                break;
            case "endQuestion":
                this.manager.loadScreen("leaderboard", '/getLeaderboardView')
                    .then(()=>{
                        const playerList = data.players;
                        const cont = document.getElementById("leaderboardCont")
                        playerList.forEach((player, index) =>{
                            const person = document.createElement("div")
                            person.className = "flex items-center bg-gray-100 p-3 rounded-lg"
                            person.innerHTML = this.createAndFillPersonInfo(player, index);
                            cont.appendChild(person);
                        })
                    })
                break;
            case "answerReceived":
                this.manager.loadScreen("waiting", '/waitingLobby')
                    .then(()=>{
                        document.getElementById("waitingReason").innerText = "Sacekajte sledece pitanje..."
                        if(data.correctAnswer === 'yes'){
                            document.getElementById("correctness").innerText = "Correct answer";
                        }else{
                            document.getElementById("correctness").innerText = "Wrong answer";
                        }
                    })
                break;
        }
    }

    send(payload) {
        this.ws.send(JSON.stringify(payload));
    }

    createAndFillPersonInfo(player, index) {
        return '<span class="font-bold w-8">' + index+1 + '</span>' +
        '<span class="flex-grow font-semibold text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg w-20 text-right">' + player.points + '</span>';
    }
}

