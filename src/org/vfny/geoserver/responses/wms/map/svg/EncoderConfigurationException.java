package org.vfny.geoserver.responses.wms.map.svg;

import org.vfny.geoserver.WmsException;

/**
 * @author Gabriel Roldán
 * @version $Id: EncoderConfigurationException.java,v 1.1 2004/03/14 16:15:22 groldan Exp $
 */

public class EncoderConfigurationException extends WmsException
{
  public EncoderConfigurationException(String message)
  {
    super(message);
  }

  public EncoderConfigurationException(String message, String locator)
  {
    super(message, locator);
  }

  public EncoderConfigurationException(Throwable e, String message, String locator)
  {
    super(e, message, locator);
  }
}