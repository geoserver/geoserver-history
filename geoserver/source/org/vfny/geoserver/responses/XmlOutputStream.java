/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.responses;

import java.io.*;

import org.apache.log4j.Category;


public class XmlOutputStream extends ByteArrayOutputStream {
		
		/** standard logging instance for class */
		private static Category _log = Category.getInstance( XmlOutputStream.class.getName() );

		/** XML preamble encoding length */
		private static final int ENCODING_LENGTH = 40;


		public XmlOutputStream() {
				super();
		}


		public XmlOutputStream(int length) {
				super(length);
		}
		

		public void writeToClean(OutputStream xmlOut)
				throws WfsException {

				try {
						xmlOut.write( this.toByteArray(), ENCODING_LENGTH , this.size() - ENCODING_LENGTH);
						this.reset();
				}
				catch (IOException e) {
						throw new WfsException( e, "IO problem", XmlOutputStream.class.getName() );
				}
		}


		public void writeToNormal(OutputStream xmlOut)
				throws WfsException {
				
				try {
						xmlOut.write( this.toByteArray(), 0 , this.size());
						this.reset();
				}
				catch (IOException e) {
						throw new WfsException( e, "IO problem", XmlOutputStream.class.getName() );
				}
		}


		public void writeFile(String inputFileName) 
				throws WfsException {

				try {
						File inputFile = new File(inputFileName);
						FileInputStream inputStream = new FileInputStream(inputFile);
						byte[] fileBuffer = new byte[ 20000 ];
						int bytesRead;

						while( (bytesRead = inputStream.read(fileBuffer)) != -1 ) {
								this.write( fileBuffer, 0, bytesRead);
						}

						inputStream.close();
				}
				catch (Exception e) {
						throw new WfsException( e, "Problems writing [ " + inputFileName + " ] file to XML", XmlOutputStream.class.getName() );
				}
		}

}
