window.addEventListener("load", toggle_connection, false);
var websocket;
var content = document.getElementById("status");

var ENTER="!ENTER-->"+localStorage.getItem("id");
var EXIT="!EXIT";
var PING="!PING";

var server = "ws://alpha:8080";

var timerID = 0;
function keepAlive() {
    var timeout = 20000;
    if (websocket.readyState == websocket.OPEN) {
        websocket.send('!PING');
    }
    timerId = setTimeout(keepAlive, timeout);
}
function cancelKeepAlive() {
    if (timerId) {
        clearTimeout(timerId);
    }
}

function connect()
{

    websocket = new WebSocket(server);
    showScreen('<b>Connecting to chat</b>');
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
};

function disconnect() {
    websocket.close();
};

function toggle_connection(){
    if (websocket && websocket.readyState == websocket.OPEN) {
        disconnect();
    } else {
        connect();
    };
};

function sendTxt(msg) {
    if (websocket.readyState == websocket.OPEN) {
        websocket.send(msg);
        showScreen('message sent');
    } else {
        showScreen('websocket is not connected');
    };
};

function onOpen(evt) {
    showScreen('<span style="color: green;">CONNECTED </span>');
    sendTxt(ENTER);
    keepAlive();
};

function onClose(evt) {
    showScreen('<span style="color: red;">DISCONNECTED</span>');
};

function onMessage(evt) {
    const words = evt.data.split('^'); //[0]->ora [1]->recieve [2]->Msg
    if(words.length==3){
        var dateMillis = parseInt(words[0].replace(/[\(\)']+/g,''));

        addMessage(parseInt(words[1]),dateMillis,words[2],false);
        showScreen('<span style="color: blue;">New message</span>');
    } else{
        showScreen('<span style="color: blue;">RESPONSE: ' + evt.data + '</span>');
    }
};

function onError(evt) {
    showScreen('<span style="color: red;">ERROR: ' + evt.data + '</span>');
};

function showScreen(html) {
    var content = document.getElementById("status");
    content.innerHTML = html;
};

function clearScreen() {
    output.innerHTML = "";
};