/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.featureInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.requests.wfs.FeatureRequest;
import org.vfny.geoserver.requests.wms.GetFeatureInfoRequest;
import org.vfny.geoserver.responses.wfs.GML2FeatureResponseDelegate;
import org.vfny.geoserver.responses.wfs.GetFeatureResults;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A GetFeatureInfo response handler specialized in producing GML data for a
 * GetFeatureInfo request.
 * 
 * @author Gabriel Roldan, Axios Engineering
 */
public class GmlFeatureInfoResponse extends AbstractFeatureInfoResponse {

	/**
	 * The MIME type of the format this response produces
	 */
	private static final String FORMAT = "application/vnd.ogc.gml";

	public GmlFeatureInfoResponse() {
		super.supportedFormats = Collections.singletonList(FORMAT);
	}

	public void writeTo(OutputStream out) throws ServiceException, IOException {
		GetFeatureInfoRequest fInfoReq = (GetFeatureInfoRequest)getRequest();
		FeatureRequest freq = new FeatureRequest();
		freq.setHttpServletRequest(fInfoReq.getHttpServletRequest());
		
		freq.setRequest("GETFEATURE");
		freq.setHandle("GetFeatureInfo");
		freq.setMaxFeatures(fInfoReq.getFeatureCount());
		List queries = null;
		freq.setQueries(queries);
		
		GetFeatureResults getFeatureResults = new GetFeatureResults(freq);
		FeatureTypeInfo finfo;
		FeatureResults fresults;
		int i = 0;
		for(Iterator it = results.iterator(); it.hasNext(); i++){
			fresults = (FeatureResults)it.next();
			finfo = (FeatureTypeInfo)metas.get(i);
			getFeatureResults.addFeatures(finfo, fresults);
		}
		
		GML2FeatureResponseDelegate encoder = new GML2FeatureResponseDelegate();
		encoder.prepare("GML2", getFeatureResults);
		encoder.encode(out);
	}
}