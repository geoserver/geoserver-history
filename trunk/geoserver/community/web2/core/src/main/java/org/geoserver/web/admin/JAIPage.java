package org.geoserver.web.admin;

import org.geoserver.config.GeoServer;
import org.geoserver.jai.JAIInfo;
import org.geoserver.web.GeoServerBasePage;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;

public class JAIPage extends ServerAdminPage {
    private static final long serialVersionUID = -1184717232184497578L;

    public JAIPage(){
        final IModel geoServerModel = getGeoServerModel();
        final IModel globalInfoModel = getGlobalInfoModel(); 
        final IModel jaiModel = getJAIModel();

        Form form = new Form("form", new CompoundPropertyModel(jaiModel)) {
            protected void onSubmit() {
                ((GeoServer)geoServerModel.getObject())
                    .getGlobal()
                    .getMetadata()
                    .put(
                            JAIInfo.KEY,
                            (JAIInfo)jaiModel.getObject()
                        );
            }
        };

        add( form );

        form.add(new TextField("memoryCapacity"));
        form.add(new TextField("memoryThreshold"));
        form.add(new TextField("tileThreads"));
        form.add(new TextField("tilePriority"));
        form.add(new CheckBox("recycling"));
        form.add(new CheckBox("imageIOCache"));
        form.add(new CheckBox("jpegAcceleration"));
        form.add(new CheckBox("pngAcceleration"));

        Button submit = new Button("submit", new StringResourceModel("submit", this, null));
        form.add(submit);
    }
}
