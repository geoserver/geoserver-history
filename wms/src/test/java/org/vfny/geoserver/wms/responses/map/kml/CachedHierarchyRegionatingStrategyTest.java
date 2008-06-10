package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import java.util.Iterator;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

public class CachedHierarchyRegionatingStrategyTest extends RegionatingTestSupport {
    public void testTiles() throws Exception {
        FeatureSource source = getFeatureSource(TILE_TESTS);
        FeatureCollection col = source.getFeatures();
        Iterator<SimpleFeature> it = col.iterator();
        while (it.hasNext()){
            SimpleFeature f = it.next();
            int x = (Integer) f.getAttribute("x");
            int y = (Integer) f.getAttribute("y");
            int z = (Integer) f.getAttribute("z");
            Polygon poly = (Polygon)f.getDefaultGeometry();
            long[] tileCoords = CachedHierarchyRegionatingStrategy.getTileCoords(
                    new ReferencedEnvelope(
                        poly.getEnvelopeInternal(),
                        CRS.decode("EPSG:4326")
                        ),
                    CachedHierarchyRegionatingStrategy.getWorldBounds()
                    );
            assertEquals("X should be " + x + " for " + poly + "; found x: " + tileCoords[0], x, tileCoords[0]);
            assertEquals(y, tileCoords[1]);
            assertEquals(z, tileCoords[2]);
        }
    }
    
    public void testExpandToTile() throws Exception {
    	// Prepare for failure
		ReferencedEnvelope worldBounds = 
			new ReferencedEnvelope(
					new Envelope(10.0, -180.0, 90.0, -90.0), 
					CRS.decode("EPSG:4326"));
		
		assertNull(CachedHierarchyRegionatingStrategy.expandToTile(worldBounds));
		
		// West
		ReferencedEnvelope west = 
			new ReferencedEnvelope(
					new Envelope(0, -180.0, 90.0, -90.0), 
					CRS.decode("EPSG:4326"));
		
		assertEquals(CachedHierarchyRegionatingStrategy.expandToTile(west),west);
		
		// East
		ReferencedEnvelope east = 
			new ReferencedEnvelope(
					new Envelope(180.0, 0, 90.0, -90.0), 
					CRS.decode("EPSG:4326"));
		
		assertEquals(CachedHierarchyRegionatingStrategy.expandToTile(east),east);
		
		
		// Somewhere that needs real adjustment
		ReferencedEnvelope somewhere = 
			new ReferencedEnvelope(
					new Envelope(-5.0, -44.0, 2.01, 34.0), 
					CRS.decode("EPSG:4326"));
		
		ReferencedEnvelope adjustedSomewhere = CachedHierarchyRegionatingStrategy.expandToTile(somewhere);
		
		assertEquals(adjustedSomewhere.getMinX(), -45.0);
		assertEquals(adjustedSomewhere.getMaxX(), 0.0);
		
		assertEquals(adjustedSomewhere.getMinY(), 0.0);
		assertEquals(adjustedSomewhere.getMaxY(), 45.0);
    }
}
