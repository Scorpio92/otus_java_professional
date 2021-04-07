let stompClient = null;

const connect = () => {
    stompClient = Stomp.over(new SockJS('/websocket'));
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/response', (message) => {
            $("#users").html("");
            let clients = JSON.parse(message.body);
            for (var i = 0; i < clients.length; i++) {
                showUser(clients[i].name);
            }
        });
        userList();
    });
}

const userList = () => stompClient.send("/app/users", {}, {})
const createUser = () => stompClient.send("/app/createUser", {}, JSON.stringify({
    'login': $("#userLoginTextBox").val(),
    'password': $("#userPassTextBox").val(),
    'name': $("#userNameTextBox").val(),
    'age': $("#userAgeTextBox").val()
}))

const showUser = (user) => $("#users").append("<tr><td>" + user + "</td></tr>")

window.onload = function () {
    connect();
};

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#create").click(createUser);
});