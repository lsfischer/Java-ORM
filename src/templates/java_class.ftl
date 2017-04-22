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
    <#list relations as rels>
    <#if rels.regularClass.name == name>
    <#if rels.relationshipType != '121'>
    private ArrayList<${rels.foreignClass.name}> ${rels.foreignClass.name?lower_case};
    <#else>
    private ${rels.foreignClass.name} ${rels.foreignClass.name?lower_case};
    </#if>
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
    <#list relations as rels>
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
    public void add${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) {
        this.${rels.foreignClass.name?lower_case}.add(${rels.foreignClass.name?lower_case});
    }
    <#else>
    public void set${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) {
            this.${rels.foreignClass.name?lower_case} = ${rels.foreignClass.name?lower_case};
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
                    ${name} ${name?lower_case} = new ${name}();

                    int id = rs.getInt("id");
                    ${name?lower_case}.setId(id);

                    <#list attributes as attribute>
                    ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
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
        String sql = "SELECT * FROM ${name} WHERE id = " + id;
        SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
            try{
                while(rs.next()){
                    int idFromDB = rs.getInt("id");
                    ${name?lower_case}.setId(idFromDB);

                    <#list attributes as attribute>
                    ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
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
        String sql = "SELECT * FROM ${name} WHERE " + condition;
        SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
        ResultSet rs = sqLiteConn.executeQuery(sql);
            try{
                while(rs.next()){
                    ${name} ${name?lower_case} = new ${name}();

                    int id = rs.getInt("id");
                    ${name?lower_case}.setId(id);

                    <#list attributes as attribute>
                    ${attribute.type} ${attribute.name} = rs.get${attribute.type?capitalize}("${attribute.name}");
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
//TODO ir buscar todos os autores de um livro, acho que ainda não temos isso quando fazemos o Where e o get

