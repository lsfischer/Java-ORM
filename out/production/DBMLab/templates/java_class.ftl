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
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
    }
    <#list relations as rels>
    <#if rels.foreignClass.name == name && rels.relationshipType == "N2N">
    public ArrayList<${rels.regularClass.name}> get${rels.regularClass.name}s() {
        openSqLite();
        <#list relations as rels>
        ResultSet resultSet = sqLiteConn.executeQuery("SELECT ${rels.regularClass.name?lower_case}_id FROM ${rels.regularClass.name}_${name} WHERE ${name?lower_case}_id = " + id);
        try{
            while(resultSet.next()){
                String relationId = resultSet.getString("${rels.regularClass.name?lower_case}_id");
                if(!relationId.equals("0")){
                    this.add${rels.regularClass.name}(${rels.regularClass.name}.get(relationId));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return this.${rels.regularClass.name?lower_case}s;
        </#list>
    }
    public void add${rels.regularClass.name}(${rels.regularClass.name} ${rels.regularClass.name?lower_case}){
        this.${rels.regularClass.name?lower_case}s.add(${rels.regularClass.name?lower_case});
    }
    </#if>
    <#if rels.regularClass.name == name>
    <#if rels.relationshipType != '121'>
    public ArrayList<${rels.foreignClass.name}> get${rels.foreignClass.name}() {
        openSqLite();
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
                    this.add${rels.foreignClass.name}(${rels.foreignClass.name}.get(relationId));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return this.${rels.foreignClass.name?lower_case};
    </#if>
    </#list>
    }

    <#else>
    public ${rels.foreignClass.name} get${rels.foreignClass.name}() {
        <#list relations as rels>
        <#if rels.regularClass.name == name>
        openSqLite();
        ResultSet resultSet = sqLiteConn.executeQuery("SELECT id FROM ${rels.foreignClass.name} WHERE ${name?lower_case}_id = "+id);
            try{
                resultSet.next();
                String relationId = Integer.toString(resultSet.getInt("id"));
                if(!relationId.equals("0")){
                    this.set${rels.foreignClass.name}(${rels.foreignClass.name}.get(relationId));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        sqLiteConn.close();
        return this.${rels.foreignClass.name?lower_case};
        </#if>
        </#list>
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
    public void set${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) throws IllegalArgumentException {
        if(${rels.foreignClass.name?lower_case}.getId() == 0){
            throw new IllegalArgumentException("You need to save ${rels.foreignClass.name} id: " + ${rels.foreignClass.name?lower_case}.getId() + " in the database first");
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
        openSqLite();
        if(this.id >= 1){
            String sql = String.format("UPDATE ${name} SET <#list attributes as attribute><#if attribute.name != "id">${attribute.name} = '%s'<#sep>,</#sep></#if></#list> WHERE id = '%s'",<#list attributes as attribute><#if attribute.name != "id">this.${attribute.name}<#sep>,</#sep></#if></#list>,this.id);
            sqLiteConn.executeUpdate(sql);
        }else{
            String sql = String.format("INSERT INTO ${name} (<#list attributes as attribute>${attribute.name}<#sep>,</#sep></#list>) VALUES (<#list attributes as attribute>'%s'<#sep>,</#sep></#list>)",<#list attributes as attribute>this.${attribute.name}<#sep>,</#sep></#list>);
            int idPerson = sqLiteConn.executeUpdate(sql);
            setId(idPerson);
        }
        sqLiteConn.close();
        <#list relations as rels>
        <#if rels.regularClass.name = name>
         saveRelation();
        </#if>
        </#list>
    }

    <#list relations as rels>
    <#if rels.regularClass.name = name>
     private void saveRelation(){
        get${rels.foreignClass.name}();
        openSqLite();
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
        sqLiteConn.close();
        </#if>
     }
    </#if>
    </#list>

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM ${name} WHERE id = "+this.id;
            openSqLite();
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ArrayList all(){
        ArrayList<${name}> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM ${name}");
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

                list.add(${name?lower_case});
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }
    //POR O GET A IR BUSCAR O WHERE com id = id, escusamos de ter dois metodos
    public static ${name} get(String id){
        ${name} ${name?lower_case} = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM ${name} WHERE id = " + id);
        try{
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
        }
        sqLiteConn.close();
        return ${name?lower_case};
    }

    public static ArrayList where(String condition){
        ArrayList<${name}> list = new ArrayList<>();
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT * FROM ${name} WHERE " + condition);
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

                list.add(${name?lower_case});
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return list;
    }

    @Override
    public String toString(){
        return "ID: " + this.id <#list attributes as attribute>+ "\n${attribute.name}: " + this.${attribute.name} </#list>;
    }

    <#list relations as rels>
    <#if rels.foreignClass.name == name && rels.relationshipType != "N2N">
     public ${rels.regularClass.name} get${rels.regularClass.name}(){
        ${rels.regularClass.name} ${rels.regularClass.name?lower_case} = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT ${rels.regularClass.name?lower_case}_id FROM ${rels.foreignClass.name} WHERE id = " + this.id);
        try{
            rs.next();
            ${rels.regularClass.name?lower_case} = Book.get(Integer.toString(rs.getInt("${rels.regularClass.name?lower_case}_id")));
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return ${rels.regularClass.name?lower_case};
     }
    </#if>
    </#list>

}

