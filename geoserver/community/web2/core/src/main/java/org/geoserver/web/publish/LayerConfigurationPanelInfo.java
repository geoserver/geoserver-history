package org.geoserver.web.publish;

import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.ComponentInfo;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Extension point for sections of the configuration pages for individual layers.
 * 
 * @author David Winslow <dwinslow@openplans.org>
 */
public class LayerConfigurationPanelInfo extends ComponentInfo<LayerConfigurationPanel> {
    public static final long serialVersionUID = -1l;

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.config");

    private List<String> myHandleableClasses;

    public void setSupportedTypes(List<String> types){
        myHandleableClasses = types;
    }

    public List<String> getSupportedTypes(){
        return Collections.unmodifiableList(myHandleableClasses);
    }

    public boolean canHandle(LayerInfo layer){
        if (myHandleableClasses == null) return true;

        for (String className : myHandleableClasses){
            try{
                if (Class.forName(className).isInstance(layer.getResource())){
                    return true;
                } 
            } catch (ClassNotFoundException cnfe){
                LOGGER.severe("Couldn't find class " + className + "; please check your applicationContext.xml");
            }
        }
        return false;
    }
}
