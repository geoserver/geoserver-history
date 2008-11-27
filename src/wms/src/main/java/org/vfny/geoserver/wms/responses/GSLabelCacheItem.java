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

    int autoWrap = 100;

    public GSLabelCacheItem(String layerId, TextStyle2D textStyle, LiteShape2 shape, String label) {
        super(layerId, textStyle, shape, label);
    }

    /**
     * Max amount of pixels the label will be moved around trying to find a non
     * conflicting location (how and if the moving will be done is geometry type
     * dependent)
     * 
     * @return
     */
    public int getMaxDisplacement() {
        return maxDisplacement;
    }

    public void setMaxDisplacement(int maxDisplacement) {
        this.maxDisplacement = maxDisplacement;
    }

    /**
     * When enabled, repeats labels every "repeat" pixels (works on lines only
     * atm)
     * 
     * @return
     */
    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    /**
     * When grouping, wheter we should label only the biggest geometry, or the
     * others as well
     * 
     * @return
     */
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

    /**
     * Wheter labels are allowed to go past the start/end of the line
     * 
     * @return
     */
    public boolean allowOverruns() {
        return allowOverruns;
    }

    public void setAllowOverruns(boolean allowOverruns) {
        this.allowOverruns = allowOverruns;
    }

    public int getMinGroupDistance() {
        return minGroupDistance;
    }

    /**
     * Minimum cartesian distance between two labels in the same group, in
     * pixels
     * 
     * @param minGroupDistance
     */
    public void setMinGroupDistance(int minGroupDistance) {
        this.minGroupDistance = minGroupDistance;
    }

    /**
     * Enables curved labels on linear features
     * 
     * @return
     */
    public boolean isFollowLineEnabled() {
        return followLineEnabled;
    }

    public void setFollowLineEnabled(boolean followLineEnabled) {
        this.followLineEnabled = followLineEnabled;
    }

    /**
     * Max angle between two subsequence characters in a curved label, in
     * degrees. Good visual results are obtained with an angle of less than 25
     * degrees.
     * 
     * @return
     */
    public double getMaxAngleDelta() {
        return maxAngleDelta;
    }

    public void setMaxAngleDelta(double maxAngleDelta) {
        this.maxAngleDelta = maxAngleDelta;
    }

    /**
     * Automatically wraps long labels when the label width, in pixels, exceeds
     * the autowrap length
     * 
     * @return
     */
    public int getAutoWrap() {
        return autoWrap;
    }

    public void setAutoWrap(int autoWrap) {
        this.autoWrap = autoWrap;
    }

}
