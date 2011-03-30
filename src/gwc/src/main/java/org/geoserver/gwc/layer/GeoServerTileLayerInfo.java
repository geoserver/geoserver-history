package org.geoserver.gwc.layer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.gwc.GWCConfig;
import org.springframework.util.Assert;

public class GeoServerTileLayerInfo {
    private static final String CONFIG_KEY_ENABLED = "GWC.enabled";

    private static final String CONFIG_KEY_GUTTER = "GWC.gutter";

    private static final String CONFIG_KEY_GRIDSETS = "GWC.gridSets";

    private static final String CONFIG_KEY_METATILING_X = "GWC.metaTilingX";

    private static final String CONFIG_KEY_METATILING_Y = "GWC.metaTilingY";

    private static final String CONFIG_KEY_FORMATS = "GWC.cacheFormats";

    private MetadataMap md;

    private int gutter;

    private boolean enabled;

    private List<String> cachedGridSetIds;

    private int metaTilingX;

    private int metaTilingY;

    private List<String> mimeFormats;

    private boolean cacheNonDefaultStyles;

    private static List<String> unmarshalList(final String listStr) {
        List<String> gridSetIds = Arrays.asList(listStr.split(","));
        return gridSetIds;
    }

    /**
     * Factory method based on a {@link LayerInfo}'s or {@link LayerGroupInfo}'s metadata map
     * 
     * @param resourceInfo
     * 
     * @param metadataMap
     * @return
     */
    public static GeoServerTileLayerInfo create(final ResourceInfo resourceInfo,
            final MetadataMap metadataMap, final GWCConfig defaults) {

        GeoServerTileLayerInfo info = new GeoServerTileLayerInfo();

        boolean enabled = defaults.isCacheLayersByDefault();
        if (metadataMap.containsKey(CONFIG_KEY_ENABLED)) {
            enabled = metadataMap.get(CONFIG_KEY_ENABLED, Boolean.class).booleanValue();
        }
        info.setEnabled(enabled);

        info.setGutter(0);
        if (metadataMap.containsKey(CONFIG_KEY_GUTTER)) {
            int gutter = metadataMap.get(CONFIG_KEY_GUTTER, Integer.class).intValue();
            info.setGutter(gutter);
        }

        info.setCachedGridSetIds(defaults.getDefaultCachingGridSetIds());
        if (metadataMap.containsKey(CONFIG_KEY_GRIDSETS)) {
            String gridsets = metadataMap.get(CONFIG_KEY_GRIDSETS, String.class);
            List<String> gridSetIds = unmarshalList(gridsets);
            info.setCachedGridSetIds(gridSetIds);
        }

        info.setMetaTilingX(defaults.getMetaTilingX());
        info.setMetaTilingY(defaults.getMetaTilingY());
        if (metadataMap.containsKey(CONFIG_KEY_METATILING_X)) {
            info.setMetaTilingX(metadataMap.get(CONFIG_KEY_METATILING_X, Integer.class).intValue());
        }
        if (metadataMap.containsKey(CONFIG_KEY_METATILING_Y)) {
            info.setMetaTilingY(metadataMap.get(CONFIG_KEY_METATILING_Y, Integer.class).intValue());
        }

        if (metadataMap.containsKey(CONFIG_KEY_FORMATS)) {
            String mimeFormatsStr = metadataMap.get(CONFIG_KEY_FORMATS, String.class);
            List<String> mimeFormats = unmarshalList(mimeFormatsStr);
            info.setMimeFormats(mimeFormats);
        } else if (resourceInfo instanceof FeatureTypeInfo) {
            info.setMimeFormats(defaults.getDefaultVectorCacheFormats());
        } else if (resourceInfo instanceof CoverageInfo) {
            info.setMimeFormats(defaults.getDefaultCoverageCacheFormats());
        } else {
            info.setMimeFormats(defaults.getDefaultOtherCacheFormats());
        }

        boolean cacheNonDefaultStyles = defaults.isCacheNonDefaultStyles();
        info.setCacheNonDefaultStyles(cacheNonDefaultStyles);

        return info;
    }

    public void setCacheNonDefaultStyles(boolean cacheNonDefaultStyles) {
        this.cacheNonDefaultStyles = cacheNonDefaultStyles;
    }

    public boolean isCacheNonDefaultStyles() {
        return cacheNonDefaultStyles;
    }

    public List<String> getMimeFormats() {
        return mimeFormats;
    }

    public void setMimeFormats(List<String> mimeFormats) {
        this.mimeFormats = new ArrayList<String>(mimeFormats);
    }

    public int getMetaTilingX() {
        return metaTilingX;
    }

    public int getMetaTilingY() {
        return metaTilingY;
    }

    public void setMetaTilingY(int metaTilingY) {
        Assert.isTrue(metaTilingY > 0);
        this.metaTilingY = metaTilingY;
    }

    public void setMetaTilingX(int metaTilingX) {
        Assert.isTrue(metaTilingX > 0);
        this.metaTilingX = metaTilingX;
    }

    public void setCachedGridSetIds(List<String> cachedGridSetIds) {
        this.cachedGridSetIds = cachedGridSetIds;
    }

    @SuppressWarnings("unchecked")
    public List<String> getCachedGridSetIds() {
        return cachedGridSetIds == null ? Collections.EMPTY_LIST : cachedGridSetIds;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setGutter(int gutter) {
        this.gutter = gutter;
    }

    public int getGutter() {
        return gutter;
    }
}
