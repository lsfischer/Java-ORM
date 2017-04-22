<#list classes as class>

## ${class.name}.java ##
<#assign name = class.name />
<#assign attributes = class.attributes />
<#assign relations = class.relations />
<#include "java_class.ftl" />

</#list>