package org.geoserver.sldservice.resource;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fao.styling.ColorRamp;
import org.fao.styling.RulesBuilder;
import org.fao.styling.impl.BlueColorRamp;
import org.fao.styling.impl.CustomColorRamp;
import org.fao.styling.impl.GrayColorRamp;
import org.fao.styling.impl.RandomColorRamp;
import org.fao.styling.impl.RedColorRamp;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class UserStyleResource extends BaseResource {
    private String userStyleID;

    private Style style;

    private DataConfig dataConfig;

    public UserStyleResource(Data data, DataConfig dataConfig) {
        super(data);
        this.dataConfig = dataConfig;
    }

    public void handleGet() {
        this.userStyleID = getRequest().getAttributes().get("userStyleID")
                .toString();
        style = this.dt.getStyle(this.userStyleID);
        try {
            getResponse().setEntity(
                    new StringRepresentation(this.fetchUserStyle(),
                            MediaType.TEXT_PLAIN));
        } catch (Exception e) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse().setEntity("Couldn't find requested resource",
                    MediaType.TEXT_PLAIN);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * prepare json string off UserStyle
     * 
     * @return
     */
    private String fetchUserStyle() {
        String userStyleSz;

        userStyleSz = "{'name':'" + style.getName() + "','title':'"
                + style.getTitle() + "','abstract':'" + style.getAbstract()
                + "','featureTypeStyles':[";
        FeatureTypeStyle[] ftStyleA = style.getFeatureTypeStyles();

        String fTypeStyleSz;
        FeatureTypeStyle fTypeStyle = ftStyleA[0];
        fTypeStyleSz = "{'name':'" + fTypeStyle.getName() + "','link':'"
                + getBaseUrl() + "/0','id':'0'}";
        userStyleSz += fTypeStyleSz;

        for (int i = 1; i < ftStyleA.length; i++) {
            fTypeStyle = ftStyleA[i];
            fTypeStyleSz = "{'name':'" + fTypeStyle.getName() + "','link':'"
                    + getBaseUrl() + "/" + i + "','id':,'" + i + "'}";
            userStyleSz += "," + fTypeStyleSz;
        }
        userStyleSz += "]}";
        return userStyleSz;
    }

    public boolean allowPost() {
        return true;
    }
    
    private Color getColor(Form params, String paramName, boolean required) throws ParameterException {
        if (params.getFirst(paramName) == null
                && params.getFirst(paramName).getValue() == null) {
            if(required)
                throw new ParameterException("Required color parameter '" + paramName + "' missing");
            else
                return null;
        }

        try {
            return Color.decode(params.getFirst(paramName).getValue());
        } catch (NumberFormatException e) {
            throw new ParameterException("Invalid color expression for parameter " 
                    + paramName + " (valid ones are expressed as 0xRRGGBB");
        }
    }

    public synchronized void handlePost() {
        Integer classNum = null;
        Color startColor = null;
        Color endColor = null;
        Color midColor = null;
        RulesBuilder ruBuild;
        String userStyleId = null;
        String featureTypeName = null;

        try {

            Map attributes = getRequest().getAttributes();
            if (attributes.containsKey("userStyleID"))
                userStyleId = (String) attributes.get("userStyleID");
            if (attributes.containsKey("featureType"))
                featureTypeName = (String) attributes.get("featureType");

            /*
             * get featureType dosn't manage coverage for at this moment
             */
            FeatureTypeInfo ftInf = null;
            Object obj = findLayer(attributes);
            if (obj == null || !(obj instanceof FeatureTypeInfo)) 
                throw new ParameterException("Can't locate feaureType resource");

            /*
             * is feature Type info as expected
             */
            ftInf = (FeatureTypeInfo) obj;

            /*
             * Check userStyle exist and if so should be in featureType sld list
             */
            final Style confStyle = dt.getStyle(userStyleId);
            if (this.dt.getStyle(userStyleId) == null
                    || (!ftInf.getStyles().contains(confStyle))
                    && !ftInf.getDefaultStyle().equals(confStyle))
                throw new ParameterException(
                        "Can't locate UserStyle resource for this feature type");

            /*
             * Retrive and check mandatory post pram: classMethod property
             */
            Request req = getRequest();
            Form params = req.getEntityAsForm();

            Set<String> validMethods = new HashSet<String>(Arrays.asList(
                    "unique", "equalInterval", "quantile"));
            if (params.getFirst("classMethod") == null)
                throw new ParameterException("Missing classMethod parameter value (possible values: "
                                + validMethods);
            String classMethod = params.getFirst("classMethod").getValue();
            if (!validMethods.contains(classMethod))
                throw new ParameterException("Bad classMethod parameter value " + classMethod
                                + "' (possible values: " + validMethods);

            if (params.getFirst("property") == null
                    || params.getFirst("property").getValue() == null) 
                throw new ParameterException("Required parameter 'property' missing (valid values "
                                + ftInf.getAttributeNames() + ")");
            String property = params.getFirst("property").getValue();
            if (!(ftInf.getAttributeNames().contains(property)))
                throw new ParameterException("Invalid value for arameter 'property' (valid values "
                                + ftInf.getAttributeNames() + ")");

            /*
             * Retriving or setting default value for optional param
             * ClassNum,startColor, endColor, midColor,colorRamp
             */
            if (params.getFirst("classNum") == null
                    || params.getFirst("classNum").getValue() == null) {
                classNum = 4;
            } else
                classNum = Integer.parseInt(params.getFirst("classNum")
                        .getValue());
            classNum = (classNum < 2) ? 4 : classNum;

            /*
             * Looks for color ramp type [red, blue, gray, random , custom]
             */
            Set<String> validRamps = new HashSet<String>(Arrays.asList("red",
                    "blue", "gray", "random", "custom"));
            if (params.getFirst("colorRamp") == null
                    || params.getFirst("colorRamp").getValue() == null) 
                throw new ParameterException("Required parameter 'colorRamp' missing (valid values "
                                + validRamps + ")");
            String colorRamp = params.getFirst("colorRamp").getValue();
            if (!(validRamps.contains(colorRamp)))
                throw new ParameterException("Invalid 'colorRamp' value (valid values "
                        + validRamps + ")");


            /*
             * if is custom looking for custom color
             */
            if (colorRamp.equalsIgnoreCase("custom")) {
                startColor = getColor(params, "startColor", true);
                endColor = getColor(params, "endColor", true);
                midColor = getColor(params, "midColor", false);
            }

            /*
             * Now we can start to create classification
             */
            ruBuild = new RulesBuilder();
            List<Rule> rulesL = null;
            try {
                if (classMethod.equals("quantile"))
                    rulesL = ruBuild.quantileClassification(ftInf
                            .getFeatureSource().getFeatures(), property,
                            classNum);
                else if (classMethod.equals("equalInterval"))
                    rulesL = ruBuild.equalIntervalClassification(ftInf
                            .getFeatureSource().getFeatures(), property,
                            classNum);
                else if (classMethod.equals("unique"))
                    rulesL = ruBuild.uniqueIntervalClassification(ftInf
                            .getFeatureSource().getFeatures(), property);
                Class geomT = ftInf.getFeatureType().getDefaultGeometry()
                        .getBinding();

                /*
                 * Check the number of class if more then 100 refuse to produce
                 * sld
                 * 
                 */
                if (rulesL.size() > 100) {
                    getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
                    getResponse()
                            .setEntity(
                                    "This classification produce more then 100 class, change method or attribute",
                                    MediaType.TEXT_PLAIN);
                    return;
                }

                /*
                 * now we have to create symbolizer choose the correct color
                 * ramp
                 */
                ColorRamp ramp = null;
                if (colorRamp.equalsIgnoreCase("random"))
                    ramp = (ColorRamp) new RandomColorRamp();
                else if (colorRamp.equalsIgnoreCase("red"))
                    ramp = (ColorRamp) new RedColorRamp();
                else if (colorRamp.equalsIgnoreCase("blue"))
                    ramp = (ColorRamp) new BlueColorRamp();
                else if (colorRamp.equalsIgnoreCase("custom")) {
                    if (startColor != null && endColor != null) {
                        CustomColorRamp tramp = new CustomColorRamp();
                        tramp.setStartColor(startColor);
                        tramp.setEndColor(endColor);
                        if (midColor != null)
                            tramp.setMid(midColor);
                        ramp = (ColorRamp) tramp;

                    }
                } else if (ramp == null)
                    ramp = (ColorRamp) new GrayColorRamp();

                /*
                 * Line Symbolizer
                 */

                if (geomT == LineString.class || geomT == MultiLineString.class) {
                    ruBuild.lineStyle(rulesL, ramp);
                }

                /*
                 * Polygon Symbolyzer
                 */
                else if (geomT == MultiPolygon.class || geomT == Polygon.class
                        || geomT == Point.class || geomT == MultiPoint.class) {
                    ruBuild.polygonStyle(rulesL, ramp);
                }
            

                /*
                 * Now we have to put new rules in UserStyle
                 */
    
                if (rulesL != null) {
                    Rule[] rules = new Rule[rulesL.size()];
                    rules = rulesL.toArray(rules);
                    Style style = this.dt.getStyle(userStyleId);
                    style.getFeatureTypeStyles()[0].setRules(rules);
                    
                    // grab the style location
                    StyleConfig styleConfig = dataConfig.getStyle(userStyleId);
                    
                    // save the style on disk
                    SLDTransformer transformer = new SLDTransformer();
                    
                    // check transformation can work fine before writing on disk
                    transformer.setIndentation(2);
                    transformer.transform(style); 
                    transformer.transform(style, new FileOutputStream(styleConfig.getFilename()));
    
                    /*
                     * creation ok
                     */
                    getResponse().setStatus(Status.SUCCESS_CREATED);
                    getResponse().setEntity("Classification succesfully created",
                            MediaType.TEXT_PLAIN);
                } else {
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                    getResponse().setEntity("Unable to complete classfication",
                            MediaType.TEXT_PLAIN);
    
                }
            } catch (Exception e) {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                getResponse().setEntity(e.toString(), MediaType.TEXT_PLAIN);
                return;

            }

        } catch (ParameterException e) {
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            getResponse().setEntity(e.getMessage(), MediaType.TEXT_PLAIN);
        }

    }

}
