package org.geoserver.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.geoserver.ows.util.RequestUtils;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.GeoServer;

/**
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL$
 */
public class ReverseProxyFilter implements Filter {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.filters");

    private static final String MIME_TYPES_INIT_PARAM = "mime-types";

    private final Set<Pattern> mimeTypePatterns = new HashSet<Pattern>();

    public void init(final FilterConfig filterConfig) throws ServletException {
        final String initParameter = filterConfig.getInitParameter(MIME_TYPES_INIT_PARAM);
        final String[] split = initParameter.split(",");

        LOGGER.finer("Initializing Reverse Proxy Filter");
        try {
            for (int i = 0; i < split.length; i++) {
                String mimeTypeRegExp = split[i];
                LOGGER.finest("Registering mime type regexp for reverse proxy filter: "
                        + mimeTypeRegExp);
                Pattern mimeTypePattern = Pattern.compile(mimeTypeRegExp);
                mimeTypePatterns.add(mimeTypePattern);
            }
        } catch (PatternSyntaxException e) {
            throw new ServletException("Error compiling Reverse Proxy Filter mime-types: "
                    + e.getMessage(), e);
        }
        LOGGER.finer("Reverse Proxy Filter configured");
    }

    /**
     * 
     */
    public void doFilter(final ServletRequest request,
            final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        final ServletContext servletContext = ((HttpServletRequest) request).getSession()
                .getServletContext();
        final GeoServer geoServer = (GeoServer) servletContext
                .getAttribute(GeoServer.WEB_CONTAINER_KEY);
        final String proxyBaseUrl = geoServer.getProxyBaseUrl();

        if (proxyBaseUrl == null || "".equals(proxyBaseUrl)) {
            chain.doFilter(request, response);
            return;
        }

        final CacheingResponseWrapper wrapper = new CacheingResponseWrapper(
                (HttpServletResponse) response, mimeTypePatterns);

        chain.doFilter(request, wrapper);

        wrapper.flushBuffer();

        if (wrapper.isCacheing()) {
            BufferedReader reader;
            {
                byte[] cachedContent = wrapper.getCachedContent();
                String cs = wrapper.getCharacterEncoding();
                reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                        cachedContent), cs));
            }
            PrintWriter writer = response.getWriter();
            // the request base url (eg, http://localhost:8080/)
            final String serverBase;
            // the proxy base url (eg, http://myproxyserver/)
            final String proxyBase;
            // the request context (eg, /geoserver/)
            final String context;
            // the proxy context (eg, /tools/geoserver/)
            final String proxyContext;
            {
                final String baseUrl = RequestUtils.baseURL((HttpServletRequest) request);
                final URL base = new URL(baseUrl);
                final URL proxy = new URL(proxyBaseUrl);

                serverBase = getServerBase(base);
                proxyBase = getServerBase(proxy);

                context = getContext(base);
                proxyContext = getContext(proxy);
            }

            String line;
            String translatedLine;
            LOGGER.info("translating " + ((HttpServletRequest)request).getRequestURI());
            while ((line = reader.readLine()) != null) {
                translatedLine = line.replaceAll(serverBase, proxyBase);
                translatedLine = translatedLine.replaceAll(context, proxyContext);
                if(LOGGER.isLoggable(Level.INFO)){
                    if(!line.equals(translatedLine)){
                        LOGGER.info("translated '" + line + "'");
                        LOGGER.info("        as '" + translatedLine + "'");
                    }
                }
                writer.println(translatedLine);
            }
            writer.flush();
        }
    }

    private String getContext(URL url) {
        String context = url.getPath();
        return context.endsWith("/") ? context : context + "/";
    }

    private String getServerBase(URL url) {
        StringBuffer sb = new StringBuffer();
        sb.append(url.getProtocol()).append("://");
        sb.append(url.getHost());
        if (url.getPort() != -1) {
            sb.append(":").append(url.getPort());
        }
        sb.append("/");
        return sb.toString();
    }

    public void destroy() {
    }

    /**
     * A servlet response wrapper that caches the content if its mime type matches one of the
     * provided patterns.
     * 
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.5.x
     * @source $URL$
     */
    private static class CacheingResponseWrapper extends HttpServletResponseWrapper {

        private Set<Pattern> cacheingMimeTypes;

        private boolean cacheContent;

        private ServletOutputStream outputStream;

        private PrintWriter writer;

        private ByteArrayOutputStream cache;

        public CacheingResponseWrapper(final HttpServletResponse response,
                                       Set<Pattern> cacheingMimeTypes) {
            super(response);
            this.cacheingMimeTypes = cacheingMimeTypes;
            // we can't know until setContentType is called
            this.cacheContent = false;
        }

        public boolean isCacheing() {
            return cacheContent;
        }

        public byte[] getCachedContent() {
            return cache.toByteArray();
        }

        /**
         * Among setting the response content type, determines whether the response content should
         * be cached or not, depending on the <code>mimeType</code> matching one of the patterns
         * or not.
         */
        @Override
        public void setContentType(final String mimeType) {
            Pattern p;
            for (Iterator<Pattern> it = cacheingMimeTypes.iterator(); it.hasNext();) {
                p = it.next();
                Matcher matcher = p.matcher(mimeType);
                if (matcher.matches()) {
                    cacheContent = true;
                    break;
                }
            }
            super.setContentType(mimeType);
        }

        @Override
        public void flushBuffer() throws IOException {
            if (cacheContent) {
                if (writer != null) {
                    writer.flush();
                }
                if (outputStream != null) {
                    outputStream.flush();
                }
            } else {
                super.flushBuffer();
            }
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (outputStream == null) {
                if (cacheContent) {
                    cache = new ByteArrayOutputStream();
                    outputStream = new ServletOutputStream() {
                        @Override
                        public void write(int b) throws IOException {
                            cache.write(b);
                        }
                    };
                } else {
                    outputStream = super.getOutputStream();
                }
            }
            return outputStream;
        }

        /**
         * The default behavior of this method is to return getWriter() on the wrapped response
         * object.
         */
        @Override
        public PrintWriter getWriter() throws IOException {
            if (writer == null) {
                if (cacheContent) {
                    String charset = super.getCharacterEncoding();
                    writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), charset));
                } else {
                    writer = super.getWriter();
                }
            }
            return writer;
        }
    }
}
