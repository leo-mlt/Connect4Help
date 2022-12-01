
var db = new PouchDB('my_database');
var remoteCouch = false;

function addMessage(idReceiver,timestamp,msg,sender) {

    var todo = {
        _id: new Date().toISOString(),
        idReceiver: idReceiver,
        timestamp:Number(timestamp),
        msg:msg,
        sender:sender
    };
    db.put(todo, function callback(err, result) {
        if(!err && idReceiver == idUser) {
            var messageStyle = createMessageSent(msg, sender, timestamp)
            var chatBox = document.getElementById("chat");
            chatBox.appendChild(messageStyle);
            chatBox.scrollTop = chatBox.scrollHeight;
        }
        return err;
    });


}

function getChat(idReceiver){
    db.createIndex({
        index: {fields: ['timestamp']}
    }).then(function () {
        db.find({
            selector: {
                timestamp:{$gt:null},
                idReceiver: idReceiver
            },
            fields: ['idReceiver','timestamp','msg','sender'],
            sort: [{'timestamp':'asc'}]
        }).then(function (result) {
            console.log(result);
            for(var i=0;i<result.docs.length;i++){
                var aux=result.docs[i];
                var messageStyle=createMessageSent(aux.msg, aux.sender, aux.timestamp); //aggiungere campo timestamp
                var chatBox = document.getElementById("chat");
                chatBox.appendChild(messageStyle);
            }

        }).catch(function (err) {
            console.log(err);
        });
    });

}