$('#submit').click(function(){
    console.log("sending parameters...");
    var formElement = document.querySelector("form");

    var formData = new FormData(formElement);
    console.log(formData);

    $.ajax({
        url: "http://192.168.1.46:8080/generate",
        type: 'POST',
        data: formData,
        async: false,
        cache: false,
        contentType: "application/x-www-form-urlencoded",
        processData: false,
        success: function (response) {
            console.log(response);
            alert(response);
        },
        error: function(message) {
            alert("error\n" + message.statusText);
        }
      });
});