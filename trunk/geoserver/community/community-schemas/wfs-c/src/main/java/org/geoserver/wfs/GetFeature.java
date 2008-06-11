/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.WfsFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.feature.FeatureAccess;
import org.geotools.data.feature.FeatureSource2;
import org.geotools.data.feature.adapter.FeatureCollectionAdapter;
import org.geotools.data.feature.adapter.ISOFeatureTypeAdapter;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureList;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.iso.FeatureCollections;
import org.geotools.feature.iso.Types;
import org.geotools.feature.iso.simple.SimpleFeatureFactoryImpl;
import org.geotools.feature.iso.type.AttributeDescriptorImpl;
import org.geotools.feature.visitor.FeatureVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.ProgressListener;
import org.geotools.xml.EMFUtils;
import org.opengis.feature.simple.SimpleFeatureFactory;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.AttributeTypeInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.namespace.QName;


/**
 * Web Feature Service GetFeature operation.
 * <p>
 * This operation returns an array of
 * {@link org.geotools.feature.FeatureCollection} instances.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 * @version $Id$
 */
public class GetFeature {
    /**
     * This is a temporary wraper for the ISO Features collections implementing
     * geotools FeatureCollection so there's no need to modify or fork the WFS
     * emf generated classes. In other words, this is a HACK and has to die once
     * we get an homogeinized feature model.
     *
     * @author gabriel
     */
    public final class GTHackFeatureCollection implements FeatureCollection {
        private AttributeDescriptor isoType;
        private org.opengis.feature.FeatureCollection isoFeatures;

        public GTHackFeatureCollection(org.opengis.feature.FeatureCollection isoFeatures,
            AttributeDescriptor isoType) {
            this.isoFeatures = isoFeatures;
            this.isoType = isoType;
        }

        public AttributeDescriptor getISOFeatureType() {
            return isoType;
        }

        public void close(FeatureIterator arg0) {
        }

        public void close(Iterator iterator) {
            isoFeatures.close(iterator);
        }

        public Iterator iterator() {
            Iterator iterator = isoFeatures.iterator();

            return iterator;
        }

        public int size() {
            int size = FeatureCollections.getSize(isoFeatures);

            return size;
        }

        public void accepts(FeatureVisitor arg0, ProgressListener arg1)
            throws IOException {
            throw new UnsupportedOperationException();
        }

        public void addListener(CollectionListener arg0)
            throws NullPointerException {
            throw new UnsupportedOperationException();
        }

        public FeatureIterator features() {
            throw new UnsupportedOperationException();
        }

        public org.geotools.feature.FeatureType getFeatureType() {
            throw new UnsupportedOperationException();
        }

        public org.geotools.feature.FeatureType getSchema() {
            throw new UnsupportedOperationException();
        }

        public void removeListener(CollectionListener arg0)
            throws NullPointerException {
            throw new UnsupportedOperationException();
        }

        public FeatureList sort(SortBy arg0) {
            throw new UnsupportedOperationException();
        }

        public FeatureCollection subCollection(Filter arg0) {
            throw new UnsupportedOperationException();
        }

        public void purge() {
            throw new UnsupportedOperationException();
        }

