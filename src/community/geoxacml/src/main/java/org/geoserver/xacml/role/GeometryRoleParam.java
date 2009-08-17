/**
 * @author Christian Mueller
 * 
 * Class for holding  a geometry role param
 *
 */


package org.geoserver.xacml.role;

import com.vividsolutions.jts.geom.Geometry;

public class GeometryRoleParam {
    
    private Geometry geometry;
    private String srsName;
    public Geometry getGeometry() {
        return geometry;
    }
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
    public String getSrsName() {
        return srsName;
    }
    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

}
