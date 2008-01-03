package org.geoserver.wcs.kvp;

/**
 * The only GridCS the WCS 1.1 specification talks about...
 * @author Administrator
 *
 */
public enum GridCS {
    GCSGrid2dSquare("urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS");
    
    private String xmlConstant;

    GridCS(String xmlConstant) {
        this.xmlConstant = xmlConstant;
    }

    public String getXmlConstant() {
        return xmlConstant;
    }
}
