package org.geoserver.wms.kvp;

import java.awt.image.IndexColorModel;

import org.geoserver.ows.KvpParser;
import org.vfny.geoserver.config.PaletteManager;
import org.vfny.geoserver.wms.WmsException;

public class PaletteKvpParser extends KvpParser {

    public PaletteKvpParser() {
        super("palette", IndexColorModel.class );
    }

    public Object parse(String value) throws Exception {
        // palette
        try {
            IndexColorModel model = PaletteManager.getPalette(value);
    
            if (model == null) {
                throw new WmsException("Palette " + value + " could not be found "
                    + "in $GEOSERVER_DATA_DIR/palettes directory");
            }
            
            return model;
        } catch (Exception e) {
            throw new WmsException(e, "Palette " + value + " could not be loaded", null);
        }

    }

}
