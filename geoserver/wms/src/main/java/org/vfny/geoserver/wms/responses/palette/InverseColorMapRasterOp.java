/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.palette;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterOp;
import java.awt.image.WritableRaster;


public class InverseColorMapRasterOp implements RasterOp {
    private final IndexColorModel icm;
    private int alphaThreshold;
    private boolean hasAlpha;
    private int transparencyIndex;
    private EfficientInverseColorMapComputation invCM;

    public InverseColorMapRasterOp(final IndexColorModel destCM, final int quantizationColors,
        final int alphaThreshold) {
        this.icm = destCM;
        this.alphaThreshold = alphaThreshold;
        hasAlpha = icm.hasAlpha();
        transparencyIndex = icm.getTransparentPixel();

        final int mapSize = icm.getMapSize();
        final byte[] map = new byte[(mapSize - 1) * 3];
        final byte[] r = new byte[mapSize];
        final byte[] g = new byte[mapSize];
        final byte[] b = new byte[mapSize];
        icm.getReds(r);
        icm.getGreens(g);
        icm.getBlues(b);

        for (int j = 0, index = 0; j < mapSize; j++) {
            if (index == transparencyIndex) {
                continue;
            }

            map[index++] = r[j];
            map[index++] = g[j];
            map[index++] = b[j];
        }

        invCM = new EfficientInverseColorMapComputation(map, quantizationColors);
    }

    public InverseColorMapRasterOp(final IndexColorModel destCM) {
        this(destCM, 5, 1);
    }

    public WritableRaster createCompatibleDestRaster(Raster src) {
        return Raster.createBandedRaster(DataBuffer.TYPE_BYTE, src.getWidth(), src.getHeight(), 1,
            new Point(src.getMinX(), src.getMinY()));
    }

    public WritableRaster filter(Raster src, WritableRaster dest) {
        if (dest == null) {
            dest = createCompatibleDestRaster(src);
        } else {
            if (dest.getSampleModel().getNumBands() != 1) {
                throw new IllegalArgumentException(
                    "The destination raster for the IverseColorMapRasterOp must one one bad.");
            }
        }

        final int w = dest.getWidth();
        final int h = dest.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                final int[] rgba = src.getPixel(x, y, (int[]) null);

                if (hasAlpha && (rgba[3] >= this.alphaThreshold)) {
                    int val = invCM.getIndexNearest(rgba[0] & 0xff, rgba[1] & 0xff, rgba[2]);

                    if (val >= transparencyIndex) {
                        val++;
                    }

                    dest.setSample(x, y, 0, (byte) (val & 0xff));
                } else {
                    dest.setSample(x, y, 0, transparencyIndex);
                }
            }
        }

        return dest;
    }

    public Rectangle2D getBounds2D(Raster src) {
        return (Rectangle) src.getBounds().clone();
    }

    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point();
        }

        dstPt.setLocation(srcPt);

        return dstPt;
    }

    public RenderingHints getRenderingHints() {
        return null;
    }
}
