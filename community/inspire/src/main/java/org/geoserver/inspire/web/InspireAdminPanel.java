package org.geoserver.inspire.web;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.services.AdminPagePanel;
import org.geoserver.web.util.MapModel;

import static org.geoserver.inspire.wms.InspireMetadata.*;

public class InspireAdminPanel extends AdminPagePanel {

    public InspireAdminPanel(String id, IModel<?> model) {
        super(id, model);

        PropertyModel metadata = new PropertyModel(model, "metadata");

        add(new LanguageDropDownChoice("language", new MapModel(metadata, LANGUAGE.key)));
        add(new TextField("metadataURL", new MapModel(metadata, METADATA_URL.key)));
    }
}
