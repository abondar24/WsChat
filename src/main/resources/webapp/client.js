$(document).ready(function() {

    //substr from http://<hostname>
    var host = window.location.origin.substr(7,window.location.origin.length);
    var ws = new WebSocket("ws://"+host+"/chat");

    ws.onopen = function() {
        ws.send("Hello");
    };
    ws.onmessage = function(e) {
        // Receives a message.
        console.log(e.data);
        $('#messages').append(divEscapedContentElement(e.data));
    };
    ws.onclose = function() {
        $('#messages').append(divEscapedContentElement("closed"));
    };
});

function divEscapedContentElement(message){
    return $('<div></div>').text(message);
}

