/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.CRSUtilities;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.util.CoverageStoreUtils;
import org.vfny.geoserver.util.CoverageUtils;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class CoverageInfo extends GlobalLayerSupertype {
    /**
     *
     */
    private String formatId;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String real_name;

    /**
     *
     */
    private String wmsPath;

    /**
     *
     */
    private String label;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private MetaDataLink metadataLink;

    /**
     *
     */
    private String dirName;

    /**
     *
     */
    private List keywords;

    /**
     *
     */
    private GeneralEnvelope envelope;

    /**
     *
     */
    private GeneralEnvelope lonLatWGS84Envelope;

    /**
     *
     */
    private GridGeometry grid;

    /**
     *
     */
    private CoverageDimension[] dimensions;

    /**
     *
     */
    private InternationalString[] dimensionNames;

    /**
     *
     */
    private List requestCRSs;

    /**
     *
     */
    private List responseCRSs;

    /**
     *
     */
    private String nativeFormat;

    /**
     *
     */
    private List supportedFormats;

    /**
     *
     */
    private String defaultInterpolationMethod;

    /**
     *
     */
    private List interpolationMethods;

    /**
     *
     */
    private Data data;

    /**
     *
     */
    private Map meta;

    /**
     *
     */
    private String srsName;

    /**
     *
     */
    private String srsWKT;

    /**
     *
     */
    private CoordinateReferenceSystem crs;

    /**
     * Default style used to render this Coverage with WMS
     */
    private String defaultStyle;

    /**
     * Other WMS Styles
     */
    private ArrayList styles;

    /**
     * String representation of connection parameter values
     */
    private Map parameters;
    private boolean NDimensionalCoverage;
    private Map verticalExtent;
    private Map temporalExtent;

    public CoverageInfo(CoverageInfoDTO dto, Data data)
        throws ConfigurationException {
        this.data = data;
        formatId = dto.getFormatId();
        name = dto.getName();
        real_name = dto.getRealName();
        wmsPath = dto.getWmsPath();
        label = dto.getLabel();
        description = dto.getDescription();
        metadataLink = dto.getMetadataLink();
        dirName = dto.getDirName();
        keywords = dto.getKeywords();
        crs = dto.getCrs();
        srsName = dto.getSrsName();
        srsWKT = dto.getSrsWKT();
        envelope = dto.getEnvelope();
        verticalExtent = dto.getVerticalExtent();
        temporalExtent = dto.getTemporalExtent();
        lonLatWGS84Envelope = dto.getLonLatWGS84Envelope();
        grid = dto.getGrid();
        NDimensionalCoverage = dto.isNDimensionalCoverage();
        dimensions = dto.getDimensions();
        dimensionNames = dto.getDimensionNames();
        requestCRSs = dto.getRequestCRSs();
        responseCRSs = dto.getResponseCRSs();
        nativeFormat = dto.getNativeFormat();
        supportedFormats = dto.getSupportedFormats();
        defaultInterpolationMethod = dto.getDefaultInterpolationMethod();
        interpolationMethods = dto.getInterpolationMethods();
        defaultStyle = dto.getDefaultStyle();
        styles = dto.getStyles();
        parameters = dto.getParameters();
    }

    Object toDTO() {
        CoverageInfoDTO dto = new CoverageInfoDTO();

        dto.setFormatId(formatId);
        dto.setName(name);
        dto.setRealName(real_name);
        dto.setWmsPath(wmsPath);
        dto.setLabel(label);
        dto.setDescription(description);
        dto.setMetadataLink(metadataLink);
        dto.setDirName(dirName);
        dto.setKeywords(keywords);
        dto.setCrs(crs);
        dto.setSrsName(srsName);
        dto.setSrsWKT(srsWKT);
        dto.setEnvelope(envelope);
        dto.setVerticalExtent(verticalExtent);
        dto.setTemporalExtent(temporalExtent);
        dto.setLonLatWGS84Envelope(lonLatWGS84Envelope);
        dto.setGrid(grid);
        dto.setNDimensionalCoverage(NDimensionalCoverage);
        dto.setDimensions(dimensions);
        dto.setDimensionNames(dimensionNames);
        dto.setRequestCRSs(requestCRSs);
        dto.setResponseCRSs(responseCRSs);
        dto.setNativeFormat(nativeFormat);
        dto.setSupportedFormats(supportedFormats);
        dto.setDefaultInterpolationMethod(defaultInterpolationMethod);
        dto.setInterpolationMethods(interpolationMethods);
        dto.setDefaultStyle(defaultStyle);
        dto.setStyles(styles);
        dto.setParameters(parameters);

        return dto;
    }

    public CoverageStoreInfo getFormatInfo() {
        return data.getFormatInfo(formatId);
    }

    public boolean isEnabled() {
        return (getFormatInfo() != null) && (getFormatInfo().isEnabled());
    }

    public CoverageStoreInfo getFormatMetaData() {
        return data.getFormatInfo(formatId);
    }

    public boolean containsMetaData(String key) {
        return meta.containsKey(key);
    }

    public void putMetaData(String key, Object value) {
        meta.put(key, value);
    }

    public Object getMetaData(String key) {
        return meta.get(key);
    }

    /**
     * @return Returns the data.
     */
    public Data getData() {
        return data;
    }

    /**
     * @return Returns the defaultInterpolationMethod.
     */
    public String getDefaultInterpolationMethod() {
        return defaultInterpolationMethod;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Returns the dirName.
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * @return Returns the envelope.
     */
    public GeneralEnvelope getEnvelope() {
        return envelope;
    }

    /**
     * @return Returns the formatId.
     */
    public String getFormatId() {
        return formatId;
    }

    /**
     * @return Returns the interpolationMethods.
     */
    public List getInterpolationMethods() {
        return interpolationMethods;
    }

    /**
     * @return Returns the keywords.
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return Returns the meta.
     */
    public Map getMeta() {
        return meta;
    }

    /**
     * @return Returns the metadataLink.
     */
    public MetaDataLink getMetadataLink() {
        return metadataLink;
    }

    /**
     * @return String the namespace prefix.
     */
    public String getPrefix() {
        CoverageStoreInfo info = getFormatInfo();

        if (info != null) {
            return info.getNameSpace().getPrefix();
        }

        return null;
    }

    /**
     * @return NameSpaceInfo the namespace specified for the specified
     *         CoverageStoreInfo (by ID)
     *
     * @throws IllegalStateException
     *             Thrown when disabled.
     */
    public NameSpaceInfo getNameSpace() {
        if (!isEnabled()) {
            throw new IllegalStateException("This coverage is not " + "enabled");
        }

        return getFormatInfo().getNameSpace();
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return getPrefix() + ":" + name;
    }

    /**
     * @return Returns the nativeFormat.
     */
    public String getNativeFormat() {
        return nativeFormat;
    }

    /**
     * @return Returns the requestCRSs.
     */
    public List getRequestCRSs() {
        return requestCRSs;
    }

    /**
     * @return Returns the responseCRSs.
     */
    public List getResponseCRSs() {
        return responseCRSs;
    }

    /**
     * @return Returns the srsName.
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * @return Returns the supportedFormats.
     */
    public List getSupportedFormats() {
        return supportedFormats;
    }

    /**
     * By now just return the default style to be able to declare it in WMS
     * capabilities, but all this stuff needs to be revisited since it seems
     * currently there is no way of retrieving all the styles declared for a
     * given Coverage.
     *
     * @return the default Style for the Coverage
     */
    public Style getDefaultStyle() {
        return data.getStyle(defaultStyle);
    }

    public ArrayList getStyles() {
        final ArrayList realStyles = new ArrayList();
        Iterator s_IT = styles.iterator();

        while (s_IT.hasNext())
            realStyles.add(data.getStyle((String) s_IT.next()));

        return realStyles;
    }

    /**
     *
     */
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }

    /**
     *
     */
    public GridGeometry getGrid() {
        return grid;
    }

    /**
     *
     */
    public InternationalString[] getDimensionNames() {
        return dimensionNames;
    }

    /**
     * @return Returns the dimensions.
     */
    public CoverageDimension[] getDimensions() {
        return dimensions;
    }

    public String getSrsWKT() {
        return srsWKT;
    }

    public GeneralEnvelope getWGS84LonLatEnvelope() {
        if (this.lonLatWGS84Envelope == null) {
            try {
                this.lonLatWGS84Envelope = CoverageStoreUtils
                    .getWGS84LonLatEnvelope(this.envelope);
            } catch (IndexOutOfBoundsException e) {
                return null;
            } catch (FactoryException e) {
                return null;
            } catch (TransformException e) {
                return null;
            }
        }

        return this.lonLatWGS84Envelope;
    }

    public String getWmsPath() {
        return wmsPath;
    }

    public void setWmsPath(String wmsPath) {
        this.wmsPath = wmsPath;
    }

    public GridCoverageReader getReader() {
        // /////////////////////////////////////////////////////////
        //
        // Getting coverage config and then reader
        //
        // /////////////////////////////////////////////////////////
        return data.getFormatInfo(formatId).getReader();
    }

    public GridCoverageReader createReader(Hints hints) {
        // /////////////////////////////////////////////////////////
        //
        // Getting coverage config and then reader
        //
        // /////////////////////////////////////////////////////////
        return data.getFormatInfo(formatId).createReader(hints);
    }

    public Map getParameters() {
        return parameters;
    }

    public GridCoverage getCoverage() {
        return getCoverage(null, null);
    }

    public GridCoverage getCoverage(GeneralEnvelope envelope, Rectangle dim) {
        GridCoverage gc = null;

        try {
            if (envelope == null) {
                envelope = this.envelope;
            }

            // /////////////////////////////////////////////////////////
            //
            // Do we need to proceed?
            // I need to check the requested envelope in order to see if the
            // coverage we ask intersect it otherwise it is pointless to load it
            // since its reader might return null;
            // /////////////////////////////////////////////////////////
            final CoordinateReferenceSystem sourceCRS = envelope
                .getCoordinateReferenceSystem();
            final CoordinateReferenceSystem destCRS = crs;

            if (!CRSUtilities.equalsIgnoreMetadata(sourceCRS, destCRS)) {
                // get a math transform
                final MathTransform transform = CoverageUtils.getMathTransform(sourceCRS,
                        destCRS);

                // transform the envelope
                if (!transform.isIdentity()) {
                    envelope = CRSUtilities.transform(transform, envelope);
                }
            }

            // just do the intersection since
            envelope.intersect(this.envelope);

            if (envelope.isEmpty()) {
                return null;
            }

            envelope.setCoordinateReferenceSystem(destCRS);

            // /////////////////////////////////////////////////////////
            //
            // get a reader
            //
            // /////////////////////////////////////////////////////////
            final GridCoverageReader reader = getReader();

            if (reader == null) {
                return null;
            }

            // /////////////////////////////////////////////////////////
            //
            // Reading the coverage
            //
            // /////////////////////////////////////////////////////////
            gc = reader.read(CoverageUtils.getParameters(
                        getReader().getFormat().getReadParameters(),
                        getParameters()));

            if ((gc == null) || !(gc instanceof GridCoverage2D)) {
                throw new IOException(
                    "The requested coverage could not be found.");
            }
        } catch (InvalidParameterValueException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (ParameterNotFoundException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (SecurityException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (TransformException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return gc;
    }

    public String getRealName() {
        return real_name;
    }

    public boolean isNDimensionalCoverage() {
        return NDimensionalCoverage;
    }

    /**
     * @return the temporalExtent
     */
    public Map getTemporalExtent() {
        return temporalExtent;
    }

    /**
     * @return the verticalExtent
     */
    public Map getVerticalExtent() {
        return verticalExtent;
    }
}
