package org.vfny.geoserver.xml.utilities;

import java.io.*;
import java.util.*;
import java.sql.*;

public class XmlTag {

		private String name = new String();
		private String schema = new String(); 

		public XmlTag  (String name, String schema) {
				this.name = name;
				this.schema = schema;
    }

		public String start () {
				return "<" + schema + ":" + name + ">\n";
		}

		public String end () {
				return "</" + schema + ":" + name + ">\n";
		}

		public String only () {
				return "<" + schema + ":" + name + "/>\n";
		}


}
