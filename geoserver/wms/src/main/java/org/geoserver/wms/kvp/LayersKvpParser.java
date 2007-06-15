package org.geoserver.wms.kvp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoserver.ows.FlatKvpParser;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WmsException;

public class LayersKvpParser extends FlatKvpParser {

    Data catalog;
    
    public LayersKvpParser( Data catalog ) {
        super("layers", MapLayerInfo.class );
        this.catalog = catalog;
    }

    protected Object parse(List values) throws Exception {
        MapLayerInfo[] layers;
        
        List layerNames = values;
        List realLayerNames = new ArrayList();

        String layerName = null;
        
        int l_counter = 0;
      
        ////
        // Expand the eventually WMS grouped layers into the same WMS Path element
        ////
        for (Iterator it = layerNames.iterator(); it.hasNext();) {
            layerName = (String) it.next();

            Integer layerType = catalog.getLayerType(layerName);

            if (layerType == null) {
                ////
                // Search for grouped layers (attention: heavy process)
                ////
                String catalogLayerName = null;

                for (Iterator c_keys = catalog.getLayerNames().iterator(); c_keys.hasNext();) {
                    catalogLayerName = (String) c_keys.next();

                    try {
                        FeatureTypeInfo ftype = findFeatureLayer(catalogLayerName);
                        String wmsPath = ftype.getWmsPath();

                        if ((wmsPath != null) && wmsPath.matches(".*/" + layerName)) {
                            realLayerNames.add(catalogLayerName);
                            l_counter++;

//                            if (l_counter > s_counter) {
//                                rawStyles = ((rawStyles.length() > 0) ? (rawStyles + ",") : rawStyles)
//                                    + ftype.getDefaultStyle().getName();
//                            }
                        }
                    } catch (WmsException e_1) {
                        try {
                            CoverageInfo cv = findCoverageLayer(catalogLayerName);
                            String wmsPath = cv.getWmsPath();

                            if ((wmsPath != null) && wmsPath.matches(".*/" + layerName)) {
                                realLayerNames.add(catalogLayerName);
                                l_counter++;

//                                if (l_counter > s_counter) {
//                                    rawStyles = ((rawStyles.length() > 0) ? (rawStyles + ",")
//                                                                          : rawStyles)
//                                        + cv.getDefaultStyle().getName();
//                                }
                            }
                        } catch (WmsException e_2) {
                        }
                    }
                }
            } else {
                realLayerNames.add(layerName);
                l_counter++;
            }
        }

        int layerCount = realLayerNames.size();

        if (layerCount == 0) {
            throw new WmsException("No LAYERS has been requested", getClass().getName());
        }

        layers = new MapLayerInfo[layerCount];

        for (int i = 0; i < layerCount; i++) {
            layerName = (String) layerNames.get(i);
            layers[i] = new MapLayerInfo();

            try {
                FeatureTypeInfo ftype = findFeatureLayer(layerName);

                layers[i].setFeature(ftype);
            } catch (WmsException e) {
                CoverageInfo cv = findCoverageLayer(layerName);

                layers[i].setCoverage(cv);
            }
        }

        return layers;
    }
    
    FeatureTypeInfo findFeatureLayer(String layerName)
        throws WmsException {
        
        FeatureTypeInfo ftype = null;
        Integer layerType = catalog.getLayerType(layerName);
    
        if (Data.TYPE_VECTOR != layerType) {
            throw new WmsException(new StringBuffer(layerName).append(
                    ": no such layer on this server").toString(), "LayerNotDefined");
        } else {
            ftype = catalog.getFeatureTypeInfo(layerName);
        }
    
        return ftype;
    }
    
    CoverageInfo findCoverageLayer(String layerName)
        throws WmsException {
        
        CoverageInfo cv = null;
        Integer layerType = catalog.getLayerType(layerName);
    
        if (Data.TYPE_RASTER != layerType) {
            throw new WmsException(new StringBuffer(layerName).append(
                    ": no such layer on this server").toString(), "LayerNotDefined");
        } else {
            cv = catalog.getCoverageInfo(layerName);
        }
    
        return cv;
    }
}
