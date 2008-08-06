/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.GeoServerPagingNavigator;
import org.geoserver.wms.DefaultWebMapService;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Lists the available WMS layers, allows for a map preview
 * @author Andrea Aime - TOPP
 */
public class MapPreviewPage extends GeoServerBasePage {
    private static final int MAX_ROWS = 25;

    @SuppressWarnings("serial")
    public MapPreviewPage(){
        //TODO: This should list Layers, not Resources.  (Should it exist per-workspace? just have
        //the layers grouped by workspace in a single page?)
        IModel resourceListModel = new LoadableDetachableModel(){
            public Object load() {
                List<String> result = new ArrayList<String>();
                
                // gather layer and group names
                List<LayerInfo> layers = getCatalog().getLayers();
                for (LayerInfo layer : layers) {
                    result.add(layer.getResource().getPrefixedName());
                }
                List<LayerGroupInfo> groups = getCatalog().getLayerGroups();
                for (LayerGroupInfo group : groups) {
                    result.add(group.getName());
                }
                
                // alphabetical sort
                Collections.sort(result);
                return result;
            }
        };

        final List<String> formats = getAvailableFormats();

        PageableListView layers = new PageableListView("layer", resourceListModel, MAX_ROWS){
            public void populateItem(ListItem item){
                // alternate bg color
                item.add(new SimpleAttributeModifier("class", item.getIndex() % 2 == 0 ? "even"
                        : "odd"));
                
                
                final String layerName = (String) item.getModelObject();
                
                GetMapRequest request = buildFakeGetMap(layerName);
                DefaultWebMapService.autoSetBoundsAndSize(request);
                final String linkTemplate = buildWmsLinkTemplate(request);

                final String OL_FORMAT = "application/openlayers";
                item.add(
                    new ExternalLink("layerLink", linkTemplate.replaceAll("\\$\\{format\\}", OL_FORMAT))
                        .setContextRelative(true)
                        .add(new Label("label", layerName))
                );

                item.add(new ListView("formatLink", formats){
                    public void populateItem(ListItem item){
                        String format = (String)item.getModel().getObject();
                        item.add(new ExternalLink(
                                "link",
                                linkTemplate.replaceAll("\\$\\{format\\}", format)
                                ).setContextRelative(true)
                                .add(new Label("label", format))
                            );
                    }
                });
            }
        };
        WebMarkupContainer cnt = new WebMarkupContainer("layerContainer");
        cnt.setOutputMarkupId(true);
        cnt.add(layers);
        add(cnt);
        
        // add pagers
        final GeoServerPagingNavigator topPager = new GeoServerPagingNavigator("topNav", layers);
        final GeoServerPagingNavigator bottomPager = new GeoServerPagingNavigator("bottomNav", layers);
        add(topPager);
        add(bottomPager);
        if(layers.size() < MAX_ROWS) {
            topPager.setVisible(false);
            bottomPager.setVisible(false);
        }
    }

    /**
     * Builds a fake GetMap request 
     * @param prefixedName
     * @return
     */
    protected GetMapRequest buildFakeGetMap(String prefixedName) {
        final WMS wms = getGeoServerApplication().getBeanOfType(WMS.class);
        GetMapRequest gm = new GetMapRequest(wms);
        Data catalog = (Data) getGeoServerApplication().getBean("data");
        gm.setLayers(expandLayers(prefixedName, catalog));
        gm.setFormat("application/openlayers");
        return gm;
    }

    /**
     * Expands the specified name into a list of layer info names
     * @param prefixedName
     * @param catalog
     * @return
     */
    private List<MapLayerInfo> expandLayers(String prefixedName, Data catalog) {
        Integer type = catalog.getLayerType(prefixedName);
        List<MapLayerInfo> layers = new ArrayList<MapLayerInfo>();
        if(type == Data.TYPE_VECTOR) {
             layers.add(new MapLayerInfo(catalog.getFeatureTypeInfo(prefixedName)));
        } else if(type == Data.TYPE_RASTER) {
            layers.add(new MapLayerInfo(catalog.getCoverageInfo(prefixedName)));
        } else {
            for(LayerInfo info :getCatalog().getLayerGroupByName(prefixedName).getLayers()) {
                layers.addAll(expandLayers(info.getResource().getPrefixedName(), catalog));
            }
        }
        return layers;
    }

    /**
     * Given a request and a target format, builds the WMS request
     * @param request
     * @param string
     * @return
     */
    protected String buildWmsLinkTemplate(GetMapRequest request) {
        final Envelope bbox = request.getBbox();
        if(bbox == null) {
            System.out.println("No bbox for layer " + request.getLayers()[0].getName());
            return "";
        }
        return "wms?service=WMS&version=1.1.0&request=GetMap" //
               + "&layers=" + request.getLayers()[0].getName() //
               + "&styles=" //
               + "&format=${format}" //
               + "&bbox=" + bbox.getMinX() + "," + bbox.getMinY() //
               + "," + bbox.getMaxX() + "," + bbox.getMaxY() //
               + "&width=" + request.getWidth() //
               + "&height=" + request.getHeight()
               + "&srs=" + request.getSRS();
    }

    private List<String> getAvailableFormats(){
        List<String> formats = new ArrayList<String>();

        for (GetMapProducerFactorySpi spi : 
            getGeoServerApplication().getBeansOfType(GetMapProducerFactorySpi.class)
            ) {
            formats.add((String)spi.getSupportedFormats().iterator().next());
        }
        Collections.sort(formats);

        return formats;
    }
}
