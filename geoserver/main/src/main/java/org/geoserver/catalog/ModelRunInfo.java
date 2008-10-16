/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

import java.util.Date;
import java.util.List;

import org.geotools.geometry.jts.ReferencedEnvelope;

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
     * Model
     */
    ModelInfo getModel();
    
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
}