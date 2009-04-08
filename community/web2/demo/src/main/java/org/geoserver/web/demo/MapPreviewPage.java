/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import static org.geoserver.web.demo.PreviewLayerProvider.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleExternalLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.vfny.geoserver.wms.GetMapProducer;

/**
 * Shows a paged list of the available layers and points to previews
 * in various formats 
 */
@SuppressWarnings("serial")
public class MapPreviewPage extends GeoServerBasePage {

    PreviewLayerProvider provider = new PreviewLayerProvider();

    GeoServerTablePanel<PreviewLayer> table;

    public MapPreviewPage() {
        // output formats for the drop downs
        final List<String> wmsOutputFormats = getAvailableWMSFormats();
        final List<String> wfsOutputFormats = getAvailableWFSFormats();

        // build the table
        table = new GeoServerTablePanel<PreviewLayer>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id,
                    final IModel itemModel, Property<PreviewLayer> property) {
                PreviewLayer layer = (PreviewLayer) itemModel.getObject();

                if (property == TYPE) {
                    Fragment f = new Fragment(id, "iconFragment", MapPreviewPage.this);
                    f.add(new Image("layerIcon", layer.getIcon()));
                    return f;
                } else if (property == NAME) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == TITLE) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == COMMON) {
                    Fragment f = new Fragment(id, "commonLinks", MapPreviewPage.this);
                    String olUrl = "window.open('" + layer.getWmsLink()
                            + "&format=application/openlayers" + "');";
                    f.add(buildJSExternalLink("ol", olUrl, "OpenLayers"));
                    
                    String kmlUrl = "../wms/kml_reflect?layers=" + layer.getName();
                    f.add(buildJSExternalLink("kml", kmlUrl, "KML"));
                    
                    String gmlUrl = "../ows?service=WFS&version=1.0.0&request=GetFeature&typeName="
                            + layer.getName() + "&maxFeatures=50";
                    f.add(buildJSExternalLink("gml", gmlUrl, "GML"));
                    return f;
                } else if (property == ALL) {
                    return buildJSWMSSelect(id, wmsOutputFormats, wfsOutputFormats, layer);
                } 
                throw new IllegalArgumentException(
                        "Don't know a property named " + property.getName());
            }

        };
        table.setOutputMarkupId(true);
        add(table);
    }

    private List<String> getAvailableWMSFormats() {
        List<String> formats = new ArrayList<String>();

        final GeoServerApplication application = getGeoServerApplication();
        for (GetMapProducer producer : application
                .getBeansOfType(GetMapProducer.class)) {
            formats.add(producer.getOutputFormat());
        }
        Collections.sort(formats);

        return formats;
    }

    private List<String> getAvailableWFSFormats() {
        List<String> formats = new ArrayList<String>();

        final GeoServerApplication application = getGeoServerApplication();
        for (WFSGetFeatureOutputFormat producer : application
                .getBeansOfType(WFSGetFeatureOutputFormat.class)) {
            formats.addAll(producer.getOutputFormats());
        }
        Collections.sort(formats);

        return formats;
    }

    /**
     * Builds an external link that uses javascript to open the target in a new
     * window
     */
    private Component buildJSExternalLink(String id, String url, String title) {
        SimpleExternalLink sel = new SimpleExternalLink(id, new Model("#"),
                new Model(title));
        sel.add(new AttributeAppender("onclick", new Model(url), ";"));
        return sel;
    }

    /**
     * Builds a select that reacts like a menu, fully javascript based, for wms outputs 
     */
    private Component buildJSWMSSelect(String id,
            List<String> wmsOutputFormats, List<String> wfsOutputFormats, PreviewLayer layer) {
        Fragment f = new Fragment(id, "menuFragment", MapPreviewPage.this);
        WebMarkupContainer menu = new WebMarkupContainer("menu");
        
        // the wms formats are always there
        menu.add(new org.apache.wicket.markup.html.list.ListView("wmsFormats", wmsOutputFormats) {

            @Override
            protected void populateItem(ListItem item) {
                item.add(new Label("wmsFormat", new Model(item.getModelObjectAsString())));
            }
            
        });
        
        // the vector ones, it depends, we might have to hide them
        boolean vector = layer.groupInfo == null && layer.layerInfo.getType() != LayerInfo.Type.RASTER;
        WebMarkupContainer wfsFormats = new WebMarkupContainer("wfs");
        wfsFormats.add(new org.apache.wicket.markup.html.list.ListView("wfsFormats", vector ? wfsOutputFormats : Collections.emptyList()) {

            @Override
            protected void populateItem(ListItem item) {
                item.add(new Label("wfsFormat", item.getModel()));
            }
            
        });
        wfsFormats.setVisible(vector);
        menu.add(wfsFormats);
        
        // build the wms request, redirect to it in a new window, reset the selection
        String wmsUrl = "'" + layer.getWmsLink()
                + "&format=' + this.options[this.selectedIndex].text";
        String wfsUrl = "'../ows?service=WFS&version=1.0.0&request=GetFeature&typeName="
          + layer.getName()
          + "&maxFeatures=50"
          + "&outputFormat=' + this.options[this.selectedIndex].text";
        String choice = "(this.options[this.selectedIndex].parentNode.label == 'WMS') ? " + wmsUrl + " : " + wfsUrl;
        menu.add(new AttributeAppender("onchange", new Model("window.open("
                + choice + ");this.selectedIndex=0"), ";"));
        f.add(menu);
        return f;
    }

//    /**
//     * Builds a select that reacts like a menu, fully javascript based, for wfs outputs 
//     */
//    private Component buildJSWFSSelect(String id,
//            List<String> wfsOutputFormats, PreviewLayer layer) {
//        // don't provide the dropdown for non vector layers
//        if(layer.groupInfo != null || layer.layerInfo.getType() == LayerInfo.Type.RASTER) {
//            return new Label(id, "");
//        }
//        
//        Fragment f = new Fragment(id, "menuFragment", MapPreviewPage2.this);
//        DropDownChoice menu = new DropDownChoice("menu", new Model(null),
//                wfsOutputFormats);
//        
//        // build the wms request, redirect to it in a new window, reset the selection
//        String url = "'../ows?service=WFS&version=1.0.0&request=GetFeature&typeName="
//                + layer.getName()
//                + "&maxFeatures=50"
//                + "&outputFormat=' + this.options[this.selectedIndex].text";
//        menu.add(new AttributeAppender("onchange", new Model("window.open("
//                + url + ");this.selectedIndex=0"), ";"));
//        f.add(menu);
//        return f;
//    }
    
   
}
