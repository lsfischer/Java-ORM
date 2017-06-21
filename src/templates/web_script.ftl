<#list classes as class>
function validate${class.name}(){
    <#list class.attributes as attr>
    <#if attr.type == "int" || attr.type == "double" || attr.type == "float">
    var ${attr.name} = document.getElementById("${attr.name}").value;
    if(!isNumeric(${attr.name})){
        alert("${attr.name} needs to be a number");
        return false;
    }
    </#if>
    </#list>
}

</#list>

function isNumeric(valor) {
    return !isNaN(parseFloat(valor)) && isFinite(valor);
}