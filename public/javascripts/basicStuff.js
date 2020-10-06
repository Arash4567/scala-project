console.log("Running basicStuff JS!")

$("#randomText").click(function () {
    $("#random").load("/random");
});

const stringText = document.getElementById("randomStringText");
stringText.onclick = () => {
    const lengthInput = document.getElementById("lengthValue");
    const url = "/randomString/" + lengthInput.value;
    console.log(url);
    fetch(url).then((response) => {
        return response.text();
    }).then((responseText) => {
        const randomString = document.getElementById("randomString");
        randomString.innerHTML = responseText;
    }); 
}
