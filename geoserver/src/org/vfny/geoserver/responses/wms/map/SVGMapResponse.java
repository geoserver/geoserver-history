package org.vfny.geoserver.responses.wms.map;

import org.geotools.svg.SVGEncoder;
import org.geotools.data.*;

import org.vfny.geoserver.*;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.responses.Response;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;

import java.io.*;
import java.util.*;

/**
 * Handles a GetMap request that spects a map in SVG format.
 * @author Gabriel Roldán
 * @version $Id: SVGMapResponse.java,v 1.1.2.1 2003/11/14 20:39:15 groldan Exp $
 */

public class SVGMapResponse extends GetMapDelegate
{
  private static final String MIME_TYPE = "image/svg+xml";

  private SVGEncoder svgEncoder;

  private FeatureTypeConfig[] requestedLayers;
  private FeatureResults[] resultLayers;
  private List styles;

  public String getContentType()
  {
    return MIME_TYPE;
  }

  protected void execute(FeatureTypeConfig[] requestedLayers,
                          FeatureResults[] resultLayers,
                          List styles)
      throws WmsException
  {
    GetMapRequest request = getRequest();
    this.requestedLayers = requestedLayers;
    this.resultLayers = resultLayers;
    this.styles = styles;
    this.svgEncoder = new SVGEncoder();
    //fast an easy way of configuring the SVG coordinates traslation
    //I assume that feature results are almost accurate with the bbox requested
    //svgEncoder.setReferenceSpace(getRequest().getBbox());

    svgEncoder.setWidth(String.valueOf(request.getWidth()));
    svgEncoder.setHeight(String.valueOf(request.getHeight()));
  }

  public void writeTo(OutputStream out) throws ServiceException
  {
    FeatureResults layer;

    try {
      Writer writer = new OutputStreamWriter(out);
      svgEncoder.encode(requestedLayers, resultLayers, writer);
    }catch (IOException ex) {
      throw new WmsException(ex, "Error writing SVG", getClass().getName() +
                             "::writeTo()");
    }
  }

}