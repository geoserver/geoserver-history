/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.util.Collections;
import java.util.List;

/**
 * Placeholder nodes are actually not part of the Catalog, but tree items that
 * do appear in order to help you manipulate the tree, such as embedded commands
 * (new datastore, new workspace) or place holders. Usually their model is the
 * same as the parent object.
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public abstract class PlaceholderNode extends CatalogNode {

    public PlaceholderNode(String id, CatalogNode parent) {
        super(id, parent);
    }

    /**
     * Overrides equals so that it does not clash with the ones in Catalog
     */
    public boolean equals(Object obj) {
        return this == obj;
    }

    /**
     * Overrides hash code so that it does not clash with the ones in Catalog
     */
    public int hashCode() {
        return 17 + 3 * getModel().hashCode();
    }

    /**
     * Placeholders are usually child-less
     */
    @Override
    protected List<CatalogNode> buildChildNodes() {
        return Collections.emptyList();
    }

    /**
     * And most often (but now always) not selectable
     */
    @Override
    public boolean isSelectable() {
        return false;
    }

}
