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
 * Abstract class that implements the ResponseHandler, generating the 
 * appropriate capabilities document for the service passed in.  This
 * class simply adds a number of convenience methods for the sub classes
 * that actually implement the ResponseHandler interface to make use 
 * of.  It translatest the conveinence requests into methods on the
 * SAX ContentHandler that is passed in the constructor.
 *
 * @author Gabriel Roldán
 * @version $Id: XmlResponseHandler.java,v 1.9 2004/09/05 17:17:58 cholmesny Exp $
 */
public abstract class XmlResponseHandler implements ResponseHandler {
    /** blank attributes to be used when none are needed. */
    protected static final Attributes atts = new AttributesImpl();
    private static final int TAB_SIZE = 2;
    private char[] cr = new char[0];
    private char[] tab = new char[0];
    private int indentLevel = 0;
    private boolean isPrettyPrinting = false;

    /** The actual SAX handler that recieves the production calls. */
    private ContentHandler contentHandler;

    /**
     * Creates a new XmlResponseHandler object.  Sets the contentHandler
     * to pass calls to and initializes the indenting and character returns.
     *
     * @param contentHandler DOCUMENT ME!
     */
    public XmlResponseHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
        cr = new char[1];
        cr[0] = '\n';
        tab = new char[TAB_SIZE];
        java.util.Arrays.fill(tab, 0, TAB_SIZE, ' ');
    }

    /**
     * Convenience method to start an element with the given name and
     * no attributes.
     *
     * @param name The name of the element to start.
     *
     * @throws SAXException For any errors.
     */
    protected void startElement(String name) throws SAXException {
        startElement("", name, name, atts);
    }

    /**
     * Covenience method to simply specify the name and attributes
     *
     * @param name The name of the element to start.
     * @param attributes The attributes of the element.
     *
     * @throws SAXException For any errors.
     */
    protected void startElement(String name, Attributes attributes)
        throws SAXException {
        startElement("", name, name, attributes);
    }

    /**
     * Full startElement, simply passes each value to the content handler
     * that is recieving production calls.  
     *
     * @param ns The namespace of the element.
     * @param name The name of the element to start.
     * @param qName The qualified name of the element.
     * @param attributes The attributes to include with the element.
     *
     * @throws SAXException If anything goes wrong.
     */
    protected void startElement(String ns, String name, String qName,
        Attributes attributes) throws SAXException {
        contentHandler.startElement(ns, name, qName, attributes);
    }

    /**
     * Convenience method to end an element, passes the name as the 
     * name and qname, no namespace. 
     *
     * @param name The name of the element to end.
     *
     * @throws SAXException For any error.
     */
    protected void endElement(String name) throws SAXException {
        endElement("", name, name);
    }

    /**
     * Full end element.
     *
     * @param ns The namespace of the element to end.
     * @param name The local name of the element to end.
     * @param qName The qualified name of the element to end.
     *
     * @throws SAXException For any errors.
     */
    protected void endElement(String ns, String name, String qName)
        throws SAXException {
        contentHandler.endElement(ns, name, qName);
    }

    /**
     * Convenience method to handles a single element, using the name and 
     * elementText passed in, with no qualified name or namespace.
     *
     * @param name The name of the element.
     * @param elementText The text this element should have.
     *
     * @throws SAXException For any errors.
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
     * @param name The name of the element to make.
     *
     * @throws SAXException For any errors.
     */
    protected void handleSingleElem(String name) throws SAXException {
        handleSingleElem(name, "");
    }

    /**
     * Convenience method to turn the string into its constituent characters
     * and passes them to the content handler as an array.
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
     * Implies a carriege return and the increment of the indent level.
     *
     * @throws SAXException For any error.
     */
    protected void indent() throws SAXException {
            ++indentLevel;
            cReturn();
    }

    /**
     * Implies a carraige return and the decrement of the indent level.
     *
     * @throws SAXException For any error.
     */
    protected void unIndent() throws SAXException {
           --indentLevel;
           if(indentLevel<0)
           	 indentLevel = 0;
           cReturn();
    }

    /**
     * Handles a tab call, passes the appropriate number of tabs
     * as ignorable whitespace to the contentHandler.
     *
     * @param level Number of tabs to indent.
     *
     * @throws SAXException For any errors.
     */
    protected void indent(int level) throws SAXException {
            while (level > 0) {
                contentHandler.ignorableWhitespace(tab, 0, tab.length);
                --level;
            }
    }

    /**
     * Handles a carraige return call, passes the carriage return and indents
     * if prettyPrinting is set to true.
     *
     * @throws SAXException For any problems.
     */
    protected void cReturn() throws SAXException {
	contentHandler.characters(cr, 0, cr.length);
        if (isPrettyPrinting) {
            indent(indentLevel);
	}
    }

    /**
     * Sets this handler to do pretty printing, with nice newlines and indents.
     *
     * @param isPrettyPrinting <tt>true</tt> if the output should look nice.
     */
    public void setPrettyPrint(boolean isPrettyPrinting) {
        if (isPrettyPrinting) {
	    cr[0] = '\n';
	} else {
	    cr[0] = ' ';
	}
	this.isPrettyPrinting = isPrettyPrinting;
    }
}
