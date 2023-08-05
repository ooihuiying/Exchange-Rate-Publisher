let sock = new SockJS("http://localhost:8080/websocket");

// Create a new StompClient object with the WebSocket endpoint
let stompClient = Stomp.over(sock);

stompClient.connect({}, frame => {
    setConnected(true);
    stompClient.subscribe("/topic/rates", payload => {
        showGreeting(JSON.parse(payload.body).content);
    });
});

function setConnected(connected) {
    $("#rates").html("");
}

function showGreeting(message) {
    $("#rates").append("<tr><td>" + message + "</td></tr>");
}