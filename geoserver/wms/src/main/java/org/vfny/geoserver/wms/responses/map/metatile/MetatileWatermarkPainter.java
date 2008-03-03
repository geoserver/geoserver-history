/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.metatile;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.WatermarkPainter;
import org.vfny.geoserver.wms.responses.map.metatile.QuickTileCache.MetaTileKey;

/**
 * Overrides the watermarking paint method so that the watermark get painted in all tiles,
 * not only in the lower corner of the meta tile
 * @author Andrea Aime - TOPP
 */
public class MetatileWatermarkPainter extends WatermarkPainter {

    private MetaTileKey key;

    public MetatileWatermarkPainter(QuickTileCache.MetaTileKey key, GetMapRequest request) {
        super(request);
        this.key = key;
    }
    
    /**
     * Overrides the base version to paint the watermark on each tile
     */
    public void paint(Graphics2D g2d, Rectangle paintArea) throws MalformedURLException,
            ClassCastException, IOException {
        BufferedImage logo = getLogo();
        if(logo != null) {
            final int metaFactor = key.getMetaFactor();
            final int tileSize = key.getTileSize();
            for (int i = 0; i < metaFactor; i++) {
                for (int j = 0; j < metaFactor; j++) {
                    int x = tileSize * i;
                    int y = tileSize * j;
                    Rectangle tileArea = new Rectangle(x, y, tileSize, tileSize);
                    final WMS wms = request.getWMS();
                    paintLogo(g2d, logo, tileArea, wms.getWatermarkTransparency(), wms.getWatermarkPosition());
                }
            }
        }
    }

}
