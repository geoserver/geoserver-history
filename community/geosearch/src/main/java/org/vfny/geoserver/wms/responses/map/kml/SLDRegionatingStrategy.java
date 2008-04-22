package org.vfny.geoserver.wms.responses.map.kml;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.feature.FeatureCollection;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.map.MapLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.vfny.geoserver.wms.WMSMapContext;

public class SLDRegionatingStrategy implements RegionatingStrategy {
    private FeatureTypeStyle[] styles;
    private static Logger LOGGER  = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    public SLDRegionatingStrategy(FeatureTypeStyle[] styles){
        this.styles = styles;
    }

    static final double TOLERANCE = 1e-6;

    private double scaleDenominator;

    public void preProcess(WMSMapContext con, MapLayer layer){
        scaleDenominator = 1; 
        try {
            scaleDenominator = 
                RendererUtilities.calculateScale(con.getAreaOfInterest(), con.getMapWidth(), con.getMapHeight(), null);
        } 
        catch( Exception e ) {
            LOGGER.severe("Error calculating scale denominator" + e );
        }
    }

    public boolean include(SimpleFeature feature){
        for (int i = 0; i < styles.length; i++){
            if (filterRules(styles[i], feature, scaleDenominator).length > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Filters the rules of <code>featureTypeStyle</code> returnting only
     * those that apply to <code>feature</code>.
     * <p>
     * This method returns rules for which:
     * <ol>
     *  <li><code>rule.getFilter()</code> matches <code>feature</code>, or:
     *  <li>the rule defines an "ElseFilter", and the feature matches no
     *  other rules.
     * </ol>
     * This method returns an empty array in the case of which no rules
     * match.
     * </p>
     * @param featureTypeStyle The feature type style containing the rules.
     * @param feature The feature being filtered against.
     *
     * @todo this method is duplicated from KMLVectorTransformer.  We should probably move it to a utility class or so.
     *
     */
    Rule[] filterRules(FeatureTypeStyle featureTypeStyle, SimpleFeature feature, double scaleDenominator) {
        Rule[] rules = featureTypeStyle.getRules();

        if ((rules == null) || (rules.length == 0)) {
            return new Rule[0];
        }

        ArrayList filtered = new ArrayList(rules.length);

        //process the rules, keep track of the need to apply an else filters
        boolean match = false;
        boolean hasElseFilter = false;

        for (int i = 0; i < rules.length; i++) {
            Rule rule = rules[i];
            LOGGER.finer(new StringBuffer("Applying rule: ").append(rule.toString()).toString());

            //does this rule have an else filter
            if (rule.hasElseFilter()) {
                hasElseFilter = true;

                continue;
            }

            //is this rule within scale?
            if ( !isWithinScale(rule, scaleDenominator)) {
                continue;
            }

            //does this rule have a filter which applies to the feature
            Filter filter = rule.getFilter();

            if ((filter == null) || filter.evaluate(feature)) {
                match = true;

                filtered.add(rule);
            }
        }

        //if no rules mached the feautre, re-run through the rules applying
        // any else filters
        if (!match && hasElseFilter) {
            //loop through again and apply all the else rules
            for (int i = 0; i < rules.length; i++) {
                Rule rule = rules[i];

                //is this rule within scale?
                if ( !isWithinScale(rule, scaleDenominator)) {
                    continue;
                }

                if (rule.hasElseFilter()) {
                    filtered.add(rule);
                }
            }
        }

        return (Rule[]) filtered.toArray(new Rule[filtered.size()]);
    }

    private boolean isWithinScale(Rule rule, double scaleDenominator){
        return ((rule.getMinScaleDenominator() - TOLERANCE) <= scaleDenominator)
            && ((rule.getMaxScaleDenominator() + TOLERANCE) > scaleDenominator);
    }

}
