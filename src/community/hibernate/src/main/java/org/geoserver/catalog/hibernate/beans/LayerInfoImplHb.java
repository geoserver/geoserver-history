/*
 */

package org.geoserver.catalog.hibernate.beans;

import java.util.logging.Logger;
import org.geoserver.catalog.ResourceInfo;
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
     * ETj: This is a terrible workaround to fix the new behaviour introduced in revision 13349 of
     * LayerInfoImpl: setName() will set the resource name instead of this very instance's name. It
     * causes an error when hibernate sets the various attribute, when the name is set before the
     * resource.
     */
    private transient String transientName = null;

    /**
     * This whole method is part of the workaround.
     */
    @Override
    public void setName(String name) {
        if (resource == null) {
            // cache the name
            transientName = name;
            LOGGER.warning("WORKAROUND in LayerInfoImplHb.setName()");
        } else {
            transientName = null;
            super.setName(name);
        }

    }

    /**
     * This whole method is part of the workaround.
     */
    @Override
    public void setResource(ResourceInfo resource) {
        super.setResource(resource);

        if (transientName != null) {
            setName(transientName);
            LOGGER.warning("WORKAROUND in LayerInfoImplHb.setResource()");
        }
    }

}
