package org.geoserver.rest.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.rest.RestletException;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StreamRepresentation;

/**
 * The ProxyRestlet implements a simple REST service that forwards HTTP requests on to a third-party
 * webserver.
 * 
 * @author David Winslow <cdwinslow@opengeo.org>
 */
public class ProxyRestlet extends Restlet {
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ProxyRestlet.class);

    @Override
    public void handle(Request request, Response response) {
        Form f = request.getResourceRef().getQueryAsForm();
        String url = f.getFirstValue("url");
        try {
            URL resolved = new URL(url);
            if (resolved.getProtocol().equals("http")) {
                final HttpURLConnection connection = (HttpURLConnection) resolved.openConnection();
                connection.setRequestMethod(request.getMethod().toString());
                
                if (request.getMethod().equals(Method.PUT) ||
                        request.getMethod().equals(Method.POST)) {
                    connection.setDoOutput(true);
                    copyStream(request.getEntity().getStream(), connection.getOutputStream());
                }

                response.setEntity(new StreamRepresentation(new MediaType(connection.getContentType())) {
                    @Override
                    public void write(OutputStream out) throws IOException {
                        copyStream(connection.getInputStream(), out);    
                    }

                    @Override
                    public InputStream getStream() throws IOException {
                        throw new UnsupportedOperationException();
                    }
                });
            } else {
                throw new RestletException("Proxy only accepts HTTP requests",
                        Status.CLIENT_ERROR_BAD_REQUEST);
            }
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Invalid proxy request. ", e);
            throw new RestletException("Invalid proxy request", Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Couldn't connect to proxied service", e);
            throw new RestletException("Couldn't connect to proxied service",
                    Status.SERVER_ERROR_BAD_GATEWAY);
        }
    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[4096];
        int length = 0;
        while ((length = in.read(buff)) != -1) {
            out.write(buff, 0, length);
        }
    }
}
