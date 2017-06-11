<!-- Endpoint on browser:  "/person/get"  -->
<h1>List one ${name}</h1>

<form action="/${name?lower_case}/update" method="post">
    <input name="id" type="hidden" value="${r"${"}obj.id}" />
    <#list attributes as attr>
    <label for="${attr.name}">${attr.name?capitalize}</label>
    <input id="${attr.name}" name="${attr.name}" type="text" value="${r"${"}obj.${attr.name}}" />
    </#list>
    <input type="submit" value="Update" />
</form>