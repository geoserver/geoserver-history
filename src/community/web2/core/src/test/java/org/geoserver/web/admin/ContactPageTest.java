package org.geoserver.web.admin;

import org.geoserver.config.ContactInfo;
import org.geoserver.web.GeoServerWicketTestSupport;

import org.apache.wicket.markup.html.form.TextField;

public class ContactPageTest extends GeoServerWicketTestSupport {
    public void testValues() {
        ContactInfo info = getGeoServerApplication()
            .getGeoServer()
            .getGlobal()
            .getContact();

        login();
        tester.startPage(ContactPage.class);
        tester.assertComponent("form:address", TextField.class);
        tester.assertModelValue("form:address", info.getAddress());
    }
}
