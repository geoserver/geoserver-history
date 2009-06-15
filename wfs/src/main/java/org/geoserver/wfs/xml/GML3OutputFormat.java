/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.wfs.BaseRequestType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Encoder;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;


public class GML3OutputFormat extends WFSGetFeatureOutputFormat {
    WFSInfo wfs;
    Catalog catalog;
    GeoServerInfo global;
    WFSConfiguration configuration;

    public GML3OutputFormat(GeoServer geoServer, WFSConfiguration configuration) {
        super(new HashSet(Arrays.asList(new Object[] {"gml3", "text/xml; subtype=gml/3.1.1"})));

        this.wfs = geoServer.getService( WFSInfo.class );
        this.catalog = geoServer.getCatalog();
        this.global = geoServer.getGlobal();
      
        this.configuration = configuration;
    }

    public String getMimeType(Object value, Operation operation) {
        return "text/xml; subtype=gml/3.1.1";
    }
    
    public String getCapabilitiesElementName() {
        return "GML3";
    }

    protected void write(FeatureCollectionType results, OutputStream output, Operation getFeature)
        throws ServiceException, IOException {
        List featureCollections = results.getFeature();

        // round up the info objects for each feature collection
        HashMap<String, Set<FeatureTypeInfo>> ns2metas = new HashMap<String, Set<FeatureTypeInfo>>();
        for (int fcIndex = 0; fcIndex < featureCollections.size(); fcIndex++) {
            if(getFeature.getParameters()[0] instanceof GetFeatureType) {
                // get the query for this featureCollection
                GetFeatureType request = (GetFeatureType) OwsUtils.parameter(getFeature.getParameters(),
                        GetFeatureType.class);
                QueryType queryType = (QueryType) request.getQuery().get(fcIndex);
                
                // may have multiple type names in each query, so add them all
                for (QName name : (List<QName>) queryType.getTypeName()) {
                    // get a feature type name from the query
                    Name featureTypeName = new NameImpl(name.getNamespaceURI(), name.getLocalPart());
                    FeatureTypeInfo meta = catalog.getFeatureTypeByName(featureTypeName);
                    
                    if (meta == null) {
                        throw new WFSException("Could not find feature type " + featureTypeName
                                + " in the GeoServer catalog");
                    }
                    
                    // add it to the map
                    Set<FeatureTypeInfo> metas = ns2metas.get(featureTypeName.getNamespaceURI());
                    
                    if (metas == null) {
                        metas = new HashSet<FeatureTypeInfo>();
                        ns2metas.put(featureTypeName.getNamespaceURI(), metas);
                    }
                    metas.add(meta);
                }
            } else {
                FeatureType featureType = ((FeatureCollection) featureCollections.get(fcIndex)).getSchema();

                //load the metadata for the feature type
                String namespaceURI = featureType.getName().getNamespaceURI();
                FeatureTypeInfo meta = catalog.getFeatureTypeByName(featureType.getName());
                
                if(meta == null)
                    throw new WFSException("Could not find feature type " + featureType.getName() + " in the GeoServer catalog");

                //add it to the map
                Set metas = (Set) ns2metas.get(namespaceURI);

                if (metas == null) {
                    metas = new HashSet();
                    ns2metas.put(namespaceURI, metas);
                }

                metas.add(meta);
            }
        }

        //set feature bounding parameter
        //JD: this is quite bad as its not at all thread-safe, once we remove the configuration
        // as being a singleton on trunk/2.0.x this should not be an issue
        if ( wfs.isFeatureBounding() ) {
            configuration.getProperties().remove( GMLConfiguration.NO_FEATURE_BOUNDS );
        }
        else {
            configuration.getProperties().add( GMLConfiguration.NO_FEATURE_BOUNDS);
        }
        
        Encoder encoder = new Encoder(configuration, configuration.schema());
        encoder.setEncoding(Charset.forName( global.getCharset() ));

        //declare wfs schema location
        BaseRequestType gft = (BaseRequestType)getFeature.getParameters()[0];
        
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(gft.getBaseUrl(),global.getProxyBaseUrl());
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
            ResponseUtils.appendPath(proxifiedBaseUrl, "schemas/wfs/1.1.0/wfs.xsd"));

        //declare application schema namespaces
        for (Iterator i = ns2metas.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            String namespaceURI = (String) entry.getKey();
            Set metas = (Set) entry.getValue();

            StringBuffer typeNames = new StringBuffer();

            for (Iterator m = metas.iterator(); m.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
                typeNames.append(meta.getPrefixedName());
                
                if (m.hasNext()) {
                    typeNames.append(",");
                }
            }

            //set the schema location
            encoder.setSchemaLocation(namespaceURI,
                ResponseUtils.appendQueryString(proxifiedBaseUrl + "wfs",
                    "service=WFS&version=1.1.0&request=DescribeFeatureType&typeName="
                    + typeNames.toString()));
        }

        encoder.encode(results, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output);
    }
}
