/**
 * 
 */
package org.geoserver.catalog.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Alessio
 *
 */
public class ModelRunInfoImpl implements ModelRunInfo {

    private Date baseTime;
    private String description;
    private Date executionTime;
    private List<CoverageInfo> gridCoverages;
    private String id;
    private List<String> keywords;
    private ModelInfo model;
    private String name;
    private Integer numTAU;
    private String gridCRS;
    private String gridCS;
    private Double[] gridLowers;
    private Double[] gridOffsets;
    private Double[] gridOrigin;
    private String gridType;
    private Double[] gridUppers;
    private Map<String, Serializable> initParams;
    private Map<String, Serializable> outParams;
    private CoordinateReferenceSystem crs;
    private ReferencedEnvelope outline;
    private String verticalCoordinateMeaning;
    private Integer TAU;
    private String TAUunit;
    private String updateSequence;

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getBaseTime()
     */
    public Date getBaseTime() {
        return baseTime;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getExecutionTime()
     */
    public Date getExecutionTime() {
        return executionTime;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getGridCoverages()
     */
    public List<CoverageInfo> getGridCoverages() {
        return gridCoverages;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getKeywords()
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getModel()
     */
    public ModelInfo getModel() {
        return model;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getNumTAU()
     */
    public Integer getNumTAU() {
        return numTAU;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getOutline()
     */
    public ReferencedEnvelope getOutline() {
        return outline;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getTAU()
     */
    public Integer getTAU() {
        return TAU;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getTAUunit()
     */
    public String getTAUunit() {
        return TAUunit;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#getUpdateSequence()
     */
    public String getUpdateSequence() {
        return updateSequence;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setBaseTime(java.util.Date)
     */
    public void setBaseTime(Date baseTime) {
        this.baseTime = baseTime;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setExecutionTime(java.util.Date)
     */
    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setKeywords(java.util.List)
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setNumTAU(java.lang.Integer)
     */
    public void setNumTAU(Integer numTAU) {
        this.numTAU = numTAU;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getCRS()
     */
    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridCRS()
     */
    public String getGridCRS() {
        return gridCRS;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridCS()
     */
    public String getGridCS() {
        return gridCS;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridLowers()
     */
    public Double[] getGridLowers() {
        return gridLowers;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridOffsets()
     */
    public Double[] getGridOffsets() {
        return gridOffsets;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridOrigin()
     */
    public Double[] getGridOrigin() {
        return gridOrigin;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridType()
     */
    public String getGridType() {
        return gridType;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getGridUppers()
     */
    public Double[] getGridUppers() {
        return gridUppers;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getInitParams()
     */
    public Map<String, Serializable> getInitParams() {
        return initParams;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getOutParams()
     */
    public Map<String, Serializable> getOutParams() {
        return outParams;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setCRS(org.opengis.referencing.crs.CoordinateReferenceSystem)
     */
    public void setCRS(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridCRS(java.lang.String)
     */
    public void setGridCRS(String gridCRS) {
        this.gridCRS = gridCRS;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridCS(java.lang.String)
     */
    public void setGridCS(String gridCS) {
        this.gridCS = gridCS;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridLowers(java.lang.Double[])
     */
    public void setGridLowers(Double[] gridLowers) {
        this.gridLowers = gridLowers;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridOffsets(java.lang.Double[])
     */
    public void setGridOffsets(Double[] gridOffsets) {
        this.gridOffsets = gridOffsets;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridOrigin(java.lang.Double[])
     */
    public void setGridOrigin(Double[] gridOrigin) {
        this.gridOrigin = gridOrigin;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridType(java.lang.String)
     */
    public void setGridType(String gridType) {
        this.gridType = gridType;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setGridUppers(java.lang.Double[])
     */
    public void setGridUppers(Double[] gridUppers) {
        this.gridUppers = gridUppers;
    }

    public void setInitParams(Map<String, Serializable> params) {
        this.initParams = params;
    }

    public void setOutParams(Map<String, Serializable> params) {
        this.outParams = params;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setVerticalCoordinateMeaning(java.lang.String)
     */
    public void setVerticalCoordinateMeaning(String vertical_coordinate_meaning) {
        this.verticalCoordinateMeaning = vertical_coordinate_meaning;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getVerticalCoordinateMeaning()
     */
    public String getVerticalCoordinateMeaning() {
        return verticalCoordinateMeaning;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setOutline(org.geotools.geometry.jts.ReferencedEnvelope)
     */
    public void setOutline(ReferencedEnvelope outline) {
        this.outline = outline;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setTAU(java.lang.Integer)
     */
    public void setTAU(Integer TAU) {
        this.TAU = TAU;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setTAUunit(java.lang.String)
     */
    public void setTAUunit(String uom) {
        this.TAUunit = uom;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelRunInfo#setUpdateSequence(java.lang.String)
     */
    public void setUpdateSequence(String updateSequence) {
        this.updateSequence = updateSequence;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGridCoverages(List<CoverageInfo> coverages) {
        this.gridCoverages = coverages;
    }

    public void setModel(ModelInfo model) {
        this.model = model;
    }

}
