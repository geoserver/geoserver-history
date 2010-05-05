package org.geoserver.web.data.layer;


@SuppressWarnings("serial")
public class AttributeNewPage extends AttributeEditPage {

    public AttributeNewPage(AttributeDescription attribute, NewFeatureTypePage previousPage) {
        super(attribute, previousPage, true);
    }

}
