package ${pkg};
<#assign uses_date = false>
<#assign uses_list = false>
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
    <#list foreignKeys as fkeys>
    <#if fkeys.relationshipType != '121'>
    private ArrayList<${fkeys.foreignClass.name}> ${fkeys.foreignClass.name?lower_case};
    <#else>
    private ${fkeys.foreignClass.name} ${fkeys.foreignClass.name?lower_case};
    </#if>
    </#list>
    private int id;
    SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");

    // Empty constructor
    public ${name}() {
    }

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
    <#list foreignKeys as fkeys>
    <#if fkeys.relationshipType != '121'>
    public ArrayList<${fkeys.foreignClass.name}> get${fkeys.foreignClass.name}() {
        return ${fkeys.foreignClass.name?lower_case};
    }

    <#else>
    public ${fkeys.foreignClass.name} get${fkeys.foreignClass.name}() {
        return ${fkeys.foreignClass.name?lower_case};
    }

    </#if>
    <#if fkeys.relationshipType != '121'>
    public void add${fkeys.foreignClass.name}(${fkeys.foreignClass.name} ${fkeys.foreignClass.name?lower_case}) {
        this.${fkeys.foreignClass.name?lower_case}.add(${fkeys.foreignClass.name?lower_case});
    }
    <#else>
    public void set${fkeys.foreignClass.name}(${fkeys.foreignClass.name} ${fkeys.foreignClass.name?lower_case}) {
            this.${fkeys.foreignClass.name?lower_case} = ${fkeys.foreignClass.name?lower_case};
        }
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
    }

    public void delete(){
        if(this.id >= 1){
            String sql = "DELETE FROM ${name} WHERE id = "+this.id;
            sqLiteConn.executeUpdate(sql);
        }else{
            System.out.println("This object does not exist in the database");
        }
    }

    public static ArrayList all(){
        ArrayList<${name}> list = new ArrayList<>();
        String sql = "SELECT * FROM ${name}";
        SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
        try{
            while(rs.next()){
                int id = rs.getInt("id");
                <#list attributes as attribute>
                ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
                </#list>
                ${name} ${name?lower_case} = new ${name}();
                ${name?lower_case}.setId(id);
                <#list attributes as attribute>
                ${name?lower_case}.set${attribute.name?capitalize}(${attribute.name});
                </#list>
                list.add(${name?lower_case});
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static ${name} get(int id){
        ${name} ${name?lower_case} = new ${name}();
            String sql = "SELECT * FROM ${name} WHERE id = "+ id;
            SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
            ResultSet rs = sqLiteConn.executeQuery(sql);
            try{
                while(rs.next()){
                    int idFromDB = rs.getInt("id");
                    <#list attributes as attribute>
                    ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
                    </#list>
                    ${name?lower_case}.setId(idFromDB);
                    <#list attributes as attribute>
                    ${name?lower_case}.set${attribute.name?capitalize}(${attribute.name});
                    </#list>
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return ${name?lower_case};
    }

    public static ArrayList where(String condition){
        ArrayList<${name}> list = new ArrayList<>();
            String sql = "SELECT * FROM ${name} WHERE "+ condition;
            SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
            ResultSet rs = sqLiteConn.executeQuery(sql);
            try{
                while(rs.next()){
                    int id = rs.getInt("id");
                    <#list attributes as attribute>
                    ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
                    </#list>
                    ${name} ${name?lower_case} = new ${name}();
                    ${name?lower_case}.setId(id);
                    <#list attributes as attribute>
                    ${name?lower_case}.set${attribute.name?capitalize}(${attribute.name});
                    </#list>
                    list.add(${name?lower_case});
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return list;
    }
}


