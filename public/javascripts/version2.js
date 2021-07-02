console.log("Running version2.js");

const csrfToken = $("#csrfToken").val();
const loginRoute = $("#loginRoute").val();
const validateRoute = $("#validateRoute").val();

//$("#contents").load("/login2");
$("#contents").load(loginRoute);  // only really works for GETS without parameters

function login() {
    const username = $("#loginName").val();
    const password = $("#loginPass").val();
    //$("#contents").load("/validate2?username=" + username + "&password=" + password);  // POST version
    $.post(validateRoute,
        {username, password, csrfToken},
        data => {
            $("#contents").html(data)
        }
    );
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
