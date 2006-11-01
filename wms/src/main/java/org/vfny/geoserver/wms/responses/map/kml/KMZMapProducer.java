/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;


/**
 * Handles a GetMap request that spects a map in KMZ format.
 * 
 * KMZ files are a zipped KML file. The KML file must have an emcompasing <document> or <folder> element.
 * So if you have many different placemarks or ground overlays, they all need to be contained 
 * within one <document> element, then zipped up and sent off with the extension "kmz".
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $
 * @author $Author: Brent Owens
 */
class KMZMapProducer implements GetMapProducer {
    /** standard logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.kmz");

	/**
	 * encoder instance which does all the hard work
	 * 
	 * @uml.property name="kmlEncoder"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private EncodeKML kmlEncoder;

	/** used to get the content disposition file name */
	private WMSMapContext mapContext;
    
    
    /**
     * Request that encoding be halted if possible.
     *
     * @param gs The orriginating Service
     */
    public void abort(Service gs) {
        this.kmlEncoder.abort();
    }
    
    /**
     * The type for the format generated by this producer. 
     *
     * @return String containing official MIME type for this format.
     */
    public String getContentType() {
        return KMZMapProducerFactory.MIME_TYPE;
    }
   
    /**
     * aborts the encoding.
     */
    public void abort() {
        LOGGER.fine("aborting KMZ map response");
        
        if (this.kmlEncoder != null) {
        	LOGGER.info("aborting KMZ encoder");
            this.kmlEncoder.abort();
        }
    }
    
    /**
     * Initializes the KML encoder.
     * None of the map production is done here, it is done in writeTo().
     * This way the output can be streamed directly to the output response and
     * not written to disk first, then loaded in and then sent to the response.
     *
     * @param map WMSMapContext describing what layers, styles, area of interest
     * etc are to be used when producing the map.
     *
     * @throws WmsException thrown if anything goes wrong during the production.
     */
    public void produceMap(WMSMapContext map)
    throws WmsException {
    	this.mapContext = map;
    	kmlEncoder = new EncodeKML(map);
    }
    
    /**
     * Makes the map and sends it to the zipped output stream
     * The produceMap() method does not create the map in this case. We produce the
     * map here so we can stream directly to the response output stream, and
     * not have to write to disk, then send it to the stream.
     *
     * @Note: Do not close the output stream in this method, it gets closed
     * later on.
     *  
     * @param out OutputStream to stream the map to.
     *
     * @throws ServiceException
     * @throws IOException
     *
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
    	final ZipOutputStream outZ = new ZipOutputStream(out);
    	kmlEncoder.encodeKMZ(outZ);
    	outZ.finish();
        outZ.flush();
    }

	public String getContentDisposition() {
		if (this.mapContext.getLayer(0) != null) {
			try {
				String title = this.mapContext.getLayer(0).getFeatureSource().getSchema().getTypeName();
				if (title != null && !title.equals("")) {
					return "inline; filename=" + title + ".kmz";
				}
			} catch (NullPointerException e) {
			}
		}
		return "inline; filename=geoserver.kmz";
	}
}