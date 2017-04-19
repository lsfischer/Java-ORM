<#assign int_types = ['int']>
<#assign real_types = ['double', 'float']>
<#assign text_types = ['String', 'Date']>

<#list classes as class>

/* Create table ${class.name} */
CREATE TABLE ${class.name} (
    id INTEGER PRIMARY KEY,
<#list class.attributes as attr>
    <#if int_types?seq_contains(attr.type)>
    ${attr.name?lower_case} INTEGER<#sep>,</#sep>
    <#elseif real_types?seq_contains(attr.type)>
    ${attr.name?lower_case} REAL<#sep>,</#sep>
    <#elseif text_types?seq_contains(attr.type)>
    ${attr.name?lower_case} TEXT<#sep>,</#sep>
    </#if>
</#list>
);
</#list>

<#list classes as class>
    <#compress>
    <#list class.foreignKeys as fk>
        <#assign name = fk.name?lower_case + "_id">
        /* Add field ${name} */
        ALTER TABLE ${class.name} ADD COLUMN ${name} INTEGER REFERENCES ${fk.name} (id);
    </#list>
    </#compress>
</#list>