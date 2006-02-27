/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.global.FeatureTypeInfo;


/**
 * Represents a WMS 1.1.1 GetFeatureInfo request.
 * <p>
 * The "GetMap" part of the request is represented by a
 * <code>GetMapRequest</code> object by itself. It is
 * intended to provide enough map context information about
 * the map over the GetFeatureInfo request is performed.
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: GetFeatureInfoRequest.java,v 1.1 2004/07/15 21:13:13 jmacgill Exp $
 */
public class GetFeatureInfoRequest extends WMSRequest {

    private static final String DEFAULT_EXCEPTION_FORMAT = "application/vnd.ogc.se_xml";

    private static final int DEFAULT_MAX_FEATURES = 1;

	/**
	 * Holds the GetMap part of the GetFeatureInfo request, wich is meant
	 * to provide enough context information about the map over the 
	 * GetFeatureInfo request is being made.
	 * 
	 * @uml.property name="getMapRequest"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private GetMapRequest getMapRequest;

	/**
	 * List of FeatureTypeInfo's parsed from the <code>QUERY_LAYERS</code>
	 * mandatory parameter.
	 * 
	 * @uml.property name="queryLayers"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	private FeatureTypeInfo[] queryLayers;

	/**
	 * Holder for the <code>INFO_FORMAT</code> optional parameter
	 * 
	 * @uml.property name="infoFormat" multiplicity="(0 1)"
	 */
	private String infoFormat;

	/**
	 * Holder for the <code>FEATURE_COUNT</code> optional parameter.
	 * Deafults to 1.
	 * 
	 * @uml.property name="featureCount" multiplicity="(0 1)"
	 */
	private int featureCount = DEFAULT_MAX_FEATURES;

	/**
	 * Holds the value of the required <code>X</code> parameter
	 * 
	 * @uml.property name="xPixel" multiplicity="(0 1)"
	 */
	private int XPixel;

	/**
	 * Holds the value of the requiered <code>Y</code> parameter
	 * 
	 * @uml.property name="yPixel" multiplicity="(0 1)"
	 */
	private int YPixel;

	/**
	 * Holder for the optional <code>EXCEPTIONS</code> parameter,
	 * defaults to <code>"application/vnd.ogc.se_xml"</code>
	 * 
	 * @uml.property name="exeptionFormat" multiplicity="(0 1)"
	 */
	private String exeptionFormat = DEFAULT_EXCEPTION_FORMAT;


    /**
     * Creates a new GetMapRequest object.
     */
    public GetFeatureInfoRequest() {
        super();
        setRequest("GetFeatureInfo");
    }

	/**
	 * @return Returns the exeptionFormat.
	 * 
	 * @uml.property name="exeptionFormat"
	 */
	public String getExeptionFormat() {
		return exeptionFormat;
	}

	/**
	 * @param exeptionFormat The exeptionFormat to set.
	 * 
	 * @uml.property name="exeptionFormat"
	 */
	public void setExeptionFormat(String exeptionFormat) {
		this.exeptionFormat = exeptionFormat;
	}

	/**
	 * @return Returns the featureCount.
	 * 
	 * @uml.property name="featureCount"
	 */
	public int getFeatureCount() {
		return featureCount;
	}

	/**
	 * @param featureCount The featureCount to set.
	 * 
	 * @uml.property name="featureCount"
	 */
	public void setFeatureCount(int featureCount) {
		this.featureCount = featureCount;
	}

	/**
	 * @return Returns the getMapRequest.
	 * 
	 * @uml.property name="getMapRequest"
	 */
	public GetMapRequest getGetMapRequest() {
		return getMapRequest;
	}

	/**
	 * @param getMapRequest The getMapRequest to set.
	 * 
	 * @uml.property name="getMapRequest"
	 */
	public void setGetMapRequest(GetMapRequest getMapRequest) {
		this.getMapRequest = getMapRequest;
	}

	/**
	 * @return Returns the infoFormat.
	 * 
	 * @uml.property name="infoFormat"
	 */
	public String getInfoFormat() {
		return infoFormat;
	}

	/**
	 * @param infoFormat The infoFormat to set.
	 * 
	 * @uml.property name="infoFormat"
	 */
	public void setInfoFormat(String infoFormat) {
		this.infoFormat = infoFormat;
	}

	/**
	 * @return Returns the queryLayers.
	 * 
	 * @uml.property name="queryLayers"
	 */
	public FeatureTypeInfo[] getQueryLayers() {
		return queryLayers;
	}

	/**
	 * @param queryLayers The queryLayers to set.
	 * 
	 * @uml.property name="queryLayers"
	 */
	public void setQueryLayers(FeatureTypeInfo[] queryLayers) {
		this.queryLayers = queryLayers;
	}

	/**
	 * @return Returns the xPixel.
	 * 
	 * @uml.property name="xPixel"
	 */
	public int getXPixel() {
		return XPixel;
	}

	/**
	 * @param pixel The xPixel to set.
	 * 
	 * @uml.property name="xPixel"
	 */
	public void setXPixel(int pixel) {
		XPixel = pixel;
	}

	/**
	 * @return Returns the yPixel.
	 * 
	 * @uml.property name="yPixel"
	 */
	public int getYPixel() {
		return YPixel;
	}

	/**
	 * @param pixel The yPixel to set.
	 * 
	 * @uml.property name="yPixel"
	 */
	public void setYPixel(int pixel) {
		YPixel = pixel;
	}

}
