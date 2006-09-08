package org.vfny.geoserver.issues;

/**
 * Every issue relates to something, an object within the specific domain model,
 * "The problem object". In order to seperate the issues from the problem domain
 * we give the user of the issues service the ability to provide a target key for 
 * domain specific object. This can then be used in the future to update, retrieve,
 * etc... issues. 
 * @author quintona
 * @since 1.1.0
 */
public interface ITarget {
    /**
     * Returns the target's type. Usually this will just be Class.name()
     * @return The target's type
     */
    String getType();
    /**
     * The id of the specific instance of the domain class. This will usually
     * obtained from some kind of primary key.
     * @return The unique key for this target type.
     */
    String getId();
    /**
     * @param type the new type string
     */
    void setType(String type);
    /**
     * @param type the new id string
     */
    void setId(String id);
}
