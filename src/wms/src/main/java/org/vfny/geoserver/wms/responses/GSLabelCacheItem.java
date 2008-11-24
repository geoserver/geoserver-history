package org.vfny.geoserver.wms.responses;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.LabelCacheItem;
import org.geotools.renderer.style.TextStyle2D;

public class GSLabelCacheItem extends LabelCacheItem {
    int maxDisplacement = 0;
    int minGroupDistance = 0;
    int repeat = 0;
    boolean labelAllGroup = false;
    boolean removeGroupOverlaps = false;
    boolean allowOverruns = true;
    boolean followLineEnabled = false;
    double maxAngleDelta;

    public GSLabelCacheItem(String layerId, TextStyle2D textStyle, LiteShape2 shape, String label) {
        super(layerId, textStyle, shape, label);
    }

    public int getMaxDisplacement() {
        return maxDisplacement;
    }

    public void setMaxDisplacement(int maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public boolean labelAllGroup() {
        return labelAllGroup;
    }

    public void setLabelAllGroup(boolean labelAllGroup) {
        this.labelAllGroup = labelAllGroup;
    }

    public boolean removeGroupOverlaps() {
        return removeGroupOverlaps;
    }

    public void setRemoveGroupOverlaps(boolean removeGroupOverlaps) {
        this.removeGroupOverlaps = removeGroupOverlaps;
    }
    
    public boolean allowOverruns() {
        return allowOverruns;
    }

    public void setAllowOverruns(boolean allowOverruns) {
        this.allowOverruns = allowOverruns;
    }

    public int getMinGroupDistance() {
        return minGroupDistance;
    }

    public void setMinGroupDistance(int minGroupDistance) {
        this.minGroupDistance = minGroupDistance;
    }

    public boolean isFollowLineEnabled() {
        return followLineEnabled;
    }

    public void setFollowLineEnabled(boolean followLineEnabled) {
        this.followLineEnabled = followLineEnabled;
    }

    public double getMaxAngleDelta() {
        return maxAngleDelta;
    }

    public void setMaxAngleDelta(double maxAngleDelta) {
        this.maxAngleDelta = maxAngleDelta;
    }

}
