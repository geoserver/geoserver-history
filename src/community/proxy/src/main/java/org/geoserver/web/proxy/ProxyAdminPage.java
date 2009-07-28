package org.geoserver.web.proxy;


import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.proxy.ProxyConfig;
import org.geoserver.proxy.ProxyConfig.Mode;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

public class ProxyAdminPage extends GeoServerSecuredPage {
    /*NOTE & DANGER:
     * There is nothing here that will guarantee any form of consistency in case of multiple people
     * editing the proxy's configuration. Probably don't do that.
     */
    
    //GeoServerTablePanel<Pattern> hostnameFilterTable;

    //GeoServerTablePanel<Pattern> mimetypeFilterTable;

    @SuppressWarnings("serial")
    public ProxyAdminPage() {
        HostnameProvider hostnameProvider = new HostnameProvider(); 
        //GeoServerTablePanel<LayerGroupInfo> table;
        GeoServerTablePanel <String> hostnameFilterTable;
        
        ProxyConfig config = ProxyConfig.loadConfFromDisk();
        
        ProxyForm proxyForm = new ProxyForm("proxyForm");
        add(proxyForm);
        // Add radio buttons for mode
        RadioChoice modeChoices = new RadioChoice("modes", new PropertyModel(config, "mode"), Mode.modeNames());
        proxyForm.add(modeChoices);
        
        // the add button
        add(new BookmarkablePageLink("addNew", ProxyAdminPage.class));
        
        GeoServerDialog dialog = new GeoServerDialog("dialog");
        add(dialog);
        
        // the removal button
        /*SelectionRemovalLink removal = 
            new SelectionRemovalLink("removeSelected", hostnameFilterTable, dialog) {
            @Override
            protected StringResourceModel canRemove(CatalogInfo object) {
                StyleInfo s = (StyleInfo) object;
                if ( StyleInfo.DEFAULT_POINT.equals( s.getName() ) || 
                    StyleInfo.DEFAULT_LINE.equals( s.getName() ) || 
                    StyleInfo.DEFAULT_POLYGON.equals( s.getName() ) || 
                    StyleInfo.DEFAULT_RASTER.equals( s.getName() ) ) {
                    return new StringResourceModel("cantRemoveDefaultStyle", StylePage.this, null );
                }
                return null;
            }
        };
        add(removal);
        
        removal.setOutputMarkupId(true);
        removal.setEnabled(false);*/
        hostnameFilterTable = 
            new GeoServerTablePanel<String>("hostnameTable", hostnameProvider, true) {
            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<String> property) {
                // TODO Auto-generated method stub
                return new Label(id, property.getModel(itemModel));
            }
        };
        hostnameFilterTable.setOutputMarkupId(true);
        add(hostnameFilterTable);

    }
    
    @SuppressWarnings("serial")
    public final class ProxyForm extends Form{
        public ProxyForm(final String componentName)
        {
            super(componentName);
        }
        
        public void onSubmit()
        {
            // TODO: Add a submit button, and make it somehow call this vvv.
            
            //ProxyConfig.writeConfigToDisk(config);
        }
    }
    
}
