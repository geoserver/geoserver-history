package org.vfny.geoserver.responses.wms.map;

import org.vfny.geoserver.responses.*;
import java.io.*;
import org.vfny.geoserver.config.*;
import org.geotools.data.*;
import java.util.*;

/**
 * @author Gabriel Roldán
 * @version $Id: JAIMapResponse.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */

public class JAIMapResponse extends GetMapDelegate
{
  public JAIMapResponse()
  {
  }
  /**
   * evaluates if this Map producer can generate the map format specified
   * by <code>mapFormat</code>
   *
   * @param mapFormat the mime type of the output map format requiered
   *
   * @return true if class can produce a map in the passed format
   */
  public boolean canProduce(String mapFormat)
  {
    return false;
  }

  public List getSupportedFormats()
  {
    List l = new ArrayList();

    return l;
  }

  public void writeTo(OutputStream out) throws org.vfny.geoserver.ServiceException, java.io.IOException
  {
    /**@todo Implement this org.vfny.geoserver.responses.Response abstract method*/
  }
  public void abort()
  {
    /**@todo Implement this org.vfny.geoserver.responses.Response abstract method*/
  }
  public String getContentType() throws java.lang.IllegalStateException
  {
    /**@todo Implement this org.vfny.geoserver.responses.Response abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getContentType() not yet implemented.");
  }
  protected void execute(FeatureTypeConfig[] parm1, FeatureResults[] parm2, List parm3) throws org.vfny.geoserver.WmsException
  {
    /**@todo Implement this org.vfny.geoserver.responses.wms.map.GetMapDelegate abstract method*/
  }

}