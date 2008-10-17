/**
 * 
 */
package org.geoserver.catalog.impl;

import java.util.Date;
import java.util.List;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;

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
    private ReferencedEnvelope outline;
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
