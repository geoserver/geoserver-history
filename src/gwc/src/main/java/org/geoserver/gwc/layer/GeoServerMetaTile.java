package org.geoserver.gwc.layer;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geoserver.ows.Response;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.WebMap;
import org.geoserver.wms.map.RenderedImageMap;
import org.geoserver.wms.map.RenderedImageMapResponse;
import org.geowebcache.grid.GridSubset;
import org.geowebcache.io.Resource;
import org.geowebcache.layer.MetaTile;
import org.geowebcache.mime.FormatModifier;
import org.geowebcache.mime.MimeType;

public class GeoServerMetaTile extends MetaTile {

    private static Map<String, Response> cachedTileEncoders = new HashMap<String, Response>();

    private WebMap webMap;

    public GeoServerMetaTile(GridSubset gridSubset, MimeType responseFormat,
            FormatModifier formatModifier, long[] tileGridPosition, int metaX, int metaY,
            Integer gutter) {
        super(gridSubset, responseFormat, formatModifier, tileGridPosition, metaX, metaY, gutter);
    }

    public void setWebMap(WebMap webMap) {
        this.webMap = webMap;
    }

    @Override
    public boolean writeTileToStream(final int tileIdx, Resource target) throws IOException {

        final Response responseEncoder = getResponseEncoder();
        if (webMap instanceof RenderedImageMap) {
            WMSMapContext mapContext = ((RenderedImageMap) webMap).getMapContext();
            try {
                {
                    RenderedImage image = ((RenderedImageMap) webMap).getImage();
                    setImage(image);
                }
                RenderedImage tile = super.metaTiledImage;
                if (this.tiles.length > 1) {
                    Rectangle tileDim = this.tiles[tileIdx];
                    tile = createTile(tileDim.x, tileDim.y, tileDim.width, tileDim.height);
                }
                RenderedImageMapResponse imageResponse = (RenderedImageMapResponse) responseEncoder;

                OutputStream outStream = target.getOutputStream();
                try {
                    imageResponse.formatImageOutputStream(tile, outStream, mapContext);
                } finally {
                    outStream.close();
                }
            } finally {
                mapContext.dispose();
            }
        } else {
            throw new IllegalArgumentException("Only RenderedImageMap are supported so far: "
                    + webMap.getClass().getName());
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private Response getResponseEncoder() {
        final String format = responseFormat.getFormat();
        final String mimeType = responseFormat.getMimeType();

        Response response = cachedTileEncoders.get(format);
        if (response == null) {
            final Operation operation;
            {
                GetMapRequest getMap = new GetMapRequest();
                getMap.setFormat(mimeType);
                Object[] parameters = { getMap };
                Service service = (Service) GeoServerExtensions.bean("wms-1_1_1-ServiceDescriptor");
                if (service == null) {
                    throw new IllegalStateException(
                            "Didn't find service descriptor 'wms-1_1_1-ServiceDescriptor'");
                }
                operation = new Operation("GetMap", service, (Method) null, parameters);
            }

            final List<Response> extensions = GeoServerExtensions.extensions(Response.class);
            final Class<?> webMapClass = webMap.getClass();
            for (Response r : extensions) {
                if (r.getBinding().isAssignableFrom(webMapClass) && r.canHandle(operation)) {
                    synchronized (cachedTileEncoders) {
                        cachedTileEncoders.put(mimeType, r);
                        response = r;
                        break;
                    }
                }
            }
            if (response == null) {
                throw new IllegalStateException("Didn't find a " + Response.class.getName()
                        + " to handle " + mimeType);
            }
        }
        return response;
    }
}
