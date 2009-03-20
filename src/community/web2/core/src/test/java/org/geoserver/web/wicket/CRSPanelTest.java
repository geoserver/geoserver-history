package org.geoserver.web.wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CRSPanelTest extends GeoServerWicketTestSupport {

    @Override
    protected void setUpInternal() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        tester.startPage( new CRSPanelTestPage( crs ) );
    }
    
    public void test() throws Exception {
        tester.assertComponent( "content", CRSPanel.class );
        CRSPanel crsPanel = (CRSPanel) tester.getComponentFromLastRenderedPage( "content");
        
        tester.assertComponent( "content:form", Form.class );
        FormTester ft = tester.newFormTester( "content:form");
        
        assertEquals( "UNKNOWN", ft.getTextComponentValue( "srs" ) );
        assertEquals( DefaultGeographicCRS.WGS84.toString(), crsPanel.getModelObject().toString() );
        
        ft.setValue( "srs", "EPSG:4326");
        crsPanel.updateModel();
        
        CoordinateReferenceSystem expected = CRS.decode( "EPSG:4326");
        assertEquals( expected.toString(), crsPanel.getModelObject().toString() );
    }
}
