/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Utility class for common operations used by forms.
 */
public class FormUtils {
    public static ActionErrors checkFileExistsAndCanRead(File fileCheck, ActionErrors errors) {
        final String path = fileCheck.getAbsolutePath();
        final int imageSpecifierIndex = path.lastIndexOf(":");
        final File file;
        if (path.length()-imageSpecifierIndex+1<=3 && path.length()-imageSpecifierIndex+1>=0){
            file = new File(path.substring(0,imageSpecifierIndex));
        }
        else file = fileCheck;
        
        if (!file.exists()) {
            String actionKey = "error.file.NotExists";
            Object[] params = new Object[] { file };
            errors.add("URL", new ActionMessage(actionKey, params));

            return errors;
        }

        //check if we can read it.  For some reason file.canRead() doesn't work
        try {
            FileInputStream in = new FileInputStream(file);
            in.read();
            in.close();
        } catch (IOException ioe) {
            String actionKey = "error.file.CantRead";
            Object[] params = new Object[] { file };
            errors.add("URL", new ActionMessage(actionKey, params));
        }

        return errors;
    }
}
