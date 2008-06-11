package org.vfny.geoserver.wms.responses.map.kml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.geoserver.ows.HttpErrorCodeException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Strategy for regionating based on algorithmic stuff related to the actual
 * data. This strategy is fairly ill-defined and should be considered highly
 * experimental.
 * 
 * @author David Winslow
 */
public class DataRegionatingStrategy extends CachedHierarchyRegionatingStrategy {

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");
    private Comparator<SimpleFeature> myComparator;
    private String myAttributeName;

    public DataRegionatingStrategy(){
    }

    protected String findCacheTable(WMSMapContext con, MapLayer layer){
        myAttributeName = (String)con.getRequest().getFormatOptions().get("regionateAttr");
        if (myAttributeName == null)
            myAttributeName = 
                con.getRequest().getWMS().getData().getFeatureTypeInfo(
                        layer.getFeatureSource().getSchema().getName()
                        ).getRegionateAttribute();

        myComparator = new DataComparator();
        return super.findCacheTable(con, layer) + "_" + myAttributeName;
    }

    public Comparator<SimpleFeature> getComparator() {
        return myComparator;
    }

    private class DataComparator implements Comparator<SimpleFeature>{
        public int compare(SimpleFeature a, SimpleFeature b){
            if ((a == null) || (b == null))
                return 0;

            Object attrA = a.getAttribute(myAttributeName);
            Object attrB = b.getAttribute(myAttributeName);

            if ((attrA == null) || (attrB == null))
                return 0;

            if (attrA instanceof Comparable) 
                return ((Comparable)attrA).compareTo(attrB);

            return 0;
        }
    }
}
