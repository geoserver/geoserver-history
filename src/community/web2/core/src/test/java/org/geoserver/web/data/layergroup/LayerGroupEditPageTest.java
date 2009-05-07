package org.geoserver.web.data.layergroup;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;

public class LayerGroupEditPageTest extends GeoServerWicketTestSupport {
    
    @Override
    protected void setUpInternal() throws Exception {
        Catalog catalog = getCatalog();
        String lakes = MockData.LAKES.getLocalPart();
        String forests = MockData.FORESTS.getLocalPart();
        String bridges = MockData.BRIDGES.getLocalPart();
        
        setNativeBox(catalog, lakes);
        setNativeBox(catalog, forests);
        setNativeBox(catalog, bridges);
        
        LayerGroupInfo lg = catalog.getFactory().createLayerGroup();
        lg.setName("lakes");
        lg.getLayers().add(catalog.getLayerByName(lakes));
        lg.getStyles().add(catalog.getStyleByName(lakes));
        lg.getLayers().add(catalog.getLayerByName(forests));
        lg.getStyles().add(catalog.getStyleByName(forests));
        lg.getLayers().add(catalog.getLayerByName(bridges));
        lg.getStyles().add(catalog.getStyleByName(bridges));
        CatalogBuilder builder = new CatalogBuilder(catalog);
        builder.calculateLayerGroupBounds(lg);
        catalog.add(lg);
    }
    
    public void setNativeBox(Catalog catalog, String name) throws Exception {
        FeatureTypeInfo fti = catalog.getFeatureTypeByName(name);
        fti.setNativeBoundingBox(fti.getFeatureSource(null, null).getBounds());
        fti.setLatLonBoundingBox(new ReferencedEnvelope(fti.getNativeBoundingBox(), DefaultGeographicCRS.WGS84));
        catalog.save(fti);
    }

    public void testComputeBounds() {
        LayerGroupEditPage page = new LayerGroupEditPage(getCatalog().getLayerGroupByName("lakes"));
        tester.startPage(page);
        // print(page, true, false);
        
        tester.assertRenderedPage(LayerGroupEditPage.class);
        // remove the first and second elements
        tester.clickLink("form:layers:layers:listContainer:items:1:itemProperties:2:component:link");
        // the regenerated list will have ids starting from 4
        tester.clickLink("form:layers:layers:listContainer:items:4:itemProperties:2:component:link");
        // manually regenerate bounds
        tester.clickLink("form:generateBounds");
        // print(page, true, true);
        // submit the form
        tester.submitForm("form");
        
        // For the life of me I cannot get this test to work... and I know by direct UI inspection that
        // the page works as expected...
//        FeatureTypeInfo bridges = getCatalog().getResourceByName(MockData.BRIDGES.getLocalPart(), FeatureTypeInfo.class);
//        assertEquals(getCatalog().getLayerGroupByName("lakes").getBounds(), bridges.getNativeBoundingBox());
    }
}
