/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig.db;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.restlet.data.MediaType;

/**
 * Restlet for GeophysicalParamResource resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class GeophysicalParamResource extends MapResource {
    private GeoServer geoServer;
    private Catalog rawCatalog;

    public void setGeoServer(GeoServer geoServer){
        this.geoServer = geoServer;
    }

    public GeoServer getGeoServer(){
        return this.geoServer;
    }

    public void setCatalog(Catalog catalog){
        this.rawCatalog = catalog;
    }

    public Catalog getCatalog(){
        return this.rawCatalog;
    }
    
    public GeophysicalParamResource(GeoServer geoServer, Catalog catalog){
        super();
        setGeoServer(geoServer);
        setCatalog(catalog);
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/geophysicparam.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("GeophysicalParam"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap(){
        return getMap(findGeophysicalParam());
    }

    public static Map getMap(GeophysicParamInfo geophysicParam) {
        Map m = new HashMap();
        
        m.put("Description", (geophysicParam.getDescription() != null ? geophysicParam.getDescription() : "[None]"));
        m.put("Name", (geophysicParam.getName() != null ? geophysicParam.getName() : "[None]"));
        m.put("Title", (geophysicParam.getTitle() != null ? geophysicParam.getTitle() : "[None]"));
        
//        Unit<?> uom = fieldType.getUnitOfMeasure();
//        if (uom == null)
//            m.put("UnitOfMeasure", "[None]");
//        else if (uom.isCompatible(Unit.ONE)) {
//            m.put("UnitOfMeasure", uom.toString());
//        } else {
//            String uomUCUM = UnitFormat.getUCUMInstance().format(uom);
//            if (uomUCUM != null) {
//                m.put("UnitOfMeasure", uomUCUM);
//            }
//        }

        return m;
    }

    private GeophysicParamInfo findGeophysicalParam() {
        Map attributes = getRequest().getAttributes();
        String variableName = null;

        GeophysicParamInfo variable = null;
        if (attributes.containsKey("parameter")) {
            variableName = (String) attributes.get("parameter");
            
            variable = rawCatalog.getGeophysicParamByName(variableName);
            
        }

        return variable;
    }

    public boolean allowGet() {
        return true;
    }
    
    public boolean allowPut() {
        return false;
    }

    public boolean allowDelete() {
        return false;
    }

}
