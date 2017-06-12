<h1>List all ${name}</h1>
<ul>
    <a href="/${name?lower_case}/create">create new person</a>
    ${r"<#list objs as obj>"}
        <li><a href="/${name?lower_case}/get?id=${r"${obj.id}"}">${r"${obj.name}"}</a> [<a href="/${name?lower_case}/delete?id=${r"${obj.id}"}">Delete</a>]</li>
    ${r"</#list>"}
</ul>
