/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.io.IOException;
import java.util.List;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.util.DataStoreUtils;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @author Gabriel Roldan
 */
public final class MapLayerInfo {
    public static int TYPE_VECTOR = LayerInfo.Type.VECTOR.getCode();

    public static int TYPE_RASTER = LayerInfo.Type.RASTER.getCode();

    public static int TYPE_REMOTE_VECTOR = LayerInfo.Type.REMOTE.getCode();

    /**
     * not a layer type at all, but indicates its a base map composed of a list of other resources
     */
    public static int TYPE_BASEMAP = -1;

    /**
     * 
     * @uml.property name="feature"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private FeatureTypeInfo feature;

    /**
     * The feature source for the remote WFS layer (see REMOVE_OWS_TYPE/URL in the SLD spec)
     */
    private FeatureSource<SimpleFeatureType, SimpleFeature> remoteFeatureSource;

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

    private LayerInfo layerInfo;

    public MapLayerInfo() {
        name = "";
        label = "";
        description = "";
        dirName = "";

        coverage = null;
        feature = null;
        type = -1;
    }

    public MapLayerInfo(FeatureSource<SimpleFeatureType, SimpleFeature> remoteSource) {
        setRemoteFeatureSource(remoteSource);
    }

    public MapLayerInfo(LayerInfo layerInfo) {
        this.layerInfo = layerInfo;
        ResourceInfo resource = layerInfo.getResource();
        if (resource instanceof FeatureTypeInfo) {
            setFeature((FeatureTypeInfo) resource);
        } else if (resource instanceof CoverageInfo) {
            setCoverage((CoverageInfo) resource);
        } else {
            throw new IllegalArgumentException(String.valueOf(layerInfo));
        }
    }

    /**
     * <p>
     * The feature source bounds. Mind, it might be null, in that case, grab the lat/lon bounding
     * box and reproject to the native bounds.
     * </p>
     * 
     * @return Envelope the feature source bounds.
     * @throws Exception
     */
    public ReferencedEnvelope getBoundingBox() throws Exception {
        if (layerInfo != null) {
            return layerInfo.getResource().getBoundingBox();
        } else if (this.type == TYPE_REMOTE_VECTOR) {
            return remoteFeatureSource.getBounds();
        }
        return null;
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
        if (layerInfo != null) {
            return layerInfo.getResource().getLatLonBoundingBox();
        }

        return DataStoreUtils.getBoundingBoxEnvelope(remoteFeatureSource);
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
        this.label = coverage.getTitle();
        this.description = coverage.getDescription();
        // this.dirName = coverage.getDirName();
        this.coverage = coverage;
        this.feature = null;
        this.type = TYPE_RASTER;
    }

    /**
     * Sets this up as a base layer
     * 
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
     * 
     * @return
     */
    public List getSubLayers() {
        return subLayerInfo;
    }

    /**
     * Returns the styles of a base layer
     * 
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
        this.name = feature.getName();
        this.label = feature.getTitle();
        this.description = feature.getAbstract();
        // this.dirName = feature.getDirName();
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
        if (layerInfo != null) {
            try {
                return layerInfo.getDefaultStyle().getStyle();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    /**
     * Returns the remote feature source in case this layer is a remote WFS layer
     * 
     * @return
     */
    public FeatureSource<SimpleFeatureType, SimpleFeature> getRemoteFeatureSource() {
        return remoteFeatureSource;
    }

    public void setRemoteFeatureSource(
            FeatureSource<SimpleFeatureType, SimpleFeature> remoteFeatureSource) {
        name = remoteFeatureSource.getSchema().getTypeName();
        label = name;
        description = "Remote WFS";
        dirName = null;
        this.remoteFeatureSource = remoteFeatureSource;

        type = TYPE_REMOTE_VECTOR;
    }

    /**
     * @return the resource SRS name or {@code null} if the underlying resource is not a registered
     *         one
     */
    public String getSRS() {
        if (layerInfo != null) {
            return layerInfo.getResource().getSRS();
        }
        return null;
    }

}
