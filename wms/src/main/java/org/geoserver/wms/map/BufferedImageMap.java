package org.geoserver.wms.map;

import java.awt.image.RenderedImage;
import java.util.Collections;
import java.util.List;

import org.geoserver.wms.Map;
import org.geoserver.wms.WMSMapContext;
import org.geotools.coverage.grid.GridCoverage2D;

public class BufferedImageMap extends Map {

    private RenderedImage image;

    private List<GridCoverage2D> renderedCoverages;

    public BufferedImageMap(final WMSMapContext mapContext, final RenderedImage image,
            final String mimeType) {
        super(mapContext);
        this.image = image;
        setMimeType(mimeType);
    }

    public RenderedImage getImage() {
        return image;
    }
    
    @Override
    protected void disposeInternal(){
        image = null;
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
