/* Basic stuff and as it is Public have access to it via the GET /assets/*file routes file

 */
    console.log("Running JS!");
    $("#randomText").click(function () {
        $("#random").load("/random");   // calls GET http://localhost:9000/random
    });

    const stringText = document.getElementById("randomStringText");
    stringText.onclick = () => {
        const lengthInput = document.getElementById("lengthValue")
        console.log(lengthInput)
        fetch("/randomString/" + lengthInput.value).then((response) => {
            return response.text()
        }).then(responseText => {
            const randomString = document.getElementById("randomString");
            randomString.innerHTML = responseText;
        })
    };
