/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.AbstractDecorator;
import org.geotools.data.DataStore;
import org.opengis.util.ProgressListener;

/**
 * Delegates every method to the wrapped {@link DataStoreInfo}. Subclasses will
 * override selected methods to perform their "decoration" job
 * 
 * @author Andrea Aime
 */
public class DecoratingDataStoreInfo extends AbstractDecorator<DataStoreInfo> implements
        DataStoreInfo {

    public DecoratingDataStoreInfo(DataStoreInfo delegate) {
        super(delegate);
    }

    public Catalog getCatalog() {
        return delegate.getCatalog();
    }

    public Map<String, Serializable> getConnectionParameters() {
        return delegate.getConnectionParameters();
    }

    public DataStore getDataStore(ProgressListener listener) throws IOException {
        return delegate.getDataStore(listener);
    }

    public String getDescription() {
        return delegate.getDescription();
    }

    public Throwable getError() {
        return delegate.getError();
    }

    public String getId() {
        return delegate.getId();
    }

    public Map<String, Serializable> getMetadata() {
        return delegate.getMetadata();
    }

    public String getName() {
        return delegate.getName();
    }

    public WorkspaceInfo getWorkspace() {
        return delegate.getWorkspace();
    }

    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    public void setDescription(String description) {
        delegate.setDescription(description);
    }

    public void setEnabled(boolean enabled) {
        delegate.setEnabled(enabled);
    }

    public void setError(Throwable t) {
        delegate.setError(t);
    }

    public void setName(String name) {
        delegate.setName(name);
    }

    public void setWorkspace(WorkspaceInfo workspace) {
        delegate.setWorkspace(workspace);
    }

    public <T> T getAdapter(Class<T> adapterClass, Map<?, ?> hints) {
        return delegate.getAdapter(adapterClass, hints);
    }

}
