package org.geoserver.monitor.web;

import java.util.Calendar;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;

import org.geoserver.web.ComponentBuilder;
import org.geoserver.web.FormTestPage;
import org.geoserver.web.GeoServerWicketTestSupport;

public class DateFieldTest extends GeoServerWicketTestSupport {

    public void testSanity() throws Exception {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DAY_OF_MONTH, 8);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 44);
        
        tester.startPage(new FormTestPage(new ComponentBuilder() {
            
            public Component buildComponent(String id) {
                return new DateField(id, cal);
            }
        }));
        
        tester.assertComponent( "form", Form.class );
        
        FormTester ft = tester.newFormTester("form");
        assertEquals("2010", ft.getTextComponentValue( "panel:year") );
        assertEquals("Oct", ((DropDownChoice)ft.getForm().get("panel:month")).getValue());
        
        assertEquals("08", ft.getTextComponentValue( "panel:day") );
        assertEquals("01", ft.getTextComponentValue( "panel:hour") );
        assertEquals("44", ft.getTextComponentValue( "panel:minute") );
        
        ft.setValue( "panel:year", "2001");
        ft.setValue( "panel:month", "Jan");
        ft.setValue( "panel:day", "1");
        ft.setValue( "panel:hour", "12");
        ft.setValue( "panel:minute", "30");
        ft.submit();
        
        assertEquals(2001, cal.get(Calendar.YEAR));
        assertEquals(0, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
    }
    
    int index(String month) {
        if (month.equals("Jan")) return 0;
        if (month.equals("Feb")) return 1;
        if (month.equals("Mar")) return 2;
        if (month.equals("Apr")) return 3;
        if (month.equals("May")) return 4;
        if (month.equals("Jun")) return 5;
        if (month.equals("Jul")) return 6;
        if (month.equals("Aug")) return 7;
        if (month.equals("Sep")) return 8;
        if (month.equals("Oct")) return 9;
        if (month.equals("Nov")) return 10;
        if (month.equals("Dec")) return 11;
        return -1;
    }
}
