<#list classes as class>

## ${class.name}.java ##
<#assign name = class.name />
<#assign attributes = class.attributes />
<#assign foreignKeys = class.foreignKeys />
<#include "java_class.ftl" />

</#list>