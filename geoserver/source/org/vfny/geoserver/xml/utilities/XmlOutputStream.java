/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.xml.utilities;

import org.apache.log4j.Category;
import java.io.*;

public class XmlOutputStream extends ByteArrayOutputStream {
		
		// create standard logging instance for class
		private static Category _log = Category.getInstance( XmlOutputStream.class.getName() );

		private static final int ENCODING_LENGTH = 40;

		public XmlOutputStream() {
				super();
		}

		public XmlOutputStream(int length) {
				super(length);
		}
		
		public void writeToClean(OutputStream xmlOut) throws IOException {
				xmlOut.write( this.toByteArray(), ENCODING_LENGTH , this.size() - ENCODING_LENGTH);
				this.reset();
		}

		public void writeToNormal(OutputStream xmlOut) throws IOException {
				xmlOut.write( this.toByteArray(), 0 , this.size());
				this.reset();
		}

		public void writeFile(String inputFileName) throws IOException {

				File inputFile = new File(inputFileName);
				FileInputStream inputStream = new FileInputStream(inputFile);
				byte[] fileBuffer = new byte[ 4096 ];
				int fileSize = inputStream.available();
				int bytesRead;

				while( (bytesRead = inputStream.read(fileBuffer)) != -1 )
						this.write( fileBuffer, 0, fileSize);
		}

}
