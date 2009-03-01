package org.geoserver.wfs;

/**
 * Configuration for gml encoding.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface GMLInfo {

    /**
     * Enumeration for srsName style.
     * <p>
     * <ul>
     *   <li>{@link #NORMAL} : EPSG:XXXX
     *   <li>{@link #XML} : http://www.opengis.net/gml/srs/epsg.xml#XXXX
     *   <li>{@link #URN} : urn:x-ogc:def:crs:EPSG:XXXX
     * </ul>
     * <p>
     *
     */
    public static enum SrsNameStyle {
        NORMAL {
            public String getPrefix() {
                return "EPSG:";
            }
        },
        XML {
            public String getPrefix() {
                return "http://www.opengis.net/gml/srs/epsg.xml#";
            }
        },
        URN {
            public String getPrefix() {
                return "urn:x-ogc:def:crs:EPSG:";
            }  
        };
        
        abstract public String getPrefix();
    }
    
    /**
     * The srs name style to be used when encoding the gml 'srsName' attribute.
     */
    SrsNameStyle getSrsNameStyle();
    
    /**
     * Sets the srs name style to be used when encoding the gml 'srsName' attribute.
     */
    void setSrsNameStyle( SrsNameStyle srsNameStyle );
}
