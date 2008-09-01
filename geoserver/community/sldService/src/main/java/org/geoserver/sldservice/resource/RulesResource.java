package org.geoserver.sldservice.resource;

import java.io.CharArrayReader;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.beanutils.PropertyUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.SLDParser;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.Data;

/**
 * Represent e Rules resource
 * 
 * @author kappu
 * 
 */
public class RulesResource extends BaseResource {
    private int featureTypeStyleID;

    private Integer lastRuleID = null;

    private Integer firstRuleID = null;

    private String userStyleID = null;

    private Style style = null;

    private FeatureTypeStyle fTStyle = null;

    private Rule[] rules = null;

    public RulesResource(Data data) {
        super(data);
    }

    public boolean allowGet() {
        return true;
    }

    public boolean allowPost() {
        return true;
    }

    private void init() {
        this.userStyleID = getRequest().getAttributes().get("userStyleID").toString();
        this.featureTypeStyleID = Integer.parseInt(getRequest().getAttributes().get(
                "featureTypeID").toString().trim());
        if (getRequest().getAttributes().get("firstRuleID") != null)
            this.firstRuleID = Integer.parseInt(getRequest().getAttributes().get(
                    "firstRuleID").toString().trim());
        if (getRequest().getAttributes().get("lastRuleID") != null)
            this.lastRuleID = Integer.parseInt(getRequest().getAttributes().get(
                    "lastRuleID").toString().trim());

    }

    public void handleGet() {
        init();
        style = this.dt.getStyle(this.userStyleID);
        fTStyle = style.getFeatureTypeStyles()[this.featureTypeStyleID];
        rules = fTStyle.getRules();
        if (style != null & fTStyle != null && rules != null) {
            getResponse().setEntity(
                    new StringRepresentation(this.fetchRules(),
                            MediaType.TEXT_PLAIN));
        } else {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            getResponse().setEntity("Couldn't find requested resource",
                    MediaType.TEXT_PLAIN);
        }
    }

    // retrive xml and try to modify rules!
    public void handlePost() {
        init();

        // per ora manca il parser json lo fai in xml :-)
        String rulesSz = null;
        XMLSerializer xmlS = new XMLSerializer();

        /* recupero le rules dalla form */
        try {
            rulesSz = URLDecoder.decode(getRequest().getEntityAsForm()
                    .getFirstValue("rules"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // rulesSz = "{"+rulesSz+"}";

        /*
         * trasfom in a json string in json array JSONArray jRules =
         * JSONArray.fromObject(rulesSz);
         * 
         * for(int i =0 ; i <jRules.size();i++){
         * this.jsonToRule(jRules.getJSONObject(i)); }
         * 
         * //JSONObject jRules = JSONObject.fromObject( rulesSz);
         * System.out.print(jRules.toString());
         */
        /*
         * parse to xml xmlS.setTypeHintsEnabled(false);
         * xmlS.setElementName(""); String xml = xmlS.write(jRules);
         * System.out.print(xml);
         */

        /*
         * parsing xml do rules[] I need to trnsform in a style adding xml
         */
        String xml = "<UserStyle><FeatureTypeStyle>" + rulesSz
                + "</FeatureTypeStyle></UserStyle>";
        System.out.print(xml);
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(GeoTools
                .getDefaultHints());
        SLDParser stylereader = new SLDParser(factory);
        /*
         * put recived xml into e reader
         */
        CharArrayReader xmlStream = new CharArrayReader(xml.toCharArray());
        stylereader.setInput(xmlStream);
        Style[] styles = stylereader.readXML();
        Rule[] rule = styles[0].getFeatureTypeStyles()[0].getRules();

        // ho recuperato le rules :-)

        DataConfig dataC = this.getDataConfig();
        File file = dataC.getStyle(this.userStyleID).getFilename();
        // devi controllare se il file esiste altrimenti lo crei e lo metti tra
        // gli styles

        SLDTransformer transform = new SLDTransformer();
        transform.setIndentation(2);

        style = this.dt.getStyle(this.userStyleID);
        fTStyle = style.getFeatureTypeStyles()[this.featureTypeStyleID];
        /*
         * insert new rules, if there is a firstruleID you start inserting from
         * that
         */
        if (this.firstRuleID != null) {

            Rule[] oldRules = fTStyle.getRules();
            List<Rule> newRules = new ArrayList();
            // TODO add check if old rule exist
            for (int i = 0; i < this.firstRuleID; i++)
                newRules.add(oldRules[i]);
            for (int i = this.firstRuleID; i < rule.length; i++)
                newRules.add(rule[i]);
            rule = newRules.toArray(rule);
        }
        fTStyle.setRules(rule);
        /*
         * salva le nuove rule nel file
         */
        try {
            String newSld = transform.transform(style);
            // FileWriter fw = new FileWriter(file);
            // fw.write(newSld);
            // fw.flush();
            // fw.close();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // catch (IOException e) {
        // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        /*
         * if (style != null & fTStyle != null && rules != null) { mve
         */
        getResponse().setEntity(
                new StringRepresentation("<result>succes<result>",
                        MediaType.TEXT_PLAIN));
        // }
    }

    private void jsonToRule(JSONObject obj) {

        Object bean = JSONObject.toBean(obj);
        try {
            Object polygonSymb = PropertyUtils.getProperty(bean,
                    "PolygonSymbolyzer");
            Object cssParameter = PropertyUtils.getProperty(polygonSymb,
                    "CssParameter");

            System.out.print(PropertyUtils.getProperty(bean,
                    "PolygonSymbolizer"));
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private String fetchRules() {
        String rulesSz;
        rulesSz = "[";
        if (this.firstRuleID != null && this.lastRuleID != null) {
            rulesSz += this.jsonRule(rules[this.firstRuleID.intValue()]);
            for (int i = 1; i < this.lastRuleID.intValue(); i++) {
                rulesSz += ",";
                rulesSz += this.jsonRule(rules[i]);
            }
            rulesSz += "]";
            return rulesSz;
        } else if (this.firstRuleID != null) {
            rulesSz += this.jsonRule(rules[this.firstRuleID.intValue()]);
            rulesSz += "]";
        } else {
            rulesSz += this.jsonRule(rules[0]);
            for (int i = 1; i < rules.length; i++) {
                rulesSz += ",";
                rulesSz += this.jsonRule(rules[i]);
            }
            rulesSz += "]";
        }
        return rulesSz;
    }
}
