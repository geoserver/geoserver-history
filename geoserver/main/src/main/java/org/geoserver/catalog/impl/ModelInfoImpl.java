/**
 * 
 */
package org.geoserver.catalog.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;

/**
 * @author Alessio
 *
 */
public class ModelInfoImpl implements ModelInfo {

    private String _abstract;
    private String originatingCenter;
    private String description;
    private Discipline discipline;
    private String id;
    private List<String> keywords;
    private Map<String, Serializable> metadata;
    private List<MetadataLinkInfo> metadataLink;
    private List<ModelRunInfo> modelRuns;
    private String name;
    private List<String> products;
    private String subCenter;
    private String title;
    private DataType typeOfData;
    private String version;
    private List<GeophysicParamInfo> params;

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getAbstract()
     */
    public String getAbstract() {
        return _abstract;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getCenter()
     */
    public String getCenter() {
        return originatingCenter;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getDiscipline()
     */
    public Discipline getDiscipline() {
        return discipline;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getKeywords()
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getMetadata()
     */
    public Map<String, Serializable> getMetadata() {
        return metadata;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getMetadataLinks()
     */
    public List<MetadataLinkInfo> getMetadataLink() {
        return metadataLink;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getModelRuns()
     */
    public List<ModelRunInfo> getModelRuns() {
        return modelRuns;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getProducts()
     */
    public List<String> getProducts() {
        return products;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getSubCenter()
     */
    public String getSubCenter() {
        return subCenter;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getTitle()
     */
    public String getTitle() {
        return title;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getTypeOfData()
     */
    public DataType getTypeOfData() {
        return typeOfData;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#getVersion()
     */
    public String getVersion() {
        return version;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setAbstract(java.lang.String)
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setCenter(java.lang.String)
     */
    public void setCenter(String center) {
        this.originatingCenter = center;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setDiscipline(org.geoserver.catalog.ModelInfo.Discipline)
     */
    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setKeywords(java.util.List)
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setProducts(java.util.List)
     */
    public void setProducts(List<String> products) {
        this.products = products;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setSubCenter(java.lang.String)
     */
    public void setSubCenter(String subcenter) {
        this.subCenter = subcenter;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setTypeOfData(org.geoserver.catalog.ModelInfo.DataType)
     */
    public void setTypeOfData(DataType type_of_data) {
        this.typeOfData = type_of_data;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.ModelInfo#setVersion(java.lang.String)
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMetadata(Map<String, Serializable> metadata) {
        this.metadata = metadata;
    }

    public void setModelRuns(List<ModelRunInfo> runs) {
        this.modelRuns = runs;
    }

    public void setMetadataLink(List<MetadataLinkInfo> metadataLink) {
        this.metadataLink = metadataLink;
    }

    public List<GeophysicParamInfo> getGeophysicalParameters() {
        return this.params;
    }

    public void setGeophysicalParameters(List<GeophysicParamInfo> params) {
        this.params = params;
    }

}
