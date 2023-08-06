let sock = new SockJS("http://localhost:8080/websocket");

// Create a new StompClient object with the WebSocket endpoint
let stompClient = Stomp.over(sock);

stompClient.connect({}, frame => {
    setConnected(true);
});

function setConnected(connected) {
    $("#rates").html("");
}

function showRates(message) {
    $("#rates").append("<tr><td>" + message + "</td></tr>");
}

function sendMessage(){

    let input = document.getElementById("currency");
    let topic = input.value;

    const subscription = `/topic/${topic}`;
    stompClient.subscribe(subscription, payload => {
        const content = JSON.parse(payload.body).content;
        showRates(JSON.stringify(content));
    });

    stompClient.send('/app/subscribe', {}, JSON.stringify({message: topic}));

}

function updateButtonStatus() {
    const input = document.getElementById('currency');
    const button = document.getElementById('connect');

    if (input.value.trim() !== '') {
        button.removeAttribute('disabled');
    } else {
        button.setAttribute('disabled', true);
    }
}