<!-- Endpoint on browser:  "/${name?lower_case}/list/"  -->
<head>
    <link rel="stylesheet" type="text/css" href="../../../styles/mainPage.css">
</head>
<h1>Existing ${name}s</h1>
<div id="bodySection">
<ul>
${r"<#assign nmr = 1 />"}
    ${r"<#list objs as obj>"}
        <li><a class="entitiesItem" href="/${name?lower_case}/get?id=${r"${obj.id}"}">${name}${r"_${nmr}"}</a> <a class="deleteButton" href="/${name?lower_case}/delete?id=${r"${obj.id}"}">[Delete]</a></li>
    ${r"<#assign nmr = nmr + 1 />"}
    ${r"</#list>"}
</ul>
<a class="button" href="/${name?lower_case}/create">Create a new ${name}</a>

<a class="button" style="margin-top: 20px;" href="/">Back</a>
</div>