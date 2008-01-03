package org.geoserver.wcs.kvp;

/**
 * The WCS 1.1 grid type enumeration
 * 
 * @author Andrea Aime
 * 
 */
public enum GridType {
    GT2dGridIn2dCrs("urn:ogc:def:method:WCS:1.1:2dGridIn2dCrs"), //
    GT2dGridIn3dCrs("urn:ogc:def:method:WCS:1.1:2dGridIn3dCrs"), //
    GT2dSimpleGrid("urn:ogc:def:method:WCS:1.1:2dSimpleGrid");

    private String xmlConstant;

    GridType(String xmlConstant) {
        this.xmlConstant = xmlConstant;
    }

    public String getXmlConstant() {
        return xmlConstant;
    }
}
