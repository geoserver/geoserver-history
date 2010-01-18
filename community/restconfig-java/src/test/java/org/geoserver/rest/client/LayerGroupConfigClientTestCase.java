/**
 * 
 */
package org.geoserver.rest.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.geoserver.rest.client.datatypes.Layer;
import org.geoserver.rest.client.datatypes.LayerGroup;
import org.geoserver.rest.client.datatypes.LayerGroups;
import org.geoserver.rest.client.datatypes.Layers;
import org.geoserver.rest.client.datatypes.Style;
import org.geoserver.rest.client.datatypes.Styles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 * @author Ronak Patel
 */
public class LayerGroupConfigClientTestCase {

	private final LayerGroupConfigClient client;

	public LayerGroupConfigClientTestCase() throws MalformedURLException {

		this.client = new LayerGroupConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost:8080/geoserver")));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerGroupConfigClient#createLayerGroup(org.geoserver.rest.client.datatypes.LayerGroup)}.
	 * @throws IOException
	 */
	@Test
	public final void testCreateLayerGroup() throws IOException {

		final LayerGroup group = new LayerGroup();
		group.setName("created");
		
		//set up the pond
		final Style roadStyle = new Style();
		roadStyle.setName("polygon");

		final Styles roadStyles = new Styles();
		roadStyles.getStyle().add(roadStyle);

		final Layer roads = new Layer();
		roads.setName("tiger_roads");
		roads.setStyles(roadStyles);

		//set up the forest
		final Style landmarkStyle = new Style();
		landmarkStyle.setName("point");

		final Styles landmarkStyles = new Styles();
		landmarkStyles.getStyle().add(landmarkStyle);

		final Layer landmarks = new Layer();
		landmarks.setName("poly_landmarks");
		landmarks.setStyles(landmarkStyles);

		final Layers layers = new Layers();
		layers.getLayer().add(roads);
		layers.getLayer().add(landmarks);
		
		final Styles styles = new Styles();
		styles.getStyle().add(roadStyle);
		styles.getStyle().add(landmarkStyle);
		
		group.setLayers(layers);
		group.setStyles(styles);

		//create the layer group
		final LayerGroup created = this.client.createLayerGroup(group);

		Assert.assertEquals("created", created.getName());
		Assert.assertEquals(2, created.getLayers().getLayer().size());

		Assert.assertEquals("tiger_roads", created.getLayers().getLayer().get(0).getName());
		Assert.assertEquals("poly_landmarks", created.getLayers().getLayer().get(1).getName());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerGroupConfigClient#deleteLayerGroup(org.geoserver.rest.client.datatypes.LayerGroup)}.
	 * @throws IOException
	 */
	@Test
	public final void testDeleteLayerGroup() throws IOException {

		final LayerGroup group = new LayerGroup();
		group.setName("created");

		this.client.deleteLayerGroup(group);
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerGroupConfigClient#getLayerGroup(java.lang.String)}.
	 * @throws IOException
	 */
	@Test
	public final void testGetLayerGroup() throws IOException {

		final LayerGroup group = this.client.getLayerGroup("tiger-ny");

		Assert.assertEquals("EPSG:4326", group.getBounds().getCrs());
		Assert.assertEquals(-74.047185, group.getBounds().getMinx());
		Assert.assertEquals(40.679648, group.getBounds().getMiny());
		Assert.assertEquals(-73.907005, group.getBounds().getMaxx());
		Assert.assertEquals(40.882078, group.getBounds().getMaxy());
		
		Assert.assertEquals(4, group.getLayers().getLayer().size());
		Assert.assertEquals("giant_polygon", group.getLayers().getLayer().get(0).getName());
		Assert.assertEquals("poly_landmarks", group.getLayers().getLayer().get(1).getName());
		Assert.assertEquals("tiger_roads", group.getLayers().getLayer().get(2).getName());
		Assert.assertEquals("poi", group.getLayers().getLayer().get(3).getName());
		
		Assert.assertEquals("tiger-ny", group.getName());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerGroupConfigClient#getLayerGroups()}.
	 * @throws IOException
	 */
	@Test
	public final void testGetLayerGroups() throws IOException {

		final LayerGroups groups = this.client.getLayerGroups();

		final List<LayerGroup> groupList = groups.getLayerGroup();

		Assert.assertEquals(3, groupList.size());
		Assert.assertEquals("spearfish", groupList.get(0).getName());
		Assert.assertEquals("tasmania", groupList.get(1).getName());
		Assert.assertEquals("tiger-ny", groupList.get(2).getName());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerGroupConfigClient#updateLayerGroup(org.geoserver.rest.client.datatypes.LayerGroup)}.
	 * @throws IOException
	 */
	@Test
	public final void testUpdateLayerGroup() throws IOException {

		final LayerGroup group = new LayerGroup();
		group.setName("created");
		
		//set up the pond
		final Style roadStyle = new Style();
		roadStyle.setName("polygon");

		final Styles roadStyles = new Styles();
		roadStyles.getStyle().add(roadStyle);

		final Layer roads = new Layer();
		roads.setName("tiger_roads");
		roads.setStyles(roadStyles);

		//set up the forest
		final Style landmarkStyle = new Style();
		landmarkStyle.setName("point");

		final Styles landmarkStyles = new Styles();
		landmarkStyles.getStyle().add(landmarkStyle);

		final Layer landmarks = new Layer();
		landmarks.setName("poly_landmarks");
		landmarks.setStyles(landmarkStyles);

		final Layers layers = new Layers();
		layers.getLayer().add(roads);
		layers.getLayer().add(landmarks);
		
		final Styles styles = new Styles();
		styles.getStyle().add(roadStyle);
		styles.getStyle().add(landmarkStyle);
		
		group.setLayers(layers);
		group.setStyles(styles);

		//create the layer group
		final LayerGroup created = this.client.createLayerGroup(group);

		Assert.assertEquals("created", created.getName());
		Assert.assertEquals(2, created.getLayers().getLayer().size());

		Assert.assertEquals("tiger_roads", created.getLayers().getLayer().get(0).getName());
		Assert.assertEquals("poly_landmarks", created.getLayers().getLayer().get(1).getName());
		
		//now update the created group
		created.getLayers().getLayer().remove(1);
		created.getStyles().getStyle().remove(1);
		
		final LayerGroup modified = this.client.updateLayerGroup(created);
		
		Assert.assertEquals("created", modified.getName());
		Assert.assertEquals(1, modified.getLayers().getLayer().size());

		Assert.assertEquals("tiger_roads", modified.getLayers().getLayer().get(0).getName());
		
		this.client.deleteLayerGroup(group);
	}

}
