package org.geoserver.wms.kvp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.geoserver.ows.KvpParser;
import org.geoserver.ows.kvp.URLKvpParser;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.SLDParser;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.SLDValidator;
import org.vfny.geoserver.wms.WmsException;

public class SLDKvpParser extends KvpParser {

    StyleFactory styleFactory;
    
    public SLDKvpParser() {
        this( CommonFactoryFinder.getStyleFactory( null ) );
    }
    
    public SLDKvpParser(StyleFactory styleFactory) {
        super( "SLD", StyledLayerDescriptor.class );
        this.styleFactory = styleFactory;
    }
    
    public Object parse(String value) throws Exception {
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(new StringBuffer("about to load remote SLD document: '")
                .append(value).append("'").toString());
        }

        URL sldUrl = null;
        
        try {
            sldUrl = (URL) new URLKvpParser("SLD").parse( value );
        }
        catch (MalformedURLException e) {
            String msg = new StringBuffer("Creating remote SLD url: ").append(e.getMessage())
                                                                      .toString();

            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, msg, e);
            }

            throw new WmsException(e, msg, "parseSldParam");
        }
        
        SLDParser parser;

        try {
            //JD: GEOS-420, Wrap the sldUrl in getINputStream method in order
            // to do compression
            parser = new SLDParser(styleFactory, Requests.getInputStream(sldUrl));
        } catch (IOException e) {
            String msg = new StringBuffer("Creating remote SLD url: ").append(e.getMessage())
                                                                      .toString();

            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, msg, e);
            }

            throw new WmsException(e, msg, "parseSldParam");
        }

        return parser.parseSLD();
    }
}
