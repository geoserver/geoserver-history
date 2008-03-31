package org.geoserver.csv;

/**
 * Provides a quick summary of what happened to each layer specified in the csv file 
 * @author Andrea Aime - TOPP
 *
 */
public class LayerResult {
    public enum LayerOperation {CREATED, REPLACED};
    
    String layerName;
    LayerOperation operation;
}
