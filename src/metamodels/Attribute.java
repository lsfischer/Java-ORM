package metamodels;

import org.w3c.dom.Attr;

public class Attribute {
    private String name;
    private String type;
    private boolean required;

    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
        this.required = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getRequired(){
        return this.required;
    }

    public  void setRequired(){
        this.required = true;
    }
}
