<h1>List all ${name}s</h1>

<a href="/${name?lower_case}/create">Create a new ${name}</a>

<ul>
    ${r"<#list objs as obj>"}
        <li><a href="/${name}/get?id=${r"${obj.id}"}">${r"${obj.name}"}</a> <a href="/${name?lower_case}/delete?id=${r"${obj.id}"}">[Delete]</a></li>
    ${r"</#list>"}

</ul>













