package org.geoserver.geosearch;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geoserver.rest.DataFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.RestletException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.data.DefaultQuery;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.wms.responses.featureInfo.FeatureTemplate;

public abstract class AbstractFeatureDescription extends GeoServerProxyAwareRestlet {
    private Data myCatalog;

    private final DataFormat format =
        new FreemarkerFormat(
                "featurepage.ftl", 
                HTMLFeatureDescription.class, 
                MediaType.TEXT_HTML
                );

    private String GEOSERVER_BASE_URL;

    public void setCatalog(Data c){
        myCatalog = c;
    }

    public Data getCatalog(){
        return myCatalog;
    }

    public SimpleFeature findFeature(Request req){
        String layer = (String)req.getAttributes().get("layer");
        String namespace = (String)req.getAttributes().get("namespace");
        String feature = (String)req.getAttributes().get("feature");

        NameSpaceInfo ns = myCatalog.getNameSpace(namespace);
        if ( ns == null ) {
            throw new RestletException( 
                    "No such namespace:" + namespace,
                    Status.CLIENT_ERROR_NOT_FOUND 
                    );
        }

        FeatureTypeInfo featureType = null;
        try {
            featureType = myCatalog.getFeatureTypeInfo(layer, ns.getUri());
        } catch (NoSuchElementException e) {
            throw new RestletException(
                    e.getMessage(),
                    Status.CLIENT_ERROR_NOT_FOUND
                    );
        }

        DefaultQuery q = new DefaultQuery();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        q.setFilter(ff.id(Collections.singleton(ff.featureId(feature))));

        FeatureCollection col = null;
        try { 
            col = featureType.getFeatureSource().getFeatures(q);
        } catch (IOException e) {
            throw new RestletException(
                    e.getMessage(),
                    Status.SERVER_ERROR_INTERNAL
                    );
        }

        if (col.size() != 1) {
            throw new RestletException(
                "Unexpected results from data query, "
                + "should be exactly one feature with given ID",
                Status.SERVER_ERROR_INTERNAL
            );
        }

        return (SimpleFeature)col.iterator().next();
    }
}
