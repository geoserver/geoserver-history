/**
 * 
 */
package org.geoserver.catalog.impl;

import java.util.List;

import org.geoserver.catalog.GeophysicParamInfo;

/**
 * @author Alessio Fabiani, GeoSolutions
 *
 */
public abstract class GeophysicParamInfoImpl implements GeophysicParamInfo {

    private List<String> aliases;
    private String description;
    private String id;
    private String name;
    private String title;

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#getAlias()
     */
    public List<String> getAlias() {
        return aliases;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#getTitle()
     */
    public String getTitle() {
        return title;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#setAlias(java.util.List)
     */
    public void setAlias(List<String> aliases) {
        this.aliases = aliases;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.geoserver.catalog.GeophysicParamInfo#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
