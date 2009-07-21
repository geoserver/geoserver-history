package org.geoserver.web.proxy;


import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.proxy.ProxyConfig;
import org.geoserver.proxy.ProxyConfig.Mode;
import org.geoserver.web.GeoServerSecuredPage;

public class ProxyAdminPage extends GeoServerSecuredPage {
    //GeoServerTablePanel<Pattern> hostnameFilterTable;

    //GeoServerTablePanel<Pattern> mimetypeFilterTable;

    public ProxyAdminPage() {
        //HostRegexProvider hostProvider = new HostRegexProvider(); 
        //GeoServerTablePanel<LayerGroupInfo> table;
        
        ProxyConfig config = ProxyConfig.loadConfFromDisk();
        
        ProxyForm proxyForm = new ProxyForm("proxyForm");
        add(proxyForm);
                
        // Add radio buttons for mode
        RadioChoice modeChoices = new RadioChoice("modes", new PropertyModel(config, "mode"), Mode.modeNames());
        proxyForm.add(modeChoices);
        
        //Make a list of 
        
        proxyForm.add(new TextArea("hostnameRegexes", new PropertyModel(config, "hostnameWhitelist")));
        proxyForm.add(new TextArea("mimetypeRegexes"));

        /*ListChoice hostnameRegexes = new ListChoice("hostnameRegexes", config.hostnameWhitelist);
        proxyForm.add(hostnameRegexes);
        
        ListChoice mimetypeRegexes = new ListChoice("mimetypeRegexes", config.hostnameWhitelist);
        proxyForm.add(mimetypeRegexes);*/
        
        // TODO: Properly adapt this code
        /*hostnameFilterTable = new GeoServerTablePanel<Pattern>("table", hostProvider, true) {
            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<Pattern> property) {
                throw new IllegalArgumentException("Don't know a property named "
                        + property.getName());
            }

            @Override
            protected void onSelectionUpdate(AjaxRequestTarget target) {
                // do some stuff here
            }

        };
        hostnameFilterTable.setOutputMarkupId(true);
        add(hostnameFilterTable);
        */
        // TODO: Add a mimetype filter table
        

    }
    
    public final class ProxyForm extends Form{
        public ProxyForm(final String componentName)
        {
            super(componentName);
        }
        
        public void onSubmit()
        {
            // TODO: Add a submit button, and make it somehow call this vvv.
            
            ProxyConfig.writeConfigToDisk(config);
        }
    }
}
