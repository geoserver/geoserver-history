package org.geoserver.wfs.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.wfs.BaseRequestType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;

import org.apache.commons.collections.MultiHashMap;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.WFSInfo;
import org.geotools.feature.FeatureCollection;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GML2OutputFormat2 extends WFSGetFeatureOutputFormat {

    WFSInfo wfs;
    Catalog catalog;
    GeoServerResourceLoader resourceLoader;
    
    public GML2OutputFormat2(GeoServer gs) {
        super(new HashSet(Arrays.asList(new Object[] {"gml2", "text/xml; subtype=gml/2.1.2"})));

        this.wfs = gs.getService( WFSInfo.class );
        this.catalog = gs.getCatalog();
        this.resourceLoader = catalog.getResourceLoader();
    }

    public String getMimeType(Object value, Operation operation) {
        return "text/xml; subtype=gml/2.1.2";
    }
    
    public String getCapabilitiesElementName() {
        return "GML2";
    }

    protected void write(FeatureCollectionType results, OutputStream output, Operation getFeature)
        throws ServiceException, IOException {
        
        //declare wfs schema location
        BaseRequestType gft = (BaseRequestType)getFeature.getParameters()[0];
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(gft.getBaseUrl(), 
            wfs.getGeoServer().getGlobal().getProxyBaseUrl());
        
        List featureCollections = results.getFeature();

        //round up the info objects for each feature collection
        MultiHashMap ns2metas = new MultiHashMap();
        
        for (Iterator fc = featureCollections.iterator(); fc.hasNext();) {
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = (FeatureCollection) fc.next();
            SimpleFeatureType featureType = features.getSchema();

            //load the metadata for the feature type
            String namespaceURI = featureType.getName().getNamespaceURI();
            FeatureTypeInfo meta = catalog.getFeatureTypeByName( namespaceURI, featureType.getTypeName() );
            if(meta == null)
                throw new WFSException("Could not find feature type " + namespaceURI + ":" + featureType.getTypeName() + " in the GeoServer catalog");

            NamespaceInfo ns = catalog.getNamespaceByURI( namespaceURI );
            ns2metas.put( ns, meta );
        }

        Collection<FeatureTypeInfo> featureTypes = ns2metas.values();
        
        //create the encoder
        ApplicationSchemaXSD xsd = new ApplicationSchemaXSD( null, catalog, proxifiedBaseUrl, 
            org.geotools.wfs.v1_0.WFS.getInstance(), featureTypes );
        Configuration configuration = new ApplicationSchemaConfiguration( xsd, new org.geotools.wfs.v1_0.WFSConfiguration() );
        
        Encoder encoder = new Encoder(configuration);
        //encoder.setEncoding(wfs.getCharSet());
        
       encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
            ResponseUtils.appendPath(proxifiedBaseUrl, "schemas/wfs/1.0.0/WFS-basic.xsd"));

        //declare application schema namespaces
        for (Iterator i = ns2metas.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            NamespaceInfo ns = (NamespaceInfo) entry.getKey();
            String namespaceURI = ns.getURI();
            
            Collection metas = (Collection) entry.getValue();

            StringBuffer typeNames = new StringBuffer();

            for (Iterator m = metas.iterator(); m.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
                typeNames.append(meta.getName());

                if (m.hasNext()) {
                    typeNames.append(",");
                }
            }

            //set the schema location
            encoder.setSchemaLocation(namespaceURI,
                ResponseUtils.appendQueryString(proxifiedBaseUrl + "wfs",
                    "service=WFS&version=1.0.0&request=DescribeFeatureType&typeName="
                    + typeNames.toString()));
        }

        encoder.encode(results, org.geotools.wfs.v1_0.WFS.FeatureCollection, output);
    }

}
