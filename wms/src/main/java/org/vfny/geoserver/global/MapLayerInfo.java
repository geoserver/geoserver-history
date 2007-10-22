/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.util.DataStoreUtils;
import java.io.IOException;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class MapLayerInfo extends GlobalLayerSupertype {
    public static int TYPE_VECTOR = Data.TYPE_VECTOR.intValue();
    public static int TYPE_RASTER = Data.TYPE_RASTER.intValue();
    public static int TYPE_BASEMAP = Data.TYPE_RASTER.intValue() + 1;
    public static int TYPE_REMOTE_VECTOR = Data.TYPE_RASTER.intValue() + 2;
    

    /**
     *
     * @uml.property name="feature"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private FeatureTypeInfo feature;
    
    /**
     * The feature source for the remote WFS layer (see REMOVE_OWS_TYPE/URL in the SLD spec)
     */
    private FeatureSource remoteFeatureSource;

    /**
     *
     * @uml.property name="coverage"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private CoverageInfo coverage;

    /**
     *
     * @uml.property name="type" multiplicity="(0 1)"
     */
    private int type;

    /**
     *
     * @uml.property name="name" multiplicity="(0 1)"
     */
    private String name;

    /**
     *
     * @uml.property name="label" multiplicity="(0 1)"
     */
    private String label;

    /**
     *
     * @uml.property name="description" multiplicity="(0 1)"
     */
    private String description;

    /**
     *
     * @uml.property name="dirName" multiplicity="(0 1)"
     */
    private String dirName;
    
    /**
     * List of sublayer for a grouped layer
     */
    private List subLayerInfo;
    
    /**
     * List of styles for a grouped layer
     */
    private List styles;

    public MapLayerInfo() {
        name = "";
        label = "";
        description = "";
        dirName = "";

        coverage = null;
        feature = null;
        type = -1;
    }

    public MapLayerInfo(CoverageInfoDTO dto, Data data)
        throws ConfigurationException {
        name = dto.getName();
        label = dto.getLabel();
        description = dto.getDescription();
        dirName = dto.getDirName();

        coverage = new CoverageInfo(dto, data);
        feature = null;
        type = TYPE_RASTER;
    }

    public MapLayerInfo(FeatureTypeInfoDTO dto, Data data)
        throws ConfigurationException {
        name = dto.getName();
        label = dto.getTitle();
        description = dto.getAbstract();
        dirName = dto.getDirName();

        feature = new FeatureTypeInfo(dto, data);
        coverage = null;
        type = TYPE_VECTOR;
    }

    public MapLayerInfo(FeatureSource remoteSource) {
        name = remoteSource.getSchema().getTypeName();
        label = name;
        description = "Remote WFS";
        dirName = null;
        this.remoteFeatureSource = remoteSource;
        
        type = TYPE_REMOTE_VECTOR;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.vfny.geoserver.global.GlobalLayerSupertype#toDTO()
     */
    Object toDTO() {
        return null;
    }

    /**
     * getBoundingBox purpose.
     *
     * <p>
     * The feature source bounds.
     * </p>
     *
     * @return Envelope the feature source bounds.
     *
     * @throws IOException
     *             when an error occurs
     */
    public Envelope getBoundingBox() throws IOException {
        if (this.type == TYPE_VECTOR) {
            try {
                return feature.getBoundingBox();
            } catch (IllegalArgumentException e) {
                FeatureSource realSource = feature.getFeatureSource();

                return DataStoreUtils.getBoundingBoxEnvelope(realSource);
            }
        } else {
            // using referenced envelope (experiment)
            return new ReferencedEnvelope(
                coverage.getEnvelope().getMinimum(0),coverage.getEnvelope().getMaximum(0),
                coverage.getEnvelope().getMinimum(1),coverage.getEnvelope().getMaximum(1),
                coverage.getCrs());
        }
    }

    /**
     * Get the bounding box in latitude and longitude for this layer.
     *
     * @return Envelope the feature source bounds.
     *
     * @throws IOException
     *             when an error occurs
     */
    public Envelope getLatLongBoundingBox() throws IOException {
        if (this.type == TYPE_VECTOR) {
            try {
                return feature.getLatLongBoundingBox();
            } catch (IllegalArgumentException e) {
                FeatureSource realSource = feature.getFeatureSource();

                return DataStoreUtils.getBoundingBoxEnvelope(realSource);
            }
        } else {
            // using referenced envelope (experiment)
            org.geotools.geometry.GeneralEnvelope ge = coverage.getWGS84LonLatEnvelope();
            return new Envelope(ge.getMinimum(0), ge.getMaximum(0), ge.getMinimum(1), ge.getMaximum(1));
        }
    }

    /**
     *
     * @uml.property name="coverage"
     */
    public CoverageInfo getCoverage() {
        return coverage;
    }

    /**
     *
     * @uml.property name="coverage"
     */
    public void setCoverage(CoverageInfo coverage) {
        this.name = coverage.getName();
        this.label = coverage.getLabel();
        this.description = coverage.getDescription();
        this.dirName = coverage.getDirName();
        this.coverage = coverage;
        this.feature = null;
        this.type = TYPE_RASTER;
    }
    
    /**
     * Sets this up as a base layer
     * @param baseLayerName
     * @param subLayerInfo
     * @param styles
     */
    public void setBase(String baseLayerName, List subLayerInfo, List styles) {
        this.name = baseLayerName;
        this.type = TYPE_BASEMAP;
        this.subLayerInfo = subLayerInfo;
        this.styles = styles;
    }
    
    /**
     * Returns the sub layers of a base layer, as a list of MapLayerInfo objects
     * @return
     */
    public List getSubLayers() {
        return subLayerInfo;
    }
    
    /**
     * Returns the styles of a base layer
     * @return
     */
    public List getStyles() {
        return styles;
    }

    /**
     *
     * @uml.property name="description"
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @uml.property name="description"
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @uml.property name="dirName"
     */
    public String getDirName() {
        return dirName;
    }

    /**
     *
     * @uml.property name="dirName"
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    /**
     *
     * @uml.property name="feature"
     */
    public FeatureTypeInfo getFeature() {
        return feature;
    }

    /**
     *
     * @uml.property name="feature"
     */
    public void setFeature(FeatureTypeInfo feature) {
        // handle InlineFeatureStuff
        try {
            this.name = feature.getName();
            this.label = feature.getTitle();
            this.description = feature.getAbstract();
            this.dirName = feature.getDirName();
        } catch (IllegalArgumentException e) {
            this.name = "";
            this.label = "";
            this.description = "";
            this.dirName = "";
        }

        this.feature = feature;
        this.coverage = null;
        this.type = TYPE_VECTOR;
    }

    /**
     *
     * @uml.property name="label"
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @uml.property name="label"
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @uml.property name="name"
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @uml.property name="name"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @uml.property name="type"
     */
    public int getType() {
        return type;
    }

    /**
     *
     * @uml.property name="type"
     */
    public void setType(int type) {
        this.type = type;
    }

    public Style getDefaultStyle() {
        if (this.type == TYPE_VECTOR) {
            return this.feature.getDefaultStyle();
        } else if (this.type == TYPE_RASTER) {
            return this.coverage.getDefaultStyle();
        }

        return null;
    }

    /**
     * Returns the remote feature source in case this layer is a remote WFS layer
     * @return
     */
    public FeatureSource getRemoteFeatureSource() {
        return remoteFeatureSource;
    }
}
