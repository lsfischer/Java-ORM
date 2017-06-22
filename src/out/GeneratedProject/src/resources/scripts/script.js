function validatePerson(){
    var age = document.getElementById("age").value;
    if(!isNumeric(age)){
        alert("age needs to be a number");
        return false;
    }
}


function isNumeric(valor) {
    return !isNaN(parseFloat(valor)) && isFinite(valor);
}