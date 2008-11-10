/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A matematical model run.
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public interface ModelRunInfo {
    
    /**
     * Identifier.
     */
    String getId();

    /**
     * Identifier.
     */
    void setId(String id);

    /**
     * Model
     */
    ModelInfo getModel();

    /**
     * Model
     */
    void setModel(ModelInfo model);

    /**
     * Name of the run.
     */
    String getName();

    /**
     * Sets the name of the run.
     */
    void setName(String name);
    
    /**
     * Description of the run.
     */
    String getDescription();

    /**
     * Sets the description of the run.
     */
    void setDescription(String description);
    
    /**
     * A collection of keywords associated with the run.
     */
    List<String> getKeywords();

    /**
     * A collection of keywords associated with the run.
     */
    void setKeywords(List<String> keywords);
    
    /**
     * The update-sequence of the run.
     */
    String getUpdateSequence();

    /**
     * Sets the update-sequence of the run.
     */
    void setUpdateSequence(String updateSequence);

    /**
     * The base-time of the run.
     */
    Date getBaseTime();

    /**
     * Sets the base-time of the run.
     */
    void setBaseTime(Date baseTime);
    
    /**
     * The execution-time of the run.
     */
    Date getExecutionTime();

    /**
     * Sets the execution-time of the run.
     */
    void setExecutionTime(Date executionTime);
    
    /**
     * The TAU of the run.
     */
    Integer getTAU();
    String  getTAUunit();
    Integer getNumTAU();
    
    /**
     * Sets the TAU of the run.
     */
    void setTAU(Integer TAU);
    void setTAUunit(String uom);
    void setNumTAU(Integer numTAU);

    /**
     * The grid-CRS of the model.
     */
    String getGridCRS();

    /**
     * Sets the grid-CRS of the model.
     */
    void setGridCRS(String gridCRS);

    /**
     * The grid-type of the model.
     */
    String getGridType();

    /**
     * Sets the grid-type of the model.
     */
    void setGridType(String gridType);
    
    /**
     * The grid-CS of the model.
     */
    String getGridCS();

    /**
     * Sets the grid-CS of the model.
     */
    void setGridCS(String gridCS);

    /**
     * The grid-origin of the model.
     */
    Double[] getGridOrigin();

    /**
     * Sets the grid-origin of the model.
     */
    void setGridOrigin(Double[] gridOrigin);

    /**
     * The grid-offsets of the model.
     */
    Double[] getGridOffsets();

    /**
     * Sets the grid-offsets of the model.
     */
    void setGridOffsets(Double[] gridOffsets);

    /**
     * The grid-resolution of the model.
     */
    Double[] getGridLowers();
    Double[] getGridUppers();

    /**
     * Sets the grid-resolution of the model.
     */
    void setGridLowers(Double[] gridLowers);
    void setGridUppers(Double[] gridUppers);

    /**
     * The CRS of the model.
     */
    CoordinateReferenceSystem getCRS();

    /**
     * Sets the CRS of the model.
     */
    void setCRS(CoordinateReferenceSystem crs);

    /**
     * The vertical coordinate meaning of the model.
     */
    String getVerticalCoordinateMeaning();

    /**
     * Sets the vertical coordinate meaning of the model.
     */
    void setVerticalCoordinateMeaning(String vertical_coordinate_meaning);
    
    /**
     * A persistent map of init parameters.
     * <p>
     * Represents a map of custom parameters used to initialize the model.
     * </p>
     * <p>
     * The key values of this map are of type {@link String} and values are of
     * type {@link Serializable}.
     * </p>
     */
    Map<String, Serializable> getInitParams();
    void setInitParams(Map<String, Serializable> params);

    /**
     * A persistent map of out parameters.
     * <p>
     * Represents a map of custom parameters used to produce the outcomes of the model.
     * </p>
     * <p>
     * The key values of this map are of type {@link String} and values are of
     * type {@link Serializable}.
     * </p>
     */
    Map<String, Serializable> getOutParams();
    void setOutParams(Map<String, Serializable> params);

    /**
     * The outline of the run.
     */
    ReferencedEnvelope getOutline();

    /**
     * Sets the outline of the run.
     */
    void setOutline(ReferencedEnvelope outline);

    /**
     * The gridCoverages of the run.
     */
    List<CoverageInfo> getGridCoverages();
    
    /**
     * Sets the gridCoverages of the run.
     */
    void setGridCoverages(List<CoverageInfo> coverages);

}