package org.geoserver.csv.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.restlet.Transformer;
import org.restlet.data.MediaType;
import org.restlet.resource.InputRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;

import com.noelios.restlet.http.HttpRequest;

public class TileLayersResource extends Resource {

    /**
     * Grabs the GWC capabilities and performs an XSLT transformation to
     * generate a sort of caps document for tiled layers and their styles
     */
    @Override
    public void handleGet() {
        try {
            // ugly way to get to the context path... it seems restlet does not expose it???
            String base = getRequest().getResourceRef().getIdentifier().replaceAll("/rest/csv/tileLayers", "");
            URL capsUrl = new URL(
                    base + "/ows?service=WMS&request=GetCapabilities&version=1.1.1");
            Representation input = new InputRepresentation(
                    capsUrl.openStream(), MediaType.TEXT_XML);
            InputStream xsl = TileLayersResource.class
                    .getResourceAsStream("tile-layers.xsl");
            Transformer tx = new Transformer(Transformer.MODE_RESPONSE,
                    new InputRepresentation(xsl, MediaType.TEXT_XML));
            getResponse().setEntity(tx.transform(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
