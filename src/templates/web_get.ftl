<!-- Endpoint on browser:  "/person/get"  -->
<head>
    <link rel="stylesheet" type="text/css" href="../../../styles/mainPage.css">
    <script src="../../../scripts/script.js"></script>
</head>

<h1>List one ${name}</h1>

<form action="/${name?lower_case}/update" method="post" onsubmit="return validate${name}()">
    <input name="id" type="hidden" value="${r"${"}obj.id}"/>
<#list attributes as attr>
    <label class="inputLabels" for="${attr.name?lower_case}">${attr.name?capitalize}</label>
    <input class="textField" id="${attr.name?lower_case}" name="${attr.name?lower_case}" type="text" value="${r"${"}obj.${attr.name}}" <#if attr.required>required</#if>/>
    <br/>
</#list>
<#list relations as rels>
    <#if rels.foreignClass.name == name>
        <h2 class="foreignTitle">${rels.regularClass.name}s</h2>
    <#else>
        <h2 class="foreignTitle">${rels.foreignClass.name}s</h2>
    </#if>
</#list>
<#if relations?size != 0>
    <#noparse>
        <select class="selectInput" name="foreignList">
            <#list relation as rel>
                <option value="${rel.id}">${rel}</option>
            </#list>
        </select>
    </#noparse>
</#if>


    <br/>
    <input class="button" type="submit" value="Update"/>
    <br/>
</form>
<a class="button" href="/${name?lower_case}/list">Back</a>

