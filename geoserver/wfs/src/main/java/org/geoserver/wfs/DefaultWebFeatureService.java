/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.GetGmlObjectType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;

import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.Service;
import org.geotools.util.Version;
import org.geotools.xml.transform.TransformerBase;
import org.opengis.filter.FilterFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;


/**
 * Web Feature Service implementation.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class DefaultWebFeatureService implements WebFeatureService, ApplicationContextAware {
    /**
     * WFS service configuration.
     */
    protected WFS wfs;

    /**
     * The catalog
     */
    protected Data catalog;

    /**
     * Filter factory
     */
    protected FilterFactory filterFactory;

    /**
     * The spring application context, used to look up transaction listeners, plugins and
     * element handlers
     */
    protected ApplicationContext context;

    /**
     * Versions of wfs supported
     */
    protected List<Version> versions;
    
    public DefaultWebFeatureService(WFS wfs, Data catalog) {
        this.wfs = wfs;
        this.catalog = catalog;
    }

    /**
     * The list of verions of wfs supported.
     */
    public List<Version> getVersions() {
        return versions;
    }
    
    /**
     * Sets the fitler factory.
     */
    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    /**
     * WFS GetCapabilities operation.
     *
     * @param request The get capabilities request.
     *
     * @return A transformer instance capable of serializing a wfs capabilities
     * document.
     *
     * @throws WFSException Any service exceptions.
     */
    public TransformerBase getCapabilities(GetCapabilitiesType request)
        throws WFSException {
        return new GetCapabilities(wfs, catalog, versions).run(request);
    }

    /**
     * WFS DescribeFeatureType operation.
     *
     * @param request The describe feature type request.
     *
     * @return A set of feature type metadata objects.
     *
     * @throws WFSException Any service exceptions.
     */
    public FeatureTypeInfo[] describeFeatureType(DescribeFeatureTypeType request)
        throws WFSException {
        return new DescribeFeatureType(wfs, catalog).run(request);
    }

    /**
     * WFS GetFeature operation.
     *
     * @param request The get feature request.
     *
     * @return A feature collection type instance.
     *
     * @throws WFSException Any service exceptions.
     */
    public FeatureCollectionType getFeature(GetFeatureType request)
        throws WFSException {
        GetFeature getFeature = new GetFeature(wfs, catalog);
        getFeature.setFilterFactory(filterFactory);

        return getFeature.run(request);
    }

    /**
     * WFS GetFeatureWithLock operation.
     *
     * @param request The get feature with lock request.
     *
      * @return A feature collection type instance.
     *
     * @throws WFSException Any service exceptions.
     */
    public FeatureCollectionType getFeatureWithLock(GetFeatureWithLockType request)
        throws WFSException {
        return getFeature(request);
    }

    /**
     * WFS LockFeatureType operation.
     *
     * @param request The lock feature request.
     *
     * @return A lock feture response type.
     *
     * @throws WFSException An service exceptions.
     */
    public LockFeatureResponseType lockFeature(LockFeatureType request)
        throws WFSException {
        LockFeature lockFeature = new LockFeature(wfs, catalog);
        lockFeature.setFilterFactory(filterFactory);

        return lockFeature.lockFeature(request);
    }

    /**
     * WFS transaction operation.
     *
     * @param request The transaction request.
     *
     * @return A transaction response instance.
     *
     * @throws WFSException Any service exceptions.
     */
    public TransactionResponseType transaction(TransactionType request)
        throws WFSException {
        Transaction transaction = new Transaction(wfs, catalog, context);
        transaction.setFilterFactory(filterFactory);

        return transaction.transaction(request);
    }
    
    /**
     * WFS GetGmlObject operation.
     * 
     * @param request The GetGmlObject request.
     *
     * @return The gml object request.
     * 
     * @throws WFSException Any service exceptions.
     *
     */
    public Object getGmlObject(GetGmlObjectType request) throws WFSException {
        
        GetGmlObject getGmlObject = new GetGmlObject(wfs,catalog);
        getGmlObject.setFilterFactory( filterFactory );
        
        return getGmlObject.run( request );
    }
    
    //the following operations are not part of the spec
    public void releaseLock(String lockId) throws WFSException {
        new LockFeature(wfs, catalog).release(lockId);
    }

    public void releaseAllLocks() throws WFSException {
        new LockFeature(wfs, catalog).releaseAll();
    }

    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        this.context = context;
        
        //load versions from service descriptors in application context
        versions = new ArrayList<Version>();
        List<Service> services = GeoServerExtensions.extensions(Service.class,context);
        for (Service s : services ) {
            if ( s.getService() instanceof WebFeatureService ) {
                versions.add( s.getVersion() );    
            }
        }
        Collections.sort( versions );
    }
}
