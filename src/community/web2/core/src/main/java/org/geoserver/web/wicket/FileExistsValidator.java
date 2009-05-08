/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import java.io.File;
import java.net.URI;
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
        String path = Converters.convert(validatable.getValue(), String.class);
        
        try {
            // Make sure we are dealing with a local path
            URI uri = new URI(path);
            if(uri.getScheme() != null && !"file".equals(uri.getScheme()))
                return;
            
            // ok, strip away the scheme and just get to the path
            path = uri.getPath();
            
            // absolute path?
            File absFile = new File(path);
            if(!absFile.exists()) {
                // local to data dir?
                File relFile = GeoserverDataDirectory.findDataFile(path);
                if(relFile == null || !relFile.exists()) {
                    error(validatable, "FileExistsValidator.fileNotFoundError", 
                            Collections.singletonMap("file", path));
                }
            }
        } catch(Exception e) {
            error(validatable, "FileExistsValidator.invalidPathError", 
                    Collections.singletonMap("path", path));
        }
        
    }

}
