/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
/**
 * This package contains an FTP Server implementation that's embedded into GeoServer and that allows
 * authenticated users to upload data files (shapefiles, geotiffs) directly to the GeoServer data
 * directory.
 * <p>
 * <H3>Configuration</H3>
 * So far the only configurable properties are whether to enable the FTP server (defaulting
 * to <b>{@code true}</b>) and on what port to run it (defaults to <b>{@code 8021}</b>).
 * This can be done through an {@code ftp.xml} XML file inside the GeoServer data directory,
 * which is automatically created if it does not already exist, and has the following structure:
 * <pre>
 * <code>
 * &lt;ftp&gt;
 *  &lt;enabled&gt;true&lt;/enabled&gt;
 *  &lt;ftpPort&gt;8021&lt;/ftpPort&gt;
 * &lt;/ftp&gt;
 * </code>
 * </pre>
 * </p>
 * <p>
 * <H3>Usage</H3>
 * This module doesn't impose any action to be automatically taken upon the user uploaded files.
 * Instead, it provides an extension point in the form of a {@link org.geoserver.ftp.FTPCallback
 * callback interface} to notify interested parties of any file related activity happening through
 * the FTP server.
 * </p>
 * <p>
 * To gather the list of extension point implementations, the normal GeoServer extension point
 * mechanism is used, meaning that a Spring bean implementing the
 * {@link org.geoserver.ftp.FTPCallback} interface must be declared in the application context, like
 * in the following XML snippet:
 * 
 * <pre>
 * <code>
 *   &lt;bean id="ftpLogger" class="org.geoserver.ftp.LoggingFTPCallback"/&gt;
 * </code>
 * </pre>
 * 
 * </p>
 * <p>
 * The {@link org.geoserver.ftp.DefaultFTPCallback} class is an empty implementation of this
 * interface serving as a base class where subclasses can override the methods for the events of
 * interest, like in:
 * 
 * <pre>
 * <code>
 * public class LoggingFTPCallback extends DefaultFTPCallback{
 *  {@code @}Override
 *  public CallbackAction onDeleteEnd(UserDetails user, File workingDir, String fileName) {
 *    LOGGER.fine("User " + user.getName() + " just deleted file " + fileName + " in directory " + workingDir.getAbsolutePath());
 *    return CallbackAction.CONTINUE;
 *  }
 * }
 * </code>
 * </pre>
 */
package org.geoserver.ftp;

