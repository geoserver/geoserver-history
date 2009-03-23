package org.geoserver.web.wicket;

import java.io.Serializable;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.tester.FormTester;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CRSPanelTest extends GeoServerWicketTestSupport {

    public void testStandloneUnset() throws Exception {
        tester.startPage( new CRSPanelTestPage() );
        
        tester.assertComponent( "form", Form.class );
        tester.assertComponent( "form:crs", CRSPanel.class );
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
        
        CRSPanel crsPanel = (CRSPanel) tester.getComponentFromLastRenderedPage( "form:crs");
        assertNull( crsPanel.getCRS() );
    }
    
    public void testStandaloneUnchanged() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        tester.startPage( new CRSPanelTestPage( crs ) );
        
        tester.assertComponent( "form", Form.class );
        tester.assertComponent( "form:crs", CRSPanel.class );
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
        
        CRSPanel crsPanel = (CRSPanel) tester.getComponentFromLastRenderedPage( "form:crs");
        assertEquals( DefaultGeographicCRS.WGS84, crsPanel.getCRS() );
    }
    
    public void testStandaloneChanged() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        tester.startPage( new CRSPanelTestPage( crs ) );
        
        TextField srs = (TextField) tester.getComponentFromLastRenderedPage( "form:crs:srs");
        srs.setModelObject( "EPSG:3005");
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
        
        CRSPanel crsPanel = (CRSPanel) tester.getComponentFromLastRenderedPage( "form:crs");
        assertEquals( CRS.decode("EPSG:3005"), crsPanel.getCRS() );
    }
    
    public void testCompoundPropertyUnchanged() throws Exception {
        Foo foo = new Foo( DefaultGeographicCRS.WGS84 );
        tester.startPage( new CRSPanelTestPage( foo ));
        
        tester.assertComponent( "form", Form.class );
        tester.assertComponent( "form:crs", CRSPanel.class );
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
        
        assertEquals( DefaultGeographicCRS.WGS84, foo.crs );
    }
    
    public void testCompoundPropertyChanged() throws Exception {
        Foo foo = new Foo( DefaultGeographicCRS.WGS84 );
        tester.startPage( new CRSPanelTestPage( foo ));
        
        TextField srs = (TextField) tester.getComponentFromLastRenderedPage( "form:crs:srs");
        srs.setModelObject( "EPSG:3005");
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
       
        assertEquals( CRS.decode("EPSG:3005"), foo.crs );
    }
    
    public void testPropertyUnchanged() throws Exception {
        Foo foo = new Foo( DefaultGeographicCRS.WGS84 );
        tester.startPage( new CRSPanelTestPage( new PropertyModel( foo, "crs") ));
        
        tester.assertComponent( "form", Form.class );
        tester.assertComponent( "form:crs", CRSPanel.class );
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
        
        assertEquals( DefaultGeographicCRS.WGS84, foo.crs );
    }
    
    public void testPropertyChanged() throws Exception {
        Foo foo = new Foo( DefaultGeographicCRS.WGS84 );
        tester.startPage( new CRSPanelTestPage( new PropertyModel( foo, "crs" ) ));
        
        TextField srs = (TextField) tester.getComponentFromLastRenderedPage( "form:crs:srs");
        srs.setModelObject( "EPSG:3005");
        
        FormTester ft = tester.newFormTester( "form");
        ft.submit();
       
        assertEquals( CRS.decode("EPSG:3005"), foo.crs );
    }
    
    static class Foo implements Serializable {
        public CoordinateReferenceSystem crs;
        
        Foo( CoordinateReferenceSystem crs ) {
            this.crs = crs;
        }
    }
}
