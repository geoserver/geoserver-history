package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.map.MapLayer;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;

public class KML3Transformer extends KMLTransformer {

    public Translator createTranslator(ContentHandler handler) {
        return new KML3Translator(handler);
    }

    protected class KML3Translator extends KMLTranslator {

        public KML3Translator(ContentHandler handler) {
            super(handler);
        }

        protected KMLVectorTransformer createVectorTransformer(WMSMapContext mapContext,
                MapLayer layer) {
            return new KML3VectorTransformer(mapContext, layer);
        }
    }

}
