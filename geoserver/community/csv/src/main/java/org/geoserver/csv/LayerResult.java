package org.geoserver.csv;

/**
 * Provides a quick summary of what happened to each layer specified in the csv
 * file
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class LayerResult {
    public enum LayerOperation {
        CREATED, REPLACED
    };

    String layerName;
    
    String layerDescription;

    LayerOperation operation;

    public LayerResult(String layerName, String layerDescription) {
        this.layerName = layerName;
        this.layerDescription = layerDescription;
        this.operation = LayerOperation.CREATED;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setOperation(LayerOperation operation) {
        this.operation = operation;
    }

    public LayerOperation getOperation() {
        return operation;
    }

    public String getLayerDescription() {
        return layerDescription;
    }
}
