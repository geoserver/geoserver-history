/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.crs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.vfny.geoserver.global.GeoserverDataDirectory;


public class CustomCRSAuthorityFactory extends EPSGCRSAuthorityFactory {
    private static final Logger LOGGER = Logger.getLogger(
            "org.geotools.referencing.crs.EPSGCRSAuthorityFactory");
    public static final String SYSTEM_DEFAULT_USER_PROJ_FILE = "user.projections.file";

    public CustomCRSAuthorityFactory() {
        try {
            loadDefault();
        } catch (IOException e) {
            System.err.println("Could not load users's projection file");
        }
    }

    protected void loadDefault() throws IOException {
        LOGGER.log(Level.INFO,
            "From CustomCRSAuthorityFactory: current directory is: "
            + System.getProperty("user.dir"));

        //Get user-defined custom projections file:
        String cust_proj_file;
        File proj_file;

        try {
            cust_proj_file = System.getProperty(SYSTEM_DEFAULT_USER_PROJ_FILE);

            if (cust_proj_file == null) {
                cust_proj_file = new File(GeoserverDataDirectory.getGeoserverDataDirectory(),
                        "user_projections/epsg.properties").getAbsolutePath();
            }

            //Attempt to load user-defined projections
            proj_file = new File(cust_proj_file);

            if (proj_file.exists()) {
                LOGGER.log(Level.INFO,
                    "From CustomCRSAuthorityFactory: custom projections file is " + cust_proj_file);
                epsg.load(new FileInputStream(proj_file));
            } else {
                LOGGER.log(Level.WARNING,
                    "Custom projections file " + cust_proj_file + " doesn't exist");
            }
        } catch (NullPointerException e) {
            LOGGER.log(Level.WARNING,
                "From CustomCRSAuthorityFactory: " + SYSTEM_DEFAULT_USER_PROJ_FILE
                + " variable not defined, " + "will use archive's default... ");
            cust_proj_file = "user_epsg.properties";

            //Use the built-in property defintions
            URL url = EPSGCRSAuthorityFactory.class.getResource(cust_proj_file);
            epsg.load(url.openStream());
        }
    }
}
