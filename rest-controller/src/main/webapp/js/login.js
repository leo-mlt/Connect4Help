var signInForm = document.getElementById("signInForm");
signInForm.addEventListener("submit", verifySignIn);

function verifySignIn(e){

    //signUpForm.get
    var elements = document.getElementById("signInForm").elements;
    var obj ={};
    for(var i = 0 ; i < (elements.length-1) ; i++){
        var item = elements.item(i);
        obj[item.name] = item.value;
    }

    var raw= JSON.stringify(obj);
    console.log(raw);
    var myHeaders = new Headers();
    // myHeaders.append("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9yZXN0L2FwaS91c2Vycy9sb2dpbiIsImlkIjoxLCJleHAiOjE2MTAwNzc4ODQsImlhdCI6MTYwOTg2MTg4NH0.xReTVdpVck4bac6sapQG8tAU7zSU99iNAp225a5cDaHzd4b8nb5lo_r2z-QM_Y-VnPenI3dwRcvRSVf5ngpQ_w");
    //myHeaders.append("Cookie", "JSESSIONID=38af2169361674e55d6c3c448343");
    myHeaders.append("Content-Type", "application/json");
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body:raw,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/rest/api/users/login", requestOptions)
        .then(response => response.json()
            .then(jsonBody => ({
                token: response.headers.get('Authorization').replace("Bearer", ""),
                status: response.status,
                jsonBody
        })))
            .then(result => setUserInformation(result.token, result.jsonBody, result.status))


    e.preventDefault();
    return false;

}

function setUserInformation(token, json, status){
    if(status === 200){
        localStorage.setItem("token", token.toString());
        localStorage.setItem("id", json.id.toString());
        localStorage.setItem("address", json.address.toString());
        localStorage.setItem("role", json.role.toString());
        if(json.role.toString() ==="P")
            window.location.href = 'html/HomeUnique.html';
        else{
            //pagina del requester
            window.location.href = 'html/HomeUnique.html';
        }

    }
    else{
        throw Error("Login Error");
    }
}