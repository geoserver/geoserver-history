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
     * Special reserved key used to store the memento id 
     * (value <code>"org.eclipse.ui.id"</code>).
     *
     * @see #getID()
     */
    public static final String TAG_ID = "IMemento.id"; //$NON-NLS-1$

    /**
     * Creates a new child of this memento with the given type.
     * <p>
     * The <code>getChild</code> and <code>getChildren</code> methods
     * are used to retrieve children of a given type.
     * </p>
     *
     * @param type the type
     * @return a new child memento
     * @see #getChild
     * @see #getChildren
     */
    public IMemento createChild(String type);

    /**
     * Creates a new child of this memento with the given type and id.
     * The id is stored in the child memento (using a special reserved
     * key, <code>TAG_ID</code>) and can be retrieved using <code>getId</code>.
     * <p>
     * The <code>getChild</code> and <code>getChildren</code> methods
     * are used to retrieve children of a given type.
     * </p>
     *
     * @param type the type
     * @param id the child id
     * @return a new child memento with the given type and id
     * @see #getID
     */
    public IMemento createChild(String type, String id);

    /**
     * Returns the first child with the given type id.
     *
     * @param type the type id
     * @return the first child with the given type
     */
    public IMemento getChild(String type);

    /**
     * Returns all children with the given type id.
     *
     * @param type the type id
     * @return the list of children with the given type
     */
    public IMemento[] getChildren(String type);

    /**
     * Returns the floating point value of the given key.
     *
     * @param key the key
     * @return the value, or <code>null</code> if the key was not found or was found
     *   but was not a floating point number
     */
    public Float getFloat(String key);

    /**
     * Returns the id for this memento.
     *
     * @return the memento id, or <code>null</code> if none
     * @see #createChild(java.lang.String,java.lang.String)
     */
    public String getID();

    /**
     * Returns the integer value of the given key.
     *
     * @param key the key
     * @return the value, or <code>null</code> if the key was not found or was found
     *   but was not an integer
     */
    public Integer getInteger(String key);

    /**
     * Returns the string value of the given key.
     *
     * @param key the key
     * @return the value, or <code>null</code> if the key was not found
     */
    public String getString(String key);

    /**
     * Returns the data of the Text node of the memento. Each memento is allowed
     * only one Text node.
     * 
     * @return the data of the Text node of the memento, or <code>null</code>
     * if the memento has no Text node.
     * @since 2.0
     */
    public String getTextData();

    /**
     * Sets the value of the given key to the given floating point number.
     *
     * @param key the key
     * @param value the value
     */
    public void putFloat(String key, float value);

    /**
     * Sets the value of the given key to the given integer.
     *
     * @param key the key
     * @param value the value
     */
    public void putInteger(String key, int value);

    /**
     * Copy the attributes and children from  <code>memento</code>
     * to the receiver.
     *
     * @param memento the IMemento to be copied.
     */
    public void putMemento(IMemento memento);

    /**
     * Sets the value of the given key to the given string.
     *
     * @param key the key
     * @param value the value
     */
    public void putString(String key, String value);

    /**
     * Sets the memento's Text node to contain the given data. Creates the Text node if
     * none exists. If a Text node does exist, it's current contents are replaced. 
     * Each memento is allowed only one text node.
     * 
     * @param data the data to be placed on the Text node
     * @since 2.0
     */
    public void putTextData(String data);
}
