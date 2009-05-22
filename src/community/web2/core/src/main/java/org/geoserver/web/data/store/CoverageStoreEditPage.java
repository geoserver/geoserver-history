/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;

import org.apache.wicket.markup.html.form.Form;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * Supports coverage store configuration
 * 
 * @author Andrea Aime
 */
public class CoverageStoreEditPage extends AbstractCoverageStorePage {
    Form paramsForm;

    /**
     * 
     * @param storeId
     *            the store id
     */
    public CoverageStoreEditPage(final String storeId) throws IllegalArgumentException {
        Catalog catalog = getCatalog();
        CoverageStoreInfo store = catalog.getCoverageStore(storeId);
        if (store == null) {
            throw new IllegalArgumentException("Cannot find coverage store " + storeId);
        }

        initUI(store);
    }

    @Override
    protected final void onSave(final CoverageStoreInfo info) throws IllegalArgumentException {
        final Catalog catalog = getCatalog();

        final String url = info.getURL();
        Map<String, Serializable> connectionParameters = info.getConnectionParameters();

        Format format = GridFormatFinder.findFormat(url);
        if (format instanceof UnknownFormat) {
            throw new IllegalArgumentException(
                    "No grid format found capable of connection to the provided URL");
        }
        if (!(format instanceof AbstractGridFormat)) {
            throw new IllegalArgumentException("Unknown format type: "
                    + format.getClass().getName());
        }
        AbstractGridFormat gridFormat = (AbstractGridFormat) format;
        GridCoverageReader reader;
        try {
            reader = gridFormat.getReader(url);
        } catch (RuntimeException e) {
            LOGGER
                    .log(Level.INFO, "Editing CoverageStore " + info.getName() + ": <" + url + ">",
                            e);
            throw new IllegalArgumentException("Unable to access the coverage. Original error: "
                    + e.getMessage());
        }
        if (reader == null) {
            throw new IllegalArgumentException(
                    "Can't obtain a coverage reader for the provided URL");
        }
        try {
            reader.dispose();
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Closing test reader for " + url, e);
        }

        try {
            catalog.save(info);
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Saving the store for " + url, e);
            throw new IllegalArgumentException("Unable to save the store: " + e.getMessage());
        }

        setResponsePage(StorePage.class);
    }

}
