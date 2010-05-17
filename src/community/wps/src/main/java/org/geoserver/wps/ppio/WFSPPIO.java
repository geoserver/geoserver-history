/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.ppio;

import java.io.InputStream;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

import org.geotools.feature.FeatureCollection;
import org.geotools.wfs.WFS;
import org.geotools.wfs.v1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.ContentHandler;

/**
 * Allows reading and writing a WFS feature collection
 */
public class WFSPPIO extends XMLPPIO {

    protected WFSPPIO() {
        super( FeatureCollectionType.class, FeatureCollection.class, "text/xml; subtype=wfs-collection/1.0", WFS.FeatureCollection );
    }

    @Override
    public Object decode(InputStream input) throws Exception {
        //TODO: parse the input stream as a wfs:FeatureCollection
        return null;
    }
    
    @Override
    public Object decode(Object input) throws Exception {
        FeatureCollectionType fc = (FeatureCollectionType) input;
        return fc.getFeature().get( 0 );
    }
    
    @Override
    public void encode(Object object, ContentHandler handler) throws Exception {
        FeatureCollection features = (FeatureCollection) object;
        SimpleFeatureType featureType = (SimpleFeatureType) features.getSchema();
        
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fc.getFeature().add( features );
        
        Encoder e = new Encoder( new WFSConfiguration() );
        e.getNamespaces().declarePrefix( "feature", featureType.getName().getNamespaceURI() );
        e.encode( fc, WFS.FeatureCollection, handler );
    }
}
