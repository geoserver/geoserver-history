package org.geoserver.teradata;

import org.apache.wicket.markup.html.form.Form;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.web.data.store.DefaultDataStoreEditPanel;

public class TeradataEditPanel extends DefaultDataStoreEditPanel {

    public TeradataEditPanel(String componentId, Form storeEditForm) {
        super(componentId, storeEditForm);
    }
    
    @Override
    protected void applyDataStoreParamsDefaults(StoreInfo info) {
        super.applyDataStoreParamsDefaults(info);
        
        //override the Application parameter
    }

}
