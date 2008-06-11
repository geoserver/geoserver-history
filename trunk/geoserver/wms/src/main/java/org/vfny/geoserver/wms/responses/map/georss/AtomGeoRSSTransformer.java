/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;


public class AtomGeoRSSTransformer extends GeoRSSTransformerBase {
    public Translator createTranslator(ContentHandler handler) {
        return new AtomGeoRSSTranslator(handler);
    }

    public class AtomGeoRSSTranslator extends GeoRSSTranslatorSupport {
        public AtomGeoRSSTranslator(ContentHandler contentHandler) {
            super(contentHandler, null, "http://www.w3.org/2005/Atom");
            
            nsSupport.declarePrefix("georss","http://www.georss.org/georss");
        }

        public void encode(Object o) throws IllegalArgumentException {
            WMSMapContext map = (WMSMapContext) o;

            start("feed");

            //title
            element("title", map.getTitle());

            //link
            //TODO: make getFeature request
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(null, "href", "href", null, "http://localhost...");
            element("link", null, atts);

            //updated
            //TODO: proper xml encoding of date
            element("updated", new Date().toString());

            //entries
            try {
                encodeEntries(map);
            } 
            catch (IOException e) {
                throw new RuntimeException(e);
            }

            end("feed");
        }

        void encodeEntries(WMSMapContext map) throws IOException{
            List featureCollections = loadFeatureCollections(map);
            for (Iterator f = featureCollections.iterator(); f.hasNext();) {
                FeatureCollection<SimpleFeatureType, SimpleFeature> features = (FeatureCollection) f.next();
                FeatureIterator <SimpleFeature> iterator = null;

                try {
                    iterator = features.features();

                    while (iterator.hasNext()) {
                        SimpleFeature feature = iterator.next();
                        try {
                            encodeEntry(feature);
                        }
                        catch( Exception e ) {
                            LOGGER.warning("Encoding failed for feature: " + feature.getID());
                            LOGGER.log(Level.FINE, "", e );
                        }
                        
                    }
                } finally {
                    if (iterator != null) {
                        features.close(iterator);
                    }
                }
            }
            
        }

        void encodeEntry(SimpleFeature feature) {
            start("entry");

            //title
            element("title", feature.getID());

            //link
            //TODO: make getFeature with fid filter
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(null, "href", "href", null, "http://localhost...");
            element("link", null, atts);

            //id
            element("id", feature.getID());

            //updated
            //TODO: proper xml encoding of date
            element("updated", new Date().toString());

            //content
            element("content", "some content");

            //where
            start("georss:where");
            encodeGeometry(feature);
            end("georss:where");

            end("entry");
        }
    }
}
