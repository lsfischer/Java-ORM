<!-- Endpoint on browser:  "/${name?lower_case}/list/"  -->
<h1>List all ${name}s</h1>

<a href="/${name?lower_case}/create">Create a new ${name}</a>

<ul>
    ${r"<#list objs as obj>"}
        <li><a href="/${name?lower_case}/get?id=${r"${obj.id}"}">${r"${obj.__name__}"}</a> <a href="/${name?lower_case}/delete?id=${r"${obj.id}"}">[Delete]</a></li>
    ${r"</#list>"}

</ul>



