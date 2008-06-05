package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import java.util.Iterator;
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
}
