window.addEventListener("load",loadContact,false);

var idUser;

function loadContact(){
    document.getElementById("sendMessage").addEventListener("click", sendMessage, false);
    var myHeaders = new Headers();
    //myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Bearer "+localStorage.getItem("token").toString());
    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };
    var url = "http://localhost:8080/rest/api/users/"+localStorage.getItem("id")+"/services";



    fetch(url, requestOptions)
        .then(response => response.json()) //Indirizzamentro alla pagina dei servizi
        .then(result => createContacts(result,localStorage.getItem("role")))
        .catch(error => console.log('error', error));


}

function createContacts(result, role) {
    var valuesSoFar = Object.create(null);
    for (var i=0; i<result.length;i++){
        if(result[i].performerUser!=0){
            if(role=="P")
                var value=result[i].requestUser;
            else
                var value=result[i].performerUser;
            if (value in valuesSoFar){
                continue;
            }
            valuesSoFar[value] = true;
            createContact(result[i],role);
        }

    }
}
function createContact(result,role){
    var id;
    var nome;
    var contacts=document.getElementById("contacts");
    var li=document.createElement("li");
        var div1=document.createElement("div");
        div1.setAttribute("class","d-flex bd-highlight");
            var div2=document.createElement("div");
            div2.className="user_info";
                var span=document.createElement("span");
                if(role=="P") {
                    id=result.requestUser; //Errore nel nome(RISOLTO)
                    nome=result.nameRequester;
                    span.textContent = result.nameRequester + " " + result.surnameRequester;
                }else {
                    id=result.performerUser;
                    nome=result.namePerformer;
                    span.textContent = result.namePerformer + " " + result.surnamePerformer;
                }
                div2.appendChild(span);
        div1.appendChild(div2);
    li.appendChild(div1);
    var selectUserReceive = function (id_user) {
        return function () {
            deleteChat(id_user);
            idUser=id_user;
            var chatWith=document.getElementById("chatWith");
            chatWith.textContent="Chat with "+nome;
            console.log(idUser)

        };
    }(id,nome);
    li.addEventListener("click",selectUserReceive)
    contacts.appendChild(li);
}
function clearElem(elem){
    if(elem.firstChild==null)
        return;

    elem.firstChild.remove();
    clearElem(elem);
}
function deleteChat(id_user){

    console.log(id_user)
    if(id_user==idUser){
        return;
    }
    var chat=document.getElementById("chat");

    clearElem(chat);

    getChat(id_user);

}

function sendMessage(){
    var messageBox = document.getElementById("textMessage");
    var message = messageBox.value;

    if(message.length == 0)
        return;

    //var chatBox = document.getElementById("chat");

    var err=addMessage(idUser,new Date().getTime(),message,true);
    if (!err) {
    //    var messageStyle = createMessageSent(message, true, new Date().getTime());

        //chatBox.appendChild(messageStyle);

        sendTxt(message+"-->"+idUser); //funzione che chiama il socket
    }
    messageBox.value = "";



}

function createMessageSent(message, sender_receiver, timestamp){
    var timeStamp = new Date(timestamp);
    var divMessageBox = document.createElement("div");
    var valueDivBox;
    var msgCont = "msg_cotainer";
    var msgTime = "msg_time";
    if(sender_receiver){
        valueDivBox = "end"
        msgCont += "_send";
        msgTime += "_send";
    }else{
        valueDivBox = "start";
    }
    divMessageBox.setAttribute("class", "d-flex justify-content-"+valueDivBox+" mb-4");

        var divMessageValue = document.createElement("div");
        divMessageValue.setAttribute("class", msgCont);

            var content = document.createTextNode(""+message);



            var spanTime = document.createElement("span")
            spanTime.setAttribute("class", msgTime)
                var contentSpan = document.createTextNode(""+timeStamp.getHours()+":"+timeStamp.getMinutes()
                    +", "+timeStamp.getDate()+"/"+(timeStamp.getMonth()+1))
            spanTime.appendChild(contentSpan)

        divMessageValue.appendChild(content);
        divMessageValue.appendChild(spanTime);
    divMessageBox.appendChild(divMessageValue);

    return divMessageBox;
}

