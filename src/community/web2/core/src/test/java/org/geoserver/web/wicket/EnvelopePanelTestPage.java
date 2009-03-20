package org.geoserver.web.wicket;

import org.apache.wicket.markup.html.WebPage;

import com.vividsolutions.jts.geom.Envelope;

public class EnvelopePanelTestPage extends WebPage {

    public EnvelopePanelTestPage(Envelope e) {
        add( new EnvelopePanel( "content", e ));
    }
}
