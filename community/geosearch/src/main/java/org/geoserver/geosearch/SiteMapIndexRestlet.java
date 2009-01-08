package org.geoserver.geosearch;

import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.NameSpaceInfo;

public class SiteMapIndexRestlet extends GeoServerProxyAwareRestlet {

    private Data myData;
    private DataConfig myDataConfig;
    private String GEOSERVER_ROOT;
    private Namespace SITEMAP = Namespace.getNamespace("http://www.sitemaps.org/schemas/sitemap/0.9");

    public Data getData(){
        return myData;
    }

    public DataConfig getDataConfig(){
        return myDataConfig;
    }

    public void setData(Data d){
        myData = d;
    }

    public void setDataConfig(DataConfig dc){
        myDataConfig = dc;
    }

    public void handle(Request request, Response response){
        GEOSERVER_ROOT = getBaseURL(request);

        if (request.getMethod().equals(Method.GET)){
            doGet(request, response);
        } else { 
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    /**
     * Creates a "sitemap.xml" with all the namespaces that contain
     * one or more featuretypes.
     * 
     * @param request
     * @param response
     */
    public void doGet(Request request, Response response){
        Document d = new Document();
        Element sitemapindex = new Element("sitemapindex", SITEMAP);
	//urlset.addNamespaceDeclaration(GEOSITEMAP);
        d.setRootElement(sitemapindex);

        
        NameSpaceInfo[] namespaces = getData().getNameSpaces();
        for (int i = 0; i < namespaces.length; i++){
            for ( Iterator t = namespaces[i].getTypeNames().iterator(); t.hasNext(); ) {
                try {
                    FeatureTypeInfo fti = getData().getFeatureTypeInfo((String)t.next());
                    if ( fti != null && fti.isIndexingEnabled() ) {
                        addSitemap(sitemapindex, GEOSERVER_ROOT + "/layers/" + fti.getName() + "/sitemap.xml");
                    }
                }
                catch( Exception e ) {
                    // Do nothing ?
                }
            }
            
        }

        response.setEntity(new JDOMRepresentation(d));
    }

    private void addSitemap(Element sitemapindex, String url){
        Element sitemapElement = new Element("sitemap", SITEMAP);
        Element loc = new Element("loc", SITEMAP);
        loc.setText(url);
        sitemapElement.addContent(loc);

        sitemapindex.addContent(sitemapElement);
    }

    public static String getParentUrl(String url){
        while (url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }

        int lastSlash = url.lastIndexOf('/');
        if (lastSlash != -1){
            url = url.substring(0, lastSlash);
        }

        return url;
    }
}
