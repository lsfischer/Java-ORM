function validateAuthor(){
}

function validateBook(){
    var price = document.getElementById("price").value;
    if(!isNumeric(price)){
        alert("price needs to be a number");
        return false;
    }
    var quantity = document.getElementById("quantity").value;
    if(!isNumeric(quantity)){
        alert("quantity needs to be a number");
        return false;
    }
}


function isNumeric(valor) {
    return !isNaN(parseFloat(valor)) && isFinite(valor);
}