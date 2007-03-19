/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import net.opengis.wfs.FeatureCollectionType;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDSchema;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.GetFeature;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class GML3OutputFormat extends WFSGetFeatureOutputFormat {
    WFS wfs;
    Data catalog;
    WFSConfiguration configuration;

    public GML3OutputFormat(WFS wfs, Data catalog, WFSConfiguration configuration) {
        super("text/xml; subtype=gml/3.1.1");

        this.wfs = wfs;
        this.catalog = catalog;
        this.configuration = configuration;
    }

    public String getMimeType(Object value, Operation operation) {
        return "text/xml; subtype=gml/3.1.1";
    }

    protected void write(FeatureCollectionType results, OutputStream output, Operation getFeature)
        throws ServiceException, IOException {
        List featureCollections = results.getFeature();

        // round up the info objects for each feature collection
        HashMap /* <String,Set> */ ns2metas = new HashMap();

        for (Iterator fc = featureCollections.iterator(); fc.hasNext();) {
            GetFeature.GTHackFeatureCollection features = (GetFeature.GTHackFeatureCollection) fc
                .next();
            AttributeDescriptor descriptor = features.getISOFeatureType();

            // load the metadata for the feature type
            Name name = descriptor.getName();
            String namespaceURI = name.getNamespaceURI();
            String localPart = name.getLocalPart();
            FeatureTypeInfo meta = catalog.getFeatureTypeInfo(localPart, namespaceURI);

            // add it to the map
            Set metas = (Set) ns2metas.get(namespaceURI);

            if (metas == null) {
                metas = new HashSet();
                ns2metas.put(namespaceURI, metas);
            }

            metas.add(meta);
        }

        XSDSchema schema = configuration.schema();

        Encoder encoder = new Encoder(configuration, schema);

        // declare wfs schema location
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
            ResponseUtils.appendPath(wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd"));

        // declare application schema namespaces
        for (Iterator i = ns2metas.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();

            String namespaceURI = (String) entry.getKey();
            Set metas = (Set) entry.getValue();

            StringBuffer typeNames = new StringBuffer();

            for (Iterator m = metas.iterator(); m.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
                typeNames.append(meta.getName());

                if (m.hasNext()) {
                    typeNames.append(",");
                }
            }

            // set the schema location
            encoder.setSchemaLocation(namespaceURI,
                ResponseUtils.appendQueryString(wfs.getOnlineResource().toString(),
                    "service=WFS&version=1.1.0&request=DescribeFeatureType&typeName="
                    + typeNames.toString()));
        }

        try {
            encoder.encode(results, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output);
        } catch (SAXException e) {
            String msg = "Error occurred encoding features";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }
}
