/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLHandlerFeature;
import org.geotools.gml.GMLFilterFeature;
import org.geotools.filter.FilterFilter;
import org.geotools.feature.FeatureType;
import com.vividsolutions.jts.geom.Geometry;
import org.vfny.geoserver.responses.WfsException;

/**
 * Uses SAX to extact a Transactional request from and incoming XML stream.
 *
 * @version $VERSION$
 * @author Chris Holmes, TOPP
 */
public class TransactionFeatureHandler
    extends GMLFilterFeature {
    //    implements ContentHandler, FilterHandler, GMLHandlerFeature {

        /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    private boolean inFilter = false;

    private TransactionFilterHandler parent;
      /**
     * Constructor with parent, which must implement GMLHandlerJTS.
     *
     * @param parent The parent of this filter.
     */
    public TransactionFeatureHandler (TransactionFilterHandler parent) {
        super(parent);
	this.parent = parent;
    }

      /**
     * Checks for GML element start and - if not a coordinates element - sends
     * it directly on down the chain to the appropriate parent handler.  If it
     * is a coordinates (or coord) element, it uses internal methods to set the
     * current state of the coordinates reader appropriately. 
     *
     * @param namespaceURI The namespace of the element.
     * @param localName The local name of the element.
     * @param qName The full name of the element, including namespace prefix.
     * @param atts The element attributes.
     * @throws SAXException Some parsing error occured while reading
     * coordinates.
     */
    public void startElement(String namespaceURI, String localName, 
                             String qName, Attributes atts)
        throws SAXException {
	LOGGER.finer("starting element " + localName + " in transFeat");
	if( localName.endsWith("Member") ) {
	    LOGGER.finer("should start a transactionFeature");
	    //if(localName.equals("Filter")) {
            inFilter = true;
        }

	super.startElement(namespaceURI, localName, qName, atts);
    }

     /**
     * Checks for GML element end and - if not a coordinates element - sends it
     * directly on down the chain to the appropriate parent handler.  If it is
     * a coordinates (or coord) element, it uses internal methods to set the 
     * current state of the coordinates reader appropriately.
     *
     * @param namespaceURI Namespace of the element.
     * @param localName Local name of the element.
     * @param qName Full name of the element, including namespace prefix.
     * @throws SAXException Parsing error occurred while reading coordinates.
     */
    public void endElement(String namespaceURI, String localName, String qName)
				throws SAXException {
	if ( localName.endsWith("Member") ) {
	    //if(localName.equals("Filter")) {
            inFilter = false;
        LOGGER.fine("finished a filter with transactionfilter");
	}
	super.endElement(namespaceURI, localName, qName);
    }

    /**
     * Manages the start of a new main or sub geometry.  This method looks at
     * the status of the current handler and either returns a new sub-handler
     * (if the last one was successfully returned already) or passes the
     * element start notification along to the current handler as a sub
     * geometry notice.
     *
     * @param geometry The geometry from the child.
     */
    public void geometry(Geometry geometry) {
	if (inFilter) {
	    LOGGER.finer("calling gmlFeatureFilter for geom " + geometry);
	    super.geometry(geometry);
	} else {
	    LOGGER.finer("passing geometry to parent " + geometry);
	    parent.geometry(geometry);
	}
    }
}
