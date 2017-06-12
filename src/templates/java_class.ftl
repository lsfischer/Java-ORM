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

/**
 * Classe ${name}
 */
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
    private String __name__;
    private static SQLiteConn sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");

    <#if requiredAttributes?size != 0>
    /**
     * Construtor da classe ${name}
     <#list requiredAttributes as requiredAttribute>
     * @param ${requiredAttribute.name} Parametro para inicializaçao do Atributo ${requiredAttribute.name}
     </#list>
     */
    public ${name}(<#list requiredAttributes as requiredAttribute>${requiredAttribute.type} ${requiredAttribute.name}<#sep>, </#sep></#list>) {
    <#list requiredAttributes as requiredAttribute>
        this.${requiredAttribute.name} = ${requiredAttribute.name};
    </#list>
    }
    <#else>
    /**
     * Construtor vazio da classe ${name}
     */
    public ${name}(){
        this.__name__ = "${name}_"+this.id;
    }
    </#if>

    <#list attributes as attribute>
    <#-- Getter -->
    /**
     * Metodo que retorna o Atributo ${attribute.name}
     * @return Valor do Atributo ${attribute.name}
     */
    public ${attribute.type} get${attribute.name?cap_first}() {
        return ${attribute.name};
    }

    <#-- Setter -->
    /**
     * Metodo que altera o valor do Atributo ${attribute.name}
     * @param ${attribute.name} Novo valor do Atributo
     */
    public void set${attribute.name?lower_case?cap_first}(${attribute.type} ${attribute.name}) {
        this.${attribute.name} = ${attribute.name};
    }

    </#list>
    /**
     * Metodo privado que inicializa o atributo sqLiteConn
     */
    private static void openSqLite(){
        sqLiteConn = new SQLiteConn("src/${pkg}/${pkg}.db");
    }

    <#list relations as rels>
    <#if rels.foreignClass.name == name && rels.relationshipType == "N2N">
    /**
     * Metodo que retorna uma ArrayList de ${rels.regularClass.name} pertencentes ao ${name}
     * @return ArrayList de ${rels.regularClass.name}
     */
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

    /**
     * Metodo que adiciona um ${rels.regularClass.name} a ArrayList de ${rels.regularClass.name}
     * @param ${rels.regularClass.name?lower_case} ${rels.regularClass.name} a ser adicionado
     */
    public void add${rels.regularClass.name}(${rels.regularClass.name} ${rels.regularClass.name?lower_case}){
        this.${rels.regularClass.name?lower_case}s.add(${rels.regularClass.name?lower_case});
    }
    </#if>
    <#if rels.regularClass.name == name>
    <#if rels.relationshipType != '121'>
    /**
     * Metodo que retorna os ${rels.foreignClass.name} pertencentes a este Objeto
     * @return
     */
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
    /**
     * Metodo que retorna um ${rels.foreignClass.name} pertencente a este Objeto
     * @return ${rels.foreignClass.name} do ${rels.regularClass.name}
     */
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
    /**
     * Metodo que adiciona um ${rels.foreignClass.name} a ArrayList de ${rels.foreignClass.name}
     * @param ${rels.foreignClass.name?lower_case} ${rels.foreignClass.name} a ser adicionado
     */
    public void add${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) {
        this.${rels.foreignClass.name?lower_case}.add(${rels.foreignClass.name?lower_case});
        <#if rels.relationshipType = "N2N">
        ${rels.foreignClass.name?lower_case}.add${rels.regularClass.name}(this);
        </#if>
    }
    <#else>
    /**
     * Metodo que altera o valor do atributo ${rels.foreignClass.name?lower_case}
     * @param ${rels.foreignClass.name?lower_case} ${rels.foreignClass.name} a ser inserido
     */
    public void set${rels.foreignClass.name}(${rels.foreignClass.name} ${rels.foreignClass.name?lower_case}) {
        this.${rels.foreignClass.name?lower_case} = ${rels.foreignClass.name?lower_case};
    }
    </#if>
    </#if>
    </#list>

    /**
      * Metodo que retorna o atributo Id
      * @return Inteiro id
      */
    public int getId() {
        return this.id;
    }

    /**
     * Metodo que altera o atributo Id
     * @param id Novo Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Metodo que guarda na base de dados o estado atual de todos os atributos da Classe
     */
    public void save(){
        <#list relations as rels>
        <#if rels.regularClass.name = name && rels.secondClassRequired>
        <#if rels.relationshipType != "121">
        if(!${rels.foreignClass.name?lower_case}.isEmpty()){
        <#else>
        if(!${rels.foreignClass.name?lower_case}.equals(null)){
        </#if>
        </#if>
        <#if rels.foreignClass.name = name && rels.firstClassRequired>
        <#if rels.relationshipType == "N2N">
        if(!${rels.regularClass.name?lower_case}s.isEmpty()){
        </#if>
        </#if>
        </#list>
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
        <#if rels.regularClass.name = name && rels.secondClassRequired>
         }else{
            System.out.println("You need to add a ${rels.foreignClass.name?lower_case} to this ${name?lower_case}");
        }
        </#if>
        <#if rels.foreignClass.name = name && rels.firstClassRequired && rels.relationshipType == "N2N">
        }else{
            System.out.println("You need to add a ${rels.regularClass.name?lower_case} to this ${name?lower_case}");
        }
        </#if>
        </#list>
    }

    <#list relations as rels>
    <#if rels.regularClass.name = name>
     /**
      * Metodo privado que adiciona na tabela relacao, as relacoes entre este Objeto e os Objetos presentes na ArrayList de {rels.foreignClass.name}
      */
     private void saveRelation(){
        <#if rels.relationshipType == "N2N">
         for(${rels.foreignClass.name} object : ${rels.foreignClass.name?lower_case}){
            if(object.getId() == 0){
                object.save();
            }
            openSqLite();
            String sql = String.format("INSERT INTO ${rels.regularClass.name}_${rels.foreignClass.name} (${rels.regularClass.name?lower_case}_id, ${rels.foreignClass.name?lower_case}_id) VALUES ('%s', '%s')", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
         }
        </#if>
        <#if rels.relationshipType =="121">
        if(${rels.foreignClass.name?lower_case}.getId() == 0){
            ${rels.foreignClass.name?lower_case}.save();
        }
        openSqLite();
        String sql = String.format("UPDATE ${rels.foreignClass.name} SET ${name?lower_case}_id = '%s' WHERE id = '%s'", this.id, ${rels.foreignClass.name?lower_case}.getId());
        sqLiteConn.executeUpdate(sql);
        sqLiteConn.close();
        </#if>
        <#if rels.relationshipType == "12N" || rels.relationshipType == "N21">
        for(${rels.foreignClass.name} object : ${rels.foreignClass.name?lower_case}){
            if(object.getId() == 0){
                object.save();
            }
            openSqLite();
            String sql = String.format("UPDATE ${rels.foreignClass.name} SET ${name?lower_case}_id = '%s' WHERE id = '%s'", this.id, object.getId());
            sqLiteConn.executeUpdate(sql);
            sqLiteConn.close();
        }
        </#if>
     }
    </#if>
    </#list>

    /**
     * Metodo que elimina da base de dados o registo correspondente a esta Classe
     */
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

    /**
     * Metodo que retorna uma ArrayList de ${name} correspondente a todos os ${name} atualmente na base de dados
     * @return ArrayList de ${name}
     */
    public static ArrayList<${name}> all(){
        return where("id = id");
    }

    /**
     * Metodo que retorna um ${name} correspondente ao Id inserido
     * @param id Id do ${name} a retornar
     * @return ${name}
     */
    public static ${name} get(String id){
        ${name} ${name?lower_case} = null;
        if(!where("id = "+id).isEmpty()){
            ${name?lower_case} = where("id = "+id).get(0);
        }else{
            System.out.println("There is no ${name} with id: "+ id);
        }
        return ${name?lower_case};
    }

    /**
     * Metodo que retorna uma ArrayList de ${name} com base na condicao recebida por parametro
     * @param condition String que especifica a condiçao a realizar na base de dados
     * @return ArrayList de ${name}
     */
    public static ArrayList<${name}> where(String condition){
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
     /**
      * Metodo que retorna o ${rels.regularClass.name} pertencente a este Objeto
      * @return ${rels.regularClass.name}
      */
     public ${rels.regularClass.name} get${rels.regularClass.name}(){
        ${rels.regularClass.name} ${rels.regularClass.name?lower_case} = null;
        openSqLite();
        ResultSet rs = sqLiteConn.executeQuery("SELECT ${rels.regularClass.name?lower_case}_id FROM ${rels.foreignClass.name} WHERE id = " + this.id);
        try{
            rs.next();
            ${rels.regularClass.name?lower_case} = ${rels.regularClass.name}.get(Integer.toString(rs.getInt("${rels.regularClass.name?lower_case}_id")));
        }catch(Exception e){
            e.printStackTrace();
        }
        sqLiteConn.close();
        return ${rels.regularClass.name?lower_case};
     }
    </#if>
    </#list>

}

