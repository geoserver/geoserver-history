package org.vfny.geoserver.wms.requests;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class GetMapKvpReaderTest extends TestCase {

	public void testFilterBaseMap() {
		Map kvp = new HashMap();
		kvp.put(new String("LAYERS"), new String("basemap1") );
		kvp.put(new String("STYLES"), new String("basemap1") );
		
		GetMapKvpReader kvpReader = new GetMapKvpReader(kvp, null);
		
		Map layers = new HashMap();
		layers.put(new String("basemap1"), new String("topp:states") );
		
		Map styles = new HashMap();
		styles.put(new String("basemap1"), new String("population") );
		
		kvpReader.filterBaseMap(layers, styles);
		
		String l = (String) kvp.get("LAYERS");
		assertTrue("LAYERS should be: 'topp:states' not "+l, l.equalsIgnoreCase("topp:states"));
		
		String s = (String) kvp.get("STYLES");
		assertTrue("STYLES should be: 'population' not "+s, s.equalsIgnoreCase("population"));
		
		// -----------------
		
		kvp = new HashMap();
		kvp.put(new String("LAYERS"), new String("thing,basemap1,thing2,basemap2") );
		kvp.put(new String("STYLES"), new String("things,basemap1,things2,basemap2") );
		
		kvpReader = new GetMapKvpReader(kvp, null);
		
		layers.put(new String("basemap2"), new String("topp:test") );
		styles.put(new String("basemap2"), new String("teststyle") );
		
		kvpReader.filterBaseMap(layers, styles);
		
		l = (String) kvp.get("LAYERS");
		assertTrue("LAYERS should be: 'thing,topp:states,thing2,topp:test' not "+l, l.equalsIgnoreCase("thing,topp:states,thing2,topp:test"));
		s = (String) kvp.get("STYLES");
		assertTrue("STYLES should be: 'things,population,things2,teststyle' not "+s, s.equalsIgnoreCase("things,population,things2,teststyle"));
		
		System.out.println("LAYERS="+l);
		System.out.println("STYLES="+s);
	}
	
}
