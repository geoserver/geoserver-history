package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;
import org.geotools.map.MapLayer;
import org.geotools.xml.transform.Translator;

public class GeoSearchTransformer extends KMLTransformer {

    Data catalog;
    
    public GeoSearchTransformer(Data catalog) {
        this.catalog = catalog;
    }
    
    public Translator createTranslator(ContentHandler handler){
        return new KML3Translator(handler);
    }

    protected class KML3Translator extends KMLTranslator {
        public KML3Translator(ContentHandler handler){
            super(handler);
        }

        protected KMLVectorTransformer createVectorTransformer(WMSMapContext mapContext,
                MapLayer layer){
            return new GeoSearchVectorTransformer(mapContext, layer, catalog);
        }
    }
}

