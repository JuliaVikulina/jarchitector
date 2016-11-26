$('#submit').click(function(){
    console.log("sending parameters...");

    var width = document.getElementById("width").value;
    var height = document.getElementById("height").value;
    var seed = document.getElementById("seed").value;

    var mess = "width="+ width
     + "&" + "height=" + height
     + "&" + "seed=" + seed;

    console.log(mess);

    $.ajax({
        url: "http://192.168.1.46:8080/generate",
        type: 'POST',
        data: mess,
                // data: $('#formdata').serialize(),

        async: false,
        cache: false,
        // contentType: "application/x-www-form-urlencoded",
        // processData: false,
        success: function (response) {
            console.log(JSON.stringify(response));
            // alert(response);
            $("#response").val(JSON.stringify(response));
        },
        error: function(message) {
            alert("error\n" + message.statusText);
        }
      });

    // var formElement = document.querySelector("form");

    // var width = document.getElementById("width").value;
    // var height = document.getElementById("height").value;

    // // var formData = new FormData(formElement);

    // // formData.append("width", width);
    // // formData.append("height", height);

    // var form = {"width": width, "height": height};


    // console.log(form);

    // $.ajax({
    //     url: "http://192.168.1.46:8080/generate",
    //     type: 'POST',
    //     data: form,
    //     async: false,
    //     cache: false,
    //     contentType: "application/x-www-form-urlencoded",
    //     processData: false,
    //     success: function (response) {
    //         console.log(response);
    //         alert(response);
    //     },
    //     error: function(message) {
    //         alert("error\n" + message.statusText);
    //     }
    //   });
});