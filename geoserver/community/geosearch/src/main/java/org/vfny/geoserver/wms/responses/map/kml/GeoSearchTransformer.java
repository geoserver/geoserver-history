package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;
import org.geotools.map.MapLayer;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.data.Query;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;

public class GeoSearchTransformer extends KMLTransformer {

    public GeoSearchTransformer() {
    }

    public Translator createTranslator(ContentHandler handler){
        return new GeoSearchKMLTranslator(handler);
    }

    protected class GeoSearchKMLTranslator extends KMLTranslator {
        public GeoSearchKMLTranslator(ContentHandler handler){
            super(handler);
        }

        protected KMLVectorTransformer createVectorTransformer(WMSMapContext mapContext,
                MapLayer layer){
            return new GeoSearchVectorTransformer(mapContext, layer);
        }
    }
}

