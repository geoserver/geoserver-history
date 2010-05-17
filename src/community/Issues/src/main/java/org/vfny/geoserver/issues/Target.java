package org.vfny.geoserver.issues;

import java.io.Serializable;

/**
 * Every issue relates to something, an object within the specific domain model,
 * "The problem object". In order to seperate the issues from the problem domain
 * we give the user of the issues service the ability to provide a target key for 
 * domain specific object. This can then be used in the future to update, retrieve,
 * etc... issues. 
 * @author quintona
 * @since 1.1.0
 */
public class Target implements Serializable {

    /** long serialVersionUID field */
    private static final long serialVersionUID = -6828722211977040816L;
    private String type;
    private String id;
    
    public Target(String type, String id){
        this.type = type;
        this.id = id;
    }
    /**
     * The id of the specific instance of the domain class. This will usually
     * obtained from some kind of primary key.
     * @return The unique key for this target type.
     */
    public String getId() {
        return id;
    }
    /**
     * @param type the new id string
     */
    public void setId( String id ) {
        this.id = id;
    }
    /**
     * Returns the target's type. Usually this will just be Class.name()
     * @return The target's type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the new type string
     */
    public void setType( String type ) {
        this.type = type;
    }
    
    
}
