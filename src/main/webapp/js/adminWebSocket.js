const gameCode = document.getElementById("gameId").innerText;

const socket = new WebSocket(
    `ws://localhost:8080/quiz?gameId=${gameCode}&role=admin`
);

socket.onopen = () => {
    console.log("Admin connected to WS");
};

socket.onmessage = (event) => {
    const data = JSON.parse(event.data);

    if (data.type === "playerCount") {
        document.getElementById("playerCount").innerText = data.count;
    }
    if(data.type === "question"){
        fetch(window.ctx + "/admin/nextQuestion")
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
    }
    if(data.type === "endQuestion"){
        fetch(window.ctx + "/getLeaderboardView")
            .then(resp => resp.text())
            .then(html => document.getElementById("replaceable").innerHTML = html)
            .then(()=>{
                document.getElementById("nextQuestion").style.display = 'block';
                const playerList = data.players;
                const cont = document.getElementById("leaderboardCont")
                playerList.forEach((player, index) =>{
                    if(index>=10)return;
                    const person = document.createElement("div")
                    person.className = "flex items-center bg-gray-100 p-3 rounded-lg"
                    person.innerHTML = this.createAndFillPersonInfo(player, index);
                    cont.appendChild(person);
                })
            })
    }
    if(data.type === "finalLeaderboard"){
        fetch(window.ctx + "/finalLeaderboard")
            .then(resp => resp.text())
            .then(html => document.getElementById("replaceable").innerHTML = html)
            .then(()=>{
                const playerList = data.players
                const cont = document.getElementById("leaderboardCont")
                playerList.forEach((player, index) => {
                    if (index >= 10) return;
                    let person;
                    switch(index){
                        case 0:
                            person = firstPlace(player)
                            break;
                        case 1:
                            person = secondPlace(player)
                            break;
                        case 2:
                            person = thirdPlace(player)
                            break;
                        default:
                            person = restPlayers(player, index)
                    }
                    cont.appendChild(person)
                })
            })
    }
};

socket.onclose = () => {
    console.log("Admin WS closed");
};

socket.onerror = (err) => {
    console.error("WS error:", err);
};

document.getElementById("startQuiz").addEventListener('click', () =>{
        fetch(window.ctx + '/admin/nextQuestion')
            .then(resp => resp.text())
            .then(html => document.getElementById("replaceable").innerHTML = html)
            .then(()=>{
                socket.send("{\"type\":\"nextQuestion\"}");
            })
})

document.getElementById("nextQuestion").addEventListener('click', () => {
    document.getElementById("nextQuestion").style.display = 'none';
    fetch(window.ctx + '/admin/nextQuestion')
        .then(resp => resp.text())
        .then(html => document.getElementById("replaceable").innerHTML = html)
        .then(()=>{
             socket.send("{\"type\":\"nextQuestion\"}");
        })
})

function createAndFillPersonInfo(player, index) {
    return '<span class="font-bold w-8">' + index + '</span>' +
        '<span class="flex-grow font-semibold text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg w-20 text-right">' + player.points + '</span>';
}
function firstPlace(player) {
    const firstPlace = document.createElement("div");
    firstPlace.className = "flex items-center p-4 rounded-lg bg-gradient-to-r from-amber-300 to-yellow-400 text-yellow-900 shadow-lg"
    firstPlace.innerHTML = '<span class="text-2xl font-bold w-10">🥇</span>' +
        '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg">'+ player.points +'</span>';
    return firstPlace;
}

function secondPlace(player) {
    const secondPlace = document.createElement("div");
    secondPlace.className = "flex items-center p-4 rounded-lg bg-gradient-to-r from-slate-200 to-gray-300 text-gray-800 shadow-lg"
    secondPlace.innerHTML = '<span class="text-2xl font-bold w-10">🥈</span>' +
        '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg">'+ player.points +'</span>';
    return secondPlace
}

function thirdPlace(player) {
    const thirdPlace = document.createElement("div");
    thirdPlace.className = "flex items-center p-4 rounded-lg bg-gradient-to-r from-amber-400 to-yellow-600 text-white shadow-lg"
    thirdPlace.innerHTML = '<span class="text-2xl font-bold w-10">🥉</span>' +
        '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg">'+ player.points +'</span>';
    return thirdPlace
}

function restPlayers(player, index) {
    const rest = document.createElement("div");
    rest.className = "flex items-center p-3 rounded-lg bg-white"
    rest.innerHTML = '<span class="text-2xl font-bold w-10">'+ (index+1) +'.</span>' +
        '<span class="flex-grow font-bold text-lg text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg">'+ player.points +'</span>';
    return rest
}