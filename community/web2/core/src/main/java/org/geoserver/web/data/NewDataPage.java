/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.coverage.RasterCoverageConfiguration;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geoserver.web.data.tree.DataPage;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFinder;
import org.opengis.coverage.grid.Format;

/**
 * Page that presents a list of vector and raster store types available in the
 * classpath in order to choose what kind of data source to create.
 * <p>
 * Meant to be called by {@link DataPage} when about to add a new datastore or
 * coverage.
 * </p>
 * 
 * @author Gabriel Roldan
 */
public class NewDataPage extends GeoServerBasePage {
    
    transient Map<String, DataAccessFactory> dataStores = getAvailableDataStores();
    transient Map<String, Format> coverages = getAvailableCoverageStores();

    /**
     * Creates the page components to present the list of available vector and
     * raster data source types
     * 
     * @param workspaceId
     *                the id of the workspace to attach the new resource store
     *                to.
     */
    public NewDataPage(final String workspaceId) {

        final ArrayList<String> sortedDsNames = new ArrayList<String>(
                dataStores.keySet());
        Collections.sort(sortedDsNames);

        final ListView dataStoreLinks = new ListView("vectorResources",
                sortedDsNames) {
            @Override
            protected void populateItem(ListItem item) {
                final String dataStoreFactoryName = item
                        .getModelObjectAsString();
                final DataAccessFactory factory = dataStores
                        .get(dataStoreFactoryName);
                final String description = factory.getDescription();
                Link link;
                link = new Link("resourcelink") {
                    @Override
                    public void onClick() {
                        setResponsePage(new DataStoreConfiguration(workspaceId,
                                dataStoreFactoryName));
                    }
                };
                link.add(new Label("resourcelabel", dataStoreFactoryName));
                item.add(link);
                item.add(new Label("resourceDescription", description));
                item.add(new Image("storeIcon",
                        getStoreIcon(factory.getClass())));
            }
        };

        final List<String> sortedCoverageNames = new ArrayList<String>(
                coverages.keySet());
        Collections.sort(sortedCoverageNames);

        final ListView coverageLinks = new ListView("rasterResources",
                sortedCoverageNames) {
            @Override
            protected void populateItem(ListItem item) {
                final String coverageFactoryName = item
                        .getModelObjectAsString();
                Format format = coverages.get(coverageFactoryName);
                final String description = format.getDescription();
                Link link;
                link = new Link("resourcelink") {
                    @Override
                    public void onClick() {
                        setResponsePage(new RasterCoverageConfiguration(
                                workspaceId, coverageFactoryName));
                    }
                };
                link.add(new Label("resourcelabel", coverageFactoryName));
                item.add(link);
                item.add(new Label("resourceDescription", description));
                item.add(new Image("storeIcon",
                        getStoreIcon(format.getClass())));
            }
        };

        add(dataStoreLinks);
        add(coverageLinks);
    }

    protected ResourceReference getStoreIcon(Class factoryClass) {
        // look for the associated panel info if there is one
        List<DataStorePanelInfo> infos = getGeoServerApplication()
                .getBeansOfType(DataStorePanelInfo.class);
        for (DataStorePanelInfo panelInfo : infos) {
            if (panelInfo.getFactoryClass().equals(factoryClass))
                return new ResourceReference(panelInfo.getIconBase(), panelInfo
                        .getIcon());
        }

        if (DataAccessFactory.class.isAssignableFrom(factoryClass))
            // fall back on generic vector icon otherwise
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/vector.png");
        else
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/raster.png");

    }

    /**
     * @return the name/description set of available datastore factories
     */
    private Map<String, DataAccessFactory> getAvailableDataStores() {
        final Iterator<DataAccessFactory> availableDataStores;
        availableDataStores = DataAccessFinder.getAvailableDataStores();

        Map<String, DataAccessFactory> storeNames = new HashMap<String, DataAccessFactory>();

        while (availableDataStores.hasNext()) {
            DataAccessFactory factory = availableDataStores.next();
            storeNames.put(factory.getDisplayName(), factory);
        }
        return storeNames;
    }

    /**
     * @return the name/description set of available raster formats
     */
    private Map<String, Format> getAvailableCoverageStores() {
        Format[] availableFormats = GridFormatFinder.getFormatArray();
        Map<String, Format> formatNames = new HashMap<String, Format>();
        for (Format format : availableFormats) {
            formatNames.put(format.getName(), format);
        }
        return formatNames;
    }

}
