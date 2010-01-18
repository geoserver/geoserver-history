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
import org.geoserver.rest.client.datatypes.Layers;
import org.geoserver.rest.client.datatypes.Style;
import org.geoserver.rest.client.datatypes.Styles;
import org.junit.Test;

public class LayerConfigClientTestCase {

	private final LayerConfigClient client;

	public LayerConfigClientTestCase() throws MalformedURLException {

		this.client = new LayerConfigClient(new GeoserverConfigClient("admin", "geoserver", new URL("http://localhost:8080/geoserver")));
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerConfigClient#getLayer(java.lang.String)}.
	 * @throws IOException
	 */
	@Test
	public final void testGetLayer() throws IOException {

		final Layer layer = this.client.getLayer("nurc:mosaic");

		Assert.assertEquals("mosaic", layer.getName());
		Assert.assertEquals("RASTER", layer.getType());
		Assert.assertEquals("raster", layer.getDefaultStyle().getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/styles/raster.json", layer.getDefaultStyle().getHref());
		Assert.assertEquals("mosaic", layer.getResource().getName());
		Assert.assertEquals("http://localhost:8080/geoserver/rest/workspaces/nurc/coveragestores/mosaic/coverages/mosaic.json", layer.getResource().getHref());
		Assert.assertTrue(layer.isEnabled());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerConfigClient#getLayers()}.
	 * @throws IOException
	 */
	@Test
	public final void testGetLayers() throws IOException {

		final Layers layers = this.client.getLayers();

		final List<Layer> layerList = layers.getLayer();

		Assert.assertEquals(19, layerList.size());
		Assert.assertEquals("Arc_Sample", layerList.get(0).getName());
		Assert.assertEquals("Pk50095", layerList.get(1).getName());
		Assert.assertEquals("mosaic", layerList.get(2).getName());
		Assert.assertEquals("Img_Sample", layerList.get(3).getName());
		Assert.assertEquals("archsites", layerList.get(4).getName());
		Assert.assertEquals("bugsites", layerList.get(5).getName());
		Assert.assertEquals("restricted", layerList.get(6).getName());
		Assert.assertEquals("roads", layerList.get(7).getName());
		Assert.assertEquals("streams", layerList.get(8).getName());
		Assert.assertEquals("sfdem", layerList.get(9).getName());
		Assert.assertEquals("giant_polygon", layerList.get(10).getName());
		Assert.assertEquals("poi", layerList.get(11).getName());
		Assert.assertEquals("poly_landmarks", layerList.get(12).getName());
		Assert.assertEquals("tiger_roads", layerList.get(13).getName());
		Assert.assertEquals("states", layerList.get(14).getName());
		Assert.assertEquals("tasmania_cities", layerList.get(15).getName());
		Assert.assertEquals("tasmania_roads", layerList.get(16).getName());
		Assert.assertEquals("tasmania_state_boundaries", layerList.get(17).getName());
		Assert.assertEquals("tasmania_water_bodies", layerList.get(18).getName());
	}

	/**
	 * Test method for {@link org.geoserver.rest.client.LayerConfigClient#updateLayer(org.geoserver.rest.client.datatypes.Layer)}.
	 * @throws IOException 
	 * @throws  
	 */
	@Test
	public final void testUpdateLayer() throws IOException {

		final Style style = new Style();
		style.setName("polygon");
		
		final Styles styles = new Styles();
		styles.getStyle().add(style);
		
		final Layer layer = new Layer();
		layer.setName("nurc:mosaic");
		layer.setStyles(styles);
		layer.setEnabled(true);
		
		//update the layer
		final Layer updatedLayer = this.client.updateLayer(layer);
		
		Assert.assertEquals("mosaic", updatedLayer.getName());
		Assert.assertEquals("raster", updatedLayer.getDefaultStyle().getName());
		Assert.assertEquals("polygon", updatedLayer.getStyles().getStyle().get(0).getName());
	}
}
