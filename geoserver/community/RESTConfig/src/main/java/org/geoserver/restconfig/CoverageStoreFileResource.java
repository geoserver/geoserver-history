/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.restconfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.Format;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

/**
 * This class extends Resource to handle the GET and PUT requests to manage the
 * upload of the GeoTIFF files.
 * 
 * @author $Author: Tobia Di Pisa (ilcovo@gmail.com) $ (last modification)
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 */

public class CoverageStoreFileResource extends Resource {
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	private DataConfig myDataConfig;

    private Data myData;

    private GeoServer myGeoServer;

    private GlobalConfig myGlobalConfig;

    // //////
    // A map from .xxx file extensions
    // //////

    private static Map myFormats = new HashMap();
    static {
        myFormats.put("tiff", "GeoTIFF");
        myFormats.put("hdf4", "HDF-AVHRR");
    }

    Logger log = Logger.getLogger(CoverageStoreFileResource.class);

    private CoverageStoreFileResource() {
        // myFormats = new HashMap();
        // myFormats.put("tiff", "GeoTIFF");
    }

    public CoverageStoreFileResource(Data data, DataConfig dataConfig,
            GeoServer gs, GlobalConfig gc) {
        this();
        setData(data);
        setDataConfig(dataConfig);
        setGeoServer(gs);
        setGlobalConfig(gc);
    }

    public void setDataConfig(DataConfig dc) {
        myDataConfig = dc;
    }

    public DataConfig getDataConfig() {
        return myDataConfig;
    }

    public void setData(Data d) {
        myData = d;
    }

    public Data getData() {
        return myData;
    }

    public void setGeoServer(GeoServer geoserver) {
        myGeoServer = geoserver;
    }

    public GeoServer getGeoServer() {
        return myGeoServer;
    }

    public void setGlobalConfig(GlobalConfig gc) {
        myGlobalConfig = gc;
    }

    public GlobalConfig getGlobalConfig() {
        return myGlobalConfig;
    }

    public boolean allowGet() {
        return true;
    }

    /**
     * This function handles the GET request to verify if the CoverageStore or
     * the Coverage are available.
     */

