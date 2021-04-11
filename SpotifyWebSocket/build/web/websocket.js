var wsUri = "ws://" + document.location.host + document.location.pathname + "spotify";
var websocket  = null;
var output = null;
var error_output = null;
var current_action = "";
// última mensagem recebida do servidor
var last_data = "";


window.onload = function () { 
    websocket = new WebSocket(wsUri);
    output = document.getElementById("output");
    error_output = document.getElementById("error");
    
    websocket.onerror = function(evt) { onError(evt) };
    websocket.onopen = function(evt) { onOpen(evt); load_SongData();};
    websocket.onmessage = function (event){ last_data = event.data ; default_onMessage(event); };    
}


function load_SongData(){
    execute('current');
    setTimeout(function (){execute('art');},200);

}

function onClick_Play(){
    execute('play');
    load_SongData();
}


function onClick_Next(){
    execute('next');
    load_SongData();
}


function onClick_Previous(){
    execute('prev');
    load_SongData();
}

function execute(action){
    current_action = action;
    if (action === "play"){
        playpause();
    }else if (action === "next"){
        next();
    }else if (action === "prev"){
        previous();
    }else if (action === "metadata"){
        metadata();
    }else if (action === "art"){
        art();
    }else if (action === "current"){
        current();
    }
    
    
}

function loadArt(imgUrl){
    document.getElementById('art_image').src = imgUrl;
}

function playpause(){
    websocket.send('play');
}

function next(){
    websocket.send('next');
}

function previous(){
    websocket.send('prev');
}

function metadata(){
    websocket.send('metadata');
}

function art(){
    websocket.send('art');
}

function current(){
    websocket.send('current');
}


function default_onMessage(event){
    
    if (current_action ==="art"){
        //nada
        loadArt(event.data);
    }else {
        writeToScreen("\n "+event.data);
    }
    
    
}

function onError(evt) {
    writeErrorToScreen('<span style="color: red;">ERROR:</span> ' );
    writeToScreen(evt.data);
}


function writeToScreen(message) {
    output.innerText = message;
}

function writeErrorToScreen(message){
    error_output.innerHTML = message;
}

function onOpen() {
    writeToScreen("Connected to " + wsUri);
}



// End test functions