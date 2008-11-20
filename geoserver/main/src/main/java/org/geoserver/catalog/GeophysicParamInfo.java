/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

import java.util.List;

/**
 * A geophysical parameter.
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public interface GeophysicParamInfo {

    /**
     * @return The identifier of the geophysical parameter.
     */
    String getId();
    
    /**
     * 
     */
    void setId(String id);
    
    /**
     * The name of the geophysical parameter.
     * 
     */
    String getName();

    /**
     * Sets the name of the geophysical parameter.
     * 
     */
    void setName(String name);

    /**
     * A set of aliases or alternative names that the geophysical parameter is also known by. 
     */
    List<String> getAlias();
    
    /**
     * A set of aliases or alternative names that the geophysical parameter is also known by. 
     */
    void setAlias(List<String> aliases);
    
    /**
     * The title of the geophysical parameter.
     * <p>
     * This is usually something that is meant to be displayed in a user
     * interface.
     * </p>
     * 
     */
    String getTitle();

    /**
     * Sets the title of the geophysical parameter.
     * 
     */
    void setTitle(String title);

    /**
     * A description of the geophysical parameter.
     * <p>
     * This is usually something that is meant to be displayed in a user
     * interface.
     * </p>
     * 
     */
    String getDescription();

    /**
     * Sets the description.
     * 
     */
    void setDescription(String description);
    
    /**
     * 
     */
    List<ModelRunInfo> getModelRuns();
    void setModelRuns(List<ModelRunInfo> modelRuns);
    
    /**
     * 
     */
    List<CoverageInfo> getGridCoverages();
    void setGridCoverages(List<CoverageInfo> gridCoverages);
    
}