    public void handleGet() {
        String coverageStore = (String) getRequest().getAttributes().get("folder");
        String coverageName = (String) getRequest().getAttributes().get("layer");
        String qualified = coverageStore + ":" + coverageName;

        CoverageStoreConfig csc = myDataConfig.getDataFormat(coverageStore);
        CoverageConfig cc = (CoverageConfig) myDataConfig.getCoverages().get(qualified);

        if (csc == null || cc == null) {
            getResponse().setEntity(
                    new StringRepresentation("Giving up because coveragestore "
                            + coverageStore + "or" + coverageName
                            + "does not exist.", MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return;
        }

        getResponse().setEntity(
                new StringRepresentation(
                        "Handling GET on a CoverageStoreFileResource",
                        MediaType.TEXT_PLAIN));
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    public boolean allowPut() {
        return true;
    }

    /**
     * This function handles the PUT requests managing the construction of the
     * coverage store and the coverage if necessary.
     */

    public void handlePut() {
        String coverageStore = (String) getRequest().getAttributes().get("folder");
        String coverageName = (String) getRequest().getAttributes().get("layer");
        coverageName = coverageName == null ? coverageStore : coverageName;
        String extension = (String) getRequest().getAttributes().get("type");
        
        Form form = getRequest().getResourceRef().getQueryAsForm();
        
        String formatId = (String) myFormats.get(extension);
        Format format;
        try {
            format = CoverageStoreUtils.acquireFormat(formatId);
        } catch (IOException e) {
            getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + e, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        if (format == null) {
            getResponse().setEntity(new StringRepresentation("Unrecognized extension: " + extension, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }

        File uploadedFile = null;
        try {
            uploadedFile = handleUpload(coverageName, extension, getRequest());
        } catch (Exception e) {
            getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + e, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        int numDataSets = 0;
        AbstractGridCoverage2DReader cvReader;
        if (format instanceof AbstractGridFormat) {
            cvReader = (AbstractGridCoverage2DReader) ((AbstractGridFormat) format).getReader(uploadedFile);

            try {
            	numDataSets = cvReader.getGridCoverageCount();
            } catch (UnsupportedOperationException e) {
            	numDataSets = 1;
            }
        }

        for (int c = 0; c < numDataSets; c++) {
            // //////
            // Configuration for the coverage store
            // //////
            String realCoverageStore = coverageStore + (c > 0 ? "_" + c : "");
            String realCoverageName = coverageName + (c > 0 ? "_" + c : "");
            CoverageStoreConfig csc = myDataConfig.getDataFormat(realCoverageStore);

            if (csc == null) {
                csc = new CoverageStoreConfig(realCoverageStore, format);
                csc.setEnabled(true);
                if (form.getFirst("namespace") != null)
                	csc.setNameSpaceId(form.getFirstValue("namespace"));
                else
                	csc.setNameSpaceId(myDataConfig.getDefaultNameSpace().getPrefix());
                try {
                    csc.setUrl(uploadedFile.toURL().toExternalForm() + (c > 0 ? ":" + c : ""));
                } catch (MalformedURLException e) {
                    getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + e, MediaType.TEXT_PLAIN));
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return;
                }

                // //////
                // TODO: something better exists, I hope
                // //////

                csc.setAbstract("Autoconfigured by RESTConfig");

                myDataConfig.addDataFormat(csc);
            }

            save();

            // //////
            // Configuration for the coverage
            // //////
            String qualified = realCoverageStore + ":" + realCoverageName;

            CoverageConfig cc = (CoverageConfig) myDataConfig.getCoverages().get(qualified);

            if (cc == null) {
                try {
                	AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) ((AbstractGridFormat) format).getReader(new URL(csc.getUrl()));

                	if (reader == null) {
                		getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: Invalid GeoTIFF file!", MediaType.TEXT_PLAIN));
                		getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                		return;
                	}
                    cc = new CoverageConfig(csc.getId(), format, reader, myDataConfig);

                    if ("UNKNOWN".equals(cc.getSrsName())) {
                        CoordinateReferenceSystem sourceCRS = cc.getCrs();
                        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326", true);

                        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
                        GeneralEnvelope envelope = CRS.transform(transform, cc.getEnvelope());
                        envelope.setCoordinateReferenceSystem(targetCRS);

                        cc.setSrsName("EPSG:4326");
                        cc.setCrs(targetCRS);
                        cc.setEnvelope(envelope);
                    }

                    List requestResponseCRSs = new ArrayList();
                    requestResponseCRSs.add(cc.getSrsName());

                    cc.setRequestCRSs(requestResponseCRSs);
                    cc.setResponseCRSs(requestResponseCRSs);
                    
                    if (form.getFirst("style") != null)
                    	cc.setDefaultStyle(form.getFirstValue("style"));
                    else {
                        if (cc.getName().toLowerCase().contains("lowcloud"))
                        	cc.setDefaultStyle("lowcloud");

                        if (cc.getName().toLowerCase().contains("mcsst"))
                        	cc.setDefaultStyle("mcsst");
                    }
                } catch (Exception e) {
                    getResponse().setEntity(new StringRepresentation("Failure while saving configuration: " + e, MediaType.TEXT_PLAIN));
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return;
                }

            }

            myDataConfig.removeCoverage(qualified);
            myDataConfig.addCoverage(qualified, cc);

            save();
        }

        getResponse().setEntity(new StringRepresentation("Handling PUT on a CoverageStoreFileResource", MediaType.TEXT_PLAIN));
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    /**
     * This function gets the stream of the request to copy it into a file.
     * 
     * @param coverageName
     * @param extension
     * @param request
     * @return File
     * @throws IOException
     * @throws ConfigurationException
     */

    private File handleUpload(String coverageName, String extension,
            Request request) throws IOException, ConfigurationException {

        // //////
        // TODO: don't manage the temp file manually, java can take care of it
        // //////

        File dir = GeoserverDataDirectory.findCreateConfigDir("data");
        File tempFile = new File(dir, coverageName + "." + extension + ".tmp");
        File newFile = new File(dir, coverageName + "." + extension);

        BufferedInputStream stream = new BufferedInputStream(request.getEntity().getStream());

        FileOutputStream out = new FileOutputStream(tempFile);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = stream.read(buffer))) {
        	out.write(buffer, 0, n);
            count += n;
        }
        out.flush();
        out.close();

        tempFile.renameTo(newFile);

        return newFile;
    }

    /**
     * This function manages the save of the configuration for the CoverageStore
     * and the Coverage
     */

    private void save() {
        try {
            saveConfiguration();
        } catch (Exception e) {
            getResponse().setEntity(new StringRepresentation("Failure while saving configuration: " + e, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }
    }

    private void saveConfiguration() throws ConfigurationException {
        getData().load(getDataConfig().toDTO());
        XMLConfigWriter.store((DataDTO) getData().toDTO(), GeoserverDataDirectory.getGeoserverDataDirectory());
    }

    /**
     * @return the myFormats
     */
    public static Map getAllowedFormats() {
        return myFormats;
    }
}