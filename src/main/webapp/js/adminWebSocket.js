const gameCode = document.getElementById("gameId").innerText;

const socket = new WebSocket(
    `ws://localhost:8080/quiz?gameId=${gameCode}&role=admin`
);

// handle open
socket.onopen = () => {
    console.log("Admin connected to WS");
};

// handle messages from server
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
                    const person = document.createElement("div")
                    person.className = "flex items-center bg-gray-100 p-3 rounded-lg"
                    person.innerHTML = this.createAndFillPersonInfo(player, index);
                    cont.appendChild(person);
                })
            })
    }
};

// handle close
socket.onclose = () => {
    console.log("Admin WS closed");
};

// handle error
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
        .then(html => document.getElementById("replaceable").innerHTML = html);
})

function createAndFillPersonInfo(player, index) {
    return '<span class="font-bold w-8">' + index + '</span>' +
        '<span class="flex-grow font-semibold text-left">'+ player.name +'</span>' +
        '<span class="font-bold text-lg w-20 text-right">' + player.points + '</span>';
}