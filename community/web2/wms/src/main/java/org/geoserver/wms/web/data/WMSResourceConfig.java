package org.geoserver.wms.web.data;

import org.geoserver.web.data.ResourceConfigurationPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;

public class WMSResourceConfig extends ResourceConfigurationPanel {

    public WMSResourceConfig(String id, IModel model){
        super(id, model);

        add(new ListChoice("defaultStyle"));
        add(new ListMultipleChoice("extraStyles"));
    }
}
