package org.geoserver.importer;

public enum ImportStatus {
    SUCCESS(true), DEFAULTED_SRS(true), DUPLICATE(false), MISSING_SRS(false), MISSING_BBOX(false), OTHER(false);
    
    boolean success;
    ImportStatus(boolean success) {
        this.success = success;
    }
    
    public boolean successful() {
        return success;
    }
}
