/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

/**
 * Class to record capabilities for a {@link RasterMapProducer}.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class MapProducerCapabilities {

    private final boolean tiledRequestsSupported;

    private final boolean multivalueRequestsSupported;

    private final boolean paletteSupported;

    private final boolean transparencySupported;

    public MapProducerCapabilities(boolean tiledRequestsSupported,
            boolean multivalueRequestsSupported, boolean paletteSupported,
            boolean transparencySupported) {
        super();
        this.tiledRequestsSupported = tiledRequestsSupported;
        this.multivalueRequestsSupported = multivalueRequestsSupported;
        this.paletteSupported = paletteSupported;
        this.transparencySupported = transparencySupported;
    }

    /**
     * If the map producer can be used in a meta-tiling context
     * @return
     */
    public boolean isTiledRequestsSupported() {
        return tiledRequestsSupported;
    }

    /**
     * Returns true if the map producer can handle list or results (one per time/elevation/dimension value)
     * instead of a single one 
     * @return
     */
    public boolean isMultivalueRequestsSupported() {
        return multivalueRequestsSupported;
    }

    /**
     * Returns true if paletted images are supported
     * @return
     */
    public boolean isPaletteSupported() {
        return paletteSupported;
    }

    /**
     * Returns true if background transparency is supported
     * @return
     */
    public boolean isTransparencySupported() {
        return transparencySupported;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (multivalueRequestsSupported ? 1231 : 1237);
        result = prime * result + (paletteSupported ? 1231 : 1237);
        result = prime * result + (tiledRequestsSupported ? 1231 : 1237);
        result = prime * result + (transparencySupported ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MapProducerCapabilities other = (MapProducerCapabilities) obj;
        if (multivalueRequestsSupported != other.multivalueRequestsSupported)
            return false;
        if (paletteSupported != other.paletteSupported)
            return false;
        if (tiledRequestsSupported != other.tiledRequestsSupported)
            return false;
        if (transparencySupported != other.transparencySupported)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RasterMapProducerCapabilities [multivalueRequestsSupported="
                + multivalueRequestsSupported + ", paletteSupported=" + paletteSupported
                + ", tiledRequestsSupported=" + tiledRequestsSupported + ", transparencySupported="
                + transparencySupported + "]";
    }
}