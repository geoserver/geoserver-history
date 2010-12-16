package org.geoserver.inspire.wms;

public enum InspireMetadata {
    LANGUAGE("inspire.language"),
    METADATA_URL("inspire.metadataURL");

    public String key;
    private InspireMetadata(String key) {
        this.key = key;
    }
}
