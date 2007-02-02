/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data;

import org.geotools.catalog.Service;
import org.geotools.catalog.ServiceFinder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Loads the GeoServer catalog on container startup.
 * <p>
 * This bean needs to be defined in a spring context as follows:
 * <pre>
 *         <code>
 *         &lt;bean id="catalog" class="org.geoserver.data.GeoServerCatalog"/&gt;
 *  &lt;bean id="serviceFinder" class="org.geoserver.data.GeoServerServiceFinder"/&gt;
 *         &lt;bean id="catalogLoader" class="org.geoserver.data.CatalogLoader"&gt;
 *                 &lt;constructor-arg ref="catalog"/&gt;
 *              &lt;constructor-arg ref="serviceFinder"/&gt;
 *         &lt;/bean&gt;
 *         </code>
 * </pre>
 * </p>
 */
public class CatalogLoader implements ResourceLoaderAware, InitializingBean, DisposableBean {
    GeoServerCatalog catalog;
    ServiceFinder finder;
    ResourceLoader loader;

    public CatalogLoader(GeoServerCatalog catalog, ServiceFinder finder) {
        this.catalog = catalog;
        this.finder = finder;
    }

    public void setResourceLoader(ResourceLoader loader) {
        this.loader = loader;
    }

    public void afterPropertiesSet() throws Exception {
        Resource resource = loader.getResource("classpath:catalog.xml");

        if (!resource.exists()) {
            String msg = "Could not find catalog.xml";
            throw new Exception(msg);
        }

        //read the catalog file
        File catalogFile = resource.getFile();
        CatalogReader reader = new CatalogReader();
        reader.read(catalogFile);

        //populate the catalog with datastores
        for (Iterator d = reader.dataStores().iterator(); d.hasNext();) {
            Map params = (Map) d.next();
            List services = finder.aquire(params);

            for (Iterator s = services.iterator(); s.hasNext();) {
                catalog.add((Service) s.next());
            }
        }

        //setup namespace mappings
        Map nsMappings = reader.namespaces();

        for (Iterator ns = nsMappings.entrySet().iterator(); ns.hasNext();) {
            Map.Entry nsMapping = (Map.Entry) ns.next();

            String pre = (String) nsMapping.getKey();
            String uri = (String) nsMapping.getValue();

            catalog.getNamespaceSupport().declarePrefix(pre, uri);
        }
    }

    public void destroy() throws Exception {
        //TODO: write out catalog.xml file
    }
}
