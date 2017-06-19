<!-- Endpoint on browser:  "/"  -->
<head>
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="../styles/mainPage.css">
</head>
<div id="bodySection">
<h1>DBM Admin Interface</h1>
    <div id="entities">
    <h2>Entities:</h2>
      <ul>
        <#list classes as class>
    	<li><a class="entitiesItem" href="/${class.name?lower_case}/list">${class.name}</a></li>
        </#list>
      </ul>
    </div>
</div>
