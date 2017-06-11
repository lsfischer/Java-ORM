<!-- Endpoint on browser:  "/"  -->
<h1>DBM Admin Interface</h1>
    <h2>Entities:</h2>
      <ul>
        <#list classes as class>
            <li><a href="/${class.name?lower_case}/list">${class.name}</a></li>
        </#list>
      </ul>



