package org.geoserver.restconfig.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.restlet.Finder;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

/**
 * Restlet for GeophysicalParamGridCoveragesList resources
 *
 * @author Alessio Fabiani <alessio.fabiani@geo-solutions.it> , GeoSolutions S.a.S.
 */
public class GeophysicalParamsListFinder extends Finder {

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

    public Resource findTarget(Request request, Response response){
        Resource r = new GeophysicParamsList();
        r.init(getContext(), request, response);
        return r;
    }

    protected class GeophysicParamsList extends MapResource{
        private Map myPostFormats;

        public GeophysicParamsList(){
            super();
            myPostFormats = new HashMap();
            myPostFormats.put(MediaType.TEXT_XML, new AutoXMLFormat());
            myPostFormats.put(MediaType.APPLICATION_JSON, new JSONFormat());
            myPostFormats.put(null, myPostFormats.get(MediaType.TEXT_XML));
        }

        public Map getSupportedFormats() {
            Map m = new HashMap();
            m.put("html",
                    new FreemarkerFormat(
                        "HTMLTemplates/geophysicparams.ftl",
                        getClass(),
                        MediaType.TEXT_HTML)
                 );
            m.put("json", new JSONFormat());
            m.put("xml", new AutoXMLFormat("GeophysicParams"));
            m.put(null, m.get("html"));
            return m;
        }

        public Map getMap() {
            Map m = new HashMap();
            List l = new ArrayList();
            Map geophysicParams = getVirtualGeophysicParamsMap(getCatalog());
            
            l.addAll(geophysicParams.keySet());
            Collections.sort(l);
            m.put("GeophysicParams", l);
            
            return m;
        }

        public boolean allowPost(){
            return true;
        }

        // TODO: POST support for folders/ url
        public void handlePost(){
            MediaType type = getRequest().getEntity().getMediaType();
            LOG.info("GeophysicParam posted, mediatype is:" + type);
            DataFormat format = (DataFormat)myPostFormats.get(type);
            LOG.info("Using post format: " + format);
            Map m = (Map)format.readRepresentation(getRequest().getEntity());
            LOG.info("Read data as: " + m);
        }
    }

    public static Map getVirtualGeophysicParamsMap(Catalog catalog){
        Map geophysicParams = new HashMap();
        Iterator it =  catalog.getGeophysicParams().iterator();
        while (it.hasNext()) {
            GeophysicParamInfo entry = (GeophysicParamInfo)it.next();
            geophysicParams.put(entry.getName(), entry);
        }

        return geophysicParams;
    }
}