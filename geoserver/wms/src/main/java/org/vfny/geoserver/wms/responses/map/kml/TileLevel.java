package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
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
    int myFeaturesPerTile;

    PriorityQueue myFeatures;

    private List myChildren;
    private int roundRobinCounter;

    public TileLevel(ReferencedEnvelope bbox, int featuresPerTile, Comparator comp){
        myBBox = bbox;
        myFeaturesPerTile = featuresPerTile;
        myFeatures = new PriorityQueue(featuresPerTile, comp);
        myChildren = null;
    }

    private TileLevel(ReferencedEnvelope bbox, List childTiles, Comparator comp){
        myBBox = bbox;
        myFeaturesPerTile = 0;
        myFeatures = new PriorityQueue(1, comp);
        myChildren = childTiles;
    }

    public static TileLevel makeRootLevel(ReferencedEnvelope fullBounds, int featuresPerTile, Comparator comp){
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
        children.add(new TileLevel(western, featuresPerTile, comp));
        children.add(new TileLevel(eastern, featuresPerTile, comp));
        return new TileLevel(fullBounds, children, comp);
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

    public void populateExcluding(FeatureCollection collection, Set<String> exclude){
    	Iterator it = collection.iterator();
    	try{
	    	while (it.hasNext()){
	    		SimpleFeature feature = (SimpleFeature)it.next();
                if (!exclude.contains(feature.getID()))
                    add(feature);
	    	}
    	} finally {
    		collection.close(it);
    	}
    }

    public boolean withinTileBounds(SimpleFeature f){
        return CachedHierarchyRegionatingStrategy.containsCentroid(
                myBBox, 
                (Geometry)f.getDefaultGeometry()
                );
    }

    public void add(SimpleFeature f){
        if (withinTileBounds(f)){
            myFeatures.add(f);
            if (myFeatures.size() > myFeaturesPerTile) {
            	SimpleFeature child = (SimpleFeature) myFeatures.poll();
                addToChild(child);
                //System.out.println(child.getProperty("STATE_NAME") +"(" +  ((Geometry) child.getDefaultGeometry()).getArea() + ") got bumped " 
                //		+ "for " + f.getProperty("STATE_NAME") + " (" +((Geometry) f.getDefaultGeometry()).getArea()+ ")");
            }
        }
    }

    private void addToChild(SimpleFeature f){
        if (myChildren == null){
            myChildren = createChildTiles();
        }

        roundRobinCounter = (roundRobinCounter + 1) % myChildren.size();
        for (int i = 0; i < myChildren.size(); i++){
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
            //System.out.println(child.myBBox);
        }
    }

    private List createChildTiles(){
        List children = new ArrayList();

        for (ReferencedEnvelope bbox : CachedHierarchyRegionatingStrategy.quadSplit(myBBox))
            children.add(new TileLevel(bbox, myFeaturesPerTile, myFeatures.comparator()));

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

    public ReferencedEnvelope getBounds(){
        return myBBox;
    }

    public Set getFeatureSet(){
        Set fids = new TreeSet();
        
        Iterator it = myFeatures.iterator();
        while (it.hasNext()){
            SimpleFeature f = (SimpleFeature)it.next();
            fids.add(f.getID());
        }

        return fids;
    }

    public int getFeatureCount(){
        return myFeatures.size();
    }

    public List tilesAtDepth(int i){
        List l = new ArrayList();
        accumulateTiles(l, i);
        return l;
    }

    public void writeTo(Statement st, String tableName, ReferencedEnvelope worldBounds){
        long[] coords = CachedHierarchyRegionatingStrategy.getTileCoords(myBBox, worldBounds);

        Iterator it = myFeatures.iterator();
        while (it.hasNext()){
            try{
            	CachedHierarchyRegionatingStrategy.featureCounter++;
                String fid = ((SimpleFeature)it.next()).getID();
                String SQL = "INSERT INTO " + tableName + " VALUES ( " + coords[0] + ", " + coords[1] + ", " + coords[2] + ", \'" + fid + "\' )";
                st.execute(SQL);
            } catch (Exception e){
                LOGGER.log(Level.SEVERE, "SQL Error while trying to store tile hierarchy!", e);
            }
        }

        if (myChildren != null){
            it = myChildren.iterator();
            while (it.hasNext()){
                TileLevel child = (TileLevel)it.next();
                child.writeTo(st, tableName, worldBounds);
            }
        } else {
        }
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
