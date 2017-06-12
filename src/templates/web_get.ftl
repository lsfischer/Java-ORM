<form action="/${name?lower_case}/update" method="post">
    <input name="id" type="hidden" value="${r"${obj.id}"}" />
    <#list attributes as attr>
        <label for="${attr.name?lower_case}">${attr.name}</label>
        <input id="${attr.name?lower_case}" name="${attr.name}" type="text" value="${r"${obj."}${attr.name?lower_case}${r"}"}"
    </#list>
    <input type="submit" value="Update" />
</form>