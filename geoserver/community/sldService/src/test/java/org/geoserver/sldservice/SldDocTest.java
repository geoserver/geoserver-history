package org.geoserver.sldservice;

import java.io.File;

import junit.framework.TestCase;

import org.geotools.styling.StyledLayerDescriptor;

public class SldDocTest extends TestCase {

	public void testLoadFile() {
		SldDoc sldDoc= new SldDoc();
		File f = new File("src/test/resources/Lakes.sld");
		try {
			StyledLayerDescriptor s= sldDoc.loadFile(f);
			assertNotNull(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
