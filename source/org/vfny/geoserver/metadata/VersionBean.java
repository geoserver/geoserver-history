/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.metadata;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.internal.configuration.*;

/**
 * Reads all necessary versioning data and abstracts it away from the response servlets.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class VersionBean {
 		
		private static final String FREEFS_VERSION = "0.9a";
		private static final String WFS_VERSION = "0.0.13";
		private static final String WFS_UPDATE_SEQUENCE = "0";
		private static final String WFS_NAME = "Web Feature Server";

		public VersionBean() {}

		public String getFreeFsVersion() {
				return FREEFS_VERSION;
		}

		public String getWfsVersion() {
				return WFS_VERSION;
		}

		public String getWfsUpdateSequence() {
				return WFS_UPDATE_SEQUENCE;
		}

		public String getWfsName() {
				return WFS_NAME;
		}

}
