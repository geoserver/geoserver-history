/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.decorations;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.responses.Decoration;
import org.vfny.geoserver.wms.WMSMapContext;

import org.geotools.renderer.lite.RendererUtilities;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;

public class ScaleRatioDecoration implements Decoration {
    /** A logger for this class. */
    private static final Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses");

    public void loadOptions(Map<String, String> options) {
    }

    public Dimension findOptimalSize(WMSMapContext mapContext){
        return new Dimension(50, 50);
    }

    public String getScaleText(WMSMapContext mapContext) {
        return String.format(
            "1 : %0$1.0f", 
            RendererUtilities.calculateOGCScale(
                mapContext.getAreaOfInterest(),
                mapContext.getRequest().getWidth(),
                new HashMap()
            )
        );
    }

    public void paint(Graphics2D g2d, Rectangle paintArea, WMSMapContext mapContext) 
    throws Exception {
        Color oldColor = g2d.getColor();
        g2d.setColor(Color.WHITE);
        g2d.fill(paintArea);
        g2d.setColor(Color.BLACK);
        g2d.drawString(
            getScaleText(mapContext), 
            (float)paintArea.getMinX(), 
            (float)paintArea.getMaxY()
        );
        g2d.setColor(oldColor);
    }
}