        public boolean add(Object arg0) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection arg0) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean containsAll(Collection arg0) {
            throw new UnsupportedOperationException();
        }

        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection arg0) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection arg0) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray(Object[] arg0) {
            throw new UnsupportedOperationException();
        }

        public Object getAttribute(String arg0) {
            throw new UnsupportedOperationException();
        }

        public Object getAttribute(int arg0) {
            throw new UnsupportedOperationException();
        }

        public Object[] getAttributes(Object[] arg0) {
            throw new UnsupportedOperationException();
        }

        public ReferencedEnvelope getBounds() {
            throw new UnsupportedOperationException();
        }

        public Geometry getDefaultGeometry() {
            return getPrimaryGeometry();
        }

        public Geometry getPrimaryGeometry() {
            throw new UnsupportedOperationException();
        }

        public String getID() {
            throw new UnsupportedOperationException();
        }

        public int getNumberOfAttributes() {
            throw new UnsupportedOperationException();
        }

        public void setAttribute(int arg0, Object arg1)
            throws IllegalAttributeException, ArrayIndexOutOfBoundsException {
            throw new UnsupportedOperationException();
        }

        public void setAttribute(String arg0, Object arg1)
            throws IllegalAttributeException {
            throw new UnsupportedOperationException();
        }

        public void setDefaultGeometry(Geometry arg0) throws IllegalAttributeException {
            setPrimaryGeometry(arg0);
        }

        public void setPrimaryGeometry(Geometry geometry)
            throws IllegalAttributeException {
            throw new UnsupportedOperationException();
        }
    }

    /** Standard logging instance for class */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests");

    /** The catalog */
    protected Data catalog;

    /** The wfs configuration */
    protected WFS wfs;

    /** filter factory */
    protected FilterFactory filterFactory;

    /**
     * Creates the GetFeature operation.
     *
     */
    public GetFeature(WFS wfs, Data catalog) {
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

    public FeatureCollectionType run(GetFeatureType request)
        throws WFSException {
        List queries = request.getQuery();

        if (queries.isEmpty()) {
            throw new WFSException("No query specified");
        }

        if (EMFUtils.isUnset(queries, "typeName")) {
            String msg = "No feature types specified";
            throw new WFSException(msg);
        }

        // Optimization Idea
        //
        // We should be able to reduce this to a two pass opperations.
        //
        // Pass #1 execute
        // - Attempt to Locks Fids during the first pass
        // - Also collect Bounds information during the first pass
        //
        // Pass #2 writeTo
        // - Using the Bounds to describe our FeatureCollections
        // - Iterate through FeatureResults producing GML
        //
        // And allways remember to release locks if we are failing:
        // - if we fail to aquire all the locks we will need to fail and
        // itterate through the the FeatureSources to release the locks
        //
        if (request.getMaxFeatures() == null) {
            request.setMaxFeatures(BigInteger.valueOf(Integer.MAX_VALUE));
        }

        int maxFeatures = request.getMaxFeatures().intValue();

        FeatureCollectionType result = WfsFactory.eINSTANCE.createFeatureCollectionType();
        int count = 0; // should probably be long

        try {
            for (int i = 0; (i < request.getQuery().size()) && (count <= maxFeatures); i++) {
                QueryType query = (QueryType) request.getQuery().get(i);

                FeatureTypeInfo meta = null;

                if (query.getTypeName().size() == 1) {
                    meta = featureTypeInfo((QName) query.getTypeName().get(0));
                } else {
                    // TODO: a join is taking place
                }

                List atts = meta.getAttributes();
                List attNames = meta.getAttributeNames();

                // make sure property names are cool
                List propNames = query.getPropertyName();

                for (Iterator iter = propNames.iterator(); iter.hasNext();) {
                    String propName = (String) iter.next();

                    // HACK: strip off namespace
                    if (propName.indexOf(':') != -1) {
                        propName = propName.substring(propName.indexOf(':') + 1);
                    }

                    if (!attNames.contains(propName)) {
                        String mesg = "Requested property: " + propName + " is " + "not available "
                            + "for " + query.getTypeName() + ".  " + "The possible propertyName "
                            + "values are: " + attNames;

                        throw new WFSException(mesg);
                    }
                }

                // we must also include any properties that are mandatory ( even
                // if not requested ),
                // ie. those with minOccurs > 0
                if (propNames.size() != 0) {
                    Iterator ii = atts.iterator();
                    List tmp = new LinkedList();

                    while (ii.hasNext()) {
                        AttributeTypeInfo ati = (AttributeTypeInfo) ii.next();
                        LOGGER.finer("checking to see if " + propNames + " contains" + ati);

                        if (((ati.getMinOccurs() > 0) && (ati.getMaxOccurs() != 0))) {
                            // mandatory, add it
                            tmp.add(ati.getName());

                            continue;
                        }

                        // check if it was requested
                        for (Iterator p = propNames.iterator(); p.hasNext();) {
                            String propName = (String) p.next();

                            if (propName.matches("(\\w+:)?" + ati.getName())) {
                                tmp.add(ati.getName());

                                break;
                            }
                        }
                    }

                    // replace property names
                    query.getPropertyName().clear();
                    query.getPropertyName().addAll(tmp);
                }

                DataStore dataStore = meta.getDataStoreInfo().getDataStore();
                String typeName = meta.getTypeName();

                final FeatureSource source;
                final FeatureType featureType;
                final org.opengis.feature.FeatureCollection features;

                AttributeDescriptor descriptor;

                if (dataStore instanceof FeatureAccess) {
                    String uri = meta.getNameSpace().getURI();
                    Name name = Types.typeName(uri, typeName);
                    source = (FeatureSource2) ((FeatureAccess) dataStore).access(name);

                    descriptor = (AttributeDescriptor) ((FeatureSource2) source).describe();
                    featureType = (FeatureType) descriptor.getType();
                } else {
                    source = dataStore.getFeatureSource(typeName);
                    featureType = new ISOFeatureTypeAdapter(source.getSchema());

                    Name tname = featureType.getName();
                    Name name = Types.typeName(tname.getNamespaceURI(), tname.getLocalPart());
                    descriptor = new AttributeDescriptorImpl(featureType, name, 0,
                            Integer.MAX_VALUE, true, null);
                }

                int queryMaxFeatures = maxFeatures - count;
                org.geotools.data.Query gtQuery = toDataQuery(query, queryMaxFeatures, featureType);
                LOGGER.fine("Query is " + query + "\n To gt2: " + gtQuery);

                if (source instanceof FeatureSource2) {
                    Filter filter = gtQuery.getFilter();
                    FeatureSource2 fsource = (FeatureSource2) source;
                    features = (org.opengis.feature.FeatureCollection) fsource.content(filter,
                            queryMaxFeatures);
                } else {
                    FeatureCollection gtFeatures = source.getFeatures(gtQuery);
                    SimpleFeatureFactory featureFactory = new SimpleFeatureFactoryImpl();
                    features = new FeatureCollectionAdapter((SimpleFeatureType) featureType,
                            gtFeatures, featureFactory);
                    ((FeatureCollectionAdapter) features).setMaxFeatures(queryMaxFeatures);
                }

                count += FeatureCollections.getSize(features);

                FeatureCollection hackFeatureCollection = new GTHackFeatureCollection(features,
                        descriptor);

                result.getFeature().add(hackFeatureCollection);
            }
        } catch (IOException e) {
            throw new WFSException("Error occurred getting features", e, request.getHandle());
        }

        // locking
        if (request instanceof GetFeatureWithLockType) {
            GetFeatureWithLockType withLockRequest = (GetFeatureWithLockType) request;

            LockFeatureType lockRequest = WfsFactory.eINSTANCE.createLockFeatureType();
            lockRequest.setExpiry(withLockRequest.getExpiry());
            lockRequest.setHandle(withLockRequest.getHandle());
            lockRequest.setLockAction(AllSomeType.ALL_LITERAL);

            for (int i = 0; i < request.getQuery().size(); i++) {
                QueryType query = (QueryType) request.getQuery().get(i);

                LockType lock = WfsFactory.eINSTANCE.createLockType();
                lock.setFilter(query.getFilter());
                lock.setHandle(query.getHandle());

                // TODO: joins?
                lock.setTypeName((QName) query.getTypeName().get(0));
                lockRequest.getLock().add(lock);
            }

            LockFeature lockFeature = new LockFeature(wfs, catalog);
            lockFeature.setFilterFactory(filterFactory);

            LockFeatureResponseType response = lockFeature.lockFeature(lockRequest);
            result.setLockId(response.getLockId());
        }

        result.setNumberOfFeatures(BigInteger.valueOf(count));
        result.setTimeStamp(Calendar.getInstance());

        return result;
    }

    /**
     * Get this query as a geotools Query.
     *
     * <p>
     * if maxFeatures is a not positive value DefaultQuery.DEFAULT_MAX will be
     * used.
     * </p>
     *
     * <p>
     * The method name is changed to toDataStoreQuery since this is a one way
     * conversion.
     * </p>
     *
     * @param maxFeatures
     *            number of features, or 0 for DefaultQuery.DEFAULT_MAX
     *
     * @return A Query for use with the FeatureSource interface
     *
     */
    public org.geotools.data.Query toDataQuery(QueryType query, int maxFeatures, FeatureType schema)
        throws WFSException {
        if (maxFeatures <= 0) {
            maxFeatures = DefaultQuery.DEFAULT_MAX;
        }

        String[] props = null;

        if (!query.getPropertyName().isEmpty()) {
            props = new String[query.getPropertyName().size()];

            for (int p = 0; p < query.getPropertyName().size(); p++) {
                String propertyName = (String) query.getPropertyName().get(p);
                props[p] = propertyName;
            }
        }

        Filter filter = (Filter) query.getFilter();

        if (filter == null) {
            filter = Filter.INCLUDE;
        }

        // only handle non-joins for now
        QName typeName = (QName) query.getTypeName().get(0);
        DefaultQuery dataQuery = new DefaultQuery(typeName.getLocalPart(), filter, maxFeatures,
                props, query.getHandle());

        AttributeDescriptor defaultGeometry = schema.getDefaultGeometry();
        GeometryType geomType = (GeometryType) ((defaultGeometry == null) ? null
                                                                          : defaultGeometry.getType());

        // figure out the crs the data is in
        CoordinateReferenceSystem crs = (geomType == null) ? null : geomType.getCRS();

        if (crs == null) {
            // set to be the server default
            try {
                crs = CRS.decode("EPSG:4326");
                dataQuery.setCoordinateSystem(crs);
            } catch (Exception e) {
                // should never happen
                throw new RuntimeException(e);
            }
        }

        // handle reprojection
        if (query.getSrsName() != null) {
            CoordinateReferenceSystem target;

            try {
                target = CRS.decode(query.getSrsName().toString());
            } catch (Exception e) {
                String msg = "Unable to support srsName: " + query.getSrsName();
                throw new WFSException(msg, e);
            }

            // if the crs are not equal, then reproject
            if (!crs.equals(target)) {
                dataQuery.setCoordinateSystemReproject(crs);
            }
        }

        // handle sorting
        if (query.getSortBy() != null) {
            List sortBy = query.getSortBy();
            dataQuery.setSortBy((SortBy[]) sortBy.toArray(new SortBy[sortBy.size()]));
        }

        return dataQuery;
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
