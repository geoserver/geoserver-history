/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.global.ServerConfig;
import org.vfny.geoserver.global.ServiceConfig;
import org.vfny.geoserver.requests.Request;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: CapabilitiesResponse.java,v 1.21.2.2 2003/12/30 23:08:26 dmzwiers Exp $
 */
public abstract class CapabilitiesResponse extends XMLFilterImpl
    implements Response, XMLReader {
    /** handler to do the processing */
    private ContentHandler contentHandler;

    private static OutputStream nullOutputStream = new OutputStream()
    {
      public void write(int b)throws IOException
      {
      }
      public void write(byte b[]) throws IOException
      {
      }
      public void write(byte b[], int off, int len) throws IOException
      {
      }
      public void flush() throws IOException
      {
      }
      public void close() throws IOException
      {
      }
    };

    /**
     * writes to a void output stream to throw any exception that can occur
     * in writeTo too.
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException {
      writeTo(nullOutputStream);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return ServerConfig.getInstance().getGlobalConfig().getMimeType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WfsException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            // don't know what this should be, or if its even important
            InputSource inputSource = new InputSource("XXX");
            SAXSource source = new SAXSource(this, inputSource);
            Charset charset = ServerConfig.getInstance().getGlobalConfig()
                                         .getCharSet();
            Writer writer = new OutputStreamWriter(out, charset);
            StreamResult result = new StreamResult(writer);

            transformer.transform(source, result);
        } catch (TransformerException ex) {
            throw new WfsException(ex);
        } catch (TransformerFactoryConfigurationError ex) {
            throw new WfsException(ex);
        }
    }

    /**
     * sets the content handler.
     *
     * @param handler DOCUMENT ME!
     */
    public void setContentHandler(ContentHandler handler) {
        contentHandler = handler;
    }

    /**
     * walks the given collection.
     *
     * @param systemId DOCUMENT ME!
     *
     * @throws java.io.IOException DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     */
    public void parse(String systemId) throws java.io.IOException, SAXException {
        walk();
    }

    /**
     * walks the given collection.
     *
     * @param input DOCUMENT ME!
     *
     * @throws java.io.IOException DOCUMENT ME!
     * @throws SAXException DOCUMENT ME!
     */
    public void parse(InputSource input)
        throws java.io.IOException, SAXException {
        walk();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void walk() throws SAXException {
        contentHandler.startDocument();

        ServiceConfig service = getServiceConfig();
        ResponseHandler handler = getResponseHandler(contentHandler);
        handler.handleDocument(service);
        handler.endDocument(service);
        contentHandler.endDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract ServiceConfig getServiceConfig();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract ResponseHandler getResponseHandler(
        ContentHandler contentHandler);
}
