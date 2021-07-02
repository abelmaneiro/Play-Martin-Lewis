console.log("Running version2.js");

const loginRoute = $("#loginRoute").val()
//$("#contents").load("/login2");
$("#contents").load(loginRoute);  // only really works for GETS without parameters

function login() {
    const username = $("#loginName").val();
    const password = $("#loginPass").val();
    $("#contents").load("/validate2?username=" + username + "&password=" + password);

}

function createUser() {
    const username = $("#createName").val();
    const password = $("#createPass").val();
    $("#contents").load("/create2?username=" + username + "&password=" + password);
}

function deleteTask(index) {
    $("#contents").load("/deleteTask2?index=" + index);
}

function addTask() {
    const task = $("#newTask").val();
    $("#contents").load("/addTask2?task="+ encodeURIComponent(task));
}
