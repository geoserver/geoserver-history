package org.vfny.geoserver.issues;


/**
 * The generic memento interface for the issues module within geoserver.
 * This interface is designed to be easily adapted to suite the eclipse
 * equivalent
 * @author quintona
 * @since 1.1.0
 */
public interface IMemento {
    
    /**
     * Creates a new child of this memento
     * @return The new child
     */
    public IMemento createChild();
    /**
     * Creats a new child of this memento with the given id
     * Throws an exception if a duplicate id is passed to the method
     * @param id
     * @return The new child
     */
    public IMemento createChild(String id) throws Exception;
    /**
     * Returns the child specified by the given id
     * @param id
     * @return The child memento, null if the child does not exist
     */
    public IMemento getChild(String id);
    /**
     * Returns the children of this memento
     * @return An array of children
     */
    public IMemento[] getChildren();
    /**
     * Returns the id of this memento
     */
    public String getID();
    /**
     * Returns a value as identified by the key
     * @param key
     * @return The value, null if the key could not be resolved
     */
    public Object getValue(String key);
    /**
     * Adds a value to this memento
     * @param key The key to associate the value to
     * @param value The actual value
     */
    public void putValue(String key, Object value);
    
}
