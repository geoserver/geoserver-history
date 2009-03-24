package org.geoserver.web.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class EnvelopePanelTestPage extends WebPage {

    public EnvelopePanelTestPage(ReferencedEnvelope e) {
        Form form = new Form("form");
        form.add( new EnvelopePanel( "content", e ));
        add(form);
    }
}
