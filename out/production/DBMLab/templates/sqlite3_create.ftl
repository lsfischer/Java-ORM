<#assign int_types = ['int']>
<#assign real_types = ['double', 'float']>
<#assign text_types = ['String', 'Date']>
<#assign object_type = ['Relation']>

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
        <#assign name = fk.foreignClass.name?lower_case + "_id">
        <#if fk.relationshipType != "N2N">
        ALTER TABLE ${fk.foreignClass.name} ADD COLUMN (${class.name?lower_case}_id) INTEGER REFERENCES ${class.name}(id);
        <#else>
        CREATE TABLE ${class.name}_${fk.foreignClass.name} (
           id INTEGER PRIMARY KEY,
           ${class.name?lower_case}_id INTEGER,
           ${fk.foreignClass.name?lower_case}_id INTEGER,
           FOREIGN KEY (${class.name?lower_case}_id) REFERENCES ${class.name}(id),
           FOREIGN KEY (${fk.foreignClass.name?lower_case}_id) REFERENCES ${fk.foreignClass.name}(id)
        );
        </#if>
    </#list>
    </#compress>
</#list>


