const api_url =
    "http://localhost:8080/rest/api/users";

// Defining async function
async function getapi(url) {

    // Storing response
    const response = await fetch(url);

    // Storing data in form of JSON
    var data = await response.json();
    console.log(data);
    if (response) {
     //   hideloader();
    }
    show(data);
}
// Calling that async function
getapi(api_url);

// Function to hide the loader
function hideloader() {
    document.getElementById('loading').style.display = 'none';
}
// Function to define innerHTML for HTML table
function show(data) {
    let tab =
        `<tr> 
          <th>Name</th> 
          <th>Office</th> 
          <th>Position</th> 
          <th>Salary</th> 
         </tr>`;

    // Loop to access all rows
    for(var i = 0; i < data.length; i++) {
        var r = data[i];

       tab += `<tr>  
        <td>${r.name} </td> 
        <td>${r.role}</td> 
        <td>${r.surname}</td>  
        <td>${r.telephone}</td>           
    </tr>`;
    }
    // Setting innerHTML as tab variable
    //document.getElementById("prova").innerHTML = tab;
}