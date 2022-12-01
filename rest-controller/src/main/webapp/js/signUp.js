var signUpForm = document.getElementById("signUpForm");
signUpForm.addEventListener("submit", verifySignUp);

function verifySignUp(e){

    //signUpForm.get
    var elements = document.getElementById("signUpForm").elements;
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
    //myHeaders.append("Cookie", "JSESSIONID=38af2169361674e55d6c3c448343");
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body:raw,
        redirect: 'follow'
    };
/*
    fetch("http://localhost:8080/rest/api/users/", requestOptions)
        .then(response => response.status)
        .then(result => window.alert(result))
        .catch(error => console.log('error', error));
*/
    fetch("http://localhost:8080/rest/api/users/", requestOptions)
        .then(response => {
            if(response.status === 201 || response.status === 200){ /**Aggiustare i valori dentro if*/
                return response.text()
            }else{
                throw Error(response.statusText)
            }
        })
        .then(result => window.alert(result + "OK"))
        .catch(error => console.log('error', error));

    e.preventDefault();
    //return false;

    /************************AGGIUSTARE IL INDIRIZZAMENTO PAGINA***********************************************/
}

