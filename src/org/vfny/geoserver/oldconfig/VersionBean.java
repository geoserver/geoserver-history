/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.oldconfig;

/**
 * Reads all necessary versioning data and abstracts it away from the response
 * servlets.
 *
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class VersionBean
{

  /** Version of GeoServer */
  private static final String FREEFS_VERSION = "0.94";

  /** Web Feature Server version */
  private static final String WFS_VERSION = "1.0.0";

  /** Update sequence number */
  private static final String WFS_UPDATE_SEQUENCE = "0";

  /** Web Feature Service Name */
  private static final String WFS_NAME = "Web Feature Server";

  /** Empty constructor */
  public VersionBean()
  {}

  /** Returns GeoServer version */
  public String getFreeFsVersion()
  {
    return FREEFS_VERSION;
  }

  /** Returns Web Feature Service version */
  public String getWfsVersion()
  {
    return WFS_VERSION;
  }

  /** Returns WFS update sequence number */
  public String getWfsUpdateSequence()
  {
    return WFS_UPDATE_SEQUENCE;
  }

  /** Returns Web Feature Server name */
  public String getWfsName()
  {
    return WFS_NAME;
  }

}
