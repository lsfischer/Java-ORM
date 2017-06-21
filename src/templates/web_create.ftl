<head>
    <link rel="stylesheet" type="text/css" href="../../../styles/mainPage.css">
    <script src="../../../scripts/script.js"></script>
</head>
<h1>Create a new ${name}</h1>

<form action="/${name?lower_case}/create" method="post" onsubmit="return validate${name}()">
    <#list attributes as attr>
    <label class="inputLabels" for="${attr.name?lower_case}">${attr.name?capitalize}</label>
    <input class="textField" id="${attr.name?lower_case}" name="${attr.name?lower_case}" type="text" <#if attr.required>required</#if>/>
    <br/>
    </#list>
    <#list relations as rels>
    <select class="selectInput" name="<#if rels.relationshipType == "N2N"><#if name?lower_case == rels.foreignClass.name?lower_case>${rels.regularClass.name?lower_case}_id<#else>${rels.foreignClass.name?lower_case}_id</#if>" multiple="multiple"</#if> <#if (rels.regularClass.name == name && rels.secondClassRequired) || (rels.foreignClass.name == name && rels.firstClassRequired)>required</#if>>
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
<a class="button" href="/${name?lower_case}/list">Back</a>










