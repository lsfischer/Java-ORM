<form action="/${name?lower_case}/create" method="post">
    <#list attributes as attr>
        <label for="${attr.name?lower_case}">${attr.name}</label>
        <input id="${attr.name?lower_case}" name="${attr.name?lower_case}" type="text" />
    </#list>
    <input type="submit" value="Create" />
</form>