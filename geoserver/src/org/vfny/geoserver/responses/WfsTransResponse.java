/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
// Imported JAVA API for XML Parsing 1.0 classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.Serializer;
import  org.apache.xml.serialize.SerializerFactory;
import  org.apache.xml.serialize.XMLSerializer;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.AttributeType;
import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.postgis.PostgisConnection;
import org.geotools.data.postgis.PostgisDataSource;
import org.vfny.geoserver.requests.TransactionRequest;
import org.vfny.geoserver.requests.SubTransactionRequest;
import org.vfny.geoserver.requests.DeleteRequest;
import org.vfny.geoserver.requests.UpdateRequest;
import org.vfny.geoserver.requests.InsertRequest;
import org.vfny.geoserver.config.TypeInfo;
import org.vfny.geoserver.config.TypeRepository;


/**
 * Java representation of a WFS_TransactionResponse xml element.
 * The status and handle are required, so they are in the constructor,
 * and a locator, a message, and any number of InsertResults can be
 * added.  This object can then write itself out to xml for a response.
 *
 *@author Chris Holmes
 *@version $VERSION$
 */
public class WfsTransResponse {

    /** Standard logging instance for class */
    private static final Logger LOG = 
        Logger.getLogger("org.vfny.geoserver.responses");
    /** Status if the transaction was successful  */
    public static final short SUCCESS = 0;
    
    /** Status if one of more operations failed */
    public static final short FAILED = 1;
    
    /** Status for transaction partially succeeding, data
	in incosistent state.*/
    public static final short PARTIAL = 2;

    /** The name of the root element of the xml document */
    public static final String ROOT = "TransactionResponse";

    /** Name of the transaction result element of the xml document */
    public static final String TRANS_RESULT = "TransactionResult";

    /** Name of the version element of the xml document */
    public static final String VERSION = "version";
    
    /** Current version of wfs */
    public static final String CUR_VERSION = "1.0.0";

     /** Name of the version element of the xml document */
    public static final String HANDLE = "handle";

    /** Name of the status element of the xml document */
    public static final String STATUS = "Status";

    /** Status of the transaction represented by this response.*/
    private short status;

    /** Handle of the transaction request */
    private String handle;

    /** Optional element, Used in the case of an error, to figure out which
	transaction failed.*/
    private String locator = null;

    /** Message element used to report any error messages */
    private String message = null;

    /** Holds the feature identifiers of newly created features from
	an insert*/
    private List insertResults;

    /** Only constructor, as status and handle are mandatory
     *
     * @param status The status of the transaction.
     * @param handle the handle of the response.  Should be the
     * same as the handle of the transaction request.
     */ 
    public WfsTransResponse(short status, String handle) {
	this.status = status;
	this.handle = handle;
    }

    /** 
     *Sets the string to indicate which part of the transaction failed.
     * Should be the handle of the sub-request that failed. 
     *
     * @param locator the handle of the failed transaction.
     */
    public void setLocator(String locator) {
	this.locator = locator;
    }

     /** 
     * Sets the string to give a message about a failure.
     *
     * @param message to give the user information about the failure.
     */
    public void setMessage(String message) {
	this.message = message;
    }

    /**
     * adds an insert result for a successful insert operation.
     *
     * @param handle the handle of the insert request.
     * @param featureIds the list of successfully added feature ids.
     */
    public void addInsertResult(String handle, List featureIds) {
	if (insertResults == null) {
	    insertResults = new ArrayList();
	}
	insertResults.add(new InsertResult(handle, featureIds));
	
    }


    /**
     * Generates the xml represented by this object.
     */
    public String getXmlResponse() {
	String retString;
	Document doc = null;
	try {
	    DocumentBuilderFactory dfactory = 
		DocumentBuilderFactory.newInstance();
	    dfactory.setNamespaceAware(true);
	    doc = dfactory.newDocumentBuilder().newDocument();		
	    //retval = new org.apache.xerces.dom.DocumentImpl();
	    Element root = doc.createElement(ROOT);
	    doc.appendChild( root );
	    root.setAttribute(VERSION, CUR_VERSION);
	    //TODO: insert results
	    Element transResult = doc.createElement(TRANS_RESULT);
	    transResult.setAttribute(HANDLE, handle);
	    root.appendChild(transResult);
	    Element statusElem = doc.createElement(STATUS);
	    transResult.appendChild(statusElem);
	    Element result = null;
	    switch (status) {
	    case SUCCESS: 
		result = doc.createElement("SUCCESS");
		break;
	    case FAILED:
		result = doc.createElement("FAILED");
		break;
	    case PARTIAL:
		result = doc.createElement("PARTIAL");
		break;
	    }
		statusElem.appendChild(result);
	    addTextElement(doc, transResult, "Locator", locator);
	    addTextElement(doc, transResult, "Message", message);

    
	    StringWriter sw = new StringWriter();
	    OutputFormat format  = new OutputFormat(doc );
            XMLSerializer serial = new XMLSerializer( sw, format );
            serial.asDOMSerializer();
            serial.serialize( root );
	    retString = sw.toString();
	} catch (IOException ioe) {
	    LOG.warning("io exception " + ioe);
	    retString =  "Io exception " + ioe;
	} catch (javax.xml.parsers.ParserConfigurationException e) {
	    LOG.warning("Problem configuring parser, make sure one is in " + 
			"the classpath" + e);
	    retString = "couldn't find xml parser: " + e.getMessage();
	}
	return retString;
    }

    /**
     * Helper method to add an element with a child text node.
     * 
     * @param d the Document to be used to create the element.
     * @param parent the element to serve as parent to this new element.
     * @param elem_name the name of this new element.
     * @param value the text that this new element will hold.
     */
     private void addTextElement(Document d, Element parent, 
				String elem_name, String value) {
	if ( value != null && !value.equals(""))
	{
	    Element new_element = d.createElement(elem_name);
	    parent.appendChild(new_element);
	    new_element.appendChild(d.createTextNode(value.toString()));
	}
    }

    /**
     * Private class to reprent an InsertResult xml element.
     */
    private class InsertResult{

	/** Handle of the insert statement that generated this result. */
	private String handle;
	
	/** List of the ids of the features added by the insert statement.*/
	private List featureIds;

	/** Constructor. */
	public InsertResult(String handle, List featureIds){
	    this.handle = handle;
	    this.featureIds = featureIds;
	}
    }
}
