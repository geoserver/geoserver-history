/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket.browser;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * Returns only visible files whose name ends with the specified extension
 * @author Andrea Aime
 */
@SuppressWarnings("serial")
public class ExtensionFileFilter implements FileFilter, Serializable {
    String extension;

    /**
     * Builds a file filter for the specified extension
     * @param extension an extension, e.g., ".txt"
     */
    public ExtensionFileFilter(String extension) {
        this.extension = extension.toUpperCase();
    }

    public boolean accept(File pathname) {
        if(pathname.isFile()) {
            return pathname.getName().toUpperCase().endsWith(extension);
        }
        if(!pathname.isDirectory())
            return false;
        if(pathname.isHidden())
            return false;
        return true;
    }
    
}
