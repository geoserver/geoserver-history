package org.vfny.geoserver.wms.responses.map.kml;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.wms.util.WMSRequests;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import org.xml.sax.ContentHandler;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Encodes a KML document contianing a network link.
 * <p>
 * This transformer transforms a {@link GetMapRequest} object.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class KMLNetworkLinkTransformer extends TransformerBase {

    /**
     * flag controlling wether the network link should be a super overlay.
     */
    boolean encodeAsRegion = false;
 
    public Translator createTranslator(ContentHandler handler) {
        return new KMLNetworkLinkTranslator( handler );
    }
    
    public void setEncodeAsRegion(boolean encodeAsRegion) {
        this.encodeAsRegion = encodeAsRegion;
    }
    
    class KMLNetworkLinkTranslator extends TranslatorSupport {

        public KMLNetworkLinkTranslator(ContentHandler contentHandler) {
            super(contentHandler, null,null);
        }
        
        public void encode(Object o) throws IllegalArgumentException {
            GetMapRequest request = (GetMapRequest) o;
            
            start( "kml" );
            start( "Folder" );
        
            if ( encodeAsRegion ) {
                encodeAsSuperOverlay( request );
            }
            else {
                encodeAsOverlay( request );
            }
            
            end( "Folder" );
            end( "kml" );
        }
        
        protected void encodeAsSuperOverlay( GetMapRequest request ) {
            MapLayerInfo[] layers = request.getLayers();
            for ( int i = 0; i < layers.length; i++ ) {
                start("NetworkLink");
                element( "name", layers[i].getName() );
                element( "open", "1" );
                element( "visibility", "1" );
             
                //region
                start( "Region" );
                
                Envelope bbox = request.getBbox();
                start( "LatLonAltBox" );
                element( "north", ""+bbox.getMaxY() );
                element( "south", ""+bbox.getMinY() );
                element( "east", ""+bbox.getMaxX() );
                element( "west", ""+bbox.getMinX() );
                end( "LatLonAltBox");
                
                start( "Lod" );
                element( "minLodPixels", "256" );
                element( "maxLodPixels", "-1" );
                end( "Lod" );
                
                end( "Region" );
                
                //link
                start("Link" );
  
                String href = WMSRequests.getGetMapUrl(request, layers[i].getName(),null, null, null);
                start( "href" );
                cdata( href );
                end( "href" );
                
                element( "viewRefreshMode", "onRegion" );
                end( "Link" );
                
                end( "NetworkLink");
            }
        }
        
        protected void encodeAsOverlay( GetMapRequest request ) {
            MapLayerInfo[] layers = request.getLayers();
            for ( int i = 0; i < layers.length; i++ ) {
                start("NetworkLink");
                element( "name", layers[i].getName() );
                element( "open", "1" );
                element( "visibility", "1" );
                
                start( "Url" );
                
                String href = WMSRequests.getGetMapUrl(request, layers[i].getName(),null, null, null);
                start( "href" );
                cdata( href );
                end( "href" );
                
                element( "viewRefreshMode", "onStop" );
                element( "viewRefreshTime", "3" );
                end( "Url" );
                
                end( "NetworkLink" );
            }
        }
    }
}
