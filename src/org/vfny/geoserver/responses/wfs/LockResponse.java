/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.Feature;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wfs.LockRequest;
import org.vfny.geoserver.responses.Response;


/**
 * Handles a Lock request and creates a LockResponse string.
 *
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: LockResponse.java,v 1.11 2004/02/09 23:29:42 dmzwiers Exp $
 *
 * @task TODO: implement response streaming in writeTo instead of the current
 *       response String generation
 */
public class LockResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** indicates whether the output should be formatted. */
    private static boolean verbose = false;

    /** the new line character to use in the response. */
    private static String nl = "";

    /** temporal, it will disappear when the response streaming be implemented */
    private String xmlResponse = null;
    FeatureLock featureLock;
    LockRequest request;

    /**
     * Constructor
     *
     * @param gs DOCUMENT ME!
     */
    public LockResponse(GeoServer gs) {
        featureLock = null;
        request = null;
        verbose = gs.isVerbose();
        nl = verbose ? "\n" : "";
    }

    public void execute(Request req) throws WfsException {
        if (!(req instanceof LockRequest)) {
            throw new WfsException("bad request, expected LockRequest, got "
                + req);
        }

        request = (LockRequest) req;
        xmlResponse = getXmlResponse(request);
    }

    public String getContentType(GeoServer gs) {
        return gs.getMimeType();
    }

    public void writeTo(OutputStream out) throws ServiceException {
        try {
            byte[] content = xmlResponse.getBytes();
            out.write(content);
        } catch (IOException ex) {
            throw new WfsException(ex, "", getClass().getName());
        }
    }

    /**
     * Parses the LockFeature reqeust and returns either the full xml lock
     * result or just the lockId.
     *
     * @param request the locks to attempt
     * @param getXml if <tt>true</tt> then the full xml response is returned,
     *        if <tt>false</tt> then only the lockId is returned.
     *
     * @return XML response or lockId, depending on getXml
     *
     * @throws WfsException for any problems doing the lock.
     * @throws IOException DOCUMENT ME!
     *
     * @task REVISIT: this will have to be reworked for the next version of the
     *       spec, when getFeatureWithLock can specify lockAction, but we'll
     *       cross that bridge when we come to it.
     * @task TODO: I really think we've got too much code duplication here with
     *       FeatureResponse.  Locking is easily the most complex operation
     *       we've got, and having each do their own thing will easily mess
     *       things up with just a little change.  The fact that the code is
     *       forked has already given me a good amount of greif, but I don't
     *       want to rework it before 1.1.0.  But there should be a single
     *       performLock that both can use, or else really move _all_ the
     *       functionality to geotools. I do understand that
     *       getFeatureWithLock does need to be sure that there are no time
     *       differences between lock acquiring and reporting, but something
     *       needs to be done.
     */
    public static String performLock(LockRequest request, boolean getXml)
        throws WfsException, IOException {
        LOGGER.finer("about to do Lock response on:" + request);

        List locks = request.getLocks();

        if (locks.size() == 0) {
            throw new WfsException("A LockFeature request must contain at "
                + "least one LOCK element");
        }

        LockRequest.Lock curLock = (LockRequest.Lock) locks.get(0);
        boolean lockAll = request.getLockAll();

        FeatureLock featureLock = request.toFeatureLock();
        Set lockedFids = new HashSet();
        Set lockFailedFids = new HashSet();
        GeoServer config = request.getGeoServer();
        Data catalog = request.getWFS().getData();
        FilterFactory filterFactory = FilterFactory.createFilterFactory();
        LOGGER.info("locks size is " + locks.size());

        if (locks.size() == 0) {
            throw new WfsException("Lock Request must contain at least one "
                + " Lock element, your request is " + request);
        }

        for (int i = 0, n = locks.size(); i < n; i++) {
            curLock = (LockRequest.Lock) locks.get(i);
            LOGGER.info("curLock is " + curLock);

            String curTypeName = curLock.getFeatureType();
            Filter curFilter = curLock.getFilter();

            FeatureTypeInfo meta = catalog.getFeatureTypeInfo(curTypeName);
            NameSpaceInfo namespace = meta.getDataStoreInfo().getNameSpace();
            FeatureSource source = meta.getFeatureSource();
            FeatureResults features = source.getFeatures(curFilter);
            
            if( source instanceof FeatureLocking){            
                ((FeatureLocking)source).setFeatureLock(featureLock);
            }
            FeatureReader reader = null;

            try {
                for (reader = features.reader(); reader.hasNext();) {
                    Feature feature = reader.next();
                    String fid = feature.getID();
                    if( !(source instanceof FeatureLocking)){
                        LOGGER.fine("Lock " + fid +
                                " not supported by data store (authID:"
                                + featureLock.getAuthorization() + ")");
                        lockFailedFids.add(fid);                        
                    }
                    else {
                        Filter fidFilter = filterFactory.createFidFilter(fid);
                    
                        //DEFQuery is just some indirection, should be in the locking interface.
                        //int numberLocked = ((DEFQueryFeatureLocking)source).lockFeature(feature);
                        //HACK: Query.NO_NAMES isn't working in postgis right now,
                        //so we'll just use all.
                        Query query = new DefaultQuery(
                                meta.getShortName(), fidFilter,
                                Query.DEFAULT_MAX, Query.ALL_NAMES,
                                curLock.getHandle());
                        int numberLocked = ((FeatureLocking)source).lockFeatures( query );
    
                        if (numberLocked == 1) {
                            LOGGER.fine("Lock " + fid + " (authID:"
                                + featureLock.getAuthorization() + ")");
                            lockedFids.add(fid);
                        } else if (numberLocked == 0) {
                            LOGGER.fine("Lock " + fid + " conflict (authID:"
                                + featureLock.getAuthorization() + ")");
                            lockFailedFids.add(fid);
                        } else {
                            LOGGER.warning("Lock " + numberLocked + " " + fid
                                + " (authID:" + featureLock.getAuthorization()
                                + ") duplicated FeatureID!");
                            lockedFids.add(fid);
                        }
                    }
                }
            } catch (IllegalAttributeException e) {
                // TODO: JG - I really dont like this
                // reader says it will throw this if the attribtues do not match
                // the FeatureTypeInfo
                // I figure if this is thrown we are poorly configured or
                // the DataStoreInfo needs some quality control
                //
                // should rollback the lock as well :-(
                throw new WfsException("Lock request " + curFilter
                    + " did not match " + curTypeName);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

        if (lockAll && !lockFailedFids.isEmpty()) {
            // I think we need to release and fail when lockAll fails
            //
            // abort will release the locks
            throw new WfsException("Could not aquire locks for:"
                + lockFailedFids);
        }

        if (getXml) {
            return generateXml(featureLock.getAuthorization(), lockAll,
                lockedFids, lockFailedFids, request);
        } else {
            return featureLock.getAuthorization();
        }
    }

    /**
     * Convenience function for backwards compatability, gets the full xml
     * response.
     *
     * @param request the locks to attempt.
     *
     * @return The xml response for this request.
     *
     * @throws WfsException If anything goes wrong.
     */
    private static String getXmlResponse(LockRequest request)
        throws WfsException {
        try {
            return performLock(request, true);
        } catch (IOException ioException) {
            ioException.printStackTrace(System.out);

            WfsException wfsException = new WfsException(
                    "Problem aquiring lock");
            wfsException.initCause(ioException);
            throw wfsException;
        }
    }

    /**
     * Creates the xml for a lock response.
     *
     * @param lockId the lockId to print in the response.
     * @param lockAll indicates if information about which features were locked
     *        should be printed.
     * @param lockedFeatures a list of features locked by the request.  This is
     *        not used if lockAll is false.
     * @param notLockedFeatures a list of features that matched the filter but
     *        were not locked by the request.  This is not used if lockAll is
     *        false.
     * @param request DOCUMENT ME!
     *
     * @return The xml response of this lock.
     */
    private static String generateXml(String lockId, boolean lockAll,
        Set lockedFeatures, Set notLockedFeatures, Request request) {
        String indent = verbose ? "   " : "";
        String xmlHeader = "<?xml version=\"1.0\" encoding=\""
            + request.getWFS().getGeoServer().getCharSet().displayName() + "\"?>";
        StringBuffer returnXml = new StringBuffer(xmlHeader);
        returnXml.append(nl + "<WFS_LockFeatureResponse " + nl);
        returnXml.append(indent + "xmlns=\"http://www.opengis.net/wfs\" " + nl);

        //this not needed yet, only when FeaturesLocked element used.
        //TODO: get rid of this hardcoding, and make a common utility to get all
        //these namespace imports, as everyone is using them, and changes should
        //go through to all the operations.
        if (!lockAll) {
            returnXml.append(indent
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" " + nl);
        }

        returnXml.append(indent + "xmlns:xsi=\"http://www.w3.org/2001/"
            + "XMLSchema-instance\" " + nl);
        returnXml.append(indent + "xsi:schemaLocation=\"http://www.opengis");
        returnXml.append(".net/wfs ");
        returnXml.append(request.getBaseUrl());
        returnXml.append("wfs/1.0.0/WFS-transaction.xsd\">");
        returnXml.append(nl);
        returnXml.append(indent + "<LockId>" + lockId + "</LockId>" + nl);

        if (!lockAll) {
            if ((lockedFeatures != null) && (lockedFeatures.size() > 0)) {
                returnXml.append(indent + "<FeaturesLocked>" + nl);

                for (Iterator i = lockedFeatures.iterator(); i.hasNext();) {
                    returnXml.append(indent + indent);
                    returnXml.append("<ogc:FeatureId fid=\"" + i.next()
                        + "\"/>" + nl);
                }

                returnXml.append(indent + "</FeaturesLocked>" + nl);
            }

            if ((notLockedFeatures != null) && (notLockedFeatures.size() > 0)) {
                returnXml.append("<FeaturesNotLocked>" + nl);

                for (Iterator i = notLockedFeatures.iterator(); i.hasNext();) {
                    returnXml.append(indent + indent);
                    returnXml.append("<ogc:FeatureId fid=\"" + i.next()
                        + "\"/>" + nl);
                }

                returnXml.append("</FeaturesNotLocked>" + nl);
            }
        }

        returnXml.append("</WFS_LockFeatureResponse>");

        return returnXml.toString();
    }

    /**
     * Release locks if lockAll failed.
     *
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort(Service gs) {
        if (request == null) {
            return; // request was not attempted
        }

        if (featureLock == null) {
            return; // we have no locks
        }

        Data catalog = gs.getData();

        // I think we need to release and fail when lockAll fails
        //
        try {
            GeoServer config = gs.getGeoServer();

            for (Iterator i = request.getLocks().iterator(); i.hasNext();) {
                LockRequest.Lock curLock = (LockRequest.Lock) i.next();

                String curTypeName = curLock.getFeatureType();

                FeatureTypeInfo meta = catalog.getFeatureTypeInfo(curTypeName);
                FeatureLocking source = (FeatureLocking) meta.getFeatureSource();

                Transaction t = new DefaultTransaction();

                try {
                    t.addAuthorization(featureLock.getAuthorization());
                    source.getDataStore().getLockingManager().release(featureLock
                        .getAuthorization(), t);
                } finally {
                    t.close();
                }
            }
        } catch (IOException ioException) {
            LOGGER.warning("Abort not complete:" + ioException);
        }
    }
}
