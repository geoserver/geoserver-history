package org.geoserver.rest.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geoserver.proxy.ProxyConfig;
import org.geoserver.proxy.ProxyConfig.Mode;
import org.geoserver.rest.RestletException;
import org.geoserver.security.PropertyFileWatcher;
import org.restlet.Context;
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
 * @author Alan Gerber <agerber@openplans.org>
 */
public class ProxyRestlet extends Restlet {
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ProxyRestlet.class);

    private ProxyConfig config;
    private boolean watcherWorks;
    private PropertyFileWatcher configWatcher;

    /*
     * Initialize the proxy
     */
    public ProxyRestlet()
    {
        super();
        init();
    }
    
    /*
     * Initialize the proxy with context to call parent with
     */
    public ProxyRestlet(Context context)
    {
        super(context);
        init();
    }
    
    /*
     * Prepares the proxy's environment.
     */
    private void init()
    {
        try{
            configWatcher = new PropertyFileWatcher(ProxyConfig.getConfigFile());
            watcherWorks = true;
        }
        catch(Exception e){
            LOGGER.log(Level.WARNING, "Proxy could not create configuration watcher.  Proxy will not be able to update its configuration when it is modified.  Exception:", e);
            watcherWorks = false;
        }
    }
    
    @Override
    public void handle(Request request, Response response) {
        /* Check the proxy's config has been modified if the watcher was created correctly*/
        if (watcherWorks && configWatcher.isStale()) {
            config = ProxyConfig.loadConfFromDisk();
        }
        /* Grab the argument */
        Form f = request.getResourceRef().getQueryAsForm();
        /* The first argument should be the request for a URL to grab by proxy */
        String url = f.getFirstValue("url");
        try {
            URL resolved = new URL(url);
            /* Allow the request to go through if it's in the list of allowed URLs */
            if (this.checkURLPermission(resolved) == true) {
                final HttpURLConnection connection = (HttpURLConnection) resolved.openConnection();
                connection.setRequestMethod(request.getMethod().toString());

                if (request.getMethod().equals(Method.PUT)
                        || request.getMethod().equals(Method.POST)) {
                    connection.setDoOutput(true);
                    copyStream(request.getEntity().getStream(), connection.getOutputStream());
                }

                response.setEntity(new StreamRepresentation(new MediaType(connection
                        .getContentType())) {
                    @Override
                    public void write(OutputStream out) throws IOException {
                        copyStream(connection.getInputStream(), out);
                    }

                    @Override
                    public InputStream getStream() throws IOException {
                        throw new UnsupportedOperationException();
                    }
                });
            }
            /* Otherwise tell the client it can't do what it wanted to */
            else {
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

    /*
     * public void init(List<String> permissionRegexes){ //for }
     */

    /* Checks a URL against all regular expressions for permitted locations to connect */
    private boolean checkURLPermission(URL locator) {
        /* Check that the correct protocol is being used */
        if (locator.getProtocol().equals("http")) {
            boolean hostnameOk = false, mimetypeOk = false;
            /* Check if the hostname matches a whitelist if configured to do so */
            if (config.mode == Mode.HOSTNAME || config.mode == Mode.HOSTNAMEANDMIMETYPE
                    || config.mode == Mode.HOSTNAMEORMIMETYPE) {
                /* Iterate through the whitelist of hosts */
                for (Pattern pattern : config.hostnameWhitelist) {
                    /* Check if the regex matches the URL. */
                    Matcher matcher = pattern.matcher(locator.toString());
                    if (matcher.find()) {
                        /* Then the URL is allowed. */
                        hostnameOk = true;
                    }
                }
            }
            /* Check if the MIMEType matches a whitelist if configured to do so */
            if (config.mode == Mode.MIMETYPE || config.mode == Mode.HOSTNAMEANDMIMETYPE
                    || config.mode == Mode.HOSTNAMEORMIMETYPE) {
                // TODO: Actually check the MIMEType
                if (true) {
                    mimetypeOk = true;
                } else {
                    mimetypeOk = false;
                }
            }

            /* Return whether an action is permitted based on how proxy is configured */
            switch (config.mode) {
            case MIMETYPE:
                return mimetypeOk;
            case HOSTNAME:
                return hostnameOk;
            case HOSTNAMEANDMIMETYPE:
                return hostnameOk && mimetypeOk;
            case HOSTNAMEORMIMETYPE:
                return hostnameOk || mimetypeOk;
            }
        }
        /* The request is not permitted to go through. */
        return false;
    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buff = new byte[4096];
        int length = 0;
        while ((length = in.read(buff)) != -1) {
            out.write(buff, 0, length);
        }
    }
}
