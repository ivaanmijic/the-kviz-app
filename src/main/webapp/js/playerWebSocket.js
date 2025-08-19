function downloadLeaderboard(leaderboard) {
    const ws = XLSX.utils.json_to_sheet(leaderboard);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Leaderboard");
    XLSX.writeFile(wb, "leaderboard.xlsx");
}

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
                        playerList.every((player, index) =>{
                            if(index>=5)return false;
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
            case "finalLeaderboard":
                this.manager.loadScreen("finalLeaderboard", '/finalLeaderboard')
                    .then(()=>{
                        const playerList = data.players
                        const cont = document.getElementById("leaderboardCont")
                        playerList.every((player, index) => {
                            let person;
                            switch(index){
                                case 0:
                                    person = this.firstPlace(player)
                                    break;
                                case 1:
                                    person = this.secondPlace(player)
                                    break;
                                case 2:
                                    person = this.thirdPlace(player)
                                    break;
                                default:
                                    person = this.restPlayers(player, index)
                            }
                            if(player.name === window.myName) {
                                person.classList.add("ring-4")
                                person.classList.add("ring-indigo-500")
                            }
                            cont.appendChild(person)
                        })
                        document.getElementById("downloadXLS").addEventListener('click', ()=>{
                            downloadLeaderboard(playerList)
                        })

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

    firstPlace(player) {
        const firstPlace = document.createElement("div");
        firstPlace.className = "flex items-center p-4 rounded-lg bg-gradient-to-r from-amber-300 to-yellow-400 text-yellow-900 shadow-lg"
        firstPlace.innerHTML = '<span class="text-2xl font-bold w-10">🥇</span>' +
        '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg">'+ player.points +'</span>';
        return firstPlace;
    }

    secondPlace(player) {
        const secondPlace = document.createElement("div");
        secondPlace.className = "flex items-center p-4 rounded-lg bg-gradient-to-r from-slate-200 to-gray-300 text-gray-800 shadow-lg"
        secondPlace.innerHTML = '<span class="text-2xl font-bold w-10">🥈</span>' +
        '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg">'+ player.points +'</span>';
        return secondPlace
    }

    thirdPlace(player) {
        const thirdPlace = document.createElement("div");
        thirdPlace.className = "flex items-center p-4 rounded-lg bg-gradient-to-r from-amber-400 to-yellow-600 text-white shadow-lg"
        thirdPlace.innerHTML = '<span class="text-2xl font-bold w-10">🥉</span>' +
            '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
            '<span class="font-bold text-lg">'+ player.points +'</span>';
        return thirdPlace
    }

    restPlayers(player, index) {
        const rest = document.createElement("div");
        rest.className = "flex items-center p-3 rounded-lg bg-white"
        rest.innerHTML = '<span class="text-2xl font-bold w-10">'+ index+1 +'.</span>' +
            '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
            '<span class="font-bold text-lg">'+ player.points +'</span>';
        return rest
    }
}

