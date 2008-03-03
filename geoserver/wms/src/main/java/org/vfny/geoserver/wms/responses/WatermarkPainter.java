/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.util.SoftValueHashMap;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.requests.GetMapRequest;

/**
 * This class prints undistorted watermarks on the map by getting information
 * from the layers.
 */
public class WatermarkPainter {
    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.wms.responses");

    public static final Color TRANSPARENT = new Color(255, 255, 255, 0);

    private static final int TRANSPARENT_CODE = (255 << 16) | (255 << 8) | 255;

    /**
     * Transient cache to avoid reloading the same file over and over
     */
    private static final Map logoCache = new SoftValueHashMap();

    /**
     * The GetMapRequest
     */
    protected GetMapRequest request;

    /**
     * Initializes a new Watermark Painter
     * 
     * @param background
     *            background color, or null if transparent
     */
    public WatermarkPainter(GetMapRequest request) {
        this.request = request;
    }

    /**
     * Print the WaterMarks into the graphic2D.
     * 
     * @param g2D
     * @param paintArea
     * @throws IOException
     * @throws ClassCastException
     * @throws MalformedURLException
     */
    public void paint(Graphics2D g2D, Rectangle paintArea) throws MalformedURLException,
            ClassCastException, IOException {
        BufferedImage logo = getLogo();

        if (logo != null) {
            final WMS wms = this.request.getWMS();
            paintLogo(g2D, logo, paintArea, wms.getWatermarkTransparency(), wms
                    .getWatermarkPosition());
        }
    }

    protected void paintLogo(Graphics2D graphics, BufferedImage logo, Rectangle tile,
            int watermarkTransparency, int watermarkPosition) {
        final int logoWidth = logo.getWidth();
        final int logoHeight = logo.getHeight();

        Composite oldComposite = graphics.getComposite();
        try {
            final float alpha = (float) ((100.0 - watermarkTransparency) / 100.0);
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            double tx = tile.getMinX(); 
            double ty = tile.getMinY();
            switch(watermarkPosition) {
                case WMS.WATERMARK_UL:
                    break;
                case WMS.WATERMARK_UC:
                    tx += (tile.getWidth() - logo.getWidth()) / 2; 
                    break;
                case WMS.WATERMARK_UR:
                    tx += tile.getWidth() - logo.getWidth();
                    break;
                case WMS.WATERMARK_CL:
                    ty += (tile.getHeight() - logo.getHeight()) / 2;
                    break;
                case WMS.WATERMARK_CC:
                    ty += (tile.getHeight() - logo.getHeight()) / 2;
                    tx += (tile.getWidth() - logo.getWidth()) / 2; 
                    break;
                case WMS.WATERMARK_CR:
                    ty += (tile.getHeight() - logo.getHeight()) / 2;
                    tx += tile.getWidth() - logo.getWidth();
                    break;
                case WMS.WATERMARK_LL:
                    ty += tile.getHeight() - logo.getHeight();
                    break;
                case WMS.WATERMARK_LC:
                    ty += tile.getHeight() - logo.getHeight();
                    tx += (tile.getWidth() - logo.getWidth()) / 2; 
                    break;
                case WMS.WATERMARK_LR:
                    ty += tile.getHeight() - logo.getHeight();
                    tx += tile.getWidth() - logo.getWidth();
                    break;
                
            }
            graphics.drawRenderedImage(logo, AffineTransform.getTranslateInstance(tx, ty));
        } finally {
            graphics.setComposite(oldComposite);
        }

    }

    protected BufferedImage getLogo() throws IOException {
        BufferedImage logo = null;

        if (this.request.getWMS().isGlobalWatermarking()) {
            // fully resolve the url (consider data dir)
            URL url = null;
            try {
                final String globalWatermarkingURL = this.request.getWMS()
                        .getGlobalWatermarkingURL();
                url = new URL(globalWatermarkingURL);

                // make sure we can read images directly from the data dir
                if (url.getProtocol() == null || url.getProtocol().equals("file")) {
                    File file = GeoserverDataDirectory.findDataFile(globalWatermarkingURL);
                    if (file.exists())
                        url = file.toURL();
                }
            } catch (MalformedURLException e) {
                url = null;
            }

            if (url == null)
                return null;

            LogoCacheEntry entry = (LogoCacheEntry) logoCache.get(url);
            if (entry == null || entry.isExpired()) {
                logo = ImageIO.read(url);
                if (url.getProtocol().equals("file")) {
                    entry = new LogoCacheEntry(logo, new File(url.getFile()));
                    logoCache.put(url, entry);
                }
            } else {
                logo = entry.getLogo();
            }
        }
        return logo;
    }

    /**
     * Contains an already loaded logo and the tools to check it's up to date
     * compared to the file system
     * 
     * @author Andrea Aime - TOPP
     * 
     */
    private static class LogoCacheEntry {
        private BufferedImage logo;

        private long lastModified;

        private File file;

        public LogoCacheEntry(BufferedImage logo, File file) {
            this.logo = logo;
            this.file = file;
            lastModified = file.lastModified();
        }

        public boolean isExpired() {
            return file.lastModified() > lastModified;
        }

        public BufferedImage getLogo() {
            return logo;
        }
    }
}