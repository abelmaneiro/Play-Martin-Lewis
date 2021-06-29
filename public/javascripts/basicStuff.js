/* Basic stuff and as it is Public have access to it via the GET /assets/*file routes file

 */
    console.log("Running JS!")
    $("#randomText").click(function () {
        $("#random").load("/random")   // calls GET http://localhost:9000/random
    })