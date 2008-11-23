/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.units.Converter;
import javax.units.SI;
import javax.units.Unit;
import javax.units.UnitFormat;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.factory.FactoryFinder;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeBuilder;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.SchemaException;
import org.geotools.feature.type.NumericAttributeType;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.LiteralExpression;
import org.geotools.measure.Measure;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.filter.And;
import org.opengis.filter.spatial.DWithin;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.AttributeTypeInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WFS;
import org.vfny.geoserver.wfs.Query;
import org.vfny.geoserver.wfs.WfsException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.operation.distance.DistanceOp;


/**
 * Handles a Get Feature request and creates a Get Feature response GML
 * string.
 *
 * @author Chris Holmes, TOPP
 * @author Jody Garnett, Refractions Research
 * @version $Id: GetNearestResponse.java,v 1.24 2004/04/05 12:03:19 cholmesny Exp $
 */
public class GetNearestResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses");
    GetNearestResponseDelegate delegate;
    String featureTypeName;

    /**
     * This is the request provided to the execute( Request ) method.<p>We
     * save it so we can access the handle provided by the user for error
     * reporting during the writeTo( OutputStream ) opperation.</p>
     *  <p>This value will be <code>null</code> until execute is
     * called.</p>
     */
    private GetNearestRequest request;

    /**
             * Empty constructor
             */
    public GetNearestResponse() {
        request = null;
    }

    /**
     * Returns any extra headers that this service might want to set in
     * the HTTP response object.
     *
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) {
        return delegate.getContentType(gs);
    }

    public String getContentEncoding() {
        return delegate.getContentEncoding();
    }

    /**
     * Jody here with one pass replacement for writeTo.<p>This code is
     * a discussion point, when everyone has had there input we will try and
     * set things up properly.</p>
     *  <p>I am providing a mirror of the existing desing: - execute
     * gathers the resultList - sets up the header</p>
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if ((request == null) || (delegate == null)) {
            throw new IllegalStateException("execute has not been called prior to writeTo");
        }

        delegate.encode(out);
    }

    /**
     * Executes GetNearestRequest.<p>Willing to execute a FetureRequest,
     * or GetNearestRequestWith Lock.</p>
     *
     * @param req DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request req) throws ServiceException {
        execute((GetNearestRequest) req);
    }

    /**
     * use the SPI mechanism to get a GetNearestResponseDelegate for the
     * specified output format.
     *
     * @param outputFormat
     *
     * @return
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public static GetNearestResponseDelegate getDelegate(String outputFormat)
        throws NoSuchElementException {
        GetNearestResponseDelegateProducerSpi spi;
        Iterator spi_it = FactoryFinder.factories(GetNearestResponseDelegateProducerSpi.class);

        while (spi_it.hasNext()) {
            spi = (GetNearestResponseDelegateProducerSpi) spi_it.next();
            if (spi.canProduce(outputFormat)) {
                return spi.createFeatureDelegateProducer(outputFormat);
            }
        }

        throw new NoSuchElementException();
    }

    /**
     * Performs a getFeatures, or getFeaturesWithLock (using gt2
     * locking ).<p>The idea is to grab the FeatureResulsts during
     * execute, and use them during writeTo.</p>
     *
     * @param request
     *
     * @throws ServiceException
     * @throws WfsException DOCUMENT ME!
     *
     * @task TODO: split this up a bit more?  Also get the proper namespace
     *       declrations and schema locations.  Right now we're back up to
     *       where we were with 1.0., as we can return two FeatureTypes in the
     *       same namespace.  CITE didn't check for two in different
     *       namespaces, and gml builder just couldn't deal.  Now we should be
     *       able to, we just need to get the reporting right, use the
     *       AllSameType function as  Describe does.
     */
    public void execute(GetNearestRequest request) throws ServiceException {
        LOGGER.finest("execute GetNearestRequest response. Called request is: " + request);
        this.request = request;
        this.featureTypeName = null;

        String outputFormat = request.getOutputFormat();

        try {
            delegate = GetNearestResponse.getDelegate(outputFormat);
        } catch (NoSuchElementException ex) {
            throw new WfsException("output format: " + outputFormat + " not "
                + "supported by geoserver", ex);
        }

        GetNearestResults results = new GetNearestResults(request);

        //
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
        //   itterate through the the FeatureSources to release the locks
        //
        WFS wfs = request.getWFS();
        GeoServer config = wfs.getGeoServer();
        Data catalog = wfs.getData();
        FeatureTypeInfo meta = null;
        NameSpaceInfo namespace;
        Query query;

        FeatureSource source;
        Feature feature;
        String fid;
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
        FidFilter fidFilter;

        try {
            for (Iterator it = request.getQueries().iterator(); it.hasNext();) {
                query = (Query) it.next();

                // the feature type name used in the content disposition response will match
                // the first feature type
                if (featureTypeName == null) {
                    featureTypeName = query.getTypeName();
                }

                meta = catalog.getFeatureTypeInfo(query.getTypeName());
                namespace = meta.getDataStoreInfo().getNameSpace();
                source = meta.getFeatureSource();

                List attrs = meta.getAttributes();

                List propNames = query.getPropertyNames(); // REAL LIST: be careful here :)
                List attributeNames = meta.getAttributeNames();

                for (Iterator iter = propNames.iterator(); iter.hasNext();) {
                    String propName = (String) iter.next();

                    if (!attributeNames.contains(propName)) {
                        String mesg = "Requested property: " + propName + " is "
                            + "not available for " + query.getTypeName() + ".  "
                            + "The possible propertyName values are: " + attributeNames;
                        throw new WfsException(mesg);
                    }
                    
                    
                }

                List extraGeometries = new ArrayList();
                List properties = new ArrayList();
                if (propNames.size() != 0) {
                    Iterator ii = attrs.iterator();
                    

                    while (ii.hasNext()) {
                        AttributeTypeInfo ati = (AttributeTypeInfo) ii.next();

                        //String attName = (String) ii.next();
                        LOGGER.finer("checking to see if " + propNames + " contains" + ati);

                        if (((ati.getMinOccurs() > 0) && (ati.getMaxOccurs() != 0))
                                || propNames.contains(ati.getName())) {
                            properties.add(ati.getName());
                        }
                        
                        //if(wfs.isFeatureBounding() && meta.getFeatureType().getAttributeType(ati.getName()) instanceof GeometryAttributeType
                        //        && !properties.contains(ati.getName())) {
                        //    properties.add(ati.getName());
                        //    extraGeometries.add(ati.getName());
                        //}
                        if(meta.getFeatureType().getAttributeType(ati.getName()) instanceof GeometryAttributeType
                                && !properties.contains(ati.getName())) {
                            properties.add(ati.getName());
                            extraGeometries.add(ati.getName());
                        }
                    }

                    query.setPropertyNames(properties);
                }
                
                // Add range to filter
                AttributeExpression geomAttb = filterFactory.createAttributeExpression(meta.getFeatureType(), meta.getFeatureType().getDefaultGeometry().getName());
                LiteralExpression pointExpr = filterFactory.createLiteralExpression(request.getPoint());
                DWithin dWithin = filterFactory.dwithin(geomAttb, pointExpr, request.getMaxRange(), request.getUnits());
                if (query.getFilter() == null) {
                    query.addFilter((Filter)dWithin);
                    
                } else {
                    And andFilter = filterFactory.and(Arrays.asList(new Filter[] { (Filter)dWithin, query.getFilter() }));
                    query.addFilter((Filter)andFilter);
                }
                
                LOGGER.fine("Query is " + query + "\n To gt2: " + query.toDataQuery(Integer.MAX_VALUE));

                //DJB: note if maxFeatures gets to 0 the while loop above takes care of this! (this is a subtle situation)
                FeatureCollection featuresCheck = source.getFeatures(query.toDataQuery(Integer.MAX_VALUE));
                
                // find nearest feature
                Unit fromUnit = SI.METER;
                Unit toUnit = UnitFormat.getInstance().parseUnit(request.getUnits());
                Converter unitConvert = fromUnit.getConverterTo(toUnit);
                Feature nearestFeature = null;
                double nearestDistance = 9e9;
                double nearestBearing = 0;
                for (Iterator sItr = featuresCheck.iterator(); sItr.hasNext();) {
                    Feature f = (Feature)sItr.next();
                    if (f.getDefaultGeometry() == null) continue;
                    DistanceOp op = new DistanceOp(request.getPoint(), f.getDefaultGeometry());
                    Coordinate[] co = op.closestPoints();
                    Measure m = DefaultGeographicCRS.WGS84.distance(new double[] { co[0].x, co[0].y, }, new double[] { co[1].x, co[1].y, });
                    if (m.doubleValue() > nearestDistance) continue;
                    nearestFeature = f;
                    nearestDistance = m.doubleValue();
                    nearestBearing = calcBearing(co);
                }

                //GR: I don't know if the featuresults should be added here for later
                //encoding if it was a lock request. may be after ensuring the lock
                //succeed?
                FeatureCollection features = FeatureCollections.newCollection();
                if (nearestFeature != null) features.add(superFeature(nearestFeature, unitConvert.convert(nearestDistance), nearestBearing));
                
                // we may need to shave off geometries we did load only to make bounds
                // computation happy
                if(extraGeometries.size() > 0) {
                    List residualProperties = new ArrayList(properties);
                    residualProperties.removeAll(extraGeometries);
                    residualProperties.add("nearest_distance");
                    residualProperties.add("nearest_bearing");
                    String[] residualNames = (String[]) residualProperties.toArray(new String[residualProperties.size()]);
                    FeatureType targetType = DataUtilities.createSubType(superFeatureType(meta.getFeatureType()), residualNames);
                    features = new FeatureBoundsFeatureCollection(features, targetType);
                }

                results.addFeatures(meta, features);
            }

            //end for
            //prepare to encode in the desired output format
            delegate.prepare(outputFormat, results);
        } catch (IOException e) {
            throw new ServiceException(e, "problem with FeatureResults", request.getHandle());
        } catch (NoSuchElementException e) {
            throw new ServiceException(e, "problem with FeatureResults", request.getHandle());
        } catch (SchemaException e) {
            throw new ServiceException(e, "problem with FeatureResults", request.getHandle());
        } catch (ParseException e) {
            throw new ServiceException(e, "problem with FeatureResults", request.getHandle());
        }
    }
    
    private FeatureType superFeatureType(FeatureType oldType) {
        FeatureType featureType = null;
        try {
            FeatureTypeBuilder typeBuilder = FeatureTypeBuilder.newInstance(oldType.getTypeName());
            typeBuilder.setNamespace(oldType.getNamespace());

            //typeBuilder.setDefaultGeometry(oldType.getDefaultGeometry());
            for (int i = 0; i < oldType.getAttributeCount(); i++) {
                //if (oldType.getAttributeType(i).equals(oldType.getDefaultGeometry())) continue;
                typeBuilder.addType(oldType.getAttributeType(i));
            }
            typeBuilder.addType(new NumericAttributeType("nearest_distance", Double.class, false, 0, 999, null, null));        
            typeBuilder.addType(new NumericAttributeType("nearest_bearing", Double.class, false, 0, 999, null, null));        

            featureType = typeBuilder.getFeatureType();
        } catch (Exception e) {
            LOGGER.severe("Error createing super feature type: " + e); e.printStackTrace();
        }
        return featureType;
    }
    
    private Feature superFeature(Feature oldFeature, Double distance, Double bearing) {
        FeatureType featureType = superFeatureType(oldFeature.getFeatureType());
        Object[] attbs = new Object[featureType.getAttributeCount()];
        for (int i = 0; i < oldFeature.getFeatureType().getAttributeCount(); i++) {
            attbs[i] = oldFeature.getAttribute(i);
        }
        attbs[attbs.length - 2] = distance;
        attbs[attbs.length - 1] = bearing;
        try {
            return featureType.create(attbs, oldFeature.getID());
        } catch (Exception e) {
            LOGGER.severe("Error createing super feature: " + e); e.printStackTrace();
        }
        return null;
    }
    
    private double calcBearing(Coordinate[] coords) {
        double y = Math.sin(coords[0].x - coords[1].x) * Math.cos(coords[1].y);
        double x = Math.cos(coords[0].y)*Math.sin(coords[1].y) - Math.sin(coords[0].y)*Math.cos(coords[1].y)*Math.cos(coords[0].x - coords[1].x);
        double brng = ((Math.atan2(y, x) * 180.0 / Math.PI) + 360) % 360;
        return brng;
    }

    /**
     * Release locks if we are into that sort of thing.
     *
     * @see org.vfny.geoserver.Response#abort()
     */
    public void abort(Service gs) {
    }

    public String getContentDisposition() {
        if ((featureTypeName != null) && (featureTypeName.indexOf(':') != -1)) {
            featureTypeName = featureTypeName.substring(featureTypeName.indexOf(':') + 1);
        }

        return delegate.getContentDisposition(featureTypeName);
    }
}
