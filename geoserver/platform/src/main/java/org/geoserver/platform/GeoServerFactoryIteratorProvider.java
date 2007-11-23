package org.geoserver.platform;

import java.util.Iterator;
import java.util.List;

import org.geotools.factory.FactoryIteratorProvider;
import org.geotools.factory.GeoTools;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * A factory iterator provider which provides factories from a spring context.
 * <p>
 * This class allows us to tie into the geotools factory plugin mechanism.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeoServerFactoryIteratorProvider implements
        FactoryIteratorProvider, InitializingBean, DisposableBean {

    public <T> Iterator<T> iterator(Class<T> category) {
        List extensions = GeoServerExtensions.extensions(category);
        return extensions.iterator();
    }

    public void afterPropertiesSet() throws Exception {
        GeoTools.addFactoryIteratorProvider(this);
    }

    public void destroy() throws Exception {
        GeoTools.removeFactoryIteratorProvider(this);
    }

}
