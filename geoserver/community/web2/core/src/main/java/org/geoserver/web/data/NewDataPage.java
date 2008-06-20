package org.geoserver.web.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFinder;
import org.geotools.factory.Hints;
import org.vfny.geoserver.util.DataStoreUtils;

public class NewDataPage extends GeoServerBasePage {

    private final String workspaceId;

    public NewDataPage(final String workspaceId) {
        this.workspaceId = workspaceId;

        final List<String> availableDataStores = getAvailableDataStoreNames();
        final List<String> availableCoverageStores = getAvailableCoverageStoreNames();

        final ListView dataStoreLinks = new ListView("vectorResources", availableDataStores) {
            @Override
            protected void populateItem(ListItem item) {
                final String dataStoreFactoryName = item.getModelObjectAsString();
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
                String description = DataStoreUtils.aquireFactory(dataStoreFactoryName).getDescription();
                item.add(new Label("resourceDescription", description));
            }
        };
        
        final ListView coverageLinks = new ListView("rasterResources", availableCoverageStores) {
            @Override
            protected void populateItem(ListItem item) {
                final String coverageFactoryName = item.getModelObjectAsString();
                Link link;
                link = new Link("resourcelink") {
                    @Override
                    public void onClick() {
                        //TODO
                    }
                };
                link.add(new Label("resourcelabel", coverageFactoryName));
                item.add(link);
                String description = " TODO: coverage description goes here...";
                item.add(new Label("resourceDescription", description));
            }
        };
        
        add(dataStoreLinks);
        add(coverageLinks);
    }

    private List<String> getAvailableDataStoreNames() {
        final Iterator<DataAccessFactory> availableDataStores;
        availableDataStores = DataAccessFinder.getAvailableDataStores();

        List<String> storeNames = new ArrayList<String>();
        while (availableDataStores.hasNext()) {
            DataAccessFactory factory = availableDataStores.next();
            storeNames.add(factory.getDisplayName());
        }
        Collections.sort(storeNames);
        return storeNames;
    }

    private List<String> getAvailableCoverageStoreNames() {
        final Set<GridCoverageFactory> gridCoverageFactories;
        gridCoverageFactories = CoverageFactoryFinder.getGridCoverageFactories((Hints) null);

        List<String> coverageNames = new ArrayList<String>();
        for (GridCoverageFactory factory : gridCoverageFactories) {
            // TODO
        }

        return Collections.emptyList();
    }

}
