/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.vfny.geoserver.global.GlobalConfig;
import org.vfny.geoserver.global.ServerConfig;
import org.vfny.geoserver.responses.ResponseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Java representation of a WFS_TransactionResponse xml element. The status and
 * handle are required, so they are in the constructor, and a locator, a
 * message, and any number of InsertResults can be added.  This object can
 * then write itself out to xml for a response.
 *
 * @author Chris Holmes
 * @version $Id: WfsTransResponse.java,v 1.2.2.3 2003/12/31 23:36:54 dmzwiers Exp $
 */
public class WfsTransResponse {
    /** Standard logging instance for class */
    private static final Logger LOG = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** Status if the transaction was successful */
    public static final short SUCCESS = 0;

    /** Status if one of more operations failed */
    public static final short FAILED = 1;

    /**
     * Status for transaction partially succeeding, data in incosistent state.
     */
    public static final short PARTIAL = 2;

    /** The name of the root element of the xml document */
    public static final String ROOT = "TransactionResponse";

    /** The name of the insert result element of the xml document */
    public static final String INSERT_RESULT = "InsertResult";

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
    public static final String V_OFFSET = "   ";
    private boolean verbose = ServerConfig.getInstance().getGlobalConfig()
                                          .isVerbose();
    private String indent = (verbose) ? ("\n" + V_OFFSET) : " ";
    private String offset = (verbose) ? V_OFFSET : "";

    /** Status of the transaction represented by this response. */
    public final short status;

    /** Handle of the transaction request */
    private String handle;

    /**
     * Optional element, Used in the case of an error, to figure out which
     * transaction failed.
     */
    private String locator = null;

    /** Message element used to report any error messages */
    private String message = null;

    /** Holds the feature identifiers of newly created features from an insert */
    private List insertResults;

    /**
     * Only constructor, as status is mandatory
     *
     * @param status The status of the transaction.
     */
    public WfsTransResponse(short status) {
        this.status = status;
    }

    /**
     * Convenience constructor, for status and handle
     *
     * @param status The status of the transaction.
     * @param handle the handle of the response.  Should be the same as the
     *        handle of the transaction request.
     */
    public WfsTransResponse(short status, String handle) {
        this.status = status;
        this.handle = handle;
    }

    /**
     * Sets the handle for this response.
     *
     * @param handle the handle of the response.  Should be the same as the
     *        handle of the transaction request.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Sets the string to indicate which part of the transaction failed. Should
     * be the handle of the sub-request that failed.
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
     * @param featureIds the collection of successfully added feature ids.
     */
    public void addInsertResult(String handle, Collection featureIds) {
        if (insertResults == null) {
            insertResults = new ArrayList();
        }

        insertResults.add(new InsertResult(handle, featureIds));
    }

    /**
     * Generates the xml represented by this object.
     *
     * @param writer DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeXmlResponse(Writer writer) throws IOException {
        //boolean verbose = ConfigInfo.getInstance().formatOutput();
        //String indent = ((verbose) ? "\n" + OFFSET : " ");
        String xmlHeader = ServerConfig.getInstance().getXmlHeader();

        if (verbose) {
            writer.write("\n");
        }

        writer.write("<wfs:WFS_TransactionResponse");
        writer.write(indent + "version=\"" + CUR_VERSION + "\"");
        writer.write(indent + "xmlns:wfs=\"http://www.opengis.net/wfs\"");

        //if (insertResults.size() > 0){
        writer.write(indent + "xmlns:ogc=\"http://www.opengis.net/ogc\"");

        //}
        writer.write(indent
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        writer.write(indent);
        writer.write("xsi:schemaLocation=\"http://www.opengis.net/wfs ");
        writer.write(GlobalConfig.getInstance().getSchemaBaseUrl());
        writer.write("wfs/1.0.0/WFSConfig-transaction.xsd\">");

        //  + " http://schemas.opengis.net/wfs/1.0.0/WFSConfig-transaction.xsd\">");
        if (insertResults != null) {
            Iterator iter = insertResults.iterator();

            while (iter.hasNext()) {
                ((InsertResult) iter.next()).writeXml(writer);
            }
        }

        writer.write(indent + "<wfs:TransactionResult");

        if (handle != null) {
            writer.write(" handle=\"" + handle + "\"");
        }

        writer.write(">");
        writer.write(indent + offset + "<wfs:Status>");
        writer.write(indent + offset + offset);

        switch (status) {
        case SUCCESS:
            writer.write("<wfs:SUCCESS/>");

            break;

        case PARTIAL:
            writer.write("<wfs:PARTIAL/>");

            break;

        case FAILED:default:
            writer.write("<wfs:FAILED/>");

            break;
        }

        writer.write(indent + offset + "</wfs:Status>");

        if (locator != null) {
            writer.write(indent + offset + "<wfs:Locator>");
            writer.write(locator + "</wfs:Locator>");
        }

        if (message != null) {
            writer.write(indent + offset + "<wfs:Message>");
            ResponseUtils.writeEscapedString(writer, message);
            writer.write("</wfs:Message>");
        }

        writer.write(indent + "</wfs:TransactionResult>");

        if (verbose) {
            writer.write("\n");
        }

        writer.write("</wfs:WFS_TransactionResponse>");
    }

    // this may not be needed anymore
    public String getXmlResponse() {
        StringWriter writer = new StringWriter();

        try {
            writeXmlResponse(writer);
        } catch (IOException e) {
            return null;
        }

        return writer.toString();
    }

    /**
     * Helper to determine if a string is not null and not an empty string.
     *
     * @param s the string to test
     *
     * @return true if the string is not null and not an empty string.
     */
    private boolean isEmpty(String s) {
        return ((s == null) || s.equals(""));
    }

    /**
     * Private class to reprent an InsertResult xml element.
     */
    private class InsertResult {
        /** Handle of the insert statement that generated this result. */
        private String handle;

        /** List of the ids of the features added by the insert statement. */
        private Collection featureIds;

        /**
         * Constructor.
         *
         * @param handle The handle for the insert operation.
         * @param featureIds The fids that were added by the insert operation.
         */
        public InsertResult(String handle, Collection featureIds) {
            this.handle = handle;
            this.featureIds = featureIds;
        }

        public void writeXml(Writer writer) throws IOException {
            writer.write(indent);
            writer.write("<wfs:InsertResult");

            if (handle != null) {
                writer.write(" handle=\"" + handle + "\"");
            }

            writer.write(">");

            Iterator iter = featureIds.iterator();

            while (iter.hasNext()) {
                writer.write(indent + offset + "<ogc:FeatureId fid=\"");
                writer.write(iter.next() + "\"/>");
            }

            writer.write(indent + "</wfs:InsertResult>");
        }

        public void getDOM(Element root, Document doc) {
            Element insResultElem = doc.createElement(INSERT_RESULT);

            if (!isEmpty(handle)) {
                insResultElem.setAttribute(HANDLE, handle);
            }

            root.appendChild(insResultElem);

            if (featureIds != null) {
                LOG.finest("there is a list of feature ids in insertRes");

                Iterator iter = featureIds.iterator();

                while (iter.hasNext()) {
                    Element fid = doc.createElement("FeatureId");
                    fid.setAttribute("fid", iter.next().toString());
                    LOG.finest("adding fid " + fid);
                    insResultElem.appendChild(fid);
                }
            }
        }
    }
}
