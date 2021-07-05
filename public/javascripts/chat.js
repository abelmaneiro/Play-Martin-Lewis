/**
 * Code for WebSocket chat application
 */
const inputField = document.getElementById("chat-input");
const outputArea = document.getElementById("chat-area");

const socketRoute = document.getElementById("ws-route")
const socket = new WebSocket(socketRoute.value.replace("http", "ws"));  // Creates new client WebSocket to Server given in route

socket.onopen = (event) => {
    socket.send("New user connected.");
}

socket.onmessage = (event) => {
    outputArea.value += "\n" + event.data;
}

inputField.onkeydown = (event) => {
    if (event.key === 'Enter') {
        socket.send(inputField.value);
        inputField.value = "";
    }
}


