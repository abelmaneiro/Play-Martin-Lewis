"use strict"

const csrfToken = document.getElementById("csrfToken").value;  // non jQuery version
const validateRoute = document.getElementById("validateRoute").value;
const taskRoute = document.getElementById("taskRoute").value;

const createRoute = document.getElementById("createRoute").value;
const deleteRoute = document.getElementById("deleteRoute").value;
const addRoute = document.getElementById("addRoute").value;
const logoutRoute = document.getElementById("logoutRoute").value;

function login() {
    const username = document.getElementById("loginName").value;
    const password = document.getElementById("loginPassword").value;
    fetch(validateRoute, {   //POST /validate3
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken},
        body: JSON.stringify({username, password})
    }).then(res => res.json()).then(data => {
        if (data) {
            document.getElementById("login-section").hidden = true;
            document.getElementById("task-section").hidden = false;
            document.getElementById("login-message").innerHTML = ""
            loadTasks();
        } else {
            document.getElementById("login-message").innerHTML = "Login failed"
        }
    });
}

function loadTasks() {
    const ul = document.getElementById("task-list");
    ul.innerHTML = "";
    fetch(taskRoute).then(res => res.json()).then(tasks => {   // GET /taskList3
        for (let i = 0; i < tasks.length; i++) {
            const li = document.createElement("li");
            const text = document.createTextNode(tasks[i]);
            li.appendChild(text);
            li.onclick = event => {
                deleteTask(i)
            }
            ul.appendChild(li);
        }
    });
}

function addTask() {
    const task = document.getElementById("newTask").value;
    fetch(addRoute, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(task)
    }).then(res => res.json()).then(data => {
        if (data) {
            loadTasks();
            document.getElementById("newTask").value = "";
            document.getElementById("task-message").innerHTML = ""
        }
        else {
            document.getElementById("task-message").innerHTML = "Failed to delete task"
        }
    });
}

function deleteTask(index) {
    fetch(deleteRoute, {
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken },
        body: JSON.stringify(index)
    }).then(res => res.json()).then(data => {
        if (data) {
            document.getElementById("task-message").innerHTML = ""
            loadTasks();
        }
        else {
            document.getElementById("task-message").innerHTML = "Failed to delete task"
        }
    });
}

function createUser() {
    const username = document.getElementById("createName").value;
    const password = document.getElementById("createPassword").value;
    fetch(createRoute, {   //POST /validate3
        method: 'POST',
        headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken},
        body: JSON.stringify({username, password})
    }).then(res => res.json()).then(data => {
        if (data) {
            document.getElementById("login-section").hidden = true;
            document.getElementById("task-section").hidden = false;
            document.getElementById("create-message").innerHTML = ""
            loadTasks();
        } else {
            document.getElementById("create-message").innerHTML = "Create failed"
        }
    });
}

function logout() {
    fetch(logoutRoute).then(res => res.json()).then(data => {
        document.getElementById("login-section").hidden = false;
        document.getElementById("task-section").hidden = true;
    });
}

