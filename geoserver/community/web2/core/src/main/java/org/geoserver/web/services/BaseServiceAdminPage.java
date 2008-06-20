package org.geoserver.web.services;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.web.GeoServerBasePage;

/**
 * Base page for service administration pages.
 * <p>
 * Subclasses of this page should contribute form components in the {@link #build(ServiceInfo, Form)}
 * method. Each component that is added to the form should have a corresponding
 * markup entry of the following form:
 * <pre>
 * <wicket:extend>
 *   &lt;li>
 *       &lt;span>
 *         &lt;label><wicket:message key="maxFeatures.title">Maximum Features</wicket:message></label>
 *         &lt;input wicket:id="maxFeatures" class="field text" type="text"></input>
 *       &lt;/span>
 *       &lt;p class="instruct">
 *       &lt;/p>
 *     &lt;/li>
 *   
 * </wicket:extend>
 *   </pre>
 * </p>
 * 
 *@author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class BaseServiceAdminPage<T extends ServiceInfo> extends GeoServerBasePage {

    public BaseServiceAdminPage() {
        
        T info = getGeoServer().getService( getServiceClass() );
        
        Form form = new Form( "form", new CompoundPropertyModel( info ) );
        add( form );
        
        form.add( new CheckBox( "enabled" ) );
        
        form.add( new TextField( "title" ) );
        form.add( new TextArea( "abstract" ) );
        
        build(info, form);
    }
    
    /**
     * The class of the service.
     * <p>
     * This value is used to obtain a reference to the service info 
     * object via {@link GeoServer#getService(Class)}. 
     * </p>
     */
    protected abstract Class<T> getServiceClass();
    
    /**
     * Builds the form for the page.
     * <p>
     * The form uses a {@link CompoundPropertyModel} so in the normal
     * case components do not need a model as its inherited from the parent.
     * This means that component id's should match the info bean property 
     * they correspond to.
     * </p>
     * @param info The service info object.
     * @param form The page form.
     */
    protected void build( T info, Form form ) {
        
    }
}
