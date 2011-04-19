package org.geoserver.inspire.web;

import static org.geoserver.inspire.wms.InspireMetadata.LANGUAGE;
import static org.geoserver.inspire.wms.InspireMetadata.METADATA_URL;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.web.services.AdminPagePanel;
import org.geoserver.web.util.MapModel;
import org.geoserver.wms.WMSInfo;

/**
 * Panel for the WMS admin page to set the WMS INSPIRE extension preferences.
 */
public class InspireAdminPanel extends AdminPagePanel {

    private static final long serialVersionUID = -7670555379263411393L;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public InspireAdminPanel(String id, IModel<WMSInfo> model) {
        super(id, model);

        PropertyModel<MetadataMap> metadata = new PropertyModel<MetadataMap>(model, "metadata");

        add(new LanguageDropDownChoice("language", new MapModel(metadata, LANGUAGE.key)));
        TextField textField = new TextField("metadataURL", new MapModel(metadata, METADATA_URL.key));
        add(textField);
        textField.add(new AttributeModifier("title", true, new ResourceModel(
                "InspireAdminPanel.metadataURL.title")));
    }
}
