package org.geoserver.wms.legendgraphic;

import java.awt.image.BufferedImage;

import org.geoserver.wms.response.LegendGraphic;

public class BufferedImageLegendGraphic extends LegendGraphic {

    private BufferedImage legendGraphic;

    public BufferedImageLegendGraphic(final String mimeType, final BufferedImage legendGraphic) {
        this.legendGraphic = legendGraphic;
        setMimeType(mimeType);
    }

    public BufferedImage getLegend() {
        return legendGraphic;
    }
}
