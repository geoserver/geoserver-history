package org.geoserver.wms.map;

import java.awt.image.RenderedImage;
import java.util.Collections;
import java.util.List;

import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.response.Map;
import org.geotools.coverage.grid.GridCoverage2D;

public class BufferedImageMap extends Map {

    private RenderedImage image;

    private List<GridCoverage2D> renderedCoverages;

    private WMSMapContext mapContext;

    public BufferedImageMap(final WMSMapContext mapContext, final RenderedImage image,
            final String mimeType) {
        this.mapContext = mapContext;
        this.image = image;
        setMimeType(mimeType);
    }

    public RenderedImage getImage() {
        return image;
    }

    @SuppressWarnings("unchecked")
    public List<GridCoverage2D> getRenderedCoverages() {
        return renderedCoverages == null ? Collections.EMPTY_LIST : renderedCoverages;
    }

    public void setRenderedCoverages(List<GridCoverage2D> renderedCoverages) {
        this.renderedCoverages = renderedCoverages;
    }

    public WMSMapContext getMapContext() {
        return mapContext;
    }

}
