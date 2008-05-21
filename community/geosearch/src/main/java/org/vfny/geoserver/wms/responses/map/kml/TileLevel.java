package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Represent the set of features that exist in a particular tile at a particular zoomlevel in a
 * regionated hierarchy.  The mechanism is pretty simple, a TileLevel expects to have features 
 * passed to its add() method in their order of 'importance' (to be determined by client code) and
 * will automatically push features down to lower zoomlevels after a certain quota has been met.
 *
 * @author David Winslow
 */
public class TileLevel implements Serializable {
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    ReferencedEnvelope myBBox;
    long myFeaturesPerTile;
    long myZoomLevel;

    Set myFeatures;

    private List myChildren;
    private int roundRobinCounter;

    public TileLevel(ReferencedEnvelope bbox, long featuresPerTile, long zoomLevel){
        myBBox = bbox;
        myFeaturesPerTile = featuresPerTile;
        myZoomLevel = zoomLevel;
        myFeatures = new TreeSet();
        myChildren = null;
    }

    private TileLevel(ReferencedEnvelope bbox, List childTiles){
        myBBox = bbox;
        myFeaturesPerTile = 0;
        myZoomLevel = 0;
        myFeatures = new TreeSet();
        myChildren = childTiles;
    }

    public static TileLevel makeRootLevel(ReferencedEnvelope fullBounds, long featuresPerTile){
        ReferencedEnvelope western = new ReferencedEnvelope(
                fullBounds.getMinimum(0),
                fullBounds.getCenter(0),
                fullBounds.getMinimum(1),
                fullBounds.getMaximum(1),
                fullBounds.getCoordinateReferenceSystem()
                );
        ReferencedEnvelope eastern = new ReferencedEnvelope(
                fullBounds.getCenter(0),
                fullBounds.getMaximum(0),
                fullBounds.getMinimum(1),
                fullBounds.getMaximum(1),
                fullBounds.getCoordinateReferenceSystem()
                );

        LOGGER.info("Creating root tilelevel with bboxes: "  + western + "; " + eastern);
        List children = new ArrayList();
        children.add(new TileLevel(western, featuresPerTile, 1));
        children.add(new TileLevel(eastern, featuresPerTile, 1));
        return new TileLevel(fullBounds, children);
    }
    
    public void populate(FeatureCollection collection){
    	Iterator it = collection.iterator();
    	try{
	    	while (it.hasNext()){
	    		SimpleFeature feature = (SimpleFeature)it.next();
	    		add(feature);
	    	}
    	} finally {
    		collection.close(it);
    	}
    }

    public boolean withinTileBounds(SimpleFeature f){
        return myBBox.intersects(((Geometry)f.getDefaultGeometry()).getEnvelopeInternal());
    }

    public void add(SimpleFeature f){
        if (myFeatures.size() < myFeaturesPerTile) {
            myFeatures.add(f.getID());
        } else {
            addToChild(f);
        }
    }

    private void addToChild(SimpleFeature f){
        if (myChildren == null){
            myChildren = createChildTiles();
        }

        roundRobinCounter = (roundRobinCounter + 1) % myChildren.size();
        for (int i = 0; i< myChildren.size(); i++){
            int index = (roundRobinCounter + i) % myChildren.size();
            TileLevel child = (TileLevel)myChildren.get(index);

            if (child.withinTileBounds(f)){
                child.add(f);
                return;
            }
        }

        LOGGER.log(Level.SEVERE, "EEK! No child found to accept feature! Bounds are: ");
        for (int i = 0; i < myChildren.size(); i++){
            TileLevel child = (TileLevel) myChildren.get(i);
            System.out.println(child.myBBox);
        }
    }

    private List createChildTiles(){
        List children = new ArrayList();
        ReferencedEnvelope topLeft = new ReferencedEnvelope(
                myBBox.getCenter(0),
                myBBox.getMinimum(0),
                myBBox.getMaximum(1),
                myBBox.getCenter(1),
                myBBox.getCoordinateReferenceSystem()
                );

        ReferencedEnvelope topRight = new ReferencedEnvelope(
                myBBox.getMaximum(0),
                myBBox.getCenter(0),
                myBBox.getMaximum(1),
                myBBox.getCenter(1),
                myBBox.getCoordinateReferenceSystem()
                );

        ReferencedEnvelope bottomLeft = new ReferencedEnvelope(
                myBBox.getCenter(0),
                myBBox.getMinimum(0),
                myBBox.getCenter(1),
                myBBox.getMinimum(1),
                myBBox.getCoordinateReferenceSystem()
                );

        ReferencedEnvelope bottomRight = new ReferencedEnvelope(
                myBBox.getMaximum(0),
                myBBox.getCenter(0),
                myBBox.getCenter(1),
                myBBox.getMinimum(1),
                myBBox.getCoordinateReferenceSystem()
                );

        children.add(new TileLevel(topLeft, myFeaturesPerTile, myZoomLevel + 1));
        children.add(new TileLevel(topRight, myFeaturesPerTile, myZoomLevel + 1));
        children.add(new TileLevel(bottomLeft, myFeaturesPerTile, myZoomLevel + 1));
        children.add(new TileLevel(bottomRight, myFeaturesPerTile, myZoomLevel + 1));
        return children;
    }

    public TileLevel findTile(ReferencedEnvelope bounds){
        try{
            bounds = bounds.transform(myBBox.getCoordinateReferenceSystem(), true);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Couldn't transform bounding box when searching for tile!", e);
        }

        if (!myBBox.contains((Envelope)bounds)){
            Envelope overlap = bounds.intersection(myBBox);
            double ratio = (bounds.getWidth() * bounds.getHeight()) 
                / (overlap.getWidth() * overlap.getHeight());

            if (Math.abs(1 - ratio) > 0.05){
                return null;
            }
        }

        if (myChildren != null){
            Iterator it = myChildren.iterator();
            while (it.hasNext()){
                TileLevel child = (TileLevel)it.next();
                TileLevel theTile = child.findTile(bounds);
                if (theTile != null) return theTile;
            }
        }

        return this;
    }

    public boolean include(SimpleFeature feature){
        return myFeatures.contains(feature.getID());
    }

    public int depth(){
        int d = 0;

        if (myChildren != null){
            Iterator it = myChildren.iterator();
            while (it.hasNext()){
                TileLevel child = (TileLevel)it.next();
                d = Math.max(d, child.depth());
            }
        }

        return d + 1;
    }

    public long getZoomLevel(){
        return myZoomLevel;
    }

    public ReferencedEnvelope getBounds(){
        return myBBox;
    }

    public int getFeatureCount(){
        return myFeatures.size();
    }

    public List tilesAtDepth(int i){
        List l = new ArrayList();
        accumulateTiles(l, i);
        return l;
    }

    private void accumulateTiles(List l, int i){
        if (i == 1){
            l.add(this);
        } else if (myChildren != null){
            Iterator it = myChildren.iterator();
            while (it.hasNext()){
                TileLevel child = (TileLevel)it.next();
                child.accumulateTiles(l, i-1);
            }
        }
    }

    public String toString(){
        int maxdepth = depth();
        String result = "";
        for (int i = 1; i <= maxdepth; i++){
            List level = tilesAtDepth(i);
            Iterator it = level.iterator(); 
            while(it.hasNext()){
                TileLevel current = (TileLevel)it.next();
                result += current.getFeatureCount() + ", ";
            }
            result += "\n";
        }
        return result;
    }
}
