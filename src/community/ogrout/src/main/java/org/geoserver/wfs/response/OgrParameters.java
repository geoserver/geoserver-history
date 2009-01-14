/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.response;

/**
 * Parameters defining an output format generated using ogr2ogr from
 * either a GML or shapefile dump 
 * @author Andrea Aime - OpenGeo
 *
 */
public class OgrParameters {
    /**
     * The -f parameter
     */
    public String ogrFormat;
    
    /**
     * The GeoServer output format name
     */
    public String formatName;
    
    /**
     * The extension of the generated file, if any (shall include a dot, example, ".tab")
     */
    public String fileExtension;
    
    /**
     * The options that will be added to the command line
     */
    public String[] options;

    public OgrParameters(String ogrFormat, String formatName, String fileExtension, String[] options) {
        super();
        this.ogrFormat = ogrFormat;
        this.formatName = formatName;
        this.fileExtension = fileExtension;
        this.options = options;
    }

    
}
