/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipOutputStream;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;

import org.geoserver.config.GeoServer;
import org.geoserver.data.util.IOUtils;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gml.producer.FeatureTransformer;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class Ogr2OgrOutputFormat extends WFSGetFeatureOutputFormat {
    
    /**
     * The fs path to ogr2ogr. If null, we'll assume ogr2ogr is in the PATH and
     * that we can execute it just by running ogr2ogr
     */
    String ogrPath = null;

    /**
     * The full path to ogr2ogr
     */
    String ogrExecutable = "ogr2ogr";
    
    /**
     * The GDAL_DATA folder
     */
    String gdalData = null;

    /**
     * The output formats we can generate using ogr2ogr. Using a concurrent
     * one so that it can be reconfigured while the output format is working
     */
    static Map<String, OgrFormat> formats = new ConcurrentHashMap<String, OgrFormat>();

    public Ogr2OgrOutputFormat(GeoServer gs) {
        // initialize with the key set of formats, so that it will change as
        // we register new formats
        super(gs, formats.keySet());
    }

    /**
     * Returns the ogr2ogr executable full path
     * 
     * @return
     */
    public String getOgrExecutable() {
        return ogrExecutable;
    }

    /**
     * Sets the ogr2ogr executable full path. The default value is simply
     * "ogr2ogr", which will work if ogr2ogr is in the path
     * 
     * @param ogrExecutable
     */
    public void setOgrExecutable(String ogrExecutable) {
        this.ogrExecutable = ogrExecutable;
    }
    
    /**
     * Returns the location of the gdal data folder (required to set the output srs)
     * @return
     */
    public String getGdalData() {
        return gdalData;
    }

    /**
     * Sets the location of the gdal data folder (requierd to set the output srs)
     * @param gdalData
     */
    public void setGdalData(String gdalData) {
        this.gdalData = gdalData;
    }

    /**
     * @see WFSGetFeatureOutputFormat#getMimeType(Object, Operation)
     */
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        GetFeatureType request = (GetFeatureType) OwsUtils.parameter(operation.getParameters(),
                GetFeatureType.class);

        OgrFormat format = formats.get(request.getOutputFormat());
        if (format == null) {
            throw new WFSException("Unknown output format " + request.getOutputFormat());
        } else if (format.singleFile && request.getQuery().size() <= 1) {
            if(format.mimeType != null) {
                return format.mimeType;
            } else {
                // use a default binary blob
                return "application/octet-stream";
            }
        } else {
            return "application/zip";
        }
    }
    
    @Override
    public String[][] getHeaders(Object value, Operation operation) throws ServiceException {
        GetFeatureType request = (GetFeatureType) OwsUtils.parameter(operation.getParameters(),
                GetFeatureType.class);

        OgrFormat format = formats.get(request.getOutputFormat());
        if (format == null) {
            throw new WFSException("Unknown output format " + request.getOutputFormat());
        } else if (!format.singleFile || request.getQuery().size() > 1) {
            String outputFileName = ((QName) ((QueryType) request.getQuery().get(0)).getTypeName()
                    .get(0)).getLocalPart();
            return (String[][]) new String[][] { { "Content-Disposition",
                    "attachment; filename=" + outputFileName + ".zip" } };
        } else {
            return null;
        }
    }

    /**
     * Adds a ogr format among the supported ones
     * 
     * @param parameters
     */
    public void addFormat(OgrFormat parameters) {
        formats.put(parameters.formatName, parameters);
    }

    /**
     * Programmatically removes all formats
     * 
     * @param parameters
     */
    public void clearFormats() {
        formats.clear();
    }

    /**
     * Writes out the data to an OGR known format (GML/shapefile) to disk and
     * then ogr2ogr each generated file into the destination format. Finally,
     * zips up all the resulting files.
     */
    @Override
    protected void write(FeatureCollectionType featureCollection, OutputStream output,
            Operation getFeature) throws IOException, ServiceException {

        // figure out which output format we're going to generate
        GetFeatureType gft = (GetFeatureType) getFeature.getParameters()[0];
        OgrFormat format = formats.get(gft.getOutputFormat());
        if (format == null)
            throw new WFSException("Unknown output format " + gft.getOutputFormat());

        // create the first temp directory, used for dumping gs generated
        // content
        File tempGS = org.geoserver.data.util.IOUtils.createTempDirectory("ogrtmpin");
        File tempOGR = org.geoserver.data.util.IOUtils.createTempDirectory("ogrtmpout");

        // build the ogr wrapper used to run the ogr2ogr commands
        OGRWrapper wrapper = new OGRWrapper(ogrExecutable, gdalData);

        // actually export each feature collection
        try {
            Iterator outputFeatureCollections = featureCollection.getFeature().iterator();
            SimpleFeatureCollection curCollection;

            File outputFile = null;
            while (outputFeatureCollections.hasNext()) {
                curCollection = (SimpleFeatureCollection) outputFeatureCollections
                        .next();

                // write out the gml
                File intermediate = writeToDisk(tempGS, curCollection);

                // convert with ogr2ogr
                String epsgCode = null;
                final SimpleFeatureType schema = curCollection.getSchema();
                final CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();
                outputFile = wrapper.convert(intermediate, tempOGR, schema.getTypeName(), format, crs);

                // wipe out the input dir contents
                IOUtils.emptyDirectory(tempGS);
            }
            
            // was is a single file output?
            if(format.singleFile && featureCollection.getFeature().size() == 1) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(outputFile);
                    org.apache.commons.io.IOUtils.copy(fis, output);
                } finally {
                    if(fis != null) {
                        fis.close();
                    }
                }
            } else {
                // scan the output directory and zip it all
                ZipOutputStream zipOut = new ZipOutputStream(output);
                IOUtils.zipDirectory(tempOGR, zipOut, null);
                zipOut.flush();
            }

            // delete the input and output directories
            IOUtils.delete(tempGS);
            IOUtils.delete(tempOGR);
        } catch (Exception e) {
            throw new ServiceException("Exception occurred during output generation", e);
        }
    }
    
    /**
     * Writes to disk using shapefile if the feature type allows for it, GML otherwise
     * @param tempDir
     * @param curCollection
     * @return
     */
    private File writeToDisk(File tempDir,
            SimpleFeatureCollection curCollection) throws Exception {
        // create the temp file for this output
        File outFile = new File(tempDir, curCollection.getSchema().getTypeName() + ".gml");

        // write out
        OutputStream os = null;
        try {
            os = new FileOutputStream(outFile);

            // let's invoke the transformer
            FeatureTransformer ft = new FeatureTransformer();
            ft.setNumDecimals(16);
            ft.setNamespaceDeclarationEnabled(false);
            ft.getFeatureNamespaces().declarePrefix("topp",
                    curCollection.getSchema().getName().getNamespaceURI());
            ft.transform(curCollection, os);
        } finally {
            os.close();
        }

        return outFile;
    }

}