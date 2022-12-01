document.getElementById("reset-form")
    .addEventListener("click", function (e){
        resetForm(e)
    }
    );

document.getElementById("submit-form")
    .addEventListener("click", function (e){
            submitForm(e)
        }
    );

// Funzione chiamata dalla homeRequester() in home.js per caricare elementi dinamici della pagina
function createBodyRequestService(){

    // Settare in automatico il campo Address
    if(localStorage.getItem('address').toString() !== " "){ // Nel campo address di default ci va l'indirizzo dell'utente
        document.getElementById("address").value = localStorage.getItem("address").toString();
    }

    // Settare in automatico le scelte del menù a tendina Category
    var myHeaders = new Headers();

    //myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Bearer "+localStorage.getItem("token"));

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/rest/api/categories/", requestOptions)
        .then(response => response.json()
            .then(jsonBody => ({
                status: response.status,
                jsonBody
            })))
        .then(result => setCategorySelectOptions(result.jsonBody, result.status))

}

//Funzione di utilità per caricare elementi dinamici della pagina
function setCategorySelectOptions(jsonArray, status){

    if(status == 200 || status == 201) {
        console.log(jsonArray)

        var select = document.getElementById("select-category")
        for (i=0; i<jsonArray.length; i++){
            console.log(jsonArray[i].categoryName)
            var newOption = document.createElement("option")
            newOption.setAttribute("value", jsonArray[i].categoryName)
            var textNewOption = document.createTextNode(jsonArray[i].categoryName)

            newOption.appendChild(textNewOption)

            select.appendChild(newOption)
        }


    }

}

function resetForm(e=null){
    if(e)
        e.preventDefault(); // Evita che va in alto alla pagina ogni volta che resettiamo i campi
    document.getElementById("add-request-form").reset();
    if(localStorage.getItem('address').toString() !== " "){
        document.getElementById("address").value = localStorage.getItem("address").toString();
    }
}


function submitForm(e){
    e.preventDefault();

    var elements = document.getElementById("add-request-form").elements;
    var obj ={};
    for(var i = 0 ; i < (elements.length-1) ; i++){
        var item = elements.item(i);
        console.log(item.value);
    }

    var ok

    ok = checkEmptyFields()

    if(!ok){

        window.alert("Fields cannot be empty")

    } else {

        ok = checkDatesAreInTheFuture()
        if(!ok){
            window.alert("Dates cannot be in the past")
        } else {
            ok = checkDateConsistency()

            if (!ok) {
                window.alert("Slot start and Slot end must be in the same day.\n Slot start must be earlier than Slot end")
            } else {

                ok = checkExpritaionDate()

                if (!ok) {

                    window.alert("Expiration date must be earlier than Slot start")

                } else{

                    sendPostRequest();
                }
            }
        }
    }
}

function checkEmptyFields(){

    var ok = true
    //--------------------- check SERVICE CATEGORY --------------------- //

    if(document.getElementById("select-category").value === ""){
        document.getElementById("select-category").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("select-category").classList.contains("input-error"))
            document.getElementById("select-category").classList.remove("input-error")
    }


    //--------------------- check SLOT START --------------------- //

    if(document.getElementById("slot-start").value === ""){
        document.getElementById("slot-start").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("slot-start").classList.contains("input-error"))
            document.getElementById("slot-start").classList.remove("input-error")
    }

    //--------------------- check SLOT END --------------------- //

    if(document.getElementById("slot-end").value === ""){
        document.getElementById("slot-end").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("slot-end").classList.contains("input-error"))
            document.getElementById("slot-end").classList.remove("input-error")
    }

    //--------------------- check DETAILS --------------------- //

    if(document.getElementById("details").value === ""){
        document.getElementById("details").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("details").classList.contains("input-error"))
            document.getElementById("details").classList.remove("input-error")
    }

    //--------------------- check DETAILS --------------------- //
    if(document.getElementById("details").value === ""){
        document.getElementById("details").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("details").classList.contains("input-error"))
            document.getElementById("details").classList.remove("input-error")
    }

    //--------------------- check EXPIRATION DATE --------------------- //
    if(document.getElementById("expiration-date").value === ""){
        document.getElementById("expiration-date").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("expiration-date").classList.contains("input-error"))
            document.getElementById("expiration-date").classList.remove("input-error")
    }

    //--------------------- check ADDRESS --------------------- //

    if(document.getElementById("address").value === ""){
        document.getElementById("address").classList.add("input-error")
        ok = false
    } else{
        if(document.getElementById("address").classList.contains("input-error"))
            document.getElementById("address").classList.remove("input-error")
    }

    /*if(!ok)
        window.alert("All fields are required")*/

    return ok
}

