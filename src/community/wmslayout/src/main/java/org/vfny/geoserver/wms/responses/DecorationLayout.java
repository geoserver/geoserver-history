/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.responses.decorations.LegendDecoration;
import org.vfny.geoserver.wms.responses.decorations.ScaleRatioDecoration;
import org.vfny.geoserver.wms.responses.decorations.WatermarkDecoration;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The DecorationLayout class describes a set of overlays to be used to enhance a WMS response.
 * It maintains a collection of Decoration objects and the configuration associated with each, and
 * delegates the actual rendering operations to the decorations.
 *
 * @author David Winslow <dwinslow@opengeo.org> 
 */
public class DecorationLayout {
    private static Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.responses");

    private static class Block {
        private static enum Mode {OVERLAY, APPEND};

        public static enum Position {
            UL, UC, UR, CL, CC, CR, LL, LC, LR;

            public static Rectangle findBounds(
                Position p,
                Rectangle container,
                Dimension dim,
                Point o,
                Mode m) {

                int x = 0, y = 0;
                int height = dim.height, width = dim.width;
                

                if (m == Mode.APPEND) {
                    switch (p) {
                        case UC:
                            return new Rectangle(
                                (int)container.getMinX(), (int)(container.getMinY() - dim.height),
                                (int)container.getWidth(), dim.height
                            );
                        case LC:
                            return new Rectangle(
                                (int)container.getMinX(), (int) container.getMaxY(),
                                (int)container.getWidth(), dim.height
                            );
                        case CR:
                            return new Rectangle(
                                (int)container.getMaxX(), (int) container.getMinY(),
                                dim.width, (int) container.getHeight()
                            );
                        case CL: 
                            return new Rectangle(
                                (int) (container.getMinX() - dim.width), (int) container.getMinY(),
                                dim.width, (int) container.getHeight()
                            );
                        default:
                            System.out.println("Returning null for Position: " + p);
                            return null;
                    }
                } else {
                    // adjust Y coord
                    switch (p) {
                        case UC:
                        case UR:
                        case UL:
                            y = (int) (container.getMinY() + o.y);
                            break;

                        case CL:
                        case CC:
                        case CR:
                            y = (int) (container.getMinY() + container.getMaxY() - dim.height) / 2;
                            // ignore vertical offset when vertically centered
                            break;

                        case LL:
                        case LC:
                        case LR:
                            y = (int) (container.getMaxY() - o.y - dim.height);
                    }

                    // adjust X coord
                    switch(p){
                        case UL:
                        case CL:
                        case LL:
                            x = (int) (container.getMinX() + o.x);
                            break;

                        case UC:
                        case CC:
                        case LC:
                            x = (int) (container.getMinX() + container.getMaxX()) / 2;
                            // ignore horizontal offset when horizontally centered
                            break;

                        case UR:
                        case CR:
                        case LR:
                            x = (int) (container.getMaxX() - o.x - dim.width);
                    }

                    // in the event that this block is the same size as the container, 
                    // ignore the offset so it will fit
                    if ((dim.width + (2 * o.x)) > container.width) {
                        x = (int) container.getMinX() + o.x;
                        width = container.width - (2 * o.x);
                    }

                    if ((dim.height + (2 * o.y)) > container.height) {
                        y = (int) container.getMinY() + o.y;
                        height = container.height - (2 * o.y);
                    }

                    return new Rectangle(x, y, width, height);
                }
            }
        }

        final Decoration decoration;
        final Position position;
        final Dimension dimension;
        final Point offset;
        final Mode mode;

        public Block(Decoration d, Position p, Dimension dim, Point o, Mode m) {
            decoration = d;
            position = p;
            dimension = dim;
            offset = o;
            mode = m;
        }

        public Dimension findOptimalSize(Graphics2D g2d, WMSMapContext mapContext){
            return (dimension != null) 
                ? dimension 
                : decoration.findOptimalSize(g2d, mapContext);
        }

        public void paint(Graphics2D g2d, Rectangle rect, WMSMapContext mapContext) 
        throws Exception {
            Dimension desiredSize = findOptimalSize(g2d, mapContext);

            Rectangle box = Position.findBounds(position, rect, desiredSize, offset, mode);
            Shape oldClip = g2d.getClip();
            g2d.setClip(box);
            decoration.paint(g2d, box, mapContext);
            g2d.setClip(oldClip);
        }
    }

    private List<Block> blocks;

    public DecorationLayout(){
        this.blocks = new ArrayList<Block>();
    }

    public static DecorationLayout fromFile(File f) throws Exception {
        DecorationLayout dl = new DecorationLayout();
        
        Document confFile = new SAXBuilder().build(f);

        for (Element e : (List<Element>)confFile.getRootElement().getChildren("decoration")){
            Map<String, String> m = new HashMap<String,String>();
            for (Element option : (List<Element>)e.getChildren("option")){
                m.put(option.getAttributeValue("name"), option.getAttributeValue("value"));
            }

            Decoration decoration = null;
            if (e.getAttributeValue("type").equals("watermark")) {
                decoration = new WatermarkDecoration();
            } else if (e.getAttributeValue("type").equals("legend")) {
                decoration = new LegendDecoration();
            } else if (e.getAttributeValue("type").equals("scaleratio")) {
                decoration = new ScaleRatioDecoration();
            }

            decoration.loadOptions(m);

            Block.Position pos = null;

            if (e.getAttributeValue("affinity").equalsIgnoreCase("bottom,right")) {
                pos = Block.Position.LR;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("bottom,left")) {
                pos = Block.Position.LL;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("bottom,center")) {
                pos = Block.Position.LC;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("center,left")) {
                pos = Block.Position.CL;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("center,right")) {
                pos = Block.Position.CR;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("center,center")) {
                pos = Block.Position.CC;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("top,right")) {
                pos = Block.Position.UR;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("top,left")) {
                pos = Block.Position.UL;
            } else if (e.getAttributeValue("affinity").equalsIgnoreCase("top,center")) {
                pos = Block.Position.LC;
            }

            Dimension size = null;

            if (e.getAttributeValue("size") != null 
                    && !e.getAttributeValue("size").equalsIgnoreCase("auto")) {
                String[] sizeArr = e.getAttributeValue("size").split(",");

                size = new Dimension(Integer.valueOf(sizeArr[0]), Integer.valueOf(sizeArr[1]));
            }

            String[] offsetArr = e.getAttributeValue("offset").split(",");
            Point offset =
                new Point(Integer.valueOf(offsetArr[0]), Integer.valueOf(offsetArr[1]));

            dl.addBlock(new Block(
                decoration,
                pos,
                size,
                offset,
                Block.Mode.OVERLAY
            ));
        }

        return dl;
    }

    private void addBlock(Block b){
        blocks.add(b);
    }

    public void paint(Graphics2D g2d, Rectangle paintArea, WMSMapContext mapContext){ 
        for (Block b : blocks) {
            try{
                b.paint(g2d, paintArea, mapContext);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "couldn't paint due to: ", e);
            }
        }
    }
}
