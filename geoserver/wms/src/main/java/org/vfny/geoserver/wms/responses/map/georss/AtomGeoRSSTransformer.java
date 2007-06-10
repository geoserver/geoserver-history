package org.vfny.geoserver.wms.responses.map.georss;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.map.MapLayer;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

public class AtomGeoRSSTransformer extends GeoRSSTransformerBase {

   
    public Translator createTranslator(ContentHandler handler) {
        return new AtomGeoRSSTranslator( handler );
    }

    public class AtomGeoRSSTranslator extends GeoRSSTranslatorSupport {

        public AtomGeoRSSTranslator(ContentHandler contentHandler ) {
            super(contentHandler, null, "http://www.w3.org/2005/Atom" );
        }

        public void encode(Object o) throws IllegalArgumentException {
            WMSMapContext map = (WMSMapContext) o;
            
            start( "feed" );
           
            //title
            element( "title", map.getTitle() );
           
            //link
            //TODO: make getFeature request
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(null, "href", "href", null, "http://localhost..." );
            element( "link", null, atts );
            
            //updated
            //TODO: proper xml encoding of date
            element( "updated", new Date().toString() );
            
            //entries
            encodeEntries( map );
            
            end( "feed" );
        }
        
        void encodeEntries( WMSMapContext map ) {
            for ( int i = 0; i < map.getLayerCount(); i++ ) {
                MapLayer layer = map.getLayer( i );
                
                FeatureCollection features = null;
                try {
                    FeatureSource source = layer.getFeatureSource();
                    features = source.getFeatures();
                }
                catch( Exception e ) {
                    String msg = "Unable to encode map layer: " + layer ;
                    LOGGER.log( Level.SEVERE, msg, e );
                }
                
                if ( features != null ) {
                    
                    FeatureIterator iterator = null;
                    try {
                        iterator = features.features();
                        while( iterator.hasNext() ) {
                            encodeEntry( iterator.next() );
                        }
                    }
                    finally {
                        if ( iterator != null ) {
                            features.close( iterator );
                        }
                    }
                    
                }
                
            }
        }
        
        void encodeEntry( Feature feature ) {
            start( "entry" );
            
            //title
            element( "title", feature.getID() );
            
            //link
            //TODO: make getFeature with fid filter
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(null, "href", "href", null, "http://localhost..." );
            element( "link", null, atts );
            
            //id
            element( "id", feature.getID() );
            
            //updated
            //TODO: proper xml encoding of date
            element( "updated", new Date().toString() );
            
            //content
            element( "content", "some content" );
            
            //where
            start( "georss:where" );
            encodeGeometry( feature );
            end( "georss:where" );
            
            end( "entry" );
        }
     
        
    }
}
