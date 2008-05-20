package org.vfny.geoserver.wms.responses.map.kml;

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

public class TileLevel implements Serializable {
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    ReferencedEnvelope myBBox;
    String myAttributeName;
    long myFeaturesPerTile;
    long myZoomLevel;

    Number myMax;
    Number myMin;
    Set myFeatures;

    private List myChildren;
    private int roundRobinCounter;

    public TileLevel(ReferencedEnvelope bbox, String attributeName, long featuresPerTile, long zoomLevel){
        myBBox = bbox;
        myFeaturesPerTile = featuresPerTile;
        myZoomLevel = zoomLevel;
        myFeatures = new TreeSet();
        myChildren = null;
        myAttributeName = attributeName;
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

        children.add(new TileLevel(topLeft, myAttributeName, myFeaturesPerTile, myZoomLevel + 1));
        children.add(new TileLevel(topRight, myAttributeName, myFeaturesPerTile, myZoomLevel + 1));
        children.add(new TileLevel(bottomLeft, myAttributeName, myFeaturesPerTile, myZoomLevel + 1));
        children.add(new TileLevel(bottomRight, myAttributeName, myFeaturesPerTile, myZoomLevel + 1));
        return children;
    }

    public TileLevel findTile(ReferencedEnvelope bounds){
        try{
            bounds = bounds.transform(myBBox.getCoordinateReferenceSystem(), true);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Couldn't transform bounding box when searching for tile!", e);
        }

        if (!myBBox.contains((Envelope)bounds)){
            return null;
        }

        TileLevel theTile = null;

        if (myChildren != null){
            Iterator it = myChildren.iterator();
            while (it.hasNext()){
                TileLevel child = (TileLevel)it.next();
                theTile = child.findTile(bounds);
                if (theTile != null) break;
            }
        }

        if (theTile == null){
            return this;
        }

        return this;
    }

    public Number getMax(){
        return myMax;
    }

    public Number getMin(){
        return myMin;
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
