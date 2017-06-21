<!-- Endpoint on browser:  "/person/get"  -->
<head>
    <link rel="stylesheet" type="text/css" href="../../../styles/mainPage.css">
    <script src="../../../scripts/script.js"></script>
</head>

<h1>List one ${name}</h1>

<form action="/${name?lower_case}/update" method="post" onsubmit="return validate${name}()">
    <input name="id" type="hidden" value="${r"${"}obj.id}"/>
<#list attributes as attr>
    <label class="inputLabels" for="${attr.name}">${attr.name?capitalize}</label>
    <input class="textField" id="${attr.name}" name="${attr.name}" type="text" value="${r"${"}obj.${attr.name}}" <#if attr.required>required</#if>/>
    <br/>
</#list>
    <input class="button" type="submit" value="Update"/>
    <br/>
</form>
<#list relations as rels>
    <#if rels.foreignClass.name == name>
    <h2 class="foreignTitle">${rels.regularClass.name}s</h2>
    <#else>
    <h2 class="foreignTitle">${rels.foreignClass.name}s</h2>
    </#if>
</#list>
<#noparse>
<select class="selectInput">
    <#list relation as rel>
        <option>${rel}</option>
    </#list>
</select>
</#noparse>
