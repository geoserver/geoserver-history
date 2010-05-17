/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.CapabilitiesHandler;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.WCSCapabilitiesRequest;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;

/**
 * reads a WCS GetCapabilities request from an XML stream
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id$
 */
public class CapabilitiesXmlReader extends XmlRequestReader {
	/**
	 * Constructs a new reader.
	 * 
	 * @param service
	 *            The WFS service handling the request.
	 */
	public CapabilitiesXmlReader(WCSInfo wcs) {
		super(wcs);
	}

	/**
	 * Reads the Capabilities XML request into a CapabilitiesRequest object.
	 * 
	 * @param reader
	 *            The plain POST text from the client.
	 * 
	 * @return The read CapabilitiesRequest object.
	 * 
	 * @throws WmsException
	 *             For any problems reading the request
	 */
	public Request read(Reader reader, HttpServletRequest req)
			throws WcsException {
		InputSource requestSource = new InputSource(reader);

		// instantiante parsers and content handlers
		CapabilitiesHandler currentRequest = new CapabilitiesHandler(
				new WCSCapabilitiesRequest((WCSInfo) getService()));

		// read in XML file and parse to content handler
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			ParserAdapter adapter = new ParserAdapter(parser.getParser());
			adapter.setContentHandler(currentRequest);
			adapter.parse(requestSource);
			LOGGER.fine("just parsed: " + requestSource);
		} catch (SAXException e) {
			throw new WcsException(e, "XML capabilities request parsing error",
					getClass().getName());
		} catch (IOException e) {
			throw new WcsException(e, "XML capabilities request input error",
					getClass().getName());
		} catch (ParserConfigurationException e) {
			throw new WcsException(e, "Some sort of issue creating parser",
					getClass().getName());
		}

		Request r = currentRequest.getRequest(req);

		if (r.getService() != null) {
			final String service = r.getService();

			if (!service.trim().toUpperCase().startsWith("WCS")) {
				throw new WcsException("SERVICE parameter is wrong.");
			}
		} else {
			throw new WcsException("SERVICE parameter is mandatory.");
		}

		if (r.getVersion() != null) {
			final String version = r.getVersion();

			if (!version.equals("1.0.0")) {
				throw new WcsException("VERSION parameter is wrong.");
			}
		} else {
			throw new WcsException("VERSION parameter is mandatory.");
		}

		/*
		 * if (r.getRequest() != null) { final String requestType =
		 * r.getRequest(); if (!requestType.equalsIgnoreCase("GetCapabilities")
		 * ) { throw new WcsException("REQUEST parameter is wrong."); } } else {
		 * throw new WcsException("REQUEST parameter is mandatory."); }
		 */
		return r;
	}
}
