/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: XmlResponseHandler.java,v 1.2.2.6 2004/01/06 23:03:13 dmzwiers Exp $
 */
public abstract class XmlResponseHandler implements ResponseHandler {
    /** blank attributes to be used when none are needed. */
    protected static final Attributes atts = new AttributesImpl();
    //protected static GeoServer server = GeoServer.getInstance();
    private static final int TAB_SIZE = 2;
    private char[] cr = new char[0];
    private char[] tab = new char[0];
    private int indentLevel = 0;

    /** DOCUMENT ME! */
    private ContentHandler contentHandler;

    /**
     * Creates a new XmlResponseHandler object.
     *
     * @param contentHandler DOCUMENT ME!
     */
    public XmlResponseHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void startElement(String name) throws SAXException {
        startElement("", name, name, atts);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void startElement(String name, Attributes attributes)
        throws SAXException {
        startElement("", name, name, attributes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ns DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param qName DOCUMENT ME!
     * @param attributes DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void startElement(String ns, String name, String qName,
        Attributes attributes) throws SAXException {
        contentHandler.startElement(ns, name, qName, attributes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void endElement(String name) throws SAXException {
        endElement("", name, name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ns DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param qName DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void endElement(String ns, String name, String qName)
        throws SAXException {
        contentHandler.endElement(ns, name, qName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param elementText DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleSingleElem(String name, String elementText)
        throws SAXException {
        startElement(name);
        characters(elementText);
        endElement(name);
    }

    /**
     * Convenience method for handleSingleElem(name, "").  Just makes a single
     * elem named 'name'
     *
     * @param name DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleSingleElem(String name) throws SAXException {
        handleSingleElem(name, "");
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void characters(String s) throws SAXException {
        if (s != null) {
            char[] chars = s.toCharArray();
            contentHandler.characters(chars, 0, chars.length);
        }
    }

    /**
     * implies a carriege return and the increment of the indent level
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void indent() throws SAXException {
        ++indentLevel;
        cReturn();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void unIndent() throws SAXException {
        --indentLevel;
        cReturn();
    }

    /**
     * Handles a tab call - does nothing.
     *
     * @param level DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void indent(int level) throws SAXException {
        while (level > 0) {
            contentHandler.ignorableWhitespace(tab, 0, TAB_SIZE);
            --level;
        }
    }

    /**
     * Handles a carraige return call - does nothing.
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void cReturn() throws SAXException {
        contentHandler.characters(cr, 0, cr.length);
        indent(indentLevel);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newLines DOCUMENT ME!
     * @param indent DOCUMENT ME!
     */
    public void setPrettyPrint(boolean newLines, boolean indent) {
        if (newLines) {
            cr = new char[1];
            cr[0] = '\n';
        } else {
            cr = new char[0];
        }

        if (indent) {
            tab = new char[TAB_SIZE];
            java.util.Arrays.fill(tab, 0, TAB_SIZE, ' ');
        } else {
            tab = new char[0];
        }
    }
}
