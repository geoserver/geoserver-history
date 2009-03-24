/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.decorations;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.responses.Decoration;
import org.vfny.geoserver.wms.WMSMapContext;

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
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.imageio.ImageIO;

public class LegendDecoration implements Decoration {
    /** A logger for this class. */
    private static final Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses");

    public void loadOptions(Map<String, String> options){
    }

    public Dimension findOptimalSize(WMSMapContext mapContext){
        int x = 0, y = 0;
        try {
            for (MapLayerInfo layer : mapContext.getRequest().getLayers()){
                try {
                    BufferedImage legend = getLegend(layer);
                    x = Math.max(x, (int)legend.getWidth());
                    y += legend.getHeight();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error getting legend for " + layer);
                    continue;
                }
            }
            x += 5;
            return new Dimension(x, y);
        } catch (Exception e) {
            return new Dimension(50,50);
        }
    }

    public void paint(Graphics2D g2d, Rectangle paintArea, WMSMapContext mapContext) 
    throws Exception {
        Dimension d = findOptimalSize(mapContext);

        Color oldColor = g2d.getColor();
        g2d.setColor(Color.WHITE);
        g2d.fill(paintArea);
        final WMS wms = mapContext.getRequest().getWMS();

        AffineTransform tx = 
            AffineTransform.getTranslateInstance(paintArea.getX(), paintArea.getY());
        
        double scaleFactor = (paintArea.getWidth() / d.getWidth());
        scaleFactor = Math.min(scaleFactor, paintArea.getHeight() / d.getHeight());

        if (scaleFactor < 1.0) {
            tx.scale(scaleFactor, scaleFactor);
        }

        for (MapLayerInfo layer : mapContext.getRequest().getLayers()){
            BufferedImage legend = getLegend(layer);
            g2d.drawImage(legend, tx, null);
            tx.translate(0, legend.getHeight());
        }
        g2d.setColor(oldColor);
    }

    protected BufferedImage getLegend(MapLayerInfo layer) 
    throws MalformedURLException, IOException {
        // TODO: Do this via direct GetLegendGraphic instantiation
        URL imageURL = new URL("http://localhost:8080/geoserver/wms?request=getLegendGraphic&format=image/png&layer=" + layer.getName());

        return ImageIO.read(imageURL);
    }
}
