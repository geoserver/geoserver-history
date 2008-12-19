/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.restconfig;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;

/**
 * This class extends Resource to handle the GET and PUT requests to manage the
 * upload of the GeoTIFF files.
 * 
 * @author $Author: Tobia Di Pisa (tobia.dipisa@geo-solutions.it) $ (last
 *         modification)
 * @author $Author: Alessio Fabiani (alessio.fabiani@geo-solutions.it) $ (last
 *         modification)
 */

public class CoverageStoreFileResource extends Resource {

    private DataConfig myDataConfig;

    private Data myData;

    private GeoServer myGeoServer;

    private GlobalConfig myGlobalConfig;

    // //////
    // A map from .xxx file extensions
    // //////

    private static Map<String, String> myFormats = new HashMap<String, String>();
    static {
        final Format[] formats = CoverageStoreUtils.formats;
        for (Format format : formats) {
            myFormats.put(format.getName().toLowerCase(), format.getName());
        }

    }

    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(CoverageStoreFileResource.class);

    private CoverageStoreFileResource() {
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

    public synchronized void handleGet() {
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

    public synchronized void handlePut() {
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
            String method = (String) getRequest().getAttributes().get("method");
            if (method != null && method.equalsIgnoreCase("file"))
                uploadedFile = RESTUtils.handleBinUpload(coverageName, extension, getRequest());
            else if (method != null && method.equalsIgnoreCase("url"))
                uploadedFile = RESTUtils.handleURLUpload(coverageName, extension, getRequest());
            else if (method != null && method.equalsIgnoreCase("external"))
                uploadedFile = RESTUtils.handleEXTERNALUpload(coverageName, extension, getRequest());
            else {
                getResponse().setEntity(new StringRepresentation("Unrecognized upload method: " + method, MediaType.TEXT_PLAIN));
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return;
            }
        } catch (Throwable t) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, t.getLocalizedMessage(), t);
            getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + t,MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // Add overviews to the Coverage
        //
        // /////////////////////////////////////////////////////////////////////
        if (form.getFirst("overviews") != null && form.getFirstValue("overviews").equalsIgnoreCase("yes"))
            /* TODO: Add overviews here */;

        try {
            final File outputDir = RESTUtils.unpackZippedDataset(coverageStore, uploadedFile);
            if (outputDir != null && outputDir.exists() && outputDir.canRead()) {
                final File[] files = outputDir.listFiles();
                boolean found = false;
                for (File file : files) {
                    if ((((AbstractGridFormat) format)).accepts(file)) {
                        uploadedFile = file;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    getResponse().setEntity(new StringRepresentation("Failure while setting up datastore: Unable to handle source in zip file", MediaType.TEXT_PLAIN));
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return;
                }
            }

        } catch (Exception mue) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, mue.getLocalizedMessage(), mue);
            getResponse().setEntity(new StringRepresentation("Failure while setting up datastore: " + mue, MediaType.TEXT_PLAIN));
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
            String qualified = realCoverageStore + ":" + realCoverageName;

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
                    if (LOGGER.isLoggable(Level.SEVERE))
                        LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + e, MediaType.TEXT_PLAIN));
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return;
                }

                // //////
                // TODO: something better exists, I hope
                // //////

                csc.setAbstract("Autoconfigured by RESTConfig");

                myDataConfig.addDataFormat(csc);
            } else {
                try {
                    csc.setUrl(uploadedFile.toURL().toExternalForm() + (c > 0 ? ":" + c : ""));
                } catch (MalformedURLException e) {
                    if (LOGGER.isLoggable(Level.SEVERE))
                        LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + e, MediaType.TEXT_PLAIN));
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return;
                }

                myDataConfig.removeCoverage(qualified);
            }

            // //////
            // Configuration for the coverage
            // //////
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

                    if ("UNKNOWN".equals(cc.getUserDefinedCrsIdentifier())) {
                        CoordinateReferenceSystem sourceCRS = cc.getEnvelope().getCoordinateReferenceSystem();
                        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326", true);

                        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
                        GeneralEnvelope envelope = CRS.transform(transform, cc.getEnvelope());
                        envelope.setCoordinateReferenceSystem(targetCRS);

                        cc.setUserDefinedCrsIdentifier("EPSG:4326");
                        // cc.setCrs(targetCRS);
                        cc.setEnvelope(envelope);
                    }

                    List requestResponseCRSs = new ArrayList();
                    requestResponseCRSs.add(cc.getUserDefinedCrsIdentifier());

                    cc.setRequestCRSs(requestResponseCRSs);
                    cc.setResponseCRSs(requestResponseCRSs);

                    if (form.getFirst("style") != null)
                        cc.setDefaultStyle(form.getFirstValue("style"));

                    if (form.getFirst("wmspath") != null)
                        cc.setWmsPath(form.getFirstValue("wmspath"));

                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE))
                        LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
                    getResponse().setEntity(new StringRepresentation("Failure while saving configuration: " + e, MediaType.TEXT_PLAIN));
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    return;
                }

            }

            qualified = realCoverageStore + ":" + cc.getName();
            myDataConfig.removeCoverage(qualified);
            myDataConfig.addCoverage(qualified, cc);
        }

        save();

        getResponse().setEntity(new StringRepresentation("Handling PUT on a CoverageStoreFileResource", MediaType.TEXT_PLAIN));
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    /**
     * This function manages the save of the configuration for the CoverageStore
     * and the Coverage
     */

    private void save() {
        try {
            RESTUtils.saveConfiguration(getDataConfig(), getData());
        } catch (Exception e) {
            getResponse().setEntity(new StringRepresentation("Failure while saving configuration: " + e, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }
    }

    /**
     * @return the myFormats
     */
    public static Map getAllowedFormats() {
        return myFormats;
    }

}