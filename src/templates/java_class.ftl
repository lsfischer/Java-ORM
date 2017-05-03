package ${pkg};
<#assign uses_date = false>
<#assign uses_list = false>
<#assign requiredAttributes =[]/>
<#list attributes as attribute>
    <#if attribute.required>
        <#assign requiredAttributes = requiredAttributes + [attribute] />
    </#if>
</#list>
<#list attributes as attribute>
    <#if attribute.type == "Date" && uses_date == false>
        <#assign uses_date = true>
        <#lt>import java.util.Date;
    </#if>
    <#if attribute.type?starts_with("List") && uses_list == false>
            <#assign uses_list = true>
            <#lt>import java.util.List;
        </#if>
</#list>

import utils.sqlite.SQLiteConn;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ${name} {

    <#list attributes as attribute>
    private ${attribute.type} ${attribute.name};
    </#list>
    <#list relations as rels>
    <#if rels.foreignClass.name == name && rels.relationshipType =="N2N">
    private ArrayList<${rels.regularClass.name}> ${rels.regularClass.name?lower_case}s = new ArrayList<>();
    </#if>
    <#if rels.regularClass.name == name>
    <#if rels.relationshipType != '121'>
    private ArrayList<${rels.foreignClass.name}> ${rels.foreignClass.name?lower_case} = new ArrayList<>();
    <#else>
    private ${rels.foreignClass.name} ${rels.foreignClass.name?lower_case};
    </#if>
    </#if>
    </#list>
    private int id;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");

    <#if requiredAttributes?size != 0>
    public ${name}(<#list requiredAttributes as requiredAttribute>${requiredAttribute.type} ${requiredAttribute.name}<#sep>, </#sep></#list>) {
    <#list requiredAttributes as requiredAttribute>
        this.${requiredAttribute.name} = ${requiredAttribute.name};
    </#list>
    }
    <#else>
    //Empty Constructor
    public ${name}(){
    }
    </#if>

    <#list attributes as attribute>
    <#-- Getter -->
    public ${attribute.type} get${attribute.name?cap_first}() {
        return ${attribute.name};
    }

    <#-- Setter -->
    public void set${attribute.name?lower_case?cap_first}(${attribute.type} ${attribute.name}) {
        this.${attribute.name} = ${attribute.name};
    }

    </#list>
    <#list relations as rels>
    <#if rels.foreignClass.name == name && rels.relationshipType == "N2N">
    public ArrayList<${rels.regularClass.name}> get${rels.regularClass.name}s() {
        return ${rels.regularClass.name?lower_case}s;
    }
    public void add${rels.regularClass.name}(${rels.regularClass.name} ${rels.regularClass.name?lower_case}){
        this.${rels.regularClass.name?lower_case}s.add(${rels.regularClass.name?lower_case});
    }
    </#if>
    <#if rels.regularClass.name == name>
    <#if rels.relationshipType != '121'>
    public ArrayList<${rels.foreignClass.name}> get${rels.foreignClass.name}() {
        return ${rels.foreignClass.name?lower_case};
    }

    <#else>
    public ${rels.foreignClass.name} get${rels.foreignClass.name}() {
        return ${rels.foreignClass.name?lower_case};
    }

    </#if>
    <#if rels.relationshipType != '121'>
    public void add${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) throws IllegalArgumentException {
        if(${rels.foreignClass.name?lower_case}.getId() == 0){
            throw new IllegalArgumentException("You need to save ${rels.foreignClass.name} id: " + ${rels.foreignClass.name?lower_case}.getId() + " in the database first");
        }else{
            this.${rels.foreignClass.name?lower_case}.add(${rels.foreignClass.name?lower_case});
            <#if rels.relationshipType = "N2N">
            ${rels.foreignClass.name?lower_case}.add${rels.regularClass.name}(this);
            </#if>
            //TODO Secalhar aqui arranjar maneira de chamar o update, caso o livro ja esteja na BD e queiramos adicionar mais um autor
        }
    }
    <#else>
    public void set${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) {
        if(${rels.foreignClass.name?lower_case}.getId() == 0){
            System.out.println("You need to save this object in the DataBase first");
        }else{
            this.${rels.foreignClass.name?lower_case} = ${rels.foreignClass.name?lower_case};
        }
    }
    </#if>
    </#if>
    </#list>
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void save(){
        if(this.id >= 1){
            String sql = String.format("UPDATE ${name} SET <#list attributes as attribute><#if attribute.name != "id">${attribute.name} = '%s'<#sep>,</#sep></#if></#list> WHERE id = '%s'",<#list attributes as attribute><#if attribute.name != "id">this.${attribute.name}<#sep>,</#sep></#if></#list>,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO ${name} (<#list attributes as attribute>${attribute.name}<#sep>,</#sep></#list>) VALUES (<#list attributes as attribute>'%s'<#sep>,</#sep></#list>)",<#list attributes as attribute>this.${attribute.name}<#sep>,</#sep></#list>);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
        <#list relations as rels>
        <#if rels.regularClass.name = name>
         saveRelation();
        </#if>
        </#list>
    }

    <#list relations as rels>
    <#if rels.regularClass.name = name>
     private void saveRelation(){
        <#if rels.relationshipType == "N2N">
         for(${rels.foreignClass.name} object : ${rels.foreignClass.name?lower_case}){
            String sql = String.format("INSERT INTO ${rels.regularClass.name}_${rels.foreignClass.name} (${rels.regularClass.name?lower_case}_id, ${rels.foreignClass.name?lower_case}_id) VALUES ('%s', '%s')", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
         }
        </#if>
        <#if rels.relationshipType =="121">
        String sql = String.format("UPDATE ${rels.foreignClass.name} SET ${name?lower_case}_id = '%s' WHERE id = '%s'", this.id, ${rels.foreignClass.name?lower_case}.getId());
        sqLiteConn.executeUpdate(sql);
        </#if>
        <#if rels.relationshipType == "12N" || rels.relationshipType == "N21">
        for(${rels.foreignClass.name} object : ${rels.foreignClass.name?lower_case}){
            String sql = String.format("UPDATE ${rels.foreignClass.name} SET ${name?lower_case}_id = '%s' WHERE id = '%s'", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
        }
        </#if>
     }
    </#if>
    </#list>

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM ${name} WHERE id = "+this.id;
            sqLiteConn.executeUpdate(sql);
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    private static ResultSet getResultSet(String condition){
        String sql;
        if(condition.isEmpty()){
            sql = "SELECT * FROM ${name}";
        }else{
            sql = "SELECT * FROM ${name} WHERE " + condition;
        }
        return sqLiteConn.executeQuery(sql);
    }

    private static void getRelations(${name} ${name?lower_case}, int id){
        <#list relations as rels>
        <#if rels.regularClass.name == name>
        <#if rels.relationshipType == "N2N">
        String sql = "SELECT ${rels.foreignClass.name?lower_case}_id FROM ${name}_${rels.foreignClass.name} WHERE ${name?lower_case}_id = " + id;
        <#else>
        String sql = "SELECT id FROM ${rels.foreignClass.name} WHERE ${name?lower_case}_id = "+id;
        </#if>
        ResultSet resultSet = sqLiteConn.executeQuery(sql);
        try{
            while(resultSet.next()){
                <#if rels.relationshipType != "N2N">
                String relationId = resultSet.getString("id");
                <#else>
                String relationId = Integer.toString(resultSet.getInt("${rels.foreignClass.name?lower_case}_id"));
                </#if>
                if(!relationId.equals("0")){
                    <#if rels.relationshipType != "121">
                    ${name?lower_case}.add${rels.foreignClass.name}(${rels.foreignClass.name}.get(relationId));
                    <#else>
                    ${name?lower_case}.set${rels.foreignClass.name}(${rels.foreignClass.name}.get(relationId));
                    </#if>
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        </#if>
        <#if rels.foreignClass.name == name && rels.relationshipType == "N2N">
        String sql = "SELECT ${rels.regularClass.name?lower_case}_id FROM ${rels.regularClass.name}_${name} WHERE ${name?lower_case}_id = " + id;
        ResultSet resultSet = sqLiteConn.executeQuery(sql);
        try{
            while(resultSet.next()){
                String relationId = Integer.toString(resultSet.getInt("${rels.regularClass.name?lower_case}_id"));
                if(!relationId.equals("0")){
                    ${name?lower_case}.add${rels.regularClass.name}(${rels.regularClass.name}.get(relationId));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        </#if>
        </#list>
    }

    public static ArrayList all(){
        ArrayList<${name}> list = new ArrayList<>();
        ResultSet rs = getResultSet("");
        try{
            while(rs.next()){
                ${name} ${name?lower_case} = new ${name}(<#list requiredAttributes as requiredAttribute>rs.get${requiredAttribute.type?capitalize}("${requiredAttribute.name}")<#sep>, </#sep></#list>);

                int id = rs.getInt("id");
                ${name?lower_case}.setId(id);

                <#list attributes as attribute>
                <#if !attribute.required>
                ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
                ${name?lower_case}.set${attribute.name?capitalize}(${attribute.name});

                </#if>
                </#list>
                <#if relations?size != 0>
                getRelations(${name?lower_case},id);

                </#if>
                list.add(${name?lower_case});
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static ${name} get(String id){
        ${name} ${name?lower_case};

        try{

            ResultSet rs = getResultSet("id = " + id);
            rs.next();

            ${name?lower_case} = new ${name?capitalize}(<#list requiredAttributes as requiredAttribute>rs.get${requiredAttribute.type?capitalize}("${requiredAttribute.name}")<#sep>, </#sep></#list>);

            int idFromDB = rs.getInt("id");
            ${name?lower_case}.setId(idFromDB);

            <#list attributes as attribute>
            <#if !attribute.required>
            ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
            ${name?lower_case}.set${attribute.name?capitalize}(${attribute.name});

            </#if>
            </#list>

        }catch(Exception e){
            e.printStackTrace();
            return new ${name?capitalize}(<#list requiredAttributes as requiredAttribute><#if requiredAttribute.type != "String">0<#else>""</#if><#sep>, </#sep></#list>);
        }
        <#if relations?size != 0>
        getRelations(${name?lower_case},${name?lower_case}.getId());

        </#if>
        return ${name?lower_case};
    }

    public static ArrayList where(String condition){
        ArrayList<${name}> list = new ArrayList<>();
        ResultSet rs = getResultSet(condition);
        try{
            while(rs.next()){
                ${name} ${name?lower_case} = new ${name}(<#list requiredAttributes as requiredAttribute>rs.get${requiredAttribute.type?capitalize}("${requiredAttribute.name}")<#sep>, </#sep></#list>);

                int id = rs.getInt("id");
                ${name?lower_case}.setId(id);

                <#list attributes as attribute>
                <#if !attribute.required>
                ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
                ${name?lower_case}.set${attribute.name?capitalize}(${attribute.name});

                </#if>
                </#list>
                <#if relations?size != 0>
                getRelations(${name?lower_case},id);

                </#if>
                list.add(${name?lower_case});
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id <#list attributes as attribute>+ "\n${attribute.name}: " + this.${attribute.name} </#list>;
    }

    <#list relations as rels>
    <#if rels.foreignClass.name == name && rels.relationshipType != "N2N">
     public ${rels.regularClass.name} get${rels.regularClass.name}(){
        String sql = String.format("SELECT * FROM ${rels.regularClass.name} WHERE id = (SELECT ${rels.regularClass.name?lower_case}_id FROM ${name} WHERE id = '%s')"",this.id);
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try{
            ${rels.regularClass.name} ${rels.regularClass.name?lower_case} = new ${rels.regularClass.name}(<#list requiredAttributes as requiredAttribute>rs.get${requiredAttribute.type?capitalize}("${requiredAttribute.name}")<#sep>, </#sep></#list>);
        }catch(Exception e){
            e.printStackTrace();
        }

     }
    </#if>
    </#list>
}

