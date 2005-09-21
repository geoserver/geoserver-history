/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vfny.geoserver.wfs.WfsException;


/**
 * Smart ByteArray to store XML values and offers convenient methods for
 * writing XML and file values to the stream.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 * @task TODO: remove this class since it is not used
 */
public class XmlOutputStream extends ByteArrayOutputStream {
    /** XML preamble encoding length */
    private static final int ENCODING_LENGTH = 40;

    /**
     * Empty constructor, calls super.
     */
    public XmlOutputStream() {
        super();
    }

    /**
     * Constructor with XML length set.
     *
     * @param length The column/XML tag name.
     */
    public XmlOutputStream(int length) {
        super(length);
    }

    /**
     * Writes XML output to XML final, stripping XML encoding preamble.
     *
     * @param xmlOut XML output to write to XML final.
     *
     * @throws WfsException For IOExceptions
     */
    public void writeToClean(OutputStream xmlOut) throws WfsException {
        try {
            xmlOut.write(this.toByteArray(), ENCODING_LENGTH,
                this.size() - ENCODING_LENGTH);
            this.reset();
        } catch (IOException e) {
            throw new WfsException(e, "IO problem",
                XmlOutputStream.class.getName());
        }
    }

    /**
     * Writes XML output directly, with no modification, to XML final.
     *
     * @param xmlOut XML output to write to XML final.
     *
     * @throws WfsException For IOExceptions
     */
    public void writeToNormal(OutputStream xmlOut) throws WfsException {
        try {
            xmlOut.write(this.toByteArray(), 0, this.size());
            this.reset();
        } catch (IOException e) {
            throw new WfsException(e, "IO problem",
                XmlOutputStream.class.getName());
        }
    }

    /**
     * Writes file contents directly to XML output stream.
     *
     * @param inputFileName File to write to XML stream.
     *
     * @throws WfsException For IOExceptions
     */
    public void writeFile(String inputFileName) throws WfsException {
        try {
            File inputFile = new File(inputFileName);
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] fileBuffer = new byte[20000];
            int bytesRead;

            while ((bytesRead = inputStream.read(fileBuffer)) != -1) {
                this.write(fileBuffer, 0, bytesRead);
            }

            inputStream.close();
        } catch (IOException e) {
            throw new WfsException(e,
                "Problems writing [ " + inputFileName + " ] file to XML",
                XmlOutputStream.class.getName());
        }
    }
}
