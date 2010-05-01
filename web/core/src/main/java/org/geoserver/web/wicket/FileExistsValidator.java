/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.geotools.util.Converters;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Checks the specified file exists on the file system, including checks in the data directory
 */
@SuppressWarnings("serial")
public class FileExistsValidator extends AbstractValidator {
    
    @Override
    protected void onValidate(IValidatable validatable) {
        String uriSpec = Converters.convert(validatable.getValue(), String.class);
        
        // Make sure we are dealing with a local path
        try {
            URI uri = new URI(uriSpec);
            if(uri.getScheme() != null && !"file".equals(uri.getScheme()))
                return;
            
            // ok, strip away the scheme and just get to the path
            String path = uri.getPath();
            if(path != null && new File(path).exists())
                return;
        } catch(URISyntaxException e) {
            // may be a windows path, move on               
        }
        
        // local to data dir?
        File relFile = GeoserverDataDirectory.findDataFile(uriSpec);
        if(relFile == null || !relFile.exists()) {
            error(validatable, "FileExistsValidator.fileNotFoundError", 
                    Collections.singletonMap("file", uriSpec));
        }
    }

}