package org.vfny.geoserver.issues;

import java.io.Serializable;

public class Target implements ITarget, Serializable {

    /** long serialVersionUID field */
    private static final long serialVersionUID = -6828722211977040816L;
    private String type;
    private String id;
    
    public Target(String type, String id){
        this.type = type;
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    public void setId( String id ) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType( String type ) {
        this.type = type;
    }
    
    
}
