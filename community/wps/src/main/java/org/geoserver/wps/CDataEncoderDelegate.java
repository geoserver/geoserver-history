/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import org.geoserver.wps.ppio.CDataPPIO;
import org.geotools.xml.EncoderDelegate;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * Encodes objects as text sections within CDATA markers
 * 
 * @author Andrea Aime - OpenGeo
 */
public class CDataEncoderDelegate implements EncoderDelegate {

	CDataPPIO ppio;
	Object object;

	public CDataEncoderDelegate(CDataPPIO ppio, Object object) {
		this.ppio = ppio;
		this.object = object;
	}

	public void encode(ContentHandler output) throws Exception {
		((LexicalHandler) output).startCDATA();
		char[] chars = ppio.encode(object).toCharArray();
		output.characters(chars, 0, chars.length);
		((LexicalHandler) output).endCDATA();
	}

	public String encode() throws Exception {
		return ppio.encode(object);
	}

}
