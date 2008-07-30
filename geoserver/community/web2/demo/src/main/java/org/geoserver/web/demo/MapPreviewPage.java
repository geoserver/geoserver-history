package org.geoserver.web.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerBasePage;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;

public class MapPreviewPage extends GeoServerBasePage {
    @SuppressWarnings("serial")
    public MapPreviewPage(){
        //TODO: This should list Layers, not Resources.  (Should it exist per-workspace? just have
        //the layers grouped by workspace in a single page?)
        
        IModel resourceListModel = new LoadableDetachableModel(){
            public Object load(){
                return getCatalog().getResources(ResourceInfo.class);
            }
        };

        final List<String> formats = getAvailableFormats();

        add(new ListView("layer", resourceListModel){
            public void populateItem(ListItem item){
                final String prefixedName = ((ResourceInfo) item.getModelObject()).getPrefixedName();

                item.add(
                    new ExternalLink("layerLink", "wms/reflect?layers=" + prefixedName)
                        .setContextRelative(true)
                        .add(new Label("label", prefixedName))
                );

                item.add(new ListView("formatLink", formats){
                    public void populateItem(ListItem item){
                        String format = (String)item.getModel().getObject();
                        item.add(new ExternalLink(
                                "link",
                                "wms/reflect?layers=" 
                                + prefixedName
                                + "&format=" 
                                + format
                                ).setContextRelative(true)
                                .add(new Label("label", format))
                            );
                    }
                });
            }
        });
    }

    private List<String> getAvailableFormats(){
        List<String> formats = new ArrayList<String>();

        for (GetMapProducerFactorySpi spi : 
            getGeoServerApplication().getBeansOfType(GetMapProducerFactorySpi.class)
            ) {
            formats.add((String)spi.getSupportedFormats().iterator().next());
        }

        return formats;
    }
}
