let sock = new SockJS("http://localhost:8080/websocket");

// Create a new StompClient object with the WebSocket endpoint
let stompClient = Stomp.over(sock);

const tableLabels = [];
const tableValues = [];

stompClient.connect({}, frame => {
    setConnected(true);
});

function setConnected(connected) {
    $("#rates").html("");
}

// CHART
let exchangeRateChart;
document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('exchangeRateChart')
    exchangeRateChart = new Chart(ctx, {
                         type: 'line',
                         data: {
                           labels: tableLabels,
                           datasets: [{
                             label: 'Exchange rate',
                             data: tableValues,
                             borderWidth: 1
                           }]
                         },
                         options: {
                            animations: {
                               tension: {
                                 duration: 1000,
                                 easing: 'linear',
                                 from: 1,
                                 to: 0,
                                 loop: true
                               }
                            },
                         }
                      });
});

function showRates(message, messageJson) {
    $("#rates").append("<tr><td>" + message + "</td></tr>");

    tableLabels.push(messageJson.date);
    tableValues.push(messageJson.value);
    // Update the chart dataset
    exchangeRateChart.update();
}

function sendMessage(){

    let input = document.getElementById("currency");
    let topic = input.value;

    const subscription = `/topic/${topic}`;
    stompClient.subscribe(subscription, payload => {
        const content = JSON.parse(payload.body).content;
        showRates(JSON.stringify(content), content);
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
