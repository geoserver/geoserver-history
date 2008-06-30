package org.geoserver.web.demo;

import org.geoserver.web.GeoServerBasePage;
import org.geoserver.catalog.ResourceInfo;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;

public class MapPreviewPage extends GeoServerBasePage {
    public MapPreviewPage(){
        //TODO: This should list Layers, not Resources.  (Should it exist per-workspace? just have the layers grouped by workspace in a single page?)
        add(new ListView("layer", getCatalog().getResources(ResourceInfo.class)){
            public void populateItem(ListItem item){
                final ResourceInfo info = (ResourceInfo)item.getModelObject();

                item.add(new ExternalLink("layerLink", "wms/reflect?layers=" + info.getPrefixedName())
                    .setContextRelative(true)
                    .add(new Label("label", info.getPrefixedName()))
                );
                item.add(new ListView("formatLink", getGeoServerApplication().getBeansOfType(GetMapProducerFactorySpi.class)){
                    public void populateItem(ListItem item){
                        final GetMapProducerFactorySpi format = (GetMapProducerFactorySpi) item.getModelObject();
                        String formatName = (String)format.getSupportedFormats().iterator().next();
                        item.add(new ExternalLink(
                                "link",
                                "wms/reflect?layers=" + info.getPrefixedName() + "&format=" + formatName
                                ).setContextRelative(true)
                                .add(new Label("label", formatName))
                            );
                    }
                });
            }
        });

    }
}
