var stompClient = null;
var isStompConnected = false;

function sendMessage() {
    let jsonOb = {
        name: localStorage.getItem("name"),
        content: $("#message-value").val()
    };

    if (isStompConnected) {
        stompClient.send("/app/message", {}, JSON.stringify(jsonOb));
        
        // Clear the input field after sending the message
        $("#message-value").val('');

        // Set focus back to the input field
        $("#message-value").focus();
    } else {
        console.error("Stomp client is not connected.");
    }
}

function connect() {
    let socket = new SockJS("/server1");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected : " + frame);
        isStompConnected = true;
        stompClient.subscribe("/topic/return-to", function (response) {
            showMessage(JSON.parse(response.body));
        });
    });
}

function showMessage(message) {
    $("#message-container-table").prepend(`<tr><td><b>${message.name} :</b> ${message.content}</td></tr>`);
}

// Function to check if a username is present in the URL and start the chat if found
function checkAndStartChat() {
    const username = getUsernameFromURL();
    if (username) {
        localStorage.setItem("name", username);
        $("#name-title").html(`Welcome , ${username} !`);
        connect();
    }
}

// Function to extract the username from the URL query parameters
function getUsernameFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('username');
}

$(document).ready(() => {
    checkAndStartChat();
    
    // Add an event listener to the input field to detect the Enter key press
    $("#message-value").keypress((e) => {
        if (e.which === 13) { // 13 is the key code for Enter
            sendMessage();
        }
    });

    $("#send-btn").click(() => {
        sendMessage();
    });

    $("#logout").click(() => {
        localStorage.removeItem("name");
        if (stompClient !== null) {
            stompClient.disconnect();
        }
        window.location.href = "/dashboard";
    });
});
