package org.vfny.geoserver.wms.responses.map.kml;

import java.util.Iterator;
import java.util.List;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests for the custom quad tree class that is used for regionating.  
 * 
 * @author David Winslow <dwinslow@openplans.org>
 */
public class TileLevelTest extends RegionatingTestSupport {
    /**
     * Verify that TileLevels fill in appropriately when data is tightly clustered geographically 
     */
    public void testDeepTiles() throws Exception {
        FeatureSource source = getFeatureSource(STACKED_FEATURES);
        FeatureCollection collection = source.getFeatures();

        TileLevel root = TileLevel.makeRootLevel(TileLevel.getWorldBounds(), 1);
        root.populate(collection);

        assertEquals("Found unexpected depth for tree: " + root, 11, root.depth());

        for (int i = 2; i < 11; i++){
            List tiles = root.tilesAtDepth(i);

            assertEquals(1, countFeaturesIn(tiles));
        }
    }

    /**
     * Helper method to count the features in a list of tiles.
     * @param tiles the List in which to count the features
     * @return the total number of features in those tiles (and not in any child tiles)
     */
    private int countFeaturesIn(List tiles) {
        Iterator levelIterator = tiles.iterator();
        int count = 0;
        while (levelIterator.hasNext()){
            TileLevel tile = (TileLevel)levelIterator.next();
            if (tile != null){
                count += tile.getFeatureCount();
            }
        }
        return count;
    }

    /**
     * Verify that TileLevels are built appropriately when the data is spread out geographically.
     */
    public void testShallowTiles() throws Exception {
        FeatureSource source = getFeatureSource(DISPERSED_FEATURES);
        FeatureCollection collection = source.getFeatures();

        TileLevel root = TileLevel.makeRootLevel(TileLevel.getWorldBounds(), 1);
        root.populate(collection);

        assertEquals("Expected 3 but found " + root.depth() + " for depth of tree: " + root, 3, root.depth());
        assertEquals(2, countFeaturesIn(root.tilesAtDepth(2)));
        assertEquals(8, countFeaturesIn(root.tilesAtDepth(3)));
    }

    public void testFindTile() throws Exception {
        FeatureSource source = getFeatureSource(DISPERSED_FEATURES);
        FeatureCollection collection = source.getFeatures();

        TileLevel root = TileLevel.makeRootLevel(TileLevel.getWorldBounds(), 1);
        root.populate(collection);

        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        ReferencedEnvelope[] boxes = new ReferencedEnvelope[]{
            // one of the two top-level tiles
            new ReferencedEnvelope(-180, 0, -90, 90, crs),  
            // a tile below the top level
            new ReferencedEnvelope(-180, -90, -90, 0, crs), 
            // a tile that doesn't have a direct match, should return the smallest existing tile that matches
            new ReferencedEnvelope(-180, -135, -90, -45, crs) 
        };

        assertEquals(1, root.findTile(boxes[0]).getZoomLevel()); 
        assertEquals(2, root.findTile(boxes[1]).getZoomLevel());
        assertEquals(2, root.findTile(boxes[2]).getZoomLevel());
    }
}
