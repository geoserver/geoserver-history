/**
 * @author Christian Mueller
 * 
 * Class for holding  a geometry role param
 *
 */


package org.geoserver.xacml.role;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;

public class GeometryRoleParam implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
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
