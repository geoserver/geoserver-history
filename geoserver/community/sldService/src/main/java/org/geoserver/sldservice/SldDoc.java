package org.geoserver.sldservice;

import java.io.File;
import java.io.IOException;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.styling.SLDParser;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.filter.FilterFactory2;

/*Should load the sld file*/
public class SldDoc {
	
	
	
	public StyledLayerDescriptor loadFile( File file) throws Exception {
		
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		StyleFactory factory = CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints());
        SLDParser stylereader = new SLDParser(factory, file);
        StyledLayerDescriptor newSLD = stylereader.parseSLD();
        //TODO: handle exceptions
        if (newSLD == null) {
            //exceptional!
            throw (IOException) new IOException("SLD import returned null"); //
        }
        return newSLD;
    }
	
}
