
function clearElem(elem){
    if(elem.firstChild==null)
        return;

    elem.firstChild.remove();
    clearElem(elem);
}
function clickFindServices(){
    for(var i=0;i<arrayTime.length;i++)
        clearInterval(arrayTime[i]);
    var cointeinerCard = document.getElementById("container");
    clearElem(cointeinerCard);

    findServices();
}

function findServices(){

    //**********************************************SISTEMARE CODICE*****************************************************************************
    var elements = document.getElementById("findServices").elements;
    var obj ={};
    for(var i = 0 ; i < (elements.length-1) ; i++){
        var item = elements.item(i);
        obj[item.name] = item.value;
    }

    var raw= JSON.stringify(obj);
    var obj2 = JSON.parse(raw)
    console.log(raw);
    console.log(obj2.address, obj2.radius)
    var myHeaders = new Headers();

    //myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Bearer "+localStorage.getItem("token"));
    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };
    //*******Per visializzare dati ----> Inserire un raggio più ampio!*************************
    fetch("http://localhost:8080/rest/api/services?address="+obj2.address+"&radius="+obj2.radius, requestOptions)
        .then(response => response.json()
            .then(jsonBody => ({
                status: response.status,
                jsonBody
            })))
        .then(result => setServiceInformation(result.jsonBody, result.status))

}

function setServiceInformation(json, status){
    if(status == 200 || status == 201){
        console.log(json)
        createCards(json, localStorage.getItem("role"),true)
    }
}


function loadAddress() {
    if(localStorage.getItem('role').toString() === "P" ){
        //console.log(localStorage.getItem('address'))
        document.getElementById("search").value = localStorage.getItem("address").toString();
    }
}

