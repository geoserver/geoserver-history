package org.geoserver.web.wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;

import com.vividsolutions.jts.geom.Envelope;

public class EnvelopePanelTest extends GeoServerWicketTestSupport {

    Envelope e;
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        
        e = new Envelope(-180,180,-90,90);
        tester.startPage(new EnvelopePanelTestPage(e) ); 
    }
    
    public void test() throws Exception {
        tester.assertComponent( "content:form", Form.class );
        
        FormTester ft = tester.newFormTester( "content:form");
        assertEquals( "-180", ft.getTextComponentValue( "minX") );
        assertEquals( "-90", ft.getTextComponentValue( "minY") );
        assertEquals( "180", ft.getTextComponentValue( "maxX") );
        assertEquals( "90", ft.getTextComponentValue( "maxY") );
        
        EnvelopePanel ep = (EnvelopePanel) tester.getComponentFromLastRenderedPage("content");
        assertEquals( e, ep.getModelObject() );
        
        ft.setValue( "minX", "-2");
        ft.setValue( "minY", "-2");
        ft.setValue( "maxX", "2");
        ft.setValue( "maxY", "2");
        
        ep.updateModel();
        
        assertEquals( new Envelope(-2,2,-2,2), ep.getModelObject() );
    }
}
