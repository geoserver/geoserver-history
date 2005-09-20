/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.vfny.geoserver.global.ConfigurationException;


/**
 * WriterUtils purpose.
 * 
 * <p>
 * Used to provide assitance writing xml to a Writer.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WriterHelper.java,v 1.9 2004/02/09 23:29:49 dmzwiers Exp $
 */
public class WriterHelper {
    /** Used internally to create log information to detect errors. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.global");

    /** The output writer. */
    protected Writer writer;

    /**
     * WriterUtils constructor.
     * 
     * <p>
     * Should never be called.
     * </p>
     */
    protected WriterHelper() {
    }

    /**
     * WriterUtils constructor.
     * 
     * <p>
     * Stores the specified writer to use for output.
     * </p>
     *
     * @param writer the writer which will be used for outputing the xml.
     */
    public WriterHelper(Writer writer) {
        LOGGER.fine("In constructor WriterHelper");
        this.writer = writer;
    }

    /**
     * write purpose.
     * 
     * <p>
     * Writes the String specified to the stored output writer.
     * </p>
     *
     * @param s The String to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void write(String s) throws ConfigurationException {
        try {
            writer.write(s);
            writer.flush();
        } catch (IOException e) {
            throw new ConfigurationException("Write" + writer, e);
        }
    }

    /**
     * writeln purpose.
     * 
     * <p>
     * Writes the String specified to the stored output writer.
     * </p>
     *
     * @param s The String to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void writeln(String s) throws ConfigurationException {
        try {
            writer.write(s + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new ConfigurationException("Writeln" + writer, e);
        }
    }

    /**
     * openTag purpose.
     * 
     * <p>
     * Writes an open xml tag with the name specified to the stored output
     * writer.
     * </p>
     *
     * @param tagName The tag name to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void openTag(String tagName) throws ConfigurationException {
        writeln("<" + tagName + ">");
    }

    /**
     * openTag purpose.
     * 
     * <p>
     * Writes an open xml tag with the name and attributes specified to the
     * stored output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param attributes The tag attributes to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void openTag(String tagName, Map attributes)
        throws ConfigurationException {
        write("<" + tagName + " ");

        Iterator i = attributes.keySet().iterator();

        while (i.hasNext()) {
            String s = (String) i.next();

            if (attributes.get(s) != null) {
                write(s + " = " + "\"" + (attributes.get(s)).toString() + "\" ");
            }
        }

        writeln(">");
    }

    /**
     * closeTag purpose.
     * 
     * <p>
     * Writes an close xml tag with the name specified to the stored output
     * writer.
     * </p>
     *
     * @param tagName The tag name to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void closeTag(String tagName) throws ConfigurationException {
        writeln("</" + tagName + ">");
    }

    /**
     * textTag purpose.
     * 
     * <p>
     * Writes a text xml tag with the name and text specified to the stored
     * output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param data The text data to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void textTag(String tagName, String data)
        throws ConfigurationException {
        writeln("<" + tagName + ">" + data + "</" + tagName + ">");
    }

    /**
     * valueTag purpose.
     * 
     * <p>
     * Writes an xml tag with the name and value specified to the stored output
     * writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param value The text data to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void valueTag(String tagName, String value)
        throws ConfigurationException {
        writeln("<" + tagName + " value = \"" + value + "\" />");
    }

    /**
     * attrTag purpose.
     * 
     * <p>
     * Writes an xml tag with the name and attributes specified to the stored
     * output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param attributes The tag attributes to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void attrTag(String tagName, Map attributes)
        throws ConfigurationException {
        write("<" + tagName + " ");

        Iterator i = attributes.keySet().iterator();

        while (i.hasNext()) {
            String s = (String) i.next();

            if (attributes.get(s) != null) {
                write(s + " = " + "\"" + (attributes.get(s)).toString() + "\" ");
            }
        }

        write(" />\n");
    }

    /**
     * textTag purpose.
     * 
     * <p>
     * Writes an xml tag with the name, text and attributes specified to the
     * stored output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param attributes The tag attributes to write.
     * @param data The tag text to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void textTag(String tagName, Map attributes, String data)
        throws ConfigurationException {
        write("<" + tagName + " ");

        Iterator i = attributes.keySet().iterator();

        while (i.hasNext()) {
            String s = (String) i.next();

            if (attributes.get(s) != null) {
                write(s + " = " + "\"" + (attributes.get(s)).toString() + "\" ");
            }
        }

        write(">" + data + "</" + tagName + ">");
    }

    /**
     * comment purpose.
     * 
     * <p>
     * Writes an xml comment with the text specified to the stored output
     * writer.
     * </p>
     *
     * @param comment The comment text to write.
     *
     * @throws ConfigurationException When an IO exception occurs.
     */
    public void comment(String comment) throws ConfigurationException {
        writeln("<!--");
        writeln(comment);
        writeln("-->");
    }
}
