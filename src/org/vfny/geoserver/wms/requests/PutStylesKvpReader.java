package org.vfny.geoserver.wms.requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.TemporaryFeatureTypeInfo;
import org.vfny.geoserver.util.SLDValidator;
import org.vfny.geoserver.wms.WmsException;
import org.xml.sax.InputSource;

public class PutStylesKvpReader extends WmsKvpRequestReader {

	/** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers.wms");
    
    StyleFactory styleFactory = StyleFactoryFinder.createStyleFactory();
    
	public PutStylesKvpReader(Map kvp) {
		super(kvp);
	}

	public Request getRequest(HttpServletRequest httpRequest)
		throws ServiceException {
	
		PutStylesRequest request = new PutStylesRequest();
		request.setHttpServletRequest(httpRequest);
		
		String version = getRequestVersion();
		request.setVersion(version);
		
		parse(request);
		
		return request;
	}

	protected void parse(PutStylesRequest request) throws WmsException {
		//parse the sld
		if (getValue("SLD") != null) {
			parseSldParam(request);
		}
		else if (getValue("SLD_BODY") != null) {
			parseSldBodyParam(request);
		}
		else {
			String msg = "No sld parameter found: SLD or SLD_BODY";
	        LOGGER.log(Level.WARNING, msg);
	        throw new WmsException(msg);	
		}
		
		//parse the mode
		String mode = getValue("MODE");
		if (mode != null) {
			if (mode.equalsIgnoreCase(PutStylesRequest.INSERT_AND_REPLACE)) {
				request.setMode(PutStylesRequest.INSERT_AND_REPLACE);
			}
			else if (mode.equalsIgnoreCase(PutStylesRequest.REPLACE_ALL)) {
				request.setMode(PutStylesRequest.REPLACE_ALL);
			}
			else {
				String msg = "Unsupported mode: " + mode;
		        LOGGER.log(Level.WARNING, msg);
		        throw new WmsException(msg);	
			}
		}
		else {
			String msg = "MODE is a mandatory parameter";
	        LOGGER.log(Level.WARNING, msg);
	        throw new WmsException(msg);	
		}
    }
	
	 /**
     * Takes the SLD_BODY parameter value and parses it to a geotools'
     * <code>StyledLayerDescriptor</code>, then takes the layers and styles to
     * use in the map composition from there.
     *
     * @param request DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    private void parseSldBodyParam(PutStylesRequest request)
        throws WmsException {
        final String sldBody = getValue("SLD_BODY");

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to parse SLD body: " + sldBody);
        }
                
        
        if (getValue("VALIDATESCHEMA") != null)
        {
        	//Get a reader from the given string
            Reader reader = getReaderFromString(sldBody);
        	//-InputStream in = new StringBufferInputStream(sldBody);
        	// user requested to validate the schema.
        	SLDValidator validator = new SLDValidator();
        	List errors =null;

        	 //Create a sax input source from the reader
            InputSource in = new InputSource(reader);
            errors = validator.validateSLD(in, request.getHttpServletRequest().getSession().getServletContext());
            if (errors.size() != 0) {
              reader = getReaderFromString(sldBody);
              throw new WmsException(SLDValidator.getErrorMessage(reader, errors));
            }

        }

        
        Reader reader = getReaderFromString(sldBody);
        SLDParser parser = new SLDParser(styleFactory, reader);
        StyledLayerDescriptor sld = parser.parseSLD();
        
        request.setStyledLayerDescriptor(sld);
    }
    
    /**
     * Create a reader of the given String.
     * This reader will be used in the InputSource for the sld parser.
     * The advantage with a reader over a input stream is that we don't have to consider encoding.
     * The xml declaration with encoding is ignored using a Reader in parser.
     * The encoding of the string has been appropiate handled by the servlet when streaming in.
     *
     * @param sldBody the sldbody to create a reader of.
     * @return The created reader
     * @see Reader
     */
    private Reader getReaderFromString(String sldBody) {
      return new StringReader(sldBody);
    }
    
	/**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    private void parseSldParam(PutStylesRequest request) throws WmsException 
	{
        String urlValue = getValue("SLD");               

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("about to load remote SLD document: '" + urlValue + "'");
        }

        URL sldUrl;

        try {
            sldUrl = new URL(urlValue);
        } catch (MalformedURLException e) {
            String msg = "Creating remote SLD url: " + e.getMessage();
            LOGGER.log(Level.WARNING, msg, e);
            throw new WmsException(e, msg, "parseSldParam");
        }

        
        if (getValue("VALIDATESCHEMA") != null)
        {
        	// user requested to validate the schema.
        	SLDValidator validator = new SLDValidator();
        	List errors =null;
        	try {
        		//JD: GEOS-420, Wrap the sldUrl in getINputStream method in order
        		// to do compression
        		InputStream in = getInputStream(sldUrl);
        		errors = validator.validateSLD(in, request.getHttpServletRequest().getSession().getServletContext());
        		in.close();
        		if (errors.size() != 0)
        			throw new WmsException(SLDValidator.getErrorMessage(sldUrl.openStream(),errors));
        	}
        	catch (IOException e)
			{
        		String msg = "Creating remote SLD url: " + e.getMessage();
                LOGGER.log(Level.WARNING, msg, e);
                throw new WmsException(e, msg, "parseSldParam");
			}
        }
        SLDParser parser;

        try {
        	//JD: GEOS-420, Wrap the sldUrl in getINputStream method in order
    		// to do compression
            parser = new SLDParser(styleFactory, getInputStream( sldUrl));
        } catch (IOException e) {
            String msg = "Creating remote SLD url: " + e.getMessage();
            LOGGER.log(Level.WARNING, msg, e);
            throw new WmsException(e, msg, "parseSldParam");
        }

        StyledLayerDescriptor sld = parser.parseSLD();
        request.setStyledLayerDescriptor(sld);
        
    }
    
    /**
     * This method gets the correct input stream for a URL.
     * If the URL is a http/https connection, the Accept-Encoding: gzip, deflate is added.
     * It the paramter is added, the response is checked to see if the response
     * is encoded in gzip, deflate or plain bytes. The correct input stream wrapper is then
     * selected and returned.
     *
     * This method was added as part of GEOS-420
     * 
     * @param sldUrl The url to the sld file
     * @return The InputStream used to validate and parse the SLD xml.
     * @throws IOException
     */
    private InputStream getInputStream(URL sldUrl) throws IOException {
      //Open the connection
      URLConnection conn = sldUrl.openConnection();

      //If it is the http or https scheme, then ask for gzip if the server supports it.
      if (conn instanceof HttpURLConnection) {
        //Send the requested encoding to the remote server.
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
      }
      //Conect to get the response headers
      conn.connect();
      //Return the correct inputstream
      //If the connection is a url, connection, check the response encoding.
      if (conn instanceof HttpURLConnection) {
        //Get the content encoding of the server response
        String encoding = conn.getContentEncoding();
        //If null, set it to a emtpy string
        if (encoding == null) encoding = "";
        if (encoding.equalsIgnoreCase("gzip")) {
          //For gzip input stream, use a GZIPInputStream
          return new GZIPInputStream(conn.getInputStream());
        } else if (encoding.equalsIgnoreCase("deflate")) {
          //If it is encoded as deflate, then select the inflater inputstream.
          return new InflaterInputStream(conn.getInputStream(), new Inflater(true));
        } else {
          //Else read the raw bytes
          return conn.getInputStream();
        }
      } else {
        //Else read the raw bytes.
        return conn.getInputStream();
      }
    }
    
    
}
