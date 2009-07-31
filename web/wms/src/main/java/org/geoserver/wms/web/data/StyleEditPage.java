/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.wicket.WicketRuntimeException;
import org.geoserver.catalog.StyleInfo;

/**
 * Style edit page
 */
public class StyleEditPage extends AbstractStylePage {
    
    public StyleEditPage(StyleInfo style) {
        super(style);
        uploadForm.setVisible(false);
    }

    @Override
    protected void onStyleFormSubmit() {
        // write out the file and save name modifications
        try {
            StyleInfo style = (StyleInfo) styleForm.getModelObject();
            getCatalog().save(style);
            
            // write out the SLD
            try {
                getCatalog().getResourcePool().writeStyle(style,
                        new ByteArrayInputStream(sldEditorPanel.getRawSLD().getBytes()));
            } catch (IOException e) {
                throw new WicketRuntimeException(e);
            }
            setResponsePage( StylePage.class );
        } catch( Exception e ) {
            LOGGER.log(Level.SEVERE, "Error occurred saving the style", e);
            styleForm.error( e );
        }
        
    }
    
}
