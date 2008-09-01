/**
 * 
 */
package org.geoserver.sldservice.resource;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.xml.transform.TransformerException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.fao.styling.ColorRamp;
import org.fao.styling.RulesBuilder;
import org.fao.styling.impl.BlueColorRamp;
import org.fao.styling.impl.CustomColorRamp;
import org.fao.styling.impl.RandomColorRamp;
import org.fao.styling.impl.RedColorRamp;
import org.geotools.feature.FeatureCollection;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDTransformer;
import org.opengis.feature.type.FeatureType;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.global.Data;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Alessio
 * 
 */
public class ClessifierResource extends Resource {
    final private RulesBuilder builder = new RulesBuilder();

    private Data dt;

    public ClessifierResource(Data dt) {
        this.dt = dt;
    }

    public boolean allowGet() {
        return true;
    }

    public void handleGet() {
        Request req = getRequest();
        Map attributes = req.getAttributes();
        Form parameters = req.getResourceRef().getQueryAsForm();

        List<Rule> rules = this.generateClassifiedSLD(attributes, parameters);
        JSONArray json = null;

        if (rules != null)
            json = this.jsonRulesList(getRequest(), rules);

        if (json != null) {
            String message = json.toString();
            getResponse().setEntity(
                    new StringRepresentation(message, MediaType.TEXT_PLAIN));
        } else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse().setEntity("404 - Couldn't find requested resource",
                    MediaType.TEXT_PLAIN);
        }
    }

    private JSONArray jsonRulesList(Request req, List<Rule> rules) {

        Reference ref = req.getResourceRef();
        JSONArray json = null;

        List out = new ArrayList();
        for (Rule rule : rules) {
            out.add(jsonRule(rule));
        }

        json = JSONArray.fromObject(out);

        return json;
    }

    /**
     * 
     * @param rule
     * @return a string with json Rule representation
     */
    private String jsonRule(Rule rule) {

        String ruleSz = null;
        String xmlRule;
        XMLSerializer xmlS = new XMLSerializer();

        SLDTransformer transform = new SLDTransformer();
        transform.setIndentation(2);
        try {
            xmlRule = transform.transform(rule);
            xmlS.setRemoveNamespacePrefixFromElements(true);
            xmlS.setSkipNamespaces(true);
            JSONObject json = (JSONObject) xmlS.read(xmlRule);
            ruleSz = json.toString();

        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return ruleSz;

    }

    private List<Rule> generateClassifiedSLD(Map attributes, Form form) {
        /* Looks in attribute map if there is the featureType param */
        if (attributes.containsKey("featureType")) {
            final String featureTypeName = (String) attributes.get("featureType");

            final String property = form.getFirstValue("attribute");
            final String method = form.getFirstValue("method", "equalInterval");
            final String intervals = form.getFirstValue("intervals", "2");
            final String open = form.getFirstValue("open", "false");
            final String colorRamp = form.getFirstValue("ramp");

            if (property != null && property.length() > 0) {
                /* First try to find as a FeatureType */
                try {
                    FeatureType ftType = this.dt.getFeatureTypeInfo(
                            featureTypeName).getFeatureType();
                    FeatureCollection ftCollection = this.dt
                            .getFeatureTypeInfo(featureTypeName)
                            .getFeatureSource(true).getFeatures();
                    List<Rule> rules = null;

                    if ("equalInterval".equals(method)) {
                        rules = builder.equalIntervalClassification(
                                ftCollection, property, Integer
                                        .parseInt(intervals), Boolean
                                        .parseBoolean(open));
                    } else if ("uniqueInterval".equals(method)) {
                        rules = builder.uniqueIntervalClassification(
                                ftCollection, property);
                    } else if ("quantile".equals(method)) {
                        rules = builder.quantileClassification(ftCollection,
                                property, Integer.parseInt(intervals), Boolean
                                        .parseBoolean(open));
                    }

                    if (colorRamp != null && colorRamp.length() > 0) {
                        ColorRamp ramp = null;
                        if (colorRamp.equalsIgnoreCase("random"))
                            ramp = (ColorRamp) new RandomColorRamp();
                        else if (colorRamp.equalsIgnoreCase("red"))
                            ramp = (ColorRamp) new RedColorRamp();
                        else if (colorRamp.equalsIgnoreCase("blue"))
                            ramp = (ColorRamp) new BlueColorRamp();
                        else if (colorRamp.equalsIgnoreCase("custom")) {
                            Color startColor = Color.decode(form.getFirst(
                                    "startColor").getValue());
                            Color endColor = Color.decode(form.getFirst(
                                    "endColor").getValue());
                            Color midColor = (form.contains("midColor") ? Color
                                    .decode(form.getFirst("midColor")
                                            .getValue()) : null);
                            if (startColor != null && endColor != null) {
                                CustomColorRamp tramp = new CustomColorRamp();
                                tramp.setStartColor(startColor);
                                tramp.setEndColor(endColor);
                                if (midColor != null)
                                    tramp.setMid(midColor);
                                ramp = (ColorRamp) tramp;
                            }
                        }

                        Class geomT = ftType.getGeometryDescriptor().getType()
                                .getBinding();

                        /*
                         * Line Symbolizer
                         */
                        if (geomT == LineString.class
                                || geomT == MultiLineString.class) {
                            builder.lineStyle(rules, ramp);
                        }

                        /*
                         * Polygon Symbolyzer
                         */
                        else if (geomT == MultiPolygon.class
                                || geomT == Polygon.class
                                || geomT == Point.class
                                || geomT == MultiPoint.class) {
                            builder.polygonStyle(rules, ramp);
                        }
                    }

                    return rules;
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else
                return null;
        } else
            return null;
    }
}
