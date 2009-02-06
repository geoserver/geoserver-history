package org.geoserver.catalog.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.rest.format.StreamDataFormat;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.restlet.data.MediaType;

public class SLDFormat extends StreamDataFormat {

    public SLDFormat() {
        super(new MediaType( "text/sld+xml"));
    }
    
    @Override
    protected void write(Object object, OutputStream out) throws IOException {
        Style style = (Style) object;
        
        //wrap in a StyledLayerDescriptor
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        NamedLayer nl = sf.createNamedLayer();
        nl.setName( style.getName() );
        nl.addStyle( style );
        
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setStyledLayers( new StyledLayer[]{ nl });
        
        SLDTransformer writer = new SLDTransformer();
        try {
            writer.transform( sld, out );
        } 
        catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    @Override
    protected Object read(InputStream in) throws IOException {
        SLDParser parser
            = new SLDParser( CommonFactoryFinder.getStyleFactory(null), in );
       
        Style[] styles = parser.readXML();
        if ( styles.length > 0 ) {
            return styles[0];
        }
        return null;
    }
}
