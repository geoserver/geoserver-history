/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.catalog.StoreInfo;

/**
 * Base class for panels containing the form edit components for {@link StoreInfo} objects
 * 
 * @author Gabriel Roldan
 * @see DefaultCoverageStoreEditPanel
 */
public abstract class StoreEditPanel extends Panel {

    private static final long serialVersionUID = 1L;

    protected final Form storeEditForm;

    protected StoreEditPanel(final String componentId, final Form storeEditForm) {
        super(componentId);
        this.storeEditForm = storeEditForm;
    }

}
