package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.map.MapLayer;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;

public class OWS5Transformer extends KMLTransformer {
    
    private boolean extendedDataModule;
    private boolean styleModule;
    
    public OWS5Transformer(boolean extendedDataModule, boolean styleModule) {
        super();
        this.extendedDataModule = extendedDataModule;
        this.styleModule = styleModule;
    }

    public Translator createTranslator(ContentHandler handler) {
        return new KML3Translator(handler);
    }

    protected class KML3Translator extends KMLTranslator {

        public KML3Translator(ContentHandler handler) {
            super(handler);
        }

        protected KMLVectorTransformer createVectorTransformer(WMSMapContext mapContext,
                MapLayer layer) {
            return new OWS5VectorTransformer(mapContext, layer, extendedDataModule, styleModule);
        }
    }

}
