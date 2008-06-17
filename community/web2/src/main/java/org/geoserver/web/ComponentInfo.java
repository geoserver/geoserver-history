package org.geoserver.web;

import java.io.Serializable;

import org.apache.wicket.Component;

/**
 * Information about a component being plugged into a user interface.
 * <p>
 * Subclasses of this class are used to imnplement user interface "extension points". 
 * </p>
 * 
 * @author Andrea Aime, The Open Planning Project
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @param <C>
 */
public abstract class ComponentInfo<C extends Component> implements Serializable {

    String id;
    String title;
    Class<C> componentClass;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Class<C> getComponentClass() {
        return componentClass;
    }
    public void setComponentClass(Class<C> componentClass) {
        this.componentClass = componentClass;
    }
}
