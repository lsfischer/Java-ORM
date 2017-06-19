<head>
    <link rel="stylesheet" type="text/css" href="../../../styles/mainPage.css">
</head>
<h1>Create new ${name}:</h1>

<form action="/${name?lower_case}/create" method="post">
    <#list attributes as attr>
    <label class="inputLabels" for="${attr.name?lower_case}">${attr.name?capitalize}</label>
    <input class="textField" id="${attr.name?lower_case}" name="${attr.name?lower_case}" type="text" />
    <br/>
    </#list>
    <#list relations as rels>
    <select name="<#if rels.relationshipType == "N2N"><#if name?lower_case == rels.foreignClass.name?lower_case>${rels.regularClass.name?lower_case}_id<#else>${rels.foreignClass.name?lower_case}_id</#if>" multiple="multiple"</#if>>
        <#noparse>
            <#list foreignObjs as obj>
                <option value="${obj.id}">${obj}</option>
            </#list>
        </#noparse>
    </select>
    </#list>
    <br/>
    <input class="button" type="submit" value="Create" />
</form>










