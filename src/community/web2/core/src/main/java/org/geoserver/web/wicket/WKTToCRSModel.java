package org.geoserver.web.wicket;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.model.IModel;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A model allowing to edit an WKT property with the CRSPanel (by dynamically
 * converting it into a {@link CoordinateReferenceSystem} and back)
 * @author Andrea Aime - OpenGeo
 *
 */
@SuppressWarnings("serial")
public class WKTToCRSModel implements IModel {
    private static final Logger LOGGER = Logging.getLogger(WKTToCRSModel.class);
    IModel srsModel; 
    
    public WKTToCRSModel(IModel srsModel) {
        this.srsModel = srsModel;
    }

    public Object getObject() {
        String wkt = (String) srsModel.getObject();
        try {
            return CRS.parseWKT(wkt);
        } catch(Exception e) {
            return null;
        }
    }

    public void setObject(Object object) {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) object;
        try {
            srsModel.setObject(crs.toString());
        } catch(Exception e) {
            LOGGER.log(Level.INFO, "Failed to lookup the SRS code for " + crs);
            srsModel.setObject(null);
        }
        
    }

    public void detach() {
        srsModel.detach();
    }
    
}