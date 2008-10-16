/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A matematical model.
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public interface ModelInfo {
    
    /**
     * Enumeration for type of model discipline.
     */
    public enum Discipline {
        METEO {
            public Integer getCode() {
                return 0;
            }
            
            public String getDescription() {
                return "Meteorological Products";
            }
        },
        HYDRO {
            public Integer getCode() {
                return 1;
            }
            
            public String getDescription() {
                return "Hydrological Products";
            }
        }, 
        LAND {
            public Integer getCode() {
                return 2;
            }
            
            public String getDescription() {
                return "Land Surface Products";
            }
        }, 
        SPACE {
            public Integer getCode() {
                return 3;
            }
            
            public String getDescription() {
                return "Space Products";
            }
        },
        OCEAN {
            public Integer getCode() {
                return 4;
            }
            
            public String getDescription() {
                return "Oceanographic Products";
            }
        };
        
        public abstract Integer getCode();
        public abstract String getDescription();
    }

    /**
     * Enumeration for type of model type of data.
     */
    public enum DataType {
        ANALYSIS {
            public Integer getCode() {
                return 0;
            }
            
            public String getDescription() {
                return "Analysis Products";
            }
        },
        FORECAST {
            public Integer getCode() {
                return 1;
            }
            
            public String getDescription() {
                return "Forecast Products";
            }
        }, 
        ANALYSYS_AND_FORECAST {
            public Integer getCode() {
                return 2;
            }
            
            public String getDescription() {
                return "Analysis and Forecast Products";
            }
        }, 
        CONTROL_FORECAST {
            public Integer getCode() {
                return 3;
            }
            
            public String getDescription() {
                return "Control Forecast Products";
            }
        },
        PERTURBED_FORECAST {
            public Integer getCode() {
                return 4;
            }
            
            public String getDescription() {
                return "Perturbed Forecast Products";
            }
        },
        CONTROL_AND_PERTURBED_FORECAST {
            public Integer getCode() {
                return 5;
            }
            
            public String getDescription() {
                return "Control and Perturbed Forecast Products";
            }
        },
        SATELLITE {
            public Integer getCode() {
                return 6;
            }
            
            public String getDescription() {
                return "Processed Satellite Observations";
            }
        },
        RADAR {
            public Integer getCode() {
                return 7;
            }
            
            public String getDescription() {
                return "Processed Radar Observations";
            }
        };
        
        public abstract Integer getCode();
        public abstract String getDescription();
    }

    /**
     * Identifier.
     */
    String getId();
    
    /**
     * Name of the model.
     */
    String getName();

    /**
     * Sets the name of the model.
     */
    void setName(String name);
    
    /**
     * Title of the model.
     */
    String getTitle();

    /**
     * Sets the title of the model.
     */
    void setTitle(String title);

    /**
     * Abstract of the model.
     */
    String getAbstract();

    /**
     * Sets the abstract of the model.
     */
    void setAbstract(String _abstract);
    
    /**
     * Description of the model.
     */
    String getDescription();

    /**
     * Sets the description of the model.
     */
    void setDescription(String description);
    
    /**
     * The discipline of the model.
     */
    Discipline getDiscipline();

    /**
     * Sets the discipline of the model.
     */
    void setDiscipline(Discipline discipline);

    /**
     * A collection of keywords associated with the resource.
     */
    List<String> getKeywords();

    /**
     * A collection of keywords associated with the resource.
     */
    void setKeywords(List<String> keywords);
    
    /**
     * The version of the model.
     */
    String getVersion();

    /**
     * Sets the version of the model.
     */
    void setVersion(String version);

    /**
     * The center of the model.
     */
    String getCenter();

    /**
     * Sets the center of the model.
     */
    void setCenter(String center);
    
    /**
     * The sub-center of the model.
     */
    String getSubCenter();

    /**
     * Sets the sub-center of the model.
     */
    void setSubCenter(String subcenter);

    /**
     * The products of the model.
     */
    List<String> getProducts();

    /**
     * Sets the products of the model.
     */
    void setProducts(List<String> products);
    
    /**
     * The type of data of the model.
     */
    DataType getTypeOfData();

    /**
     * Sets the type of data of the model.
     */
    void setTypeOfData(DataType discipline);

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

    /**
     * A collection of metadata links for the resource.
     * 
     * @see MetadataLinkInfo
     */
    List<MetadataLinkInfo> getMetadataLinks();

    /**
     * A persistent map of metadata.
     * <p>
     * Data in this map is intended to be persisted.
     * </p>
     * <p>
     * The key values of this map are of type {@link String} and values are of
     * type {@link Serializable}.
     * </p>
     */
    Map<String, Serializable> getMetadata();
    
    /**
     * The runs of the model.
     */
    List<ModelRunInfo> getModelRuns();

}
