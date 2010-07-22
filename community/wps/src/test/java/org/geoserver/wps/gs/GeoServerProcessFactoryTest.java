package org.geoserver.wps.gs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.Parameter;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

public class GeoServerProcessFactoryTest extends TestCase {

	GeoServerProcessFactory factory;

	@Override
	protected void setUp() throws Exception {
		factory = new GeoServerProcessFactory();
	}

	public void testNames() {
		Set<Name> names = factory.getNames();
		assertTrue(names.size() > 0);
		// System.out.println(names);
		assertTrue(names.contains(new NameImpl("gs", "Bounds")));
	}

	public void testDescribeBounds() {
		NameImpl boundsName = new NameImpl("gs", "Bounds");
		InternationalString desc = factory.getDescription(boundsName);
		assertNotNull(desc);

		Map<String, Parameter<?>> params = factory.getParameterInfo(boundsName);
		assertEquals(1, params.size());

		Parameter<?> features = params.get("features");
		assertEquals(FeatureCollection.class, features.type);
		assertTrue(features.required);

		Map<String, Parameter<?>> result = factory.getResultInfo(boundsName,
				null);
		assertEquals(1, result.size());
		Parameter<?> bounds = result.get("bounds");
		assertEquals(ReferencedEnvelope.class, bounds.type);
	}

	public void testExecuteBounds() throws ProcessException {
		// prepare a mock feature collection
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName("test");
		final ReferencedEnvelope re = new ReferencedEnvelope(-10, 10, -10, 10,
				null);
		FeatureCollection fc = new ListFeatureCollection(tb.buildFeatureType()) {
			@Override
			public synchronized ReferencedEnvelope getBounds() {
				return re;
			}
		};

		org.geotools.process.Process p = factory.create(new NameImpl("gs",
				"Bounds"));
		Map<String, Object> inputs = new HashMap<String, Object>();
		inputs.put("features", fc);
		Map<String, Object> result = p.execute(inputs, null);

		assertEquals(1, result.size());
		ReferencedEnvelope computed = (ReferencedEnvelope) result.get("bounds");
		assertEquals(re, computed);
	}

	public void testSPI() throws Exception {
		NameImpl boundsName = new NameImpl("gs", "Bounds");
		ProcessFactory factory = Processors.createProcessFactory(boundsName);
		assertNotNull(factory);
		assertTrue(factory instanceof GeoServerProcessFactory);

		org.geotools.process.Process buffer = Processors
				.createProcess(boundsName);
		assertNotNull(buffer);
	}

}
