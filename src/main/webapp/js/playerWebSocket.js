document.getElementById("joinBtn").addEventListener("click", () => {
    const code = document.getElementById("quizId").value.trim();

    if (code.length !== 6) {
        alert("Code must be 6 characters");
        return;
    }

    // Save to session storage for later pages
    sessionStorage.setItem("quizCode", code);

    // Try connecting to WS immediately
    const socket = new WebSocket(`ws://localhost:8080/quiz?gameId=${code}&role=player`);

    socket.onopen = () => {
        // Save the socket globally
        window.quizSocket = socket;
    };

    socket.onmessage = (event) =>{
        const msg = JSON.parse(event.data);
        if (msg.type === "error") {
            alert(msg.message); // "Invalid code"
            ws.close(); // close manually
        }else if(msg.type === "ok"){
            fetch(window.ctx + "/enterName")
                .then(resp => resp.text())
                .then(html => document.body.innerHTML = html)
        }
    }

    socket.onerror = () => {
        alert("Invalid game code or server not available");
    };
});