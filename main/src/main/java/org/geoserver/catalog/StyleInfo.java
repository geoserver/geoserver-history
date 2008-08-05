package org.geoserver.catalog;

import java.io.IOException;
import java.io.Serializable;

import org.geotools.styling.Style;

/**
 * A style for a geospatial resource.
 * 
 * @author Justin Deoliveira, The Open Planning project
 */
public interface StyleInfo extends Serializable {

    /**
     * Identifier for the style.
     */
    String getId();
    
    /**
     * Name of the style.
     * <p>
     * This value is unique among all styles and can be used to identify the
     * style.
     * </p>
     * 
     * @uml.property name="name"
     */
    String getName();

    /**
     * Sets the name of the style.
     * 
     * @uml.property name="name"
     */
    void setName(String name);

    /**
     * The name of the file the style originates from.
     */
    String getFilename();

    /**
     * Sets the name of the file the style originated from.
     */
    void setFilename( String fileName );
    
    /**
     * The style object.
     */
    Style getStyle() throws IOException;
}
