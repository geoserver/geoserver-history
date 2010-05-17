/*
 */

package org.geoserver.catalog.hibernate.beans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.hibernate.Hibernable;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class LayerInfoImplHb extends LayerInfoImpl implements Hibernable {

    /**
     *
     */
    private static final long serialVersionUID = 4909148252767410025L;

    private final static Logger LOGGER = Logging.getLogger(LayerInfoImplHb.class);

    /**
     * ETj: This is a workaround to fix the new behaviour introduced in
     * revision 13349 of LayerInfoImpl: setName() will set the resource name
     * instead of this very instance's name.
     * It causes an error when hibernate sets the various attribute,
     * when the name is set before the resource.
     */

    /**
     * This whole method is part of the workaround.
     */
    public String getName() {
        // TODO: uncomment back when resource/publish split is complete
        // return name;
        if(resource != null)
            return resource.getName();
        else {
            if(LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Warning, some code requested the LayerInfo name, but the resource is not set");
            return null;
        }
    }

    /**
     * This whole method is part of the workaround.
     */
    public void setName(String name) {
        // TODO: remove this log and reinstate field assignment when resource/publish split is complete
        if(LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Warning, some code is setting the LayerInfo name, but that will be ignored");
        this.name = name;
        if(resource != null)
            resource.setName(name);
    }

    @PrePersist
    public void prepersist() {
        if(resource != null) {
            setName(resource.getName());
        }
    }

}
