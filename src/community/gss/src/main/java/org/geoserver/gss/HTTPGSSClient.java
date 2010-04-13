/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import net.opengis.ows10.ExceptionReportType;
import net.opengis.ows10.ExceptionType;
import net.opengis.wfs.TransactionType;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geoserver.gss.CentralRevisionsType.LayerRevision;
import org.geoserver.gss.xml.GSSConfiguration;
import org.geotools.xml.Parser;

/**
 * A client that connects to a single host. The client is not supposed to be thread safe, use
 * multiple instances if necessary
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public class HTTPGSSClient implements GSSClient {

    HttpClient client;

    URL address;

    String username;

    String password;

    GSSConfiguration configuration;

    public HTTPGSSClient(HttpClient client, GSSConfiguration configuration, URL gssServiceURL,
            String username, String password) {
        if (gssServiceURL.getPath().endsWith("/")) {
            try {
                String external = gssServiceURL.toExternalForm();
                this.address = new URL(external.substring(0, external.length() - 1));
            } catch (MalformedURLException e) {
                throw new RuntimeException("Unexpected error normalizing GSS url" + e);
            }
        } else {
            this.address = gssServiceURL;
        }
        this.configuration = configuration;
        this.client = client;
        this.username = username;
        this.password = password;
    }

    public long getCentralRevision(String layerName) throws IOException {
        // grab the response and parse it to an object representation
        Object response;
        GetMethod method = new GetMethod(address
                + "?service=GSS&version=1.0.0&request=GetCentralRevision&typeName=" + layerName);
        try {
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                throw new IOException("HTTP client returned with code " + statusCode);
            }

            Parser parser = new Parser(configuration);
            parser.setStrict(true);
            parser.setFailOnValidationError(true);
            response = parser.parse(method.getResponseBodyAsStream());
        } catch (Exception e) {
            throw (IOException) new IOException("Error occurred while asking unit about "
                    + "the last central revision for layer " + layerName).initCause(e);
        } finally {
            method.releaseConnection();
        }

        // interpret the parsed response, if any
        if (response instanceof CentralRevisionsType) {
            CentralRevisionsType cr = (CentralRevisionsType) response;
            for (LayerRevision lr : cr.getLayerRevisions()) {
                String qname = lr.getTypeName().getPrefix() + ":" + lr.getTypeName().getLocalPart();
                if (qname.equals(layerName)) {
                    return lr.getCentralRevision();
                }
            }
            throw new IOException("Response to GetCentralRevision received, but it "
                    + "did not contain a central revision for " + layerName);
        } else if (response instanceof ExceptionReportType) {
            throw convertServiceException("The Unit service reported a failure: ",
                    (ExceptionReportType) response);
        } else {
            if (response == null) {
                throw new IOException("The response was parsed to a null object");
            }
            throw new IOException("The response was parsed to an unrecognized object type: "
                    + response.getClass());
        }
    }

    IOException convertServiceException(String intro, ExceptionReportType response) {
        StringBuilder sb = new StringBuilder(intro);
        for (Iterator it = response.getException().iterator(); it.hasNext();) {
            ExceptionType et = (ExceptionType) it.next();
            for (Iterator eit = et.getExceptionText().iterator(); eit.hasNext();) {
                String text = (String) eit.next();
                sb.append(text);
                if (eit.hasNext())
                    sb.append(". ");
            }

        }

        return new IOException(sb.toString());
    }

    public TransactionType getDiff(String layerName, long fromVersion) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void postDiff(String layerName, long fromVersion, long toVersion, TransactionType changes)
            throws IOException {
        throw new UnsupportedOperationException();
    }

}
