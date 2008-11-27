package org.vfny.geoserver.wms.responses;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.geotools.renderer.lite.LabelCacheItem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

/**
 * Stores label items and helps in finding the interferering ones, either by
 * pure overlap or within a certain distance from the specified bounds
 * 
 * @author Andrea Aime
 * 
 */
public class LabelIndex {

    Quadtree index = new Quadtree();

    /**
     * Returns true if there is any label in the index within the specified
     * distance from the bounds. For speed reasons the bounds will be simply
     * expanded by the distance, no curved buffer will be generated
     * 
     * @param bounds
     * @param distance
     * @return
     */
    public boolean labelsWithinDistance(Rectangle2D bounds, double distance) {
        if (distance < 0)
            return false;

        Envelope e = toEnvelope(bounds);
        e.expandBy(distance);
        List results = index.query(e);
        if (results.size() == 0)
            return false;
        for (Iterator it = results.iterator(); it.hasNext();) {
            InterferenceItem item = (InterferenceItem) it.next();
            if (item.env.intersects(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a label into the index
     * 
     * @param item
     * @param bounds
     */
    public void addLabel(LabelCacheItem item, Rectangle2D bounds) {
        Envelope e = toEnvelope(bounds);
        index.insert(e, new InterferenceItem(e, item));
    }

    /**
     * Turns the specified Java2D rectangle into a JTS envelope
     * 
     * @param bounds
     * @return
     */
    private Envelope toEnvelope(Rectangle2D bounds) {
        return new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
    }

    /**
     * Simple structure stored into the quadtree (keeping the item around helps
     * in debugging)
     * 
     * @author Andrea Aime
     * 
     */
    static class InterferenceItem {
        Envelope env;

        LabelCacheItem item;

        public InterferenceItem(Envelope env, LabelCacheItem item) {
            super();
            this.env = env;
            this.item = item;
        }

    }
}
