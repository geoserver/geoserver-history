/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vnfy.geoserver.config.xml;

/**
 * ConfigException purpose.
 * <p>
 * Description of ConfigException 
 * Represents errors that occur in the load process of a configuration module.
 * <p>
 * Example Use:
 * <pre><code>
 * throw new ConfigException(exception);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ConfigException.java,v 1.1.2.1 2003/12/30 23:39:19 dmzwiers Exp $
 */
public class ConfigException extends Exception {
	private ConfigException(){}
	public ConfigException(String msg){
		super(msg);
	}
	public ConfigException(Throwable e){
		super(e);
	}
   public ConfigException(String msg, Throwable exp) {
	   super(msg, exp);
   }
}