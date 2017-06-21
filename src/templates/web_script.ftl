<#list classes as class>
    <#assign requiredAttributes =[]/>
    <#list class.attributes as attribute>
        <#if attribute.required>
            <#assign requiredAttributes = requiredAttributes + [attribute] />
        </#if>
    </#list>
function validate${class.name}(){
    <#list requiredAttributes as requiredAttr>
    var ${requiredAttr.name} = document.getElementById("${requiredAttr.name}").value;
    if(${requiredAttr.name} == ""){
        alert("${requiredAttr.name} is required");
        return false;
    }
    </#list>
}

</#list>
