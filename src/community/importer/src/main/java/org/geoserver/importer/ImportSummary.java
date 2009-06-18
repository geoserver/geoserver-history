package org.geoserver.importer;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.geoserver.catalog.LayerInfo;

/**
 * Contains summary information
 */
@SuppressWarnings("serial")
public class ImportSummary implements Serializable {
    long startTime;

    long endTime;

    int totalLayers;

    int failures;

    String currentLayer;
    
    Exception error;
    
    // concurrent list so that we can manipulate it while it's being iterated over
    List<LayerSummary> layers = new CopyOnWriteArrayList<LayerSummary>();

    public ImportSummary() {
        startTime = System.currentTimeMillis();
    }

    public void newLayer(String currentLayer) {
        totalLayers++;
        this.currentLayer = currentLayer;
    }

    public void end(Exception error) {
        this.error = error;
        this.currentLayer = null;
        this.endTime = System.currentTimeMillis();
    }

    public void end() {
        this.currentLayer = null;
        this.endTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getTotalLayers() {
        return totalLayers;
    }

    public List<LayerSummary> getLayers() {
        return layers;
    }

    public int getFailures() {
        return failures;
    }

    public String getCurrentLayer() {
        return currentLayer;
    }

    public void completeLayer(String layerName, LayerInfo layer, ImportStatus status) {
        layers.add(new LayerSummary(layerName, layer, status));
        if(status.successful())
            failures++;
    }
    
    public void completeLayer(String layerName, LayerInfo layer, Exception error) {
        layers.add(new LayerSummary(layerName, layer, error));
        failures++;
    }

}
