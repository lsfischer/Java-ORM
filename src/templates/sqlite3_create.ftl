<#assign int_types = ['int']>
<#assign real_types = ['double', 'float']>
<#assign text_types = ['String', 'Date']>
<#assign object_type = ['Relation']>

<#list classes as class>

<#if class.foreignKeys?has_content>
<#assign hasForeignKey = 1>
<#else>
<#assign hasForeignKey = 0>
</#if>

/* Create table ${class.name} */
CREATE TABLE ${class.name} (
    id INTEGER PRIMARY KEY,
<#list class.attributes as attr>
    <#if int_types?seq_contains(attr.type)>
    ${attr.name?lower_case} INTEGER<#sep>,</#sep><#if hasForeignKey==1 && attr?is_last>,</#if>
    <#elseif real_types?seq_contains(attr.type)>
    ${attr.name?lower_case} REAL<#sep>,</#sep><#if hasForeignKey==1 && attr?is_last>,</#if>
    <#elseif text_types?seq_contains(attr.type)>
    ${attr.name?lower_case} TEXT<#sep>,</#sep></#if><#if hasForeignKey==1 && attr?is_last>,</#if>
</#list>
    <#list class.foreignKeys as fk>
    <#if fk.relationshipType == "1">
    ${fk.foreignClass.name?lower_case}_id INTEGER,
    </#if>
    </#list>
    <#list class.foreignKeys as fk>
    <#if fk.relationshipType == "1">
    FOREIGN KEY (${fk.foreignClass.name?lower_case}_id) REFERENCES ${fk.foreignClass.name}(id)<#sep>,</#sep>
    </#if>
    </#list>
);
</#list>

<#list classes as class>
<#list class.foreignKeys as fk>
<#assign name = fk.foreignClass.name?lower_case + "_id">
<#if fk.relationshipType == 'N2N'>
CREATE TABLE ${class.name}_${fk.foreignClass.name} (
   id INTEGER PRIMARY KEY,
   ${class.name?lower_case}_id INTEGER,
   ${fk.foreignClass.name?lower_case}_id INTEGER,
   FOREIGN KEY (${class.name?lower_case}_id) REFERENCES ${class.name}(id),
   FOREIGN KEY (${fk.foreignClass.name?lower_case}_id) REFERENCES ${fk.foreignClass.name}(id)
);
</#if>
</#list>
</#list>




//TODO Retira esta parte porque o sqlite3 nao deixa fazer foreign keys desta maneira
<#list classes as class>
    <#compress>
    <#list class.foreignKeys as fk>
        <#assign name = fk.foreignClass.name?lower_case + "_id">
        ALTER TABLE ${class.name}_${fk.foreignClass.name} ADD FOREIGN KEY (${class.name?lower_case}_id) REFERENCES ${class.name}(id);
        ALTER TABLE ${class.name}_${fk.foreignClass.name} ADD FOREIGN KEY (${fk.foreignClass.name?lower_case}_id) REFERENCES ${fk.foreignClass.name}(id);
    </#list>
    </#compress>
</#list>

