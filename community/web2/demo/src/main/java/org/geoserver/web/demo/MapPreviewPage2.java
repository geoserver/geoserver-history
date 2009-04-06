package org.geoserver.web.demo;

import static org.geoserver.web.demo.PreviewLayerProvider.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleExternalLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.vfny.geoserver.wms.GetMapProducer;

public class MapPreviewPage2 extends GeoServerBasePage {

    PreviewLayerProvider provider = new PreviewLayerProvider();

    GeoServerTablePanel<PreviewLayer> table;

    public MapPreviewPage2() {
        final List<String> wmsOutputFormats = getAvailableWMSFormats();
        final List<String> wfsOutputFormats = getAvailableWFSFormats();

        table = new GeoServerTablePanel<PreviewLayer>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id,
                    final IModel itemModel, Property<PreviewLayer> property) {
                PreviewLayer layer = (PreviewLayer) itemModel.getObject();

                if (property == TYPE) {
                    return new Label(id, TYPE.getModel(itemModel));
                } else if (property == WORKSPACE) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == NAME) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == OL) {
                    String url = "window.open('" + layer.getWmsLink()
                            + "&format=application/openlayers" + "');";
                    return buildJSExternalLink(id, url, "OpenLayers");
                } else if (property == KML) {
                    String url = "../wms/kml_reflect?layers=" + layer.getName();
                    return buildJSExternalLink(id, url, "KML");
                } else if (property == GML) {
                    String url = "../ows?service=WFS&version=1.0.0&request=GetFeature&typeName="
                            + layer.getName() + "&maxFeatures=50";
                    return buildJSExternalLink(id, url, "GML");
                } else if (property == WMS) {
                    return buildJSWMSSelect(id, wmsOutputFormats, layer);
                } else if (property == WFS) {
                    return buildJSWFSSelect(id, wfsOutputFormats, layer);
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
     * 
     * @param id
     * @param url
     * @param title
     * @return
     */
    private Component buildJSExternalLink(String id, String url, String title) {
        SimpleExternalLink sel = new SimpleExternalLink(id, new Model("#"),
                new Model(title));
        sel.add(new AttributeAppender("onclick", new Model(url), ";"));
        return sel;
    }

    private Component buildJSWMSSelect(String id,
            List<String> wmsOutputFormats, PreviewLayer layer) {
        Fragment f = new Fragment(id, "menuFragment", MapPreviewPage2.this);
        DropDownChoice menu = new DropDownChoice("menu", new Model(null),
                wmsOutputFormats);
        String url = "'" + layer.getWmsLink()
                + "&format=' + this.options[this.selectedIndex].text";
        menu.add(new AttributeAppender("onchange", new Model("window.open("
                + url + ");this.selectedIndex=0"), ";"));
        f.add(menu);
        return f;
    }

    private Component buildJSWFSSelect(String id,
            List<String> wfsOutputFormats, PreviewLayer layer) {
        Fragment f = new Fragment(id, "menuFragment", MapPreviewPage2.this);
        DropDownChoice menu = new DropDownChoice("menu", new Model(null),
                wfsOutputFormats);
        String url = "'../ows?service=WFS&version=1.0.0&request=GetFeature&typeName="
                + layer.getName()
                + "&maxFeatures=50"
                + "&outputFormat=' + this.options[this.selectedIndex].text";
        menu.add(new AttributeAppender("onchange", new Model("window.open("
                + url + ");this.selectedIndex=0"), ";"));
        f.add(menu);
        return f;
    }
}
