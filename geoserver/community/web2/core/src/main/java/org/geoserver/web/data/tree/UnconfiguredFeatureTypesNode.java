/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;

public class UnconfiguredFeatureTypesNode extends AbstractPlaceholderNode {

    public UnconfiguredFeatureTypesNode(String id, AbstractCatalogNode parent) {
        super(id, parent);
    }

    @Override
    protected DataStoreInfo getModel() {
        return getCatalog().getDataStoreByName(name);
    }

    @Override
    protected String getNodeLabel() {
        try {
            Set<String> typeNames = new HashSet<String>(Arrays
                    .asList(getModel().getDataStore(null).getTypeNames()));
            for (FeatureTypeInfo ft : getCatalog().getFeatureTypesByStore(
                    getModel())) {
                typeNames.remove(ft.getName());
            }
            return typeNames.size() + " unconfigured layers ...";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error trying to compute unconfigured types");
        }

        return "Show unconfigured layers";
    }
}
