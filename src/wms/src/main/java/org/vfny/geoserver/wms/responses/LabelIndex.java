package org.vfny.geoserver.wms.responses;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.geotools.renderer.lite.LabelCacheItem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;

public class LabelIndex {

    Quadtree index = new Quadtree();

    public boolean labelsWithinDistance(Rectangle2D bounds, double distance) {
        if(distance < 0)
            return false;
        
        Envelope e = toEnvelope(bounds);
        e.expandBy(distance);
        List results = index.query(e);
        if(results.size() == 0)
            return false;
        for (Iterator it = results.iterator(); it.hasNext();) {
            InterferenceItem item = (InterferenceItem) it.next();
            if(item.env.intersects(e)) {
                return true;
            }
        }
        return false;
    }
    
    public void addLabel(LabelCacheItem item, Rectangle2D bounds) {
        Envelope e = toEnvelope(bounds); 
        index.insert(e, new InterferenceItem(e, item));
    }
    
    private Envelope toEnvelope(Rectangle2D bounds) {
        return new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY());
    }
    
    static class InterferenceItem {
        Envelope env;
        LabelCacheItem item;
        public InterferenceItem(Envelope env, LabelCacheItem item) {
            super();
            this.env = env;
            this.item = item;;
        }
        
    }
}
