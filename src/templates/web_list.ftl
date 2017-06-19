<!-- Endpoint on browser:  "/${name?lower_case}/list/"  -->
<h1>List all ${name}s</h1>

<a href="/${name?lower_case}/create">Create a new ${name}</a>

<ul>
${r"<#assign nmr = 1 />"}
    ${r"<#list objs as obj>"}
        <li><a href="/${name?lower_case}/get?id=${r"${obj.id}"}">${r"${name}_${nmr}"}</a> <a href="/${name?lower_case}/delete?id=${r"${obj.id}"}">[Delete]</a></li>
    ${r"<#assign nmr = nmr + 1 />"}
    ${r"</#list>"}

</ul>