function checkDateConsistency() {
    var ok = true

    var slotStart = new Date(document.getElementById("slot-start").value)
    var slotEnd = new Date(document.getElementById("slot-end").value)

    //--------------------- check SLOT START stesso giorno di SLOT END --------------------- //

    if(!(slotStart.getFullYear() === slotEnd.getFullYear() &&
        slotStart.getMonth() === slotEnd.getMonth() &&
        slotStart.getDate() === slotEnd.getDate())){

        document.getElementById("slot-start").classList.add("input-error")
        document.getElementById("slot-end").classList.add("input-error")

        window.alert("Slot start and Slot end must be in the same day")
        ok = false

    } else {
        if(document.getElementById("slot-start").classList.contains("input-error"))
            document.getElementById("slot-start").classList.remove("input-error")

        if(document.getElementById("slot-end").classList.contains("input-error"))
            document.getElementById("slot-end").classList.remove("input-error")
    }

    //--------------------- check SLOT START < SLOT END --------------------- //

    if(slotStart.getTime() >= slotEnd.getTime() ){

        document.getElementById("slot-start").classList.add("input-error")
        document.getElementById("slot-end").classList.add("input-error")

        ok = false

    } else {

        if(document.getElementById("slot-start").classList.contains("input-error"))
            document.getElementById("slot-start").classList.remove("input-error")

        if(document.getElementById("slot-end").classList.contains("input-error"))
            document.getElementById("slot-end").classList.remove("input-error")

    }
    return ok;
}

function checkExpritaionDate(){

    var ok = true

    //--------------------- check EXPIRATION DATE < SLOT START --------------------- //

    var slotStart = new Date(document.getElementById("slot-start").value)
    var expirationDate = new Date(document.getElementById("expiration-date").value)

    if(slotStart.getTime() <= expirationDate.getTime() ){
        document.getElementById("expiration-date").classList.add("input-error")
        ok = false
    } else {
        if(document.getElementById("expiration-date").classList.contains("input-error"))
            document.getElementById("expiration-date").classList.remove("input-error")
    }

    return ok
}

function checkDatesAreInTheFuture(){
    var ok = true

    var slotStart = new Date(document.getElementById("slot-start").value)
    var slotEnd = new Date(document.getElementById("slot-end").value)
    var expirationDate = new Date(document.getElementById("expiration-date").value)
    var currentTime = new Date()

    if(slotStart.getTime() <currentTime.getTime() ||
        slotEnd.getTime() < currentTime.getTime() ||
        expirationDate < currentTime.getTime())
        ok = false

    return ok
}

function sendPostRequest(){
    var elements = document.getElementById("add-request-form").elements;
    var obj ={};
    for(var i = 0 ; i < (elements.length) ; i++){
        var item = elements.item(i);
        obj[item.name] = item.value;
        if(item.name === "endSlot" || item.name === "startSlot" || item.name === "expirationDate" )
            obj[item.name] = item.value.replace("T", " ") + ":00";
    }

    var raw= JSON.stringify(obj);
    console.log(raw);

    var myHeaders = new Headers();

    myHeaders.append("Authorization", "Bearer " + localStorage.getItem("token"))
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body:raw,
        redirect: 'follow'
    };

    var userId = localStorage.getItem("id");

    fetch("http://localhost:8080/rest/api/users/"+ userId + "/services/", requestOptions)
        .then(response => {
            if(response.status === 201 || response.status === 200){ /**Aggiustare i valori dentro if*/
                return response.text()
            }else{
                throw Error(response.statusText)
            }
        })
        .then(result => {
            window.alert("Service added");
            resetForm();
            })
        .catch(error => console.log('error', error));
    //e.preventDefault();
}
