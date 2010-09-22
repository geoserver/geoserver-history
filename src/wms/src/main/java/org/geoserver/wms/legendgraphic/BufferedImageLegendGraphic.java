package org.geoserver.wms.legendgraphic;

import java.awt.image.BufferedImage;

public class BufferedImageLegendGraphic {

    private BufferedImage legendGraphic;

    public BufferedImageLegendGraphic(final BufferedImage legendGraphic) {
        this.legendGraphic = legendGraphic;
    }

    public BufferedImage getLegend() {
        return legendGraphic;
    }
}
