/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.GetLogType;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.data.FeatureSource;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.store.MaxFeaturesFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.filter.expression.AbstractExpressionVisitor;
import org.geotools.filter.visitor.AbstractFilterVisitor;
import org.geotools.xml.EMFUtils;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.PropertyName;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.namespace.QName;


/**
 * Versioning Web Feature Service GetLog operation
 *
 * @author Andrea Aime, TOPP
 *
 */
public class GetLog {
    /**
     * logger
     */
    static Logger LOGGER = Logger.getLogger("org.geoserver.wfsv");

    /**
     * WFS configuration
     */
    WFS wfs;

    /**
     * The catalog
     */
    Data catalog;

    /**
     * Filter factory
     */
    FilterFactory filterFactory;

    /**
     * Creates the GetLog operation
     *
     * @param wfs
     * @param catalog
     */
    public GetLog(WFS wfs, Data catalog) {
        this.wfs = wfs;
        this.catalog = catalog;
    }

    /**
     * @return The reference to the GeoServer catalog.
     */
    public Data getCatalog() {
        return catalog;
    }

    /**
     * @return The reference to the WFS configuration.
     */
    public WFS getWFS() {
        return wfs;
    }

    /**
     * Sets the filter factory to use to create filters.
     *
     * @param filterFactory
     */
    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public FeatureCollectionType run(GetLogType request) {
        List queries = request.getDifferenceQuery();

        // basic checks
        if (queries.isEmpty()) {
            throw new WFSException("No difference query specified");
        }

        if (EMFUtils.isUnset(queries, "typeName")) {
            String msg = "No feature types specified";
            throw new WFSException(msg);
        }

        FeatureCollectionType result = WfsFactory.eINSTANCE.createFeatureCollectionType();
        int residual = request.getMaxFeatures() != null ? request.getMaxFeatures().intValue() : Integer.MAX_VALUE;

        // for each difference query check the feature type is versioned, and
        // gather bounds
        try {
            for (int i = 0; i < queries.size() && residual > 0; i++) {
                DifferenceQueryType query = (DifferenceQueryType) queries.get(i);
                FeatureTypeInfo meta = featureTypeInfo((QName) query.getTypeName());
                FeatureSource source = meta.getFeatureSource();

                if (!(source instanceof VersioningFeatureSource)) {
                    throw new WFSException("Feature type" + query.getTypeName()
                        + " is not versioned");
                }

                Filter filter = (Filter) query.getFilter();

                //  make sure filters are sane
                if (filter != null) {
                    final FeatureType featureType = source.getSchema();
                    ExpressionVisitor visitor = new AbstractExpressionVisitor() {
                            public Object visit(PropertyName name, Object data) {
                                // case of multiple geometries being returned
                                if (name.evaluate(featureType) == null) {
                                    // we want to throw wfs exception, but cant
                                    throw new WFSException("Illegal property name: "
                                        + name.getPropertyName(), "InvalidParameterValue");
                                }

                                return name;
                            }
                            ;
                        };

                    filter.accept(new AbstractFilterVisitor(visitor), null);
                }

                // extract collection
                VersioningFeatureSource store = (VersioningFeatureSource) source;
                FeatureCollection logs = store.getLog(query.getFromFeatureVersion(),
                        query.getToFeatureVersion(), filter, null, residual);
                residual -= logs.size();

                // TODO: handle logs reprojection in another CRS
                result.getFeature().add(logs);
            }
        } catch (IOException e) {
            throw new WFSException("Error occurred getting features", e, request.getHandle());
        }

        result.setNumberOfFeatures(BigInteger.valueOf(residual));
        result.setTimeStamp(Calendar.getInstance());

        return result;
    }

    FeatureTypeInfo featureTypeInfo(QName name) throws WFSException, IOException {
        FeatureTypeInfo meta = catalog.getFeatureTypeInfo(name.getLocalPart(),
                name.getNamespaceURI());

        if (meta == null) {
            String msg = "Could not locate " + name + " in catalog.";
            throw new WFSException(msg);
        }

        return meta;
    }
}
