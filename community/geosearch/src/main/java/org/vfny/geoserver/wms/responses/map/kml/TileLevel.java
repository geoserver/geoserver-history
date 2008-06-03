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
    long myZoomLevel;

    PriorityQueue myFeatures;

    private List myChildren;
    private int roundRobinCounter;

    public TileLevel(ReferencedEnvelope bbox, int featuresPerTile, long zoomLevel, Comparator comp){
        myBBox = bbox;
        myFeaturesPerTile = featuresPerTile;
        myZoomLevel = zoomLevel;
        myFeatures = new PriorityQueue(featuresPerTile, comp);
        myChildren = null;
    }

    private TileLevel(ReferencedEnvelope bbox, List childTiles, Comparator comp){
        myBBox = bbox;
        myFeaturesPerTile = 0;
        myZoomLevel = 0;
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
        children.add(new TileLevel(western, featuresPerTile, 1, comp));
        children.add(new TileLevel(eastern, featuresPerTile, 1, comp));
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

    public boolean withinTileBounds(SimpleFeature f){
        return myBBox.intersects(((Geometry)f.getDefaultGeometry()).getEnvelopeInternal());
    }

    public void add(SimpleFeature f){
        myFeatures.add(f);
        if (myFeatures.size() > myFeaturesPerTile) {
            addToChild((SimpleFeature)myFeatures.poll());
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

        children.add(new TileLevel(topLeft, myFeaturesPerTile, myZoomLevel + 1, myFeatures.comparator()));
        children.add(new TileLevel(topRight, myFeaturesPerTile, myZoomLevel + 1, myFeatures.comparator()));
        children.add(new TileLevel(bottomLeft, myFeaturesPerTile, myZoomLevel + 1, myFeatures.comparator()));
        children.add(new TileLevel(bottomRight, myFeaturesPerTile, myZoomLevel + 1, myFeatures.comparator()));
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

    public long getZoomLevel(){
        return myZoomLevel;
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

    static ReferencedEnvelope getWorldBounds(){
    	try{
    		// To avoid Caused by: org.geotools.referencing.operation.projection.ProjectionException: 
    		// Latitude 90°00.0'S is too close to a pole.
    	    return new ReferencedEnvelope(-179.9, 179.9, -89.95, 89.95, CRS.decode("EPSG:4326"));
    	} catch (Exception e){
    	    LOGGER.log(Level.SEVERE, "Failure to find EPSG:4326!!", e);
    	}
    	
        return null;
    }

    public static long[] getTileCoords(ReferencedEnvelope requestBBox, ReferencedEnvelope worldBounds){
        try{
            requestBBox = requestBBox.transform(worldBounds.getCoordinateReferenceSystem(), true);
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Couldn't reproject while acquiring tile coordinates", e);
        }

        long z = Math.round(Math.log(worldBounds.getWidth() / requestBBox.getWidth())/Math.log(2));
        long x = Math.round(((requestBBox.getMinimum(0) - worldBounds.getMinimum(0)) / worldBounds.getLength(0)) * Math.pow(2, z));
        long y = Math.round(((worldBounds.getMaximum(1) - requestBBox.getMaximum(1)) / worldBounds.getLength(1)) * Math.pow(2, z-1));

        return new long[]{x,y,z};
    }

    public void writeTo(Statement st, String tableName){
        long[] coords = getTileCoords(myBBox, getWorldBounds());

        Iterator it = myFeatures.iterator();
        while (it.hasNext()){
            try{
                String fid = ((SimpleFeature)it.next()).getID();
                st.execute("INSERT INTO " + tableName + " VALUES ( " + coords[0] + ", " + coords[1] + ", " + coords[2] + ", \'" + fid + "\' )");
            } catch (Exception e){
                LOGGER.log(Level.SEVERE, "SQL Error while trying to store tile hierarchy!", e);
            }
        }

        if (myChildren != null){
            it = myChildren.iterator();
            while (it.hasNext()){
                TileLevel child = (TileLevel)it.next();
                child.writeTo(st, tableName);
            }
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
