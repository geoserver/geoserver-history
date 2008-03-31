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

    LayerOperation operation;

    public LayerResult(String layerName) {
        this.layerName = layerName;
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
}
