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
    const msg = JSON.parse(event.data);
    console.log("Message:", msg);

    if (msg.type === "playerCount") {
        document.getElementById("playerCount").innerText = msg.count;
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